package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.javatuples.Pair;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class BranchMenuController {

    @FXML
    private StackPane shipContainer;

    @FXML
    private Button confirmButton;

    private String shipType;
    private GridPane shipGrid;
    private Pair<Integer, Integer> selectedCoordinates;
    private Map<Pair<Integer, Integer>, Rectangle> branchHighlights = new HashMap<>();

    public void initializeWithShip(String shipType) {
        this.shipType = shipType;
        loadShipView();
        setupBranchSelection();
    }

    private void loadShipView() {
        try {
            URL shipUrl = getClass().getResource("/fxml/" + shipType + ".fxml");
            if (shipUrl == null) {
                System.err.println("File nave non rotate: " + shipType);
                return;
            }

            FXMLLoader shipLoader = new FXMLLoader(shipUrl);
            Node shipNode = shipLoader.load();

            findShipGrid(shipNode);

            shipContainer.getChildren().add(shipNode);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void findShipGrid(Node node) {
        if (node instanceof GridPane) {
            shipGrid = (GridPane) node;
        } else if (node instanceof javafx.scene.Parent) {
            for (Node child : ((javafx.scene.Parent) node).getChildrenUnmodifiable()) {
                findShipGrid(child);
                if (shipGrid != null) break;
            }
        }
    }

    private void setupBranchSelection() {
        if (shipGrid == null) return;

        addBranchHighlight(1, 2);
        addBranchHighlight(3, 2);
    }

    private void addBranchHighlight(int row, int col) {
        Rectangle highlight = new Rectangle(40, 40);
        highlight.setFill(Color.TRANSPARENT);
        highlight.setStroke(Color.YELLOW);
        highlight.setStrokeWidth(3);
        highlight.setOpacity(0.8);

        Pair<Integer, Integer> coords = new Pair<>(row, col);
        branchHighlights.put(coords, highlight);

        highlight.setOnMouseClicked(event -> selectBranch(coords, event));

        if (shipGrid != null) {
            shipGrid.add(highlight, col, row);
        }
    }

    private void selectBranch(Pair<Integer, Integer> coords, MouseEvent event) {
        branchHighlights.values().forEach(r -> r.setFill(Color.TRANSPARENT));

        Rectangle selected = branchHighlights.get(coords);
        selected.setFill(Color.YELLOW.deriveColor(1, 1, 1, 0.3));

        selectedCoordinates = coords;
        confirmButton.setDisable(false);
    }

    @FXML
    private void onConfirmSelection() {
        if (selectedCoordinates != null) {
            int row = selectedCoordinates.getValue0();
            int col = selectedCoordinates.getValue1();

            boolean isLearner = ClientGameModel.getInstance()
                    .getShip(ClientGameModel.getInstance().getUsername()).isLearner;
            int colOffset = isLearner ? 5 : 4;

            Pair<Integer, Integer> gameCoords = new Pair<>(row, col - colOffset);

            try {
                ClientGameModel.getInstance().getClient()
                        .chooseBranch(ClientGameModel.getInstance().getUsername(), gameCoords);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}