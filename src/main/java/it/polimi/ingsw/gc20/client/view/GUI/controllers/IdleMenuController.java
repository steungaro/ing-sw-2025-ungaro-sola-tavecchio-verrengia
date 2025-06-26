package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.util.Map;

public class IdleMenuController implements MenuController.ContextDataReceiver {
    @FXML
    private Label messageLabel;
    @FXML
    private Label dot1;
    @FXML
    private Label dot2;
    @FXML
    private Label dot3;
    @FXML
    private Label dot4;

    public void initialize() {
        startLoadingAnimation();
    }

    public void initializeWithMessage(String message) {
        messageLabel.setText(message);
    }

    @Override
    public void setContextData(Map<String, Object> contextData) {
        if (contextData.containsKey("message")) {
            String message = (String) contextData.get("message");
            initializeWithMessage(message);
        } else {
            throw new IllegalArgumentException("Context data must contain 'message'");
        }
    }

    private void startLoadingAnimation() {
        if (dot1 != null && dot2 != null && dot3 != null && dot4 != null) {
            FadeTransition fade1 = createFadeTransition(dot1, 0);     // Blue
            FadeTransition fade2 = createFadeTransition(dot2, 0.25);  // Green
            FadeTransition fade3 = createFadeTransition(dot3, 0.5);   // Red
            FadeTransition fade4 = createFadeTransition(dot4, 0.75);  // Orange

            fade1.play();
            fade2.play();
            fade3.play();
            fade4.play();
        }
    }

    private FadeTransition createFadeTransition(Label dot, double delay) {
        FadeTransition fade = new FadeTransition(Duration.seconds(1.2), dot);
        fade.setFromValue(0.3);
        fade.setToValue(1.0);
        fade.setCycleCount(Timeline.INDEFINITE);
        fade.setAutoReverse(true);
        fade.setDelay(Duration.seconds(delay));
        return fade;
    }
}