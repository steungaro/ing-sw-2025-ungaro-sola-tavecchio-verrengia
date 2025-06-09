package it.polimi.ingsw.gc20.client.view.GUI;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TestGUIApplication extends Application {

    private GUIView guiView;
    private Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;
        this.guiView = new GUIView();

        // Imposta il titolo della finestra
        primaryStage.setTitle("Test Galaxy Trucker GUI");

        // Crea la schermata di test
        showTestMenu();

        // Mostra la finestra
        primaryStage.show();
    }

    private void showTestMenu() {
        // Creazione del layout principale
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        // Titolo
        Label titleLabel = new Label("Test delle Schermate");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Pulsanti per ogni schermata
        Button welcomeButton = createScreenButton("Welcome", "welcome");
        Button loginButton = createScreenButton("Login", "login");
        Button mainMenuButton = createScreenButton("Menu Principale", "mainMenu");
        Button inLobbyButton = createScreenButton("Lobby", "inLobby");
        Button gameButton = createScreenButton("Partita", "game");
        Button shipButton = createScreenButton("Nave", "ship0");

        // Aggiungi componenti al layout
        root.getChildren().addAll(
                titleLabel,
                welcomeButton,
                loginButton,
                mainMenuButton,
                inLobbyButton,
                gameButton,
                shipButton
        );

        // Creazione della scena
        Scene scene = new Scene(root, 400, 500);
        primaryStage.setScene(scene);
    }

    private Button createScreenButton(String label, String screenName) {
        Button button = new Button(label);
        button.setMaxWidth(200);
        button.setPrefWidth(200);
        button.setOnAction(e -> {
            guiView.initGUI(primaryStage);
            guiView.showScene(screenName);
        });
        return button;
    }

    /**
     * Metodo per tornare al menu di test
     */
    private Button createBackButton() {
        Button backButton = new Button("Torna al menu di test");
        backButton.setOnAction(e -> showTestMenu());
        return backButton;
    }

    public static void main(String[] args) {
        launch(args);
    }
}