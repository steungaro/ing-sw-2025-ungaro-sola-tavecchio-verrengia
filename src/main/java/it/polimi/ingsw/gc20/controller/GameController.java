package it.polimi.ingsw.gc20.controller;

import it.polimi.ingsw.gc20.controller.event.*;
import it.polimi.ingsw.gc20.controller.states.*;
import it.polimi.ingsw.gc20.exceptions.*;
import it.polimi.ingsw.gc20.model.cards.*;
import it.polimi.ingsw.gc20.model.components.*;
import it.polimi.ingsw.gc20.model.gamesets.*;
import it.polimi.ingsw.gc20.model.player.*;
import it.polimi.ingsw.gc20.interfaces.*;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Controller class for managing the game flow and player interactions
 */
public class GameController implements GameControllerInterface {
    private final GameModel model;
    private State state;
    private final String gameID;

    private final List<String> connectedPlayers = new ArrayList<>();
    private final List<String> disconnectedPlayers = new ArrayList<>();

    private final Map<EventType<? extends Event>, List<EventHandler<? extends Event>>> eventHandlers = new HashMap<>();

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
        state = new AssemblingState(model);
        connectedPlayers.addAll(usernames);
        //TODO: notify players of game start
    }

    /**
     * Changes the game state and notifies players of the change
     * @param state is the new state of the game
     */
    public void setState(State state) {
        this.state = state;
        //TODO: notify players of state change
    }

    /** Getter method for the gameID
     * @return game id
     */
    public String getGameID() {
        return gameID;
    }

    /**
     * @return the first player in the list of players that is online
     */
    public String getFirstOnlinePlayer() {
        return model.getInGamePlayers().stream()
                .map(Player::getUsername)
                .filter(connectedPlayers::contains)
                .findFirst()
                .orElse(null);
    }

    /**
     * @param username is the username of the player
     * @return the next player in the list of players that is online or null if there are no more players (the last player has played or there are no players online)
     */
    private String getNextOnlinePlayer(String username) {
        if (model.getInGamePlayers().getLast().getUsername().equals(username)) {
            return null;
        }
        return model.getInGamePlayers()
                .stream()
                .map(Player::getUsername)
                .dropWhile(name -> !name.equals(username))
                .skip(1)  // Skip the current player
                .filter(connectedPlayers::contains)
                .findFirst()
                .orElse(null);
    }

    /**
     * Handles the card drawing phase of the game
     */
    public void drawCard(){
        AdventureCard card = model.drawCard();
        if (card == null) {
            state = new EndgameState(state.getController());
        } else {
            card.setState(this, model);
        }
    }

    /**
     * Returns the current game state
     * @return the current game state
     */
    public String getState() {
        return state.toString();
    }

    /**
     * Accepts a planet card and lands on the planet
     * @param username is the username of the player that wants to land on the planet
     * @param planetIndex is the index of the planet card in the player's hand
     * @throws IllegalStateException if the game is not in the planet phase
     * @throws InvalidTurnException if it is not the player's turn
     */
    @Override
    public void landOnPlanet(String username, int planetIndex) throws InvalidTurnException, IllegalStateException {
        state.landOnPlanet(getPlayerByID(username), planetIndex);
    }

    /**
     * Shoots an enemy (pirates, slavers, smugglers)
     * @param username is the username of the player that wants to shoot the enemy
     * @param cannons is the list of cannons that the player wants to use to shoot
     * @param batteries is the list of batteries that the player wants to use to shoot
     * @return 1 if the player wins, 0 if it's a draw, -1 if the player loses
     * @throws IllegalStateException if the game is not in the shooting phase
     * @throws InvalidTurnException if it is not the player's turn
     */
    @Override
    public int shootEnemy(String username, List<Cannon> cannons, List<Battery> batteries) throws IllegalStateException, InvalidTurnException, InvalidShipException {
        return state.shootEnemy(getPlayerByID(username), cannons, batteries);
    }

    /**
     * Loads cargo onto the player's ship
     * @param username is the username of the player that wants to load the cargo
     * @param loaded is the cargo that the player wants to load
     * @param ch is the cargo hold where the player wants to load the cargo
     * @throws IllegalStateException if the game is not in the cargo loading phase
     * @throws InvalidTurnException if it is not the player's turn
     * @apiNote To be used after accepting a planet, accepting a smuggler, accepting an abandoned station
     */
    @Override
    public void loadCargo(String username, CargoColor loaded, CargoHold ch) throws InvalidTurnException, CargoException {
        Player player = getPlayerByID(username);
        state.loadCargo(player, loaded, ch);
    }

    /**
     * Unloads cargo from the player's ship
     * @param username is the username of the player that wants to unload the cargo
     * @param lost is the cargo that the player wants to unload
     * @param ch is the cargo hold where the player wants to unload the cargo
     * @throws IllegalStateException if the game is not in the cargo unloading phase
     * @throws InvalidTurnException if it is not the player's turn
     * @apiNote To be used after accepting a planet, accepting a smuggler, accepting an abandoned station (to unload without limits) or after smugglers, combat zone (to remove most valuable one)
     */
    @Override
    public void unloadCargo(String username, CargoColor lost, CargoHold ch) throws InvalidTurnException, CargoException {
        state.unloadCargo(getPlayerByID(username), lost, ch);
    }

    /**
     * Moves cargo from one cargo hold to another
     * @param username is the username of the player that wants to move the cargo
     * @param cargo is the cargo that the player wants to move
     * @param from is the cargo hold where the player wants to move the cargo from
     * @param to is the cargo hold where the player wants to move the cargo to
     * @throws IllegalStateException if the game is not in the cargo moving phase
     * @throws InvalidTurnException if it is not the player's turn
     * @apiNote To be used in losing/gaining cargo
     */
    @Override
    public void moveCargo(String username, CargoColor cargo, CargoHold from, CargoHold to) throws InvalidTurnException, CargoException {
        state.moveCargo(getPlayerByID(username), cargo, from, to);
    }

    /**
     * Accepts a card and performs the action associated with it
     * @param username is the username of the player that wants to accept the card
     * @throws IllegalStateException if the game is not in the card phase
     * @throws InvalidTurnException if it is not the player's turn
     */
    @Override
    public void acceptCard(String username) throws IllegalStateException, InvalidTurnException {
        state.acceptCard(getPlayerByID(username));
    }

    public int getOnlinePlayers() {
        return connectedPlayers.size();
    }

    /**
     * To be called when a player wants to activate their cannons
     * @param username is the username of the player that wants to activate their cannons
     * @param cannons is the list of cannons that the player wants to activate
     * @param batteries is the list of batteries that the player wants to use to activate the cannons
     * @throws IllegalStateException if the game is not in the cannon activation phase
     * @throws InvalidTurnException if it is not the player's turn
     */
    @Override
    public void activateCannons(String username, List<Cannon> cannons, List<Battery> batteries) throws InvalidTurnException, InvalidShipException {
        state.activateCannons(getPlayerByID(username), cannons, batteries);
    }

    /**
     * @return the score of the game
     * @throws IllegalStateException if the game is not in the endgame phase
     */
    @Override
    public Map<String, Integer> getScore() throws IllegalStateException {
        return state.getScore();
    }

    /**
     * To be called when a player wants to lose crew
     * @param username is the username of the player that wants to lose crew
     * @param cabins is the list of cabins that the player wants to lose crew from
     * @throws IllegalStateException if the game is not in the correct phase
     * @throws InvalidTurnException if it is not the player's turn
     */
    @Override
    public void loseCrew(String username, List<Cabin> cabins) throws InvalidTurnException {
        state.loseCrew(getPlayerByID(username), cabins);
    }

    /**
     * To be called when a light_fire/meteor is firing the player
     * @param username is the username of the player that wants to activate the shield
     * @param shieldComp is the shield component that the player wants to activate
     * @param batteryComp is the energy component that the player wants to use to activate the shield
     * @throws IllegalStateException if the game is not in the meteor swarm phase
     * @throws IllegalArgumentException if it is not the player's turn
     * @apiNote Ship may need to be validated
     */
    public void activateShield(String username, Shield shieldComp, Battery batteryComp) throws InvalidTurnException, InvalidShipException {
        state.activateShield(getPlayerByID(username), shieldComp, batteryComp);
    }

    /**
     * Returns the active adventure card
     * @return the active adventure card
     */
    @Override
    public AdventureCard getActiveCard() {
        return model.getActiveCard();
    }

    /**
     * To be called when a player terminates their turn. Based on the type of card, the game will move to the next player or the next phase
     * @param username is the username of the player that wants to terminate their turn
     * @throws IllegalStateException if the game is not in the card phase
     * @throws InvalidTurnException if it is not the player's turn
     */
    public void endMove(String username) throws InvalidTurnException, InvalidShipException {
        state.endMove(getPlayerByID(username));
    }

    /**
     * To be called when a player activates their engines
     * @param username is the username of the player that wants to activate their engines
     * @param engines is the list of engines that the player wants to activate
     * @param batteries is the list of batteries that the player wants to use to activate the engines
     * @throws IllegalStateException if the game is not in the engine activation phase
     * @throws InvalidTurnException if it is not the player's turn
     * @throws InvalidShipException if the ship is not valid
     */
    @Override
    public void activateEngines(String username, List<Engine> engines, List<Battery> batteries) throws InvalidTurnException, InvalidShipException {
        state.activateEngines(getPlayerByID(username), engines, batteries);
    }

    /**
     * To be called when a player needs to chose one of the two branches a ship is divided into after a fire.
     * @param username is the username of the player that wants to choose a branch
     * @param col is the column of the ship where the player wants to choose a branch
     * @param row is the row of the ship where the player wants to choose a branch
     * @throws InvalidTurnException if it is not the player's turn
     * @throws InvalidShipException if the ship is not valid (in case a new heavyFire is shot right after choosing)
     */
    @Override
    public void chooseBranch(String username, int col, int row) throws InvalidTurnException, InvalidShipException {
        state.chooseBranch(getPlayerByID(username), col, row);
    }


    /**
     * @param asker is the username of the player asking for data
     * @param asked is the username of the player being asked for data
     * @return player data, if the asker is the same as the asked, return the full data, otherwise return only public data
     */
    @Override
    public Player getPlayerData(String asker, String asked) {
        if (asker.equals(asked)) {
            return getPlayerByID(asker);
        } else {
            return getPlayerByID(asked).getPublicData();
        }
    }


    /**
     * Calculates and returns the final scores for all players
     *
     * @return Map associating each player with their score
     */
    public Map<String, Integer> getPlayerScores(){
        return state.getScore();
    }

    /**
     * Handles the event of a player giving up
     * @param username is the username of the player that wants to give up
     */
    @Override
    public void giveUp(String username) {
        //TODO
        //model.giveUp(getPlayerByID(username));
    }

    /**
     * Handles a player leaving the game mid-session
     *
     * @param username Username of the exiting player
     * @throws IllegalArgumentException if player was not in game or not connected
     */
    public void disconnectPlayer(String username) {
        // Find player with matching username
        if (connectedPlayers.contains(username)) {
            connectedPlayers.remove(username);
            disconnectedPlayers.add(username);
        } else {
            throw new IllegalArgumentException("Player not found in game, not connected or never joined");
        }
        if(connectedPlayers.size() == 1){
            state = new PausedState(state, model, this);
        }else if(connectedPlayers.isEmpty()){
            state = new EndgameState(this);
            MatchController.getInstance().endGame(gameID);
        }
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
        if (getPlayerByID(username) != null) {
            throw new IllegalArgumentException("Player was never part of this game");
        }

        // Check if player is in the disconnected list
        if (!isPlayerDisconnected(username)) {
            return false; // Player isn't disconnected, nothing to do
        }

        // Remove player from disconnected list
        disconnectedPlayers.remove(username);
        connectedPlayers.add(username);

        if(connectedPlayers.size() == 2){
            state.resume();
        }
        return true;
    }

    /**
     * Retrieves player data by username
     *
     * @param username Username of the player
     * @return Player object containing player data
     * @throws IllegalArgumentException if the player is not found
     */
    public Player getPlayerByID(String username){
        return model.getGame().getPlayers()
                .stream()
                .filter(player -> player.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));
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
     * Takes a component from the unviewed pile
     *
     * @param username Username of the player taking the component
     * @param component Component to take from the unviewed pile
     * @return The taken component
     * @throws IllegalStateException if game is not in ASSEMBLING state
     * @throws NoSuchElementException if the component is not in the unviewed pile
     */
    public synchronized Component takeComponentFromUnviewed(String username, Component component) {
        return state.takeComponentFromUnviewed(getPlayerByID(username), component);
    }

    /**
     * Takes a component from the viewed pile
     *
     * @param username Username of the player taking the component
     * @param component Component to take from the viewed pile
     * @return The taken component
     * @throws IllegalStateException if game is not in ASSEMBLING state
     * @throws NoSuchElementException if the component is not in the viewed pile //TODO check if this is the right exception
     */
    public synchronized Component takeComponentFromViewed(String username, Component component) {
        return state.takeComponentFromViewed(getPlayerByID(username), component);
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
        if (model.getLevel() != 2) {
            throw new IllegalStateException("Booking components is only available in level 2 games");
        }
        return state.takeComponentFromBooked(getPlayerByID(username), component);
    }

    /**
     * Adds a component to player's booked list (Level 2 only)
     *
     * @param username Username of the player booking the component
     * @param component Component to book
     * @throws IllegalStateException if game is not in ASSEMBLING state or not level 2
     * @throws IllegalArgumentException if there aren't enough available spaces in the player's booked list
     */
    public void addComponentToBooked(String username, Component component) {
        if (model.getLevel() != 2) {
            throw new IllegalStateException("Booking components is only available in level 2 games");
        }
        state.addComponentToBooked(getPlayerByID(username), component);
    }

    /**
     * Adds a component to the viewed pile so other players can see it
     *
     * @param component Component to add to viewed pile
     * @throws IllegalStateException if game is not in ASSEMBLING state
     */
    public synchronized void addComponentToViewed(String username, Component component) {
        state.addComponentToViewed(component);
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
        state.placeComponent(getPlayerByID(username), component, x, y);
    }

    /**
     * Rotates a component clockwise
     *
     * @param component Component to rotate
     * @throws IllegalStateException if game is not in ASSEMBLING state
     */
    public void rotateComponentClockwise(Component component) {
        state.rotateComponentClockwise(component);
    }

    /**
     * Rotates a component counterclockwise
     *
     * @param component Component to rotate
     * @throws IllegalStateException if game is not in ASSEMBLING state
     */
    public void rotateComponentCounterclockwise(Component component) {
        state.rotateComponentCounterclockwise(component);
    }

    /**
     * Removes a component from the player's ship (only during validating phase and if the ship is not valid)
     *
     * @param username Username of the player removing the component
     * @param component Component to remove
     * @throws IllegalStateException if game is not in VALIDATING state
     * @apiNote view must call this function until the ship is valid
     * @see #validateShip(String username)
     */
    public void removeComponentFromShip(String username, Component component) {
        state.removeComp(getPlayerByID(username), component);
    }

    /**
     * Validates the player's ship
     *
     * @param username Username of the player that wants to validate the ship
     * @return true if ship is valid, false otherwise
     * @throws IllegalStateException if game is not in VALIDATING state
     * @apiNote this function must be called in a loop from the view until the ship is valid
     * @see #removeComponentFromShip(String, Component)
     * @implNote this function will also draw a card if all players have valid ships (and will change the state)
     */
    public boolean validateShip(String username) {
        return state.isShipValid(getPlayerByID(username));
    }

    /**
     * Stops the assembling phase for a player
     * @param username is the username of the player that wants to stop assembling
     * @param position is the relative position on board where the player wants to put their rocket
     * @throws IllegalStateException if game is not in ASSEMBLING state
     * @implNote performs state change to VALIDATING when all players have completed assembling
     */
    public void stopAssembling(String username, int position) {
        state.stopAssembling(getPlayerByID(username), position);
        if (state.allAssembled()) {
            state = new ValidatingShipState(model);
            //TODO: notify players of state change
        }
    }

    /**
     * Turns the hourglass for a player
     *
     * @param username Username of the player turning the hourglass
     * @throws IllegalStateException if game is not in ASSEMBLING state or not level 2
     * @throws IllegalArgumentException if the player is not connected
     * @throws HourglassException if the hourglass time is not 0
     * @throws HourglassException if the player has not completed assembling yet when turning last time
     */
    public void turnHourglass(String username) throws HourglassException {
        if (isPlayerDisconnected(username)) {
            throw new IllegalArgumentException("Cannot turn hourglass for not connected player");
        }
        if (model.getLevel() != 2) {
            throw new IllegalStateException("Hourglass is only available in level 2 games");
        }
        state.turnHourglass(getPlayerByID(username));
    }

    /**
     * Gets the remaining time for the hourglass
     *
     * @param username Username of the player checking the hourglass
     * @return Remaining time in seconds
     * @throws IllegalStateException if game is not in ASSEMBLING state
     * @throws IllegalArgumentException if the game is not in level 2 or the player is disconnected
     */
    public int getHourglassTime(String username) {
        if (model.getLevel() != 2) {
            throw new IllegalStateException("Hourglass is only available in level 2 games");
        }
        if (isPlayerDisconnected(username)) {
            throw new IllegalArgumentException("Player disconnected");
        }
        return state.getHourglassTime(getPlayerByID(username));
    }

    /**
     * Peeks at the selected deck
     *
     * @param username Username of the player peeking at the deck
     * @param num number of the deck to peek at
     * @return List of AdventureCard from the selected deck
     * @throws IllegalStateException if game is not in ASSEMBLING state
     * @throws IllegalArgumentException if player is not connected or has already completed assembling
     * @throws IllegalArgumentException if the game is not in level 2
     */
    public List<AdventureCard> peekDeck(String username, int num){
        if (model.getLevel() != 2) {
            throw new IllegalStateException("Decks are only available in level 2 games");
        }
        if (isPlayerDisconnected(username)) {
            throw new IllegalArgumentException("Cannot view deck for not connected player");
        }
        // TODO: synchronize this method on the selected deck?
        return state.peekDeck(getPlayerByID(username), num);
    }

    /**
     * Adds an alien to the player's ship
     *
     * @param username Username of the player adding the alien
     * @param color Color of the alien
     * @param cabin Cabin where the alien will be placed
     * @throws IllegalStateException if game is not in VALIDATING state
     * @throws IllegalArgumentException if ship is invalid or component is not a cabin
     * @throws IllegalArgumentException if the game is not in level 2
     * @throws IllegalArgumentException if the cabin provided is a StartingCabin
     */
    public void addAlien(String username, AlienColor color, Cabin cabin) {
                if(model.getLevel() != 2){
            throw new IllegalArgumentException("Aliens are only available in level 2 games");
        }
        state.addAlien(getPlayerByID(username), color, cabin);
    }

    /**
     * Sets the player to be ready to fly
     * @param username is the username of the player that wants to fly
     * @throws IllegalStateException if game is not in VALIDATING state
     * @throws IllegalArgumentException if the player's ship is not valid
     * @apiNote be careful to add aliens before calling this function
     */
    public void readyToFly(String username) {
        state.readyToFly(getPlayerByID(username));
        if (state.allShipsReady()) {
            state.initAllShips();
            model.createDeck();
            drawCard();
            //TODO: notify players of state change
        }
    }

    public void defeated(String username) {
        model.giveUp(getPlayerByID(username));
    }

    /**
     * @param username is the username of the player that wants to roll the dice
     * @throws IllegalStateException if the game is not in the correct phase
     * @throws InvalidTurnException  if it is not the player's turn
     */
    @Override
    public void rollDice(String username) throws IllegalStateException, InvalidTurnException, InvalidShipException {
        state.rollDice(getPlayerByID(username));
    }

    public String getCurrentPlayer() throws IllegalStateException {
        if (state instanceof PlayingState) {
            return ((PlayingState) state).getCurrentPlayer();
        } else {
            throw new IllegalStateException("No current player in this state");
        }
    }

    public List<String> getInGameConnectedPlayers() {
        return model.getInGamePlayers().stream()
                .map(Player::getUsername)
                .filter(connectedPlayers::contains)
                .collect(Collectors.toList());
    }
}