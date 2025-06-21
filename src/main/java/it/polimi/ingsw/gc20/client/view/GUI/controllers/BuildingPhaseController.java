package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.ViewLobby;
import it.polimi.ingsw.gc20.client.view.common.localmodel.*;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.*;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
import it.polimi.ingsw.gc20.server.model.components.AlienColor;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.stream.Collectors;

public abstract class BuildingPhaseController implements GameModelListener, GUIController {

    @FXML
    protected StackPane shipContainer;

    @FXML
    protected Pane componentInHandPane;

    @FXML
    private Pane coveredDeckPane;

    @FXML
    protected FlowPane uncoveredComponentsPane;

    @FXML
    private HBox nonLearnerButtonsContainer;

    @FXML
    protected ImageView bgImage;

    @FXML
    protected StackPane rootPane;

    @FXML
    protected Label playerColorLabel;

    @FXML
    protected Label usernameLabel;

    @FXML
    protected Label creditsLabel;

    @FXML
    protected Label inGameLabel;

    @FXML
    protected GridPane componentsGrid;

    @FXML
    private ListView<ViewPlayer> otherPlayersShipsList;

    @FXML
    protected GridPane bookedGrid;

    private ViewComponent selectedComponent;
    protected boolean placementModeActive = false;
    protected ViewShip ship;
    protected final Map<String, Integer> gridComponents = new HashMap<>();
    protected ShipController.CellClickHandler cellClickHandler;
    private ViewPlayer currentPlayerBeingViewed;

    public void initialize() {
        String myUsername = ClientGameModel.getInstance().getUsername();
        this.currentPlayerBeingViewed = getPlayerFromModel(myUsername);

        if (this.currentPlayerBeingViewed == null) {
            showError("Could not load current player data. Building phase may not function correctly.");
            nonLearnerButtonsContainer.setVisible(false);
            nonLearnerButtonsContainer.setManaged(false);
        } else {
            loadShipData(this.currentPlayerBeingViewed);
        }

        covered();
        loadShip();
        loadUncoveredComponents();
        loadOtherPlayersList();

        ClientGameModel.getInstance().addListener(this);

        coveredDeckPane.setOnMouseClicked(_ -> {
            if (isViewingOwnShip()) {
                takeCoveredComponent();
            } else {
                showError("You can only take covered components for your own ship.");
            }
        });

        componentInHandPane.setOnMouseClicked(_ -> {
            if (isViewingOwnShip()) {
                activatePlacementMode();
            } else {
                showError("Placement mode is only available for your own ship.");
            }
        });
        updateComponentInHand();
        updateBookedComponents();
    }

    private void covered(){
        if (coveredDeckPane != null) {
            try {
                Image deckBackgroundImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/fxml/tiles/component.png")));
                if (deckBackgroundImage.isError()) {
                    coveredDeckPane.setStyle(coveredDeckPane.getStyle() + "; -fx-background-color: lightgrey;");
                } else {
                    BackgroundImage bgImage = new BackgroundImage(
                            deckBackgroundImage,
                            BackgroundRepeat.NO_REPEAT,
                            BackgroundRepeat.NO_REPEAT,
                            BackgroundPosition.CENTER,
                            new BackgroundSize(1.0, 1.0, true, true, true, false)
                    );
                    coveredDeckPane.setBackground(new Background(bgImage));
                }
            } catch (Exception e) {
                showError("Could not load deck background image: " + e.getMessage());
                coveredDeckPane.setStyle(coveredDeckPane.getStyle() + "; -fx-background-color: lightgrey;");
            }
        }
    }

    private ViewPlayer getPlayerFromModel(String username) {
        ViewPlayer[] players = ClientGameModel.getInstance().getPlayers();
        if (players == null || username == null) return null;
        return Arrays.stream(players)
                .filter(p -> p != null && username.equals(p.username))
                .findFirst()
                .orElse(null);
    }

    private boolean isViewingOwnShip() {
        return currentPlayerBeingViewed != null &&
                currentPlayerBeingViewed.username.equals(ClientGameModel.getInstance().getUsername());
    }

