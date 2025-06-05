package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.components.*;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class BuildingPhaseController implements Initializable {

    @FXML
    private BorderPane shipContainer;

    @FXML
    private Pane componentInHandPane;

    @FXML
    private Pane coveredDeckPane;

    @FXML
    private FlowPane uncoveredComponentsPane;

    @FXML
    private HBox nonLearnerButtonsContainer;

    @FXML
    private HBox nonLearnerComponentInHandButtons;

    private ViewComponent selectedComponent;
    private ShipController shipController;
    private boolean placementModeActive = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadShip();
        loadCoveredDeck();
        loadUncoveredComponents();

        String username = ClientGameModel.getInstance().getUsername();
        boolean isLearner = ClientGameModel.getInstance().getShip(username).isLearner;
        nonLearnerButtonsContainer.setVisible(!isLearner);
        nonLearnerButtonsContainer.setManaged(!isLearner);
        coveredDeckPane.setOnMouseClicked(event -> takeCoveredComponent());
        componentInHandPane.setOnMouseClicked(event -> activatePlacementMode());
    }

    private void loadShip() {
        try {
            String username = ClientGameModel.getInstance().getUsername();
            ViewShip playerShip = ClientGameModel.getInstance().getShip(username);

            String shipFileName;
            if (playerShip != null && playerShip.isLearner) {
                shipFileName = "ship0";
            } else {
                shipFileName = "ship2";
            }

            String path = "/fxml/" + shipFileName + ".fxml";
            URL resourceUrl = getClass().getResource(path);

            FXMLLoader loader = new FXMLLoader(resourceUrl);
            Parent shipRoot = loader.load();

            shipController = loader.getController();
            shipController.setShipState(ShipController.ShipState.Building);

            shipContainer.setCenter(shipRoot);

        } catch (IOException e) {
            e.printStackTrace();
            showError("Error loading ship: " + e.getMessage());
        }
    }

    private void loadCoveredDeck() {
        // TODO -> Mettere l'immagine della carta coperta (da creare)
        Rectangle coveredCard = new Rectangle(0, 0, 120, 80);
        coveredCard.setFill(Color.DARKGRAY);
        coveredCard.setStroke(Color.BLACK);
        coveredCard.setArcWidth(10);
        coveredCard.setArcHeight(10);
        coveredDeckPane.getChildren().add(coveredCard);
    }

    private void loadUncoveredComponents() {
        uncoveredComponentsPane.getChildren().clear();
        ClientGameModel client = ClientGameModel.getInstance();
        List<ViewComponent> uncoveredComponents = new ArrayList<>();
        if (client.getBoard() != null && client.getBoard().viewedPile != null) {
            uncoveredComponents = client.getBoard().viewedPile;
            for (ViewComponent component : uncoveredComponents) {
                Pane componentPane = createComponentPane(component);
                componentPane.setOnMouseClicked(event -> selectComponent(component, componentPane));
                uncoveredComponentsPane.getChildren().add(componentPane);
            }
        }
    }

    private void takeCoveredComponent() {
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

                    new Thread(() -> {
                        try {
                            Thread.sleep(500);
                            javafx.application.Platform.runLater(() -> {
                                updateComponentInHand();
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }).start();
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

    private void updateComponentInHand() {
        ViewComponent componentInHand = ClientGameModel.getInstance().getComponentInHand();

        componentInHandPane.getChildren().clear();

        if (componentInHand != null) {
            Pane componentPane = createComponentPane(componentInHand);
            componentInHandPane.getChildren().add(componentPane);
            selectedComponent = componentInHand;

            String username = ClientGameModel.getInstance().getUsername();
            boolean isLearner = ClientGameModel.getInstance().getShip(username).isLearner;
            nonLearnerComponentInHandButtons.setVisible(!isLearner);
            nonLearnerComponentInHandButtons.setManaged(!isLearner);
        }
    }

    /**
     * Creates a pane that visually represents a component
     */
    private Pane createComponentPane(ViewComponent component) {
        Pane pane = new Pane();
        pane.setPrefSize(80, 80);
        pane.setStyle("-fx-border-color: #444; -fx-border-width: 1;");

        if (component == null) {
            return pane;
        }

        String imageUrl = getComponentImageUrl(component);
        if (imageUrl != null) {
            try {
                javafx.scene.image.Image image = new javafx.scene.image.Image(getClass().getResourceAsStream(imageUrl));
                javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(image);
                imageView.setFitWidth(70);
                imageView.setFitHeight(70);
                imageView.setPreserveRatio(true);
                imageView.setX(5);
                imageView.setY(5);
                pane.getChildren().add(imageView);
            } catch (Exception e) {
                Rectangle fallback = new Rectangle(5, 5, 70, 70);
                pane.getChildren().add(fallback);
            }
        }

        return pane;
    }

    private String getComponentImageUrl(ViewComponent component) {
        if (component == null) return null;

        String imagePath = "/fxml/tiles/" + component.id + ".jpg";
        return imagePath;
    }

    /**
     * Rotates the selected component
     */
    @FXML
    private void rotateComponent() {
        if (selectedComponent != null) {
            componentInHandPane.getChildren().clear();
            componentInHandPane.getChildren().add(createComponentPane(selectedComponent));
        } else {
            showError("No component selected!");
        }
    }

    /**
     * Places the selected component on the ship
     */
    @FXML
    private void placeComponent() {
        if (selectedComponent == null) {
            showError("No component selected!");
            return;
        }

        if (shipController != null) {
            selectedComponent = null;
            componentInHandPane.getChildren().clear();

            loadUncoveredComponents();
        }
    }

    /**
     * Discards the selected component
     */
    @FXML
    private void discardComponent() {
        if (selectedComponent == null) {
            showError("No component selected!");
            return;
        }
        selectedComponent = null;
        componentInHandPane.getChildren().clear();

        loadUncoveredComponents();
    }

    /**
     * Shows an error message in a dialog window
     */
    private void showError(String message) {
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
        TextInputDialog dialog = new TextInputDialog("0");
        dialog.setTitle("Stop Assembling Ship");
        dialog.setHeaderText("Choose your starting board position");
        dialog.setContentText("Enter board index (0-4):");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(boardIndex -> {
            try {
                int index = Integer.parseInt(boardIndex);
                if (index >= 0 && index <= 4) {
                    ClientGameModel.getInstance().getClient().stopAssembling(
                            ClientGameModel.getInstance().getUsername(),
                            index
                    );
                } else {
                    showError("Invalid board index. Please enter a number between 0 and 4.");
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
        dialog.setContentText("Enter deck index (0-2):");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(indexStr -> {
            try {
                int index = Integer.parseInt(indexStr);
                if (index >= 0 && index <= 2) {
                    String username = ClientGameModel.getInstance().getUsername();
                    ClientGameModel.getInstance().getClient().peekDeck(username, index);
                } else {
                    showError("Invalid index. Please enter a number between 0 and 2.");
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

    private void selectComponent(ViewComponent component, Pane sourcePane) {
        selectedComponent = component;

        componentInHandPane.getChildren().clear();
        componentInHandPane.getChildren().add(createComponentPane(component));

        uncoveredComponentsPane.getChildren().forEach(node -> {
            node.setStyle(node.getStyle().replace("-fx-border-width: 3;", ""));
        });
        sourcePane.setStyle(sourcePane.getStyle() + "-fx-border-width: 3;");

        String username = ClientGameModel.getInstance().getUsername();
        boolean isLearner = ClientGameModel.getInstance().getShip(username).isLearner;
        nonLearnerComponentInHandButtons.setVisible(!isLearner);
        nonLearnerComponentInHandButtons.setManaged(!isLearner);
    }

    private void activatePlacementMode() {
        if (ClientGameModel.getInstance().getComponentInHand() != null) {
            System.out.println("No component in hand");
            placementModeActive = true;
            componentInHandPane.setStyle("-fx-border-color: green; -fx-border-width: 2;");

            if (shipController != null) {
                shipController.enableGridInteraction(this::handleCellClick);
            } else {
                System.out.println("ERROR: ShipController is null");
            }
        } else {
            System.out.println("No component in hand to place");
        }
    }

    private void handleCellClick(int row, int col) {
        if (placementModeActive && ClientGameModel.getInstance().getComponentInHand() != null) {
            try {
                String username = ClientGameModel.getInstance().getUsername();
                ClientGameModel.getInstance().getClient().placeComponent(username, new org.javatuples.Pair<>(row, col));

                placementModeActive = false;
                componentInHandPane.setStyle("-fx-border-color: #444; -fx-border-width: 1;");

                if (shipController != null) {
                    shipController.disableGridInteraction();
                }

                new Thread(() -> {
                    try {
                        Thread.sleep(500);
                        javafx.application.Platform.runLater(() -> {
                            updateComponentInHand();
                            if (shipController != null) {
                                shipController.buildShipComponents(ClientGameModel.getInstance().getShip(username));
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            } catch (Exception e) {
                placementModeActive = false;
                componentInHandPane.setStyle("-fx-border-color: #444; -fx-border-width: 1;");
                showError("Error during placement: " + e.getMessage());
            }
        }
    }
}