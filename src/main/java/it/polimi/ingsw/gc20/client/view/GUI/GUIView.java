package it.polimi.ingsw.gc20.client.view.GUI;

import it.polimi.ingsw.gc20.client.view.TUI.MenuState;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.rmi.RemoteException;

public class GUIView extends ClientGameModel {

    private static Stage primaryStage;

    public GUIView() {
        super();
    }

    // Metodo per inizializzare l'interfaccia grafica
    public void initGUI(Stage stage) {
        primaryStage = stage;

        // Configurare la schermata iniziale (login o simile)
        setupLoginScene();
    }

    private void setupLoginScene() {
        // Implementazione della scena di login
    }

    // Sovrascrivi i metodi della View astratta per aggiornare la GUI
    @Override
    public void updateView(Message message) throws RemoteException {
        super.updateView(message);
        // Aggiornamento specifico della GUI
        Platform.runLater(() -> {
            // Aggiorna elementi JavaFX qui
        });
    }

    @Override
    public void display() {

    }

    @Override
    public void display(MenuState menuState) {

    }

    @Override
    public void shutdown() {

    }

    @Override
    public void notifyDisconnection() throws RemoteException {
        // Gestisci la disconnessione
    }

    public void login(String username, String server, int port) {
        // Implementa la logica di login
    }
}