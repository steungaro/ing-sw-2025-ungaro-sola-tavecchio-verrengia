package it.polimi.ingsw.gc20.client.view.GUI;

import it.polimi.ingsw.gc20.client.view.GUI.controllers.*;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.network.NetworkManager;
import it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards.ViewAdventureCard;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
import it.polimi.ingsw.gc20.common.message_protocol.Message;
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
import java.util.HashMap;

/**
 * GUIView is the main JavaFX-based graphical user interface implementation for the Galaxy Trucker game client.
 * This class extends ClientGameModel and provides comprehensive GUI functionality including scene management,
 * user interaction handling, and real-time game state visualization.
 * 
 * <p>The class manages the complete user interface lifecycle from initial connection setup through
 * gameplay phases including building, navigation, and combat. It uses JavaFX Scene management
 * with FXML-based views and implements the Observer pattern to respond to game state changes.</p>
 * 
 * <p>Key features include:
 * - State-based GUI navigation with automatic scene transitions
 * - Real-time game updates through message handling
 * - Network connection management (RMI and Socket support)
 * - Error handling and user feedback systems
 * - Multi-threaded UI updates with Platform.runLater()
 * - Context-aware menu systems with data passing capabilities</p>
 * 
 * @author Galaxy Trucker Development Team
 * @version 1.0
 * @since 1.0
 */
public class GUIView extends ClientGameModel {

    /**
     * Enumeration defining all possible GUI states in the application.
     * Each state corresponds to a specific FXML file and represents a distinct
     * user interface screen or dialog.
     * 
     * <p>States are organized into categories:
     * - Connection states (WELCOME, LOGIN, NETWORK)
     * - Menu states (MAIN_MENU, IN_LOBBY)
     * - Game phase states (BUILDING_PHASE, BUILDING_PHASE0, BUILDING_PHASE2)
     * - Action states (ROLL_DICE_MENU, PLANET_MENU, CARGO_MENU)
     * - Board view states (BOARD0, SHIP0, SHIP2)</p>
     */
    public enum GuiState {
        /** Initial welcome screen displayed on application startup */
        MENU("menu"),
        /** Welcome screen with the game introduction */
        WELCOME("welcome"),
        /** User authentication screen */
        LOGIN("login"),
        /** Network connection configuration screen */
        NETWORK("network"),
        /** Main menu with game options */
        MAIN_MENU("mainMenu"),
        /** Lobby waiting room for multiplayer games */
        IN_LOBBY("inLobby"),
        /** General building phase interface */
        BUILDING_PHASE("buildingPhase"),
        /** Building phase for beginner players */
        BUILDING_PHASE0("buildingPhase0"),
        /** Advanced building phase interface */
        BUILDING_PHASE2("buildingPhase2"),
        /** Branch selection menu for ship splits */
        BRANCH_MENU("activationMenu"),
        /** Ship configuration validation interface */
        VALIDATION_MENU("validationMenu"),
        /** Crew placement interface */
        POPULATE_SHIP_MENU("populateShipMenu"),
        /** Dice rolling interface for random events */
        ROLL_DICE_MENU("rollDiceMenu"),
        /** Planet selection interface */
        PLANET_MENU("planetMenu"),
        /** Shield activation interface */
        SHIELDS_MENU("activationMenu"),
        /** Crew loss selection interface */
        LOSE_CREW_MENU("loseCrewMenu"),
        /** Energy loss interface */
        LOSE_ENERGY_MENU("activationMenu"),
        /** Continue playing the decision interface */
        KEEP_PLAYING_MENU("keepPlayingMenu"),
        /** Idle state waiting interface */
        IDLE_MENU("idleMenu"),
        /** Game board view */
        BOARD0("board0"),
        /** Player ship view */
        SHIP0("ship0"),
        /** Card acceptance decision interface */
        CARD_ACCEPTANCE_MENU("cardAcceptance"),
        /** Secondary ship view */
        SHIP2("ship2"),
        /** Cargo management interface */
        CARGO_MENU("cargoMenu"),
        /** Cannon activation interface */
        CANNONS_MENU("activationMenu"),
        /** Final score and ranking display */
        LEADER_BOARD_MENU("leaderBoardMenu"),
        /** Automatic action notification interface */
        AUTOMATIC_ACTION("automaticAction"),
        /** Card deck preview interface */
        PEEK_DECKS("peekDecks"),
        /** Engine activation interface */
        ENGINE_MENU("activationMenu");

