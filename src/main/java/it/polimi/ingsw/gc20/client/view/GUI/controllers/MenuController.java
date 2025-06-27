package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.GUI.GUIView;
import it.polimi.ingsw.gc20.client.view.common.ViewLobby;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.GameModelListener;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ViewPlayer;
import it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards.ViewAdventureCard;
import it.polimi.ingsw.gc20.client.view.common.localmodel.board.ViewBoard;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class MenuController implements GameModelListener {

    public enum DisplayContext {
        THUMBNAIL,
        MAIN_VIEW,
        DIALOG
    }

    public interface ContextDataReceiver {
        void setContextData(Map<String, Object> contextData);
    }

    private record DisplayConfig(double aspectRatio, double maxWidth, double maxHeight, double minWidth,
                                 double minHeight, double padding, double widthMultiplier, double heightMultiplier) {
    }

    private static final DisplayConfig SHIP_THUMBNAIL = new DisplayConfig(1.3, 200, 150, 100, 75, 5, 0.95, 0.95);
    private static final DisplayConfig SHIP_MAIN_VIEW = new DisplayConfig(1.2, 800, 600, 200, 150, 20, 0.98, 0.98);
    private static final DisplayConfig SHIP_DIALOG = new DisplayConfig(1.25, 400, 300, 150, 100, 10, 0.92, 0.92);
    private static final DisplayConfig BOARD_MAIN_VIEW = new DisplayConfig(1.8, 1200, 800, 300, 200, 15, 0.98, 0.98);
    private static final DisplayConfig BOARD_DIALOG = new DisplayConfig(1.8, 600, 450, 200, 150, 12, 0.92, 0.92);

    private boolean canGoBack = false;

    private Timeline hourglassTimeline;
    private LocalDateTime hourglassStartTime;
    private static final int HOURGLASS_DURATION = 90;

    @FXML public VBox drawnCard;
    @FXML private StackPane currentFrame;
    @FXML private StackPane gameBoard;
    @FXML private StackPane player1Ship;
    @FXML private StackPane player2Ship;
    @FXML private StackPane player3Ship;
    @FXML private StackPane player4Ship;
    @FXML private Label player1Name;
    @FXML private Label player2Name;
    @FXML private Label player3Name;
    @FXML private Label player4Name;
    @FXML public Button backButton;
    @FXML private Label serverMessages;
    @FXML public Button acceptButton;
    @FXML public Button discardButton;
    @FXML public Button turnHourGlassButton;
    @FXML private Label hourglassTimer;
    @FXML private VBox hourglassContainer;

    private ClientGameModel gameModel;
    private ViewPlayer[] players;
    private static MenuController currentInstance;
    private Node viewStack = null;
    private DisplayContext currentDisplayContext = DisplayContext.MAIN_VIEW;

    public enum ContentType {
        SHIP, BOARD
    }

    @FXML
    public void initialize() {
        gameModel = ClientGameModel.getInstance();
        players = gameModel.getPlayers();

        loadPlayerShips();
        loadPlayerNames();
        printCurrentCard(gameModel.getCurrentCard());
        initializeGameBoard();
        setVisibility();
        initializeHourglassTimer();

        ClientGameModel.getInstance().addListener(this);
        currentInstance = this;
    }

    private void initializeHourglassTimer() {

        hourglassTimeline = new Timeline(
                new KeyFrame(Duration.seconds(1.0), e -> updateHourglassTimer())
        );
        hourglassTimeline.setCycleCount(Timeline.INDEFINITE);

        ViewBoard currentBoard = gameModel.getBoard();

        if (currentBoard != null && currentBoard.timeStampOfLastHourglassRotation > 0) {

            long currentTimeMillis = System.currentTimeMillis();
            long elapsedMillis = currentTimeMillis - currentBoard.timeStampOfLastHourglassRotation;
            long elapsedSeconds = elapsedMillis / 1000;
            long remainingSeconds = HOURGLASS_DURATION - elapsedSeconds;

            if (remainingSeconds > 0) {
                this.hourglassStartTime = LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(currentBoard.timeStampOfLastHourglassRotation),
                        ZoneId.systemDefault()
                );

                hourglassTimeline.play();
                hourglassContainer.setVisible(!gameModel.getBoard().isLearner);
            } else {
                updateHourglassDisplay(0);
                hourglassContainer.setVisible(false);
            }
        } else {
            updateHourglassDisplay(0);
            hourglassContainer.setVisible(false);
        }
    }


    public void startHourglassCountdown(LocalDateTime timeStampOfLastHourglassRotation) {

        Platform.runLater(() -> {
            this.hourglassStartTime = timeStampOfLastHourglassRotation;

            if (hourglassTimeline != null) {
                hourglassTimeline.stop();
                System.out.println("DEBUG - Stopped previous timeline");
            }

            long startTimeMillis = hourglassStartTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            long currentTimeMillis = System.currentTimeMillis();
            long elapsedMillis = currentTimeMillis - startTimeMillis;
            long elapsedSeconds = elapsedMillis / 1000;
            long remainingSeconds = HOURGLASS_DURATION - elapsedSeconds;

            System.out.println("DEBUG - At start - Elapsed: " + elapsedSeconds + ", Remaining: " + remainingSeconds);

            if (remainingSeconds > 0) {
                hourglassTimeline.play();
                hourglassContainer.setVisible(!gameModel.getBoard().isLearner);
            } else {
                updateHourglassDisplay(0);
                hourglassContainer.setVisible(false);
            }
        });
    }

    public void stopHourglassCountdown() {

        Platform.runLater(() -> {
            if (hourglassTimeline != null) {
                hourglassTimeline.stop();
            }
            hourglassStartTime = null;
            updateHourglassDisplay(0);
            hourglassContainer.setVisible(false);
        });
    }

    private void updateHourglassTimer() {
        if (hourglassStartTime == null) {
            System.out.println("DEBUG - No start time, stopping countdown");
            stopHourglassCountdown();
            return;
        }

        long startTimeMillis = hourglassStartTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long currentTimeMillis = System.currentTimeMillis();
        long elapsedMillis = currentTimeMillis - startTimeMillis;
        long elapsedSeconds = elapsedMillis / 1000;
        long remainingSeconds = HOURGLASS_DURATION - elapsedSeconds;

        if (remainingSeconds <= 0) {
            updateHourglassDisplay(0);
            hourglassTimer.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold; -fx-font-size: 16px;");
            if (hourglassTimeline != null) {
                hourglassTimeline.stop();
            }
        } else {
            System.out.println("" + remainingSeconds);
            updateHourglassDisplay(remainingSeconds);

            if (remainingSeconds <= 10) {
                hourglassTimer.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold; -fx-font-size: 16px;");
            } else if (remainingSeconds <= 30) {
                hourglassTimer.setStyle("-fx-text-fill: #f39c12; -fx-font-weight: bold; -fx-font-size: 16px;");
            } else {
                hourglassTimer.setStyle("-fx-text-fill: #2ecc71; -fx-font-weight: bold; -fx-font-size: 16px;");
            }
        }
    }

    /**
     * Aggiorna il display del timer (solo secondi)
     */
    private void updateHourglassDisplay(long seconds) {
        Platform.runLater(() -> {
            String timeText = String.format("%02d", seconds);
            hourglassTimer.setText(timeText);
        });
    }

    @Override
    public void onBoardUpdated(ViewBoard board) {

        loadPlayerNames();

        if (board.timeStampOfLastHourglassRotation > 0) {
            LocalDateTime hourglassTime = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(board.timeStampOfLastHourglassRotation),
                    ZoneId.systemDefault()
            );
            startHourglassCountdown(hourglassTime);
        } else {
            stopHourglassCountdown();
        }
    }
    /**
     * Static method to get the current MenuController instance
     */
    public static MenuController getCurrentInstance() {
        return currentInstance;
    }

    /**
     * Sets visibility of player ship containers based on the number of players
     */
    private void setVisibility() {
        StackPane[] shipPanes = {player1Ship, player2Ship, player3Ship, player4Ship};

        for (int i = 0; i < shipPanes.length; i++) {
            boolean isPlayerInGame = i < players.length && players[i] != null;

            Node parentContainer = shipPanes[i].getParent();
            if (parentContainer != null) {
                parentContainer.setVisible(isPlayerInGame);
            }
        }

        updateBackButtonVisibility();

        ViewAdventureCard currentCard = gameModel.getCurrentCard();
        boolean isCardDrawn = currentCard != null && currentCard.id != 0;
        drawnCard.setVisible(isCardDrawn);
        acceptButton.setVisible(false);
        discardButton.setVisible(false);
    }

    public void setAcceptableButtonVisibility(boolean visible) {
        acceptButton.setVisible(visible);
        discardButton.setVisible(visible);
    }

    private void updateBackButtonVisibility() {
        backButton.setVisible(canGoBack);
    }

    /**
     * Loads player names in the sidebar
     */
    private void loadPlayerNames() {
        turnHourGlassButton.setVisible(gameModel.getBoard().assemblingState);
        players = gameModel.getPlayers();
        for (int i = 0; i < players.length; i++) {
            if (players[i] == null) continue;

            Label playerNameLabel = switch (i) {
                case 0 -> player1Name;
                case 1 -> player2Name;
                case 2 -> player3Name;
                case 3 -> player4Name;
                default -> null;
            };

            if (playerNameLabel != null) {
                String text = players[i].username;


                if (players[i].username.equals(gameModel.getUsername())){
                    text += " (You)";
                }

                if(!players[i].inGame){
                    text += " (Early landed)";
                }

                playerNameLabel.setText(text);
            }
        }
    }

    /**
     * Loads all player ships in the sidebar
     */
    private void loadPlayerShips() {
        for (int i = 0; i < players.length; i++) {
            if (players[i] == null) continue;

            StackPane shipContainer = getPlayerShipContainer(i);
            String fxmlPath = getFXMLPath(ContentType.SHIP);

            if (shipContainer != null) {
                loadFXMLInContainer(shipContainer, fxmlPath, DisplayContext.THUMBNAIL,
                        ContentType.SHIP, players[i]);
                canGoBack = true;
                updateBackButtonVisibility();
                makeShipContainerClickable(shipContainer, players[i]);
            }
        }
    }

    /**
     * Returns to the previous view in the stack
     */
    @FXML
    private void returnToPreviousView(){
        closeCurrentControllerListeners();
        
        if (viewStack!= null) {
            currentFrame.getChildren().clear();
            currentFrame.getChildren().add(viewStack);
            canGoBack = false;
        }
        updateBackButtonVisibility();
    }

    @FXML
    public void tunrHourGlass(){
        if (gameModel.getClient() != null) {
            try {
                gameModel.getClient().turnHourglass(gameModel.getUsername());
            } catch (Exception e) {
                serverMessages.setText("Error accepting card: " + e.getMessage());
                serverMessages.getParent().getParent().setVisible(true);
            }
        }
    }

    /**
     * Helper method to close listeners for the current controller
     */
    private void closeCurrentControllerListeners() {
        if (!currentFrame.getChildren().isEmpty()) {
            Node currentContent = currentFrame.getChildren().getFirst();
            Object currentController = currentContent.getUserData();

            try{
                ClientGameModel.getInstance().removeListener((GameModelListener) currentController);
            } catch (ClassCastException e) {
                // ignore, not a GameModelListener
            }

            try{
                ((BindCleanUp) currentController).cleanup();
            } catch (ClassCastException e) {
                // ignore, not a BindCleanUp
            }
        }
    }

    @FXML
    public void handleAcceptCard(){
        if (gameModel.getClient() != null) {
            try{
                gameModel.getClient().acceptCard(
                        ClientGameModel.getInstance().getUsername());
            } catch (Exception e) {
                serverMessages.setText("Error accepting card: " + e.getMessage());
                serverMessages.getParent().getParent().setVisible(true);
            }
        }
    }

    @FXML
    public void handleDiscardCard() {
        if (ClientGameModel.getInstance().getClient() != null) {
            try {
                ClientGameModel.getInstance().getClient().endMove(
                        ClientGameModel.getInstance().getUsername());
            } catch (Exception e) {
                serverMessages.setText("Error accepting card: " + e.getMessage());
                serverMessages.getParent().getParent().setVisible(true);
            }
        }
    }

    /**
     * Makes a ship container clickable and sets up the click handler
     */
    private void makeShipContainerClickable(StackPane shipContainer, ViewPlayer player) {
        shipContainer.setOnMouseEntered(_ -> shipContainer.setStyle("-fx-cursor: hand;"));
        shipContainer.setOnMouseExited(_ -> shipContainer.setStyle("-fx-cursor: default;"));

        shipContainer.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                loadPlayerShipInCurrentFrame(player);
            }
        });
    }

    private void loadPlayerShipInCurrentFrame(ViewPlayer currentPlayer) {

        currentFrame.getChildren().clear();

        if (currentPlayer != null) {
            String fxmlPath = getFXMLPath(ContentType.SHIP);
            loadFXMLInContainer(currentFrame, fxmlPath, DisplayContext.MAIN_VIEW,
                    ContentType.SHIP, currentPlayer);

            canGoBack = true;
            updateBackButtonVisibility();
        }

    }

    /**
     * Gets the ship container for the specified player
     */
    private StackPane getPlayerShipContainer(int playerIndex) {
        return switch (playerIndex) {
            case 0 -> player1Ship;
            case 1 -> player2Ship;
            case 2 -> player3Ship;
            case 3 -> player4Ship;
            default -> null;
        };
    }

    /**
     * Initializes the game board
     */
    private void initializeGameBoard() {
        String fxmlPath = getFXMLPath(ContentType.BOARD);
        loadFXMLInContainer(gameBoard, fxmlPath, DisplayContext.MAIN_VIEW,
                ContentType.BOARD, null);
    }

    /**
     * Static method with parameters support
     */
    public static void loadContentInCurrentFrame(String contentFileName, GUIView guiView, Map<String, Object> contextData, boolean isTemporaryView, boolean acceptable) {
        MenuController instance = getCurrentInstance();
        if (instance == null) {
            return;
        }

        instance.setAcceptableButtonVisibility(acceptable);

        try {
            if (contentFileName == null || contentFileName.trim().isEmpty()) {
                guiView.displayErrorMessage("FXML file name cannot be null or empty");
                return;
            }

            String fxmlPath = "/fxml/" + contentFileName.trim() + ".fxml";
            URL fxmlResource = MenuController.class.getResource(fxmlPath);

            if (fxmlResource == null) {
                guiView.displayErrorMessage("FXML file not found: " + fxmlPath);
                return;
            }

            if(!isTemporaryView) {
                instance.closeCurrentControllerListeners();
            }

            FXMLLoader loader = new FXMLLoader(fxmlResource);
            Parent content = loader.load();
            Object controller = loader.getController();

            if (controller != null) {
                content.setUserData(controller);

                try {
                    ((ContextDataReceiver) controller).setContextData(contextData);
                } catch (ClassCastException e) {
                    // ignore
                }

                try{
                    ClientGameModel.getInstance().addListener((GameModelListener) controller);
                } catch (ClassCastException e) {
                    // ignore, not a GameModelListener
                }
            }

            if (controller != null && contextData != null) {
                try {
                    ((ContextDataReceiver) controller).setContextData(contextData);
                } catch (ClassCastException e) {
                    guiView.displayErrorMessage("Unable to obtain the context data receiver: " + e.getMessage());
                }
            }

            try{
                Region region = (Region) content;
                region.prefWidthProperty().bind(instance.currentFrame.widthProperty());
                region.prefHeightProperty().bind(instance.currentFrame.heightProperty());
                region.setMaxWidth(Region.USE_PREF_SIZE);
                region.setMaxHeight(Region.USE_PREF_SIZE);
            } catch (ClassCastException e) {
                System.err.println("Content is not a Region, cannot bind size: " + e.getMessage());
            }

            instance.currentFrame.getChildren().clear();
            instance.currentFrame.getChildren().add(content);

            if (!isTemporaryView) {
                instance.canGoBack = false;
                instance.updateBackButtonVisibility();
                instance.viewStack = instance.currentFrame.getChildren().getFirst();
            } else {
                instance.canGoBack = true;
                instance.updateBackButtonVisibility();
            }

        } catch (IOException e) {
            System.err.println("Error loading menu: " + e.getMessage());
            guiView.displayErrorMessage("Error loading menu " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            guiView.displayErrorMessage("Unexpected error: " + e.getMessage());
        }
    }

    // GRAPHICAL METHODS

    /**
     * Generic method to load any type of FXML content into a container
     */
    private void loadFXMLInContainer(StackPane container, String fxmlPath,
                                     DisplayContext context, ContentType contentType,
                                     ViewPlayer player) {
        this.currentDisplayContext = context;
        try {
            if (!fxmlPath.startsWith("/")) {
                fxmlPath = "/" + fxmlPath + ".fxml";
            }
            URL fxmlUrl = getClass().getResource(fxmlPath);

            if (fxmlUrl == null) {
                System.err.println("FXML file not found: " + fxmlPath);
                return;
            }

            if (!container.getChildren().isEmpty()) {
                Node oldContent = container.getChildren().getFirst();
                Object oldController = oldContent.getUserData();

                try{
                    ((BindCleanUp) oldController).cleanup();
                } catch (ClassCastException e) {
                    // ignore, not a BindCleanUp
                }
            }

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(fxmlUrl);
            Node content = loader.load();
            Object controller = loader.getController();


            try{
                StackPane contentPane = (StackPane) content;
                configureGenericSizing(container, contentPane, context, contentType);
            } catch (ClassCastException e) {
                Pane contentPane = (Pane) content;
                StackPane wrapper = new StackPane(contentPane);
                configureGenericSizing(container, wrapper, context, contentType);
                content = wrapper;
            }

            if (controller != null) {
                content.setUserData(controller);
            }

            container.getChildren().clear();
            container.getChildren().add(content);
            configureController(controller, contentType, player);

        } catch (IOException e) {
            serverMessages.setText("Error accepting card: " + e.getMessage());
            serverMessages.getParent().getParent().setVisible(true);
        }
    }

    /**
     * Unified method to configure sizing for any element
     */
    private void configureGenericSizing(StackPane container, StackPane element,
                                      DisplayContext context, ContentType contentType) {
        DisplayConfig config = getDisplayConfig(context, contentType);

        element.prefWidthProperty().unbind();
        element.prefHeightProperty().unbind();

        if (context == DisplayContext.THUMBNAIL) {
            configureThumbnailSizing(container, element, config);
        } else {
            configureResponsiveSizing(container, element, config);
        }
    }

    /**
     * Enhanced configuration for thumbnails
     */
    private void configureThumbnailSizing(StackPane container, StackPane element, DisplayConfig config) {
        element.setMinWidth(config.minWidth);
        element.setMinHeight(config.minHeight);
        element.setMaxWidth(config.maxWidth);
        element.setMaxHeight(config.maxHeight);

        element.prefWidthProperty().bind(
            container.widthProperty()
                .multiply(config.widthMultiplier)
                .subtract(config.padding)
        );
        element.prefHeightProperty().bind(
            container.heightProperty()
                .multiply(config.heightMultiplier)
                .subtract(config.padding)
        );

        Platform.runLater(() -> {
            double containerWidth = container.getWidth();
            double containerHeight = container.getHeight();

            if (containerWidth > 0 && containerHeight > 0) {
                double calculatedWidth = containerWidth * config.widthMultiplier - config.padding;
                double calculatedHeight = containerHeight * config.heightMultiplier - config.padding;

                if (calculatedWidth < config.minWidth || calculatedHeight < config.minHeight) {
                    element.prefWidthProperty().unbind();
                    element.prefHeightProperty().unbind();
                    element.setPrefWidth(Math.max(calculatedWidth, config.minWidth));
                    element.setPrefHeight(Math.max(calculatedHeight, config.minHeight));
                }
            }
        });
    }

    /**
     * Responsive configuration for the main view and dialog
     */
    private void configureResponsiveSizing(StackPane container, StackPane element, DisplayConfig config) {
        element.setMinWidth(config.minWidth);
        element.setMinHeight(config.minHeight);
        element.setMaxWidth(config.maxWidth);
        element.setMaxHeight(config.maxHeight);

        container.widthProperty().addListener((_, _, newWidth) -> {
            if (newWidth.doubleValue() > 0) {
                updateElementSizeSafely(container, element, config);
            }
        });

        container.heightProperty().addListener((_, _, newHeight) -> {
            if (newHeight.doubleValue() > 0) {
                updateElementSizeSafely(container, element, config);
            }
        });

        Platform.runLater(() -> updateElementSizeSafely(container, element, config));
    }

    /**
     * Gets the appropriate configuration for the context and content type
     */
    private DisplayConfig getDisplayConfig(DisplayContext context, ContentType contentType) {
        return switch (contentType) {
            case SHIP -> switch (context) {
                case THUMBNAIL -> SHIP_THUMBNAIL;
                case MAIN_VIEW -> SHIP_MAIN_VIEW;
                case DIALOG -> SHIP_DIALOG;
            };
            case BOARD -> switch (context) {
                case MAIN_VIEW -> BOARD_MAIN_VIEW;
                case DIALOG, THUMBNAIL -> BOARD_DIALOG;
            };
        };
    }

    /**
     * Updates element size safely (handles bindings)
     */
    private void updateElementSizeSafely(StackPane container, StackPane element, DisplayConfig config) {
        double containerWidth = container.getWidth();
        double containerHeight = container.getHeight();

        if (containerWidth <= 0 || containerHeight <= 0) return;

        double availableWidth = containerWidth - config.padding;
        double availableHeight = containerHeight - config.padding;

        double targetWidth, targetHeight;

        if (availableWidth / availableHeight > config.aspectRatio) {
            targetHeight = availableHeight;
            targetWidth = targetHeight * config.aspectRatio;
        } else {
            targetWidth = availableWidth;
            targetHeight = targetWidth / config.aspectRatio;
        }

        targetWidth = Math.max(config.minWidth, Math.min(targetWidth, config.maxWidth));
        targetHeight = Math.max(config.minHeight, Math.min(targetHeight, config.maxHeight));

        element.prefWidthProperty().unbind();
        element.prefHeightProperty().unbind();

        element.setPrefWidth(targetWidth);
        element.setPrefHeight(targetHeight);
        element.setMaxWidth(targetWidth);
        element.setMaxHeight(targetHeight);
    }

    private void configureController(Object controller, ContentType contentType,
                                   ViewPlayer player) {
        switch (contentType) {
            case SHIP -> {
                try{
                    ShipController shipController = (ShipController) controller;
                    shipController.setShouldLoadComponentStats(currentDisplayContext != DisplayContext.THUMBNAIL);

                    if(player != null) {
                        shipController.buildShipComponents(gameModel.getShip(player.username));
                    }
                } catch (ClassCastException e) {
                    System.err.println("Controller is not a ShipController: " + e.getMessage());
                }
            }
            case BOARD -> {
                try {
                    BoardController boardController = (BoardController) controller;
                    boardController.updateBoardDisplay(gameModel.getBoard());
                    try{
                        ClientGameModel.getInstance().addListener(boardController);
                    } catch (ClassCastException e) {
                        System.err.println("Controller is not a GameModelListener: " + e.getMessage());
                    }
                } catch (ClassCastException e) {
                    System.err.println("Controller is not a BoardController: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Gets the appropriate FXML path for the content type
     */
    private String getFXMLPath(ContentType contentType) {
        return switch (contentType) {
            case SHIP -> gameModel.getShip(gameModel.getUsername()).isLearner ?
                        "/fxml/ship0.fxml" : "/fxml/ship2.fxml";
            case BOARD -> gameModel.getShip(gameModel.getUsername()).isLearner ?
                         "/fxml/board0.fxml" : "/fxml/board2.fxml";
        };
    }

    @Override
    public void onShipUpdated(ViewShip ship) {
        loadPlayerShips();
    }

    @Override
    public void onLobbyUpdated(ViewLobby lobby) {
        // ignore
    }

    @Override
    public void onErrorMessageReceived(String message) {
        // ignore
    }

    @Override
    public void onComponentInHandUpdated(ViewComponent component) {
        // ignore
    }

    @Override
    public void onCurrentCardUpdated(ViewAdventureCard currentCard) {
        printCurrentCard(currentCard);
    }

    private void printCurrentCard(ViewAdventureCard currentCard) {
        Platform.runLater(() -> {
            try {
                if (currentCard != null && currentCard.id != 0) {
                    String cardInfo = "Drawn card: " + currentCard.getClass().getSimpleName();
                    String currentText = serverMessages.getText();
                    if (currentText.equals("Waiting for server messages...")) {
                        serverMessages.setText(cardInfo);
                    } else {
                        serverMessages.setText(currentText + "\n" + cardInfo);
                    }

                    javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(getImage(currentCard));

                    imageView.setPreserveRatio(true);
                    imageView.setSmooth(true);
                    imageView.setCache(true);

                    imageView.setFitWidth(drawnCard.getWidth() - 20);

                    drawnCard.getChildren().clear();
                    drawnCard.getChildren().add(imageView);
                    drawnCard.setAlignment(Pos.CENTER);

                    drawnCard.setVisible(true);
                }
            } catch (Exception e) {
                System.err.println("Error loading image: " + e.getMessage());
                serverMessages.setText("Error accepting card: " + e.getMessage());
                serverMessages.getParent().getParent().setVisible(true);
            }
        });
    }

    private javafx.scene.image.Image getImage(ViewAdventureCard viewAdventureCard) {
        try {
            String series = (viewAdventureCard.id > 20) ? "II" : "I";
            int adjustedId = (viewAdventureCard.id > 20) ? (viewAdventureCard.id - 20) : viewAdventureCard.id;
            String imagePath = "/fxml/cards/GT-cards_" + series + "_IT_0" + adjustedId + ".jpg";

            java.io.InputStream imageStream = getClass().getResourceAsStream(imagePath);
            if (imageStream == null) {
                System.err.println("Img file not found : " + imagePath);
                return null;
            }

            return new javafx.scene.image.Image(imageStream);
        } catch (Exception e) {
            System.err.println("Error loading img: " + e.getMessage());
            serverMessages.setText("Error accepting card: " + e.getMessage());
            serverMessages.getParent().getParent().setVisible(true);
            return null;
        }

    }

    @FXML
    public void quit(){
        if (gameModel.getClient() != null) {
            try {
                ClientGameModel.getInstance().getClient().giveUp(ClientGameModel.getInstance().getUsername());
            } catch (Exception e) {
                serverMessages.setText("Error accepting card: " + e.getMessage());
                serverMessages.getParent().getParent().setVisible(true);
            }
        }
        Platform.exit();
    }

    @FXML
    public void endTurn(){
        if (gameModel.getClient() != null) {
            try {
                gameModel.getClient().endMove(gameModel.getUsername());
            } catch (Exception e) {
                serverMessages.setText("Error accepting card: " + e.getMessage());
                serverMessages.getParent().getParent().setVisible(true);
            }
        }
    }

    /**
     * Cleanup method to stop timers when the controller is destroyed
     */
    public void cleanup() {
        if (hourglassTimeline != null) {
            hourglassTimeline.stop();
        }
    }
}