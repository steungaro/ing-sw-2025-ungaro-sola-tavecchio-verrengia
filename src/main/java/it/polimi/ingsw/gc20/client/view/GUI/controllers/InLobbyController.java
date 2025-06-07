package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.GUI.GUIView;
import it.polimi.ingsw.gc20.client.view.common.ViewLobby;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.application.Platform;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class InLobbyController {

    private ScheduledExecutorService scheduler;

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

    private ViewLobby currentLobby;
    private boolean isOwner = false;
    private String currentUsername;
    private final ClientGameModel clientController = ClientGameModel.getInstance();

    @FXML
    public void initialize() {
        initLobbyData(clientController.getCurrentLobby(), clientController.getUsername());
        startPeriodicUpdates();
    }

    public void initLobbyData(ViewLobby lobby, String username) {
        if (lobby != null) {
            this.currentLobby = lobby;
            lobbyTitleLabel.setText("Lobby: " + lobby.getID());
            currentUsername = username;

            playerCountLabel.setText(String.format("%d/%d",
                    lobby.getPlayersList().size(),
                    lobby.getMaxPlayers()));

            updatePlayerList(lobby, username);

            isOwner = username.equals(lobby.getOwner());
            waitingMessageLabel.setVisible(!isOwner);
            waitingMessageLabel.setManaged(!isOwner);
            ownerControlsBox.setVisible(isOwner);
            leaveLobbyButton.setVisible(!isOwner);
            ownerControlsBox.setManaged(isOwner);
            leaveLobbyButton.setManaged(!isOwner);
            ownerControlsBox.setDisable(!isOwner);
            killLobbyButton.setDisable(!isOwner);

            updateStartButtonState();
        } else {
            lobbyTitleLabel.setText("Error: Lobby data not available");
        }
    }

    private void startPeriodicUpdates() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }

        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> Platform.runLater(() -> {
            if (currentLobby != null) {
                ViewLobby updatedLobby = clientController.getCurrentLobby();
                if (updatedLobby != null) {
                    updatePlayerList(updatedLobby, currentUsername);

                    playerCountLabel.setText(String.format("%d/%d",
                            updatedLobby.getPlayersList().size(),
                            updatedLobby.getMaxPlayers()));
                    currentLobby = updatedLobby;
                    updateStartButtonState();
                }
            }
        }), 0, 3, TimeUnit.SECONDS);
    }

    private void stopPeriodicUpdates() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }

    private void updatePlayerList(ViewLobby lobby, String username) {
        playersListView.getItems().clear();
        for (String player : lobby.getPlayersList()) {
            String displayName = player;
            if (player.equals(lobby.getOwner())) {
                displayName += " (Owner)";
            }
            if (player.equals(username)) {
                displayName += " (You)";
            }
            playersListView.getItems().add(displayName);
        }
    }

    private void updateStartButtonState() {
        boolean canStart = currentLobby != null && currentLobby.getPlayersList().size() >= 2;
        startGameButton.setDisable(!canStart);

        if (!canStart) {
            startGameButton.setStyle("-fx-background-color: #555555; -fx-text-fill: #aaaaaa;");
            startGameButton.setText("Waiting for other players...");
        } else {
            startGameButton.setStyle("-fx-background-color: #4a7eb3; -fx-text-fill: white;");
            startGameButton.setText("Start Game");
        }
    }

    @FXML
    public void onStartGame() {
        if (!isOwner) return;

        try {
            ClientGameModel.getInstance().getClient().startLobby(currentUsername);
            ((GUIView)ClientGameModel.getInstance()).showScene("game");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onLeaveLobby() {
        try {
            stopPeriodicUpdates();
            ClientGameModel.getInstance().getClient().leaveLobby(currentUsername);
            ((GUIView)ClientGameModel.getInstance()).showScene("login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onKillLobby() {
        if (!isOwner) return;

        try {
            stopPeriodicUpdates();
            ClientGameModel.getInstance().getClient().killLobby(currentUsername);
            ((GUIView)ClientGameModel.getInstance()).showScene("login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}