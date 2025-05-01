package it.polimi.ingsw.gc20.client.network.socket;

import it.polimi.ingsw.gc20.common.interfaces.GameControllerInterface;
import it.polimi.ingsw.gc20.server.model.components.AlienColor;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import org.javatuples.Pair;

import java.rmi.RemoteException;
import java.util.List;

public class SocketGameController implements GameControllerInterface {

    @Override
    public void killGame(String username) throws RemoteException {

    }

    @Override
    public void takeComponentFromUnviewed(String username, int index) throws RemoteException {

    }

    @Override
    public void takeComponentFromViewed(String username, int index) throws RemoteException {

    }

    @Override
    public void takeComponentFromBooked(String username, int index) throws RemoteException {

    }

    @Override
    public void addComponentToBooked(String username) throws RemoteException {

    }

    @Override
    public void addComponentToViewed(String username) throws RemoteException {

    }

    @Override
    public void placeComponent(String username, Pair<Integer, Integer> coordinates) throws RemoteException {

    }

    @Override
    public void rotateComponentClockwise(String username) throws RemoteException {

    }

    @Override
    public void rotateComponentCounterclockwise(String username) throws RemoteException {

    }

    @Override
    public void stopAssembling(String username, int position) throws RemoteException {

    }

    @Override
    public void peekDeck(String username, int num) throws RemoteException {

    }

    @Override
    public void turnHourglass(String username) throws RemoteException {

    }

    @Override
    public void validateShip(String username) throws RemoteException {

    }

    @Override
    public void removeComponentFromShip(String username, Pair<Integer, Integer> coordinates) throws RemoteException {

    }

    @Override
    public void addAlien(String username, AlienColor color, Pair<Integer, Integer> cabin) throws RemoteException {

    }

    @Override
    public void readyToFly(String username) throws RemoteException {

    }

    @Override
    public void chooseBranch(String username, Pair<Integer, Integer> coordinates) throws RemoteException {

    }

    @Override
    public void rollDice(String username) throws RemoteException {

    }

    @Override
    public void landOnPlanet(String username, int planetIndex) throws RemoteException {

    }

    @Override
    public void loadCargo(String username, CargoColor loaded, Pair<Integer, Integer> ch) throws RemoteException {

    }

    @Override
    public void unloadCargo(String username, CargoColor lost, Pair<Integer, Integer> ch) throws RemoteException {

    }

    @Override
    public void moveCargo(String username, CargoColor cargo, Pair<Integer, Integer> from, Pair<Integer, Integer> to) throws RemoteException {

    }

    @Override
    public void acceptCard(String username) throws RemoteException {

    }

    @Override
    public void loseCrew(String username, List<Pair<Integer, Integer>> cabins) throws RemoteException {

    }

    @Override
    public void endMove(String username) throws RemoteException {

    }

    @Override
    public void shootEnemy(String username, List<Pair<Integer, Integer>> cannons, List<Pair<Integer, Integer>> batteries) throws RemoteException {

    }

    @Override
    public void giveUp(String username) throws RemoteException {

    }

    @Override
    public void activateEngines(String username, List<Pair<Integer, Integer>> engines, List<Pair<Integer, Integer>> batteries) throws RemoteException {

    }

    @Override
    public void activateShield(String username, Pair<Integer, Integer> shield, Pair<Integer, Integer> battery) throws RemoteException {

    }

    @Override
    public void activateCannons(String username, List<Pair<Integer, Integer>> cannons, List<Pair<Integer, Integer>> batteries) throws RemoteException {

    }
}
