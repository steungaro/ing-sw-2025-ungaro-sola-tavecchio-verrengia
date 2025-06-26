package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.common.message_protocol.toclient.*;
import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.controller.managers.Translator;
import it.polimi.ingsw.gc20.server.exceptions.*;
import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.server.model.cards.Planet;
import it.polimi.ingsw.gc20.server.model.components.CargoHold;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.server.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.server.model.player.Player;
import it.polimi.ingsw.gc20.server.model.ship.Ship;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the state of the game when a Planets card is played.
 * The player can:
 * - land on a planet
 *      + load cargo from the planet to the player's cargo hold
 *      + unload cargo from the player's cargo hold
 *      + move cargo from one cargo hold to another
 *      + end the move
 * - discard the card (end the move)
 * if any player does not accept the card, or if the card has been played, a new card is drawn
 */
@SuppressWarnings("unused") // dynamically created by Cards
public class PlanetsState extends CargoState {
    private final List<Planet> planets;
    private final int lostDays;
    private String landedPlayer;
    private int landedPlanetIndex;
    private final List<Player> playersToMove;
    private List<CargoColor> reward;

    /**
     * Constructs a PlanetsState object. This initializes the state with the provided
     * game model, controller, and adventure card, setting the planets, lost days,
     * and initial phase to LAND_ON_PLANET. It also configures the standby message
     * for the current player and notifies the message manager of the phase change.
     *
     * @param model      the game model that provides the data structure and logic for the game
     * @param controller the game controller that manages game flow and interactions
     * @param card       the adventure card associated with this game state
     */
    public PlanetsState(GameModel model, GameController controller, AdventureCard card) {
        super(model, controller);
        this.planets = card.getPlanets();
        this.lostDays = card.getLostDays();
        this.landedPlayer = null;
        this.landedPlanetIndex = -1;
        this.playersToMove = new ArrayList<>();
        reward = new ArrayList<>();
        phase = StatePhase.LAND_ON_PLANET;
        setStandbyMessage("Waiting for " + getCurrentPlayer() + " to land on a planet.");
        getController().getMessageManager().notifyPhaseChange(phase, this);
    }

    @Override
    public void landOnPlanet(Player player, int planetIndex) throws InvalidTurnException, InvalidStateException {
        if (!getCurrentPlayer().equals(player.getUsername())) {
            throw new InvalidTurnException("It's not your turn!");
        }
        if (phase != StatePhase.LAND_ON_PLANET) {
            throw new InvalidStateException("You can't land on a planet unless you are in the planet phase.");
        }
        if (planets.get(planetIndex).getAvailable()) {
            landedPlayer = player.getUsername();
            landedPlanetIndex = planetIndex;
            playersToMove.add(player);
            phase = StatePhase.ADD_CARGO;
            reward = planets.get(planetIndex).land(player);
            setStandbyMessage("Waiting for " + getCurrentPlayer() + " to load cargo from the planet.");
            getController().getMessageManager().notifyPhaseChange(phase, this);
        } else {
            throw new InvalidStateException("The planet is not available.");
        }
    }

    @Override
    public void loadCargo(Player player, CargoColor loaded, Pair<Integer, Integer> chTo) throws ComponentNotFoundException, InvalidTurnException, CargoException, CargoNotLoadable, CargoFullException, InvalidStateException {
        //check if the player is on the planet
        if (!player.getUsername().equals(landedPlayer)) {
            throw new InvalidTurnException("You can't load cargo unless you are on the planet.");
        }
        //check if the planet is available
        if (phase != StatePhase.ADD_CARGO) {
            throw new InvalidStateException("You can't load cargo unless you are on the planet.");
        }
        if (reward.contains(loaded)) {
            getModel().addCargo(player, loaded, Translator.getComponentAt(player, chTo, CargoHold.class));
            getController().getMessageManager().broadcastUpdate(Ship.messageFromShip(player.getUsername(), player.getShip(), "loaded cargo"));
            reward.remove(loaded);
        } else {
            throw new CargoException("You can't load this cargo, it's not in the reward.");
        }
        phase =  StatePhase.ADD_CARGO;
        getController().getMessageManager().notifyPhaseChange(phase, this);
    }

    @Override
    public void unloadCargo(Player player, CargoColor unloaded, Pair<Integer, Integer> ch) throws ComponentNotFoundException, InvalidTurnException, InvalidCargoException, InvalidStateException {
        if (!player.getUsername().equals(landedPlayer)) {
            throw new InvalidStateException("You can't unload cargo unless you are on the planet.");
        }
        if (phase != StatePhase.ADD_CARGO) {
            throw new InvalidStateException("You can't unload cargo unless you are on the planet.");
        }
        try {
            getModel().moveCargo(player, unloaded, Translator.getComponentAt(player, ch, CargoHold.class), null);
            getController().getMessageManager().broadcastUpdate(Ship.messageFromShip(player.getUsername(), player.getShip(), "unloaded cargo"));
        } catch (CargoNotLoadable | CargoFullException _) {
            //ignore this exception, we are unloading the cargo
        }
        getController().getMessageManager().notifyPhaseChange(phase, this);
    }

    @Override
    public void moveCargo(Player player, CargoColor cargo, Pair<Integer, Integer> from, Pair<Integer, Integer> to) throws ComponentNotFoundException, InvalidTurnException, InvalidStateException, CargoNotLoadable, CargoFullException, InvalidCargoException {
        if (!player.getUsername().equals(landedPlayer)) {
            throw new InvalidTurnException("You can't move cargo unless you are on the planet.");
        }
        if (phase != StatePhase.ADD_CARGO) {
            throw new InvalidStateException("You can't move cargo unless you are on the planet.");
        }
        getModel().moveCargo(player, cargo, Translator.getComponentAt(player, from, CargoHold.class), Translator.getComponentAt(player, to, CargoHold.class));
        getController().getMessageManager().broadcastUpdate(Ship.messageFromShip(player.getUsername(), player.getShip(), "moved cargo"));
        getController().getMessageManager().notifyPhaseChange(phase, this);
    }

    @Override
    public void endMove(Player player) throws InvalidTurnException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn!");
        }
        landedPlayer = null;
        landedPlanetIndex = -1;

        nextPlayer();
        if (getCurrentPlayer() == null) {
            playersToMove.reversed().forEach(p -> getModel().movePlayer(p, -lostDays));
            for (Player p : getModel().getInGamePlayers()) {
                getController().getMessageManager().broadcastUpdate(new PlayerUpdateMessage(p.getUsername(), 0, p.isInGame(), p.getColor(), (p.getPosition() % getModel().getGame().getBoard().getSpaces() + getModel().getGame().getBoard().getSpaces()) % getModel().getGame().getBoard().getSpaces()));
            }
            getController().getMessageManager().broadcastPhase(new DrawCardPhaseMessage() );
            phase = StatePhase.DRAW_CARD_PHASE;
            getController().getActiveCard().playCard();
            getController().setState(new PreDrawState(getController()));
        } else {
            phase = StatePhase.LAND_ON_PLANET;
            setStandbyMessage("Waiting for " + getCurrentPlayer() + " to land on a planet.");
            getController().getMessageManager().notifyPhaseChange(phase, this);
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

    @Override
    public List<Planet> getPlanets() {
        return planets;
    }

    @Override
    public List<CargoColor> cargoReward() {
        return reward;
    }
}
