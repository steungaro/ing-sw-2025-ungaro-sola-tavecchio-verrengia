package it.polimi.ingsw.gc20.client.view.GUI.viewer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static javafx.application.Application.launch;

public class Board0Viewer extends Application {
        @Override
        public void start(Stage primaryStage) throws Exception {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/board0.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 800, 800);

            primaryStage.setTitle("Board Viewer");
            primaryStage.setScene(scene);
            primaryStage.show();
        }

        public static void main(String[] args) {
            launch(args);
        }
}

