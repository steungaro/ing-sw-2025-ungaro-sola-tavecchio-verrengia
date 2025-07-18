package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.client.view.common.ViewLobby;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.GameModelListener;
import it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards.ViewAdventureCard;
import it.polimi.ingsw.gc20.client.view.common.localmodel.board.ViewBoard;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
import org.javatuples.Pair;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Scanner;

/**
 * The BuildingMenu class represents the menu state where players can
 * assemble their ship by interacting with various game components.
 * It provides functionality for displaying options, handling user inputs,
 * and executing actions like adding components to the ship, rotating components,
 * and interacting with various decks and piles in the game.
 * <p>
 * This class implements the {@link MenuState} interface, which defines the methods.
 */
public class BuildingMenu implements MenuState, GameModelListener {
    private final Scanner scanner = new Scanner(System.in);
    private final String username = ClientGameModel.getInstance().getUsername();
    private List<ViewAdventureCard> adventureCards;
    
    public BuildingMenu(List<ViewAdventureCard> adventureCards) {
        this.adventureCards = adventureCards;
    }

    /**
     * Displays the current state of the building ship menu to the user.
     * The display includes the available actions based on the player's current
     * game state, including components in hand, uncovered components, covered components,
     * and other menu options related to building the ship.
     * <p>
     * The menu dynamically adjusts its options depending on whether the player
     * has a component in their hand or not and whether the player's ship is in learner mode.
     * <p>
     * Prints game-related details to the console, including the list of cards,
     * components, ship status, and the options' menu for player selection. At the
     * end of the menu display, an input prompt is provided.
     */
    @Override
    public void displayMenu(){
        ClientGameModel.getInstance().addListener(this);
        if(adventureCards!=null){
            ClientGameModel.getInstance().printCardsInLine(adventureCards);
        }

        if(ClientGameModel.getInstance().getComponentInHand() == null) {
            System.out.println("Uncovered components:");
            ClientGameModel.getInstance().printViewedPile();
            System.out.println("Covered components: " + ClientGameModel.getInstance().getBoard().unviewedPile);
            ClientGameModel.getInstance().printShip(username);
            System.out.println("\u001B[1mBuilding Ship Menu\u001B[22m");
            System.out.println("1. Take component from covered components");
            System.out.println("2. Take component from uncovered components");
            System.out.println("3. Stop assembling ship");
            if(!ClientGameModel.getInstance().getShip(username).isLearner) {
                System.out.println("4. Take component from the booked components");
                System.out.println("5. Turn hourglass");
                System.out.println("6. Peek a deck of cards");
            }
        } else {
            System.out.println("Component in hand:");
            System.out.println("Ship:");
            ClientGameModel.getInstance().printShip(username);
            System.out.println("Component in hand: ");
            System.out.println(ClientGameModel.getInstance().getComponentInHand());
            System.out.println("Building Ship Menu");
            System.out.println("1. Put the component in your hand back to the uncovered components");
            System.out.println("2. Add the component in your hand to your ship");
            System.out.println("3. Rotate the component in your hand Clockwise");
            System.out.println("4. Rotate the component in your hand Counter-Clockwise");
            if(!ClientGameModel.getInstance().getShip(username).isLearner) {
                System.out.println("5. Add the component in your hand to booked components");
                System.out.println("6. Turn hourglass");
            }
        }
        System.out.println("v. Viewing game options");
        System.out.print(" > ");
    }

    /**
     * Displays the current menu to the player with an error message.
     * This method is used to inform the player about any errors or invalid inputs
     * while still providing the menu options.
     *
     * @param errorMessage The error message to display to the player
     * @see #displayMenu()
     */
    @Override
    public void displayMenu(String errorMessage) {
        System.out.println("\u001B[31m" + errorMessage + "\u001B[0m");
        displayMenu();
    }

