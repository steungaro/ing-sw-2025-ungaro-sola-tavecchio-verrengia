
package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.GUI.GUIView;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import it.polimi.ingsw.gc20.server.model.lobby.Lobby;
import it.polimi.ingsw.gc20.server.model.player.Player;

import java.io.IOException;
import java.util.List;

public class InLobbyController {

    @FXML
    private Label lobbyTitleLabel;

    @FXML
    private Label waitingMessageLabel;

    @FXML
    private Label playerCountLabel;

    @FXML
    private ListView<String> playersListView;

    @FXML
    private VBox ownerControlsBox;

    @FXML
    private Button startGameButton;

    @FXML
    private Button leaveLobbyButton;

    @FXML
    private Button killLobbyButton;

    private Lobby currentLobby;
    private boolean isOwner = false;
    private String currentUsername;
    private ClientGameModel clientController = ClientGameModel.getInstance();

    @FXML
    private void initialize() {
        setupLobby(currentLobby, currentUsername);
        startGameButton.setOnAction(event -> onStartGame());
        leaveLobbyButton.setOnAction(event -> onLeaveLobby());
        killLobbyButton.setOnAction(event -> onKillLobby());
    }

    public void setupLobby(Lobby lobby, String username) {
        this.currentLobby = lobby;
        this.currentUsername = username;

        this.isOwner = username.equals(lobby.getOwnerUsername());

        lobbyTitleLabel.setText("LOBBY DI " + lobby.getOwnerUsername().toUpperCase());
        updatePlayerCount();

        ownerControlsBox.setVisible(isOwner);
        waitingMessageLabel.setVisible(!isOwner);
        killLobbyButton.setVisible(isOwner);

        if (isOwner) {
            updateStartButtonState();
            leaveLobbyButton.setText("ESCI DALLA LOBBY");
        } else {
            leaveLobbyButton.setText("LASCIA LOBBY");
        }

        loadPlayers();
        startLobbyUpdateTimer();

        System.out.println("Lobby attuale: " + ClientGameModel.getInstance().getCurrentLobby());
    }

    private void updatePlayerCount() {
        playerCountLabel.setText(String.format("%d/%d giocatori",
                currentLobby.getUsers().size(),
                currentLobby.getMaxPlayers()));
    }

    private void updateStartButtonState() {
        boolean canStart = currentLobby.getUsers().size() >= 2;
        startGameButton.setDisable(!canStart);

        if (!canStart) {
            startGameButton.setStyle("-fx-background-color: #555555; -fx-text-fill: #aaaaaa;");
            startGameButton.setText("IN ATTESA DI ALTRI GIOCATORI");
        } else {
            startGameButton.setStyle("-fx-background-color: #4a7eb3; -fx-text-fill: white;");
            startGameButton.setText("AVVIA PARTITA");
        }
    }

    private void loadPlayers() {
        playersListView.getItems().clear();

        List<String> players = currentLobby.getUsers();

        for (String player : players) {
            String displayName = player;
            if (player.equals(currentLobby.getOwnerUsername())) {
                displayName += " (Proprietario)";
            }
            if (player.equals(currentUsername)) {
                displayName += " (Tu)";
            }
            playersListView.getItems().add(displayName);
        }
    }

    private void startLobbyUpdateTimer() {
        // TODO: Maybe
    }

    private void onStartGame() {
        if (!isOwner) return;

        try {
            ClientGameModel.getInstance().getClient().startLobby(currentUsername);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        ((GUIView)ClientGameModel.getInstance()).showScene("game");
    }

    private void onLeaveLobby() {

        try {
            ClientGameModel.getInstance().getClient().leaveLobby(currentUsername);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        ((GUIView)ClientGameModel.getInstance()).showScene("login");
    }

    private void onKillLobby() {
        if (!isOwner) return;

        try {
            ClientGameModel.getInstance().getClient().killLobby(currentUsername);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        ((GUIView)ClientGameModel.getInstance()).showScene("login");
    }
}