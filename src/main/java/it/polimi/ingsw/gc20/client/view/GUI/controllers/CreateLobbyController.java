package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.GUI.GUIView;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class CreateLobbyController {

    @FXML
    private TextField lobbyNameField;

    @FXML
    private ComboBox<Integer> numPlayersComboBox;

    @FXML
    private ComboBox<String> levelComboBox;

    @FXML
    private Button createButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Label errorLabel;


    /**
     * Initializes the create lobby controller and sets up the user interface components.
     * This method is called automatically by JavaFX after loading the FXML file.
     * It performs the following operations:
     * - Populates the number of players combo box with options 2, 3, and 4 (default: 2)
     * - Populates the level combo box with options "L" (Learner) and "2" (Advanced) (default: "L")
     * - Hides the error label initially
     * - Sets up the cancel button event handler
     */
    @FXML
    public void initialize() {
        numPlayersComboBox.getItems().addAll(2, 3, 4);
        numPlayersComboBox.setValue(2);

        levelComboBox.getItems().addAll("L", "2");
        levelComboBox.setValue("L");

        errorLabel.setVisible(false);

        cancelButton.setOnAction(_ -> handleCancelButton());
    }


    @FXML
    private void handleCreateButton() {
        String lobbyName = lobbyNameField.getText().trim();

        if (lobbyName.isEmpty()) {
            errorLabel.setText("Lobby name cannot be empty");
            errorLabel.setVisible(true);
            return;
        }

        createButton.setDisable(true);
        errorLabel.setVisible(false);

        Integer numPlayers = numPlayersComboBox.getValue();
        int level = levelComboBox.getValue().equals("L") ? 0 : 2;

        new Thread(() -> {
            try {
                ClientGameModel.getInstance().getClient().createLobby(lobbyName, ClientGameModel.getInstance().getUsername(), numPlayers, level);

            } catch (Exception e) {
                javafx.application.Platform.runLater(() -> {
                    errorLabel.setText("Error creating lobby: " + e.getMessage());
                    errorLabel.setVisible(true);
                    createButton.setDisable(false);
                });
            }
        }).start();
    }

    @FXML
    private void handleCancelButton() {
        ((GUIView)ClientGameModel.getInstance()).showScene("mainMenu");

    }
}