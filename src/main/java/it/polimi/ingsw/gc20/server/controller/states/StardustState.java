package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.common.message_protocol.toclient.AutomaticActionMessage;
import it.polimi.ingsw.gc20.common.message_protocol.toclient.PlayerUpdateMessage;
import it.polimi.ingsw.gc20.common.message_protocol.toclient.StandbyMessage;
import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.server.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.server.model.player.Player;
import it.polimi.ingsw.gc20.server.network.NetworkService;


public class StardustState extends PlayingState {

    /**
     * Default constructor
     */
    public StardustState(GameModel model, GameController controller, AdventureCard card) {
        super(model, controller);
        for (String username : getController().getInGameConnectedPlayers()) {
            NetworkService.getInstance().sendToClient(username, new AutomaticActionMessage(card.getName() + " is played"));
        }
        phase = StatePhase.AUTOMATIC_ACTION;
        automaticAction();
    }

    @Override
    public String toString() {
        return "StardustState";
    }

    /**
     * this method is called when the stardust card is drawn and the automatic action is performed
     */
    @Override
    public void automaticAction() {
        getController().getInGameConnectedPlayers().stream()
                .map(p ->getController().getPlayerByID(p))
                .forEach(player -> getModel().movePlayer(player, -player.getShip().getAllExposed()));
        //draw a new card
        for (String player : getController().getInGameConnectedPlayers()) {
            for (Player username : getController().getPlayers()){
                NetworkService.getInstance().sendToClient(username.getUsername(), new PlayerUpdateMessage(player, 0, getController().getPlayerByID(player).isInGame(), getController().getPlayerByID(player).getColor(), getController().getPlayerByID(player).getPosition() % getModel().getGame().getBoard().getSpaces()));
            }
        }
        for (String player : getController().getInGameConnectedPlayers()) {
            NetworkService.getInstance().sendToClient(player, new StandbyMessage("draw a new card"));
        }
        phase = StatePhase.STANDBY_PHASE;
        getController().getActiveCard().playCard();
        //getController().setState(new PreDrawState(getController()));
    }
}
