package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.common.message_protocol.toclient.LandOnPlanetPhase;
import it.polimi.ingsw.gc20.common.message_protocol.toclient.PlayerUpdateMessage;
import it.polimi.ingsw.gc20.common.message_protocol.toclient.StandbyMessage;
import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.exceptions.*;
import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.server.model.cards.Planet;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.server.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.server.model.player.Player;
import it.polimi.ingsw.gc20.server.network.NetworkService;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused") // dynamically created by Cards
public class PlanetsState extends CargoState {
    private final List<Planet> planets;
    private final int lostDays;
    private String landedPlayer;
    private int landedPlanetIndex;
    private final List<Player> playersToMove;
    /**
     * Default constructor
     */
    @SuppressWarnings("unused") // dynamically created by Cards
    public PlanetsState(GameModel model, GameController controller, AdventureCard card) {
        super(model, controller);
        this.planets = card.getPlanets();
        this.lostDays = card.getLostDays();
        this.landedPlayer = null;
        this.landedPlanetIndex = -1;
        this.playersToMove = new ArrayList<>();
        phase = StatePhase.LAND_ON_PLANET;
        for (String username : getController().getInGameConnectedPlayers()) {
            if (username.equals(getCurrentPlayer())) {
                NetworkService.getInstance().sendToClient(username, new LandOnPlanetPhase(planets));
            } else {
                NetworkService.getInstance().sendToClient(username, new StandbyMessage("waiting for " + getCurrentPlayer() + " to land on a planet"));
            }
        }
    }

    @Override
    public String toString() {
        return "PlanetsState{" +
                "planets=" + planets +
                ", lostDays=" + lostDays +
                ", landedPlayer='" + landedPlayer + '\'' +
                ", landedPlanetIndex=" + landedPlanetIndex +
                '}';
    }



    /**
     * Accepts a planet card and lands on the planet
     * @param player player that wants to land on a planet
     * @param planetIndex is the index of the planet card in the player's hand
     * @throws InvalidStateException if the game is not in the planet phase, or if the planet is not available
     * @throws InvalidTurnException if it is not the player's turn
     */
    @Override
    public void landOnPlanet(Player player, int planetIndex) throws InvalidTurnException, InvalidStateException {
        if (!getCurrentPlayer().equals(player.getUsername())) {
            throw new InvalidTurnException("It's not your turn");
        }
        if (phase != StatePhase.LAND_ON_PLANET) {
            throw new InvalidStateException("You can't land on a planet unless you are in the planet phase");
        }
        if (planets.get(planetIndex).getAvailable()) {
            planets.get(planetIndex).setAvailable(false);
            landedPlayer = player.getUsername();
            landedPlanetIndex = planetIndex;
            playersToMove.add(player);
            phase = StatePhase.ADD_CARGO;
            //send the message to the player
            for (String username : getController().getInGameConnectedPlayers()) {
                if (username.equals(getCurrentPlayer())) {
                    NetworkService.getInstance().sendToClient(username, new LandOnPlanetPhase(planets));
                } else {
                    NetworkService.getInstance().sendToClient(username, new StandbyMessage("waiting for " + getCurrentPlayer() + " to load cargo"));
                }
            }
        } else {
            throw new InvalidStateException("The planet is not available");
        }
    }

    /**
     * this method is used to load the cargo on the ship
     * @param player the player who is loading the cargo
     * @param loaded the color of the cargo to be loaded
     * @param chTo the cargo hold to which the cargo is loaded
     * @throws InvalidTurnException if it's not the player's turn
     * @throws InvalidStateException if the game is not in the planet phase
     * @throws CargoException if the cargo is incorrect
     * @throws CargoNotLoadable if the cargo is not loadable
     * @throws CargoFullException if the cargo hold is full
     */
    @Override
    public void loadCargo(Player player, CargoColor loaded, Pair<Integer, Integer> chTo) throws InvalidTurnException, CargoException, CargoNotLoadable, CargoFullException, InvalidStateException {
        //check if the player is on the planet
        if (!player.getUsername().equals(landedPlayer)) {
            throw new InvalidTurnException("You can't load cargo unless you are on the planet");
        }
        //check if the planet is available
        if (phase != StatePhase.ADD_CARGO) {
            throw new InvalidStateException("You can't load cargo unless you are on the planet");
        }
        if (planets.get(landedPlanetIndex).getReward().contains(loaded)) {
            planets.get(landedPlanetIndex).getReward().remove(loaded);
            super.loadCargo(player, loaded, chTo);
        } else {
            throw new CargoException("You can't load this cargo, it's not in the reward");
        }
    }

