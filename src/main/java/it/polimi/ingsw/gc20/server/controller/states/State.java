package it.polimi.ingsw.gc20.server.controller.states;


import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.exceptions.*;
import it.polimi.ingsw.gc20.server.model.cards.Planet;
import it.polimi.ingsw.gc20.server.model.components.AlienColor;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.server.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.server.model.player.Player;
import org.javatuples.Pair;

import java.util.List;

/**
 * Represents an abstract state in the game. This class serves as a base for specific game
 * states and provides a set of methods that can be overridden by subclasses.
 * It encapsulates the behavior relevant to a particular state of the game and
 * ensures state-specific actions are implemented correctly.
 * <p>
 * A state interacts with both the game model and the game controller and encapsulates
 * specific behavior for game phases. State transitions and actions depend on the
 * current state and the rules defined for the game.
 */
public abstract class State{
    private final GameModel model;
    private final GameController controller;
    protected StatePhase phase;
    private String standbyMessage;
    /**
     * Constructs a new State object with the specified GameModel and GameController.
     *
     * @param model       the game model that holds the state data and game entities
     * @param controller  the game controller that manages the interaction logic and user input
     */
    public State(GameModel model, GameController controller) {
        this.model = model;
        this.controller = controller;
    }
    /**
     * Constructs a new State object with the specified GameController.
     *
     * @param controller the game controller that manages the interaction logic and user input
     */
    public State(GameController controller) {
        this.model = controller.getModel();
        this.controller = controller;
    }
    /**
     * Constructs a new State object with the specified GameModel.
     *
     * @param model the game model that holds the state data and game entities
     */
    public State(GameModel model) {
        this.model = model;
        this.controller = null;
    }

    /**
     * Sets the standby message for the current state.
     *
     * @param standbyMessage the message to be displayed during the standby phase
     */
    public void setStandbyMessage(String standbyMessage) {
        this.standbyMessage = standbyMessage;
    }

    /**
     * Retrieves the standby message for the current state.
     *
     * @return the standby message as a String
     */
    public String getStandbyMessage() {
        return standbyMessage;
    }

