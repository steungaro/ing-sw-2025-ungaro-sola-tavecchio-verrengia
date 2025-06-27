package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.network.common.Client;
import it.polimi.ingsw.gc20.client.view.GUI.GUIView;
import it.polimi.ingsw.gc20.client.view.common.ViewLobby;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import it.polimi.ingsw.gc20.client.view.common.localmodel.LobbyListListener;

import java.rmi.RemoteException;
import java.util.List;

public class MainMenuController implements LobbyListListener {

    @FXML
    private ListView<ViewLobby> lobbiesListView;

    @FXML
    private Button joinLobbyButton;

    @FXML
    private Button refreshButton;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Button createLobbyButton;

    private GUIView guiView;

    /**
     * Initializes the main menu controller and sets up the user interface components.
     * This method is called automatically by JavaFX after loading the FXML file.
     * It configures the lobby list view, sets up event handlers for buttons,
     * registers as a lobby list listener, and loads the initial lobby data.
     * The method also configures the list view cell factory to display lobby information
     * in a formatted way showing lobby ID, owner, and player count.
     */
    @FXML
    public void initialize() {
        guiView = (GUIView) ClientGameModel.getInstance();
        lobbiesListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        setUsername( ClientGameModel.getInstance().getUsername());

        lobbiesListView.setCellFactory(_ -> new ListCell<>() {
            @Override
            protected void updateItem(ViewLobby lobby, boolean empty) {
                super.updateItem(lobby, empty);

                if (empty || lobby == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(lobby.getID() + " (Owner: " + lobby.getOwner() + ") - " +
                            "Players: " + lobby.getPlayersList().size() + "/" + lobby.getMaxPlayers());
                }
            }
        });

        ClientGameModel.getInstance().addLobbyListListener(this);

        lobbiesListView.getSelectionModel().selectedItemProperty().addListener((_, _, newVal) -> joinLobbyButton.setDisable(newVal == null));

        joinLobbyButton.setOnAction(_ -> onJoinLobby());
        refreshButton.setOnAction(_ -> onRefreshLobbies());
        loadLobbies();

        createLobbyButton.setOnAction(_ -> handleCreateLobby());
    }

    /**
     * Handles lobby list changes from the game model.
     * This method is called when the lobby list is updated in the game model
     * and refreshes the displayed lobby list to show the most current information.
     * It implements the LobbyListListener interface to respond to lobby list updates.
     */
    @Override
    public void onLobbyListChanged() {
        loadLobbies();
    }

    private void onJoinLobby() {
        ViewLobby selectedLobby = lobbiesListView.getSelectionModel().getSelectedItem();
        if (selectedLobby != null) {
            joinLobbyOnServer(selectedLobby);
        }
    }

    private void onRefreshLobbies() {
         askLobbyList();
    }

    private void loadLobbies() {
        List<ViewLobby> lobbies = ClientGameModel.getInstance().getLobbyList();
        lobbiesListView.getItems().clear();
        if(lobbies!= null)
            lobbiesListView.getItems().addAll(lobbies);
    }

    private void askLobbyList() {
        try {
            ClientGameModel.getInstance().getClient().getLobbies(ClientGameModel.getInstance().getUsername());
        } catch (RemoteException e) {
            System.err.println("Error while fetching lobby list: " + e.getMessage());
        }
    }

    private void joinLobbyOnServer(ViewLobby lobby) {
        Client client = ClientGameModel.getInstance().getClient();
        if (client != null) {
            try {
                client.joinLobby(lobby.getID(), ClientGameModel.getInstance().getUsername());
            } catch (java.rmi.RemoteException e){
                System.err.println("Error joining lobby: " + e.getMessage());
            }
        }
    }

    /**
     * Sets the username display in the welcome message.
     * This method updates the welcome label to show a personalized greeting
     * with the specified username, providing a friendly user experience.
     * 
     * @param username the username to display in the welcome message
     */
    public void setUsername(String username) {
        welcomeLabel.setText("Welcome, " + username + "!");
    }

    private void handleCreateLobby() {
        guiView.showScene("createLobby");
    }
}