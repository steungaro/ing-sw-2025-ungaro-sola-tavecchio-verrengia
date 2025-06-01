package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.common.message_protocol.toclient.AutomaticActionMessage;
import it.polimi.ingsw.gc20.common.message_protocol.toclient.StandbyMessage;
import it.polimi.ingsw.gc20.common.message_protocol.toclient.UpdateShipMessage;
import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.server.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.server.model.player.Player;
import it.polimi.ingsw.gc20.server.model.ship.Ship;
import it.polimi.ingsw.gc20.server.network.NetworkService;

@SuppressWarnings("unused") // dynamically created by Cards
public class EpidemicState extends PlayingState {
    /**
     * Default constructor
     */
    public EpidemicState(GameModel model, GameController controller, AdventureCard card) {
        super(model, controller);
        //notify the players that the epidemic is starting, and an automatic action is going to be performed
        for (String player : getController().getInGameConnectedPlayers()) {
            NetworkService.getInstance().sendToClient(player, new AutomaticActionMessage("An epidemic is Starting!, you will lose 1 crew member for each populated adjacent cabins"));
        }
        phase = StatePhase.AUTOMATIC_ACTION;
        automaticAction();
    }

    @Override
    public String toString() {
        return "EpidemicState";
    }


    /**
     * This method is called when the player has to perform an automatic action.
     * In this case, the action is to infect all players in the game and if necessary, to remove crew members
     */
    @Override
    public void automaticAction() {
        //apply the epidemic effect to all players in the game
        getModel().getInGamePlayers().stream()
                .filter(p -> getController().getInGameConnectedPlayers().contains(p.getUsername()))
                .forEach(p -> p.getShip().epidemic());
        //notify all the players with all the ship updates
        for (Player p: getController().getModel().getInGamePlayers()) {
            for (Player username : getController().getPlayers()) {
                NetworkService.getInstance().sendToClient(username.getUsername(), Ship.messageFromShip(p.getUsername(), p.getShip(), "epidemic"));
            }
        }

        //effect ended, draw a new card
        phase = StatePhase.STANDBY_PHASE;
        //notify all the players that the epidemic effect is over, and we wait for a new card
        for (String player : getController().getInGameConnectedPlayers()) {
            NetworkService.getInstance().sendToClient(player, new StandbyMessage("waiting for a new card"));
        }
        getModel().getActiveCard().playCard();
        getController().setState(new PreDrawState(getController()));
    }
}
