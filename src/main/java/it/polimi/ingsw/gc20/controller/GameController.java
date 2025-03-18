package it.polimi.ingsw.gc20.controller;

import it.polimi.ingsw.gc20.model.gamesets.GameModel;
import java.io.*;
import java.util.*;


/**
 * 
 */
public class GameController {
    public GameModel model;
    public State state;

    /**
     * Default constructor
     */
    public GameController() {
    }

    /**
     * Private function that moves the state forward
     */
    private void nextState() {
        state = State.values()[(state.ordinal() + 1) % State.values().length];
    }
}