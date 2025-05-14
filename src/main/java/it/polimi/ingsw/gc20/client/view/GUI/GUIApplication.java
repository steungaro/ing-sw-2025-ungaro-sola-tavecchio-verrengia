package it.polimi.ingsw.gc20.client.view.GUI;

import it.polimi.ingsw.gc20.client.view.common.View;
import javafx.application.Application;
import javafx.stage.Stage;

public class GUIApplication extends Application {

    private GUIView guiView;

    @Override
    public void start(Stage primaryStage) {
        // Inizializza la GUIView
        guiView = new GUIView();
        // Imposta come istanza singleton di View
        View.setInstance(guiView);
        // Inizializza l'interfaccia grafica
        guiView.initGUI(primaryStage);

        primaryStage.setTitle("Game Client");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}