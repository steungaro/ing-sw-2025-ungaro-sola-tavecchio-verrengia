package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.ClientController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;
import it.polimi.ingsw.gc20.server.model.lobby.Lobby;

import java.io.IOException;
import java.util.List;

public class LobbyListController {

    @FXML
    private ListView<Lobby> lobbiesListView;
    
    @FXML
    private Button joinLobbyButton;
    
    @FXML
    private Button refreshButton;
    
    @FXML
    private Button backButton;
    
    private ClientController clientController;
    
    public void setClientController(ClientController clientController) {
        this.clientController = clientController;
    }
    
    @FXML
    private void initialize() {
        lobbiesListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        
        lobbiesListView.setCellFactory(// TODO);

        lobbiesListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            joinLobbyButton.setDisable(newVal == null);
        });
        
        joinLobbyButton.setOnAction(event -> onJoinLobby());
        refreshButton.setOnAction(event -> onRefreshLobbies());
        backButton.setOnAction(event -> onBack());
        
        // Carica inizialmente le lobby
        loadLobbies();
    }
    
    private void loadLobbies() {
        // Implementazione per caricare le lobby dal server
        if (clientController != null) {
            List<Lobby> lobbies = clientController.getAvailableLobbies();
            lobbiesListView.getItems().clear();
            // lobbiesListView.getItems().addAll(lobbies);
        }
    }
    
    private void onJoinLobby() {
        Lobby selectedLobby = lobbiesListView.getSelectionModel().getSelectedItem();
        if (selectedLobby != null) {
            // 1. Invia richiesta al server per entrare nella lobby
            boolean joinSuccessful = joinLobbyOnServer(selectedLobby);
            
            // 2. Se l'ingresso è avvenuto con successo, naviga alla schermata InLobby
            if (joinSuccessful) {
                navigateToInLobbyScreen(selectedLobby);
            }
        }
    }
    
    private boolean joinLobbyOnServer(Lobby lobby) {
        // Implementa la comunicazione con il server per entrare nella lobby
        if (clientController != null) {
            // return clientController.joinLobby(lobby.getId());
        }
        
        // Placeholder per test
        return true;
    }
    
    private void navigateToInLobbyScreen(Lobby lobby) {
        try {
            // Carica l'FXML per la schermata InLobby
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/InLobby.fxml"));
            Parent inLobbyRoot = loader.load();
            
            // Ottieni il controller e inizializzalo con i dati della lobby
            InLobbyController inLobbyController = loader.getController();
            inLobbyController.setClientController(clientController);
            inLobbyController.setupLobby(lobby);
            
            // Ottieni lo Stage corrente dalla scena attuale
            Stage stage = (Stage) joinLobbyButton.getScene().getWindow();
            
            // Crea e imposta la nuova scena
            Scene inLobbyScene = new Scene(inLobbyRoot);
            stage.setScene(inLobbyScene);
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
            // Mostra un messaggio di errore all'utente
            // Ad esempio, usando un Alert
        }
    }
    
    private void onRefreshLobbies() {
        // Aggiorna l'elenco delle lobby
        loadLobbies();
    }
    
    private void onBack() {
        try {
            // Carica l'FXML per la schermata del menu principale
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainMenu.fxml"));
            Parent mainMenuRoot = loader.load();
            
            // Ottieni il controller e inizializzalo
            MainMenuController mainMenuController = loader.getController();
            mainMenuController.setClientController(clientController);
            
            // Ottieni lo Stage corrente dalla scena attuale
            Stage stage = (Stage) backButton.getScene().getWindow();
            
            // Crea e imposta la nuova scena
            Scene mainMenuScene = new Scene(mainMenuRoot);
            stage.setScene(mainMenuScene);
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
            // Mostra un messaggio di errore all'utente
        }
    }
}