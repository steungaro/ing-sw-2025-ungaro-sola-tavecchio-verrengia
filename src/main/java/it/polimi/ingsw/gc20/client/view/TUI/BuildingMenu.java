package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import org.javatuples.Pair;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;

import java.io.IOException;

public class BuildingMenu implements MenuState{
    private final Terminal terminal;
    private final LineReader lineReader;
    private final String username = ClientGameModel.getInstance().getUsername();
    
    public BuildingMenu(Terminal terminal) {
        this.terminal = terminal;
        this.lineReader = LineReaderBuilder.builder().terminal(terminal).build();
    }

    public void displayMenu(){
        TUI.clearConsole(terminal);
        if(ClientGameModel.getInstance().getComponentInHand() == null) {
            terminal.writer().println("Building Ship Menu");
            terminal.writer().println("1. Take component from covered components");
            terminal.writer().println("2. Take component from uncovered components");
            terminal.writer().println("3. Stop assembling ship");
            if(!ClientGameModel.getInstance().getShip(username).isLearner) {
                terminal.writer().println("4. Take component from the booked components");
                terminal.writer().println("5. Turn hourglass");
                terminal.writer().println("6. Peek a deck of cards with argument");
            }
        } else {
            terminal.writer().println("Building Ship Menu");
            terminal.writer().println("1. Put the component in your hand back to the uncovered components");
            terminal.writer().println("2. Add the component in your hand to your ship");
            terminal.writer().println("3. Rotate the component in your hand Clockwise");
            terminal.writer().println("4. Rotate the component in your hand Counter-Clockwise");
            if(!ClientGameModel.getInstance().getShip(username).isLearner) {
                terminal.writer().println("5. Add the component in your hand to booked components");
                terminal.writer().println("6. Turn hourglass");
            }
        }
    }

    /**
     * Handles user input for the current menu
     * @return true if the menu should continue, false if it should exit
     */
    public boolean handleInput() throws IOException {
        // Hide cursor
        TUI.hideCursor(terminal);
        int choice = terminal.reader().read();
        // Show cursor
        TUI.showCursor(terminal);
        // Handle user input for the building menu
        if(ClientGameModel.getInstance().getComponentInHand() == null) {
            switch (choice) {
                case 1:
                    terminal.writer().println("Type the index of the covered component you want to take:");
                    terminal.writer().print(" > ");
                    // Read the index of the component to take
                    int index = Integer.parseInt(lineReader.readLine().trim());
                    ClientGameModel.getInstance().getClient().takeComponentFromUnviewed(username, index);
                    break;
                case 2:
                    terminal.writer().println("Type the index of the uncovered component you want to take:");
                    terminal.writer().print(" > ");
                    // Read the index of the component to take
                    int index1 = Integer.parseInt(lineReader.readLine().trim());
                    ClientGameModel.getInstance().getClient().takeComponentFromViewed(username, index1);
                    break;
                case 3:
                    terminal.writer().println("Type the index of the board where you want to start the game:");
                    terminal.writer().print(" > ");
                    // Read the index of the board to take
                    int index3 = Integer.parseInt(lineReader.readLine().trim());
                    ClientGameModel.getInstance().getClient().stopAssembling(username, index3);
                    break;
                case 4:
                    terminal.writer().println("Type the index of the booked component you want to take:");
                    terminal.writer().print(" > ");
                    // Read the index of the component to take
                    int index2 = Integer.parseInt(lineReader.readLine().trim());
                    ClientGameModel.getInstance().getClient().takeComponentFromBooked(username, index2);
                    break;
                case 5:
                    ClientGameModel.getInstance().getClient().turnHourglass(username);
                    break;
                case 6:
                    terminal.writer().println("Type the index of the deck you want to peek:");
                    terminal.writer().print(" > ");
                    // Read the index of the deck to peek
                    int index4 = Integer.parseInt(lineReader.readLine().trim());
                    ClientGameModel.getInstance().getClient().peekDeck(username, index4);
                    break;
                case 'q':
                    ClientGameModel.getInstance().shutdown();
                    break;
                default:
                    terminal.writer().println("Invalid choice. Please try again.");
                    return false;
            }
        }else{
            switch (choice){
                case 5:
                    ClientGameModel.getInstance().getClient().addComponentToBooked(username);
                    break;
                case 1:
                    ClientGameModel.getInstance().getClient().addComponentToViewed(username);
                    break;
                case 2:
                    terminal.writer().println("Type the coordinates of the component you want to add (x y):");
                    terminal.writer().print(" > ");
                    // Read the coordinates of the component to add
                    String coordinates = lineReader.readLine().trim();
                    String[] parts = coordinates.split(" ");
                    int x = Integer.parseInt(parts[0]) - 5;
                    int y = Integer.parseInt(parts[1]) - 4;
                    Pair<Integer, Integer> coordinatesPair = new Pair<>(x, y);
                    ClientGameModel.getInstance().getClient().placeComponent(username, coordinatesPair);
                    break;
                case 3:
                    terminal.writer().println("Type the number of rotations:");
                    terminal.writer().print(" > ");
                    // Read the number of rotations
                    int numRotations = Integer.parseInt(lineReader.readLine().trim());
                    // Rotate the component in hand
                    for(int i = 0; i < numRotations; i++){
                        ClientGameModel.getInstance().getClient().rotateComponentClockwise(username);
                    }
                    break;
                case 4:
                    terminal.writer().println("Type the number of rotations:");
                    terminal.writer().print(" > ");
                    // Read the number of rotations
                    int numRotationsCCW = Integer.parseInt(lineReader.readLine().trim());
                    // Rotate the component in hand
                    for(int i = 0; i < numRotationsCCW; i++){
                        ClientGameModel.getInstance().getClient().rotateComponentCounterclockwise(username);
                    }
                    break;
                case 6:
                    ClientGameModel.getInstance().getClient().turnHourglass(username);
                    break;
                default:
                    terminal.writer().println("Invalid choice. Please try again.");
                    return false;
            }
        }
        return true;
    }

    /**
     * Get the name of the current state
     * @return State name
     */
    public String getStateName(){
        return "Building Ship Menu";
    }
}
