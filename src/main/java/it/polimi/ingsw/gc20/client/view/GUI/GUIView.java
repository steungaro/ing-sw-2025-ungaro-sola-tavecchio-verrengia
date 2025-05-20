package it.polimi.ingsw.gc20.client.view.GUI;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.network.NetworkManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.RemoteException;

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

    public void setupConnection(String ipAddress, int port, boolean isRMI) {
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
            System.out.println("errore creazione client");
            client = null;
        }

        if (client != null) {
            showScene("login");
        }
    }

    @Override
    public void mainMenuState(){

    }

    @Override
    public void notifyDisconnection() throws RemoteException {
        // TODO: Implementare la notifica di disconnessione
    }

    @Override
    public void login() {
        client.login(username);
        this.username = username;
    }
}