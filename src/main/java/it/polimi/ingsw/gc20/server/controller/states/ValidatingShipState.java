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

/**
 * This class represents the state of the game when players are validating their ships.
 * Players can validate their ships, add aliens, and remove components from their ships.
 * If all ships are valid, the game transitions to the next phase.
 */
public class ValidatingShipState extends State {
    private final Map<Player, Boolean> validShips = new HashMap<>();
    private final Map<Player, Boolean> readyToFly = new HashMap<>();
    private StatePhase phase;

    /**
     * Constructs a ValidatingShipState object. This initializes the state with the provided
     * game model and controller, setting the initial phase to VALIDATE_SHIP_PHASE.
     * It also broadcasts the current board state to all players and checks if each player's ship is valid.
     *
     * @param model      the game model that provides the data structure and logic for the game
     * @param controller the game controller that manages game flow and interactions
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
    public void isShipValid(Player player) throws InvalidStateException{
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
        } else {
            getController().getMessageManager().sendToPlayer(player.getUsername(), new ValidateShipPhase());
        }
    }

    @Override
    public boolean allShipsValidated() throws InvalidStateException {
        if (phase != StatePhase.VALIDATE_SHIP_PHASE) {
            throw new InvalidStateException("Cannot check if all ships are validated in this phase.");
        }
        //check if all the ships are valid
        return validShips.values().stream().allMatch(Boolean::booleanValue);
    }

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

    @Override
    public void initAllShips() throws InvalidStateException {
        if (!allShipsReadyToFly()) {
            throw new InvalidStateException("Cannot initialize ships: some ships are not ready to fly.");
        }
        for (Player player : getModel().getInGamePlayers()) {
            getModel().addPieces(player);
        }
    }

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

    @Override
    public boolean isConcurrent(){
        return true;
    }

    @Override
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

    @Override
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
