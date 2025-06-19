package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.common.message_protocol.toclient.*;
import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.controller.managers.Translator;
import it.polimi.ingsw.gc20.server.exceptions.*;
import it.polimi.ingsw.gc20.server.model.components.AlienColor;
import it.polimi.ingsw.gc20.server.model.components.Cabin;
import it.polimi.ingsw.gc20.server.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.server.model.player.Player;
import it.polimi.ingsw.gc20.server.model.ship.Ship;
import org.javatuples.Pair;

import java.util.HashMap;
import java.util.Map;

public class ValidatingShipState extends State {
    private final Map<Player, Boolean> validShips = new HashMap<>();
    private final Map<Player, Boolean> readyToFly = new HashMap<>();
    private StatePhase phase;
    /**
     * Default constructor
     */
    public ValidatingShipState(GameModel model, GameController controller) {
        super(model, controller);
        getController().getMessageManager().broadcastUpdate(BoardUpdateMessage.fromBoard(getModel().getGame().getBoard(), getModel().getGame().getPlayers(), false));
        for (Player player : model.getInGamePlayers()) {
            validShips.put(player, player.getShip().isValid());
            readyToFly.put(player, model.getLevel() == 0); // if level 0, alien is considered added
            phase = StatePhase.VALIDATE_SHIP_PHASE;
            for (String username : getController().getInGameConnectedPlayers()) {
                try {
                    isShipValid(getController().getPlayerByID(username));
                } catch (InvalidStateException e) {
                    // ignore, this cannot happen here
                }
            }
        }
    }

    @Override
    public String toString() {
        return "ValidatingShipState";
    }

    /**
     * this method is called to validate the ship of the player
     * @param player the player that is validating the ship
     * @return true if the ship is valid, false otherwise
     * @throws InvalidStateException if the game is not in the validate ship phase
     */
    public boolean isShipValid(Player player) throws InvalidStateException{
        if (phase != StatePhase.VALIDATE_SHIP_PHASE) {
            throw new InvalidStateException("Cannot validate ship in this phase.");
        }
        if (getModel().shipValidating(player)) {
            //if the ship is valid, he can add the aliens
            validShips.put(player, true);
            if (getModel().getLevel() == 0) {
                readyToFly.put(player, true);
                if (allShipsReadyToFly()) {
                    initAllShips();
                    for (Player p : getModel().getInGamePlayers()) {
                        getController().getMessageManager().broadcastUpdate(Ship.messageFromShip(p.getUsername(), p.getShip(), "init ship"));
                    }
                    getController().getMessageManager().broadcastPhase(new DrawCardPhaseMessage());
                    phase = StatePhase.DRAW_CARD_PHASE;
                    getController().setState(new PreDrawState(getController()));
                } else {
                    getController().getMessageManager().sendToPlayer(player.getUsername(), new StandbyMessage("ship is valid waiting for other players"));
                }
            } else {
                if (allShipsValidated()) {
                    phase = StatePhase.ADD_ALIEN_PHASE;
                    getController().getMessageManager().broadcastPhase(new AlienPlacementePhaseMessage());
                } else {
                    getController().getMessageManager().sendToPlayer(player.getUsername(), new StandbyMessage("ship is valid waiting for other players"));
                }
            }
            return true;
        } else {
            getController().getMessageManager().sendToPlayer(player.getUsername(), new ValidateShipPhase());
        }
        return false;
    }

    /**
     * this method is called to check if all the ships are valid
     * @return true if all the ships are valid, false otherwise
     * @throws InvalidStateException if the game is not in the validate ship phase
     */
    @Override
    public boolean allShipsValidated() throws InvalidStateException {
        if (phase != StatePhase.VALIDATE_SHIP_PHASE) {
            throw new InvalidStateException("Cannot check if all ships are validated in this phase.");
        }
        //check if all the ships are valid
        return validShips.values().stream().allMatch(Boolean::booleanValue);
    }

    /**
     * this method is called to remove a component from the ship of the player
     * @param player the player that is removing the component
     * @param coordinates the coordinates of the component to remove
     * @throws ComponentNotFoundException if the component is not found
     * @throws InvalidStateException if the game is not in the validate ship phase
     */
    @Override
    public void removeComp(Player player, Pair<Integer, Integer> coordinates) throws ComponentNotFoundException, InvalidStateException, InvalidTileException {
        if (phase!=StatePhase.VALIDATE_SHIP_PHASE) {
            throw new InvalidStateException("Cannot remove a component from a valid ship.");
        } else if (player.getShip().getComponentAt(coordinates.getValue0(), coordinates.getValue1()).getIDComponent() == 1000) {
            throw new InvalidTileException("Cannot remove the starting cabin from the ship now.");
        }
        //remove the component from the ship
        getModel().removeComponent(coordinates.getValue0(), coordinates.getValue1(), player);
        //notify the players of the ship changes
        getController().getMessageManager().broadcastUpdate(Ship.messageFromShip(player.getUsername(), player.getShip(), "removed component"));
        isShipValid(player);
    }

