package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ViewPlayer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    @FXML public VBox currentFrame;
    @FXML private VBox player1Ship;
    @FXML private VBox player2Ship;
    @FXML private VBox player3Ship;
    @FXML private VBox player4Ship;
    @FXML private Label player1Name;
    @FXML private Label player2Name;
    @FXML private Label player3Name;
    @FXML private Label player4Name;

    @FXML private VBox drawnCard;
    @FXML private Button acceptButton;
    @FXML private Button discardButton;

    @FXML private Button activateComponentButton;
    @FXML private Button endTurnButtonLeft;
    @FXML private Button button1;
    @FXML private Button button2;
    @FXML private Button button3;
    @FXML private Button button4;

    @FXML private VBox gameBoard;
    @FXML private ScrollPane messageScrollPane;
    @FXML private Label serverMessages;

    private ClientGameModel gameModel;
    private ViewPlayer[] players;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gameModel = ClientGameModel.getInstance();
        setupEventHandlers();
        initializePlayerDisplay();
        initializeGameBoard();
        initializeCurrentFrame();
        setupVisibility();
    }

    private void setupEventHandlers() {
        acceptButton.setOnAction(event -> handleAcceptCard());
        discardButton.setOnAction(event -> handleDiscardCard());

        activateComponentButton.setOnAction(event -> handleActivateComponent());
        endTurnButtonLeft.setOnAction(event -> handleEndTurn());

        button1.setOnAction(event -> handleButton1());
        button2.setOnAction(event -> handleButton2());
        button3.setOnAction(event -> handleButton3());
        button4.setOnAction(event -> handleButton4());
    }

    private void setupVisibility() {
    }

    private void initializePlayerDisplay() {
        loadPlayerUsername();
        loadPlayerShips();
    }

    private void loadPlayerUsername() {
        players = gameModel.getPlayers();
        for (int i = 0; i < players.length; i++) {
            if (players[i] == null) return;
            switch (i) {
                case 0 -> player1Name.setText(players[i].username);
                case 1 -> player2Name.setText(players[i].username);
                case 2 -> player3Name.setText(players[i].username);
                case 3 -> player4Name.setText(players[i].username);
            }
        }
    }

    private void loadPlayerShips() {
        for (int i = 0; i < players.length; i++) {
            if (players[i] == null) return;
            switch (i) {
                case 0 -> loadShipFXML(player1Ship, players[i]);
                case 1 -> loadShipFXML(player2Ship, players[i]);
                case 2 -> loadShipFXML(player3Ship, players[i]);
                case 3 -> loadShipFXML(player4Ship, players[i]);
            }
        }
    }

    private void loadShipFXML(VBox container, ViewPlayer player) {
        try {
            FXMLLoader loader;
            if(gameModel.getShip(ClientGameModel.getInstance().getUsername()).isLearner) {
                loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/gc20/client/view/GUI/fxml/ship0.fxml"));
            }
            else {
                loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/gc20/client/view/GUI/fxml/ship.fxml"));
            }
            container.getChildren().clear();
            container.getChildren().add(loader.load());

            Object controller = loader.getController();
            if (controller instanceof ShipController c) {
                c.buildShipComponents(ClientGameModel.getInstance().getShip(player.username));
                c.updateStatisticBoard(player);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeGameBoard() {
        VBox container = gameBoard;
        try {
            FXMLLoader loader;
            if(gameModel.getShip(ClientGameModel.getInstance().getUsername()).isLearner) {
                loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/gc20/client/view/GUI/fxml/board0.fxml"));
            }
            else {
                loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/gc20/client/view/GUI/fxml/board2.fxml"));
            }
            container.getChildren().clear();
            container.getChildren().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void initializeCurrentFrame() {
        loadShipFXML(currentFrame, Arrays.stream(gameModel.getPlayers())
                .filter(p -> p != null && p.username.equals(gameModel.getUsername()))
                .findFirst().orElse(null));
    }

    public void updateServerMessages(String message) {

    }

    private void handleAcceptCard() {
        try {
            gameModel.getClient().acceptCard(gameModel.getUsername());
        } catch (RemoteException e) {
            updateServerMessages("Error accepting card: " + e.getMessage());
        }
    }

    private void handleDiscardCard() {
        try {
            gameModel.getClient().endMove(ClientGameModel.getInstance().getUsername());
        } catch (RemoteException e) {
            updateServerMessages("Error discarding card: " + e.getMessage());
        }
    }

    private void handleActivateComponent() {

    }

    private void handleEndTurn() {
    }

    private void handleButton1() {

    }

    private void handleButton2() {

    }

    private void handleButton3() {

    }

    private void handleButton4() {

    }

    public void setControlsEnabled(boolean enabled) {
        Platform.runLater(() -> {
            acceptButton.setDisable(!enabled);
            discardButton.setDisable(!enabled);
            activateComponentButton.setDisable(!enabled);
            endTurnButtonLeft.setDisable(!enabled);
            button1.setDisable(!enabled);
            button2.setDisable(!enabled);
            button3.setDisable(!enabled);
            button4.setDisable(!enabled);
        });
    }
}