        /** The FXML filename associated with this GUI state */
        private final String fxmlFileName;

        /**
         * Constructs a GuiState with the specified FXML filename.
         * 
         * @param fxmlFileName the name of the FXML file (without extension) associated with this state
         */
        GuiState(String fxmlFileName) {
            this.fxmlFileName = fxmlFileName;
        }

        /**
         * Returns the FXML filename associated with this GUI state.
         * 
         * @return the FXML filename without extension
         */
        public String getFxmlFileName() {
            return fxmlFileName;
        }
    }

    /** Observable property for tracking current GUI state changes */
    private final ObjectProperty<GuiState> currentGuiStateProperty = new SimpleObjectProperty<>();
    /** The primary JavaFX stage for the application */
    private static Stage primaryStage;

    /**
     * Constructs a new GUIView instance.
     * Initializes the parent ClientGameModel and sets up GUI state change listeners.
     * The listener automatically handles scene transitions when the GUI state changes.
     * 
     * @throws RemoteException if RMI communication fails during initialization
     */
    public GUIView() throws RemoteException {
        super();
        currentGuiStateProperty.addListener((_, oldValue, newValue) -> {
            if (newValue != null) {
                Platform.runLater(() -> handleGuiStateChange(oldValue, newValue));
            }
        });
    }

    /**
     * Handles GUI state transitions by loading appropriate FXML scenes.
     * This method is called automatically when the GUI state changes and ensures
     * that scene transitions only occur when necessary to avoid redundant loads.
     * 
     * @param oldState the previous GUI state (maybe null)
     * @param newState the new GUI state to transition to
     */
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

    /**
     * Gets the current GUI state.
     * 
     * @return the current GuiState, or null if not set
     */
    public GuiState getCurrentGuiState() {
        return currentGuiStateProperty.get();
    }

    /**
     * Sets the current GUI state, triggering automatic scene transition.
     * 
     * @param newState the new GUI state to set
     */
    public void setCurrentGuiState(GuiState newState) {
        currentGuiStateProperty.set(newState);
    }

    /**
     * Displays menu content within the main menu framework.
     * This method handles both direct menu transitions and menu content loading
     * with support for context data passing, temporary displays, and acceptance dialogs.
     * 
     * @param contentState the GUI state representing the content to display
     * @param contextData optional data to pass to the menu controller (maybe null)
     * @param isTemporary whether this is a temporary display that should be dismissed automatically
     * @param acceptable whether this menu requires user acceptance/confirmation
     */
    private void showMenuContent(GuiState contentState, Map<String, Object> contextData, boolean isTemporary, boolean acceptable) {
        Platform.runLater(() -> {
            if (getCurrentGuiState() == GuiState.MENU) {
                MenuController.loadContentInCurrentFrame(contentState.getFxmlFileName(), this, contextData, isTemporary, acceptable);
            } else {
                setCurrentGuiState(GuiState.MENU);
                Platform.runLater(() ->
                        MenuController.loadContentInCurrentFrame(contentState.getFxmlFileName(), this, contextData, isTemporary, acceptable)
                );
            }
        });
    }

    /**
     * Displays menu content with default parameters (non-temporary, non-acceptable).
     * 
     * @param contentState the GUI state representing the content to display
     */
    private void showMenuContent(GuiState contentState) {
        showMenuContent(contentState, null, false, false);
    }

    /**
     * Displays an error message to the user using a modal dialog.
     * This method is thread-safe and can be called from any thread.
     * 
     * @param message the error message to display
     */
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

