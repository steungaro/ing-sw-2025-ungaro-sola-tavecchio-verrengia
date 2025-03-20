package it.polimi.ingsw.gc20.controller;

import it.polimi.ingsw.gc20.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.model.components.Component;
import it.polimi.ingsw.gc20.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.model.player.Player;
import it.polimi.ingsw.gc20.model.player.PlayerColor;

import javax.smartcardio.Card;
import java.security.InvalidParameterException;
import java.util.*;


/**
 * Controller class for managing the game flow and player interactions
 */
public class GameController {
    private GameModel model;
    private State state;
    private String gameID;
    private List<String> connectedPlayers = new ArrayList<>();
    private List<String> disconnectedPlayers = new ArrayList<>();

    /**
     * Default constructor
     *
     * @param id        unique identifier for this game
     * @param usernames list of player usernames
     * @param level     game difficulty level
     * @throws IllegalArgumentException if number of players is not between 2 and 4
     */
    public GameController(String id, List<String> usernames, int level) {
        if(usernames.size() > 4 || usernames.size() < 2) {
            throw new IllegalArgumentException("The number of players must be between 2 and 4");
        }
        gameID = id;
        model = new GameModel();
        model.startGame(level, usernames, gameID);
        state = State.CREATING;
        connectedPlayers.addAll(usernames);
    }

    /**
     * Private function that moves the state forward to the next state in sequence
     */
    private void nextState() {
        state = State.values()[(state.ordinal() + 1) % State.values().length];
    }

    /**
     * Sets the game state to assembling phase
     */
    public boolean assemblingStateComplete(){
        //TODO: Implement assembling phase
        return false;
    }

    /**
     * Handles the card drawing phase of the game
     * Processes the card based on its type
     */
    public void drawCard(){
        AdventureCard card = model.drawCard();
        //TODO: managing the different types of cards
    }

    /**
     * Gets a list of player colors that aren't currently assigned to any player
     *
     * @return List of available player colors
     */
    public List<PlayerColor> getAvailableColors(){
        List<PlayerColor> availableColors = new ArrayList<>(Arrays.asList(PlayerColor.values()));

        for (Player p: model.getGame().getPlayers()){
            PlayerColor color = p.getColor();
            availableColors.remove(color);
        }
        return availableColors;
    }

    public void initPlayersShip(){
        for (Player p: model.getGame().getPlayers()){
            if(!model.shipValidating(p)){
                //TODO: ship loop until bro gives us a valid ship
            }
        }

    }

    /**
     * Sets the color for a specific player
     *
     * @param username Username of the player to update
     * @param color Color to assign to the player
     * @throws IllegalArgumentException if player is not found or color is already taken
     */
    public void setPlayerColor(String username, PlayerColor color) {
        // Check if color is available
        List<PlayerColor> availableColors = getAvailableColors();
        if (!availableColors.contains(color)) {
            throw new IllegalArgumentException("Color is already taken by another player");
        }

        // Find player with matching username
        Player targetPlayer = null;
        for (Player p : model.getGame().getPlayers()) {
            if (p.getUsername().equals(username)) {
                targetPlayer = p;
                break;
            }
        }

        if (targetPlayer == null) {
            throw new IllegalArgumentException("Player not found in game");
        }

        // Set the color for the player
        targetPlayer.setColor(color);
    }

    public List<AdventureCard> peekDeck(int num){
        return model.viewDeck(num);
    }
    /**
     * Calculates and returns the final scores for all players
     *
     * @return Map associating each player with their score
     */
    public Map<Player, Integer> getPlayerScores(){
        Map<Player, Integer> score = model.calculateScore();
        return score;
    }

    /**
     * Handles a player leaving the game mid-session
     *
     * @param username Username of the exiting player
     * @throws IllegalArgumentException if player with given username is not found
     */
    public void disconnectPlayer(String username) {
        // Find player with matching username
        Player exitingPlayer = null;
        for (Player p : model.getGame().getPlayers()) {
            if (p.getUsername().equals(username)) {
                exitingPlayer = p;
                connectedPlayers.remove(username);
                break;
            }
        }

        if (exitingPlayer == null) {
            throw new IllegalArgumentException("Player not found in game");
        }

        // Add player to disconnected list if not already there
        if (!disconnectedPlayers.contains(username)) {
            disconnectedPlayers.add(username);
        }

        // Player remains in the game model but is marked as disconnected
    }

    /**
     * Handles reconnection of a previously disconnected player
     *
     * @param username Username of the player attempting to reconnect
     * @return true if reconnection was successful, false otherwise
     * @throws IllegalArgumentException if the player was never part of this game
     */
    public boolean reconnectPlayer(String username) {
        // Check if player was originally in this game
        if (!connectedPlayers.contains(username)) {
            throw new IllegalArgumentException("Player was never part of this game");
        }

        // Check if player is in the disconnected list
        if (!disconnectedPlayers.contains(username)) {
            return false; // Player isn't disconnected, nothing to do
        }

        // Remove player from disconnected list
        disconnectedPlayers.remove(username);

        // Player data is still in the model, so no need to recreate
        return true;
    }

    /**
     * Retrieves player data by username
     *
     * @param username Username of the player
     * @return Player object containing player data
     * @throws IllegalArgumentException if the player is not found
     */
    public Player getPlayerData(String username) {
        for (Player p : model.getGame().getPlayers()) {
            if (p.getUsername().equals(username)) {
                return p;
            }
        }
        throw new IllegalArgumentException("Player not found in game");
    }

