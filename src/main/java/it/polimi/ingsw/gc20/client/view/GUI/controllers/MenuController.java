package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ViewPlayer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Arrays;

public class MenuController {

    public enum DisplayContext {
        THUMBNAIL,
        MAIN_VIEW,
        DIALOG
    }

    private static class DisplayConfig {
        final double aspectRatio;
        final double maxWidth;
        final double maxHeight;
        final double minWidth;
        final double minHeight;
        final double padding;
        final double widthMultiplier;
        final double heightMultiplier;

        DisplayConfig(double aspectRatio, double maxWidth, double maxHeight, 
                     double minWidth, double minHeight, double padding,
                     double widthMultiplier, double heightMultiplier) {
            this.aspectRatio = aspectRatio;
            this.maxWidth = maxWidth;
            this.maxHeight = maxHeight;
            this.minWidth = minWidth;
            this.minHeight = minHeight;
            this.padding = padding;
            this.widthMultiplier = widthMultiplier;
            this.heightMultiplier = heightMultiplier;
        }
    }

    private static final DisplayConfig SHIP_THUMBNAIL = new DisplayConfig(1.3, 200, 150, 100, 75, 5, 0.95, 0.95);
    private static final DisplayConfig SHIP_MAIN_VIEW = new DisplayConfig(1.2, 800, 600, 200, 150, 20, 0.98, 0.98);
    private static final DisplayConfig SHIP_DIALOG = new DisplayConfig(1.25, 400, 300, 150, 100, 10, 0.92, 0.92);

    private static final DisplayConfig BOARD_MAIN_VIEW = new DisplayConfig(1.8, 1200, 800, 300, 200, 15, 0.98, 0.98);
    private static final DisplayConfig BOARD_DIALOG = new DisplayConfig(1.8, 600, 450, 200, 150, 12, 0.92, 0.92);

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
    @FXML private VBox sidebarPane;

    private ClientGameModel gameModel;
    private ViewPlayer[] players;

    public enum ContentType {
        SHIP, BOARD
    }

    @FXML
    public void initialize() {
        gameModel = ClientGameModel.getInstance();
        players = gameModel.getPlayers();
        
        loadPlayerShips();
        initializeCurrentFrame();
        initializeGameBoard();
    }

    /**
     * Generic method to load any type of FXML content into a container
     */
    private void loadFXMLInContainer(StackPane container, String fxmlPath, 
                                   DisplayContext context, ContentType contentType, 
                                   ViewPlayer player, Object... additionalParams) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            container.getChildren().clear();
            Node content = loader.load();

            container.getChildren().removeIf(node -> node instanceof ImageView);

            if (content instanceof StackPane contentPane) {
                configureGenericSizing(container, contentPane, context, contentType);
            } else if (content instanceof Pane contentPane) {
                StackPane wrapper = new StackPane(contentPane);
                configureGenericSizing(container, wrapper, context, contentType);
                content = wrapper;
            }

            container.getChildren().add(content);

            configureController(loader.getController(), contentType, player, additionalParams);

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading FXML: " + fxmlPath);
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
     * Responsive configuration for main view and dialog
     */
    private void configureResponsiveSizing(StackPane container, StackPane element, DisplayConfig config) {
        element.setMinWidth(config.minWidth);
        element.setMinHeight(config.minHeight);
        element.setMaxWidth(config.maxWidth);
        element.setMaxHeight(config.maxHeight);
        
        container.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            if (newWidth.doubleValue() > 0) {
                updateElementSizeSafely(container, element, config);
            }
        });
        
        container.heightProperty().addListener((obs, oldHeight, newHeight) -> {
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
                case DIALOG -> BOARD_DIALOG;
                case THUMBNAIL -> BOARD_DIALOG;
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

    /**
     * Configures the controller of the loaded content
     */
    private void configureController(Object controller, ContentType contentType, 
                                   ViewPlayer player, Object... additionalParams) {
        switch (contentType) {
            case SHIP -> {
                if (controller instanceof ShipController shipController && player != null) {
                    shipController.buildShipComponents(gameModel.getShip(player.username));
                    shipController.updateStatisticBoard(player);
                }
            }
            case BOARD -> {
                if (controller instanceof BoardController boardController) {
                    boardController.updateBoardDisplay(gameModel.getBoard());
                }
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
            }
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
     * Initializes currentFrame with current player's ship
     */
    private void initializeCurrentFrame() {
        ViewPlayer currentPlayer = Arrays.stream(gameModel.getPlayers())
                .filter(p -> p != null && p.username.equals(gameModel.getUsername()))
                .findFirst().orElse(null);
        
        if (currentPlayer != null) {
            String fxmlPath = getFXMLPath(ContentType.SHIP);
            loadFXMLInContainer(currentFrame, fxmlPath, DisplayContext.MAIN_VIEW, 
                              ContentType.SHIP, currentPlayer);
        }
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
     * Gets the appropriate FXML path for the content type
     */
    private String getFXMLPath(ContentType contentType) {
        return switch (contentType) {
            case SHIP -> gameModel.getShip(gameModel.getUsername()).isLearner ? 
                        "/fxml/ship0.fxml" : "/fxml/ship.fxml";
            case BOARD -> gameModel.getShip(gameModel.getUsername()).isLearner ? 
                         "/fxml/board0.fxml" : "/fxml/board2.fxml";
        };
    }

    /**
     * Static method to load ship in other controllers
     */
    public static void loadShipInContainer(StackPane container, ViewPlayer player, 
                                         DisplayContext context) {
        MenuController controller = new MenuController();
        String fxmlPath = controller.getFXMLPath(ContentType.SHIP);
        controller.loadFXMLInContainer(container, fxmlPath, context, ContentType.SHIP, player);
    }

    /**
     * Static method to load board in other controllers
     */
    public static void loadBoardInContainer(StackPane container, DisplayContext context) {
        MenuController controller = new MenuController();
        String fxmlPath = controller.getFXMLPath(ContentType.BOARD);
        controller.loadFXMLInContainer(container, fxmlPath, context, ContentType.BOARD, null);
    }

    /**
     * Generic static method for any content
     */
    public static void loadContentInContainer(StackPane container, ContentType contentType, 
                                            DisplayContext context, ViewPlayer player) {
        MenuController controller = new MenuController();
        String fxmlPath = controller.getFXMLPath(contentType);
        controller.loadFXMLInContainer(container, fxmlPath, context, contentType, player);
    }
}