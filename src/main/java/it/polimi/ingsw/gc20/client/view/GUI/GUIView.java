package it.polimi.ingsw.gc20.client.view.GUI;

import it.polimi.ingsw.gc20.client.view.common.View;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;
import it.polimi.ingsw.gc20.client.network.NetworkManager;
import it.polimi.ingsw.gc20.client.view.GUI.controllers.NetworkController;
import it.polimi.ingsw.gc20.client.network.common.Client;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.RemoteException;

public class GUIView extends View {

    private static Stage primaryStage;

    public GUIView() {
        super();
    }

    public void initGUI(Stage stage) {
        primaryStage = stage;
        setupWelcomeScene();
    }

    private void setupWelcomeScene() {
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

    public void setupConnection(String ipAddress, int port, boolean isRMI) {
        if (isRMI) {
            System.out.println("Connessione RMI a " + ipAddress + ":" + port);
            // TODO: implementare la connessione RMI
        } else {
            System.out.println("Connessione Socket a " + ipAddress + ":" + port);
            // TODO: implementare la connessione Socket
        }

        // Mostra la schermata di login dopo la connessione
        showLoginScene();
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