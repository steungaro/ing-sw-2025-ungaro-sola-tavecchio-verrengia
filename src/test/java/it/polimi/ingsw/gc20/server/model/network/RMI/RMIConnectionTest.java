package it.polimi.ingsw.gc20.server.model.network.RMI;

import it.polimi.ingsw.gc20.network.RMI.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

public class RMIConnectionTest {
    private static final Logger LOGGER = Logger.getLogger(RMIConnectionTest.class.getName());
    private static final int PORT = 1099;
    private static Registry registry;
    private static RMIAuthService authService;
    private static TestRMIGameService gameService;

    @BeforeAll
    public static void setup() throws RemoteException {
        // Create the RMI registry
        try {
            registry = LocateRegistry.createRegistry(PORT);
            LOGGER.info("Registry RMI created on port " + PORT);
        } catch (RemoteException e){
            //registry already created
            registry = LocateRegistry.getRegistry(PORT);
            LOGGER.info("Registry RMI already created on port " + PORT);
        }
        // create a new instance of the server
        RMIServer server = new RMIServer();

        // create the services
        authService = new RMIAuthService(server);
        gameService = new TestRMIGameService(authService);
        LOGGER.info("Services created");
        // Bind the services to the registry
        RMIAuthInterface authServiceStub = (RMIAuthInterface) UnicastRemoteObject.exportObject(authService, 0);
        RMIGameInterface gameServiceStub = (RMIGameInterface) UnicastRemoteObject.exportObject(gameService, 0);

        registry.rebind("AuthService", authServiceStub);
        registry.rebind("GameService", gameServiceStub);
        LOGGER.info("Services bound to the registry");
    }

    @Test
    public void testRMIConnection() throws RemoteException, NotBoundException {
        // obtain the stub for the authService
        RMIAuthInterface authClient = (RMIAuthInterface) registry.lookup("AuthService");
        // obtain the stub for the gameService
        RMIGameInterface gameClient = (RMIGameInterface) registry.lookup("GameService");

        assertNotNull(authClient, "AuthService stub is null");
        assertNotNull(gameClient, "GameService stub is null");

        // test auth
        String username = "testUser";
        String token = authClient.login(username);
        assertNotNull(token, "Auth token is null");
        assertEquals (username, authClient.getUsername(token), "Username does not match");

        //create a view
        TestViewImpl view = new TestViewImpl();
        ViewInterface viewStub = (ViewInterface) UnicastRemoteObject.exportObject(view, 0);
        // register the view
        gameClient.registerView(token, viewStub);

        // test sending a message
        String message = "Hello, World!";
        gameClient.sendMessage(token, message);

        //ping
        assertTrue (gameClient.ping(token), "Ping failed");

        // test joining a game and leaving a game
        gameClient.joinGame(token);
        gameClient.leaveGame(token);

        //Logout
        assertTrue (authClient.logout(token), "Logout failed");
    }

    @AfterAll
    public static void tearDown() throws RemoteException, NotBoundException {
        // Unbind the services from the registry
        registry.unbind("AuthService");
        registry.unbind("GameService");

        // Unexport the services
        UnicastRemoteObject.unexportObject(authService, true);
        UnicastRemoteObject.unexportObject(gameService, true);

        LOGGER.info("Services unbound");
    }
}
