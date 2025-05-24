package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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

    private String username;


    @FXML
    public void initialize() {
        numPlayersComboBox.getItems().addAll(2, 3, 4);
        numPlayersComboBox.setValue(2);

        levelComboBox.getItems().addAll("L", "2");
        levelComboBox.setValue("L");

        errorLabel.setVisible(false);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @FXML
    private void handleCreateButton() {
        String lobbyName = lobbyNameField.getText().trim();

        if (lobbyName.isEmpty()) {
            errorLabel.setText("Il nome della lobby non può essere vuoto");
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

                String createdLobbyName = lobbyName;
                long startTime = System.currentTimeMillis();
                boolean success = false;

                try {
                    // Attendi fino a 10 secondi per la creazione della lobby
                    while (System.currentTimeMillis() - startTime < 10000) {
                        var lobbyList = ClientGameModel.getInstance().getLobbyList();

                        // Controlla che lobbyList non sia null prima di usarlo
                        if (lobbyList != null && lobbyList.stream()
                                .anyMatch(lobby -> lobby.getID().equals(createdLobbyName))) {
                            success = true;
                            break;
                        }
                        Thread.sleep(100); // Polling interval
                    }

                    boolean finalSuccess = success;
                    javafx.application.Platform.runLater(() -> {
                        if (finalSuccess) {
                            errorLabel.setText("Lobby creata con successo!");
                            errorLabel.setStyle("-fx-text-fill: green;");
                        } else {
                            errorLabel.setText("Timeout: la lobby potrebbe non essere stata creata");
                        }
                        errorLabel.setVisible(true);
                        createButton.setDisable(false);
                    });
                } catch (InterruptedException e) {
                    javafx.application.Platform.runLater(() -> {
                        errorLabel.setText("Operazione interrotta");
                        errorLabel.setVisible(true);
                        createButton.setDisable(false);
                    });
                }
            } catch (Exception e) {
                javafx.application.Platform.runLater(() -> {
                    errorLabel.setText("Errore nella creazione della lobby: " + e.getMessage());
                    errorLabel.setVisible(true);
                    createButton.setDisable(false);
                });
            }
        }).start();
    }

    @FXML
    private void handleCancelButton() {
        closeStage();
    }

    private void closeStage() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}