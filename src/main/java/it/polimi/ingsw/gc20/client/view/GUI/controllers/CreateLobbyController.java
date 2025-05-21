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

    /**
     * Inizializza il controller
     */
    @FXML
    public void initialize() {
        // Inizializza la combobox per il numero di giocatori (2-4)
        numPlayersComboBox.getItems().addAll(2, 3, 4);
        numPlayersComboBox.setValue(2);

        // Inizializza la combobox per il livello (L o 2)
        levelComboBox.getItems().addAll("L", "2");
        levelComboBox.setValue("L");

        // Nasconde l'etichetta di errore all'inizio
        errorLabel.setVisible(false);
    }

    /**
     * Imposta il nome utente
     * @param username il nome utente
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gestisce l'azione del pulsante di creazione lobby
     */
    @FXML
    private void handleCreateButton() {
        String lobbyName = lobbyNameField.getText().trim();
        
        if (lobbyName.isEmpty()) {
            errorLabel.setText("Il nome della lobby non può essere vuoto");
            errorLabel.setVisible(true);
            return;
        }

        Integer numPlayers = numPlayersComboBox.getValue();
        
        // Converti il valore del livello in intero (L = 0, 2 = 2)
        int level = levelComboBox.getValue().equals("L") ? 0 : 2;

        try {
            ClientGameModel.getInstance().getClient().createLobby(lobbyName, username, numPlayers, level);
            closeStage();
        } catch (Exception e) {
            errorLabel.setText("Errore nella creazione della lobby: " + e.getMessage());
            errorLabel.setVisible(true);
        }
    }

    /**
     * Gestisce l'azione del pulsante di annullamento
     */
    @FXML
    private void handleCancelButton() {
        closeStage();
    }

    /**
     * Chiude la finestra corrente
     */
    private void closeStage() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}