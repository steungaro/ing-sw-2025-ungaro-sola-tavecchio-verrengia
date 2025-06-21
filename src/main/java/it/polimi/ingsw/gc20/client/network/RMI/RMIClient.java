package it.polimi.ingsw.gc20.client.network.RMI;

import it.polimi.ingsw.gc20.client.network.common.Client;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.common.interfaces.AuthInterface;
import it.polimi.ingsw.gc20.common.interfaces.GameControllerInterface;
import it.polimi.ingsw.gc20.common.interfaces.MatchControllerInterface;
import it.polimi.ingsw.gc20.server.model.components.AlienColor;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import org.javatuples.Pair;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.logging.Logger;

public class RMIClient implements Client {
    private final String serverAddress;
    private final int port;

    private AuthInterface authService;
    private GameControllerInterface gameService;
    private MatchControllerInterface matchService;

    private boolean connected = false;

    private final Logger LOGGER = Logger.getLogger(RMIClient.class.getName());

    public RMIClient(String serverAddress, int port) {
        this.serverAddress = serverAddress;
        this.port = port;
    }

    @Override
    public void pong(String username) {
        try {
            authService.pong(username);
        } catch (RemoteException e) {
            LOGGER.warning("Error during pong back: " + e.getMessage());
        }
    }

    @Override
    public void start() {
        try {
            // Look up the registry
            Registry registry = LocateRegistry.getRegistry(serverAddress, port);

            // Look up the remote services
            authService = (AuthInterface) registry.lookup("AuthService");
            gameService = (GameControllerInterface) registry.lookup("GameService");
            matchService = (MatchControllerInterface) registry.lookup("MatchService");

            connected = true;
            LOGGER.info("Connected to RMI server at " + serverAddress + ":" + port);
        } catch (RemoteException | NotBoundException e) {
            LOGGER.severe("Error connecting to RMI server: " + e.getMessage());
            connected = false;
        }
    }

    @Override
    public void login(String username) {
        if (!connected) return;

        try {
            ClientGameModel.getInstance().loggedIn = authService.login(username, ClientGameModel.getInstance());
        } catch (Exception e) {
            LOGGER.warning("Error during login: " + e.getMessage());
        }
    }