    /**
     * this method is used to unload the cargo from the ship
     * @param player the player who is unloading the cargo
     * @param unloaded the color of the cargo to be unloaded
     * @param ch the cargo hold from which the cargo is unloaded
     * @throws InvalidTurnException if it's not the player's turn
     * @throws InvalidStateException if the game is not in the planet phase
     * @throws InvalidCargoException if the cargo is incorrect
     */
    @Override
    public void unloadCargo(Player player, CargoColor unloaded, Pair<Integer, Integer> ch) throws InvalidTurnException, InvalidCargoException, InvalidStateException {
        if (!player.getUsername().equals(landedPlayer)) {
            throw new InvalidStateException("You can't unload cargo unless you are on the planet");
        }
        if (phase != StatePhase.ADD_CARGO) {
            throw new InvalidStateException("You can't unload cargo unless you are on the planet");
        }
        super.unloadCargo(player, unloaded, ch);
    }

    /**
     * this method is used to move the cargo from one cargo hold to another
     * @param player the player who is moving the cargo
     * @param cargo the color of the cargo to be moved
     * @param from the cargo hold from which the cargo is moved
     * @param to the cargo hold to which the cargo is moved
     * @throws InvalidTurnException if it's not the player's turn
     * @throws InvalidStateException if the game is not in the planet phase
     * @throws CargoNotLoadable if the cargo is not loadable
     * @throws CargoFullException if the cargo hold is full
     */
    @Override
    public void moveCargo(Player player, CargoColor cargo, Pair<Integer, Integer> from, Pair<Integer, Integer> to) throws InvalidTurnException, InvalidStateException, CargoNotLoadable, CargoFullException, InvalidCargoException {
        if (!player.getUsername().equals(landedPlayer)) {
            throw new InvalidTurnException("You can't move cargo unless you are on the planet");
        }
        if (phase != StatePhase.ADD_CARGO) {
            throw new InvalidStateException("You can't move cargo unless you are on the planet");
        }
        super.moveCargo(player, cargo, from, to);
    }

    /**
     * this method is used to end the move of the player
     * @param player the player who is ending the move
     * @throws InvalidTurnException if it's not the player's turn
     */
    @Override
    public void endMove(Player player) throws InvalidTurnException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        landedPlayer = null;
        landedPlanetIndex = -1;

        nextPlayer();
        if (getCurrentPlayer() == null) {
            playersToMove.reversed().forEach(p -> getModel().movePlayer(p, -lostDays));
            for (Player p : getModel().getInGamePlayers()) {
                for (Player username : getController().getPlayers()) {
                    NetworkService.getInstance().sendToClient(username.getUsername(), new PlayerUpdateMessage(p.getUsername(), 0, p.isInGame(), p.getColor(), p.getPosition() % getModel().getGame().getBoard().getSpaces()));
                }
            }
            for (String username : getController().getInGameConnectedPlayers()) {
                NetworkService.getInstance().sendToClient(username, new StandbyMessage("moving player and drawing a new card"));
            }
            phase = StatePhase.STANDBY_PHASE;
            getController().getActiveCard().playCard();
            getController().setState(new PreDrawState(getController()));
        } else {
            for (String username : getController().getInGameConnectedPlayers()) {
                if (username.equals(getCurrentPlayer())) {
                    NetworkService.getInstance().sendToClient(username, new LandOnPlanetPhase(planets));
                } else {
                    NetworkService.getInstance().sendToClient(username, new StandbyMessage("waiting for " + getCurrentPlayer() + " to land on a planet"));
                }
            }
            phase = StatePhase.LAND_ON_PLANET;
        }
    }

    @Override
    public void currentQuit(Player player){
        try {
            endMove(player);
        } catch (InvalidTurnException _) {
            // This exception should never be thrown here, as the player is the current player
        }
    }
}
