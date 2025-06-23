package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import java.util.List;
import java.util.Map;

public class LeaderBoardMenuController {

    @FXML
    private Label titleLabel;

    @FXML
    private Label endGameLabel;

    @FXML
    private Label winnersLabel;

    @FXML
    private VBox scoreboardContainer;

    @FXML
    private Label disconnectionLabel;

    @FXML
    public void initialize() {
    }

    public void setScoreboard(Map<String, Integer> playerScores) {
        scoreboardContainer.getChildren().clear();

        playerScores.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(entry -> {
                    Label scoreLabel = new Label(String.format("%-20s: %d points",
                            entry.getKey(), entry.getValue()));
                    scoreLabel.setTextFill(javafx.scene.paint.Color.WHITE);
                    scoreLabel.setFont(Font.font("Monospaced", 16.0));
                    scoreboardContainer.getChildren().add(scoreLabel);
                });
    }

    public void initializeWithScores(Map<String, Integer> playerScores) {
        setScoreboard(playerScores);
    }
}