    /**
     * Retrieves the current phase of the state.
     *
     * @return the current {@code StatePhase} associated with this state
     */
    public StatePhase getPhase() {
        return phase;
    }
    /**
     * Retrieves the game model associated with the current state.
     *
     * @return the {@code GameModel} containing the state data and game entities
     */
    public GameModel getModel() {
        return model;
    }
    /**
     * Retrieves the game controller associated with the current state.
     *
     * @return the {@code GameController} that manages the interaction logic and user input
     */
    public GameController getController() {
        return controller;
    }
    /**
     * Throws an {@code InvalidStateException} to indicate that an action cannot
     * be performed in the current state of the object.
     *
     * @throws InvalidStateException if the operation is not allowed in the
     *                               current state of the object
     */
    private void exception() throws InvalidStateException {
        throw new InvalidStateException("Cannot perform this action in " + this + " state");
    }
    /**
     * Allows a player to take a component from the unviewed components at a specified index.
     * If the operation is not allowed in the current state, an {@code InvalidStateException} is thrown.
     * If the specified component is not found, a {@code ComponentNotFoundException} is thrown.
     *
     * @param player the player attempting to take the component
     * @param index  the index of the component in the unviewed components list
     * @throws InvalidStateException        if the action cannot be performed in the current state
     * @throws ComponentNotFoundException   if the specified component is not found
     */
    public void takeComponentFromUnviewed(Player player, int index) throws InvalidStateException, ComponentNotFoundException {
        exception();
    }
    /**
     * Allows a player to take a component from the list of viewed components at the specified index.
     * If the operation is not allowed in the current state, an {@code InvalidStateException} is thrown.
     * If the specified component is not found, a {@code ComponentNotFoundException} is thrown.
     *
     * @param player the player attempting to take the component
     * @param index  the index of the component in the viewed components list
     * @throws InvalidStateException      if the action cannot be performed in the current state
     * @throws ComponentNotFoundException if the specified component is not found
     */
    public void takeComponentFromViewed(Player player, int index)throws InvalidStateException, ComponentNotFoundException {
        exception();
    }
    /**
     * Allows a player to take a component from the list of booked components at the specified index.
     * If the operation is not allowed in the current state, an {@code InvalidStateException} is thrown.
     * If the specified component is not found, a {@code ComponentNotFoundException} is thrown.
     *
     * @param player the player attempting to take the component
     * @param index  the index of the component in the booked components list
     * @throws InvalidStateException      if the action cannot be performed in the current state
     * @throws ComponentNotFoundException if the specified component is not found
     */
    public void takeComponentFromBooked(Player player, int index) throws InvalidStateException, ComponentNotFoundException {
        exception();
    }
    /**
     * Adds a component to the booked components list for the specified player.
     * If the operation cannot be performed in the current state, an exception is thrown.
     * Additionally, if there is no available space to add the component, a space-related exception is raised.
     *
     * @param player the player attempting to add the component to the booked components
     * @throws InvalidStateException if the action cannot be performed in the current state
     * @throws NoSpaceException if there is no available space to add the component
     */
    public void addComponentToBooked(Player player) throws InvalidStateException, NoSpaceException {
        exception();
    }
    /**
     * Adds a component to the list of viewed components for the specified player.
     * This operation ensures that the component is marked as viewed in the game state.
     *
     * @param player the player for whom the component is being added to the viewed components
     * @throws InvalidStateException if the action cannot be performed in the current state
     * @throws DuplicateComponentException if the component already exists in the viewed components
     */
    public void addComponentToViewed(Player player) throws InvalidStateException, DuplicateComponentException {
        exception();
    }
    /**
     * Places a component on the ship for the specified player at the provided coordinates.
     * This operation may not be allowed in certain game states or if the action violates game rules.
     *
     * @param player      the player attempting to place the component
     * @param coordinates the pair of integers representing the coordinates on the ship where the component is to be placed
     * @throws InvalidStateException      if the action cannot be performed in the current state of the game
     * @throws InvalidTileException       if the specified coordinates are invalid or cannot accept the component
     * @throws ComponentNotFoundException if the component to be placed cannot be found or is unavailable
     */
    public void placeComponent(Player player, Pair<Integer, Integer> coordinates) throws InvalidStateException, InvalidTileException, ComponentNotFoundException{
        exception();
    }
    /**
     * Rotates a component in the game clockwise.
     *
     * @param player the player whose component is to be rotated
     * @throws InvalidStateException if the player's state does not allow this action to be performed
     */
    public void rotateComponentClockwise(Player player) throws InvalidStateException{
        exception();
    }
    /**
     * Rotates the specified player's component counterclockwise.
     *
     * @param player the player whose component is to be rotated
     * @throws InvalidStateException if the operation cannot be performed due to an invalid state
     */
    public void rotateComponentCounterclockwise(Player player) throws InvalidStateException{
        exception();
    }
    /**
     * Stops the assembling process for a given player at the specified position.
     *
     * @param player the player for whom the assembling process should be stopped
     * @param position the position on the board where the player wants to start the game
     * @throws InvalidIndexException if the provided position is out of valid bounds
     * @throws InvalidStateException if the assembling process cannot be stopped due to the current state
     */
    public void stopAssembling(Player player, int position) throws InvalidIndexException, InvalidStateException {
        exception();
    }
    /**
     * Allows a player to peek at a specified deck without removing the cards.
     *
     * @param player The player who is peeking at the deck.
     * @param num The index of the deck to peek at.
     * @throws InvalidIndexException If the specified number exceeds the allowed range.
     * @throws InvalidStateException If the current state does not permit peeking at the deck.
     */
    public void peekDeck(Player player, int num) throws InvalidIndexException, InvalidStateException {
        exception();
    }
    /**
     * Turns the hourglass for the given player. This method may throw exceptions
     * if certain conditions are not met or if an error occurs during execution.
     *
     * @param player the player whose hourglass is being turned
     * @throws HourglassException if there is an issue with the hourglass operation (it cannot be turned)
     * @throws InvalidStateException if the state is invalid for the operation
     */
    public void turnHourglass(Player player) throws HourglassException, InvalidStateException {
        exception();
    }
    /**
     * Validates whether the player's ship meets the required criteria.
     *
     * @param player the Player whose ship is to be validated
     * @throws InvalidStateException if the state of the game or player is invalid for validation
     */
    public void isShipValid(Player player) throws InvalidStateException {
        exception();
    }
    /**
     * Removes a component from the specified player's ship at the given coordinates.
     *
     * @param player the player from whose ship the component will be removed
     * @param coordinates the coordinates on the ship where the component is located
     * @throws InvalidStateException if the game state does not allow the action
     * @throws InvalidTileException if the specified coordinates are invalid
     * @throws ComponentNotFoundException if no component is found at the specified coordinates
     */
    public void removeComp(Player player, Pair<Integer, Integer> coordinates) throws InvalidStateException, InvalidTileException, ComponentNotFoundException {
        exception();
    }
    /**
     * Validates all ships and determines if they are in a correct state.
     * It performs necessary checks and throws an exception if any validation fails.
     *
     * @return true if all ships are validated successfully, false otherwise
     * @throws InvalidStateException if the game state is invalid for validation
     */
    public boolean allShipsValidated() throws InvalidStateException{
        exception();
        return false;
    }
    /**
     * Adds an alien to the specified player's cabin at the given position with the specified color.
     *
     * @param player The player to which the alien will be added.
     * @param color The color of the alien being added.
     * @param cabin The coordinates of the cabin where the alien will be placed.
     * @throws InvalidAlienPlacement If the alien cannot be placed at the specified position.
     * @throws InvalidStateException If the game state does not allow adding an alien at this time.
     * @throws ComponentNotFoundException If a required component for placing the alien is missing.
     */
    public void addAlien(Player player, AlienColor color, Pair<Integer, Integer> cabin) throws InvalidAlienPlacement, InvalidStateException, ComponentNotFoundException {
        exception();
    }
    /**
     * Initializes all ships by adding astronauts to all empty cabins.
     *
     * @throws InvalidStateException if the game state is invalid for initializing ships
     */
    public void initAllShips() throws InvalidStateException {
        exception();
    }
    /**
     * Loads cargo onto the specified player with the given parameters.
     *
     * @param player the player to load the cargo onto
     * @param loaded the color of the cargo to be loaded
     * @param ch a pair of coordinates representing the cargo's location
     * @throws InvalidStateException if the game is in an invalid state for this action
     * @throws InvalidTurnException if the action is attempted outside the player's turn
     * @throws CargoException if a generic cargo-related error occurs
     * @throws CargoNotLoadable if the specified cargo cannot be loaded
     * @throws CargoFullException if the player already has the maximum allowable cargo
     * @throws ComponentNotFoundException if a required component related to cargo loading is not found
     */
    public void loadCargo(Player player, CargoColor loaded, Pair<Integer, Integer> ch) throws InvalidStateException, InvalidTurnException, CargoException, CargoNotLoadable, CargoFullException, ComponentNotFoundException {
        exception();
    }
    /**
     * Unloads cargo of a specified color from a player's possession during the game.
     *
     * @param player The player who is unloading the cargo.
     * @param lost The color of the cargo being unloaded.
     * @param ch A pair representing the coordinates of the cargo to be unloaded.
     * @throws ComponentNotFoundException if a required game component is not found.
     * @throws InvalidStateException if the game state does not allow unloading cargo at this time.
     * @throws InvalidTurnException if the action is attempted outside the player's turn.
     * @throws CargoException if there is an error related to the cargo operation.
     * @throws CargoNotLoadable if the specified cargo cannot be unloaded due to prior constraints.
     * @throws CargoFullException if the cargo operation fails due to being at capacity.
     * @throws InvalidCargoException if the specified cargo is invalid or does not exist.
     */
    public void unloadCargo(Player player, CargoColor lost, Pair<Integer, Integer> ch) throws ComponentNotFoundException, InvalidStateException, InvalidTurnException, CargoException, CargoNotLoadable, CargoFullException, InvalidCargoException {
        exception();
    }
    /**
     * Moves a cargo of the specified color from one location to another.
     *
     * @param player The player performing the cargo movement.
     * @param cargo The color of the cargo to be moved.
     * @param from The initial location of the cargo, represented as a pair of coordinates.
     * @param to The destination location of the cargo, represented as a pair of coordinates.
     * @throws InvalidStateException If the game is in an invalid state for this action.
     * @throws InvalidTurnException If the action is attempted outside the player's turn.
     * @throws CargoException If an error occurs with the cargo during the move.
     * @throws CargoNotLoadable If the cargo cannot be loaded for movement.
     * @throws CargoFullException If the destination is already full and cannot accept more cargo.
     * @throws InvalidCargoException If the cargo specified is invalid.
     * @throws ComponentNotFoundException If a required component for this action is not found.
     */
    public void moveCargo(Player player, CargoColor cargo, Pair<Integer, Integer> from, Pair<Integer, Integer> to) throws InvalidStateException, InvalidTurnException, CargoException, CargoNotLoadable, CargoFullException, InvalidCargoException, ComponentNotFoundException {
        exception();
    }
    /**
     * Reduces the energy level of the given player based on the provided battery details.
     *
     * @param player The player whose energy is to be reduced.
     * @param battery A pair containing two integer values representing the battery coordinates.
     * @throws InvalidStateException If the current state does not permit energy loss.
     * @throws InvalidTurnException If the energy loss is attempted during another player's turn.
     * @throws EnergyException If an error related to energy levels occurs during processing.
     * @throws ComponentNotFoundException If a required component for energy calculation is not found.
     */
    public void loseEnergy(Player player, Pair<Integer, Integer> battery) throws InvalidStateException, InvalidTurnException, EnergyException, ComponentNotFoundException {
        exception();
    }
    /**
     * Allows the specified player to land on a planet identified by the given planet index.
     * This method checks for valid game state and turn before proceeding.
     *
     * @param player      the player attempting to land on the planet
     * @param planetIndex the index of the planet where the player wants to land
     * @throws InvalidStateException if the game is not in a state where landing is allowed
     * @throws InvalidTurnException  if it is not the given player's turn to land on a planet
     */
    public void landOnPlanet(Player player, int planetIndex) throws InvalidStateException, InvalidTurnException {
        exception();
    }
    /**
     * Handles the action of accepting a card during a player's turn in the game.
     *
     * @param player the player who is performing the action
     * @throws InvalidStateException if the game is not in a valid state for this action
     * @throws InvalidTurnException if it is not the player's turn to perform this action
     */
    public void acceptCard(Player player) throws InvalidStateException, InvalidTurnException {
        exception();
    }
    /**
     * Handles the loss of crew members in specified cabins for a given player.
     *
     * @param player The player initiating the action.
     * @param cabins A list of cabin positions represented as pairs of integers where the crew loss is to be applied.
     * @throws InvalidStateException If the game is in an invalid state for this operation.
     * @throws InvalidTurnException If it is not the player's turn to perform this action.
     * @throws EmptyCabinException If the targeted cabin does not contain any crew members.
     * @throws ComponentNotFoundException If a specified component cannot be located.
     */
    public void loseCrew(Player player, List<Pair<Integer, Integer>> cabins) throws InvalidStateException, InvalidTurnException, EmptyCabinException, ComponentNotFoundException {
        exception();
    }
    /**
     * Concludes the move for the given player in the game.
     * Verifies the validity of the current state, turn, and ship before completing the move.
     *
     * @param player the player whose move is being concluded
     * @throws InvalidStateException if the game state is invalid for concluding the move
     * @throws InvalidTurnException if it is not the given player's turn to move
     * @throws InvalidShipException if the player's ship is not valid
     */
    public void endMove(Player player) throws InvalidStateException, InvalidTurnException, InvalidShipException {
        exception();
    }
    /**
     * Activates the specified engines of a player's ship, using energy from specified batteries.
     *
     * @param player the player attempting to activate the engines
     * @param engines a list of engine coordinates represented as pairs of integers
     * @param batteries a list of battery coordinates represented as pairs of integers for providing energy to the engines
     * @throws InvalidStateException if the game state is invalid for this action
     * @throws InvalidTurnException if it is not the player's turn
     * @throws InvalidShipException if the player's ship is not valid
     * @throws InvalidEngineException if the specified engines are invalid
     * @throws EnergyException if there is not enough energy to activate the engines
     * @throws DieNotRolledException if a required die has not been rolled
     * @throws ComponentNotFoundException if specified components (engines or batteries) cannot be found
     */
    public void activateEngines(Player player, List<Pair<Integer, Integer>> engines, List<Pair<Integer, Integer>> batteries) throws InvalidStateException, InvalidTurnException, InvalidShipException, InvalidEngineException, EnergyException, DieNotRolledException, ComponentNotFoundException {
        exception();
    }
    /**
     * Activates the shield for the player's ship using the provided shield and battery components.
     *
     * @param player The player activating the shield.
     * @param shield The shield component to be activated, represented as a pair of integers.
     * @param battery The battery component required to power the shield, represented as a pair of integers.
     * @throws InvalidStateException If the game state does not allow the shield to be activated.
     * @throws InvalidTurnException If it is not the player's turn.
     * @throws InvalidShipException If the player's ship is not valid.
     * @throws EnergyException If there is not enough energy to activate the shield.
     * @throws DieNotRolledException If the player has not rolled the dice required for the action.
     * @throws ComponentNotFoundException If the shield or battery component cannot be found.
     */
    public void activateShield(Player player, Pair<Integer, Integer> shield, Pair<Integer, Integer> battery) throws InvalidStateException, InvalidTurnException, InvalidShipException, EnergyException, DieNotRolledException, ComponentNotFoundException {
        exception();
    }
    /**
     * Activates the specified cannons for the given player in the game.
     *
     * @param player The player who is activating the cannons.
     * @param cannons A list of pairs representing the coordinates of the cannons to be activated.
     * @param batteries A list of pairs representing the coordinates of the batteries linked to the cannons.
     * @throws InvalidStateException If the game is in an invalid state for cannon activation.
     * @throws InvalidTurnException If the action is attempted out of the player's turn.
     * @throws InvalidShipException If the player's ship is not valid.
     * @throws InvalidCannonException If the specified cannon coordinates are invalid or unavailable.
     * @throws EnergyException If the player has not enough energy to activate the cannons.
     * @throws DieNotRolledException If dice roll required for the action has not been completed.
     * @throws ComponentNotFoundException If the specified cannons or batteries cannot be found in the game.
     */
    public void activateCannons(Player player, List<Pair<Integer, Integer>> cannons, List<Pair<Integer, Integer>> batteries) throws InvalidStateException, InvalidTurnException, InvalidShipException, InvalidCannonException, EnergyException, DieNotRolledException, ComponentNotFoundException {
        exception();
    }
    /**
     * Checks if all necessary parts are assembled and ready for use.
     *
     * @return true if all components are completely assembled, false otherwise
     * @throws InvalidStateException if the current state is invalid for performing this check
     */
    public boolean allAssembled() throws InvalidStateException {
        exception();
        return false;
    }
    /**
     * Checks if all ships are ready to fly.
     *
     * @return true if all ships are ready to fly; otherwise, false.
     * @throws InvalidStateException if the current state is invalid for this operation
     */
    public boolean allShipsReadyToFly() throws InvalidStateException {
        exception();
        return false;
    }
    /**
     * Executes an automatic action within the system. This method is designed to
     * perform predefined operations and processes without requiring direct user input.
     *
     * @throws InvalidStateException if the system is in an invalid state, that prevents
     *                               the action from being executed.
     */
    public void automaticAction() throws InvalidStateException {
        exception();
    }
    /**
     * Resumes the operation or process associated with the specified username.
     *
     * @param username the identifier of the user whose operation needs to be resumed
     * @throws InvalidStateException if the operation cannot be resumed due to its current state
     */
    public void resume(String username) throws InvalidStateException {
        exception();
    }
    /**
     * Terminates the current game session. This method enforces the stopping
     * of all game-related processes or states to ensure that the game is
     * properly shut down.
     *
     * @throws InvalidStateException if the game is in a state where termination
     *         is not allowed or not possible.
     */
    public void killGame() throws InvalidStateException {
        exception();
    }
    /**
     * Retrieves the current score or performs an operation related to scoring.
     *
     * @throws InvalidStateException if the operation fails due to an invalid state
     */
    public void getScore() throws InvalidStateException {
        exception();
    }
    /**
     * Allows a player to choose a branch at the specified coordinates during their turn.
     *
     * @param player The player making the branch choice.
     * @param coordinates A pair of integers representing the coordinates of the branch to be chosen.
     * @throws InvalidTurnException If it is not the player's turn to make a move.
     * @throws InvalidStateException If the game is not in a state where a branch can be selected.
     * @throws ComponentNotFoundException If the specified branch does not exist at the given coordinates.
     */
    public void chooseBranch(Player player, Pair<Integer, Integer> coordinates) throws InvalidTurnException, InvalidStateException, ComponentNotFoundException {
        exception();
    }
    /**
     * Sets the current player of the game.
     *
     * @param currentPlayer the name or identifier of the player to set as the current player
     * @throws InvalidStateException if the game state does not allow changing the current player
     */
    public void setCurrentPlayer(String currentPlayer) throws InvalidStateException {
        exception();
    }
    /**
     * Advances the game to the next player's turn. This method updates the
     * current player state and ensures that the game flow moves accordingly.
     *
     * @throws InvalidStateException if the current state of the game does not
     * permit transitioning to the next player.
     */
    public void nextPlayer() throws InvalidStateException{
        exception();
    }
    /**
     * Simulates rolling dice for the given player. This method may trigger various exceptions
     * based on the current game state or the player's turn and status.
     *
     * @param player the player object for whom the dice is being rolled
     * @return the result of the dice roll as an integer value representing the outcome
     * @throws InvalidStateException if the game is not in a valid state to roll the dice
     * @throws InvalidTurnException if it is not the given player's turn
     * @throws InvalidShipException if the player's ship is not valid
     * @throws DieNotRolledException if the dice roll was not performed correctly
     */
    public int rollDice(Player player) throws InvalidStateException, InvalidTurnException, InvalidShipException, DieNotRolledException {
        exception();
        return 0;
    }
    /**
     * Advances the state of the application to the next round.
     *
     * @throws InvalidStateException if the current state does not
     *         allow transitioning to the next round.
     */
    public void nextRound() throws InvalidStateException{
        exception();
    }
    /**
     * Retrieves the name or identifier of the current player in the game.
     *
     * @return The name or identifier of the current player as a String.
     * @throws InvalidStateException If the current state of the game does not allow
     *         determining the current player.
     */
    public String getCurrentPlayer() throws InvalidStateException{
        return null;
    }
    /**
     * Handles the scenario where the current player decides to quit the game.
     *
     * @param playerByID the player object representing the current player who is quitting
     * @throws InvalidTurnException if the method is called when it is not the player's turn
     * @throws InvalidShipException if the player's ship is not valid
     * @throws InvalidStateException if the game is in an invalid state when this method is invoked
     */
    public void currentQuit(Player playerByID) throws InvalidTurnException, InvalidShipException, InvalidStateException {
        exception();
    }

