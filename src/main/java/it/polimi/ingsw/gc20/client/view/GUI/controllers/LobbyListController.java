package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.network.common.Client;
import it.polimi.ingsw.gc20.client.view.common.ViewLobby;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;
import it.polimi.ingsw.gc20.server.model.lobby.Lobby;
import org.jline.utils.Log;
import javafx.scene.control.ListCell;
import javafx.util.Callback;

import java.io.Console;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class LobbyListController {

    @FXML
    private ListView<ViewLobby> lobbiesListView;
    
    @FXML
    private Button joinLobbyButton;
    
    @FXML
    private Button refreshButton;
    
    @FXML
    private Button backButton;

    @FXML
    private void initialize() {
        lobbiesListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        lobbiesListView.setCellFactory(listView -> new ListCell<ViewLobby>() {
            @Override
            protected void updateItem(ViewLobby lobby, boolean empty) {
                super.updateItem(lobby, empty);

                if (empty || lobby == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Qui definisci come vuoi visualizzare l'oggetto ViewLobby
                    setText(lobby.getOwner() + " (Proprietario: " + lobby.getOwner() + ") - " +
                            "Giocatori: " + lobby.getPlayersList() + "/" + lobby.getMaxPlayers());
                }
            }
        });


        lobbiesListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            joinLobbyButton.setDisable(newVal == null);
        });
        
        joinLobbyButton.setOnAction(event -> onJoinLobby());
        refreshButton.setOnAction(event -> onRefreshLobbies());
        backButton.setOnAction(event -> onBack());
        
        loadLobbies();
    }
    
    private void loadLobbies() {
        // Implementazione per caricare le lobby dal server
        if (ClientGameModel.getInstance().getClient() != null) {
            List<ViewLobby> lobbies = ClientGameModel.getInstance().getLobbyList();
            lobbiesListView.getItems().clear();
            lobbiesListView.getItems().addAll(lobbies);
        }
    }
    
    private void onJoinLobby() {
        ViewLobby selectedLobby = lobbiesListView.getSelectionModel().getSelectedItem();
        if (selectedLobby != null) {
            // 1. Invia richiesta al server per entrare nella lobby
            joinLobbyOnServer(selectedLobby);
        }
    }
    
    private void joinLobbyOnServer(ViewLobby lobby) {
        Client client = ClientGameModel.getInstance().getClient();
        if (client != null) {
            try {
                client.joinLobby(lobby.getID(), lobby.getOwner());
            } catch (java.rmi.RemoteException e){
                System.out.println("Errore di connessione al server: " + e.getMessage());
            }
        }
    }
    
    private void onRefreshLobbies() {
        loadLobbies();
    }
    
    private void onBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainMenu.fxml"));
            Parent mainMenuRoot = loader.load();
            
            MainMenuController mainMenuController = loader.getController();
            // TODO Disconnect client

            Stage stage = (Stage) backButton.getScene().getWindow();
            
            Scene mainMenuScene = new Scene(mainMenuRoot);
            stage.setScene(mainMenuScene);
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}