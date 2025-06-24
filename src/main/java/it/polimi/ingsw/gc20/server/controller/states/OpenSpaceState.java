package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.common.message_protocol.toclient.*;
import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.controller.managers.Translator;
import it.polimi.ingsw.gc20.server.exceptions.*;
import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.server.model.components.Battery;
import it.polimi.ingsw.gc20.server.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.server.model.player.Player;
import it.polimi.ingsw.gc20.server.model.ship.Ship;
import org.javatuples.Pair;

import java.util.*;

/**
 * Represents the OpenSpaceState in the game, which is a specific playing state where
 * players navigate through open space. This state focuses on player actions related to
 * selecting and activating engines and batteries, transitioning phases, and ending turns.
 * It is dynamically created during the game and integrates with the game model, controller,
 * and adventure card mechanics.
 */
@SuppressWarnings("unused") // dynamically created by Cards
public class OpenSpaceState extends PlayingState {
    private final Map<Player, Integer> declaredEngines = new HashMap<>();

    /**
     * Constructs an OpenSpaceState object. This initializes the state with the provided
     * game model, controller, and adventure card and sets the initial phase to ENGINES_PHASE.
     * It also configures the standby message for the current player and notifies the
     * message manager of the phase change.
     *
     * @param model      the game model that provides the data structure and logic for the game
     * @param controller the game controller that manages game flow and interactions
     * @param card       the adventure card associated with this game state
     */
    public OpenSpaceState(GameModel model, GameController controller, AdventureCard card) {
        super(model, controller);
        phase = StatePhase.ENGINES_PHASE;
        setStandbyMessage(getCurrentPlayer() + " is selecting engines to activate.");
        getController().getMessageManager().notifyPhaseChange(phase, this);
    }

    @Override
    public void activateEngines(Player player, List<Pair<Integer, Integer>> engines, List<Pair<Integer, Integer>> batteries) throws InvalidTurnException, InvalidStateException, EnergyException, InvalidEngineException, ComponentNotFoundException {
        //check if the player is the current player
        if (!getCurrentPlayer().equals(player.getUsername())) {
            throw new InvalidTurnException("It's not your turn!");
        }
        //check if the player is in the engine phase
        if (phase != StatePhase.ENGINES_PHASE) {
            throw new InvalidStateException("You are not in the engines phase.");
        }

        //translate the engines and batteries from the coordinates
        List<Battery> energy = new ArrayList<>();
        if (Translator.getComponentAt(player, batteries, Battery.class) != null)
            energy.addAll(Translator.getComponentAt(player, batteries, Battery.class));
        //save the declared engine power in the map
        declaredEngines.put(player, getModel().enginePower(player, engines.size(), energy));
        getController().getMessageManager().broadcastUpdate(Ship.messageFromShip(player.getUsername(), player.getShip(), "activated engines"));
        //go to the next player
        nextPlayer();
        if (getCurrentPlayer() == null) {
            phase = StatePhase.AUTOMATIC_ACTION;
            getController().getMessageManager().broadcastPhase(new AutomaticActionMessage("All players have selected their engines and batteries, moving players and then drawing a new card."));
            endTurn();
        } else {
            phase = StatePhase.ENGINES_PHASE;
            setStandbyMessage(getCurrentPlayer() + " is selecting engines and batteries.");
            getController().getMessageManager().notifyPhaseChange(phase, this);
        }
    }

    /**
     * Ends card and initiates the transition to the next phase of the game.
     * This method performs the necessary updates to player statuses, broadcasts messages about player
     * positions, and transitions the game phase to the draw card phase. It also interacts with
     * multiple components such as the game model, controller, and message manager to ensure
     * the state of the game is updated appropriately.
     *
     * @throws InvalidStateException if the current phase is not {@code StatePhase.AUTOMATIC_ACTION}.
     */
    private void endTurn() throws InvalidStateException {
        if (phase != StatePhase.AUTOMATIC_ACTION) {
            throw new InvalidStateException("You are not in the automatic action phase.");
        }
        declaredEngines.forEach((key, value) -> {
            getModel().movePlayer(key, value);
            if (value == 0) {
                getController().getPlayerByID(key.getUsername()).setGameStatus(false);
                getController().getMessageManager().sendToPlayer(key.getUsername(), new StandbyMessage("You have no engines left, you are out of the game!"));
            }
        });
        //notify all players that all the player position has been updated

        for (Player player : getModel().getInGamePlayers()) {
            getController().getMessageManager().broadcastUpdate(new PlayerUpdateMessage(player.getUsername(), 0, player.isInGame(), player.getColor(), (player.getPosition() % getModel().getGame().getBoard().getSpaces() + getModel().getGame().getBoard().getSpaces()) % getModel().getGame().getBoard().getSpaces()));
        }
        phase = StatePhase.DRAW_CARD_PHASE;
        getController().getMessageManager().broadcastPhase(new DrawCardPhaseMessage());
        getModel().getActiveCard().playCard();
        getController().setState(new PreDrawState(getController()));
    }

    @Override
    public void currentQuit(Player player) {
        try {
            activateEngines(player, new ArrayList<>(), new ArrayList<>());
        } catch (InvalidTurnException | InvalidStateException | EnergyException | InvalidEngineException |
                 ComponentNotFoundException e) {
            //ignore
        }
    }
    @Override
    public String createsEnginesMessage() {
        return "You are in the open space, select the engines and batteries to use";
    }
}