    /**
     * Handles user input for the building ship menu. This method processes the player's
     * choice and executes the corresponding action, such as taking components, placing
     * components, rotating components, or interacting with the game state.
     * <p>
     * The method also manages the transition between different states of the game based on
     * user input and updates the game model accordingly.
     *
     * @param choice The player's input choice from the menu
     * @throws RemoteException If a remote method invocation error occurs during communication with the game client
     */
    @Override
    public void handleInput(String choice) throws RemoteException {
        adventureCards = null;
        ClientGameModel.getInstance().removeListener(this);
        ClientGameModel.getInstance().setBusy();
        // Handle user input from the building menu
        if(ClientGameModel.getInstance().getComponentInHand() == null) {
            switch (choice) {
                case "1":
                        int index;
                    do {
                        System.out.println("Type the index of the covered component you want to take (0 to " + (ClientGameModel.getInstance().getBoard().unviewedPile - 1) + ") or [b] to go back:");
                        System.out.print(" > ");
                        // Read the index of the component to take
                        // Check if the index is valid
                        String input = scanner.nextLine().trim();
                        if (input.equals("b")) {
                            TUI.clearConsole();
                            displayMenu();
                            ClientGameModel.getInstance().setFree();
                            return;
                        }
                        try {
                            index = Integer.parseInt(input);
                        } catch (NumberFormatException e) {
                            index = -1;
                            System.out.println("\u001B[31mInvalid input. Please enter a valid index.\u001B[0m");
                        }
                    } while (index < 0 || index >= ClientGameModel.getInstance().getBoard().unviewedPile);
                    ClientGameModel.getInstance().getClient().takeComponentFromUnviewed(username, index);
                    break;
                case "2":
                        int index1;
                    do {
                        System.out.println("Type the index of the uncovered component you want to take (0 to " + (ClientGameModel.getInstance().getBoard().viewedPile.size() - 1) + ") or [b] to go back:");
                        System.out.print(" > ");
                        // Read the index of the component to take
                        try {
                            String input = scanner.nextLine().trim();
                            if (input.equals("b")) {
                                TUI.clearConsole();
                                displayMenu();
                                ClientGameModel.getInstance().setFree();
                                return;
                            }
                            index1 = Integer.parseInt(input);
                        } catch (NumberFormatException e) {
                            System.out.println("\u001B[31mInvalid input. Please enter a valid index.\u001B[0m");
                            index1 = -1;
                        }
                    } while (index1 < 0 || index1 >= ClientGameModel.getInstance().getBoard().viewedPile.size());
                    ClientGameModel.getInstance().getClient().takeComponentFromViewed(username, index1);
                    break;
                case "3":
                    int index3;
                    do {
                        ClientGameModel.getInstance().printBoard();
                        System.out.println("Type the index of the board where you want to start the game (1 to 4) or [b] to go back:");
                        System.out.print(" > ");
                        // Read the index of the board to take
                        try {
                            String input = scanner.nextLine().trim();
                            if (input.equals("b")) {
                                TUI.clearConsole();
                                displayMenu();
                                ClientGameModel.getInstance().setFree();
                                return;
                            }
                            index3 = Integer.parseInt(input);
                        } catch (NumberFormatException e) {
                            index3 = -1;
                            System.out.println("\u001B[31mInvalid input. Please enter a valid index.\u001B[0m");
                        }
                    } while (index3 < 0 || index3 > 4);
                    ClientGameModel.getInstance().getClient().stopAssembling(username, index3);
                    break;
                case "4":
                    int index2;
                    do {
                        System.out.println("Type the index of the booked component you want to take (0 to 1) or [b] to go back:");
                        System.out.print(" > ");
                        // Read the index of the component to take
                        try {
                            String input = scanner.nextLine().trim();
                            if (input.equals("b")) {
                                TUI.clearConsole();
                                displayMenu();
                                ClientGameModel.getInstance().setFree();
                                return;
                            }
                            index2 = Integer.parseInt(input);
                        } catch (NumberFormatException e) {
                            index2 = -1;
                            System.out.println("\u001B[31mInvalid input. Please enter a valid index.\u001B[0m");
                        }
                    } while (index2 < 0 || index2 > 1);
                    ClientGameModel.getInstance().getClient().takeComponentFromBooked(username, index2);
                    break;
                case "5":
                    ClientGameModel.getInstance().getClient().turnHourglass(username);
                    break;
                case "6":
                    int index4;
                    do {
                        System.out.println("Type the index of the deck you want to peek (1 to 3) or [b] to go back:");
                        System.out.print(" > ");
                        // Read the index of the deck to peek
                        try {
                            String input = scanner.nextLine().trim();
                            if (input.equals("b")) {
                                TUI.clearConsole();
                                displayMenu();
                                ClientGameModel.getInstance().setFree();
                                return;
                            }
                            index4 = Integer.parseInt(input);
                        } catch (NumberFormatException e) {
                            index4 = -1;
                            System.out.println("\u001B[31mInvalid input. Please enter a valid index.\u001B[0m");
                        }
                    } while (index4 < 1 || index4 > 3);
                    ClientGameModel.getInstance().getClient().peekDeck(username, index4);
                    break;
                case "q":
                    ClientGameModel.getInstance().shutdown();
                    break;
                case "v":
                    TUI.viewOptionsMenu();
                    break;
                default:
                    System.out.println("\u001B[31mInvalid choice. Please try again.\u001B[0m");
                    System.out.print(" > ");
                    break;
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
                    int row = 0;
                    int col = 0;
                    do {
                        System.out.println("Type the coordinates of where you want to place your component (row col) or [b] to go back:");
                        System.out.print(" > ");
                        // Read the coordinates of the component to add
                        String coordinates = scanner.nextLine().trim();
                        if (coordinates.equals("b")) {
                            TUI.clearConsole();
                            displayMenu();
                            ClientGameModel.getInstance().setFree();
                            return;
                        }
                        if (coordinates.equals("q")) {
                            ClientGameModel.getInstance().shutdown();
                            return;
                        }
                        String[] parts = coordinates.split(" ");
                        try {
                            row = Integer.parseInt(parts[0]) - 5;
                            col = Integer.parseInt(parts[1]) - (ClientGameModel.getInstance().getShip(username).isLearner ? 5 : 4);
                        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                            System.out.println("\u001B[31mInvalid input. Please enter valid coordinates.\u001B[0m");
                        }
                    } while (row < 0 || row > 4 || col < 0 || col > 6);
                    Pair<Integer, Integer> coordinatesPair = new Pair<>(row, col);
                    ClientGameModel.getInstance().getClient().placeComponent(username, coordinatesPair);
                    break;
                case "3":
                    ClientGameModel.getInstance().getClient().rotateComponentClockwise(username);
                    break;
                case "4":
                    ClientGameModel.getInstance().getClient().rotateComponentCounterclockwise(username);
                    break;
                case "6":
                    ClientGameModel.getInstance().getClient().turnHourglass(username);
                    break;
                case "q":
                    ClientGameModel.getInstance().shutdown();
                    break;
                case "v":
                    TUI.viewOptionsMenu();
                    break;
                default:
                    System.out.println("\u001B[31mInvalid choice. Please try again.\u001B[0m");
                    System.out.print(" > ");
                    break;
            }
        }
        ClientGameModel.getInstance().setFree();
    }

    /**
     * Get the name of the current state
     * @return State name
     */
    @Override
    public String getStateName(){
        return "Building Ship Menu";
    }

    @Override
    public void onShipUpdated(ViewShip ship) {
        ClientGameModel.getInstance().setCurrentMenuState(this);
    }

    @Override
    public void onLobbyUpdated(ViewLobby lobby) {

    }

    @Override
    public void onErrorMessageReceived(String message) {

    }

    @Override
    public void onComponentInHandUpdated(ViewComponent component) {

    }

    @Override
    public void onCurrentCardUpdated(ViewAdventureCard currentCard) {
        
    }

    @Override
    public void onBoardUpdated(ViewBoard board) {

    }
}