    /**
     * Retrieves all usernames of players in the game
     *
     * @return List of player usernames
     */
    public List<String> getAllUsernames() {
        List<String> usernames = new ArrayList<>();
        for (Player p : model.getGame().getPlayers()) {
            usernames.add(p.getUsername());
        }
        return usernames;
    }

    /**
     * Retrieves the list of players who have disconnected from the game
     *
     * @return List of disconnected player usernames
     */
    public List<String> getDisconnectedPlayers() {
        return new ArrayList<>(disconnectedPlayers);
    }

    /**
     * Checks if a player is currently disconnected
     *
     * @param username Username to check
     * @return true if player is disconnected, false otherwise
     */
    public boolean isPlayerDisconnected(String username) {
        return disconnectedPlayers.contains(username);
    }

    /**
     * Gets the current state of the game
     *
     * @return Current game state
     */
    public State getState() {
        return state;
    }

    /**
     * Takes a component from the unviewed pile
     *
     * @param username Username of the player taking the component
     * @param component Component to take from the unviewed pile
     * @return The taken component
     * @throws IllegalStateException if game is not in ASSEMBLING state
     * @throws NoSuchElementException if the component is not in the unviewed pile
     */
    public Component takeComponentFromUnviewed(String username, Component component) {
        if (state != State.ASSEMBLING) {
            throw new IllegalStateException("Cannot take components outside the assembling phase");
        }

        try {
            model.componentFromUnviewed(component);
            // Add component to viewed pile so other players can see it
            model.componentToViewed(component);
            return component;
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Component not found in unviewed pile");
        }
    }

    /**
     * Takes a component from the viewed pile
     *
     * @param username Username of the player taking the component
     * @param component Component to take from the viewed pile
     * @return The taken component
     * @throws IllegalStateException if game is not in ASSEMBLING state
     * @throws NoSuchElementException if the component is not in the viewed pile
     */
    public Component takeComponentFromViewed(String username, Component component) {
        if (state != State.ASSEMBLING) {
            throw new IllegalStateException("Cannot take components outside the assembling phase");
        }

        try {
            model.componentFromViewed(component);
            return component;
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Component not found in viewed pile");
        }
    }

    /**
     * Takes a component that was previously booked by a player (Level 2 only)
     *
     * @param username Username of the player taking the component
     * @param component Component to take from the booked pile
     * @return The taken component
     * @throws IllegalStateException if game is not in ASSEMBLING state or not level 2
     * @throws IllegalArgumentException if the component is not in the player's booked list
     */
    public Component takeComponentFromBooked(String username, Component component) {
        if (state != State.ASSEMBLING) {
            throw new IllegalStateException("Cannot take components outside the assembling phase");
        }

        if (model.getLevel() != 2) {
            throw new IllegalStateException("Booking components is only available in level 2 games");
        }

        Player player = getPlayerData(username);

        try {
            model.componentFromBooked(component, player);
            return component;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Component not found in player's booked list");
        }
    }

    /**
     * Adds a component to player's booked list (Level 2 only)
     *
     * @param username Username of the player booking the component
     * @param component Component to book
     * @throws IllegalStateException if game is not in ASSEMBLING state or not level 2
     */
    public void addComponentToBooked(String username, Component component) {
        if (state != State.ASSEMBLING) {
            throw new IllegalStateException("Cannot book components outside the assembling phase");
        }

        if (model.getLevel() != 2) {
            throw new IllegalStateException("Booking components is only available in level 2 games");
        }

        Player player = getPlayerData(username);
        model.componentToBooked(component, player);
    }

    /**
     * Adds a component to the viewed pile so other players can see it
     *
     * @param component Component to add to viewed pile
     * @throws IllegalStateException if game is not in ASSEMBLING state
     */
    public void addComponentToPile(Component component) {
        if (state != State.ASSEMBLING) {
            throw new IllegalStateException("Cannot manipulate components outside the assembling phase");
        }

        model.componentToViewed(component);
    }

    /**
     * Places a component on the player's ship at specified coordinates
     *
     * @param username Username of the player placing the component
     * @param component Component to place
     * @param x X-coordinate on ship grid
     * @param y Y-coordinate on ship grid
     * @throws IllegalStateException if game is not in ASSEMBLING state
     * @throws InvalidParameterException if placement is invalid (handled by ship implementation)
     */
    public void placeComponent(String username, Component component, int x, int y) {
        if (state != State.ASSEMBLING) {
            throw new IllegalStateException("Cannot place components outside the assembling phase");
        }

        Player player = getPlayerData(username);

        try {
            model.addToShip(component, player, x, y);
        } catch (Exception e) {
            // Ship implementation will throw appropriate exceptions if placement is invalid
            throw new InvalidParameterException("Cannot place component at specified location: " + e.getMessage());
        }
    }

    /**
     * Rotates a component clockwise
     *
     * @param component Component to rotate
     */
    public void rotateComponentClockwise(Component component) {
        model.RotateClockwise(component);
    }

    /**
     * Rotates a component counterclockwise
     *
     * @param component Component to rotate
     */
    public void rotateComponentCounterclockwise(Component component) {
        model.RotateCounterclockwise(component);
    }

    /**
     * Removes a component from the player's ship
     *
     * @param username Username of the player removing the component
     * @param component Component to remove
     * @throws IllegalStateException if game is not in ASSEMBLING state
     */
    public void removeComponentFromShip(String username, Component component) {
        if (state != State.ASSEMBLING) {
            throw new IllegalStateException("Cannot remove components outside the assembling phase");
        }

        Player player = getPlayerData(username);
        model.removeComponent(component, player);
    }
}
