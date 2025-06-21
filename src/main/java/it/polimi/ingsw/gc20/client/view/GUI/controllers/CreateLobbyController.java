package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.GUI.GUIView;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class CreateLobbyController implements GUIController {

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


    @FXML
    public void initialize() {
        numPlayersComboBox.getItems().addAll(2, 3, 4);
        numPlayersComboBox.setValue(2);

        levelComboBox.getItems().addAll("L", "2");
        levelComboBox.setValue("L");

        errorLabel.setVisible(false);
        errorLabel.setManaged(false);

        createButton.setOnAction(_ -> handleCreateButton());
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

        int numPlayers = numPlayersComboBox.getValue();
        int level = levelComboBox.getValue().equals("L") ? 0 : 2;

        try {
            ClientGameModel.getInstance().getClient().createLobby(lobbyName, ClientGameModel.getInstance().getUsername(), numPlayers, level);
        } catch (Exception e) {
            errorLabel.setText("Error creating lobby: " + e.getMessage());
            errorLabel.setVisible(true);
            errorLabel.setManaged(true);
        }
    }

    @FXML
    private void handleCancelButton() {
        ((GUIView)ClientGameModel.getInstance()).showScene("mainMenu");

    }

    @Override
    public void showError(String errorMessage) {
        errorLabel.setText(errorMessage);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }
}