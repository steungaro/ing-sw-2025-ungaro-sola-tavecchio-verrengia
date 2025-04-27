package it.polimi.ingsw.gc20.controller;

import it.polimi.ingsw.gc20.controller.states.*;
import it.polimi.ingsw.gc20.exceptions.*;
import it.polimi.ingsw.gc20.model.cards.*;
import it.polimi.ingsw.gc20.model.components.*;
import it.polimi.ingsw.gc20.model.gamesets.*;
import it.polimi.ingsw.gc20.model.player.*;
import it.polimi.ingsw.gc20.interfaces.*;
import org.javatuples.Pair;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private final Logger logger = Logger.getLogger(GameController.class.getName());

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

        this.gameID = id;
        this.model = new GameModel();

        try {
            model.startGame(level, usernames, gameID);
            state = new AssemblingState(model);
            connectedPlayers.addAll(usernames);
            //TODO: notify players of game start
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error starting game", e);
        }
    }

    /**
     * Changes the game state and notifies players of the change
     * @param state is the new state of the game
     */
    public void setState(State state) {
        try{
            this.state = state;
            //TODO: notify players of state change
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error setting state", e);
        }
    }

    /** Getter method for the gameID
     * @return game id
     */
    public String getGameID() {
        try{
            return gameID;
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, "Error getting game ID", e);
        }
        return null;
    }

    /**
     * @return the first player in the list of players that is online
     */
    public String getFirstOnlinePlayer() {
        try{
            return model.getInGamePlayers().stream()
                    .map(Player::getUsername)
                    .filter(connectedPlayers::contains)
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error getting first online player", e);
        }
        return null;
    }


    /**
     * Handles the card drawing phase of the game
     */
    public void drawCard() {
        try {
            AdventureCard card = model.drawCard();
            card.setState(this, model);
        } catch (EmptyDeckException e) {
            state = new EndgameState(state.getController());
        }
    }

    /**
     * Returns the current game state
     * @return the current game state
     */
    public String getState() {
        try{
            return state.toString();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error getting state", e);
        }
        return null;
    }

    /**
     * Accepts a planet card and lands on the planet
     * @param username is the username of the player that wants to land on the planet
     * @param planetIndex is the index of the planet card in the player's hand
     */
    @Override
    public void landOnPlanet(String username, int planetIndex) {
        try{
            state.landOnPlanet(getPlayerByID(username), planetIndex);
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, "Error landing on planet", e);
        }

    }

    /**
     * Shoots an enemy (pirates, slavers, smugglers)
     * @param username is the username of the player that wants to shoot the enemy
     * @param cannons is the list of coordinates of the cannons that the player wants to use to shoot
     * @param batteries is the list of coordinates of the batteries that the player wants to use to shoot
     */
    @Override
    public void shootEnemy(String username, List<Pair<Integer, Integer>> cannons, List<Pair<Integer, Integer>> batteries) {
        try{
            state.shootEnemy(getPlayerByID(username), cannons, batteries);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error shooting enemy", e);
        }
    }

    /**
     * Loads cargo onto the player's ship
     * @param username is the username of the player that wants to load the cargo
     * @param loaded is the cargo that the player wants to load
     * @param ch are the coordinates of the cargo hold where the player wants to load the cargo
     * @apiNote To be used after accepting a planet, accepting a smuggler, accepting an abandoned station
     */
    @Override
    public void loadCargo(String username, CargoColor loaded, Pair<Integer, Integer> ch) {
        try {
            Player player = getPlayerByID(username);
            state.loadCargo(player, loaded, ch);
        } catch (Exception e){
            logger.log(Level.SEVERE, "Error loading cargo", e);
        }
    }

    /**
     * Unloads cargo from the player's ship
     * @param username is the username of the player that wants to unload the cargo
     * @param lost is the cargo that the player wants to unload
     * @param ch are the coordinates of the cargo hold where the player wants to unload the cargo
     * @apiNote To be used after accepting a planet, accepting a smuggler, accepting an abandoned station (to unload without limits) or after smugglers, combat zone (to remove most valuable one)
     */
    @Override
    public void unloadCargo(String username, CargoColor lost, Pair<Integer, Integer> ch) {
        try{
            state.unloadCargo(getPlayerByID(username), lost, ch);
        } catch (Exception e){
            logger.log(Level.SEVERE, "Error unloading cargo", e);
        }
    }

    /**
     * Moves cargo from one cargo hold to another
     * @param username is the username of the player that wants to move the cargo
     * @param cargo is the cargo that the player wants to move
     * @param from are the coordinates of the cargo hold where the player wants to move the cargo from
     * @param to are the coordinates of the cargo hold where the player wants to move the cargo to
     * @apiNote To be used in losing/gaining cargo
     */
    @Override
    public void moveCargo(String username, CargoColor cargo, Pair<Integer, Integer> from, Pair<Integer, Integer> to) {
        try{
            state.moveCargo(getPlayerByID(username), cargo, from, to);
        } catch (Exception e){
            logger.log(Level.SEVERE, "Error moving cargo", e);
        }
    }

    /**
     * Accepts a card and performs the action associated with it
     * @param username is the username of the player that wants to accept the card
     */
    @Override
    public void acceptCard(String username) {
        try{
            state.acceptCard(getPlayerByID(username));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error accepting card", e);
        }
    }

    public int getOnlinePlayers() {
        try{
            return connectedPlayers.size();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error getting online players", e);
        }
        return 0;
    }

    /**
     * To be called when a player wants to activate their cannons
     * @param username is the username of the player that wants to activate their cannons
     * @param cannons is the list of coordinates of the cannons that the player wants to activate
     * @param batteries is the list of coordinates of the batteries that the player wants to use to activate the cannons
     */
    @Override
    public void activateCannons(String username, List<Pair<Integer, Integer>> cannons, List<Pair<Integer, Integer>> batteries) {
        try{
            state.activateCannons(getPlayerByID(username), cannons, batteries);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error activating cannons", e);
        }
    }

    /**
     * @return the score of the game
     */
    public Map<String, Integer> getScore() {
        try{
            return state.getScore();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error getting score", e);
        }
        return null;
    }

    /**
     * To be called when a player wants to lose crew
     * @param username is the username of the player that wants to lose crew
     * @param cabins is the list of coordinates of the cabins that the player wants to lose crew from
     */
    @Override
    public void loseCrew(String username, List<Pair<Integer, Integer>> cabins) {
        try{
            state.loseCrew(getPlayerByID(username), cabins);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error losing crew", e);
        }
    }

    /**
     * To be called when a light_fire/meteor is firing the player
     * @param username is the username of the player that wants to activate the shield
     * @param shieldComp are the coordinates of the shield component that the player wants to activate
     * @param batteryComp are the coordinates of the energy component that the player wants to use to activate the shield
     * @apiNote Ship may need to be validated
     */
    public void activateShield(String username, Pair<Integer, Integer> shieldComp, Pair<Integer, Integer> batteryComp) {
        try{
            state.activateShield(getPlayerByID(username), shieldComp, batteryComp);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error activating shield", e);
        }
    }

    /**
     * Returns the active adventure card
     * @return the active adventure card
     */
    public AdventureCard getActiveCard() {
        try{
            return model.getActiveCard();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error getting active card", e);
        }
        return null;
    }

    /**
     * To be called when a player terminates their turn. Based on the type of card, the game will move to the next player or the next phase
     * @param username is the username of the player that wants to terminate their turn
     */
    public void endMove(String username) {
        try{
            state.endMove(getPlayerByID(username));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error ending move", e);
        }
    }

    /**
     * To be called when a player activates their engines
     * @param username is the username of the player that wants to activate their engines
     * @param engines is the list of coordinates of the engines that the player wants to activate
     * @param batteries is the list of coordinates of the batteries that the player wants to use to activate the engines
     */
    @Override
    public void activateEngines(String username, List<Pair<Integer, Integer>> engines, List<Pair<Integer, Integer>> batteries) {
        try{
            state.activateEngines(getPlayerByID(username), engines, batteries);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error activating engines", e);
        }
    }

    /**
     * To be called when a player needs to chose one of the two branches a ship is divided into after a fire.
     * @param username is the username of the player that wants to choose a branch
     * @param coordinates are the coordinates of the branch that the player wants to choose
     */
    @Override
    public void chooseBranch(String username, Pair<Integer, Integer> coordinates) {
        try{
            state.chooseBranch(getPlayerByID(username), coordinates);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error choosing brance", e);
        }
    }


    /**
     * @param asker is the username of the player asking for data
     * @param asked is the username of the player being asked for data
     * @return player data, if the asker is the same as the asked, return the full data, otherwise return only public data
     */
    public Player getPlayerData(String asker, String asked) {
        try{
            if (asker.equals(asked)) {
                return getPlayerByID(asker);
            } else {
                return getPlayerByID(asked).getPublicData();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error getting player data", e);
        }

        return null;
    }


    /**
     * Calculates and returns the final scores for all players
     *
     * @return Map associating each player with their score
     */
    public Map<String, Integer> getPlayerScores(){
        try{
            return state.getScore();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error getting player score", e);
        }
        return null;
    }

    /**
     * Handles the event of a player giving up
     * @param username is the username of the player that wants to give up
     */
    @Override
    public void giveUp(String username) {
        if(!Objects.equals(state.toString(), "PreDrawState")){
            throw new IllegalStateException("Can only give up when the turn has ended");
        }
        try {
            model.giveUp(getPlayerByID(username));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error giving up", e);
        }
    }

    /**
     * Handles a player leaving the game mid-session
     *
     * @param username Username of the exiting player
     */
    public void disconnectPlayer(String username) {
        try{
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
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error disconnecting player", e);
        }
    }

    /**
     * Handles reconnection of a previously disconnected player
     *
     * @param username Username of the player attempting to reconnect
     * @return true if reconnection was successful, false otherwise
     */
    public boolean reconnectPlayer(String username) {
        try{
            // Check if player was originally in this game
            if (getPlayerByID(username) == null) {
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
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error reconnecting player", e);
        }
        return false;
    }

    /**
     * Retrieves player data by username
     *
     * @param username Username of the player
     * @return Player object containing player data
     */
    public Player getPlayerByID(String username){
        try{
            return model.getGame().getPlayers()
                    .stream()
                    .filter(player -> player.getUsername().equals(username))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Player not found"));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error getting player by ID", e);
        }
        return null;
    }

    /**
     * Retrieves all usernames of players in the game
     *
     * @return List of player usernames
     */
    public List<String> getAllUsernames() {
        try{
            List<String> usernames = new ArrayList<>();
            for (Player p : model.getGame().getPlayers()) {
                usernames.add(p.getUsername());
            }
            return usernames;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error getting all usernames", e);
        }
        return null;
    }

    /**
     * Retrieves the list of players who have disconnected from the game
     *
     * @return List of disconnected player usernames
     */
    public List<String> getDisconnectedPlayers() {
        try{
            return new ArrayList<>(disconnectedPlayers);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error getting disconnected players", e);
        }
        return null;
    }

    /**
     * Checks if a player is currently disconnected
     *
     * @param username Username to check
     * @return true if player is disconnected, false otherwise
     */
    public boolean isPlayerDisconnected(String username) {
        try{
            return disconnectedPlayers.contains(username);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error checking if the player is disconnect", e);
        }
        return false;
    }

    /**
     * Takes a component from the unviewed pile and adds it to the player's hand
     *
     * @param username Username of the player taking the component
     * @param index Index of the component in the unviewed pile
     * @throws IllegalStateException if game is not in ASSEMBLING state
     * @throws NoSuchElementException if the component is not in the unviewed pile
     * @throws IllegalArgumentException if the player already has a component in hand
     */
    public synchronized void takeComponentFromUnviewed(String username, int index) {
        try{
            state.takeComponentFromUnviewed(getPlayerByID(username), index);
            // TODO: notify players of component taken
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error taking component from unviewed", e);
        }
    }

    /**
     * Takes a component from the viewed pile and adds it to the player's hand
     *
     * @param username Username of the player taking the component
     * @param index Index of the component in the viewed pile
     * @throws IllegalStateException if game is not in ASSEMBLING state
     * @throws NoSuchElementException if the component is not in the viewed pile
     * @throws IllegalArgumentException if the player already has a component in hand
     */
    public synchronized void takeComponentFromViewed(String username, int index) {
        try{
            state.takeComponentFromViewed(getPlayerByID(username), index);
            // TODO: notify players of component taken
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error taking component from view", e);
        }
    }

    /**
     * Takes a component that was previously booked by a player (Level 2 only) and adds it to the player's hand
     *
     * @param username Username of the player taking the component
     * @param index Index of the component in the booked list
     */
    public void takeComponentFromBooked(String username, int index) {
        try{
            if (model.getLevel() != 2) {
                throw new IllegalStateException("Booking components is only available in level 2 games");
            }
            state.takeComponentFromBooked(getPlayerByID(username), index);
            // TODO: notify players of component taken
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error taking componente from booked", e);
        }
    }

    /**
     * Adds the component in hand to player's booked list (Level 2 only)
     *
     * @param username Username of the player booking the component
     */
    public void addComponentToBooked(String username) {
        try{
            if (model.getLevel() != 2) {
                throw new IllegalStateException("Booking components is only available in level 2 games");
            }
            state.addComponentToBooked(getPlayerByID(username));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error adding component to booked", e);
        }
    }

    /**
     * Adds the component in hand to the viewed pile so other players can see it
     *
     * @param username Username of the player adding the component
     */
    public synchronized void addComponentToViewed(String username) {
        try{
            state.addComponentToViewed(getPlayerByID(username));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error adding component to view", e);
        }
    }

    /**
     * Places the component in hand on the player's ship at specified coordinates
     *
     * @param username Username of the player placing the component
     * @param coordinates Coordinates where the component will be placed
     */
    public void placeComponent(String username, Pair<Integer, Integer> coordinates) {
        try{
            state.placeComponent(getPlayerByID(username), coordinates);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error placing component", e);
        }
    }

    /**
     * Rotates the component in hand clockwise
     *
     * @param username Username of the player rotating the component
     */
    public void rotateComponentClockwise(String username) {
        try{
            state.rotateComponentClockwise(getPlayerByID(username));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error rotating component clockwise", e);
        }
    }

    /**
     * Rotates the component in hand counterclockwise
     *
     * @param username Username of the player rotating the component
     */
    public void rotateComponentCounterclockwise(String username) {
        try{
            state.rotateComponentCounterclockwise(getPlayerByID(username));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error rotating component clockwise", e);
        }
    }

    /**
     * Removes a component from the player's ship (only during validating phase and if the ship is not valid)
     *
     * @param username Username of the player removing the component
     * @param coordinates Coordinates of the component to be removed
     * @apiNote view must call this function until the ship is valid
     * @see #validateShip(String username)
     */
    public void removeComponentFromShip(String username, Pair<Integer, Integer> coordinates) {
        try{
            state.removeComp(getPlayerByID(username), coordinates);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error removing component from ship", e);
        }
    }

    /**
     * Validates the player's ship
     *
     * @param username Username of the player that wants to validate the ship
     * @throws IllegalStateException if game is not in VALIDATING state
     * @apiNote this function must be called in a loop from the view until the ship is valid
     * @implNote this function will also draw a card if all players have valid ships (and will change the state)
     */
    public void validateShip(String username) {
        try{
            state.isShipValid(getPlayerByID(username));
            // TODO: notify players of ship validation
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error validating ship", e);
        }
    }

    /**
     * Stops the assembling phase for a player
     * @param username is the username of the player that wants to stop assembling
     * @param position is the relative position on board where the player wants to put their rocket
     * @implNote performs state change to VALIDATING when all players have completed assembling
     */
    public void stopAssembling(String username, int position) {
        try{
            state.stopAssembling(getPlayerByID(username), position);
            if (state.allAssembled()) {
                state = new ValidatingShipState(model);
                //TODO: notify players of state change
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error stopping assembling", e);
        }
    }

    /**
     * Turns the hourglass for a player
     *
     * @param username Username of the player turning the hourglass
     */
    public void turnHourglass(String username) {
        try{
            if (isPlayerDisconnected(username)) {
                throw new IllegalArgumentException("Cannot turn hourglass for not connected player");
            }
            if (model.getLevel() != 2) {
                throw new IllegalStateException("Hourglass is only available in level 2 games");
            }
            state.turnHourglass(getPlayerByID(username));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error turning hourglass", e);
        }
    }

    /**
     * Gets the remaining time for the hourglass
     *
     * @param username Username of the player checking the hourglass
     * @return Remaining time in seconds
     */
    public int getHourglassTime(String username) {
        try {
            if (model.getLevel() != 2) {
                throw new IllegalStateException("Hourglass is only available in level 2 games");
            }
            if (isPlayerDisconnected(username)) {
                throw new IllegalArgumentException("Player disconnected");
            }
            return state.getHourglassTime(getPlayerByID(username));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error getting hourglass", e);
        }
        return 0;
    }

    /**
     * Peeks at the selected deck
     *
     * @param username Username of the player peeking at the deck
     * @param num number of the deck to peek at
     */
    public void peekDeck(String username, int num) {
        try{
            if (model.getLevel() != 2) {
                throw new IllegalStateException("Decks are only available in level 2 games");
            }
            if (isPlayerDisconnected(username)) {
                throw new IllegalArgumentException("Cannot view deck for not connected player");
            }
            state.peekDeck(getPlayerByID(username), num);
            // TODO: notify players of deck peek
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error peeking deek", e);
        }
    }

    /**
     * Adds an alien to the player's ship
     *
     * @param username Username of the player adding the alien
     * @param color Color of the alien
     * @param cabin Cabin where the alien will be placed (coordinates)
     */
    @Override
    public void addAlien(String username, AlienColor color, Pair<Integer, Integer> cabin) {
        try{
            if(model.getLevel() != 2){
                throw new IllegalArgumentException("Aliens are only available in level 2 games");
            }
            state.addAlien(getPlayerByID(username), color, cabin);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error adding alien", e);
        }
    }

    /**
     * Sets the player to be ready to fly
     * @param username is the username of the player that wants to fly
     * @apiNote be careful to add aliens before calling this function
     */
    public void readyToFly(String username) {
        try{
            state.readyToFly(getPlayerByID(username));
            if (state.allShipsReadyToFly()) {
                state.initAllShips();
                model.createDeck();
                drawCard();
                //TODO: notify players of state change
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error ready to fly", e);
        }
    }

    public void defeated(String username) {
        try{
            model.giveUp(getPlayerByID(username));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error defeating enemy", e);
        }
    }

    /**
     * @param username is the username of the player that wants to roll the dice
     */
    @Override
    public void rollDice(String username) {
        try{
            state.rollDice(getPlayerByID(username));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error rolling dice", e);
        }
    }

    /*
     * @param username is the username of the player that wants to roll the dice
     * @throws IllegalStateException if the game is not in the correct phase
     * @throws InvalidTurnException  if it is not the player's turn
     */
    public GameModel getModel() {
        try{
            return model;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error getting model", e);
        }
        return null;
    }


    public List<String> getInGameConnectedPlayers() {
        try{
            return model.getInGamePlayers().stream()
                    .map(Player::getUsername)
                    .filter(connectedPlayers::contains)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error getting in came connected players", e);
        }
        return null;
    }

    @Override
    public void killGame(String username) {
        if (connectedPlayers.contains(username)) {
            MatchController.getInstance().endGame(this.getGameID());
        } else {
            logger.log(Level.SEVERE, "Player " + username + " is not connected to the game but tried to kill it");
        }
    }
}