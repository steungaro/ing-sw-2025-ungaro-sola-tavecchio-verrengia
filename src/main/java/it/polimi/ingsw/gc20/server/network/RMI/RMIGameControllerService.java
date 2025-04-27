package it.polimi.ingsw.gc20.server.network.RMI;

import it.polimi.ingsw.gc20.common.interfaces.GameControllerInterface;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.game.*;
import it.polimi.ingsw.gc20.server.model.components.AlienColor;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.server.network.common.QueueHandler;
import org.javatuples.Pair;

import java.io.Serial;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.logging.Logger;

public class RMIGameControllerService extends UnicastRemoteObject implements GameControllerInterface {
    private static final Logger LOGGER = Logger.getLogger(RMIGameControllerService.class.getName());
    private final QueueHandler queueHandler;
    @Serial
    private static final long serialVersionUID = 1L;

    public RMIGameControllerService() throws RemoteException {
        super();
        this.queueHandler = QueueHandler.getInstance();
    }

    @Override
    public void giveUp(String username) throws RemoteException {
        LOGGER.fine("Received RMI call: giveUp from " + username);
        queueHandler.enqueue(new GiveUpMessage(username));
    }

    @Override
    public void takeComponentFromUnviewed(String username, int index) throws RemoteException {
        LOGGER.fine("Received RMI call: takeComponentFromUnviewed from " + username);
        queueHandler.enqueue(new TakeComponentMessage(username, index, PileEnum.UNVIEWED));
    }

    @Override
    public void takeComponentFromViewed(String username, int index) throws RemoteException {
        LOGGER.fine("Received RMI call: takeComponentFromViewed from " + username);
        queueHandler.enqueue(new TakeComponentMessage(username, index, PileEnum.VIEWED));
    }

    @Override
    public void takeComponentFromBooked(String username, int index) throws RemoteException {
        LOGGER.fine("Received RMI call: takeComponentFromBooked from " + username);
        queueHandler.enqueue(new TakeComponentMessage(username, index, PileEnum.BOOKED));
    }

    @Override
    public void addComponentToBooked(String username) throws RemoteException {
        LOGGER.fine("Received RMI call: addComponentToBooked from " + username);
        queueHandler.enqueue(new AddComponentMessage(username, PileEnum.BOOKED));
    }

    @Override
    public void addComponentToViewed(String username) throws RemoteException {
        LOGGER.fine("Received RMI call: addComponentToViewed from " + username);
        queueHandler.enqueue(new AddComponentMessage(username, PileEnum.VIEWED));
    }

    @Override
    public void placeComponent(String username, Pair<Integer, Integer> coordinates) throws RemoteException {
        LOGGER.fine("Received RMI call: placeComponent from " + username);
        queueHandler.enqueue(new PlaceComponentMessage(username, coordinates));
    }

    @Override
    public void rotateComponentClockwise(String username) throws RemoteException {
        LOGGER.fine("Received RMI call: rotateComponentClockwise from " + username);
        queueHandler.enqueue(new RotateComponentMessage(username, 0));
    }

    @Override
    public void rotateComponentCounterclockwise(String username) throws RemoteException {
        LOGGER.fine("Received RMI call: rotateComponentCounterclockwise from " + username);
        queueHandler.enqueue(new RotateComponentMessage(username, 1));
    }

    @Override
    public void stopAssembling(String username, int position) throws RemoteException {
        LOGGER.fine("Received RMI call: stopAssembling from " + username);
        queueHandler.enqueue(new AssemblyEndedMessage(username, position));
    }

    @Override
    public void peekDeck(String username, int num) throws RemoteException {
        LOGGER.fine("Received RMI call: peekDeck from " + username);
        queueHandler.enqueue(new PeekDeckMessage(username, num));
    }

    @Override
    public void turnHourglass(String username) throws RemoteException {
        LOGGER.fine("Received RMI call: turnHourglass from " + username);
        queueHandler.enqueue(new TurnHourglassMessage(username));
    }

