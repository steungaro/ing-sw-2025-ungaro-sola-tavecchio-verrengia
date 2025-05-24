package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.View;
import it.polimi.ingsw.gc20.client.view.GUI.GUIView;
import javafx.fxml.FXML;
import it.polimi.ingsw.gc20.client.network.common.Client;
import it.polimi.ingsw.gc20.client.view.common.View;
import it.polimi.ingsw.gc20.client.view.GUI.GUIView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class NetworkController {

    @FXML
    private RadioButton socketRadioButton;

    @FXML
    private RadioButton rmiRadioButton;

    @FXML
    private TextField ipAddressField;

    @FXML
    private TextField portField;

    @FXML
    private Button connectButton;

    @FXML
    private Label errorLabel;

    private ToggleGroup connectionTypeGroup;

    private GUIView guiView;

    private Client client;

    @FXML
    public void initialize() {
        guiView = (GUIView) View.getInstance();

        connectionTypeGroup = new ToggleGroup();
        socketRadioButton.setToggleGroup(connectionTypeGroup);
        rmiRadioButton.setToggleGroup(connectionTypeGroup);

        socketRadioButton.setSelected(true);

        ipAddressField.setText("localhost");
        portField.setText("502");

        connectButton.setOnAction(event -> handleConnect());
    }

    private void handleConnect() {
        String ipAddress = ipAddressField.getText();
        int port;

        try {
            port = Integer.parseInt(portField.getText());
        } catch (NumberFormatException e) {
            System.out.println("Port number must be an integer");
            return;
        }

        boolean isRMI = rmiRadioButton.isSelected();

        // Chiama il metodo nella GUIView per stabilire la connessione
        guiView.setupConnection(ipAddress, port, isRMI);
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}