package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.common.message_protocol.toclient.*;
import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.controller.managers.Translator;
import it.polimi.ingsw.gc20.server.exceptions.*;
import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.server.model.components.Battery;
import it.polimi.ingsw.gc20.server.model.components.Cabin;
import it.polimi.ingsw.gc20.server.model.components.Cannon;
import it.polimi.ingsw.gc20.server.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.server.model.player.Player;
import it.polimi.ingsw.gc20.server.model.ship.Ship;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents the SlaversState in the game, which is a specific playing state where players
 * must fight against slavers using their cannons and batteries. This state handles the
 * mechanics of selecting cannons, losing crew, accepting or refusing the card, and managing
 * player turns.
 */
@SuppressWarnings("unused") // dynamically created by Cards
public class SlaversState extends PlayingState {
    private final int firePower;
    private final int lostMembers;
    private final int reward;
    private final int lostDays;

    /**
     * Constructs a SlaversState object. This initializes the state with the provided game model,
     * controller, and adventure card, setting the firepower, lost crew members, reward credits,
     * and lost days. It also sets the initial phase to CANNONS_PHASE and configures the standby
     * message for the current player, notifying the message manager of the phase change.
     *
     * @param model      the game model that provides the data structure and logic for the game
     * @param controller the game controller that manages game flow and interactions
     * @param card       the adventure card associated with this game state
     */
    public SlaversState(GameModel model, GameController controller, AdventureCard card) {
        super(model, controller);
        this.firePower = card.getFirePower();
        this.lostMembers = card.getCrew();
        this.reward = card.getCredits();
        this.lostDays = card.getLostDays();
        phase = StatePhase.CANNONS_PHASE;
        setStandbyMessage(getCurrentPlayer() + " is choosing cannons to fight the slavers.");
        getController().getMessageManager().notifyPhaseChange(phase, this);
    }

