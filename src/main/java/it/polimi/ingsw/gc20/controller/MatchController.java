package it.polimi.ingsw.gc20.controller;

import lobby.Lobby;

import java.io.*;
import java.util.*;

/**
 * 
 */
public class MatchController {

    /**
     * Default constructor
     */
    public MatchController() {
    }

    /**
     * 
     */
    private List<GameController> games;

    /**
     * 
     */
    private List<String> usernames;

    /**
     * 
     */
    private List<GameController> games;

    /**
     * @return
     */
    public MatchController getInstance() {
        // TODO implement here
        return null;
    }

    /**
     * @param maxMatches 
     * @param maxLobbies 
     * @return
     */
    public MatchController getInstance(Integer maxMatches, Integer maxLobbies) {
        // TODO implement here
        return null;
    }

    /**
     * @param m 
     * @return
     */
    public void setMaxLobbies(Integer m) {
        // TODO implement here
        return null;
    }

    /**
     * @param m 
     * @return
     */
    public void setMaxMatches(Integer m) {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public List<Lobby> getLobbies() {
        // TODO implement here
        return null;
    }

    /**
     * @param id 
     * @return
     */
    public Lobby getLobby(String id) {
        // TODO implement here
        return null;
    }

    /**
     * @param player 
     * @return
     */
    public Lobby getLobby(String player) {
        // TODO implement here
        return null;
    }

    /**
     * @param lobby 
     * @param user 
     * @return
     */
    public LobbyInfo joinLobby(String lobby, String user) {
        // TODO implement here
        return null;
    }

    /**
     * @param name 
     * @param maxPlayers 
     * @param user 
     * @param id 
     * @return
     */
    public LobbyInfo createLobby(String name, Integer maxPlayers, String user, String id) {
        // TODO implement here
        return null;
    }

    /**
     * @param id 
     * @return
     */
    public void leaveLobby(String id) {
        // TODO implement here
        return null;
    }

    /**
     * @param id 
     * @return
     */
    public void startLobby(String id) {
        // TODO implement here
        return null;
    }

}