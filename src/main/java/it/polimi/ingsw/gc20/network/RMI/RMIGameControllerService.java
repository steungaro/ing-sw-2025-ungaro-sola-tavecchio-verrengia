package it.polimi.ingsw.gc20.network.RMI;

import it.polimi.ingsw.gc20.interfaces.GameControllerInterface;
import it.polimi.ingsw.gc20.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.model.components.*;
import it.polimi.ingsw.gc20.model.gamesets.*;
import it.polimi.ingsw.gc20.model.player.*;
import it.polimi.ingsw.gc20.network.common.QueueHandler;
import it.polimi.ingsw.gc20.network.message_protocol.toserver.Message;
import it.polimi.ingsw.gc20.network.message_protocol.toserver.game.*;
import org.javatuples.Pair;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RMIGameControllerService extends UnicastRemoteObject implements GameControllerInterface {
    private static final Logger LOGGER = Logger.getLogger(RMIGameControllerService.class.getName());
    private final QueueHandler queueHandler;

    public RMIGameControllerService() throws RemoteException {
        super();
        this.queueHandler = QueueHandler.getInstance();
    }

    @Override
    public void giveUp(String username) throws RemoteException {
        LOGGER.fine("Received RMI call: giveUp from " + username);
        queueHandler.enqueue(new GiveUpMessage(username, "giveUp"));
    }


    @Override
    public void getPlayerData(String asker, String asked) throws RemoteException {
        LOGGER.fine("Received RMI call: getPlayerData from " + asker + " to " + asked);
        queueHandler.enqueue(new GetPlayerDataMessage(asker, asked));
    }

    @Override
    public String getState() throws RemoteException {
        return "";
    }

    @Override
    public Map<String, Integer> getPlayerScores() throws RemoteException {
        return Map.of();
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
    public int getHourglassTime(String username) throws RemoteException {
        return 0;
    }

    @Override
    public void turnHourglass(String username) throws RemoteException {

    }

    @Override
    public void validateShip(String username) throws RemoteException {

    }

    @Override
    public void removeComponentFromShip(String username, Component component) throws RemoteException {

    }

    @Override
    public void addAlien(String username, AlienColor color, Cabin cabin) throws RemoteException {

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
    public AdventureCard getActiveCard() throws RemoteException {
        return null;
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
    public void activateEngines(String username, List<Pair<Integer, Integer>> engines, List<Pair<Integer, Integer>> batteries) throws RemoteException {

    }


    @Override
    public void activateShield(String username, Pair<Integer, Integer> shield, Pair<Integer, Integer> battery) throws RemoteException {
        LOGGER.fine("Received RMI call: activateShield from " + username);
        queueHandler.enqueue(new ActivateShieldMessage(username, shield, battery));
    }

    @Override
    public void activateCannons(String username, List<Pair<Integer, Integer>> cannons, List<Pair<Integer, Integer>> batteries) throws RemoteException {
        LOGGER.fine("Received RMI call: activateCannons from " + username);
        queueHandler.enqueue(new ActivateDoubleCannonsMessage(username, cannons, batteries));
    }

    @Override
    public Map<String, Integer> getScore() throws RemoteException {
        return Map.of();
    }
}