    @Override
    public void stop() {
        connected = false;
        LOGGER.info("Disconnected from RMI server.");
        ClientGameModel.getInstance().shutdown();
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Deprecated
    @Override
    public void logout(String username) {
        // Logout logic is not required for this project
    }

    @Override
    public String getAddress() {
        return serverAddress;
    }

    @Override
    public int getPort() {
        return port;
    }


    @Override
    public void takeComponentFromUnviewed(String username, int index)  {
        try {
            gameService.takeComponentFromUnviewed(username, index);
        } catch (RemoteException e) {
            LOGGER.warning("Error during taking component from unviewed: " + e.getMessage());
        }
    }

    @Override
    public void takeComponentFromViewed(String username, int index)  {
        try {
            gameService.takeComponentFromViewed(username, index);
        } catch (RemoteException e) {
            LOGGER.warning("Error during taking component from viewed: " + e.getMessage());
        }
    }

    @Override
    public void takeComponentFromBooked(String username, int index)  {
        try {
            gameService.takeComponentFromBooked(username, index);
        } catch (RemoteException e) {
            LOGGER.warning("Error during taking component from booked: " + e.getMessage());
        }
    }

    @Override
    public void addComponentToBooked(String username)  {
        try {
            gameService.addComponentToBooked(username);
        } catch (RemoteException e) {
            LOGGER.warning("Error during adding component to booked: " + e.getMessage());
        }
    }

    @Override
    public void addComponentToViewed(String username)  {
        try {
            gameService.addComponentToViewed(username);
        } catch (RemoteException e) {
            LOGGER.warning("Error during adding component to viewed: " + e.getMessage());
        }
    }

    @Override
    public void placeComponent(String username, Pair<Integer, Integer> coordinates)  {
        try {
            gameService.placeComponent(username, coordinates);
        } catch (RemoteException e) {
            LOGGER.warning("Error during placing component: " + e.getMessage());
        }
    }

    @Override
    public void rotateComponentClockwise(String username)  {
        try {
            gameService.rotateComponentClockwise(username);
        } catch (RemoteException e) {
            LOGGER.warning("Error during rotating component clockwise: " + e.getMessage());
        }
    }

    @Override
    public void rotateComponentCounterclockwise(String username)  {
        try {
            gameService.rotateComponentCounterclockwise(username);
        } catch (RemoteException e) {
            LOGGER.warning("Error during rotating component counterclockwise: " + e.getMessage());
        }
    }

    @Override
    public void stopAssembling(String username, int position)  {
        try {
            gameService.stopAssembling(username, position);
        } catch (RemoteException e) {
            LOGGER.warning("Error during stopping assembling: " + e.getMessage());
        }
    }

    @Override
    public void peekDeck(String username, int num)  {
        try {
            gameService.peekDeck(username, num);
        } catch (RemoteException e) {
            LOGGER.warning("Error during peeking deck: " + e.getMessage());
        }
    }

    @Override
    public void turnHourglass(String username)  {
        try {
            gameService.turnHourglass(username);
        } catch (RemoteException e) {
            LOGGER.warning("Error during turning hourglass: " + e.getMessage());
        }
    }

    @Override
    public void removeComponentFromShip(String username, Pair<Integer, Integer> coordinates)  {
        try {
            gameService.removeComponentFromShip(username, coordinates);
        } catch (RemoteException e) {
            LOGGER.warning("Error during removing component from ship: " + e.getMessage());
        }
    }

    @Override
    public void addAlien(String username, AlienColor color, Pair<Integer, Integer> cabin)  {
        try {
            gameService.addAlien(username, color, cabin);
        } catch (RemoteException e) {
            LOGGER.warning("Error during adding alien: " + e.getMessage());
        }
    }

    @Override
    public void chooseBranch(String username, Pair<Integer, Integer> coordinates)  {
        try {
            gameService.chooseBranch(username, coordinates);
        } catch (RemoteException e) {
            LOGGER.warning("Error during choosing branch: " + e.getMessage());
        }
    }

    @Override
    public void rollDice(String username)  {
        try {
            gameService.rollDice(username);
        } catch (RemoteException e) {
            LOGGER.warning("Error during rolling dice: " + e.getMessage());
        }
    }

    @Override
    public void landOnPlanet(String username, int planetIndex)  {
        try {
            gameService.landOnPlanet(username, planetIndex);
        } catch (RemoteException e) {
            LOGGER.warning("Error during landing on planet: " + e.getMessage());
        }
    }

    @Override
    public void loadCargo(String username, CargoColor loaded, Pair<Integer, Integer> ch)  {
        try {
            gameService.loadCargo(username, loaded, ch);
        } catch (RemoteException e) {
            LOGGER.warning("Error during loading cargo: " + e.getMessage());
        }
    }

    @Override
    public void unloadCargo(String username, CargoColor lost, Pair<Integer, Integer> ch)  {
        try {
            gameService.unloadCargo(username, lost, ch);
        } catch (RemoteException e) {
            LOGGER.warning("Error during unloading cargo: " + e.getMessage());
        }
    }

    @Override
    public void moveCargo(String username, CargoColor cargo, Pair<Integer, Integer> from, Pair<Integer, Integer> to)  {
        try {
            gameService.moveCargo(username, cargo, from, to);
        } catch (RemoteException e) {
            LOGGER.warning("Error during moving cargo: " + e.getMessage());
        }
    }

    @Override
    public void acceptCard(String username)  {
        try {
            gameService.acceptCard(username);
        } catch (RemoteException e) {
            LOGGER.warning("Error during accepting card: " + e.getMessage());
        }
    }

    @Override
    public void loseCrew(String username, List<Pair<Integer, Integer>> cabins)  {
        try {
            gameService.loseCrew(username, cabins);
        } catch (RemoteException e) {
            LOGGER.warning("Error during losing crew: " + e.getMessage());
        }
    }

    @Override
    public void endMove(String username)  {
        try {
            gameService.endMove(username);
        } catch (RemoteException e) {
            LOGGER.warning("Error during ending move: " + e.getMessage());
        }
    }

    @Override
    public void giveUp(String username)  {
        try {
            gameService.giveUp(username);
        } catch (RemoteException e) {
            LOGGER.warning("Error during giving up: " + e.getMessage());
        }
    }

    @Override
    public void loseEnergy(String username, Pair<Integer, Integer> coordinates) throws RemoteException {
        try {
            gameService.loseEnergy(username, coordinates);
        } catch (RemoteException e) {
            LOGGER.warning("Error during losing energy: " + e.getMessage());
        }
    }

    @Override
    public void activateEngines(String username, List<Pair<Integer, Integer>> engines, List<Pair<Integer, Integer>> batteries)  {
        try {
            gameService.activateEngines(username, engines, batteries);
        } catch (RemoteException e) {
            LOGGER.warning("Error during activating engines: " + e.getMessage());
        }
    }

    @Override
    public void activateShield(String username, Pair<Integer, Integer> shield, Pair<Integer, Integer> battery)  {
        try {
            gameService.activateShield(username, shield, battery);
        } catch (RemoteException e) {
            LOGGER.warning("Error during activating shield: " + e.getMessage());
        }
    }

    @Override
    public void activateCannons(String username, List<Pair<Integer, Integer>> cannons, List<Pair<Integer, Integer>> batteries)  {
        try {
            gameService.activateCannons(username, cannons, batteries);
        } catch (RemoteException e) {
            LOGGER.warning("Error during activating cannons: " + e.getMessage());
        }
    }

    @Override
    public void joinLobby(String id, String user)  {
        try {
            matchService.joinLobby(id, user);
        } catch (RemoteException e) {
            LOGGER.warning("Error during joining lobby: " + e.getMessage());
        }
    }

    @Override
    public void createLobby(String name, String user, int maxPlayers, int level)  {
        try {
            matchService.createLobby(name, user, maxPlayers, level);
        } catch (RemoteException e) {
            LOGGER.warning("Error during creating lobby: " + e.getMessage());
        }
    }

    @Override
    public void leaveLobby(String userid)  {
        try {
            matchService.leaveLobby(userid);
        } catch (RemoteException e) {
            LOGGER.warning("Error during leaving lobby: " + e.getMessage());
        }
    }

    @Override
    public void startLobby(String id)  {
        try {
            matchService.startLobby(id);
        } catch (RemoteException e) {
            LOGGER.warning("Error during starting lobby: " + e.getMessage());
        }
    }

    @Override
    public void killLobby(String username)  {
        try {
            matchService.killLobby(username);
        } catch (RemoteException e) {
            LOGGER.warning("Error during killing lobby: " + e.getMessage());
        }

    }

    @Override
    public void getLobbies(String username) throws RemoteException {
        try {
            matchService.getLobbies(username);
        } catch (RemoteException e) {
            LOGGER.warning("Error during getting lobbies: " + e.getMessage());
        }
    }
}
