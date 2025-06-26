package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import java.util.Map;

public class LeaderBoardMenuController implements MenuController.ContextDataReceiver, BindCleanUp{

    private Map<String, Integer> leaderBoard;

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

    @SuppressWarnings( "unchecked")
    @Override
    public void setContextData(Map<String, Object> contextData) {
        if (contextData.containsKey("leaderBoard")) {
            try {
                leaderBoard = (Map<String, Integer>) contextData.get("leaderBoard");
            }
            catch (ClassCastException e) {
                throw new IllegalArgumentException("Invalid type for 'leaderBoard' in context data", e);
            }
            initializeWithScores(leaderBoard);
        } else {
            throw new IllegalArgumentException("Context data must contain 'leaderBoard'");
        }
    }

    public void cleanup() {
        System.out.println("LeaderBoardMenuController: Starting cleanup...");

        if (scoreboardContainer != null) {
            for (javafx.scene.Node node : scoreboardContainer.getChildren()) {
                try {
                    Label label = (Label) node;
                    label.setText("");
                    label.setTextFill(null);
                    label.setFont(null);
                } catch (Exception e) {
                    System.err.println("Error cleaning up score label: " + e.getMessage());
                }
            }
            scoreboardContainer.getChildren().clear();
        }

        if (titleLabel != null) {
            titleLabel.setText("");
        }

        if (endGameLabel != null) {
            endGameLabel.setText("");
        }

        if (winnersLabel != null) {
            winnersLabel.setText("");
        }

        if (disconnectionLabel != null) {
            disconnectionLabel.setText("");
        }

        if (leaderBoard != null) {
            leaderBoard.clear();
            leaderBoard = null;
        }

        titleLabel = null;
        endGameLabel = null;
        winnersLabel = null;
        scoreboardContainer = null;
        disconnectionLabel = null;

        System.out.println("LeaderBoardMenuController: Cleanup completed");
    }
}