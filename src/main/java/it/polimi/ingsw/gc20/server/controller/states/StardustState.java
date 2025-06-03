package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.common.message_protocol.toclient.AutomaticActionMessage;
import it.polimi.ingsw.gc20.common.message_protocol.toclient.DrawCardPhaseMessage;
import it.polimi.ingsw.gc20.common.message_protocol.toclient.PlayerUpdateMessage;
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
     * Executes the automatic action phase for the current state in the game.
     * This method moves players backward based on the number of exposed connectors in their ship,
     * updates player statuses, and advances the game to the draw card phase.
     *
     * The sequence of operations is as follows:
     * - Iterates over the current in-game connected players in reverse order.
     * - Moves each player backward by a value determined by the number of exposed connectors of the player's ship.
     * - Sends updates to all players about the current state and position of each player.
     * - Notifies all in-game connected players that the draw card phase is starting.
     * - Changes the game phase to the draw card phase and plays the active card.
     * - Updates the game state to the pre-draw state.
     */
    @Override
    public void automaticAction() {
        getController().getInGameConnectedPlayers().reversed().stream()
                .map(p ->getController().getPlayerByID(p))
                .forEach(player -> getModel().movePlayer(player, -player.getShip().getAllExposed()));
        //draw a new card
        for (String player : getController().getInGameConnectedPlayers()) {
            for (Player username : getController().getPlayers()){
                NetworkService.getInstance().sendToClient(username.getUsername(), new PlayerUpdateMessage(player, 0, getController().getPlayerByID(player).isInGame(), getController().getPlayerByID(player).getColor(), getController().getPlayerByID(player).getPosition() % getModel().getGame().getBoard().getSpaces()));
            }
        }
        for (String player : getController().getInGameConnectedPlayers()) {
            NetworkService.getInstance().sendToClient(player, new DrawCardPhaseMessage());
        }
        phase = StatePhase.DRAW_CARD_PHASE;
        getController().getActiveCard().playCard();
        getController().setState(new PreDrawState(getController()));
    }
}
