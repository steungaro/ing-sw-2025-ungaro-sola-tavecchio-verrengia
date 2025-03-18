package it.polimi.ingsw.gc20.controller;

import it.polimi.ingsw.gc20.model.gamesets.GameModel;
import java.io.*;
import java.util.*;


/**
 * 
 */
public class GameController {
    private GameModel model;
    private State state;

    /**
     * Default constructor
     */
    public GameController(List<String> usernames, int level) {
        if(usernames.size() > 4 || usernames.size() < 2) {
            throw new IllegalArgumentException("The number of players must be between 2 and 4");
        }
        int gameID = new Random().nextInt();
        model = new GameModel();
        model.startGame(level, usernames, gameID);


    }

    /**
     * Private function that moves the state forward
     */
    private void nextState() {
        state = State.values()[(state.ordinal() + 1) % State.values().length];
    }
}