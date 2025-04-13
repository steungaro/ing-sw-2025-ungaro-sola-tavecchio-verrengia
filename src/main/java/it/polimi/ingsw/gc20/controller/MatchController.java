package it.polimi.ingsw.gc20.controller;

import it.polimi.ingsw.gc20.model.lobby.Lobby;

import java.util.*;

/**
 * 
 */
public class MatchController {

    private List<GameController> games;
    private List<Lobby> lobbies;
    private Map<String, Lobby> playersInLobbies;
    private int maxMatches;
    private int maxLobbies;
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
    public static MatchController getInstance(int maxMatches, int maxLobbies) {
        if(instance == null) {
            instance = new MatchController();
            instance.setMaxMatches(maxMatches);
            instance.setMaxLobbies(maxLobbies);
        }
        return instance;
    }

    /**
     * @param m
     */
    public void setMaxLobbies(int m) {
        this.maxLobbies = m;
    }

    /**
     * @param m
     */
    public void setMaxMatches(int m) {
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
    public Lobby createLobby(String name, int maxPlayers, String user, int level) {
        String id = UUID.randomUUID().toString();
        Lobby l = new Lobby(id, name, user, maxPlayers, level);
        lobbies.add(l);
        return l;
    }

    /**
     * @param userid is the id of the lobby to leave
     */
    public void leaveLobby(String userid) {

        playersInLobbies.get(userid).removePlayer(userid);

    }

    public void endGame(String id) {
        games.removeIf(g -> g.getGameID().equals(id));
    }

    /**
     * @param id is the id of the lobby to start
     */
    /*public void startLobby(String id) {
        games.add(lobbies.stream()
                .filter(l -> l.getId().equals(id))
                .findFirst()
                .get()
                .createGameController());
    }*/
}