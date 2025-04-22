package it.polimi.ingsw.gc20.controller;

import it.polimi.ingsw.gc20.interfaces.MatchControllerInterface;
import it.polimi.ingsw.gc20.model.lobby.Lobby;

import java.util.*;

/**
 * 
 */
public class MatchController implements MatchControllerInterface {

    private final List<GameController> games;
    private final List<Lobby> lobbies;
    private final Map<String, Lobby> playersInLobbies;
    private int maxMatches;
    private int maxLobbies;
    private static MatchController instance;

    /**
     * Default constructor
     */
    private MatchController() {
        this.games = new ArrayList<>();
        this.playersInLobbies = new HashMap<>();
        this.lobbies = new ArrayList<>();
    }

    /**
     * @return the game controller for the given id, null if not found
     */
    public GameController getGameController(String id) {
        for(GameController g: games){
            if(g.getGameID().equals(id)){
                return g;
            }
        }
        return null;
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
     * @return the list of players in lobbies
     */
    public List<String> getPlayersInLobbies() {
        return new ArrayList<>(playersInLobbies.keySet());
    }

    /**
     * @param maxMatches is the max number of matches
     * @param maxLobbies is the max number of lobbies
     * @return this instance
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
     * @param m is the max number of lobbies
     */
    public void setMaxLobbies(int m) {
        this.maxLobbies = m;
    }

    /**
     * @param m is the max number of matches
     */
    public void setMaxMatches(int m) {
        this.maxMatches = m;
    }

    /**
     * @return the list of lobbies
     */
    public List<Lobby> getLobbies() {
        return lobbies;
    }

    /**
     * @param id is the id of the lobby
     * @return the lobby with the given id
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
     * @param id is the id of the lobby
     * @param user is the user that wants to join
     * @return the lobby with the given id
     */
    public Lobby joinLobby(String id, String user) {
        for(Lobby l: lobbies){
            if(l.getId().equals(id)){
                l.addPlayer(user);
                playersInLobbies.put(user, l);
                return l;
            }
        }
        return null;
    }

    /**
     * @param name is the name of the lobby
     * @param maxPlayers is the max number of players
     * @param user is the owner of the lobby that joins automatically
     * @return the lobby created
     * @throws IllegalArgumentException if the max number of lobbies is reached
     */
    public Lobby createLobby(String name, String user, int maxPlayers, int level) {
        String id = UUID.randomUUID().toString();
        if (lobbies.size() >= maxLobbies) {
            throw new IllegalArgumentException("Max lobbies reached");
        }
        Lobby l = new Lobby(id, name, user, maxPlayers, level);
        playersInLobbies.put(user, l);
        lobbies.add(l);
        return l;
    }

    /**
     * @param userid is the id of the lobby to leave, if the lobby is empty it is removed
     * @throws IllegalArgumentException if the user is not in any lobby
     */
    public void leaveLobby(String userid) {
        if (!playersInLobbies.containsKey(userid)) {
            throw new IllegalArgumentException("User not in any lobby");
        }
        if (playersInLobbies.get(userid).getUsers().isEmpty()) {
            lobbies.remove(playersInLobbies.get(userid));
        }
        playersInLobbies.get(userid).removePlayer(userid);
        playersInLobbies.remove(userid);
    }

    /**
     * @param id is the id of the game to end
     */
    public void endGame(String id) {
        games.removeIf(g -> g.getGameID().equals(id));
    }

    /**
     * @param id is the id of the lobby to start
     * @throws IllegalArgumentException if the lobby is not found or if the max number of matches is reached
     */
    public void startLobby(String id) {
        if (lobbies.isEmpty()) {
            throw new IllegalArgumentException("No lobbies available");
        }
        if (games.size() >= maxMatches) {
            throw new IllegalArgumentException("Max matches reached");
        }
        games.add(lobbies.stream()
                .filter(l -> l.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No such lobby"))
                .createGameController());
        lobbies.removeIf(l -> l.getId().equals(id));
        List<String> usersToRemove = new ArrayList<>();
        for (String user : playersInLobbies.keySet()) {
            if (playersInLobbies.get(user).getId().equals(id)) {
                usersToRemove.add(user);
            }
        }

        for (String user : usersToRemove) {
            playersInLobbies.remove(user);
        }
    }
}