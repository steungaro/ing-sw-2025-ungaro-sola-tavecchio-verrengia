package it.polimi.ingsw.gc20.client.view.GUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class TokenPlaceholders extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    // Classe per rappresentare un segnaposto per pedina
    public static class TokenPlaceholder extends StackPane {
        private Circle background;
        private Label label;
        private boolean occupied = false;
        private Object token = null;

        public TokenPlaceholder(double radius) {
            this.background = new Circle(radius);
            this.label = new Label();

            setupPlaceholder();
        }

        private void setupPlaceholder() {
            // Cerchio di sfondo
            background.setFill(Color.LIGHTGRAY);
            background.setStroke(Color.DARKGRAY);
            background.setStrokeWidth(2);

            // Label per testo/numero
            label.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            label.setTextFill(Color.DARKBLUE);

            // Aggiungi elementi al container
            getChildren().addAll(background, label);

            // Effetti hover
            setOnMouseEntered(e -> {
                background.setFill(occupied ? Color.LIGHTBLUE : Color.LIGHTGREEN);
                setScaleX(1.1);
                setScaleY(1.1);
            });

            setOnMouseExited(e -> {
                background.setFill(occupied ? Color.LIGHTBLUE : Color.LIGHTGRAY);
                setScaleX(1.0);
                setScaleY(1.0);
            });
        }

        // Metodi per gestire l'occupazione del segnaposto
        public void setToken(Object token, String displayText) {
            this.token = token;
            this.occupied = true;
            this.label.setText(displayText);
            this.background.setFill(Color.LIGHTBLUE);
        }

        public void removeToken() {
            this.token = null;
            this.occupied = false;
            this.label.setText("");
            this.background.setFill(Color.LIGHTGRAY);
        }

        public boolean isOccupied() {
            return occupied;
        }

        public Object getToken() {
            return token;
        }

        public void setPlaceholderText(String text) {
            if (!occupied) {
                label.setText(text);
            }
        }
    }
    }