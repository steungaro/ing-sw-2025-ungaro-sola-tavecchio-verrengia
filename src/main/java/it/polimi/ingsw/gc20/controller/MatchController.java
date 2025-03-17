package it.polimi.ingsw.gc20.controller;

import it.polimi.ingsw.gc20.model.lobby.Lobby;
import lobby.Lobby;

import java.util.*;

/**
 * 
 */
public class MatchController {

    /**
     * 
     */
    private List<GameController> games;
    
    private List<Lobby> lobbies;

    /**
     * 
     */
    private Map<String, Lobby> playersInLobbies;
    
    private Integer maxMatches; 
    
    private Integer maxLobbies;
    
    private static MatchController instance;

    /**
     * Default constructor
     */
    private MatchController() {
        this.games = new ArrayList<>();
        this.playersInLobbies = new HashMap<>();
    }
    
    /**
     * @return this
     */
    public static MatchController getInstance() {
        if(instance == null) {
            instance = new MatchController();
        }
        return instance;
    }

    /**
     * @param maxMatches 
     * @param maxLobbies 
     * @return
     */
    public static MatchController getInstance(Integer maxMatches, Integer maxLobbies) {
        if(instance == null) {
            instance = new MatchController();
            instance.setMaxMatches(maxMatches);
            instance.setMaxLobbies(maxLobbies);
        }
        return instance;
    }

    /**
     * @param m 
     * @return
     */
    public void setMaxLobbies(Integer m) {
        this.maxLobbies = m;
    }

    /**
     * @param m 
     * @return
     */
    public void setMaxMatches(Integer m) {
        this.maxMatches = m;
    }

    /**
     * @return
     */
    public List<Lobby> getLobbies() {
        return lobbies;
    }

    /**
     * @param id
     * @return
     */
    public Lobby getLobby(String id) {
        for(Lobby l: lobbies){
            if(l.getId().equals(id)){
                return l;
            }
        }
        return null;
    }

    /**
     * @param lobby 
     * @param user 
     * @return
     */
    public Lobby joinLobby(String lobby, String user) {
        for(Lobby l: lobbies){
            if(l.getId().equals(lobby)){
                l.addPlayer(user);
                return l;
            }
        }
        return null;
    }

    /**
     * @param name 
     * @param maxPlayers 
     * @param user
     * @return
     */
    public Lobby createLobby(String name, Integer maxPlayers, String user) {
        Lobby l = new Lobby(name, user, maxPlayers);
        lobbies.add(l);
        return l;
    }

    /**
     * @param id 
     * @return
     */
    public void leaveLobby(String id) {
        playersInLobbies.remove(id);
    }

    /**
     * @param id 
     * @return
     */
    public void startLobby(String id) {
        lobbies.get(lobbies.indexOf(id)).addPlayer(id);
    }

}