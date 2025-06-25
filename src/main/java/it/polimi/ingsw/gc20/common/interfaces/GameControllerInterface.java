package it.polimi.ingsw.gc20.common.interfaces;

import it.polimi.ingsw.gc20.server.model.components.AlienColor;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import org.javatuples.Pair;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.NoSuchElementException;
/**
 * Interface for the GameController, which handles the game logic and player interactions.
 * This interface is used for remote method invocation (RMI) to allow clients to interact with the game server.
 */
public interface GameControllerInterface extends Remote {

    // Ship assembly

    /**
     * Takes a component from the unviewed pile and adds it to the player's hand
     *
     * @param username Username of the player taking the component
     * @param index Index of the component in the unviewed pile
     */
    void takeComponentFromUnviewed(String username, int index) throws RemoteException;

    /**
     * Takes a component from the viewed pile and adds it to the player's hand
     *
     * @param username Username of the player taking the component
     * @param index Index of the component in the viewed pile
     * @throws IllegalStateException if the game is not in ASSEMBLING state
     * @throws NoSuchElementException if the component is not in the viewed pile
     * @throws IllegalArgumentException if the player already has a component in the hand
     */
    void takeComponentFromViewed(String username, int index) throws RemoteException;

    /**
     * Takes a component previously booked by a player (Level 2 only) and adds it to the player's hand
     *
     * @param username Username of the player taking the component
     * @param index Index of the component in the booked list
     */
    void takeComponentFromBooked(String username, int index) throws RemoteException;

    /**
     * Adds the component in the hand to the player's booked list (Level 2 only)
     *
     * @param username Username of the player booking the component
     */
    void addComponentToBooked(String username) throws RemoteException;

    /**
     * Adds the component in the hand to the viewed pile so other players can see it
     *
     * @param username Username of the player adding the component
     */
    void addComponentToViewed(String username) throws RemoteException;

    /**
     * Places the component in the hand on the player's ship at specified coordinates
     *
     * @param username Username of the player placing the component
     * @param coordinates Coordinates where the component will be placed
     */
    void placeComponent(String username, Pair<Integer, Integer> coordinates) throws RemoteException;

    /**
     * Rotates the component in the hand clockwise
     *
     * @param username Username of the player rotating the component
     */
    void rotateComponentClockwise(String username) throws RemoteException;

    /**
     * Rotates the component in the hand counterclockwise
     *
     * @param username Username of the player rotating the component
     */
    void rotateComponentCounterclockwise(String username) throws RemoteException;

    /**
     * Stops the assembling phase for a player
     * @param username is the username of the player that wants to stop assembling
     * @param position is the relative position on board where the player wants to put their rocket
     * @implNote performs state change to VALIDATING when all players have completed assembling
     */
    void stopAssembling(String username, int position) throws RemoteException;

    /**
     * Peeks at the selected deck
     *
     * @param username Username of the player peeking at the deck
     * @param num number of the deck to peek at
     */
    void peekDeck(String username, int num) throws RemoteException;

    /**
     * Turns the hourglass for a player
     *
     * @param username Username of the player turning the hourglass
     */
    void turnHourglass(String username) throws RemoteException;

    // Ship validating

    /**
     * Removes a component from the player's ship (only during the validating phase and if the ship is not valid)
     *
     * @param username Username of the player removing the component
     * @param coordinates Coordinates of the component to be removed
     * @apiNote view must call this function until the ship is valid
     */
    void removeComponentFromShip(String username, Pair<Integer, Integer> coordinates) throws RemoteException;

    /**
     * Adds an alien to the player's ship
     *
     * @param username Username of the player adding the alien
     * @param color Color of the alien
     * @param cabin Cabin where the alien will be placed (coordinates)
     */
    void addAlien(String username, AlienColor color, Pair<Integer, Integer> cabin) throws RemoteException;

    // Gameplay

    /**
     * To be called when a player needs to choose one of the two branches, a ship is divided into after a fire.
     * @param username is the username of the player that wants to choose a branch
     * @param coordinates are the coordinates of the branch that the player wants to choose
     */
    void chooseBranch(String username, Pair<Integer, Integer> coordinates) throws RemoteException;

