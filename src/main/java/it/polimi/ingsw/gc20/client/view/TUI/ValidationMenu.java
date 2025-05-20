package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import org.javatuples.Pair;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;

import java.io.IOException;
import java.rmi.RemoteException;

public class ValidationMenu implements MenuState{
    public final Terminal terminal;
    public String username = ClientGameModel.getInstance().getUsername();
    public final LineReader lineReader;


    public ValidationMenu(){
        this.terminal = null;
        this.lineReader = LineReaderBuilder.builder().terminal(terminal).build();
    }

    public void displayMenu(){
        TUI.clearConsole();
        terminal.writer().println("Validation Menu");
        if(ClientGameModel.getInstance().getShip(username).isValid()){
            System.out.println("Ship is already valid! Wait for other players before going to the next phase.");
        } else {
            System.out.println("Ship is not valid");
            System.out.println("1. Validate ship");
            System.out.println("2. Remove a component from the ship with the arguments");
        }
    }

    /**
     * Handles user input for the current menu
     * @return true if the menu should continue, false if it should exit
     */
    public boolean handleInput() throws IOException {
        // Check if the ship is valid
        if(ClientGameModel.getInstance().getShip(username).isValid()){
            return true;
        }
        int choice = terminal.reader().read();
        // Handle user input for the validation menu
        switch (choice) {
            case 1:
                // Validate ship
                ClientGameModel.getInstance().getClient().validateShip(ClientGameModel.getInstance().getUsername());
                break;
            case 2:
                // Remove a component from the ship
                terminal.writer().println("Type the coordinates of the component you want to remove (x y):");
                terminal.writer().print(" > ");
                String componentName = lineReader.readLine().trim();
                int x = Integer.parseInt(componentName.split(" ")[0]) - 5;
                int y = Integer.parseInt(componentName.split(" ")[1]) - 4;
                Pair<Integer, Integer> coordinates = new Pair<>(x, y);
                ClientGameModel.getInstance().getClient().removeComponentFromShip(ClientGameModel.getInstance().getUsername(), coordinates);
                break;
            case 'q':
                ClientGameModel.getInstance().shutdown();
                break;
            default:
                terminal.writer().println("Invalid choice. Please try again.");
                return false;
        }
        return true;
    }

    /**
     * Get the name of the current state
     * @return State name
     */
    public String getStateName(){
        return "Validation Menu";
    }
}