    @Override
    public boolean allShipsReadyToFly() {
        return validShips.values().stream().allMatch(Boolean::booleanValue) && readyToFly.values().stream().allMatch(Boolean::booleanValue);
    }
    /**
     * this method is used to add an alien to the ship of the player
     * @param player the player that is adding the alien
     * @param color the color of the alien
     * @param cabin the cabin where the alien is added
     * @throws InvalidAlienPlacement if the alien cannot be placed in the cabin
     * @throws InvalidStateException if the game is not in the add alien phase
     */
    @Override
    public void addAlien(Player player, AlienColor color, Pair<Integer, Integer> cabin) throws InvalidAlienPlacement, InvalidStateException, ComponentNotFoundException {
        if (phase != StatePhase.ADD_ALIEN_PHASE) {
            throw new InvalidStateException("Cannot add an alien in this phase.");
        }
        //add the alien to the ship
        getModel().setAlien(color, Translator.getComponentAt(player, cabin, Cabin.class), player);
        getController().getMessageManager().broadcastUpdate(Ship.messageFromShip(player.getUsername(), player.getShip(), "added alien"));
        getController().getMessageManager().sendToPlayer(player.getUsername(), new AlienPlacementePhaseMessage());
    }

    /**
     * this method is called if all the ships are ready to fly
     * @throws InvalidStateException if some ships are not ready to fly
     */
    @Override
    public void initAllShips() throws InvalidStateException {
        if (!allShipsReadyToFly()) {
            throw new InvalidStateException("Cannot initialize ships: some ships are not ready to fly.");
        }
        for (Player player : getModel().getInGamePlayers()) {
            getModel().addPieces(player);
        }
    }

    /**
     * this method is called to end the placing alien phase
     * @param player the player that is ending the placing alien phase
     * @throws InvalidStateException if the game is not in the add alien phase
     */
    @Override
    public void endMove (Player player) throws InvalidStateException {
        if (phase != StatePhase.ADD_ALIEN_PHASE) {
            throw new InvalidStateException("Cannot end move in this phase.");
        }
        readyToFly.put(player, true);
        //check if all the players are ready to fly
        if (allShipsReadyToFly()) {
            initAllShips();
            for (Player p : getModel().getInGamePlayers()) {
                getController().getMessageManager().broadcastUpdate(Ship.messageFromShip(p.getUsername(), p.getShip(), "init ship"));
            }
            getController().getMessageManager().broadcastPhase(new DrawCardPhaseMessage());
            phase = StatePhase.DRAW_CARD_PHASE;
            getController().setState(new PreDrawState(getController()));
        } else {
            getController().getMessageManager().sendToPlayer(player.getUsername(), new StandbyMessage("Your ship is already valid, wait for other players to validate their ships."));
        }
    }


    public boolean isConcurrent(){
        return true;
    }

    public void rejoin(String username){
        getController().getMessageManager().sendToPlayer(username, BoardUpdateMessage.fromBoard(getModel().getGame().getBoard(), getModel().getGame().getPlayers(), false));
        if (validShips.get(getController().getPlayerByID(username)) && getModel().getLevel() == 2 && !readyToFly.get(getController().getPlayerByID(username))) {
            getController().getMessageManager().sendToPlayer(username, new AlienPlacementePhaseMessage());
        } else if (!validShips.get(getController().getPlayerByID(username)) && phase == StatePhase.VALIDATE_SHIP_PHASE) {
            getController().getMessageManager().sendToPlayer(username, new ValidateShipPhase());
        } else {
            getController().getMessageManager().sendToPlayer(username, new StandbyMessage("Your ship is already valid, wait for other players to validate their ships."));
        }
    }

    public void resume(String reconnected){
        if (phase == StatePhase.VALIDATE_SHIP_PHASE) {
            for (String username : getController().getInGameConnectedPlayers()) {
                if (username.equals(reconnected)) {
                    rejoin(username);
                } else if (validShips.get(getController().getPlayerByID(username))){
                    getController().getMessageManager().sendToPlayer(username, new StandbyMessage("Your ship is already valid, wait for other players to validate their ships."));
                } else {
                    getController().getMessageManager().sendToPlayer(username, new ValidateShipPhase());
                }
            }
        } else if (phase == StatePhase.ADD_ALIEN_PHASE) {
            for (String username : getController().getInGameConnectedPlayers()) {
                if (username.equals(reconnected)) {
                    rejoin(username);
                } else if (readyToFly.get(getController().getPlayerByID(username))){
                    getController().getMessageManager().sendToPlayer(username, new StandbyMessage("Your ship is ready to fly, wait for other players to terminate their turn."));
                } else {
                    getController().getMessageManager().sendToPlayer(username, new AlienPlacementePhaseMessage());
                }
            }
        }
    }
}
