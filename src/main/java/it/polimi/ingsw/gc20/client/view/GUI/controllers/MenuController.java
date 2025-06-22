package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ViewPlayer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    @FXML public StackPane currentFrame;
    @FXML private StackPane player1Ship;
    @FXML private StackPane player2Ship;
    @FXML private StackPane player3Ship;
    @FXML private StackPane player4Ship;
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

    @FXML private StackPane gameBoard;
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
        acceptButton.setOnAction(_ -> handleAcceptCard());
        discardButton.setOnAction(_ -> handleDiscardCard());

        activateComponentButton.setOnAction(_ -> handleActivateComponent());
        endTurnButtonLeft.setOnAction(_ -> handleEndTurn());

        button1.setOnAction(_ -> handleButton1());
        button2.setOnAction(_ -> handleButton2());
        button3.setOnAction(_ -> handleButton3());
        button4.setOnAction(_ -> handleButton4());
    }

    private void setupVisibility() {
    }

    private void initializePlayerDisplay() {
        loadPlayerUsername();
        loadPlayerShips();
    }

    private void loadShipFXML(StackPane container, ViewPlayer player) {
        try {
            FXMLLoader loader;
            if(gameModel.getShip(ClientGameModel.getInstance().getUsername()).isLearner) {
                loader = new FXMLLoader(getClass().getResource("/fxml/ship0.fxml"));
            }
            else {
                loader = new FXMLLoader(getClass().getResource("/fxml/ship.fxml"));
            }

            container.getChildren().clear();
            Node shipNode = loader.load();

            container.getChildren().removeIf(node -> node instanceof ImageView);

            if (shipNode instanceof StackPane shipStackPane) {
                optimizeShipFitting(container, shipStackPane);
            }

            container.getChildren().add(shipNode);

            Object controller = loader.getController();
            if (controller instanceof ShipController c) {
                c.buildShipComponents(ClientGameModel.getInstance().getShip(player.username));
                c.updateStatisticBoard(player);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private void optimizeShipFitting(StackPane container, StackPane shipNode) {
        container.widthProperty().addListener((_, _, newWidth) -> {
            if (newWidth.doubleValue() > 0) {
                updateShipSize(container, shipNode);
            }
        });

        container.heightProperty().addListener((_, _, newHeight) -> {
            if (newHeight.doubleValue() > 0) {
                updateShipSize(container, shipNode);
            }
        });

        if (container.getWidth() > 0 && container.getHeight() > 0) {
            updateShipSize(container, shipNode);
        }
    }

    private void updateShipSize(StackPane container, StackPane shipNode) {
        double containerWidth = container.getWidth();
        double containerHeight = container.getHeight();

        if (containerWidth <= 0 || containerHeight <= 0) return;

        double padding = 10;
        double availableWidth = containerWidth - padding;
        double availableHeight = containerHeight - padding;

        double shipAspectRatio = 1.3;

        double targetWidth, targetHeight;

        if (availableWidth / availableHeight > shipAspectRatio) {
            targetHeight = availableHeight;
            targetWidth = targetHeight * shipAspectRatio;
        } else {
            targetWidth = availableWidth;
            targetHeight = targetWidth / shipAspectRatio;
        }

        targetWidth = Math.max(60, Math.min(targetWidth, 300));
        targetHeight = Math.max(40, Math.min(targetHeight, 200));

        shipNode.setPrefWidth(targetWidth);
        shipNode.setPrefHeight(targetHeight);
        shipNode.setMaxWidth(targetWidth);
        shipNode.setMaxHeight(targetHeight);
    }

    private void initializeGameBoard() {
        StackPane container = gameBoard;
        try {
            FXMLLoader loader;
            if(gameModel.getShip(ClientGameModel.getInstance().getUsername()).isLearner) {
                loader = new FXMLLoader(getClass().getResource("/fxml/board0.fxml"));
            }
            else {
                loader = new FXMLLoader(getClass().getResource("/fxml/board2.fxml"));
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

    public void updateServerMessages(String ignoredMessage) {

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
}