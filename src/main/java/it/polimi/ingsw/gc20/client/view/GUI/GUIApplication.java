package it.polimi.ingsw.gc20.client.view.GUI;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.rmi.RemoteException;
import java.util.Objects;

public class GUIApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws RemoteException {
        GUIView guiView = new GUIView();
        ClientGameModel.setInstance(guiView);
        guiView.initGUI(primaryStage);
        primaryStage.setTitle("Galaxy Trucker");
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/fxml/icons/astr.png"))));
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(700);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}