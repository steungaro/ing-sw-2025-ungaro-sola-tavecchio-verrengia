package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EndGameMenuController {

    @FXML
    private TableView<ScoreEntry> scoreboardTable;

    @FXML
    private TableColumn<ScoreEntry, String> playerColumn;

    @FXML
    private TableColumn<ScoreEntry, Integer> scoreColumn;

    @FXML
    private Button exitButton;

    private Map<String, Integer> scoreboard;

    public void initialize() {
        playerColumn.setCellValueFactory(cellData -> cellData.getValue().playerNameProperty());
        scoreColumn.setCellValueFactory(cellData -> cellData.getValue().scoreProperty().asObject());
    }

    public void initializeWithScoreboard(Map<String, Integer> scoreboard) {
        this.scoreboard = scoreboard;

        List<ScoreEntry> entries = scoreboard.entrySet().stream()
                .map(entry -> new ScoreEntry(entry.getKey(), entry.getValue()))
                .sorted((e1, e2) -> Integer.compare(e2.getScore(), e1.getScore()))
                .collect(Collectors.toList());

        ObservableList<ScoreEntry> data = FXCollections.observableArrayList(entries);
        scoreboardTable.setItems(data);

        String username = ClientGameModel.getInstance().getUsername();
        scoreboardTable.setRowFactory(tv -> {
            javafx.scene.control.TableRow<ScoreEntry> row = new javafx.scene.control.TableRow<>();
            row.setStyle("-fx-background-color: transparent;");
            row.itemProperty().addListener((obs, oldItem, newItem) -> {
                if (newItem != null && newItem.getPlayerName().equals(username)) {
                    row.setStyle("-fx-background-color: #5a6890;");
                } else {
                    row.setStyle("-fx-background-color: transparent;");
                }
            });
            return row;
        });
    }

    @FXML
    private void handleExit() {
        ClientGameModel.getInstance().shutdown();
    }

    public static class ScoreEntry {
        private final SimpleStringProperty playerName;
        private final SimpleIntegerProperty score;

        public ScoreEntry(String playerName, Integer score) {
            this.playerName = new SimpleStringProperty(playerName);
            this.score = new SimpleIntegerProperty(score);
        }

        public String getPlayerName() {
            return playerName.get();
        }

        public SimpleStringProperty playerNameProperty() {
            return playerName;
        }

        public Integer getScore() {
            return score.get();
        }

        public SimpleIntegerProperty scoreProperty() {
            return score;
        }
    }
}