    /**
     * Initializes the GUI system with the primary JavaFX stage.
     * Sets up the application window, shutdown hooks, and initial GUI state.
     * Also configures proper cleanup procedures for application termination.
     * 
     * @param stage the primary JavaFX stage for the application
     */
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

    /**
     * Loads and displays a JavaFX scene from an FXML file.
     * Handles FXML loading, scene creation, controller initialization, and error handling.
     * Maintains window dimensions across scene transitions when possible.
     * 
     * @param fileName the name of the FXML file to load (without extension)
     */
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
            displayErrorMessage("Error loading UI: " + e.getMessage());
        }
    }

    /**
     * Establishes a network connection to the game server.
     * Supports both RMI and Socket-based connections with flexible parameter handling.
     * Automatically transitions to the login screen upon successful connection.
     * 
     * @param ipAddress the server IP address (empty string for localhost)
     * @param port the server port (0 for default port)
     * @param isRMI true for RMI connection, false for Socket connection
     * @return true if the connection was successful, false otherwise
     */
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

    /**
     * Processes incoming messages from the server by delegating to message handlers.
     * This method is thread-safe and handles all message processing on the JavaFX Application Thread.
     * Includes comprehensive error handling for message processing failures.
     * 
     * @param message the message to process
     */
    @Override
    public void updateView(Message message) {
        Platform.runLater(() -> {
            try {
                message.handleMessage();
            } catch (Exception e) {
                System.out.println("Error while handling message: " + e.getMessage());
                displayErrorMessage("Error processing server message: " + e.getMessage());
            }
        });
    }

    /**
     * Transitions to the main menu state.
     * Called when the player should be shown the main menu interface.
     */
    @Override
    public void mainMenuState(){
        setCurrentGuiState(GuiState.MAIN_MENU);
    }

    /**
     * Displays the planet selection menu with available planets.
     * 
     * @param planets list of planets available for selection
     */
    @Override
    public void planetMenu(List<Planet> planets) {
        Map<String, Object> contextData = new HashMap<>();
        contextData.put("planets", planets);
        showMenuContent(GuiState.PLANET_MENU, contextData, false, false);
    }

    /**
     * Displays the ship population menu for crew placement.
     */
    @Override
    public void populateShipMenu() {
        showMenuContent(GuiState.POPULATE_SHIP_MENU);
    }

    /**
     * Displays the ship validation menu for configuration confirmation.
     */
    @Override
    public void validationMenu() {
        showMenuContent(GuiState.VALIDATION_MENU);
    }

    /**
     * Empty initialization method.
     * This method is intentionally left empty as GUI initialization
     * is handled by the initGUI() method.
     */
    @Override
    public void init() {
    }

    /**
     * Displays the shields' activation menu with a custom message.
     * 
     * @param message the message to display to the user regarding shield activation
     */
    @Override
    public void shieldsMenu(String message) {
        Map<String, Object> contextData = new HashMap<>();
        contextData.put("message", message);
        contextData.put("activationType", ActivationMenuController.ActivationType.SHIELDS);
        showMenuContent(GuiState.SHIELDS_MENU, contextData, false, false);
    }

    /**
     * Displays the dice rolling menu with a custom message.
     * 
     * @param message the message to display regarding the dice roll requirement
     */
    @Override
    public void rollDiceMenu(String message) {
        Map<String, Object> contextData = new HashMap<>();
        contextData.put("message", message);
        showMenuContent(GuiState.ROLL_DICE_MENU, contextData, false, false);
    }

    /**
     * Displays the cargo selection menu for a specified number of cargo items.
     * 
     * @param cargoNum the number of cargo items to select
     */
    @Override
    public void cargoMenu(int cargoNum) {
        Map<String, Object> contextData = new HashMap<>();
        contextData.put("cargoNum", cargoNum);
        showMenuContent(GuiState.CARGO_MENU, contextData, false, false);
    }

    /**
     * Displays the crew loss menu for selecting crew members to lose.
     * 
     * @param crewNum the number of crew members to lose
     */
    @Override
    public void loseCrewMenu(int crewNum) {
        Map<String, Object> contextData = new HashMap<>();
        contextData.put("crewNum", crewNum);
        showMenuContent(GuiState.LOSE_CREW_MENU, contextData, false, false);
    }

    /**
     * Displays the battery removal menu for energy loss scenarios.
     * 
     * @param batteryNum the number of batteries to remove
     */
    @Override
    public void removeBatteryMenu(int batteryNum) {
        Map<String, Object> contextData = new HashMap<>();
        contextData.put("activationTyper", ActivationMenuController.ActivationType.BATTERY);
        contextData.put("batteryNum", batteryNum);
        showMenuContent(GuiState.LOSE_ENERGY_MENU, contextData, false, false);
    }

    /**
     * Displays the appropriate building phase menu based on the player experience level.
     * Shows beginner interface for learner players and advanced interface for experienced players.
     */
    @Override
    public void assemblingStateMenu() {
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

    /**
     * Displays the final leaderboard with player rankings and scores.
     * 
     * @param leaderBoard map of player names to their final scores
     */
    @Override
    public void leaderBoardMenu(Map<String, Integer> leaderBoard) {
        Map<String, Object> contextData = new HashMap<>();
        contextData.put("leaderBoard", leaderBoard);
        showMenuContent(GuiState.LEADER_BOARD_MENU, contextData, false, false);
    }

    /**
     * Handles successful login by storing user credentials and transitioning to the main menu.
     * 
     * @param username the username that successfully logged in
     */
    @Override
    public void loginSuccessful(String username) {
        this.username = username;
        this.loggedIn = true;
        setCurrentGuiState(GuiState.MAIN_MENU);
    }

    /**
     * Handles failed login attempts by updating the login screen with error information.
     * Attempts to communicate with the LoginController to display error messages.
     * 
     * @param username the username that failed to log in
     */
    @Override
    public void loginFailed(String username) {
        Platform.runLater(() -> {
            if (getCurrentGuiState() == GuiState.LOGIN) {
                if (primaryStage != null && primaryStage.getScene() != null && primaryStage.getScene().getRoot() != null) {
                    Object controller = primaryStage.getScene().getRoot().getUserData();
                    try{
                        LoginController loginController = (LoginController) controller;
                        if (loginController != null) {
                            loginController.setErrorLabel("Login failed for user: " + username);
                        } else {
                            System.err.println("LoginController is null, cannot update errorLabel.");
                        }
                    } catch (ClassCastException e) {
                        System.err.println("Error casting controller to LoginController: " + e.getMessage());
                    } catch (NullPointerException e) {
                        System.err.println("Error accessing scene or root: " + e.getMessage());
                    }
                } else {
                    System.err.println("Cannot update errorLabel: primaryStage, scene, or root is null.");
                }
            } else {
                displayErrorMessage("Login failed for user: " + username + " (unexpected state: " + getCurrentGuiState() + ")");
            }
        });
    }

    /**
     * Displays an idle menu with a custom message for waiting states.
     * 
     * @param message the message to display during idle state
     */
    @Override
    public void idleMenu(String message) {
        Map<String, Object> contextData = new HashMap<>();
        contextData.put("message", message);
        showMenuContent(GuiState.IDLE_MENU, contextData, false, false);
    }

    /**
     * Displays "continue playing decision menu".
     */
    @Override
    public void keepPlayingMenu() {
        showMenuContent(GuiState.KEEP_PLAYING_MENU);
    }

    /**
     * Handles server disconnection by cleaning up network resources and returning to the connection screen.
     * This method is thread-safe and performs all cleanup operations on the appropriate threads.
     * 
     * @throws RemoteException if RMI communication fails during disconnection handling
     */
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

    /**
     * Performs cleanup operations when shutting down the GUI.
     * Stops network connections and cleans up resources.
     */
    @Override
    public void shutdown() {
        System.out.println("GUIView shutdown method called.");
        if (client != null) {
            client.stop();
            client = null;
        }
    }

    /**
     * Displays the branch selection menu when a ship splits into multiple paths.
     * Players must choose which branch to keep when their ship configuration creates multiple valid paths.
     */
    @Override
    public void branchMenu() {
        Map<String, Object> contextData = new HashMap<>();
        String message = "Your ship has split into two branches, please choose one to keep (last selected branch will be kept):";
        contextData.put("message", message);
        contextData.put("activationType", ActivationMenuController.ActivationType.BRANCH);
        showMenuContent(GuiState.BRANCH_MENU, contextData, false, false);
    }

    /**
     * Displays the building menu with available adventure cards for deck preview.
     * This is typically shown as a temporary overlay during the building phase.
     * 
     * @param cards list of adventure cards available for preview
     */
    @Override
    public void buildingMenu(List<ViewAdventureCard> cards) {
        Map<String, Object> contextData = new HashMap<>();
        contextData.put("cards", cards);
        showMenuContent(GuiState.PEEK_DECKS, contextData, true, false);
    }

    /**
     * Displays the cannon activation menu with a custom message.
     * 
     * @param message the message to display regarding cannon activation
     */
    @Override
    public void cannonsMenu(String message) {
        Map<String, Object> contextData = new HashMap<>();
        contextData.put("message", message);
        contextData.put("activationType", ActivationMenuController.ActivationType.CANNONS);
        showMenuContent(GuiState.CANNONS_MENU, contextData, false, false);
    }

    /**
     * Displays the card acceptance menu for automatic actions.
     * This menu allows players to accept or decline automatic card effects.
     * 
     * @param message the message describing the card effect to accept or decline
     */
    @Override
    public void cardAcceptanceMenu(String message) {
        Map<String, Object> contextData = new HashMap<>();
        contextData.put("message", message);
        showMenuContent(GuiState.AUTOMATIC_ACTION, contextData, false, true);
    }

    /**
     * Displays the comprehensive cargo menu for cargo management operations.
     * Handles both cargo loss and cargo gain scenarios with detailed options.
     * 
     * @param message descriptive message about the cargo operation
     * @param cargoToLose number of cargo items to lose
     * @param cargoToGain list of cargo colors available to gain
     * @param losing true if this is a cargo loss operation, false for cargo gain
     */
    @Override
    public void cargoMenu(String message, int cargoToLose, List<CargoColor> cargoToGain, boolean losing) {
        Map<String, Object> contextData = new HashMap<>();
        contextData.put("message", message);
        contextData.put("cargoToLose", cargoToLose);
        contextData.put("cargoToGain", cargoToGain);
        contextData.put("losing", losing);
        showMenuContent(GuiState.CARGO_MENU, contextData, false, false);
    }

    /**
     * Displays the engine activation menu with a custom message.
     * 
     * @param message the message to display regarding engine activation
     */
    @Override
    public void engineMenu(String message) {
        Map<String, Object> contextData = new HashMap<>();
        contextData.put("message", message);
        contextData.put("activationType", ActivationMenuController.ActivationType.ENGINES);
        showMenuContent(GuiState.ENGINE_MENU, contextData, false, false);
    }

    /**
     * Displays an automatic action notification with a custom message.
     * Used for actions that happen automatically without the player input.
     * 
     * @param message the message describing the automatic action that occurred
     */
    @Override
    public void automaticAction(String message) {
        Map<String, Object> contextData = new HashMap<>();
        contextData.put("message", message);
        showMenuContent(GuiState.AUTOMATIC_ACTION, contextData, false, false);
    }

    /**
     * Transitions to the lobby menu state for multiplayer game waiting.
     */
    @Override
    public void inLobbyMenu() {
        setCurrentGuiState(GuiState.IN_LOBBY);
    }

    /**
     * Initiates the login process by transitioning to login-state and sending a login request.
     * Uses the currently stored username for the login attempt.
     */
    @Override
    public void login() {
        setCurrentGuiState(GuiState.LOGIN);
        client.login(username);
    }
}