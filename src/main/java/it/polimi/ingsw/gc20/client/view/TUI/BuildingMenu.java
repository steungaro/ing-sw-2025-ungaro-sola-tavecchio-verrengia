package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards.ViewAdventureCard;
import org.javatuples.Pair;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class BuildingMenu implements MenuState{
    private final Scanner scanner = new Scanner(System.in);
    private final String username = ClientGameModel.getInstance().getUsername();
    private List<ViewAdventureCard> adventureCards;
    
    public BuildingMenu(List<ViewAdventureCard> adventureCards) {
        this.adventureCards = adventureCards;
    }

    public void displayMenu(){
        TUI.clearConsole();
        if(adventureCards!=null){
            ClientGameModel.getInstance().printCardsInLine(adventureCards);
        }

        if(ClientGameModel.getInstance().getComponentInHand() == null) {
            System.out.println("\u001B[1mBuilding Ship Menu\u001B[22m");
            System.out.println("1. Take component from covered components");
            System.out.println("2. Take component from uncovered components");
            System.out.println("3. Stop assembling ship");
            if(!ClientGameModel.getInstance().getShip(username).isLearner) {
                System.out.println("4. Take component from the booked components");
                System.out.println("5. Turn hourglass");
                System.out.println("6. Peek a deck of cards");
            }
            System.out.println("v. Viewing game options");
        } else {
            System.out.println("Building Ship Menu");
            System.out.println("1. Put the component in your hand back to the uncovered components");
            System.out.println("2. Add the component in your hand to your ship");
            System.out.println("3. Rotate the component in your hand Clockwise");
            System.out.println("4. Rotate the component in your hand Counter-Clockwise");
            if(!ClientGameModel.getInstance().getShip(username).isLearner) {
                System.out.println("5. Add the component in your hand to booked components");
                System.out.println("6. Turn hourglass");
            }
            System.out.println("v. Viewing game options");
        }
    }

    /**
     * Handles user input for the current menu
     * @return true if the menu should continue, false if it should exit
     */
    public boolean handleInput() throws IOException {
        System.out.print(" > ");
        String choice = scanner.nextLine().trim();
        adventureCards = null;
        // Handle user input for the building menu
        if(ClientGameModel.getInstance().getComponentInHand() == null) {
            switch (choice) {
                case "1":
                    System.out.println("Type the index of the covered component you want to take:");
                    System.out.print(" > ");
                    // Read the index of the component to take
                    int index = Integer.parseInt(scanner.nextLine().trim());
                    ClientGameModel.getInstance().getClient().takeComponentFromUnviewed(username, index);
                    break;
                case "2":
                    System.out.println("Type the index of the uncovered component you want to take:");
                    System.out.print(" > ");
                    // Read the index of the component to take
                    int index1 = Integer.parseInt(scanner.nextLine().trim());
                    ClientGameModel.getInstance().getClient().takeComponentFromViewed(username, index1);
                    break;
                case "3":
                    System.out.println("Type the index of the board where you want to start the game:");
                    System.out.print(" > ");
                    // Read the index of the board to take
                    int index3 = Integer.parseInt(scanner.nextLine().trim());
                    ClientGameModel.getInstance().getClient().stopAssembling(username, index3);
                    break;
                case "4":
                    System.out.println("Type the index of the booked component you want to take:");
                    System.out.print(" > ");
                    // Read the index of the component to take
                    int index2 = Integer.parseInt(scanner.nextLine().trim());
                    ClientGameModel.getInstance().getClient().takeComponentFromBooked(username, index2);
                    break;
                case "5":
                    ClientGameModel.getInstance().getClient().turnHourglass(username);
                    break;
                case "6":
                    System.out.println("Type the index of the deck you want to peek:");
                    System.out.print(" > ");
                    // Read the index of the deck to peek
                    int index4 = Integer.parseInt(scanner.nextLine().trim());
                    ClientGameModel.getInstance().getClient().peekDeck(username, index4);
                    break;
                case "q":
                    ClientGameModel.getInstance().shutdown();
                    break;
                case "v":
                    TUI.viewOptionsMenu();
                    return false;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    return false;
            }
        }else{
            switch (choice){
                case "5":
                    ClientGameModel.getInstance().getClient().addComponentToBooked(username);
                    break;
                case "1":
                    ClientGameModel.getInstance().getClient().addComponentToViewed(username);
                    break;
                case "2":
                    System.out.println("Type the coordinates of the component you want to add (x y):");
                    System.out.print(" > ");
                    // Read the coordinates of the component to add
                    String coordinates = scanner.nextLine().trim();
                    String[] parts = coordinates.split(" ");
                    int x = Integer.parseInt(parts[0]) - 5;
                    int y = Integer.parseInt(parts[1]) - 4;
                    Pair<Integer, Integer> coordinatesPair = new Pair<>(x, y);
                    ClientGameModel.getInstance().getClient().placeComponent(username, coordinatesPair);
                    break;
                case "3":
                    System.out.println("Type the number of rotations:");
                    System.out.print(" > ");
                    // Read the number of rotations
                    int numRotations = Integer.parseInt(scanner.nextLine().trim());
                    // Rotate the component in hand
                    for(int i = 0; i < numRotations; i++){
                        ClientGameModel.getInstance().getClient().rotateComponentClockwise(username);
                    }
                    break;
                case "4":
                    System.out.println("Type the number of rotations:");
                    System.out.print(" > ");
                    // Read the number of rotations
                    int numRotationsCCW = Integer.parseInt(scanner.nextLine().trim());
                    // Rotate the component in hand
                    for(int i = 0; i < numRotationsCCW; i++){
                        ClientGameModel.getInstance().getClient().rotateComponentCounterclockwise(username);
                    }
                    break;
                case "6":
                    ClientGameModel.getInstance().getClient().turnHourglass(username);
                    break;
                case "q":
                    ClientGameModel.getInstance().shutdown();
                    break;
                case "v":
                    TUI.viewOptionsMenu();
                    return false;
                default:
                    System.out.println("Invalid choice. Please try again.");
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
