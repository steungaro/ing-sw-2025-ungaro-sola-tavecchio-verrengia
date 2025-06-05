package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.common.message_protocol.toclient.DrawCardPhaseMessage;
import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.exceptions.*;
import it.polimi.ingsw.gc20.server.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.server.model.player.Player;
import it.polimi.ingsw.gc20.server.model.ship.Ship;
import org.javatuples.Pair;

import java.util.List;

public abstract class PlayingState extends State {
    public PlayingState(GameModel model, GameController controller) {
        super(model, controller);
        currentPlayer = controller.getFirstOnlinePlayer();
    }
    @Override
    public String getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    private String currentPlayer;

    @Override
    public void nextPlayer() {
        currentPlayer = getController().getInGameConnectedPlayers().stream()
                .dropWhile(p -> !p.equals(currentPlayer))
                .skip(1)
                .findFirst().orElse(null);
    }

    @Override
    public int rollDice(Player player) throws InvalidTurnException, InvalidStateException {
        return getModel().getGame().rollDice();
    }

    /**
     * This method is called from both the abandoned ship and abandoned station states
     */
    protected void abandonedEndMove () {
        nextPlayer();
        //check if the next player is null, if it is, the card is over
        if (getCurrentPlayer() == null) {
            //notify all the players that the card has been played and the next card will be drawn
            getController().getMessageManager().broadcastPhase(new DrawCardPhaseMessage());
            phase = StatePhase.DRAW_CARD_PHASE;
            getController().getActiveCard().playCard();
            getController().setState(new PreDrawState(getController()));
        } else {
            //the next player is starting their turn
            phase = StatePhase.ACCEPT_PHASE;
            setStandbyMessage(getCurrentPlayer() + " is choosing to accept or refuse the card.");
            getController().getMessageManager().notifyPhaseChange(phase, this);
        }
    }

    @Override
    public void activateCannons(Player player, List<Pair<Integer, Integer>> cannons, List<Pair<Integer, Integer>> batteries) throws InvalidStateException, InvalidTurnException, InvalidShipException, InvalidCannonException, EnergyException, DieNotRolledException, ComponentNotFoundException {
        getController().getMessageManager().broadcastUpdate(Ship.messageFromShip(player.getUsername(), player.getShip(), "activated cannons"));
    }
}
