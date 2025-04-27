package it.polimi.ingsw.gc20.server.controller;

import it.polimi.ingsw.gc20.server.exceptions.FullLobbyException;
import it.polimi.ingsw.gc20.server.exceptions.LobbyException;
import it.polimi.ingsw.gc20.common.interfaces.MatchControllerInterface;
import it.polimi.ingsw.gc20.server.model.lobby.Lobby;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 */
public class MatchController implements MatchControllerInterface {

    private final List<GameController> games;
    private final List<Lobby> lobbies;
    private final Map<String, Lobby> playersInLobbies;
    private final Map<String, GameController> playersInGames;
    private int maxMatches;
    private int maxLobbies;
    private static MatchController instance;
    private final Logger logger = Logger.getLogger(MatchController.class.getName());

    /**
     * Default constructor
     */
    private MatchController() {
        this.games = new ArrayList<>();
        this.playersInLobbies = new HashMap<>();
        this.lobbies = new ArrayList<>();
        this.playersInGames = new HashMap<>();
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
     */
    public void joinLobby(String id, String user) {
        for(Lobby l: lobbies){
            if(l.getId().equals(id)){
                try {
                    l.addPlayer(user);
                    playersInLobbies.put(user, l);
                } catch (FullLobbyException e) {
                    logger.log(Level.WARNING, "Lobby is full", e);
                }
            }
        }
    }

    /**
     * @param name is the name of the lobby
     * @param maxPlayers is the max number of players
     * @param user is the owner of the lobby that joins automatically
     * @throws IllegalArgumentException if the max number of lobbies is reached
     */
    public void createLobby(String name, String user, int maxPlayers, int level) {
        String id = UUID.randomUUID().toString();
        if (lobbies.size() >= maxLobbies) {
            logger.log(Level.WARNING, "Max lobbies reached");
        } else {
            Lobby l = new Lobby(id, name, user, maxPlayers, level);
            playersInLobbies.put(user, l);
            lobbies.add(l);
        }
    }

    /**
     * @param userid is the id of the lobby to leave, if the lobby is empty it is removed
     */
    public void leaveLobby(String userid) {
        if (playersInLobbies.containsKey(userid)) {
            if (playersInLobbies.get(userid).getUsers().isEmpty()) {
                lobbies.remove(playersInLobbies.get(userid));
            }
            try {
                playersInLobbies.get(userid).removePlayer(userid);
                playersInLobbies.remove(userid);
            } catch (LobbyException e) {
                logger.log(Level.WARNING, "Owner of the lobby can't leave the lobby", e);
            }
        }else {
            logger.log(Level.WARNING, "User not found in lobbies");
        }

    }

    /**
     * @param id is the id of the game to end
     */
    public void endGame(String id) {
        games.removeIf(g -> g.getGameID().equals(id));
        for (String user : playersInGames.keySet()) {
            if (playersInGames.get(user).getGameID().equals(id)) {
                playersInGames.remove(user);
            }
        }
    }

    /**
     * @param id is the id of the lobby to start
     */
    public void startLobby(String id) {
        if (lobbies.isEmpty()) {
            logger.log(Level.WARNING, "No lobbies available");
        } else if (games.size() >= maxMatches) {
            logger.log(Level.WARNING, "Max matches reached");
        } else {
            try {
                games.add(lobbies.stream()
                        .filter(l -> l.getId().equals(id))
                        .findFirst()
                        .orElseThrow(() -> new LobbyException("No such lobby"))
                        .createGameController());
                lobbies.removeIf(l -> l.getId().equals(id));
                List<String> usersToRemove = new ArrayList<>();
                for (String user : playersInLobbies.keySet()) {
                    if (playersInLobbies.get(user).getId().equals(id)) {
                        usersToRemove.add(user);
                    }
                }
                for (String user : usersToRemove) {
                    playersInGames.put(user, games.getLast());
                    playersInLobbies.remove(user);
                }
            } catch (LobbyException e) {
                logger.log(Level.WARNING, "No such Lobby", e);
            }
        }
    }

    public List<String> getAllUsers() {
        List<String> allUsers = new ArrayList<>();
        allUsers.addAll(playersInGames.keySet());
        allUsers.addAll(playersInLobbies.keySet());
        return allUsers;
    }

    public boolean isUsernameAvailable(String username) {
        return !playersInGames.containsKey(username) && !playersInLobbies.containsKey(username);
    }

    public GameController getGameControllerForPlayer(String username) {
        return playersInGames.getOrDefault(username, null);
    }
}