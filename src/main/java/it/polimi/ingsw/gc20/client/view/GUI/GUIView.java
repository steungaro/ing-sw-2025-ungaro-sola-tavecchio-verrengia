package it.polimi.ingsw.gc20.client.view.GUI;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
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
import org.jline.terminal.impl.CursorSupport;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class GUIView extends ClientGameModel {

    private static Stage primaryStage;

    public GUIView() {
        super();
    }

    public void initGUI(Stage stage) {
        primaryStage = stage;
        showScene("welcome");
    }

    public void showScene(String fileName){
        try {
            String path = "/fxml/" + fileName + ".fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
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
            showScene("login");
        }
    }


    @Override
    public void notifyDisconnection() throws RemoteException {
        // TODO: Implementare la notifica di disconnessione
    }

    public void login(String username, String server, int port) {
        client.login(username);
        this.username = username;
    }
}