package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.rmi.RemoteException;
import java.util.Map;

/**
 * Controller class for the dice rolling menu interface.
 * Handles user interactions for rolling dice in the game and displays related messages.
 * Implements ContextDataReceiver to receive contextual data from parent controllers.
 */
public class RollDiceMenuController implements MenuController.ContextDataReceiver {

    @FXML
    private Label messageLabel;

    @FXML
    private Label errorLabel;

    private String username;

    /**
     * Initializes the controller after its root element has been completely processed.
     * Retrieves the current username from the client game model.
     */
    public void initialize() {
        username = ClientGameModel.getInstance().getUsername();
    }

    /**
     * Initializes the controller with a specific message to display to the user.
     * Sets the text of the message label to the provided message.
     *
     * @param message The message to display in the interface
     */
    public void initializeWithMessage(String message) {
        messageLabel.setText(message);
    }

    /**
     * Handles the roll dice action triggered by the user.
     * Sends a request to the server to roll dice for the current player.
     * Displays an error message if the connection fails.
     */
    @FXML
    private void handleRollDice() {
        try {
            ClientGameModel.getInstance().getClient().rollDice(username);
        } catch (RemoteException e) {
            showError("Connection error: " + e.getMessage());
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    /**
     * Sets contextual data received from a parent controller.
     * Extracts a message from the context data and initializes the controller with it.
     * 
     * @param contextData A map containing contextual data, must include a "message" key
     * @throws IllegalArgumentException if the required message is not present in the context data
     */
    @Override
    public void setContextData(Map<String, Object> contextData) {
        if (contextData.containsKey("message")) {
            String message = (String) contextData.get("message");
            initializeWithMessage(message);
        } else {
            throw new IllegalArgumentException("Context data must contain a message");
        }
    }
}