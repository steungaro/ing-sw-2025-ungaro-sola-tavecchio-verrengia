package it.polimi.ingsw.gc20.model.gamesets;

import it.polimi.ingsw.gc20.model.player.*;
import it.polimi.ingsw.gc20.model.cards.*;
import it.polimi.ingsw.gc20.model.components.*;
import it.polimi.ingsw.gc20.model.bank.*;
import it.polimi.ingsw.gc20.model.ship.*;

public class GameModel {
    private Game game;
    private AdventureCard activeCard;

    /**
     * Default Constructor
     */
    public void GameModel (){
        this.game = null;
        this.activeCard = null;
    }

    /** set function for the game
     * @param game
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /** get function for the game
     * @return game
     */
    public Game getGame() {
        return this.game;
    }

    /** set function for the active card
     * @param activeCard
     */
    public void setActiveCard(AdventureCard activeCard) {
        this.activeCard = activeCard;
    }

    /** get function for the active card
     * @return activeCard
     */
    public AdventureCard getActiveCard() {
        return this.activeCard;
    }

    public void startGame(int numberOfPlayers, int livello, String username) {
        Game game = new Game();
        Pile pile = new Pile();
        Board board;
        if (livello==2) {
            board = new NormalBoard();
            //setto i deck
        } else {
            board = new LearnerBoard();
            //setto il deck
        }

        // adding the first player
        Player p = new Player();
        p.setColor(PlayerColor.values()[0]);
        p.setUsername(username);
        p.setGameStatus(true);
        game.addPlayer(p);
        board.addPlayer(p);
        //creating the slot for the other players
        for (int i=1; i<numberOfPlayers; i++) {
            Player player = new Player();
            player.setColor(PlayerColor.values()[i]);
            if (livello==2){
                player.setShip (new NormalShip());
            } else {
                player.setShip (new LearnerShip());
            }
            board.addPlayer(player);
            game.addPlayer(player);
        }
        game.addBoard(board);
        pile.addUnviewed(/*all components*/);
        game.setPile(pile);


        this.setGame(game);
    }
}