    private void loadShipData(ViewPlayer playerToView) {
        if (playerToView == null) {
            showError("Player data is null. Cannot display ship.");
            clearAllComponents();
            updateStatisticBoard(null);
            nonLearnerButtonsContainer.setVisible(false);
            nonLearnerButtonsContainer.setManaged(false);
            if(componentInHandPane != null) componentInHandPane.getChildren().clear();
            return;
        }

        this.currentPlayerBeingViewed = playerToView;
        this.ship = ClientGameModel.getInstance().getShip(playerToView.username);

        updateStatisticBoard(playerToView);

        if (this.ship == null) {
            System.out.println("Ship data not found for player: " + playerToView.username + ". Displaying empty grid.");
            clearAllComponents();
        } else {
            buildShipComponents(this.ship);
        }

        boolean isLoggedInPlayerLearner = (this.ship != null) && this.ship.isLearner;

        nonLearnerButtonsContainer.setVisible(!isLoggedInPlayerLearner);
        nonLearnerButtonsContainer.setManaged(!isLoggedInPlayerLearner);

        updateComponentInHand();
    }

    private void loadOtherPlayersList() {
        ClientGameModel model = ClientGameModel.getInstance();
        ViewPlayer[] allPlayers = model.getPlayers();
        String myUsername = model.getUsername();

        if (allPlayers == null) return;

        List<ViewPlayer> displayPlayers = Arrays.stream(allPlayers)
                .filter(p -> p != null && !p.username.equals(myUsername))
                .collect(Collectors.toList());

        otherPlayersShipsList.getItems().setAll(displayPlayers);

        otherPlayersShipsList.setCellFactory(_ -> new ListCell<>() {
            @Override
            protected void updateItem(ViewPlayer player, boolean empty) {
                super.updateItem(player, empty);
                if (empty || player == null) {
                    setText(null);
                    setStyle("-fx-background-color: rgba(51,51,68,0.8)");
                    setOnMouseClicked(null);
                } else {
                    setText(player.username);
                    setStyle("-fx-text-fill: white; -fx-background-color: rgba(51,51,68,0.8);");

                    setOnMouseClicked(_ -> {
                        ViewPlayer clickedPlayer = getItem();
                        if (clickedPlayer != null && !clickedPlayer.username.equals(myUsername)) {
                            navigateToOpponentShipScreen(clickedPlayer);
                        }
                    });
                }
            }
        });

        otherPlayersShipsList.getSelectionModel().selectedItemProperty().addListener((_, _, newSelection) -> {
            if (newSelection != null) {
                if (!newSelection.username.equals(myUsername)) {
                    navigateToOpponentShipScreen(newSelection);
                } else {
                    System.out.println("Selected own player from list. View remains on own ship.");
                }
            }
        });

        if (this.currentPlayerBeingViewed != null) {
            Optional<ViewPlayer> selfInList = displayPlayers.stream()
                    .filter(p -> p.username.equals(this.currentPlayerBeingViewed.username))
                    .findFirst();
            selfInList.ifPresent(player -> otherPlayersShipsList.getSelectionModel().select(player));
        }
    }

