package it.polimi.ingsw.gc20.common.interfaces;

import it.polimi.ingsw.gc20.server.model.components.AlienColor;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import org.javatuples.Pair;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

@SuppressWarnings("unused")
public interface GameControllerInterface extends Remote {
    void killGame(String username) throws RemoteException;

    // Ship assembly
    void takeComponentFromUnviewed(String username, int index) throws RemoteException;
    void takeComponentFromViewed(String username, int index) throws RemoteException;
    void takeComponentFromBooked(String username, int index) throws RemoteException;
    void addComponentToBooked(String username) throws RemoteException;
    void addComponentToViewed(String username) throws RemoteException;
    void placeComponent(String username, Pair<Integer, Integer> coordinates) throws RemoteException;

    void rotateComponentClockwise(String username) throws RemoteException;
    void rotateComponentCounterclockwise(String username) throws RemoteException;

    void stopAssembling(String username, int position) throws RemoteException;
    void peekDeck(String username, int num) throws RemoteException;
    void turnHourglass(String username) throws RemoteException;

    // Ship validating
    void validateShip(String username) throws RemoteException;
    void removeComponentFromShip(String username, Pair<Integer, Integer> coordinates) throws RemoteException;
    void addAlien(String username, AlienColor color, Pair<Integer, Integer> cabin) throws RemoteException;
    void readyToFly(String username) throws RemoteException;

    // Gameplay
    void chooseBranch(String username, Pair<Integer, Integer> coordinates) throws RemoteException;
    void rollDice(String username) throws RemoteException;
    void landOnPlanet(String username, int planetIndex) throws RemoteException;
    void loadCargo(String username, CargoColor loaded, Pair<Integer, Integer> ch) throws RemoteException;
    void unloadCargo(String username, CargoColor lost, Pair<Integer, Integer> ch) throws RemoteException;
    void moveCargo(String username, CargoColor cargo, Pair<Integer, Integer> from, Pair<Integer, Integer> to) throws RemoteException;
    void acceptCard(String username) throws RemoteException;
    void loseCrew(String username, List<Pair<Integer, Integer>> cabins) throws RemoteException;
    void endMove(String username) throws RemoteException;
    void shootEnemy(String username, List<Pair<Integer, Integer>> cannons, List<Pair<Integer, Integer>> batteries) throws RemoteException;
    void giveUp(String username) throws RemoteException;

    // Activate ship components
    void activateEngines(String username, List<Pair<Integer, Integer>> engines, List<Pair<Integer, Integer>> batteries) throws RemoteException;
    void activateShield(String username, Pair<Integer, Integer> shield, Pair<Integer, Integer> battery) throws RemoteException;
    void activateCannons(String username, List<Pair<Integer, Integer>> cannons, List<Pair<Integer, Integer>> batteries) throws RemoteException;
}