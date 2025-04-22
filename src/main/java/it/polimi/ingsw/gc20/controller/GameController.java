package it.polimi.ingsw.gc20.controller;

import it.polimi.ingsw.gc20.controller.states.*;
import it.polimi.ingsw.gc20.exceptions.*;
import it.polimi.ingsw.gc20.model.cards.*;
import it.polimi.ingsw.gc20.model.components.*;
import it.polimi.ingsw.gc20.model.gamesets.*;
import it.polimi.ingsw.gc20.model.player.*;
import it.polimi.ingsw.gc20.interfaces.*;
import org.javatuples.Pair;

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
            handleException(e);
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
            handleException(e);
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
            handleException(e);
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
            handleException(e);
        }
        return null;
    }


    /**
     * Handles the card drawing phase of the game
     */
    public void drawCard() throws EmptyDeckException {
        try{
            try{
                AdventureCard card = model.drawCard();
                card.setState(this, model);
            }
            catch (EmptyDeckException e) {
                state = new EndgameState(state.getController());
            }
        } catch(Exception e){
            handleException(e);
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
            handleException(e);
        }
        return null;
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
        try{
            state.landOnPlanet(getPlayerByID(username), planetIndex);
        }
        catch (Exception e) {
            handleException(e);
        }

    }

    /**
     * Shoots an enemy (pirates, slavers, smugglers)
     * @param username is the username of the player that wants to shoot the enemy
     * @param cannons is the list of coordinates of the cannons that the player wants to use to shoot
     * @param batteries is the list of coordinates of the batteries that the player wants to use to shoot
     * @throws IllegalStateException if the game is not in the shooting phase
     * @throws InvalidTurnException if it is not the player's turn
     */
    @Override
    public void shootEnemy(String username, List<Pair<Integer, Integer>> cannons, List<Pair<Integer, Integer>> batteries) throws IllegalStateException, InvalidTurnException, EmptyDeckException, InvalidCannonException, EnergyException {
        try{
            state.shootEnemy(getPlayerByID(username), cannons, batteries);
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * Loads cargo onto the player's ship
     * @param username is the username of the player that wants to load the cargo
     * @param loaded is the cargo that the player wants to load
     * @param ch are the coordinates of the cargo hold where the player wants to load the cargo
     * @throws IllegalStateException if the game is not in the cargo loading phase
     * @throws InvalidTurnException if it is not the player's turn
     * @apiNote To be used after accepting a planet, accepting a smuggler, accepting an abandoned station
     */
    @Override
    public void loadCargo(String username, CargoColor loaded, Pair<Integer, Integer> ch) throws InvalidTurnException, CargoException, CargoNotLoadable, CargoFullException {
        try {
            Player player = getPlayerByID(username);
            state.loadCargo(player, loaded, ch);
        } catch (Exception e){
            handleException(e);
        }
    }

    /**
     * Unloads cargo from the player's ship
     * @param username is the username of the player that wants to unload the cargo
     * @param lost is the cargo that the player wants to unload
     * @param ch are the coordinates of the cargo hold where the player wants to unload the cargo
     * @throws IllegalStateException if the game is not in the cargo unloading phase
     * @throws InvalidTurnException if it is not the player's turn
     * @apiNote To be used after accepting a planet, accepting a smuggler, accepting an abandoned station (to unload without limits) or after smugglers, combat zone (to remove most valuable one)
     */
    @Override
    public void unloadCargo(String username, CargoColor lost, Pair<Integer, Integer> ch) throws InvalidTurnException, CargoException, CargoNotLoadable, CargoFullException, InvalidCargoException {
        try{
            state.unloadCargo(getPlayerByID(username), lost, ch);
        } catch (Exception e){
            handleException(e);
        }
    }

    /**
     * Moves cargo from one cargo hold to another
     * @param username is the username of the player that wants to move the cargo
     * @param cargo is the cargo that the player wants to move
     * @param from are the coordinates of the cargo hold where the player wants to move the cargo from
     * @param to are the coordinates of the cargo hold where the player wants to move the cargo to
     * @throws IllegalStateException if the game is not in the cargo moving phase
     * @throws InvalidTurnException if it is not the player's turn
     * @apiNote To be used in losing/gaining cargo
     */
    @Override
    public void moveCargo(String username, CargoColor cargo, Pair<Integer, Integer> from, Pair<Integer, Integer> to) throws InvalidTurnException, CargoException, CargoNotLoadable, CargoFullException, InvalidCargoException {
        try{
            state.moveCargo(getPlayerByID(username), cargo, from, to);
        } catch (Exception e){
            handleException(e);
        }
    }

    /**
     * Accepts a card and performs the action associated with it
     * @param username is the username of the player that wants to accept the card
     * @throws IllegalStateException if the game is not in the card phase
     * @throws InvalidTurnException if it is not the player's turn
     */
    @Override
    public void acceptCard(String username) throws IllegalStateException, InvalidTurnException, EmptyDeckException {
        try{
            state.acceptCard(getPlayerByID(username));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int getOnlinePlayers() {
        try{
            return connectedPlayers.size();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * To be called when a player wants to activate their cannons
     * @param username is the username of the player that wants to activate their cannons
     * @param cannons is the list of coordinates of the cannons that the player wants to activate
     * @param batteries is the list of coordinates of the batteries that the player wants to use to activate the cannons
     * @throws IllegalStateException if the game is not in the cannon activation phase
     * @throws InvalidTurnException if it is not the player's turn
     */
    @Override
    public void activateCannons(String username, List<Pair<Integer, Integer>> cannons, List<Pair<Integer, Integer>> batteries) throws InvalidTurnException, InvalidShipException, InvalidCannonException, EnergyException, EmptyDeckException {
        try{
            state.activateCannons(getPlayerByID(username), cannons, batteries);
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * @return the score of the game
     * @throws IllegalStateException if the game is not in the endgame phase
     */
    @Override
    public Map<String, Integer> getScore() throws IllegalStateException {
        try{
            return state.getScore();
        } catch (Exception e) {
            handleException(e);
        }
        return null;
    }

    /**
     * To be called when a player wants to lose crew
     * @param username is the username of the player that wants to lose crew
     * @param cabins is the list of coordinates of the cabins that the player wants to lose crew from
     * @throws IllegalStateException if the game is not in the correct phase
     * @throws InvalidTurnException if it is not the player's turn
     */
    @Override
    public void loseCrew(String username, List<Pair<Integer, Integer>> cabins) throws InvalidTurnException, EmptyDeckException, EmptyCabinException {
        try{
            state.loseCrew(getPlayerByID(username), cabins);
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * To be called when a light_fire/meteor is firing the player
     * @param username is the username of the player that wants to activate the shield
     * @param shieldComp are the coordinates of the shield component that the player wants to activate
     * @param batteryComp are the coordinates of the energy component that the player wants to use to activate the shield
     * @throws IllegalStateException if the game is not in the meteor swarm phase
     * @throws IllegalArgumentException if it is not the player's turn
     * @apiNote Ship may need to be validated
     */
    public void activateShield(String username, Pair<Integer, Integer> shieldComp, Pair<Integer, Integer> batteryComp) throws InvalidTurnException, InvalidShipException, EmptyDeckException, DeadAlienException, DieNotRolledException, EnergyException {
        try{
            state.activateShield(getPlayerByID(username), shieldComp, batteryComp);
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * Returns the active adventure card
     * @return the active adventure card
     */
    @Override
    public AdventureCard getActiveCard() {
        try{
            return model.getActiveCard();
        } catch (Exception e) {
            handleException(e);
        }
        return null;
    }

    /**
     * To be called when a player terminates their turn. Based on the type of card, the game will move to the next player or the next phase
     * @param username is the username of the player that wants to terminate their turn
     * @throws IllegalStateException if the game is not in the card phase
     * @throws InvalidTurnException if it is not the player's turn
     */
    public void endMove(String username) throws InvalidTurnException, InvalidShipException, EmptyDeckException {
        try{
            state.endMove(getPlayerByID(username));
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * To be called when a player activates their engines
     * @param username is the username of the player that wants to activate their engines
     * @param engines is the list of coordinates of the engines that the player wants to activate
     * @param batteries is the list of coordinates of the batteries that the player wants to use to activate the engines
     * @throws IllegalStateException if the game is not in the engine activation phase
     * @throws InvalidTurnException if it is not the player's turn
     * @throws InvalidShipException if the ship is not valid
     */
    @Override
    public void activateEngines(String username, List<Pair<Integer, Integer>> engines, List<Pair<Integer, Integer>> batteries) throws InvalidTurnException, InvalidShipException, InvalidEngineException, EnergyException, DeadAlienException, DieNotRolledException, EmptyDeckException {
        try{
            state.activateEngines(getPlayerByID(username), engines, batteries);
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * To be called when a player needs to chose one of the two branches a ship is divided into after a fire.
     * @param username is the username of the player that wants to choose a branch
     * @param coordinates are the coordinates of the branch that the player wants to choose
     * @throws InvalidTurnException if it is not the player's turn
     * @throws InvalidShipException if the ship is not valid (in case a new heavyFire is shot right after choosing)
     */
    @Override
    public void chooseBranch(String username, Pair<Integer, Integer> coordinates) throws InvalidTurnException, InvalidShipException, EmptyDeckException {
        try{
            state.chooseBranch(getPlayerByID(username), coordinates);
        } catch (Exception e) {
            handleException(e);
        }
    }


    /**
     * @param asker is the username of the player asking for data
     * @param asked is the username of the player being asked for data
     * @return player data, if the asker is the same as the asked, return the full data, otherwise return only public data
     */
    @Override
    public Player getPlayerData(String asker, String asked) {
        try{
            if (asker.equals(asked)) {
                return getPlayerByID(asker);
            } else {
                return getPlayerByID(asked).getPublicData();
            }
        } catch (Exception e) {
            handleException(e);
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
            handleException(e);
        }
        return null;
    }

    /**
     * Handles the event of a player giving up
     * @param username is the username of the player that wants to give up
     */
    @Override
    public void giveUp(String username) {
        //TODO
        try {
            model.giveUp(getPlayerByID(username));
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * Handles a player leaving the game mid-session
     *
     * @param username Username of the exiting player
     * @throws IllegalArgumentException if player was not in game or not connected
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
            handleException(e);
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
            handleException(e);
        }
        return false;
    }

    /**
     * Retrieves player data by username
     *
     * @param username Username of the player
     * @return Player object containing player data
     * @throws IllegalArgumentException if the player is not found
     */
    public Player getPlayerByID(String username){
        try{
            return model.getGame().getPlayers()
                    .stream()
                    .filter(player -> player.getUsername().equals(username))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Player not found"));
        } catch (Exception e) {
            handleException(e);
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
            handleException(e);
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
            handleException(e);
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
            handleException(e);
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
            handleException(e);
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
            handleException(e);
        }
    }

    /**
     * Takes a component that was previously booked by a player (Level 2 only) and adds it to the player's hand
     *
     * @param username Username of the player taking the component
     * @param index Index of the component in the booked list
     * @throws IllegalStateException if game is not in ASSEMBLING state or not level 2
     * @throws IllegalArgumentException if the component is not in the player's booked list
     * @throws IllegalArgumentException if the player already has a component in hand
     */
    public void takeComponentFromBooked(String username, int index) {
        try{
            if (model.getLevel() != 2) {
                throw new IllegalStateException("Booking components is only available in level 2 games");
            }
            state.takeComponentFromBooked(getPlayerByID(username), index);
            // TODO: notify players of component taken
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * Adds the component in hand to player's booked list (Level 2 only)
     *
     * @param username Username of the player booking the component
     * @throws IllegalStateException if game is not in ASSEMBLING state or not level 2
     * @throws IllegalArgumentException if there aren't enough available spaces in the player's booked list
     */
    public void addComponentToBooked(String username) throws NoSpaceException {
        try{
            if (model.getLevel() != 2) {
                throw new IllegalStateException("Booking components is only available in level 2 games");
            }
            state.addComponentToBooked(getPlayerByID(username));
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * Adds the component in hand to the viewed pile so other players can see it
     *
     * @param username Username of the player adding the component
     * @throws IllegalStateException if game is not in ASSEMBLING state
     */
    public synchronized void addComponentToViewed(String username) throws DuplicateComponentException {
        try{
            state.addComponentToViewed(getPlayerByID(username));
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * Places the component in hand on the player's ship at specified coordinates
     *
     * @param username Username of the player placing the component
     * @param coordinates Coordinates where the component will be placed
     * @throws IllegalStateException if game is not in ASSEMBLING state
     * @throws InvalidParameterException if placement is invalid (handled by ship implementation)
     * @throws IllegalArgumentException if the player does not have a component in hand
     */
    public void placeComponent(String username, Pair<Integer, Integer> coordinates) {
        try{
            state.placeComponent(getPlayerByID(username), coordinates);
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * Rotates the component in hand clockwise
     *
     * @param username Username of the player rotating the component
     * @throws IllegalStateException if game is not in ASSEMBLING state
     */
    public void rotateComponentClockwise(String username) {
        try{
            state.rotateComponentClockwise(getPlayerByID(username));
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * Rotates the component in hand counterclockwise
     *
     * @param username Username of the player rotating the component
     * @throws IllegalStateException if game is not in ASSEMBLING state
     */
    public void rotateComponentCounterclockwise(String username) {
        try{
            state.rotateComponentCounterclockwise(getPlayerByID(username));
        } catch (Exception e) {
            handleException(e);
        }
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
    public void removeComponentFromShip(String username, Component component) throws DeadAlienException {
        try{
            state.removeComp(getPlayerByID(username), component);
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * Validates the player's ship
     *
     * @param username Username of the player that wants to validate the ship
     * @throws IllegalStateException if game is not in VALIDATING state
     * @apiNote this function must be called in a loop from the view until the ship is valid
     * @see #removeComponentFromShip(String, Component)
     * @implNote this function will also draw a card if all players have valid ships (and will change the state)
     */
    public void validateShip(String username) {
        try{
            state.isShipValid(getPlayerByID(username));
            // TODO: notify players of ship validation
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * Stops the assembling phase for a player
     * @param username is the username of the player that wants to stop assembling
     * @param position is the relative position on board where the player wants to put their rocket
     * @throws IllegalStateException if game is not in ASSEMBLING state
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
            handleException(e);
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
        try{
            if (isPlayerDisconnected(username)) {
                throw new IllegalArgumentException("Cannot turn hourglass for not connected player");
            }
            if (model.getLevel() != 2) {
                throw new IllegalStateException("Hourglass is only available in level 2 games");
            }
            state.turnHourglass(getPlayerByID(username));
        } catch (Exception e) {
            handleException(e);
        }
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
        try {
            if (model.getLevel() != 2) {
                throw new IllegalStateException("Hourglass is only available in level 2 games");
            }
            if (isPlayerDisconnected(username)) {
                throw new IllegalArgumentException("Player disconnected");
            }
            return state.getHourglassTime(getPlayerByID(username));
        } catch (Exception e) {
            handleException(e);
        }
        return 0;
    }

    /**
     * Peeks at the selected deck
     *
     * @param username Username of the player peeking at the deck
     * @param num number of the deck to peek at
     * @throws IllegalStateException if game is not in ASSEMBLING state
     * @throws IllegalArgumentException if player is not connected or has already completed assembling
     * @throws IllegalArgumentException if the game is not in level 2
     */
    public void peekDeck(String username, int num) throws InvalidIndexException {
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
            handleException(e);
        }
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
    public void addAlien(String username, AlienColor color, Cabin cabin) throws InvalidAlienPlacement {
        try{
            if(model.getLevel() != 2){
                throw new IllegalArgumentException("Aliens are only available in level 2 games");
            }
            state.addAlien(getPlayerByID(username), color, cabin);
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * Sets the player to be ready to fly
     * @param username is the username of the player that wants to fly
     * @throws IllegalStateException if game is not in VALIDATING state
     * @throws IllegalArgumentException if the player's ship is not valid
     * @apiNote be careful to add aliens before calling this function
     */
    public void readyToFly(String username) throws EmptyDeckException {
        try{
            state.readyToFly(getPlayerByID(username));
            if (state.allShipsReadyToFly()) {
                state.initAllShips();
                model.createDeck();
                drawCard();
                //TODO: notify players of state change
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    public void defeated(String username) {
        try{
            model.giveUp(getPlayerByID(username));
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * @param username is the username of the player that wants to roll the dice
     * @throws IllegalStateException if the game is not in the correct phase
     * @throws InvalidTurnException  if it is not the player's turn
     */
    @Override
    public void rollDice(String username) throws IllegalStateException, InvalidTurnException, InvalidShipException, EmptyDeckException, DeadAlienException, DieNotRolledException {
        try{
            state.rollDice(getPlayerByID(username));
        } catch (Exception e) {
            handleException(e);
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
            handleException(e);
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
            handleException(e);
        }
        return null;
    }

    /**
     * Manage Errors
     * @param e exception
     */
    public void handleException(Exception e) {
        // Log dell'errore con dettagli
        System.err.println("Errore rilevato: " + e.getMessage());

        // Gestione specifica in base al tipo di eccezione
        if (e instanceof EmptyDeckException) {
            System.err.println("Mazzo di carte vuoto: il gioco sta terminando");
            state = new EndgameState(this);
        } else if (e instanceof InvalidTurnException) {
            System.err.println("Turno non valido: azione eseguita dal giocatore sbagliato");
        } else if (e instanceof InvalidShipException) {
            System.err.println("Nave non valida: la nave contiene errori di configurazione");
        } else if (e instanceof InvalidCannonException) {
            System.err.println("Cannone non valido: impossibile attivare questo cannone");
        } else if (e instanceof InvalidEngineException) {
            System.err.println("Motore non valido: impossibile attivare questo motore");
        } else if (e instanceof EnergyException) {
            System.err.println("Energia insufficiente: non ci sono abbastanza batterie");
        } else if (e instanceof CargoException) {
            System.err.println("Errore di carico generico");
        } else if (e instanceof CargoNotLoadable) {
            System.err.println("Carico non caricabile: tipo di carico non valido");
        } else if (e instanceof CargoFullException) {
            System.err.println("Vano di carico pieno: impossibile caricare altro");
        } else if (e instanceof InvalidCargoException) {
            System.err.println("Carico non valido: operazione non consentita");
        } else if (e instanceof EmptyCabinException) {
            System.err.println("Cabina vuota: richiesta azione su cabina senza equipaggio");
        } else if (e instanceof DeadAlienException) {
            System.err.println("Alieno morto: impossibile eseguire l'azione richiesta");
        } else if (e instanceof DieNotRolledException) {
            System.err.println("Dado non lanciato: necessario lanciare il dado prima");
        } else if (e instanceof NoSpaceException) {
            System.err.println("Spazio insufficiente: impossibile aggiungere altri componenti");
        } else if (e instanceof DuplicateComponentException) {
            System.err.println("Componente duplicato: questo componente esiste già");
        } else if (e instanceof HourglassException) {
            System.err.println("Errore clessidra: operazione non valida");
        } else if (e instanceof InvalidAlienPlacement) {
            System.err.println("Posizionamento alieno non valido: posizione non consentita");
        } else if (e instanceof InvalidIndexException) {
            System.err.println("Indice non valido: valore fuori intervallo");
        } else if (e instanceof IllegalStateException) {
            System.err.println("Stato non valido: operazione non consentita nello stato attuale");
        } else if (e instanceof IllegalArgumentException) {
            System.err.println("Argomento non valido: parametro errato");
        } else if (e instanceof NoSuchElementException) {
            System.err.println("Elemento non trovato: componente o risorsa inesistente");
        } else {
            System.err.println("Errore generico non categorizzato");
            e.printStackTrace();
        }

        // Potenziale notifica ai client (da implementare)
        // notifyClientsOfError(e.getMessage());
    }
}