package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.client.network.NetworkManager;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards.ViewAdventureCard;
import it.polimi.ingsw.gc20.server.model.cards.FireType;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.server.model.cards.Planet;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;

public class TUI extends ClientGameModel {
    private static final Logger LOGGER = Logger.getLogger(TUI.class.getName());
    private MenuState currentState;

    private static final Scanner scanner = new Scanner(System.in);

    public TUI() throws RemoteException{
        super();
        LOGGER.info("TUI created");
    }

    @Override
    public void notifyDisconnection() throws RemoteException {
        System.out.println("\033[31mDisconnected from server.\033[0m");
    }

    public void init() {
        clearConsole();
        System.out.println("Welcome to Galaxy Trucker!");
        printLogo();
    }

    public static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println();
        System.out.println();
    }

    private void printLogo() {
        System.out.println(" ██████╗  █████╗ ██╗      █████╗ ██╗  ██╗██╗   ██╗    ████████╗██████╗ ██╗   ██╗ ██████╗██╗  ██╗███████╗██████╗ ");
        System.out.println("██╔════╝ ██╔══██╗██║     ██╔══██╗╚██╗██╔╝╚██╗ ██╔╝    ╚══██╔══╝██╔══██╗██║   ██║██╔════╝██║ ██╔╝██╔════╝██╔══██╗");
        System.out.println("██║  ███╗███████║██║     ███████║ ╚███╔╝  ╚████╔╝        ██║   ██████╔╝██║   ██║██║     █████╔╝ █████╗  ██████╔╝");
        System.out.println("██║   ██║██╔══██║██║     ██╔══██║ ██╔██╗   ╚██╔╝         ██║   ██╔══██╗██║   ██║██║     ██╔═██╗ ██╔══╝  ██╔══██╗");
        System.out.println("╚██████╔╝██║  ██║███████╗██║  ██║██╔╝ ██╗   ██║          ██║   ██║  ██║╚██████╔╝╚██████╗██║  ██╗███████╗██║  ██║");
        System.out.println(" ╚═════╝ ╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝          ╚═╝   ╚═╝  ╚═╝ ╚═════╝  ╚═════╝╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝");
    }

    public void shutdown() {
        System.out.println("Application shutting down.");
        client.stop();
        System.exit(0);
    }

    public void wait(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            LOGGER.warning("Error while waiting: " + e.getMessage());
        }
    }

    @SuppressWarnings("BusyWait")
    public void initNetwork() {
        String[] networkTypes = {"RMI", "Socket"};
        int selectedIndex; // To track the currently highlighted menu option

        try {

            do {
                System.out.println("Select network type:");

                for (int i = 0; i < networkTypes.length; i++) {
                    System.out.println((i + 1) + ". " + networkTypes[i]);
                }

                System.out.print(" > ");

                // Read user input
                label:
                do {
                    String input = scanner.nextLine().trim();
                    switch (input) {
                        case "q":
                            System.exit(0);
                        case "1":
                            selectedIndex = 0;
                            break label;
                        case "2":
                            selectedIndex = 1;
                            break label;
                    }
                } while (true);

                // Ask for address and port
                System.out.print("Insert server address (leave blank for default):\n > ");
                String address = scanner.nextLine().trim();
                System.out.print("Insert server port (leave blank for default):\n > ");
                String port = scanner.nextLine().trim();

                clearConsole();

                System.out.println("\033[33mTrying " + networkTypes[selectedIndex] + " connection...\033[0m");

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                // Establish the connection based on user input
                if (address.isBlank()) {
                    client = NetworkManager.initConnection(networkTypes[selectedIndex]);
                } else if (port.isBlank()) {
                    client = NetworkManager.initConnection(networkTypes[selectedIndex], address);
                } else {
                    client = NetworkManager.initConnection(networkTypes[selectedIndex], address, Integer.parseInt(port));
                }

                clearConsole();

                if (client == null || !client.isConnected()) {
                    System.out.println("\033[31mConnection failed. Type any key to try again, type [q] to exit.\033[0m");
                    System.out.print(" > ");

                    if (client != null) {
                        client.stop();
                    }

                    String retry = scanner.nextLine().trim();
                    if (retry.equals("q")) {
                        System.exit(0);
                    }
                    client = null;
                }
            } while (client == null || !client.isConnected());

                // Connection established

                System.out.println("\033[32mConnection established with server at " + client.getAddress() + ":" + client.getPort() + "\033[0m");
                System.out.println("Use [q] to quit the application at any time (works in every menu).");

        } catch (Exception e) {
            System.out.println("\033[31mAn error occurred: " + e.getMessage() + "\033[0m");
        }
    }

    public static void viewOptionsMenu() {
        clearConsole();
        System.out.println("\u001B[1mViewing options:\u001B[0m");
        System.out.println("1. View game board");
        System.out.println("2. View a player's ship");
        System.out.println("3. View uncovered components");
        System.out.println("4. View current card");
        System.out.println("b. Back to the main menu");
        System.out.print(" > ");

        String input = scanner.nextLine().trim();
        switch (input) {
            case "1" -> ClientGameModel.getInstance().printBoard();
            case "2" -> {
                System.out.print("Insert the username of the player you want to view:\n > ");
                String player = scanner.nextLine().trim();
                if (ClientGameModel.getInstance().getShip(player) != null) {
                    ClientGameModel.getInstance().printShip(player);
                } else {
                    System.out.println("\033[31mPlayer not found.\033[0m");
                }
            }
            case "3" -> ClientGameModel.getInstance().printViewedPile();
            case "4" -> ClientGameModel.getInstance().printCurrentCard();
            case "b" -> {
            }
            default -> System.out.println("\033[31mInvalid option. Back to the main menu.\033[0m");
        }
    }

    @Override
    public void login() {
        System.out.print("Insert username (or type [q] to quit):\n > ");
        String inputUsername = scanner.nextLine().trim();

        if (inputUsername.equalsIgnoreCase("q")) {
            shutdown();
            System.exit(0);
        }

        if (inputUsername.isBlank() || inputUsername.equals("__BROADCAST__")) {
            System.out.println("\033[31mUsername not valid. Please try again.\033[0m");
        } else {
            System.out.println("\033[33mLogging in as: " + inputUsername + "\033[0m");
            client.login(inputUsername);
            this.username = inputUsername;
        }
    }

    public void display(MenuState menu) {
        currentState = menu;
        boolean input = false;
        while (!input) {
            try {
                currentState.displayMenu();
                input = currentState.handleInput();
            } catch (IOException e){
                LOGGER.warning("Error while handling input: " + e.getMessage());
            }
        }
    }

    //Display the menu after we get an error, it does not change the state, simply returns to the last menu
    public void display(String message) {
        boolean input = false;
        while (!input) {
            try {
                System.out.println(message);
                currentState.displayMenu();
                input = currentState.handleInput();
            } catch (IOException e){
                LOGGER.warning("Error while handling input: " + e.getMessage());
            }
        }
    }

    public void branchMenu(){
        currentState = new BranchMenu();
        display(currentState);
    }

    public void buildingMenu(List<ViewAdventureCard> adventureCards){
        currentState= new BuildingMenu(adventureCards);
        display(currentState);
    }

    public void inLobbyMenu(){
        MenuState menu = new InLobbyMenu();
        display(menu);
    }

    public void cannonsMenu(String message){
        MenuState menu = new CannonsMenu(message);
        display(menu);
    }

    public void cardAcceptanceMenu(String message){
        MenuState menu = new CardAcceptanceMenu(message);
        display(menu);
    }

    public void engineMenu(String message){
        MenuState menu = new EngineMenu(message);
        display(menu);
    }

    public void cargoMenu(String message, int cargoToLose, List<CargoColor> cargoToGain){
        //conversion from list CargoColor to Map<Integer, CargoColor>
        Map<CargoColor, Integer> cargoMap = new HashMap<>();
        cargoToGain.forEach(cargoColor -> cargoMap.put(cargoColor, cargoMap.getOrDefault(cargoColor, 0) + 1));
        MenuState menu = new CargoMenu(message, cargoToLose, cargoMap);
        display(menu);
    }

    public void planetMenu(List<Planet> planets){
        MenuState menu = new PlanetMenu(planets);
        display(menu);
    }

    public void populateShipMenu(){
        MenuState menu = new PopulateShipMenu();
        display(menu);
    }

    public void validationMenu(){
        MenuState menu = new ValidationMenu();
        display(menu);
    }

    public void automaticAction(String message){
        System.out.println(message);
    }

    public void mainMenuState(){
        MenuState menu = new MainMenuState();
        display(menu);
    }

    public void takeComponentMenu(){
        MenuState menu = new BuildingMenu(null);
        display(menu);
    }

    public void shieldsMenu(FireType fireType, int direction, int line){
        String[] directions = {"UP", "RIGHT", "DOWN", "LEFT"};
        String Message = fireType + " from " + directions[direction]  +  " at line " + line;
        MenuState menu = new ShieldsMenu(Message);
        display(menu);
    }

    public void rollDiceMenu(FireType fireType, int direction){
        //TODO: implement this method
    }

    public void cargoMenu(int cargoNum){
        MenuState menu = new CargoMenu(null, cargoNum, new HashMap<>());
        display(menu);
    }

    public void loseCrewMenu(int crewNum){
        // TODO: implement this method
    }

    public void removeBatteryMenu(int batteryNum){
        // TODO: implement this method
    }

    public void placeComponentMenu(){
        MenuState menu = new BuildingMenu(null);
        display(menu);
    }

    public void leaderBoardMenu(Map<String, Integer> leaderBoard){
        // TODO: implement this method
    }

    @Override
    public void loginSuccessful(String username) {
        clearConsole();
        System.out.println("\033[32mLogged in as: " + username + "\033[0m");
        wait(1);
    }

    @Override
    public void loginFailed(String username) {
        clearConsole();
        System.out.println("\033[31mLogin failed for user: " + username + "\033[0m");
        login();
    }
}
