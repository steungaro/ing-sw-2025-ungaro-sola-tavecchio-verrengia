package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.ViewLobby;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.GameModelListener;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ViewPlayer;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.util.List;

public class InLobbyController implements GUIController, GameModelListener {


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

    @FXML
    private Label errorLabel;

    private ViewLobby currentLobby;
    private boolean isOwner = false;
    private String currentUsername;
    private final ClientGameModel clientController = ClientGameModel.getInstance();

    @Override
    public void showError(String errorMessage) {
        errorLabel.setText(errorMessage);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }

    @FXML
    public void initialize() {
        initLobbyData(clientController.getCurrentLobby(), clientController.getUsername());
        clientController.addListener(this);

        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
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

    @Override
    public void onShipUpdated(ViewShip ship) {
        // Not used in this controller
    }

    @Override
    public void onLobbyUpdated(ViewLobby updatedLobby) {
        if (updatedLobby != null) {
            updatePlayerList(updatedLobby, currentUsername);

            playerCountLabel.setText(String.format("%d/%d",
                    updatedLobby.getPlayersList().size(),
                    updatedLobby.getMaxPlayers()));
            currentLobby = updatedLobby;
            updateStartButtonState();

        } else {
            lobbyTitleLabel.setText("Error: Lobby data not available");
        }
    }

    @Override
    public void onPlayerListUpdated(List<ViewPlayer> players) {
        // This method is not used in this controller, as player list updates are handled through the lobby update
    }

    @Override
    public void onErrorMessageReceived(String message) {
        showError(message);
    }

    @Override
    public void onComponentInHandUpdated(ViewComponent component) {
        // Not used in this controller
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
        } catch (Exception e) {
            showError("Error starting game: " + e.getMessage());
        }
    }

    @FXML
    public void onLeaveLobby() {
        try {
            ClientGameModel.getInstance().getClient().leaveLobby(currentUsername);
        } catch (Exception e) {
            showError("Error leaving lobby: " + e.getMessage());
        }
    }

    @FXML
    public void onKillLobby() {
        if (!isOwner) return;
        try {
            ClientGameModel.getInstance().getClient().killLobby(currentUsername);
        } catch (Exception e) {
            showError("Error killing lobby: " + e.getMessage());
        }
    }
}