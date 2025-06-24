package it.polimi.ingsw.gc20.client.view.GUI;

import it.polimi.ingsw.gc20.client.view.GUI.controllers.*;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.network.NetworkManager;
import it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards.ViewAdventureCard;
import it.polimi.ingsw.gc20.client.view.common.localmodel.board.ViewBoard;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;
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
        MENU("menu"),
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
        SHIELDS_MENU("activationMenu"),
        LOSE_CREW_MENU("loseCrewMenu"),
        LOSE_ENERGY_MENU("loseEnergyMenu"),
        KEEP_PLAYING_MENU("keepPlayingMenu"),
        IDLE_MENU("idleMenu"),
        BOARD0("board0"),
        SHIP0("ship0"),
        CARD_ACCEPTANCE_MENU("cardAcceptance"),
        SHIP2("ship2"),
        CARGO_MENU("cargoMenu"),
        CANNONS_MENU("activationMenu"),
        LEADER_BOARD_MENU("leaderBoardMenu"),
        AUTOMATIC_ACTION("automaticAction"),
        PEEK_DECKS("peekDecks"),
        ENGINE_MENU("activationMenu"),;

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
                    handleGuiStateChange(oldValue, newValue);
                });
            }
        });


        boardProperty.addListener((observable, oldBoard, newBoard) -> {
            if (newBoard != null) {
                notifyCurrentBoardController(newBoard);
            }
        });
    }

    private void handleGuiStateChange(GuiState oldState, GuiState newState) {
        if (primaryStage == null) {
            System.err.println("primaryStage not initialized: " + newState.getFxmlFileName());
            return;
        }

        if (primaryStage.getScene() != null &&
                primaryStage.getScene().getRoot() != null &&
                primaryStage.getScene().getRoot().getId() != null &&
                primaryStage.getScene().getRoot().getId().equals(newState.getFxmlFileName())) {
            return;
        }

        System.out.println("GUI State changed from " +
                (oldState != null ? oldState.name() : "null") +
                " to " + newState.name());
        showScene(newState.getFxmlFileName());
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

    public void showScene(String fileName) {
        try {
            String path = "/fxml/" + fileName + ".fxml";
            URL resourceUrl = getClass().getResource(path);

            if (resourceUrl == null) {
                System.err.println("ERROR: FXML file not found: " + path);
                displayErrorMessage("Internal error: UI file not found (" + fileName + ")");
                return;
            }

            FXMLLoader loader = new FXMLLoader(resourceUrl);
            Parent root = loader.load();
            root.setUserData(loader.getController());
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
        } catch (IOException e) {
            e.printStackTrace();
            displayErrorMessage("Error loading UI: " + e.getMessage());
        }
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
            setCurrentGuiState(GuiState.LOGIN);
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
    public void setBoard(ViewBoard board) {
        this.board = board;
        this.boardProperty.set(board);
    }

    @Override
    public void mainMenuState(){
        setCurrentGuiState(GuiState.MAIN_MENU);
    }

    private void showMenuContent(GuiState contentState) {
        Platform.runLater(() -> {
            if (getCurrentGuiState() == GuiState.MENU) {
                MenuController.loadContentInCurrentFrame(contentState.getFxmlFileName(), this);
            } else {
                setCurrentGuiState(GuiState.MENU);
                Platform.runLater(() ->
                        MenuController.loadContentInCurrentFrame(contentState.getFxmlFileName(), this)
                );
            }
        });
    }


    @Override
    public void planetMenu(List<Planet> planets) {
        showMenuContent(GuiState.PLANET_MENU);
        // TODO pass planets to the controller

    }

    @Override
    public void populateShipMenu() {
        showMenuContent(GuiState.POPULATE_SHIP_MENU);
    }


    @Override
    public void validationMenu() {
        showMenuContent(GuiState.VALIDATION_MENU);
    }


    @Override
    public void init() {
        // Ignore
    }

    @Override
    public void shieldsMenu(String message) {
        showMenuContent(GuiState.SHIELDS_MENU);
    }


    @Override
    public void rollDiceMenu(String message) {
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
        showMenuContent(GuiState.CARGO_MENU);
    }


    @Override
    public void loseCrewMenu(int crewNum) {
        showMenuContent(GuiState.LOSE_CREW_MENU);
    }


    @Override
    public void removeBatteryMenu(int batteryNum) {
        showMenuContent(GuiState.LOSE_ENERGY_MENU);
    }


    @Override
    public void AssemblingStateMenu() {
        ViewShip playerShip = null;
        if (ClientGameModel.getInstance() != null && ClientGameModel.getInstance().getUsername() != null) {
            playerShip = ClientGameModel.getInstance().getShip(ClientGameModel.getInstance().getUsername());
        }

        if (playerShip != null && playerShip.isLearner) {
            showMenuContent(GuiState.BUILDING_PHASE0);
        } else {
            showMenuContent(GuiState.BUILDING_PHASE2);
        }
    }

    @Override
    public void leaderBoardMenu(Map<String, Integer> leaderBoard) {
        showMenuContent(GuiState.LEADER_BOARD_MENU);
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
                if (primaryStage != null && primaryStage.getScene() != null && primaryStage.getScene().getRoot() != null) {
                    Object controller = primaryStage.getScene().getRoot().getUserData();
                    if (controller instanceof LoginController loginController) {
                    } else {
                        System.err.println("Controller is not of type LoginController or is null.");
                    }
                } else {
                    System.err.println("Cannot update errorLabel: primaryStage, scene, or root is null.");
                }
            } else {
                displayErrorMessage("Login failed for user: " + username + " (unexpected state: " + getCurrentGuiState() + ")");
            }
        });
    }

    @Override
    public void idleMenu(String message) {
        showMenuContent(GuiState.IDLE_MENU);
    }


    @Override
    public void keepPlayingMenu() {
        showMenuContent(GuiState.KEEP_PLAYING_MENU);
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
        showMenuContent(GuiState.BRANCH_MENU);
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
        showMenuContent(GuiState.CANNONS_MENU);
    }


    @Override
    public void cardAcceptanceMenu(String message) {
        showMenuContent(GuiState.CARD_ACCEPTANCE_MENU);
    }


    @Override
    public void cargoMenu(String message, int cargoToLose, List<CargoColor> cargoToGain, boolean losing) {
        showMenuContent(GuiState.CARGO_MENU);
    }


    @Override
    public void engineMenu(String message) {
        showMenuContent(GuiState.ENGINE_MENU);
    }


    @Override
    public void automaticAction(String message) {
        showMenuContent(GuiState.AUTOMATIC_ACTION);
    }


    @Override
    public void inLobbyMenu() {
        setCurrentGuiState(GuiState.IN_LOBBY);
    }

    @Override
    public void login() {
        setCurrentGuiState(GuiState.LOGIN);
        client.login(username);
        this.username = username;
    }
}