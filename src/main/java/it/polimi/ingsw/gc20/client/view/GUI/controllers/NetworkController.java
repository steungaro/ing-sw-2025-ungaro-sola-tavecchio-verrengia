package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.GUI.GUIView;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

/**
 * Controller class for the network configuration interface.
 * Handles the setup of connection parameters (IP address, port, and connection type)
 * and manages the connection attempt to the game server.
 */
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

    private GUIView guiView;

    /**
     * Initializes the controller after its root element has been completely processed.
     * Sets up toggle groups for connection type selection, configures default values,
     * and assigns action handlers to UI components.
     */
    @FXML
    public void initialize() {
        guiView = (GUIView) ClientGameModel.getInstance();

        ToggleGroup connectionTypeGroup = new ToggleGroup();
        socketRadioButton.setToggleGroup(connectionTypeGroup);
        rmiRadioButton.setToggleGroup(connectionTypeGroup);

        socketRadioButton.setSelected(true);

        connectButton.setOnAction(_ -> handleConnect());
    }

    private void handleConnect() {
        errorLabel.setVisible(false);
        String ipAddress = ipAddressField.getText();
        int port;

        try {
            port = Integer.parseInt(portField.getText());
        } catch (NumberFormatException e) {
            port = 0;
        }

        boolean isRMI = rmiRadioButton.isSelected();

        if (guiView.setupConnection(ipAddress, port, isRMI)) {
            guiView.showScene("login");
        } else {
            errorLabel.setVisible(true);
        }
    }
}