    private void navigateToOpponentShipScreen(ViewPlayer opponent) {
        System.out.println("Navigating to view ship of: " + opponent.username);
        try {
            String shipType;
            if (ship.isLearner) {
                shipType = "ship0";
            } else{
                shipType = "ship2";
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + shipType + ".fxml"));
            Parent opponentViewRoot = loader.load();

            Object controller = loader.getController();
            if (controller instanceof ShipController c) {
                c.buildShipComponents(ClientGameModel.getInstance().getShip(opponent.username));
                c.updateStatisticBoard(opponent);
            }

            Stage stage = new Stage();
            stage.setTitle("Opponent Ship: " + opponent.username);
            stage.setScene(new Scene(opponentViewRoot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setWidth(500);
            stage.setHeight(500);
            stage.showAndWait();

        } catch (IOException e) {
            showError("Could not load opponent ship view: " + e.getMessage());
        }
    }

    private void loadShip() {
        ship = ClientGameModel.getInstance().getShip(ClientGameModel.getInstance().getUsername());
        ClientGameModel clientGameModel = ClientGameModel.getInstance();
        if (clientGameModel != null) {
            String currentUsername = clientGameModel.getUsername();
            ViewPlayer[] players = clientGameModel.getPlayers();
            if (players != null && currentUsername != null) {
                Optional<ViewPlayer> currentPlayerOpt = Arrays.stream(players)
                        .filter(p -> currentUsername.equals(p.username))
                        .findFirst();
                currentPlayerOpt.ifPresent(this::updateStatisticBoard);
            }
        }
        buildShipComponents(ship);
    }

    public void buildShipComponents(ViewShip ship) {
        if (ship == null || componentsGrid == null) return;

        clearAllComponents();

        for (int row = 0; row < getRows(); row++) {
            for (int col = 0; col < getCols(); col++) {
                ViewComponent comp = ship.getComponent(row, col);
                if (comp != null) {
                    addComponent(comp, row, col);
                }
            }
        }
    }

    public void clearAllComponents() {
        componentsGrid.getChildren().clear();

        // Re-add all empty ImageViews
        for (int row = 0; row < getRows(); row++) {
            for (int col = 0; col < getCols(); col++) {
                ImageView cell = getImageViewAt(row, col);
                if (cell != null) {
                    cell.setImage(null);
                    GridPane.setFillWidth(cell, true);
                    GridPane.setFillHeight(cell, true);
                    componentsGrid.add(cell, col, row);
                }
            }
        }
        gridComponents.clear();
    }

    protected ImageView getImageViewAt(int row, int col) {
        return null;
    }

    public boolean addComponent(ViewComponent comp, int row, int col) {
        int componentId = comp.id;
        if (row < 0 || row >= getRows() || col < 0 || col >= getCols()) {
            return false;
        }

        String cellId = row + "_" + col;
        ImageView targetCell = getImageViewAt(row, col);

        if (targetCell == null) {
            return false;
        }

        // Remove the old cell and create a new layered pane
        GridPane parent = (GridPane) targetCell.getParent();
        parent.getChildren().remove(targetCell);
        StackPane layeredPane = new StackPane();

        String imagePath = "/fxml/tiles/" + componentId + ".jpg";
        try {
            Image componentImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
            ImageView imageView = new ImageView(componentImage); // Create a new ImageView

            if (comp.rotComp >= 0 && comp.rotComp <= 3) {
                imageView.setRotate(comp.rotComp * 90); // Rotate the new ImageView
            }

            imageView.setFitWidth(targetCell.getFitWidth());
            imageView.setFitHeight(targetCell.getFitHeight());

            layeredPane.getChildren().add(imageView); // Add the rotated ImageView to the layeredPane
            setComponentProp(layeredPane, comp);

            // Make sure the layered pane fills the cell
            layeredPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            GridPane.setFillWidth(layeredPane, true);
            GridPane.setFillHeight(layeredPane, true);
            GridPane.setHgrow(layeredPane, Priority.ALWAYS);
            GridPane.setVgrow(layeredPane, Priority.ALWAYS);

            parent.add(layeredPane, col, row);

            gridComponents.put(cellId, componentId);
            return true;
        } catch (Exception e) {
            showError("Could not load component image: " + e.getMessage());
            return false;
        }
    }

    public void setComponentProp(StackPane layeredPane, ViewComponent comp) {
        if (comp.isCabin()) {
            setComponentProp(layeredPane, (ViewCabin) comp);
        } else if (comp.isBattery()) {
            setComponentProp(layeredPane, (ViewBattery) comp);
        }
    }

    public void setComponentProp(StackPane layeredPane, ViewBattery comp) {
        Label batteryLabel = new Label(Integer.toString(comp.availableEnergy));
        batteryLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-background-color: rgba(0,0,0,0.7); -fx-padding: 2px;");

        try {
            ImageView batteryIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/icons/battery.png"))));
            batteryIcon.fitWidthProperty().bind(layeredPane.widthProperty().multiply(0.3));
            batteryIcon.fitHeightProperty().bind(layeredPane.heightProperty().multiply(0.3));
            batteryIcon.setPreserveRatio(true);

            StackPane.setAlignment(batteryLabel, javafx.geometry.Pos.TOP_LEFT);
            StackPane.setAlignment(batteryIcon, javafx.geometry.Pos.BOTTOM_LEFT);

            layeredPane.getChildren().addAll(batteryLabel, batteryIcon);
        } catch (Exception e) {
            System.err.println("Unable to load battery: " + e.getMessage());
            layeredPane.getChildren().add(batteryLabel);
        }
    }

    public void setComponentProp(StackPane layeredPane, ViewCabin comp) {
        if (comp.alien) {
            String alienImagePath = comp.alienColor == AlienColor.PURPLE ?
                    "/images/icons/purple_alien.png" : "/images/icons/brown_alien.png";

            try {
                ImageView alienIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(alienImagePath))));
                alienIcon.fitWidthProperty().bind(layeredPane.widthProperty().multiply(0.4));
                alienIcon.fitHeightProperty().bind(layeredPane.heightProperty().multiply(0.4));
                alienIcon.setPreserveRatio(true);

                StackPane.setAlignment(alienIcon, javafx.geometry.Pos.TOP_LEFT);
                layeredPane.getChildren().add(alienIcon);
            } catch (Exception e) {
                System.err.println("Unable to load alien: " + e.getMessage());
            }
        } else if (comp.astronauts > 0) {
            Label astronautsLabel = new Label(Integer.toString(comp.astronauts));
            astronautsLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-background-color: rgba(0,0,0,0.7); -fx-padding: 2px;");

            try {
                ImageView astronautIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/icons/astr.png"))));
                astronautIcon.fitWidthProperty().bind(layeredPane.widthProperty().multiply(0.3));
                astronautIcon.fitHeightProperty().bind(layeredPane.heightProperty().multiply(0.3));
                astronautIcon.setPreserveRatio(true);

                StackPane.setAlignment(astronautsLabel, javafx.geometry.Pos.TOP_LEFT);
                StackPane.setAlignment(astronautIcon, javafx.geometry.Pos.BOTTOM_LEFT);

                layeredPane.getChildren().addAll(astronautsLabel, astronautIcon);
            } catch (Exception e) {
                System.err.println("Unable to load astronaut: " + e.getMessage());
                layeredPane.getChildren().add(astronautsLabel);
            }
        }

        if (comp.cabinColor != AlienColor.NONE) {
            String colorStr = switch (comp.cabinColor) {
                case PURPLE -> "purple";
                case BROWN -> "brown";
                case BOTH -> "linear-gradient(45deg, purple 50%, brown 50%)";
                default -> null;
            };

            if (colorStr != null) {
                Label colorIndicator = new Label("");
                colorIndicator.setStyle("-fx-background: " + colorStr +
                        "; -fx-min-width: 15px; -fx-min-height: 15px; -fx-border-color: white; -fx-border-width: 1px;");
                StackPane.setAlignment(colorIndicator, javafx.geometry.Pos.BOTTOM_RIGHT);
                StackPane.setMargin(colorIndicator, new Insets(0, 5, 5, 0));
                layeredPane.getChildren().add(colorIndicator);
            }
        }
    }

    public void updateStatisticBoard(ViewPlayer player) {
        if (player != null) {
            playerColorLabel.setText("Color: " + (player.playerColor != null ? player.playerColor.name() : "N/A"));
            usernameLabel.setText("Username: " + player.username);
            creditsLabel.setText("Credits: " + player.credits);
            inGameLabel.setText("In Game: " + (player.inGame ? "Yes" : "No"));
        }
    }

    private void loadUncoveredComponents() {
        uncoveredComponentsPane.getChildren().clear();
        ClientGameModel client = ClientGameModel.getInstance();
        List<ViewComponent> uncoveredComponents;
        if (client.getBoard() != null && client.getBoard().viewedPile != null) {
            uncoveredComponents = client.getBoard().viewedPile;
            for (ViewComponent component : uncoveredComponents) {
                Pane componentPane = createComponentPane(component);
                // Take the index of the component in the list to use as a unique identifier
                int index = uncoveredComponents.indexOf(component);
                componentPane.setOnMouseClicked(_ -> selectComponent(component, componentPane, index));
                uncoveredComponentsPane.getChildren().add(componentPane);
            }
        }
    }

    private void takeCoveredComponent() {
        if (ClientGameModel.getInstance().getComponentInHand() != null) {
            showError("You already have a component in hand!");
            return;
        }
        ClientGameModel model = ClientGameModel.getInstance();
        int maxIndex = model.getBoard().unviewedPile - 1;

        TextInputDialog dialog = new TextInputDialog("0");
        dialog.setTitle("Take Covered Component");
        dialog.setHeaderText("Select a component from the covered deck");
        dialog.setContentText("Enter index (0 to " + maxIndex + "):");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(indexStr -> {
            try {
                int index = Integer.parseInt(indexStr);
                if (index >= 0 && index <= maxIndex) {
                    String username = model.getUsername();
                    model.getClient().takeComponentFromUnviewed(username, index);
                    updateComponentInHand();

                } else {
                    showError("Invalid index. Please enter a number between 0 and " + maxIndex);
                }
            } catch (NumberFormatException e) {
                showError("Please enter a valid number");
            } catch (Exception e) {
                showError("Error taking component: " + e.getMessage());
            }
        });
    }

    protected void updateComponentInHand() {
        ViewComponent componentInHand = ClientGameModel.getInstance().getComponentInHand();
        componentInHandPane.getChildren().clear();

        HBox rotationButtonsContainer = (HBox) rootPane.lookup("#rotationButtonsContainer");
        if (rotationButtonsContainer != null) {
            rotationButtonsContainer.setVisible(true);
            rotationButtonsContainer.setManaged(true);
        }

        if (componentInHand != null) {
            Pane componentPane = createComponentPane(componentInHand);
            componentPane.setTranslateX((componentInHandPane.getWidth() - componentPane.getPrefWidth()) / 2);
            componentPane.setTranslateY((componentInHandPane.getHeight() - componentPane.getPrefHeight()) / 2);

            componentPane.translateXProperty().bind(
                    componentInHandPane.widthProperty().subtract(componentPane.prefWidthProperty()).divide(2)
            );
            componentPane.translateYProperty().bind(
                    componentInHandPane.heightProperty().subtract(componentPane.prefHeightProperty()).divide(2)
            );

            componentInHandPane.getChildren().add(componentPane);
            selectedComponent = componentInHand;
        } else {
            if (rotationButtonsContainer != null) {
                rotationButtonsContainer.setVisible(false);
                rotationButtonsContainer.setManaged(false);
            }
            selectedComponent = null;
        }
    }

    private Pane createComponentPane(ViewComponent component) {
        Pane pane = new Pane();
        pane.setPrefSize(80, 80);

        if (component == null) {
            return pane;
        }

        String imageUrl = getComponentImageUrl(component);
        try {
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imageUrl)));
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(70);
            imageView.setFitHeight(70);
            imageView.setPreserveRatio(true);
            imageView.setX(5);
            imageView.setY(5);
            if (component.rotComp >= 0 && component.rotComp <= 3) {
                imageView.setRotate(component.rotComp * 90);
            }
            pane.getChildren().add(imageView);
        } catch (Exception e) {
            Rectangle fallback = new Rectangle(5, 5, 70, 70);
            pane.getChildren().add(fallback);
        }

        return pane;
    }

    private String getComponentImageUrl(ViewComponent component) {
        if (component == null) return null;

        return "/fxml/tiles/" + component.id + ".jpg";
    }

    /**
     * Shows an error message in a dialog window
     */
    public void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Stops ship assembly and finishes the building phase
     */
    @FXML
    private void stopAssembling() {
        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setTitle("Stop Assembling Ship");
        dialog.setHeaderText("Choose your starting board position");
        dialog.setContentText("Enter board index (1-4):");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(boardIndex -> {
            try {
                int index = Integer.parseInt(boardIndex);
                if (index >= 1 && index <= 4) {
                    ClientGameModel.getInstance().getClient().stopAssembling(
                            ClientGameModel.getInstance().getUsername(),
                            index
                    );
                } else {
                    showError("Invalid board index. Please enter a number between 1 and 4.");
                }
            } catch (NumberFormatException e) {
                showError("Please enter a valid number.");
            } catch (Exception e) {
                showError("Error: " + e.getMessage());
            }
        });
    }

    /**
     * Takes a component from the booked components
     */
    @FXML
    private void takeBookedComponent() {
        TextInputDialog dialog = new TextInputDialog("0");
        dialog.setTitle("Take Booked Component");
        dialog.setHeaderText("Select a booked component to take");
        dialog.setContentText("Enter component index (0 or 1):");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(indexStr -> {
            try {
                int index = Integer.parseInt(indexStr);
                if (index == 0 || index == 1) {
                    String username = ClientGameModel.getInstance().getUsername();
                    ClientGameModel.getInstance().getClient().takeComponentFromBooked(username, index);
                } else {
                    showError("Invalid index. Please enter 0 or 1.");
                }
            } catch (NumberFormatException e) {
                showError("Please enter a valid number.");
            } catch (Exception e) {
                showError("Error: " + e.getMessage());
            }
        });
    }

    /**
     * Turns the hourglass
     */
    @FXML
    private void turnHourglass() {
        try {
            String username = ClientGameModel.getInstance().getUsername();
            ClientGameModel.getInstance().getClient().turnHourglass(username);
        } catch (Exception e) {
            showError("Error turning hourglass: " + e.getMessage());
        }
    }

    /**
     * Peeks at a deck of cards
     */
    @FXML
    private void peekDeck() {
        TextInputDialog dialog = new TextInputDialog("0");
        dialog.setTitle("Peek Deck");
        dialog.setHeaderText("Select a deck to peek");
        dialog.setContentText("Enter deck index (1, 2, 3):");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(indexStr -> {
            try {
                int index = Integer.parseInt(indexStr);
                if (index > 0 && index <= 3) {
                    String username = ClientGameModel.getInstance().getUsername();
                    ClientGameModel.getInstance().getClient().peekDeck(username, index);
                } else {
                    showError("Invalid index. Please enter a number between 1 and 3.");
                }
            } catch (NumberFormatException e) {
                showError("Please enter a valid number.");
            } catch (Exception e) {
                showError("Error: " + e.getMessage());
            }
        });
    }

    /**
     * Books the component currently in hand
     */
    @FXML
    private void bookComponent() {
        if (selectedComponent == null) {
            showError("No component selected!");
            return;
        }

        try {
            String username = ClientGameModel.getInstance().getUsername();
            ClientGameModel.getInstance().getClient().addComponentToBooked(username);
            selectedComponent = null;
            componentInHandPane.getChildren().clear();
        } catch (Exception e) {
            showError("Error booking component: " + e.getMessage());
        }
    }

    private void selectComponent(ViewComponent ignoredComponent, Pane sourcePane, int index) {

        // If there's a componentInHand, ignore
        if (ClientGameModel.getInstance().getComponentInHand() != null) {
            return;
        }

        // Add to componentInHand
        try{
            ClientGameModel.getInstance().getClient().takeComponentFromViewed(ClientGameModel.getInstance().getUsername(), index);
        } catch (RemoteException e) {
            showError("Error selecting component: " + e.getMessage());
            return;
        }
        uncoveredComponentsPane.getChildren().forEach(node -> node.setStyle(node.getStyle().replace("-fx-border-width: 3;", "")));
        sourcePane.setStyle(sourcePane.getStyle() + "-fx-border-width: 3;");
    }

    protected void activatePlacementMode() {
        if (ClientGameModel.getInstance().getComponentInHand() != null) {
            placementModeActive = true;
            componentInHandPane.setStyle("-fx-border-color: green; -fx-border-width: 2;");
            uncoveredComponentsPane.setStyle("-fx-border-color: green; -fx-border-width: 2;");

            enableGridInteraction(this::handleCellClick);
            enableUncoveredComponentsInteraction(this::handleUncoveredClick);
        } else {
            System.out.println("No component in hand to place");
        }
    }

    protected void deactivatePlacementMode(){
        disableGridInteraction();
        disableUncoveredComponentsInteraction();
    }

    public void enableUncoveredComponentsInteraction(Runnable handler) {
        if (uncoveredComponentsPane != null) {
            uncoveredComponentsPane.setOnMouseClicked(_ -> {
                if (handler != null) {
                    handler.run();
                }
            });
            uncoveredComponentsPane.setCursor(Cursor.HAND);
            System.out.println("Uncovered components interaction enabled.");
        } else {
            System.err.println("uncoveredComponentsPane is null. Cannot enable interaction.");
        }
    }

    public void enableGridInteraction(ShipController.CellClickHandler handler) {
        this.cellClickHandler = handler;
        System.out.println("Placing mode activated");

        for (int row = 0; row < getRows(); row++) {
            for (int col = 0; col < getCols(); col++) {
                if(!checkIsValid(row,col))
                    continue;

                final int finalRow = row;
                final int finalCol = col;

                Rectangle clickArea = new Rectangle();
                clickArea.setFill(javafx.scene.paint.Color.TRANSPARENT);
                clickArea.setStroke(javafx.scene.paint.Color.LIGHTGREEN);
                clickArea.setStrokeWidth(2);
                clickArea.setOpacity(0.7);

                // Bind size to grid cell
                clickArea.widthProperty().bind(
                        componentsGrid.widthProperty().divide(getCols()).subtract(getCols())
                );
                clickArea.heightProperty().bind(
                        componentsGrid.heightProperty().divide(getRows()).subtract(getRows())
                );

                clickArea.setOnMouseClicked(_ -> {
                    if (cellClickHandler != null) {
                        cellClickHandler.onCellClicked(finalRow, finalCol);
                    }
                });

                clickArea.setOnMouseEntered(_ -> {
                    clickArea.setFill(javafx.scene.paint.Color.color(0, 1, 0, 0.2));
                    clickArea.setCursor(javafx.scene.Cursor.HAND);
                });

                clickArea.setOnMouseExited(_ -> {
                    clickArea.setFill(javafx.scene.paint.Color.TRANSPARENT);
                    clickArea.setCursor(javafx.scene.Cursor.DEFAULT);
                });

                componentsGrid.add(clickArea, col, row);
                GridPane.setHalignment(clickArea, javafx.geometry.HPos.CENTER);
                GridPane.setValignment(clickArea, javafx.geometry.VPos.CENTER);
            }
        }
    }

    protected abstract boolean checkIsValid(int row,int col);

    protected void handleCellClick(int row, int col) {
        if (placementModeActive && ClientGameModel.getInstance().getComponentInHand() != null) {
            try {
                String username = ClientGameModel.getInstance().getUsername();
                ClientGameModel.getInstance().getClient().placeComponent(username, new org.javatuples.Pair<>(row, col));

                placementModeActive = false;
                componentInHandPane.setStyle("-fx-border-color: #444; -fx-border-width: 1;");

                deactivatePlacementMode();

                new Thread(() -> {
                    try {
                        Thread.sleep(500);
                        javafx.application.Platform.runLater(() -> {
                            updateComponentInHand();
                                buildShipComponents(ClientGameModel.getInstance().getShip(username));
                        });
                    } catch (InterruptedException e) {
                        showError("Error placing component: " + e.getMessage());
                    }
                }).start();
            } catch (Exception e) {
                placementModeActive = false;
                componentInHandPane.setStyle("-fx-border-color: #444; -fx-border-width: 1;");
                showError("Error during placement: " + e.getMessage());
            }
        }
    }

    protected void handleUncoveredClick() {
        if (placementModeActive && ClientGameModel.getInstance().getComponentInHand() != null) {
            try {
                String username = ClientGameModel.getInstance().getUsername();
                if (username == null) {
                    showError("User not identified. Cannot add component to viewed pile.");
                    return;
                }
                ClientGameModel.getInstance().getClient().addComponentToViewed(username);

                placementModeActive = false;
                componentInHandPane.setStyle("-fx-border-color: #444; -fx-border-width: 1;");

                deactivatePlacementMode();

                new Thread(() -> {
                    try {
                        Thread.sleep(200);
                        Platform.runLater(() -> {
                            updateComponentInHand();
                            loadUncoveredComponents();
                        });
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        Platform.runLater(() -> showError("UI update interrupted after adding to viewed pile."));
                    }
                }).start();

            } catch (Exception e) {
                placementModeActive = false;
                componentInHandPane.setStyle("-fx-border-color: #444; -fx-border-width: 1;");
                deactivatePlacementMode();
                showError("Error adding component to viewed pile: " + e.getMessage());
            }
        } else {
            if (!placementModeActive) {
                System.out.println("Uncovered components pane clicked, but placement mode is not active.");
            } else if (ClientGameModel.getInstance().getComponentInHand() == null) {
                Platform.runLater(() -> showError("No component in hand to add to viewed pile."));
            }
        }
    }

    public void disableUncoveredComponentsInteraction() {
        if (uncoveredComponentsPane != null) {
            uncoveredComponentsPane.setOnMouseClicked(null);
            uncoveredComponentsPane.setCursor(Cursor.DEFAULT);
            uncoveredComponentsPane.setStyle("-fx-border-color: #444; -fx-border-width: 1;");
            System.out.println("Uncovered components interaction deactivated.");
        }
    }

    public void disableGridInteraction() {
        this.cellClickHandler = null;
        System.out.println("Placing mode deactivated");

        componentsGrid.getChildren().removeIf(node -> node instanceof Rectangle);
    }

    protected abstract int getRows();
    protected abstract int getCols();

    @FXML
    private void rotateComponentClockwise() {
        try {
            String username = ClientGameModel.getInstance().getUsername();
            ClientGameModel.getInstance().getClient().rotateComponentClockwise(username);
        } catch (Exception e) {
            showError("Error rotating component clockwise: " + e.getMessage());
        }
    }

    @FXML
    private void rotateComponentCounterclockwise() {
        try {
            String username = ClientGameModel.getInstance().getUsername();
            ClientGameModel.getInstance().getClient().rotateComponentCounterclockwise(username);
        } catch (Exception e) {
            showError("Error rotating component counterclockwise: " + e.getMessage());
        }
    }

    @FXML
    private void navigateToGameBoard() {
        try {
            String boardType;
            if (ship != null && ship.isLearner) {
                boardType = "board0";
            } else {
                boardType = "board2";
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + boardType + ".fxml"));
            Parent boardViewRoot = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Game Board");
            stage.setScene(new Scene(boardViewRoot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            showError("Unable to load Game Board: " + e.getMessage());
        }
    }

    protected void handleBookedClick(int index) {
        String username = ClientGameModel.getInstance().getUsername();
        try {
            ClientGameModel.getInstance().getClient().addComponentToBooked(username);
        } catch (RemoteException e) {
            System.err.println("Error taking component from booked: " + e.getMessage());
        }
    }

    protected void disableBookedComponentsInteraction(){
        if (bookedGrid != null) {
            java.util.List<javafx.scene.Node> nodesToRemove = new java.util.ArrayList<>();

            for (javafx.scene.Node node : bookedGrid.getChildren()) {
                if (node instanceof Rectangle) {
                    nodesToRemove.add(node);
                }
            }
            bookedGrid.getChildren().removeAll(nodesToRemove);
            bookedGrid.setOnMouseClicked(null);
        }
    }

    protected abstract void updateBookedComponents();

    @Override
    public void onShipUpdated(ViewShip ship) {
        if (this.ship.equals(ship)) {
            buildShipComponents(this.ship);
        }
    }

    @Override
    public void onLobbyUpdated(ViewLobby lobby) {
        // Not needed for this controller
    }


    @Override
    public void onErrorMessageReceived(String message) {
        showError(message);
    }

    @Override
    public void onComponentInHandUpdated(ViewComponent component) {
        updateComponentInHand();
        loadUncoveredComponents();
        updateBookedComponents();
    }
}