package it.polimi.ingsw.gc20.client.view.GUI;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import javafx.application.Application;
import javafx.stage.Stage;

public class GUIApplication extends Application {

    private GUIView guiView;

    @Override
    public void start(Stage primaryStage) {
        // Inizializza la GUIView
        guiView = new GUIView();
        ClientGameModel.setInstance(guiView);
        guiView.initGUI(primaryStage);

        primaryStage.setTitle("Galaxy Trucker");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}