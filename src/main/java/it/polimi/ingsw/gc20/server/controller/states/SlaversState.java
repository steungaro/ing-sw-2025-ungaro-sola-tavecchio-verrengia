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

@SuppressWarnings("unused") // dynamically created by Cards
public class SlaversState extends PlayingState {
    private final int firePower;
    private final int lostMembers;
    private final int reward;
    private final int lostDays;
    /**
     * Default constructor
     */
    @SuppressWarnings("unused") // dynamically created by Cards
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
    public String toString() {
        return "SlaversState{" +
                "firePower=" + firePower +
                ", lostMembers=" + lostMembers +
                ", reward=" + reward +
                ", lostDays=" + lostDays +
                '}';
    }

    /**
     * this method is called when the player selects cannons and batteries to shoot the enemy
     * @param player the player that is shooting
     * @param cannons the cannons selected by the player
     * @param batteries the batteries selected by the player
     *
     * @throws InvalidStateException if the game is not in the cannon phase
     * @throws InvalidTurnException if it's not the player's turn
     * @throws InvalidCannonException if the cannon is not valid
     * @throws EnergyException if the player doesn't have enough energy
     */
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

    /**
     * this method is called when the player loses against the slavers
     * @param player the player that is losing
     * @param cabins the cabins selected by the player
     * @throws InvalidStateException if the game is not in the lose_crew_phase
     * @throws InvalidTurnException if it's not the player's turn
     * @throws EmptyCabinException if the cabin is empty
     */
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

    /**
     * this method is called when the player accepts the card
     * @param player the player that is accepting the card
     * @throws InvalidStateException if the game is not in the accept phase
     * @throws InvalidTurnException if it's not the player's turn
     */
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
    /**
     * this method is called when the player refuses the card
     * @param player the player that is refusing the card
     * @throws InvalidStateException if the game is not in the accept phase
     * @throws InvalidTurnException if it's not the player's turn
     */
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
