package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.components.*;
import it.polimi.ingsw.gc20.server.model.components.AlienColor;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ship0Controller {

    @FXML private ImageView boardImageView;
    @FXML private GridPane componentsGrid; // Allineato con il nome nel FXML
    @FXML private Pane gridWrapper;
    @FXML private Label X_Label;
    @FXML private Label Y_Label;


    // 3 Slot -> 60x90, 125x57, 125x124
    // 2 Slot -> 90x57, 90x124
    private final List<int[]> cargoCord3 = List.of(
            new int[]{60, 90},
            new int[]{125, 57},
            new int[]{125, 124}
    );

    private final List<int[]> cargoCord2 = List.of(
            new int[]{90, 57},
            new int[]{90, 124}
    );


    @FXML private ImageView imageCell_0_0;
    @FXML private ImageView imageCell_0_1;
    @FXML private ImageView imageCell_0_2;
    @FXML private ImageView imageCell_0_3;
    @FXML private ImageView imageCell_0_4;
    @FXML private ImageView imageCell_1_0;
    @FXML private ImageView imageCell_1_1;
    @FXML private ImageView imageCell_1_2;
    @FXML private ImageView imageCell_1_3;
    @FXML private ImageView imageCell_1_4;
    @FXML private ImageView imageCell_2_0;
    @FXML private ImageView imageCell_2_1;
    @FXML private ImageView imageCell_2_2;
    @FXML private ImageView imageCell_2_3;
    @FXML private ImageView imageCell_2_4;
    @FXML private ImageView imageCell_3_0;
    @FXML private ImageView imageCell_3_1;
    @FXML private ImageView imageCell_3_2;
    @FXML private ImageView imageCell_3_3;
    @FXML private ImageView imageCell_3_4;
    @FXML private ImageView imageCell_4_0;
    @FXML private ImageView imageCell_4_1;
    @FXML private ImageView imageCell_4_2;
    @FXML private ImageView imageCell_4_3;
    @FXML private ImageView imageCell_4_4;

    private final Map<String, Integer> gridComponents = new HashMap<>();

    private final int ROWS = 5;
    private final int COLS = 5;

    @FXML
    private void initialize() {
        if (boardImageView != null && boardImageView.getImage() != null) {
            setupGridBounds();
        } else if (boardImageView != null) {
            boardImageView.imageProperty().addListener((obs, oldImg, newImg) -> {
                if (newImg != null) {
                    setupGridBounds();
                }
            });
        }
    }

    private void setupGridBounds() {
        Platform.runLater(() -> {
            if (boardImageView == null || componentsGrid == null) return;

            double boardWidth = boardImageView.getBoundsInParent().getWidth();
            double boardHeight = boardImageView.getBoundsInParent().getHeight();

            double gridX = boardWidth * 0.25;
            double gridY = boardHeight * 0.18;
            double gridWidth = boardWidth * 0.5;
            double gridHeight = boardHeight * 0.64;

            componentsGrid.setLayoutX(gridX);
            componentsGrid.setLayoutY(gridY);
            componentsGrid.setPrefSize(gridWidth, gridHeight);
            componentsGrid.setMaxSize(gridWidth, gridHeight);

            updateCoordinateLabels(gridX, gridY);
        });
    }

    private void updateCoordinateLabels(double x, double y) {
        if (X_Label != null) {
            X_Label.setText(String.format("X: %.2f", x));
        }
        if (Y_Label != null) {
            Y_Label.setText(String.format("Y: %.2f", y));
        }
    }

    public boolean addComponent(ViewComponent comp, int row, int col) {
        int componentId = comp.id;
        if (row < 0 || row >= ROWS || col < 0 || col >= COLS) {
            return false;
        }

        String cellId = row + "_" + col;
        ImageView targetCell = getImageViewAt(row, col);

        if (targetCell == null) {
            return false;
        }

        GridPane parent = (GridPane) targetCell.getParent();
        parent.getChildren().remove(targetCell);
        StackPane layeredPane = new StackPane();

        String imagePath = "/tiles/" + componentId + ".png";
        try {
            Image componentImage = new Image(getClass().getResourceAsStream(imagePath));
            targetCell.setImage(componentImage);
            layeredPane.getChildren().add(targetCell);

            setComponentProp(layeredPane, comp);

            parent.add(layeredPane, col, row);

            // Rotate the IMG
            gridComponents.put(cellId, componentId);
            return true;
        } catch (Exception e) {
            System.err.println("Impossibile caricare l'immagine del componente: " + e.getMessage());
            return false;
        }
    }

    public void setComponentProp(StackPane layeredPane, ViewComponent comp) {
        return;
    }

    public void setComponentProp(StackPane layeredPane, ViewBattery comp) {
        // Implementazione specifica per ViewBattery, se necessario
        return;
    }

    public void setComponentProp(StackPane layeredPane, ViewCabin comp) {
        // Gestione degli alieni nella cabina
        if (comp.alien) {
            String alienImagePath;
            if (comp.alienColor == AlienColor.PURPLE) {
                alienImagePath = "/images/icons/purple_alien.png";
            } else {
                alienImagePath = "/images/icons/brown_alien.png";
            }

            try {
                ImageView alienIcon = new ImageView(new Image(getClass().getResourceAsStream(alienImagePath)));
                alienIcon.setFitHeight(30);
                alienIcon.setFitWidth(30);
                alienIcon.setPreserveRatio(true);

                StackPane.setAlignment(alienIcon, javafx.geometry.Pos.TOP_LEFT);
                layeredPane.getChildren().add(alienIcon);
            } catch (Exception e) {
                System.err.println("Impossibile caricare l'immagine dell'alieno: " + e.getMessage());
            }
        } else if (comp.astronauts > 0) {
            // Aggiungi il numero di astronauti
            Label astronautsLabel = new Label(Integer.toString(comp.astronauts));

            try {
                ImageView astronautIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/icons/astr.png")));
                astronautIcon.setFitHeight(25);
                astronautIcon.setFitWidth(25);
                astronautIcon.setPreserveRatio(true);

                StackPane.setAlignment(astronautsLabel, javafx.geometry.Pos.TOP_LEFT);
                StackPane.setAlignment(astronautIcon, javafx.geometry.Pos.BOTTOM_LEFT);

                layeredPane.getChildren().addAll(astronautsLabel, astronautIcon);
            } catch (Exception e) {
                System.err.println("Impossibile caricare l'immagine dell'astronauta: " + e.getMessage());
                layeredPane.getChildren().add(astronautsLabel);
            }
        }

        if (comp.cabinColor != AlienColor.NONE) {
            String colorStr;
            switch (comp.cabinColor) {
                case PURPLE -> colorStr = "purple";
                case BROWN -> colorStr = "brown";
                case BOTH -> colorStr = "both";
                default -> colorStr = null;
            }

            if (colorStr != null) {
                Label colorIndicator = new Label("");
                colorIndicator.setStyle("-fx-background-color: " + colorStr + "; -fx-min-width: 10px; -fx-min-height: 10px; -fx-border-color: white;");
                StackPane.setAlignment(colorIndicator, javafx.geometry.Pos.BOTTOM_RIGHT);
                layeredPane.getChildren().add(colorIndicator);
            }
        }
    }

    // 3 Slot -> 60x90, 125x57, 125x124
    // 2 Slot -> 90x57, 90x124
    // Cargo 30px x 30px
    public void setComponentProp(StackPane layeredPane, ViewCargoHold comp) {
        return;
    }

    public void setComponentProp(StackPane layeredPane, ViewStartingCabin comp) {
        // Implementazione specifica per ViewStartingCabin, se necessario
        return;
    }

    public void setComponentProp(StackPane layeredPane, ViewSpecialCargoHold comp) {
        // Implementazione specifica per ViewSpecialCargoHold, se necessario
        return;
    }



    public boolean removeComponent(int row, int col) {
        if (row < 0 || row >= ROWS || col < 0 || col >= COLS) {
            return false;
        }

        String cellId = row + "_" + col;
        ImageView targetCell = getImageViewAt(row, col);

        if (targetCell == null) {
            return false;
        }

        targetCell.setImage(null);
        gridComponents.remove(cellId);
        return true;
    }

    private ImageView getImageViewAt(int row, int col) {
        return switch (row) {
            case 0 -> switch (col) {
                case 0 -> imageCell_0_0;
                case 1 -> imageCell_0_1;
                case 2 -> imageCell_0_2;
                case 3 -> imageCell_0_3;
                case 4 -> imageCell_0_4;
                default -> null;
            };
            case 1 -> switch (col) {
                case 0 -> imageCell_1_0;
                case 1 -> imageCell_1_1;
                case 2 -> imageCell_1_2;
                case 3 -> imageCell_1_3;
                case 4 -> imageCell_1_4;
                default -> null;
            };
            case 2 -> switch (col) {
                case 0 -> imageCell_2_0;
                case 1 -> imageCell_2_1;
                case 2 -> imageCell_2_2;
                case 3 -> imageCell_2_3;
                case 4 -> imageCell_2_4;
                default -> null;
            };
            case 3 -> switch (col) {
                case 0 -> imageCell_3_0;
                case 1 -> imageCell_3_1;
                case 2 -> imageCell_3_2;
                case 3 -> imageCell_3_3;
                case 4 -> imageCell_3_4;
                default -> null;
            };
            case 4 -> switch (col) {
                case 0 -> imageCell_4_0;
                case 1 -> imageCell_4_1;
                case 2 -> imageCell_4_2;
                case 3 -> imageCell_4_3;
                case 4 -> imageCell_4_4;
                default -> null;
            };
            default -> null;
        };
    }

    public void clearAllComponents() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                removeComponent(row, col);
            }
        }
        gridComponents.clear();
    }
}