    /**
     * Checks if the operation or process is concurrent.
     *
     * @return true if the state is concurrent, false otherwise
     */
    public boolean isConcurrent() {
        return false;
    }

    /**
     * Allows a user to rejoin a state.
     *
     * @param username the username of the user attempting to rejoin
     * @throws InvalidStateException if the current state does not allow rejoining
     */
    public void rejoin(String username) throws InvalidStateException {
        exception();
    }

    /**
     * Generates a message related to the cannons' choice.
     *
     * @return a string containing the message
     */
    public String createsCannonsMessage(){
        return null;
    }

    /**
     * Creates and returns a message related to engines' choice.
     *
     * @return a string containing the message
     */
    public String createsEnginesMessage(){
        return null;
    }

    /**
     * Creates and returns a shield message.
     *
     * @return the shield message as a string
     */
    public String createsShieldMessage(){
        return null;
    }

    /**
     * Creates a message related to rolling dice.
     *
     * @return A string message representing the outcome or purpose of rolling dice.
     */
    public String createsRollDiceMessage(){
        return null;
    }

    /**
     * Retrieves the crew count of the entity.
     *
     * @return the number of crew members
     */
    public int getCrew(){
        return 0;
    }

    /**
     * Retrieves a message indicating that an automatic action is taking place.
     *
     * @return A string message indicating the occurrence of an automatic action.
     */
    public String getAutomaticActionMessage() {
        return "automatic action is taking place";
    }

    /**
     * Calculates and returns the amount of cargo that needs to be removed.
     *
     * @return the amount of cargo to be removed as an integer
     */
    public int cargoToRemove(){
        return 0;
    }

    /**
     * Determines the reward colors for the cargo based on specific conditions or rules.
     *
     * @return a list of CargoColor objects representing the reward colors.
     */
    public List<CargoColor> cargoReward() {
        return null;
    }

    /**
     * Retrieves a list of planets.
     *
     * @return a list of Planet objects, or an empty list if no planets are found
     */
    public List<Planet> getPlanets() {
        return null;
    }
}