    @Override
    public void activateCannons(Player player, List<Pair<Integer, Integer>> cannons, List<Pair<Integer, Integer>> batteries) throws InvalidStateException, InvalidTurnException, InvalidCannonException, EnergyException, ComponentNotFoundException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn!");
        }
        if (phase != StatePhase.CANNONS_PHASE) {
            throw new InvalidStateException("You are not in the cannons phase.");
        }
        // translate the cannons and batteries to the actual components
        Set<Cannon> cannonsComponents = new HashSet<>();
        if (Translator.getComponentAt(player, cannons, Cannon.class) != null)
            cannonsComponents.addAll(new HashSet<>(Translator.getComponentAt(player, cannons, Cannon.class)));
        List<Battery> batteriesComponents = new ArrayList<>();
        if (Translator.getComponentAt(player, batteries, Battery.class)!=null)
            batteriesComponents.addAll(Translator.getComponentAt(player, batteries, Battery.class));
        //calculate the firepower
        float firePower = getModel().firePower(player, cannonsComponents, batteriesComponents);
        getController().getMessageManager().broadcastUpdate(Ship.messageFromShip(player.getUsername(), player.getShip(), "activated cannons"));
        if (firePower > this.firePower) {
            //won, the player can accept the card
            getController().getActiveCard().playCard();
            phase = StatePhase.ACCEPT_PHASE;
            setStandbyMessage("Waiting for " + getCurrentPlayer() + " to accept or refuse the card.");
            getController().getMessageManager().notifyPhaseChange(phase, this);
        } else if (firePower == this.firePower) {
            //draw, go to the next player
            nextPlayer();
            if (getCurrentPlayer() == null) {
                //draw new card
                getController().getMessageManager().broadcastPhase(new DrawCardPhaseMessage());
                phase = StatePhase.DRAW_CARD_PHASE;
                getController().getActiveCard().playCard();
                getController().setState(new PreDrawState(getController()));
            } else {
                phase = StatePhase.CANNONS_PHASE;
                setStandbyMessage("Waiting for " + getCurrentPlayer() + " to shoot at the enemy.");
                getController().getMessageManager().notifyPhaseChange(phase, this);
            }
        } else {
            phase = StatePhase.LOSE_CREW_PHASE;
            setStandbyMessage("Waiting for " + getCurrentPlayer() + " to choose the cabins to lose crew from.");
            getController().getMessageManager().notifyPhaseChange(phase, this);
        }
    }

    @Override
    public void loseCrew(Player player, List<Pair<Integer, Integer>> cabins) throws InvalidStateException, InvalidTurnException, EmptyCabinException, ComponentNotFoundException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn!");
        }
        if (phase != StatePhase.LOSE_CREW_PHASE) {
            throw new InvalidStateException("You don't have to lose crew.");
        }
        int actualLostMembers = Math.min(player.getShip().crew(), lostMembers);
        //check if the player has enough crew to lose

        if (cabins.size() < actualLostMembers) {
            throw new InvalidStateException("Not enough cabins selected. You need to select at least " + actualLostMembers + " cabins.");
        }

        getModel().loseCrew(player, Translator.getComponentAt(player, cabins, Cabin.class));
        getController().getMessageManager().broadcastUpdate(Ship.messageFromShip(player.getUsername(), player.getShip(), "lost crew"));
        //next player
        nextPlayer();
        if (getCurrentPlayer() == null) {
            //draw new card
            getController().getMessageManager().broadcastPhase(new DrawCardPhaseMessage());
            phase = StatePhase.DRAW_CARD_PHASE;
            getController().getActiveCard().playCard();
            getController().setState(new PreDrawState(getController()));
        } else {
            phase = StatePhase.CANNONS_PHASE;
            setStandbyMessage("Waiting for " + getCurrentPlayer() + " to shoot at the enemy.");
            getController().getMessageManager().notifyPhaseChange(phase, this);
        }
    }

    @Override
    public void acceptCard(Player player) throws InvalidStateException, InvalidTurnException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn!");
        }
        if (phase != StatePhase.ACCEPT_PHASE) {
            throw new InvalidStateException("Card not defeated yet.");
        }
        getModel().movePlayer(player, -lostDays);
        getModel().addCredits(player, reward);
        getController().getMessageManager().broadcastUpdate(new PlayerUpdateMessage(player.getUsername(), reward, player.isInGame(), player.getColor(), (player.getPosition() % getModel().getGame().getBoard().getSpaces() + getModel().getGame().getBoard().getSpaces()) % getModel().getGame().getBoard().getSpaces()));
        getController().getMessageManager().broadcastPhase(new DrawCardPhaseMessage());
        phase = StatePhase.DRAW_CARD_PHASE;
        getController().getActiveCard().playCard();
        getController().setState(new PreDrawState(getController()));
    }

    @Override
    public void endMove(Player player) throws InvalidStateException, InvalidTurnException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn!");
        }
        if (phase != StatePhase.ACCEPT_PHASE) {
            throw new InvalidStateException("Card not defeated yet.");
        }
        getController().getMessageManager().broadcastPhase(new DrawCardPhaseMessage());
        phase = StatePhase.DRAW_CARD_PHASE;
        getController().getActiveCard().playCard();
        getController().setState(new PreDrawState(getController()));
    }

    @Override
    public void currentQuit(Player player){
        //if the player is in the cannon phase, if he quit, we go to the next player
        if (phase == StatePhase.CANNONS_PHASE || phase == StatePhase.LOSE_CREW_PHASE) {
            nextPlayer();
            if (getCurrentPlayer() == null) {
                //draw new card
                getController().getMessageManager().broadcastPhase(new DrawCardPhaseMessage());
                phase = StatePhase.DRAW_CARD_PHASE;
                getModel().getActiveCard().playCard();
                getController().setState(new PreDrawState(getController()));
            } else {
                phase = StatePhase.CANNONS_PHASE;
                setStandbyMessage("Waiting for " + getCurrentPlayer() + " to shoot at the enemy.");
                getController().getMessageManager().notifyPhaseChange(phase, this);
            }
        } else if (phase == StatePhase.ACCEPT_PHASE){
            try {
                endMove(player);
            } catch (InvalidStateException | InvalidTurnException e) {
                //ignore
            }
        }
    }

    @Override
    public String createsCannonsMessage(){
        return "You are fighting slavers, enemy firepower is " + firePower + ", select the cannons to activate to fight back.";
    }

    @Override
    public int getCrew(){
        return lostMembers;
    }

}
