package it.polimi.ingsw.gc20.interfaces;

import it.polimi.ingsw.gc20.exceptions.*;
import it.polimi.ingsw.gc20.model.cards.*;
import it.polimi.ingsw.gc20.model.components.*;
import it.polimi.ingsw.gc20.model.player.*;
import it.polimi.ingsw.gc20.model.gamesets.*;
import org.javatuples.Pair;

import java.rmi.Remote;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public interface GameControllerInterface extends Remote {

    // Player management
    void giveUp(String username);
    void disconnectPlayer(String username);
    boolean reconnectPlayer(String username);
    List<String> getAllUsernames();
    List<String> getDisconnectedPlayers();
    boolean isPlayerDisconnected(String username);


    Player getPlayerData(String asker, String asked);

    // Game state
    String getState();
    Map<String, Integer> getPlayerScores();

    // Ship assembly
    void takeComponentFromUnviewed(String username, int index);
    void takeComponentFromViewed(String username, int index);
    void takeComponentFromBooked(String username, int index);
    void addComponentToBooked(String username) throws NoSpaceException;
    void addComponentToViewed(String username) throws DuplicateComponentException;
    void placeComponent(String username, Pair<Integer, Integer> coordinates);

    void rotateComponentClockwise(String username);
    void rotateComponentCounterclockwise(String username);

    void stopAssembling(String username, int position);
    void peekDeck(String username, int num) throws InvalidIndexException;
    int getHourglassTime(String username);
    void turnHourglass(String username) throws HourglassException;

    // Ship validating
    void validateShip(String username);
    void removeComponentFromShip(String username, Component component) throws DeadAlienException;
    void addAlien(String username, AlienColor color, Cabin cabin) throws InvalidAlienPlacement;
    void readyToFly(String username) throws EmptyDeckException;

    // Gameplay
    void chooseBranch(String username, Pair<Integer, Integer> coordinates) throws InvalidTurnException, InvalidShipException, EmptyDeckException;
    void rollDice(String username) throws IllegalStateException, InvalidTurnException, InvalidShipException, EmptyDeckException, DeadAlienException, DieNotRolledException;
    AdventureCard getActiveCard();
    void landOnPlanet(String username, int planetIndex) throws IllegalStateException, InvalidTurnException;
    void loadCargo(String username, CargoColor loaded, Pair<Integer, Integer> ch) throws IllegalStateException, InvalidTurnException, CargoException, CargoNotLoadable, CargoFullException;
    void unloadCargo(String username, CargoColor lost, Pair<Integer, Integer> ch) throws IllegalStateException, InvalidTurnException, CargoException, CargoNotLoadable, CargoFullException, InvalidCargoException;
    void moveCargo(String username, CargoColor cargo, Pair<Integer, Integer> from, Pair<Integer, Integer> to) throws IllegalStateException, InvalidTurnException, CargoException, CargoNotLoadable, CargoFullException, InvalidCargoException;
    void acceptCard(String username) throws IllegalStateException, InvalidTurnException, EmptyDeckException;
    void loseCrew(String username, List<Pair<Integer, Integer>> cabins) throws IllegalStateException, InvalidTurnException, EmptyDeckException, EmptyCabinException;
    void endMove(String username) throws IllegalStateException, InvalidTurnException, InvalidShipException, EmptyDeckException;
    void shootEnemy(String username, List<Pair<Integer, Integer>> cannons, List<Pair<Integer, Integer>> batteries) throws IllegalStateException, InvalidTurnException, InvalidShipException, EmptyDeckException, InvalidCannonException, EnergyException;

    // Activate ship components
    void activateEngines(String username, List<Pair<Integer, Integer>> engines, List<Pair<Integer, Integer>> batteries) throws IllegalStateException, InvalidTurnException, InvalidShipException, InvalidEngineException, EnergyException, DeadAlienException, DieNotRolledException, EmptyDeckException;
    void activateShield(String username, Pair<Integer, Integer> shield, Pair<Integer, Integer> battery) throws IllegalStateException, InvalidTurnException, InvalidShipException, EmptyDeckException, DeadAlienException, DieNotRolledException, EnergyException;
    void activateCannons(String username, List<Pair<Integer, Integer>> cannons, List<Pair<Integer, Integer>> batteries) throws IllegalStateException, InvalidTurnException, InvalidShipException, InvalidCannonException, EnergyException, EmptyDeckException;

    // Game end
    Map<String, Integer> getScore() throws IllegalStateException;
}