    /**
     * method to roll the dice for a player
     * @param username is the username of the player that wants to roll the dice
     */
    void rollDice(String username) throws RemoteException;

    /**
     * Accepts a planet card and lands on the planet
     * @param username is the username of the player that wants to land on the planet
     * @param planetIndex is the index of the planet card in the player's hand
     */
    void landOnPlanet(String username, int planetIndex) throws RemoteException;

    /**
     * Loads cargo onto the player's ship
     * @param username is the username of the player that wants to load the cargo
     * @param loaded the cargo that the player wants to load
     * @param ch are the coordinates of the cargo hold where the player wants to load the cargo
     * @apiNote To be used after accepting a planet, accepting a smuggler, accepting an abandoned station
     */
    void loadCargo(String username, CargoColor loaded, Pair<Integer, Integer> ch) throws RemoteException;

    /**
     * Unloads cargo from the player's ship
     * @param username is the username of the player that wants to unload the cargo
     * @param lost the cargo that the player wants to unload
     * @param ch are the coordinates of the cargo hold where the player wants to unload the cargo
     * @apiNote To be used after accepting a planet, accepting a smuggler,
     * accepting an abandoned station (to unload without limits) or after smugglers, combat zone
     * (to remove the most valuable one)
     */
    void unloadCargo(String username, CargoColor lost, Pair<Integer, Integer> ch) throws RemoteException;

    /**
     * Moves cargo from one cargo hold to another
     * @param username is the username of the player that wants to move the cargo
     * @param cargo is the cargo that the player wants to move?
     * @param from are the coordinates of the cargo hold where the player wants to move the cargo from
     * @param to are the coordinates of the cargo hold where the player wants to move the cargo to
     * @apiNote To be used in losing/gaining cargo
     */
    void moveCargo(String username, CargoColor cargo, Pair<Integer, Integer> from, Pair<Integer, Integer> to) throws RemoteException;
    void acceptCard(String username) throws RemoteException;

    /**
     * To be called when a player wants to lose crew
     * @param username is the username of the player that wants to lose crew
     * @param cabins the list of coordinates of the cabins that the player wants to lose crew from
     */
    void loseCrew(String username, List<Pair<Integer, Integer>> cabins) throws RemoteException;

    /**
     * To be called when a player terminates their turn. Based on the type of card, the game will move to the next player or the next phase
     * @param username is the username of the player that wants to terminate their turn
     */
    void endMove(String username) throws RemoteException;

    /**
     * Handles the event of a player giving up
     * @param username is the username of the player that wants to give up
     */
    void giveUp(String username) throws RemoteException;

    /**
     * This method is called when a player loses energy
     * @param username is the username of the player that wants to lose energy
     * @param coordinates are the coordinates of the component where the player wants to lose energy
     */
    void loseEnergy(String username, Pair<Integer, Integer> coordinates) throws RemoteException;

    // Activate ship components

    /**
     * To be called when a player activates their engines
     * @param username is the username of the player that wants to activate their engines
     * @param engines the list of coordinates of the engines that the player wants to activate
     * @param batteries the list of coordinates of the batteries that the player wants to use to activate the engines
     */
    void activateEngines(String username, List<Pair<Integer, Integer>> engines, List<Pair<Integer, Integer>> batteries) throws RemoteException;

    /**
     * To be called when a light_fire/meteor is firing the player
     * @param username is the username of the player that wants to activate the shield
     * @param shield are the coordinates of the shield component that the player wants to activate
     * @param battery are the coordinates of the energy component that the player wants to use to activate the shield
     * @apiNote Ship may need to be validated
     */
    void activateShield(String username, Pair<Integer, Integer> shield, Pair<Integer, Integer> battery) throws RemoteException;

    /**
     * To be called when a player wants to activate their cannons
     * @param username is the username of the player that wants to activate their cannons
     * @param cannons the list of coordinates of the cannons that the player wants to activate
     * @param batteries the list of coordinates of the batteries that the player wants to use to activate the cannons
     */
    void activateCannons(String username, List<Pair<Integer, Integer>> cannons, List<Pair<Integer, Integer>> batteries) throws RemoteException;
}