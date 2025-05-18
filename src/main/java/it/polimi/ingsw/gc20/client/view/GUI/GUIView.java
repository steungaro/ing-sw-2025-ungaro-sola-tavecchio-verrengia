package it.polimi.ingsw.gc20.client.view.GUI;

import it.polimi.ingsw.gc20.client.view.common.View;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;
import it.polimi.ingsw.gc20.client.network.NetworkManager;
import it.polimi.ingsw.gc20.client.view.GUI.controllers.NetworkController;
import it.polimi.ingsw.gc20.client.network.common.Client;
import it.polimi.ingsw.gc20.server.model.lobby.Lobby;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class GUIView extends View {

    private static Stage primaryStage;
    private MenuStateForGui currentState;

    public enum MenuStateForGui {
        WELCOME,
        NETWORK_CONFIG,
        LOGIN,
        MAIN_MENU,
        LOBBY_LIST,
        CREATE_LOBBY,
        IN_LOBBY,
        GAME_SCREEN,
        BUILDING_PHASE,
        VALIDATION_PHASE,
        PLANET_PHASE
    }

    private Map<MenuStateForGui, Object> controllers = new HashMap<>();

    public GUIView() {
        super();
    }

    public void initGUI(Stage stage) {
        primaryStage = stage;
        changeState(MenuStateForGui.WELCOME);
    }

    public void changeState(MenuStateForGui newState, Object... params) {
        this.currentState = newState;

        switch(newState) {
            case WELCOME:
                showWelcomeScene();
                break;
            case NETWORK_CONFIG:
                showNetworkScene();
                break;
            case LOGIN:
                showLoginScene();
                break;
            case MAIN_MENU:
                showMainMenuScene((String)params[0]);
                break;
            case LOBBY_LIST:
                showLobbyListScene();
                break;
            case CREATE_LOBBY:
                showCreateLobbyScene();
                break;
            case IN_LOBBY:
                showInLobbyScene((Lobby)params[0]);
                break;
            // TODO: Implement other states
        }
    }


    private void showWelcomeScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/welcome.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 600, 400);
            primaryStage.setScene(scene);
            primaryStage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showNetworkScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/network.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 600, 400);
            primaryStage.setScene(scene);
            primaryStage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setupConnection(String ipAddress, int port, boolean isRMI, NetworkController controller) {
        String clientType = isRMI ? "RMI" : "Socket";

        System.out.println("Connessione a " + ipAddress + ":" + port);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (ipAddress.isBlank()) {
            client = NetworkManager.initConnection(clientType);
        } else if (port == 0) {
            client = NetworkManager.initConnection(clientType, ipAddress);
        } else {
            client = NetworkManager.initConnection(clientType, ipAddress, port);
        }

        if (client == null || !client.isConnected()) {
            controller.showError("Connection failed");
            client = null;
        }

        if (client != null) {
            showLoginScene();
        }
    }

    public void showMainMenuScene(String username) {
        client.login(username);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainMenu.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 600, 400);
            primaryStage.setScene(scene);
            primaryStage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showLoginScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 600, 400);
            primaryStage.setScene(scene);
            primaryStage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showCreateLobbyScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/createLobby.fxml")); // Check if necessary
            Parent root = loader.load();

            Scene scene = new Scene(root, 600, 400);
            primaryStage.setScene(scene);
            primaryStage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showLobbyListScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/lobbyList.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 600, 400);
            primaryStage.setScene(scene);
            primaryStage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateView(Message message) throws RemoteException {
        super.updateView(message);
        Platform.runLater(() -> {
            // TODO: Implementare l'aggiornamento della view
        });
    }

    @Override
    public void notifyDisconnection() throws RemoteException {
        // TODO: Implementare la notifica di disconnessione
        Platform.runLater(() -> {
            // Codice per gestire la disconnessione nell'interfaccia grafica
        });
    }

    public void login(String username, String server, int port) {
        // TODO: Implementare il login
    }
}