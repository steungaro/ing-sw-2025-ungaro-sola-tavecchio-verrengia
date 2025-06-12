package it.polimi.ingsw.gc20.client.view.GUI;

import it.polimi.ingsw.gc20.client.view.GUI.controllers.*;
import it.polimi.ingsw.gc20.client.view.TUI.LoseEnergyMenu;
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
        CARD_ACCEPTANCE_MENU("cardAcceptance"),
        SHIP2("ship2"),
        CARGO_MENU("cargoMenu"),
        CANNONS_MENU("cannonsMenu"),
        LEADER_BOARD_MENU("leaderBoardMenu"),
        AUTOMATIC_ACTION("automaticAction"),
        PEEK_DECKS("peekDecks"),
        ENGINE_MENU("engineMenu");

        private final String fxmlFileName;

        GuiState(String fxmlFileName) {
            this.fxmlFileName = fxmlFileName;
        }

        public String getFxmlFileName() {
            return fxmlFileName;
        }
    }

    private GuiState currentGuiState;

    private Stage peekDecksStage;
    private PeekDecksController peekDecksController;
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
        setCurrentGuiState(GuiState.WELCOME);
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
                e.printStackTrace();
                displayErrorMessage("Error processing server message: " + e.getMessage());
            }
        });
    }

    @Override
    public void mainMenuState(){
        setCurrentGuiState(GuiState.MAIN_MENU);
    }

    @Override
    public void setBoard(ViewBoard board) {
        this.board = board;
        this.boardProperty.set(board);
    }

    @Override
    public void planetMenu(List<Planet> planets) {
        setCurrentGuiState(GuiState.PLANET_MENU);
        Platform.runLater(() -> {
            if (primaryStage != null && primaryStage.getScene() != null && primaryStage.getScene().getRoot() != null) {
                Object controller = primaryStage.getScene().getRoot().getUserData();
                if (controller instanceof LoseCrewMenuController) {
                    ((PlanetMenuController) controller).initializeWithPlanets(planets);
                } else {
                    System.err.println("Controller for PLANET_MENU_CONTROLLER is not of type planetMenu or is null.");
                }
            } else {
                System.err.println("Cannot setup planet menu: primaryStage, scene, or root is null.");
            }
        });
        // TODO: to test, no handler needed
    }

    @Override
    public void populateShipMenu() {
        setCurrentGuiState(GuiState.POPULATE_SHIP_MENU);
        // TODO: to test, no other handler needed
    }

    @Override
    public void automaticAction(String message) {
        // ignore this method for now
        // currentGuiState = GuiState.AUTOMATIC_ACTION;
        // TODO: DECIDE WHATO TO DO
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
        // TODO : Check if message is needed to be passed to the controller (maybe to indicate the side)
    }

    @Override
    public void rollDiceMenu(String message) {
        // setCurrentGuiState(GuiState.ROLL_DICE_MENU);

        Platform.runLater(() -> {
            try {
                Stage dialogStage = new Stage();
                dialogStage.setTitle("Roll Dice");

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/rollDiceMenu.fxml"));
                Parent root = loader.load();

                Scene scene = new Scene(root);
                dialogStage.setScene(scene);

                RollDiceMenuController controller = loader.getController();
                controller.initializeWithMessage(message);

                dialogStage.initModality(javafx.stage.Modality.WINDOW_MODAL);
                dialogStage.initOwner(primaryStage);

                dialogStage.setMinWidth(600);
                dialogStage.setMinHeight(500);
                dialogStage.centerOnScreen();

                dialogStage.show();
            } catch (IOException e) {
                System.err.println("Error uploading rollDiceMenu.fxml: " + e.getMessage());
                e.printStackTrace();
                displayErrorMessage("Error opening roll dice window: " + e.getMessage());
            }
        });
    }

    @Override
    public void cargoMenu(int cargoNum) {
        currentGuiState = GuiState.CARGO_MENU;
        // Necessary?
    }

    @Override
    public void loseCrewMenu(int crewNum) {
        setCurrentGuiState(GuiState.LOSE_CREW_MENU);
        Platform.runLater(() -> {
            if (primaryStage != null && primaryStage.getScene() != null && primaryStage.getScene().getRoot() != null) {
                Object controller = primaryStage.getScene().getRoot().getUserData();
                if (controller instanceof LoseCrewMenuController) {
                    ((LoseCrewMenuController) controller).initializeWithCrewToLose(crewNum);
                } else {
                    System.err.println("Controller for LOSE_CREW_MENU is not of type LoseCreeMenu or is null.");
                }
            } else {
                System.err.println("Cannot setup lose crew menu: primaryStage, scene, or root is null.");
            }
        });
    }

    @Override
    public void removeBatteryMenu(int batteryNum) {
        setCurrentGuiState(GuiState.LOSE_ENERGY_MENU);
        // Necessary?
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
        currentGuiState = GuiState.LEADER_BOARD_MENU;
        // Necessary?
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
        // TODO: Improvment (
    }

    @Override
    public void buildingMenu(List<ViewAdventureCard> cards) {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/peekDecks.fxml"));
                Parent root = loader.load();
                peekDecksController = loader.getController();

                peekDecksStage = new Stage();
                peekDecksStage.setScene(new Scene(root));
                peekDecksStage.setTitle("Peek Decks");
                peekDecksStage.initOwner(primaryStage);
                peekDecksStage.show();

                peekDecksController.initializeWithCards(cards);
            } catch (IOException e) {
                e.printStackTrace();
                displayErrorMessage("Error opening peek decks window: " + e.getMessage());
            }
        });
    }

    @Override
    public void cannonsMenu(String message) {
        currentGuiState = GuiState.CANNONS_MENU;
        // TODO: Check button handler
    }

    @Override
    public void cardAcceptanceMenu(String message) {
        setCurrentGuiState(GuiState.CARD_ACCEPTANCE_MENU);
        // TODO: Check button handler
    }

    @Override
    public void cargoMenu(String message, int cargoToLose, List<CargoColor> cargoToGain, boolean losing) {
        currentGuiState = GuiState.CARGO_MENU;
        Platform.runLater(() -> {
            if (primaryStage != null && primaryStage.getScene() != null && primaryStage.getScene().getRoot() != null) {
                Object controller = primaryStage.getScene().getRoot().getUserData();
                if (controller instanceof CargoMenuController) {
                    ((CargoMenuController) controller).initializeWithParameters(message, cargoToLose, cargoToGain, losing);
                } else {
                    System.err.println("Controller for CARGO_MENU is not of type CargoMenuController or is null.");
                }
            } else {
                System.err.println("Cannot setup cargo menu: primaryStage, scene, or root is null.");
            }
        });
        // TODO: Check button handler
    }

    @Override
    public void engineMenu(String message) {
        currentGuiState = GuiState.ENGINE_MENU;
        // TODO: TO BE FIXED, NEED MULTIPLE SELECTIONS
    }

    @Override
    public void inLobbyMenu() {
        setCurrentGuiState(GuiState.IN_LOBBY);
    }

    @Override
    public void login() {
        currentGuiState = GuiState.LOGIN;
        client.login(username);
        this.username = username;
    }
}