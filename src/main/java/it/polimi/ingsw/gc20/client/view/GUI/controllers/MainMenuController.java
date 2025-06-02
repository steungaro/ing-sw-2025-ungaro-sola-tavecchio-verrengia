package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.network.common.Client;
import it.polimi.ingsw.gc20.client.view.GUI.GUIView;
import it.polimi.ingsw.gc20.client.view.common.ViewLobby;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.rmi.RemoteException;
import java.util.List;

public class MainMenuController {

    @FXML
    private ListView<ViewLobby> lobbiesListView;

    @FXML
    private Button joinLobbyButton;

    @FXML
    private Button refreshButton;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Button viewLobbiesButton;

    @FXML
    private Button createLobbyButton;

    @FXML
    private Button logoutButton;

    private GUIView guiView;
    private String username;

    @FXML
    public void initialize() {
        guiView = (GUIView) ClientGameModel.getInstance();
        lobbiesListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        lobbiesListView.setCellFactory(listView -> new ListCell<ViewLobby>() {
            @Override
            protected void updateItem(ViewLobby lobby, boolean empty) {
                super.updateItem(lobby, empty);

                if (empty || lobby == null) {
                    setText(null);
                    setGraphic(null);
                } else {
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
        //loadLobbies();

        createLobbyButton.setOnAction(event -> handleCreateLobby());
        logoutButton.setOnAction(event -> handleLogout());
    }

    private void onJoinLobby() {
        ViewLobby selectedLobby = lobbiesListView.getSelectionModel().getSelectedItem();
        if (selectedLobby != null) {
            joinLobbyOnServer(selectedLobby);
        }
    }

    private void onRefreshLobbies() {
         loadLobbies();
    }

    private void loadLobbies() {
        try {
            ClientGameModel.getInstance().getClient().getLobbies(ClientGameModel.getInstance().getUsername());
        } catch (RemoteException e){
            System.out.println("Errore di connessione al server: " + e.getMessage());
        }
    }

    private void joinLobbyOnServer(ViewLobby lobby) {
        Client client = ClientGameModel.getInstance().getClient();
        if (client != null) {
            try {
                client.joinLobby(lobby.getID(), ClientGameModel.getInstance().getUsername());
            } catch (java.rmi.RemoteException e){
                System.out.println("Errore di connessione al server: " + e.getMessage());
            }
        }
    }

    public void setUsername(String username) {
        this.username = username;
        welcomeLabel.setText("Benvenuto, " + username + "!");
    }

    private void handleCreateLobby() {
        guiView.showScene("createLobby");
    }
    private void handleLogout() {

        guiView.showScene("network");
    }
}