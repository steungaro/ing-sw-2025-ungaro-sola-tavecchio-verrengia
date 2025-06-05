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

@SuppressWarnings("unused") // dynamically created by Cards
public class OpenSpaceState extends PlayingState {
    private final Map<Player, Integer> declaredEngines = new HashMap<>();

    /**
     * Default constructor
     */
    public OpenSpaceState(GameModel model, GameController controller, AdventureCard card) {
        super(model, controller);
        phase = StatePhase.ENGINES_PHASE;
        setStandbyMessage(getCurrentPlayer() + " is selecting engines and batteries");
        getController().getMessageManager().notifyPhaseChange(phase, this);
    }

    @Override
    public String toString() {
        return "OpenSpaceState";
    }

    /**
     * this method is called when the player has selected the engines and batteries to use
     * @param player player who is selecting the engines
     * @param engines list of engines selected by the player
     * @param batteries list of batteries selected by the player
     * @throws InvalidStateException if the player is not in the engine phase
     * @throws InvalidTurnException if the player is not the current player
     * @throws InvalidEngineException if the player has selected an invalid engine
     * @throws EnergyException if the player has not enough energy to activate the engines
     */
    @Override
    public void activateEngines(Player player, List<Pair<Integer, Integer>> engines, List<Pair<Integer, Integer>> batteries) throws InvalidTurnException, InvalidStateException, EnergyException, InvalidEngineException, ComponentNotFoundException {
        //check if the player is the current player
        if (!getCurrentPlayer().equals(player.getUsername())) {
            throw new InvalidTurnException("It's not your turn");
        }
        //check if the player is in the engine phase
        if (phase != StatePhase.ENGINES_PHASE) {
            throw new InvalidStateException("You are not in the engines phase");
        }

        //translate the engines and batteries from the coordinates
        List<Battery> energy = new ArrayList<>();
        if (Translator.getComponentAt(player, batteries, Battery.class) != null)
            energy.addAll(Translator.getComponentAt(player, batteries, Battery.class));
        //save the declared engine power in the map
        declaredEngines.put(player, getModel().EnginePower(player, engines.size(), energy));
        getController().getMessageManager().broadcastUpdate(Ship.messageFromShip(player.getUsername(), player.getShip(), "activated engines"));
        //go to the next player
        nextPlayer();
        if (getCurrentPlayer() == null) {
            phase = StatePhase.AUTOMATIC_ACTION;
            getController().getMessageManager().broadcastPhase(new AutomaticActionMessage("All players have selected their engines and batteries, moving players and drawing a new card"));
            endTurn();
        } else {
            phase = StatePhase.ENGINES_PHASE;
            setStandbyMessage(getCurrentPlayer() + " is selecting engines and batteries");
            getController().getMessageManager().notifyPhaseChange(phase, this);
        }
    }

    /**
     * this method is called when all the players have selected their engines and batteries
     * @throws InvalidStateException if the player is not in the automatic action phase
     */
    @Override
    public void endTurn() throws InvalidStateException {
        if (phase != StatePhase.AUTOMATIC_ACTION) {
            throw new InvalidStateException("You are not in the automatic action phase");
        }
        declaredEngines.forEach((key, value) -> {
            getModel().movePlayer(key, value);
            if (value == 0) {
                getController().getPlayerByID(key.getUsername()).setGameStatus(false);
                getController().getMessageManager().sendToPlayer(key.getUsername(), new StandbyMessage("You have no engines left, you are out of the game"));
            }
        });
        //notify all players that all the player position has been updated

        for (Player player : getModel().getInGamePlayers()) {
            getController().getMessageManager().broadcastUpdate(new PlayerUpdateMessage(player.getUsername(), 0, player.isInGame(), player.getColor(), player.getPosition() % getModel().getGame().getBoard().getSpaces()));
        }
        phase = StatePhase.DRAW_CARD_PHASE;
        getController().getMessageManager().broadcastPhase(new DrawCardPhaseMessage());
        getModel().getActiveCard().playCard();
        getController().setState(new PreDrawState(getController()));
    }

    /** this method is called when a player quit
     *
     * @param player the player who quit
     */
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
