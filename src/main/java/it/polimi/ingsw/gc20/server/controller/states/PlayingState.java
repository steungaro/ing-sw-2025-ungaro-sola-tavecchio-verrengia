package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.exceptions.DieNotRolledException;
import it.polimi.ingsw.gc20.server.exceptions.InvalidShipException;
import it.polimi.ingsw.gc20.server.exceptions.InvalidTurnException;
import it.polimi.ingsw.gc20.server.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.server.model.player.Player;

public abstract class PlayingState extends State {
    public PlayingState(GameModel model, GameController controller) {
        super(model, controller);
        currentPlayer = controller.getFirstOnlinePlayer();
    }

    /*public PlayingState(GameController controller) {
        super(controller);
    }

    public PlayingState(GameModel model) {
        super(model);
    }*/

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
    public int rollDice(Player player) throws InvalidTurnException, InvalidShipException, DieNotRolledException {
        if (!currentPlayer.equals(player.getUsername())) {
            throw new InvalidTurnException("Not your turn.");
        }
        return getModel().getGame().rollDice();
    }

}
