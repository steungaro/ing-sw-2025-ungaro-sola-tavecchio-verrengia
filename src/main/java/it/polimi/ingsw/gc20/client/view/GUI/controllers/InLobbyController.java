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

    /**
     * Initializes the lobby controller and sets up the user interface.
     * This method is called automatically by JavaFX after loading the FXML file.
     * It performs the following operations:
     * - Initializes the lobby data using the current lobby and username from the game model
     * - Starts periodic updates to keep the lobby information synchronized
     */
    @FXML
    public void initialize() {
        initLobbyData(clientController.getCurrentLobby(), clientController.getUsername());
        startPeriodicUpdates();
    }

    /**
     * Displays an error message to the user in the lobby interface.
     * This method updates the waiting message label to show error information
     * when lobby operations fail or encounter issues.
     * 
     * @param message the error message to display to the user
     */
    public void showError(String message) {
        waitingMessageLabel.setText(message);
    }

    /**
     * Initializes the lobby data and configures the user interface based on the lobby state.
     * This method sets up the lobby interface with the following information:
     * - Lobby title and ID
     * - Player count display (current/maximum)
     * - Player list with owner and current user indicators
     * - UI visibility and controls based on whether the user is the lobby owner
     * - Start game button state based on minimum player requirements
     * 
     * @param lobby the lobby data to display
     * @param username the current user's username
     */
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

    /**
     * Handles the start game button click event.
     * This FXML event handler attempts to start the game if the current user is the lobby owner.
     * Only the lobby owner can initiate game start. If an error occurs during the start process,
     * an error message is displayed to the user.
     */
    @FXML
    public void onStartGame() {
        if (!isOwner) return;
        try {
            ClientGameModel.getInstance().getClient().startLobby(ClientGameModel.getInstance().getUsername());
        } catch (Exception e) {
            showError("Error starting game: " + e.getMessage());
        }
    }

    /**
     * Handles the leave lobby button click event.
     * This FXML event handler performs the following operations:
     * - Stops periodic updates to prevent memory leaks
     * - Sends a leave lobby request to the server
     * - Returns the user to the login screen
     * 
     * If an error occurs during the leave process, an error message is displayed.
     */
    @FXML
    public void onLeaveLobby() {
        try {
            stopPeriodicUpdates();
            ClientGameModel.getInstance().getClient().leaveLobby(currentUsername);
            ((GUIView)ClientGameModel.getInstance()).showScene("login");
        } catch (Exception e) {
            showError("Error leaving lobby: " + e.getMessage());
        }
    }

    /**
     * Handles the kill lobby button click event.
     * This FXML event handler allows the lobby owner to permanently delete the lobby.
     * It performs the following operations:
     * - Verifies the user is the lobby owner
     * - Stops periodic updates to prevent memory leaks
     * - Sends a kill lobby request to the server
     * - Returns the user to the login screen
     * 
     * Only the lobby owner can perform this action. If an error occurs during the process,
     * an error message is displayed.
     */
    @FXML
    public void onKillLobby() {
        if (!isOwner) return;

        try {
            stopPeriodicUpdates();
            ClientGameModel.getInstance().getClient().killLobby(currentUsername);
            ((GUIView)ClientGameModel.getInstance()).showScene("login");
        } catch (Exception e) {
            showError("Error killing lobby: " + e.getMessage());
        }
    }
}