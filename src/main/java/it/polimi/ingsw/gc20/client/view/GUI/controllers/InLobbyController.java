package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.ClientController;
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

    private Lobby currentLobby;
    private boolean isOwner = false;
    private String currentUsername;

    @FXML
    private void initialize() {
        startGameButton.setOnAction(event -> onStartGame());
        leaveLobbyButton.setOnAction(event -> onLeaveLobby());
    }

    public void setupLobby(Lobby lobby, String username) {
        this.currentLobby = lobby;
        this.currentUsername = username;

        this.isOwner = username.equals(lobby.getOwnerUsername());

        lobbyTitleLabel.setText("LOBBY DI " + lobby.getOwnerUsername().toUpperCase());
        updatePlayerCount();

        ownerControlsBox.setVisible(isOwner);
        waitingMessageLabel.setVisible(!isOwner);

        if (isOwner) {
            updateStartButtonState();
        }

        loadPlayers();

        startLobbyUpdateTimer();
    }

    private void updatePlayerCount() {
        playerCountLabel.setText(String.format("%d/%d giocatori",
                currentLobby.getUsers(),
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
        // TODO: Implementa il timer per aggiornare lo stato della lobby
    }

    private void onStartGame() {
        if (!isOwner) return;

        boolean gameStarted = clientController.startGame(currentLobby.getId());

        if (gameStarted) {
            navigateToGameScreen();
        } else {
            System.out.println("Impossibile avviare la partita");
        }
    }

    private void onLeaveLobby() {
        boolean leftSuccessfully = clientController.leaveLobby(currentLobby.getId());

        if (leftSuccessfully) {
            navigateToLobbyListScreen();
        } else {
            System.out.println("Impossibile uscire dalla lobby");
        }
    }

    private void navigateToGameScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GameScreen.fxml"));
            Parent root = loader.load();

            GameScreenController controller = loader.getController();
            controller.setClientController(clientController);
            controller.setupGame(currentLobby);

            Stage stage = (Stage) startGameButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void navigateToLobbyListScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LobbyList.fxml"));
            Parent root = loader.load();

            LobbyListController controller = loader.getController();
            controller.setClientController(clientController);

            Stage stage = (Stage) leaveLobbyButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}