    @Override
    public void validateShip(String username) throws RemoteException {
        LOGGER.fine("Received RMI call: validateShip from " + username);
        queueHandler.enqueue(new ValidateShipMessage(username));
    }

    @Override
    public void removeComponentFromShip(String username, Pair<Integer, Integer> coordinates) throws RemoteException {
        LOGGER.fine("Received RMI call: removeComponentFromShip from " + username);
        queueHandler.enqueue(new RemoveComponentMessage(username, coordinates));
    }

    @Override
    public void addAlien(String username, AlienColor color, Pair<Integer, Integer> cabin) throws RemoteException {
        LOGGER.fine("Received RMI call: addAlien from " + username);
        queueHandler.enqueue(new AddAlienMessage(username, cabin, color));
    }

    @Override
    public void readyToFly(String username) throws RemoteException {
        LOGGER.fine("Received RMI call: readyToFly from " + username);
        queueHandler.enqueue(new ReadyToFlyMessage(username));
    }

    @Override
    public void chooseBranch(String username, Pair<Integer, Integer> coordinates) throws RemoteException {
        LOGGER.fine("Received RMI call: chooseBranch from " + username);
        queueHandler.enqueue(new ChooseBranchMessage(username, coordinates));
    }

    @Override
    public void rollDice(String username) throws RemoteException {
        LOGGER.fine("Received RMI call: rollDice from " + username);
        queueHandler.enqueue(new RollDiceMessage(username));
    }

    @Override
    public void landOnPlanet(String username, int planetIndex) throws RemoteException {
        LOGGER.fine("Received RMI call: landOnPlanet from " + username);
        queueHandler.enqueue(new LandPlanetMessage(username, planetIndex));
    }

    @Override
    public void loadCargo(String username, CargoColor loaded, Pair<Integer, Integer> ch) throws RemoteException {
        LOGGER.fine("Received RMI call: loadCargo from " + username);
        queueHandler.enqueue(new LoadCargoMessage(username, ch, loaded));
    }

    @Override
    public void unloadCargo(String username, CargoColor lost, Pair<Integer, Integer> ch) throws RemoteException {
        LOGGER.fine("Received RMI call: unloadCargo from " + username);
        queueHandler.enqueue(new MoveCargoMessage(username, ch, null, lost));
    }

    @Override
    public void moveCargo(String username, CargoColor cargo, Pair<Integer, Integer> from, Pair<Integer, Integer> to) throws RemoteException {
        LOGGER.fine("Received RMI call: moveCargo from " + username);
        queueHandler.enqueue(new MoveCargoMessage(username, from, to, cargo));
    }

    @Override
    public void acceptCard(String username) throws RemoteException {
        LOGGER.fine("Received RMI call: acceptCard from " + username);
        queueHandler.enqueue(new AcceptCardMessage(username));
    }

    @Override
    public void loseCrew(String username, List<Pair<Integer, Integer>> cabins) throws RemoteException {
        LOGGER.fine("Received RMI call: loseCrew from " + username);
        queueHandler.enqueue(new LoseCrewMessage(username, cabins));
    }

    @Override
    public void endMove(String username) throws RemoteException {
        LOGGER.fine("Received RMI call: endMove from " + username);
        queueHandler.enqueue(new EndMoveMessage(username));
    }


    @Override
    public void shootEnemy(String username, List<Pair<Integer, Integer>> cannons, List<Pair<Integer, Integer>> batteries) throws RemoteException {
        LOGGER.fine("Received RMI call: shootEnemy from " + username);
        queueHandler.enqueue(new ShootEnemyMessage(username, cannons, batteries));
    }


    @Override
    public void activateEngines(String username, List<Pair<Integer, Integer>> engines, List<Pair<Integer, Integer>> batteries) throws RemoteException {
        LOGGER.fine("Received RMI call: activateEngines from " + username);
        queueHandler.enqueue(new ActivateDoubleEnginesMessage(username, engines, batteries));
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
    public void killGame(String username) throws RemoteException {
        LOGGER.fine("Received RMI call: endGame from " + username);
        queueHandler.enqueue(new EndGameMessage(username));
    }
}