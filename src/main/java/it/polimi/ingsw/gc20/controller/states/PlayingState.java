package it.polimi.ingsw.gc20.controller.states;

import it.polimi.ingsw.gc20.controller.GameController;
import it.polimi.ingsw.gc20.exceptions.*;
import it.polimi.ingsw.gc20.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.model.player.Player;

public abstract class PlayingState extends State {
    public PlayingState(GameModel model, GameController controller) {
        super(model, controller);
        currentPlayer = controller.getFirstOnlinePlayer();
    }

    public PlayingState(GameController controller) {
        super(controller);
    }

    public PlayingState(GameModel model) {
        super(model);
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    private String currentPlayer;

    public void nextPlayer() {
        currentPlayer = getController().getInGameConnectedPlayers().stream()
                .dropWhile(p -> !p.equals(currentPlayer))
                .skip(1)
                .findFirst().orElse(null);
    }

    public int rollDice(Player player) throws InvalidTurnException, InvalidShipException, EmptyDeckException, DeadAlienException, DieNotRolledException {
        if (!currentPlayer.equals(player.getUsername())) {
            throw new InvalidTurnException("Not your turn.");
        }
        return getModel().getGame().rollDice();
    }
}
