package it.polimi.ingsw.gc20.client.view.GUI;

import it.polimi.ingsw.gc20.client.view.GUI.controllers.*;
import it.polimi.ingsw.gc20.client.view.TUI.MenuState;
import it.polimi.ingsw.gc20.client.view.common.ViewLobby;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.network.NetworkManager;
import it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards.ViewAdventureCard;
import it.polimi.ingsw.gc20.client.view.common.localmodel.board.ViewBoard;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;
import it.polimi.ingsw.gc20.server.model.cards.FireType;
import it.polimi.ingsw.gc20.server.model.cards.Planet;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.application.Platform;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public class GUIView extends ClientGameModel {

    public enum GuiState {
        WELCOME("welcome"),
        LOGIN("login"),
        NETWORK("network"),
        MAIN_MENU("mainMenu"),
        IN_LOBBY("inLobby"),
        BUILDING_PHASE("buildingPhase"),
        BUILDING_PHASE0("buildingPhase0"),
        BUILDING_PHASE2("buildingPhase2"),
        BRANCH_MENU("branchMenu"),
        VALIDATION_MENU("validationMenu"),
        POPULATE_SHIP_MENU("populateShipMenu"),
        ROLL_DICE_MENU("rollDiceMenu"),
        PLANET_MENU("planetMenu"),
        SHIELDS_MENU("shieldsMenu"),
        LOSE_CREW_MENU("loseCrewMenu"),
        LOSE_ENERGY_MENU("loseEnergyMenu"),
        KEEP_PLAYING_MENU("KeepPlayingMenu"),
        IDLE_MENU("idleMenu"),
        BOARD0("board0"),
        SHIP0("ship0"),
        SHIP2("ship2");

        private final String fxmlFileName;

        GuiState(String fxmlFileName) {
            this.fxmlFileName = fxmlFileName;
        }

        public String getFxmlFileName() {
            return fxmlFileName;
        }
    }

    private GuiState currentGuiState;

    private final ObjectProperty<GuiState> currentGuiStateProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<ViewBoard> boardProperty = new SimpleObjectProperty<>();
    private static Stage primaryStage;

    public GUIView() throws RemoteException {
        super();
        currentGuiStateProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Platform.runLater(() -> {
                    if (primaryStage != null) {
                        if (primaryStage.getScene() != null &&
                                primaryStage.getScene().getRoot() != null &&
                                primaryStage.getScene().getRoot().getId() != null &&
                                primaryStage.getScene().getRoot().getId().equals(newValue.getFxmlFileName())) {
                            return;
                        }
                        showScene(newValue.getFxmlFileName());
                    } else {
                        System.err.println("primaryStage not initialized: " + newValue.getFxmlFileName());
                    }
                });
            }
        });

        boardProperty.addListener((observable, oldBoard, newBoard) -> {
            if (newBoard != null) {
                notifyCurrentBoardController(newBoard);
            }
        });
    }

    public GuiState getCurrentGuiState() {
        return currentGuiStateProperty.get();
    }

    public void setCurrentGuiState(GuiState newState) {
        currentGuiStateProperty.set(newState);
    }

    @Override
    public void displayErrorMessage(String message) {
        Platform.runLater(() -> {
            if (primaryStage == null) {
                System.err.println("Error: primaryStage not initialized: " + message);
                return;
            }
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.initOwner(primaryStage);
            alert.showAndWait();
        });
    }

    public void initGUI(Stage stage) {
        primaryStage = stage;
        setCurrentGuiState(GuiState.WELCOME); // Imposta lo stato iniziale, triggerando il listener
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down GUIView...");
            ClientGameModel.getInstance().shutdown();
        }));
        stage.setOnCloseRequest(_ -> {
            System.out.println("Close request received.");
            if (ClientGameModel.getInstance() != null) {
                ClientGameModel.getInstance().shutdown();
            }
            Platform.exit();
            System.exit(0);
        });
    }

    public FXMLLoader showScene(String fileName) {
        try {
            String path = "/fxml/" + fileName + ".fxml";
            URL resourceUrl = getClass().getResource(path);

            if (resourceUrl == null) {
                System.err.println("ERROR: FXML file not found: " + path);
                displayErrorMessage("Internal error: UI file not found (" + fileName + ")");
                return null;
            }

            FXMLLoader loader = new FXMLLoader(resourceUrl);
            Parent root = loader.load();
            root.setId(fileName);

            Scene currentScene = primaryStage.getScene();
            Scene newScene;

            if (currentScene == null) {
                newScene = new Scene(root, 1000, 700);
                primaryStage.setScene(newScene);
                primaryStage.centerOnScreen();
            } else {
                newScene = new Scene(root, currentScene.getWidth(), currentScene.getHeight());
                primaryStage.setScene(newScene);
            }

            primaryStage.setTitle("Galaxy Trucker");
            root.setId(fileName);
            return loader;
        } catch (IOException e) {
            e.printStackTrace();
            displayErrorMessage("Error loading UI: " + e.getMessage());
        }
        return null;
    }

    private void notifyCurrentBoardController(ViewBoard newBoard) {
        Platform.runLater(() -> {
            if (primaryStage != null && primaryStage.getScene() != null && primaryStage.getScene().getRoot() != null) {
                Object controller = primaryStage.getScene().getRoot().getUserData();
                if (controller instanceof BoardController) {
                    ((BoardController) controller).updateBoardDisplay(newBoard);
                }
            }
        });
    }

    public boolean setupConnection(String ipAddress, int port, boolean isRMI) {
        String clientType = isRMI ? "RMI" : "Socket";

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }

        if (ipAddress.isBlank()) {
            client = NetworkManager.initConnection(clientType);
        } else if (port == 0) {
            client = NetworkManager.initConnection(clientType, ipAddress);
        } else {
            client = NetworkManager.initConnection(clientType, ipAddress, port);
        }

        if (client == null || !client.isConnected()) {
            client = null;
            return false;
        }

        if (client != null) {
            currentGuiState = GuiState.LOGIN;
            return true;
        }
        return false;
    }

    @Override
    public void updateView(Message message) {
        Platform.runLater(() -> {
            try {
                message.handleMessage();
            } catch (Exception e) {
                System.out.println("Error while handling message: " + e.getMessage());
                e.printStackTrace(); // Utile per il debug
                displayErrorMessage("Error processing server message: " + e.getMessage());
            }
        });
    }

    @Override
    public void mainMenuState(){
        setCurrentGuiState(GuiState.MAIN_MENU);
    }

    /*@Override
    public void setShip (String username, ViewShip ship) {
        ships.put(username, ship);
        if (primaryStage.getScene() != null && primaryStage.getScene().getRoot() != null &&
                primaryStage.getScene().getRoot().getId() != null &&
                (currentGuiState.getFxmlFileName().equals("ship0") || currentGuiState.getFxmlFileName().equals("ship2"))) {
            // Get the controller of the current ship view
            ShipController shipController = primaryStage.getScene().getRoot().getUserData() instanceof ShipController ?
                    (ShipController) primaryStage.getScene().getRoot().getUserData() : null;
            if (shipController != null) {
                shipController.buildShipComponents(ClientGameModel.getInstance().getShip(ClientGameModel.getInstance().getUsername()));
            }
        }
    }*/

    @Override
    public void setBoard(ViewBoard board) {
        this.board = board;
        this.boardProperty.set(board);
    }

    @Override
    public void planetMenu(List<Planet> planets) {
        setCurrentGuiState(GuiState.PLANET_MENU);
    }

    @Override
    public void populateShipMenu() {
        setCurrentGuiState(GuiState.POPULATE_SHIP_MENU);
    }

    @Override
    public void automaticAction(String message) {
        // currentGuiState = GuiState.AUTOMATIC_ACTION;
    }

    @Override
    public void validationMenu() {
        setCurrentGuiState(GuiState.VALIDATION_MENU);
    }

    @Override
    public void init() {
        // Ignore
    }

    @Override
    public void shieldsMenu(String message) {
        setCurrentGuiState(GuiState.SHIELDS_MENU);
    }

    @Override
    public void rollDiceMenu(String message) {
        setCurrentGuiState(GuiState.ROLL_DICE_MENU);
    }

    @Override
    public void cargoMenu(int cargoNum) {
        // currentGuiState = GuiState.CARGO_MENU;
    }

    @Override
    public void loseCrewMenu(int crewNum) {
        setCurrentGuiState(GuiState.LOSE_CREW_MENU);
    }

    @Override
    public void removeBatteryMenu(int batteryNum) {
        setCurrentGuiState(GuiState.LOSE_ENERGY_MENU);
    }

    @Override
    public void AssemblingStateMenu() {
        ViewShip playerShip = null;
        if (ClientGameModel.getInstance() != null && ClientGameModel.getInstance().getUsername() != null) {
            playerShip = ClientGameModel.getInstance().getShip(ClientGameModel.getInstance().getUsername());
        }

        if (playerShip != null && playerShip.isLearner) {
            setCurrentGuiState(GuiState.BUILDING_PHASE0);
        } else {
            setCurrentGuiState(GuiState.BUILDING_PHASE2);
        }
    }

    @Override
    public void leaderBoardMenu(Map<String, Integer> leaderBoard) {
        // currentGuiState = GuiState.LEADER_BOARD_MENU;
    }

    @Override
    public void loginSuccessful(String username) {
        this.username = username;
        this.loggedIn = true;
        setCurrentGuiState(GuiState.MAIN_MENU);
    }

    @Override
    public void loginFailed(String username) {
        Platform.runLater(() -> {
            if (getCurrentGuiState() == GuiState.LOGIN) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Failed");
                alert.setHeaderText("Login attempt for user '" + username + "' failed.");
                alert.setContentText("Please check your username and try again.");
                if (primaryStage != null) alert.initOwner(primaryStage);
                alert.showAndWait();
            } else {
                displayErrorMessage("Login failed for user: " + username + " (unexpected state: " + getCurrentGuiState() + ")");
            }
        });
    }

    @Override
    public void idleMenu(String message) {
        setCurrentGuiState(GuiState.IDLE_MENU);
    }

    @Override
    public void keepPlayingMenu() {
        setCurrentGuiState(GuiState.KEEP_PLAYING_MENU);
    }

    @Override
    public void notifyDisconnection() throws RemoteException {
        Platform.runLater(() -> {
            displayErrorMessage("Disconnected from the server.");
            if (client != null) {
                client.stop();
            }
            client = null;
            loggedIn = false;
            username = null;
            setCurrentGuiState(GuiState.NETWORK);
        });
    }

    @Override
    public void shutdown() {
        System.out.println("GUIView shutdown method called.");
        if (client != null) {
            client.stop();
            client = null;
        }
    }

    @Override
    public void branchMenu() {
        setCurrentGuiState(GuiState.BRANCH_MENU);
    }

    @Override
    public void buildingMenu(List<ViewAdventureCard> cards) {
        setCurrentGuiState(GuiState.BUILDING_PHASE);
    }

    @Override
    public void cannonsMenu(String message) {
        // currentGuiState = GuiState.CANNONS_MENU;
    }

    @Override
    public void cardAcceptanceMenu(String message) {
        // currentGuiState = GuiState.CARD_ACCEPTANCE_MENU;
    }

    @Override
    public void cargoMenu(String message, int cargoToLose, List<CargoColor> cargoToGain, boolean losing) {
        // currentGuiState = GuiState.CARGO_MENU;
    }

    @Override
    public void engineMenu(String message) {
        // currentGuiState = GuiState.ENGINE_MENU;
    }

    @Override
    public void inLobbyMenu() {
        setCurrentGuiState(GuiState.IN_LOBBY);
    }

    @Override
    public void login() {
        currentGuiState = GuiState.LOGIN;
        // TODO Try to remove this line, put it in the controller instead
        client.login(username);
        this.username = username;
    }
}