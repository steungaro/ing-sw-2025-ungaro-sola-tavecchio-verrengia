package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.GUI.GUIView;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    @FXML
    private HBox shipsButtonContainer;

    @FXML
    private Label playerInfoLabel;

    @FXML
    private Label messageLabel;

    @FXML
    private StackPane gameContent;

    private GUIView guiView;
    private Map<String, Button> playerShipButtons = new HashMap<>();
    private String currentPlayer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        messageLabel.setText("Welcome to the game!");
        changeBoardView();
    }

    public <T> T changeView(String fxmlPath) {
        try {
            gameContent.getChildren().clear();

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Node nuovaVista = loader.load();

            gameContent.getChildren().add(nuovaVista);

            return loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            displayMessage("Error loading view: " + e.getMessage());
            return null;
        }
    }

    public void changeShipView(int giocatoreId) {
    }

    public void changeBoardView() {
        if(ClientGameModel.getInstance().getCurrentLobby().getLevel()==0){
            changeView("/fxml/ship0.fxml");
        } else {
            changeView("/fxml/ship2.fxml");
        }
    }

    public void setPlayerInfo(String username) {
        this.currentPlayer = username;
        playerInfoLabel.setText("Player: " + username);
    }

    public void addPlayerShips(List<String> playerNames) {
        shipsButtonContainer.getChildren().clear();
        playerShipButtons.clear();

        for (String playerName : playerNames) {
            Button shipButton = new Button(playerName);
            shipButton.setStyle("-fx-background-color: #4a5363; -fx-text-fill: white;");

            shipButton.setOnAction(event -> showPlayerShip(playerName));

            playerShipButtons.put(playerName, shipButton);
            shipsButtonContainer.getChildren().add(shipButton);
        }

        if (currentPlayer != null && playerShipButtons.containsKey(currentPlayer)) {
            highlightCurrentShip(currentPlayer);
        }
    }

    private void showPlayerShip(String playerName) {
        playerShipButtons.values().forEach(button ->
                button.setStyle("-fx-background-color: #4a5363; -fx-text-fill: white;"));

        highlightCurrentShip(playerName);

        displayMessage("Show " + playerName + "'s ship");
    }

    private void highlightCurrentShip(String playerName) {
        Button selectedButton = playerShipButtons.get(playerName);
        if (selectedButton != null) {
            selectedButton.setStyle("-fx-background-color: #7a9eb3; -fx-text-fill: white;");
        }
    }

    public void displayMessage(String message) {
        messageLabel.setText(message);
    }
}