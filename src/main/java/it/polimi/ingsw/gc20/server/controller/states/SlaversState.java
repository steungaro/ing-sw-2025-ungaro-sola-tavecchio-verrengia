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
import it.polimi.ingsw.gc20.server.network.NetworkService;
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
        for (String username : getController().getInGameConnectedPlayers()) {
            if (username.equals(getCurrentPlayer())) {
                //send the player the cannon fire
                NetworkService.getInstance().sendToClient(username, new CannonPhaseMessage(createsCannonsMessage()));
            } else {
                //send the player a standby message
                NetworkService.getInstance().sendToClient(username, new StandbyMessage("waiting for " + getCurrentPlayer() + " to shoot the enemy"));
            }
        }
        phase = StatePhase.CANNONS_PHASE;
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
     * @return 1 if the player defeated the card, 0 if the player didn't defeat the card and -1 if the player lost
     * @throws InvalidStateException if the game is not in the cannon phase
     * @throws InvalidTurnException if it's not the player's turn
     * @throws InvalidCannonException if the cannon is not valid
     * @throws EnergyException if the player doesn't have enough energy
     */
    @Override
    public void activateCannons(Player player, List<Pair<Integer, Integer>> cannons, List<Pair<Integer, Integer>> batteries) throws InvalidStateException, InvalidTurnException, InvalidCannonException, EnergyException, ComponentNotFoundException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        if (phase != StatePhase.CANNONS_PHASE) {
            throw new InvalidStateException("You are not in the cannons phase");
        }
        // translate the cannons and batteries to the actual components
        Set<Cannon> cannonsComponents = new HashSet<>();
        if (Translator.getComponentAt(player, cannons, Cannon.class) != null)
            cannonsComponents.addAll(new HashSet<>(Translator.getComponentAt(player, cannons, Cannon.class)));
        List<Battery> batteriesComponents = new ArrayList<>();
        if (Translator.getComponentAt(player, batteries, Battery.class)!=null)
            batteriesComponents.addAll(Translator.getComponentAt(player, batteries, Battery.class));
        //calculate the firepower
        float firePower = getModel().FirePower(player, cannonsComponents, batteriesComponents);
        if (firePower > this.firePower) {
            //won, the player can accept the card
            getController().getActiveCard().playCard();
            for (String username : getController().getInGameConnectedPlayers()) {
                if (username.equals(getCurrentPlayer())) {
                    NetworkService.getInstance().sendToClient(username, new AcceptPhaseMessage("Slavers defeated, do you want to accept the card?"));
                } else {
                    NetworkService.getInstance().sendToClient(username, new StandbyMessage("waiting for " + getCurrentPlayer() + " to accept the card"));
                }
            }
            phase = StatePhase.ACCEPT_PHASE;
        } else if (firePower == this.firePower) {
            //draw, go to the next player
            nextPlayer();
            if (getCurrentPlayer() == null) {
                //draw new card
                for (String username : getController().getInGameConnectedPlayers()) {
                    NetworkService.getInstance().sendToClient(username, new DrawCardPhaseMessage());
                }
                phase = StatePhase.DRAW_CARD_PHASE;
                getController().getActiveCard().playCard();
                getController().setState(new PreDrawState(getController()));
            } else {
                for (String username : getController().getInGameConnectedPlayers()) {
                    if (username.equals(getCurrentPlayer())) {
                        //send the player the cannon fire
                        NetworkService.getInstance().sendToClient(username, new CannonPhaseMessage(createsCannonsMessage()));
                    } else {
                        //send the player a standby message
                        NetworkService.getInstance().sendToClient(username, new StandbyMessage("waiting for " + getCurrentPlayer() + " to shoot the enemy"));
                    }
                }
            }
        } else {
            //lost, the player has to lose crew
            for (String username : getController().getInGameConnectedPlayers()) {
                if (username.equals(getCurrentPlayer())) {
                    NetworkService.getInstance().sendToClient(username, new LoseCrewMessage(lostMembers));
                } else {
                    NetworkService.getInstance().sendToClient(username, new StandbyMessage("waiting for " + getCurrentPlayer() + " to lose crew"));
                }
            }
            phase = StatePhase.LOSE_CREW_PHASE;
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
            throw new InvalidTurnException("It's not your turn");
        }
        if (phase != StatePhase.LOSE_CREW_PHASE) {
            throw new InvalidStateException("You don't have to lose crew");
        }
        int actualLostMembers;
        //check if the player has enough crew to lose
        if (player.getShip().crew() < lostMembers) {
            actualLostMembers = player.getShip().crew();
        } else
            actualLostMembers = lostMembers;

        if (cabins.size() < actualLostMembers) {
            throw new InvalidStateException("not enough cabins selected");
        }

        getModel().loseCrew(player, Translator.getComponentAt(player, cabins, Cabin.class));
        //next player
        nextPlayer();
        if (getCurrentPlayer() == null) {
            //draw new card
            for (String username : getController().getInGameConnectedPlayers()) {
                NetworkService.getInstance().sendToClient(username, new DrawCardPhaseMessage());
            }
            phase = StatePhase.DRAW_CARD_PHASE;
            getController().getActiveCard().playCard();
            getController().setState(new PreDrawState(getController()));
        } else {
            //the next player has to fight
            for (String username : getController().getInGameConnectedPlayers()) {
                if (username.equals(getCurrentPlayer())) {
                    //send the player the cannon fire
                    NetworkService.getInstance().sendToClient(username, new CannonPhaseMessage(createsCannonsMessage()));
                } else {
                    //send the player a standby message
                    NetworkService.getInstance().sendToClient(username, new StandbyMessage("waiting for " + getCurrentPlayer() + " to shoot the enemy"));
                }
            }
            phase = StatePhase.CANNONS_PHASE;
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
            throw new InvalidTurnException("It's not your turn");
        }
        if (phase != StatePhase.ACCEPT_PHASE) {
            throw new InvalidStateException("Card not defeated");
        }
        getModel().movePlayer(player, -lostDays);
        getModel().addCredits(player, reward);
        for (Player username : getController().getPlayers()){
            NetworkService.getInstance().sendToClient(username.getUsername(), new PlayerUpdateMessage(player.getUsername(), reward, player.isInGame(), player.getColor(), player.getPosition()%getModel().getGame().getBoard().getSpaces()));
        }
        for (String username : getController().getInGameConnectedPlayers()) {
            NetworkService.getInstance().sendToClient(username, new DrawCardPhaseMessage());
        }
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
            throw new InvalidTurnException("It's not your turn");
        }
        if (phase != StatePhase.ACCEPT_PHASE) {
            throw new InvalidStateException("Card not defeated");
        }
        for (String username : getController().getInGameConnectedPlayers()) {
            NetworkService.getInstance().sendToClient(username, new DrawCardPhaseMessage());
        }
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
                for (String username : getController().getInGameConnectedPlayers()) {
                    NetworkService.getInstance().sendToClient(username, new DrawCardPhaseMessage());
                }
                phase = StatePhase.DRAW_CARD_PHASE;
                getModel().getActiveCard().playCard();
                getController().setState(new PreDrawState(getController()));
            } else {
                //the next player has to fight
                for (String username : getController().getInGameConnectedPlayers()) {
                    if (username.equals(getCurrentPlayer())) {
                        //send the player the cannon fire
                        NetworkService.getInstance().sendToClient(username, new CannonPhaseMessage(createsCannonsMessage()));
                    } else {
                        //send the player a standby message
                        NetworkService.getInstance().sendToClient(username, new StandbyMessage("waiting for " + getCurrentPlayer() + " to shoot the enemy"));
                    }
                }
                phase = StatePhase.CANNONS_PHASE;
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
        return "You are fighting slavers, enemy firepower is " + firePower + ", select the cannons and batteries to use";
    }

    @Override
    public int getCrew(){
        return lostMembers;
    }

}
