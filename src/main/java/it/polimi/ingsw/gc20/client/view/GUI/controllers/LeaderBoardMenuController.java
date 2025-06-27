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

    /**
     * Initializes the leaderboard menu controller.
     * This method is called automatically by JavaFX after loading the FXML file.
     * Currently performs no specific initialization actions but is required for FXML compliance.
     */
    @FXML
    public void initialize() {
    }

    /**
     * Sets and displays the scoreboard with player scores.
     * This method clears the existing scoreboard container and populates it with player scores
     * sorted in descending order (highest score first). Each score entry is displayed as a
     * formatted label with the player name and their points.
     * 
     * @param playerScores a map containing player names as keys and their scores as values
     */
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

    /**
     * Initializes the leaderboard with player scores.
     * This method serves as a convenient wrapper around setScoreboard() to initialize
     * the leaderboard display with the provided player scores.
     * 
     * @param playerScores a map containing player names as keys and their scores as values
     */
    public void initializeWithScores(Map<String, Integer> playerScores) {
        setScoreboard(playerScores);
    }

    /**
     * Sets the context data for the leaderboard, specifically the player scores.
     * This method processes the context data map to extract the leaderboard information
     * and initialize the scoreboard display. The context data must contain a "leaderBoard" key
     * with a Map&lt;String, Integer&gt; value representing player names and scores.
     * 
     * @param contextData a map containing the context data, must include a "leaderBoard" key 
     *                   with a Map&lt;String, Integer&gt; value
     * @throws IllegalArgumentException if the context data does not contain the required "leaderBoard" key
     *                                or if the leaderBoard value is not of the expected type
     */
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

    /**
     * Performs cleanup operations to free resources and reset the controller state.
     * This method clears all UI components and resets their properties to prevent memory leaks
     * and ensure proper cleanup when the leaderboard menu is no longer needed. It performs:
     * - Clears and resets all labels in the scoreboard container
     * - Resets all title and message labels
     * - Clears the leaderboard data map
     * - Sets all component references to null
     */
    public void cleanup() {

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
    }
}