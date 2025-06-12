package it.polimi.ingsw.gc20.client.network.socket;

import it.polimi.ingsw.gc20.client.network.common.Client;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Pong;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.game.*;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.lobby.*;
import it.polimi.ingsw.gc20.server.model.components.AlienColor;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import org.javatuples.Pair;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.List;
import java.util.logging.Logger;

public class SocketClient implements Client {
    private final String serverAddress;
    private final int serverPort;
    private int port;
    private static final Logger LOGGER = Logger.getLogger(SocketClient.class.getName());
    private ServerSocket serverSocket;
    private Socket socket;
    private boolean running;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public SocketClient(String serverAddress, int serverPort) {
        this.serverPort = serverPort;
        this.serverAddress = serverAddress;
        this.port = 0; // Using port 0 lets the system assign an available port
    }

    @Override
    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            port = serverSocket.getLocalPort();
            running = true;
            LOGGER.info("Socket client set up on port: " + serverSocket.getLocalPort());
            // Connect to the server
            connectToServer();
        } catch (Exception e) {
            LOGGER.severe("Error while setting up socket client: " + e.getMessage());
        }
    }

    @Override
    public void stop() {
        running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            ClientGameModel.getInstance().shutdown();
        } catch (Exception e) {
            LOGGER.warning("Error while stopping socket client: " + e.getMessage());
        }
        LOGGER.info("Socket client stopped.");
    }

    public void receiveMessages() {
        while (running) {
            try {
                // Check if the socket is valid and open
                if (socket == null || socket.isClosed()) {
                    LOGGER.warning("Socket is closed, stopping message reception.");
                    break;
                }
                
                // Receive the message
                Message message = (Message) in.readObject();

                ClientGameModel.getInstance().updateView(message);
            } catch (IOException | ClassNotFoundException e) {
                disconnect();
            }
        }
    }

    @Override
    public boolean isConnected() {
        return running;
    }

    @Override
    public void login(String username) {
        try {
            out.writeObject(new LoginRequest(username));
            out.flush();
        } catch (IOException e) {
            LOGGER.warning("Error while logging in: " + e.getMessage());
        }
    }

    @Override
    @Deprecated
    public void logout(String username) {
        // logout logic is not needed for this project
    }

    @Override
    public String getAddress() {
        return serverAddress;
    }

    @Override
    public int getPort() {
        return serverPort;
    }

    @Override
    public void pong(String username) {
        try {
            out.writeObject(new Pong(username));
            out.flush();
        } catch (IOException e) {
            LOGGER.warning("Error while sending pong: " + e.getMessage());
        }
    }


    private void connectToServer() {
        try {
            // Create a socket connection to the server
            socket = new Socket(serverAddress, serverPort);
            LOGGER.info("Connected to server at " + serverAddress + ":" + serverPort);
            // Start a thread to handle incoming messages
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());
            Thread messageReceiverThread = new Thread(() -> {
                try {
                    receiveMessages();
                } catch (Exception e) {
                    LOGGER.warning("Error while receiving messages: " + e.getMessage());
                }
            });
            messageReceiverThread.start();
            LOGGER.info("Socket client started, waiting for messages...");
            running = true;
        } catch (Exception e) {
            running = false;
            LOGGER.severe("Error while connecting to server: " + e.getMessage());
        }
    }

    private void disconnect() {
        try {
            if (!running) return;
            running = false;
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            LOGGER.warning("Error while disconnecting: " + e.getMessage());
        }
    }


    @Override
    public void takeComponentFromUnviewed(String username, int index) {
        try {
            out.writeObject(new TakeComponentMessage(username, index, PileEnum.UNVIEWED));
            out.flush();
        } catch (IOException e) {
            LOGGER.warning("Error while taking component from unviewed: " + e.getMessage());
        }
    }

    @Override
    public void takeComponentFromViewed(String username, int index) {
        try {
            out.writeObject(new TakeComponentMessage(username, index, PileEnum.VIEWED));
            out.flush();
        } catch (IOException e) {
            LOGGER.warning("Error while taking component from viewed: " + e.getMessage());
        }
    }

    @Override
    public void takeComponentFromBooked(String username, int index) {
        try {
            out.writeObject(new TakeComponentMessage(username, index, PileEnum.BOOKED));
            out.flush();
        } catch (IOException e) {
            LOGGER.warning("Error while taking component from booked: " + e.getMessage());
        }
    }

    @Override
    public void addComponentToBooked(String username) {
        try {
            out.writeObject(new AddComponentMessage(username, PileEnum.BOOKED));
            out.flush();
        } catch (IOException e) {
            LOGGER.warning("Error while adding component to booked: " + e.getMessage());
        }
    }

    @Override
    public void addComponentToViewed(String username) {
        try {
            out.writeObject(new AddComponentMessage(username, PileEnum.VIEWED));
            out.flush();
        } catch (IOException e) {
            LOGGER.warning("Error while adding component to viewed: " + e.getMessage());
        }
    }

    @Override
    public void placeComponent(String username, Pair<Integer, Integer> coordinates) {
        try {
            out.writeObject(new PlaceComponentMessage(username, coordinates));
            out.flush();
        } catch (IOException e) {
            LOGGER.warning("Error while placing component: " + e.getMessage());
        }
    }

    @Override
    public void rotateComponentClockwise(String username) {
        try {
            out.writeObject(new RotateComponentMessage(username, 0));
            out.flush();
        } catch (IOException e) {
            LOGGER.warning("Error while rotating component clockwise: " + e.getMessage());
        }
    }

    @Override
    public void rotateComponentCounterclockwise(String username) {
        try {
            out.writeObject(new RotateComponentMessage(username, 1));
            out.flush();
        } catch (IOException e) {
            LOGGER.warning("Error while rotating component counterclockwise: " + e.getMessage());
        }
    }

    @Override
    public void stopAssembling(String username, int position) {
        try {
            out.writeObject(new AssemblyEndedMessage(username, position));
            out.flush();
        } catch (IOException e) {
            LOGGER.warning("Error while stopping assembling: " + e.getMessage());
        }
    }

    @Override
    public void peekDeck(String username, int num) {
        try {
            out.writeObject(new PeekDeckMessage(username, num));
            out.flush();
        } catch (IOException e) {
            LOGGER.warning("Error while peeking deck: " + e.getMessage());
        }
    }

    @Override
    public void turnHourglass(String username) {
        try {
            out.writeObject(new TurnHourglassMessage(username));
            out.flush();
        } catch (IOException e) {
            LOGGER.warning("Error while turning hourglass: " + e.getMessage());
        }
    }

    @Override
    public void removeComponentFromShip(String username, Pair<Integer, Integer> coordinates) {
        try {
            out.writeObject(new RemoveComponentMessage(username, coordinates));
            out.flush();
        } catch (IOException e) {
            LOGGER.warning("Error while removing component from ship: " + e.getMessage());
        }
    }

    @Override
    public void addAlien(String username, AlienColor color, Pair<Integer, Integer> cabin) {
        try {
            out.writeObject(new AddAlienMessage(username, cabin, color));
            out.flush();
        } catch (IOException e) {
            LOGGER.warning("Error while adding alien: " + e.getMessage());
        }
    }

    @Override
    public void chooseBranch(String username, Pair<Integer, Integer> coordinates) {
        try {
            out.writeObject(new ChooseBranchMessage(username, coordinates));
            out.flush();
        } catch (IOException e) {
            LOGGER.warning("Error while choosing branch: " + e.getMessage());
        }
    }

    @Override
    public void rollDice(String username) {
        try {
            out.writeObject(new RollDiceMessage(username));
            out.flush();
        } catch (IOException e) {
            LOGGER.warning("Error while rolling dice: " + e.getMessage());
        }
    }

    @Override
    public void landOnPlanet(String username, int planetIndex) {
        try {
            out.writeObject(new LandPlanetMessage(username, planetIndex));
            out.flush();
        } catch (IOException e) {
            LOGGER.warning("Error while landing on planet: " + e.getMessage());
        }
    }

    @Override
    public void loadCargo(String username, CargoColor loaded, Pair<Integer, Integer> ch) {
        try {
            out.writeObject(new LoadCargoMessage(username, ch, loaded));
            out.flush();
        } catch (IOException e) {
            LOGGER.warning("Error while loading cargo: " + e.getMessage());
        }
    }

    @Override
    public void unloadCargo(String username, CargoColor lost, Pair<Integer, Integer> ch) {
        try {
            out.writeObject(new MoveCargoMessage(username, ch, null, lost));
            out.flush();
        } catch (IOException e) {
            LOGGER.warning("Error while unloading cargo: " + e.getMessage());
        }
    }

    @Override
    public void moveCargo(String username, CargoColor cargo, Pair<Integer, Integer> from, Pair<Integer, Integer> to) {
        try {
            out.writeObject(new MoveCargoMessage(username, from, to, cargo));
            out.flush();
        } catch (IOException e) {
            LOGGER.warning("Error while moving cargo: " + e.getMessage());
        }
    }

    @Override
    public void acceptCard(String username) {
        try {
            out.writeObject(new AcceptCardMessage(username));
            out.flush();
        } catch (IOException e) {
            LOGGER.warning("Error while accepting card: " + e.getMessage());
        }
    }

    @Override
    public void loseCrew(String username, List<Pair<Integer, Integer>> cabins) {
        try {
            out.writeObject(new LoseCrewMessage(username, cabins));
            out.flush();
        } catch (IOException e) {
            LOGGER.warning("Error while losing crew: " + e.getMessage());
        }
    }

    @Override
    public void endMove(String username) {
        try {
            out.writeObject(new EndMoveMessage(username));
            out.flush();
        } catch (IOException e) {
            LOGGER.warning("Error while ending move: " + e.getMessage());
        }
    }

    @Override
    public void giveUp(String username) {
        try {
            out.writeObject(new GiveUpMessage(username));
            out.flush();
        } catch (IOException e) {
            LOGGER.warning("Error while giving up: " + e.getMessage());
        }
    }

    @Override
    public void loseEnergy(String username, Pair<Integer, Integer> coordinates) throws RemoteException {
        try {
            out.writeObject(new LoseEnergyMessage(username, coordinates));
            out.flush();
        } catch (IOException e) {
            LOGGER.warning("Error while losing energy: " + e.getMessage());
        }
    }

    @Override
    public void activateEngines(String username, List<Pair<Integer, Integer>> engines, List<Pair<Integer, Integer>> batteries) {
        try {
            out.writeObject(new ActivateDoubleEnginesMessage(username, engines, batteries));
            out.flush();
        } catch (IOException e) {
            LOGGER.warning("Error while activating engines: " + e.getMessage());
        }
    }

    @Override
    public void activateShield(String username, Pair<Integer, Integer> shield, Pair<Integer, Integer> battery) {
        try {
            out.writeObject(new ActivateShieldMessage(username, shield, battery));
            out.flush();
        } catch (IOException e) {
            LOGGER.warning("Error while activating shield: " + e.getMessage());
        }
    }

    @Override
    public void activateCannons(String username, List<Pair<Integer, Integer>> cannons, List<Pair<Integer, Integer>> batteries) {
        try {
            out.writeObject(new ActivateDoubleCannonsMessage(username, cannons, batteries));
            out.flush();
        } catch (IOException e) {
            LOGGER.warning("Error while activating cannons: " + e.getMessage());
        }
    }

    @Override
    public void joinLobby(String id, String user) {
        try {
            out.writeObject(new JoinLobbyMessage(user, id));
            out.flush();
        } catch (IOException e) {
            LOGGER.warning("Error while joining lobby: " + e.getMessage());
        }
    }

    @Override
    public void createLobby(String name, String user, int maxPlayers, int level) {
        try {
            out.writeObject(new CreateLobbyMessage(name, user, maxPlayers, level));
            out.flush();
        } catch (IOException e) {
            LOGGER.warning("Error while creating lobby: " + e.getMessage());
        }
    }

    @Override
    public void leaveLobby(String userid) {
        try {
            out.writeObject(new LeaveLobbyMessage(userid));
            out.flush();
        } catch (IOException e) {
            LOGGER.warning("Error while leaving lobby: " + e.getMessage());
        }
    }

    @Override
    public void startLobby(String id) {
        try {
            out.writeObject(new StartLobbyMessage(id));
            out.flush();
        } catch (IOException e) {
            LOGGER.warning("Error while starting lobby: " + e.getMessage());
        }
    }

    @Override
    public void killLobby(String username) {
        try {
            out.writeObject(new KillLobbyMessage(username));
            out.flush();
        } catch (IOException e) {
            LOGGER.warning("Error while killing lobby: " + e.getMessage());
        }
    }

    @Override
    public void getLobbies(String username) throws RemoteException {
        try {
            out.writeObject(new LobbyListRequest(username));
            out.flush();
        } catch (IOException e) {
            LOGGER.warning("Error while getting lobbies: " + e.getMessage());
        }
    }
}