package it.polimi.ingsw.gc20.client.view.GUI;

import it.polimi.ingsw.gc20.client.view.GUI.controllers.InLobbyController;
import it.polimi.ingsw.gc20.client.view.TUI.MenuState;
import it.polimi.ingsw.gc20.client.view.common.ViewLobby;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.network.NetworkManager;
import it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards.ViewAdventureCard;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;
import it.polimi.ingsw.gc20.server.model.cards.FireType;
import it.polimi.ingsw.gc20.server.model.cards.Planet;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Platform;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public class GUIView extends ClientGameModel {

    private static Stage primaryStage;

    public GUIView() throws RemoteException {
        super();
    }

    public void initGUI(Stage stage) {
        primaryStage = stage;
        showScene("welcome");
    }

    public void showScene(String fileName) {
        showScene(fileName, null);
    }

    public void showScene(String fileName, Object data) {
        try {
            String path = "/fxml/" + fileName + ".fxml";
            URL resourceUrl = getClass().getResource(path);

            if (resourceUrl == null) {
                System.err.println("ERRORE: File FXML non trovato: " + path);
                return;
            }

            FXMLLoader loader = new FXMLLoader(resourceUrl);
            Parent root = loader.load();

            if (data != null) {
                Object controller = loader.getController();
                if (fileName.equals("inLobby") && controller instanceof InLobbyController) {
                    ViewLobby lobby = getCurrentLobby();
                    ((InLobbyController) controller).initLobbyData(lobby, getUsername());
                }
            }

            Scene scene = new Scene(root, 600, 400);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Space Venture");
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
    public void updateView(Message message) {
        Platform.runLater(() -> {
            try {
                message.handleMessage();
            } catch (Exception e) {
                System.out.println("Error while handling message: " + e.getMessage());
            }
        });
    }

    @Override
    public void mainMenuState(){
        showScene("mainMenu");
    }

    @Override
    public void planetMenu(List<Planet> planets) {

    }

    @Override
    public void populateShipMenu() {

    }

    @Override
    public void automaticAction(String message) {

    }

    @Override
    public void validationMenu() {

    }

    @Override
    public void takeComponentMenu() {

    }

    @Override
    public void init() {

    }

    @Override
    public void shieldsMenu(FireType fireType, int direction, int line) {

    }

    @Override
    public void rollDiceMenu(FireType fireType, int direction) {

    }

    @Override
    public void cargoMenu(int cargoNum) {

    }

    @Override
    public void loseCrewMenu(int crewNum) {

    }

    @Override
    public void removeBatteryMenu(int batteryNum) {

    }

    @Override
    public void placeComponentMenu() {

    }

    @Override
    public void leaderBoardMenu(Map<String, Integer> leaderBoard) {

    }

    @Override
    public void loginSuccessful(String username) {

    }

    @Override
    public void loginFailed(String username) {

    }

    @Override
    public void notifyDisconnection() throws RemoteException {
        // TODO: Implementare la notifica di disconnessione
    }

    @Override
    public void display(String message) {
        // TODO
    }

    @Override
    public void display(MenuState menuState) {
        // TODO
    }

    @Override
    public void shutdown() {
        // TODO
    }

    @Override
    public void branchMenu(){
        // TODO
    }

    @Override
    public void buildingMenu(List<ViewAdventureCard> cards) {

    }

    @Override
    public void cannonsMenu(String message) {

    }

    @Override
    public void cardAcceptanceMenu(String message) {

    }

    @Override
    public void cargoMenu(String message, int cargoToLose, List<CargoColor> cargoToGain) {

    }

    @Override
    public void engineMenu(String message) {

    }

    @Override
    public void inLobbyMenu() {
        showScene("inLobby", true);
    }

    @Override
    public void login() {


        client.login(username);
        this.username = username;
    }
}