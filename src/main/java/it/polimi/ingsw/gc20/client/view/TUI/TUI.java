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

    private final Scanner scanner;

    public TUI() throws RemoteException{
        LOGGER.info("TUI created");
        scanner = new Scanner(System.in);
        super();
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

    public void initNetwork() {
        String[] networkTypes = {"RMI", "Socket"};
        int selectedIndex = 0; // To track the currently highlighted menu option

        try {

            do {
                System.out.println("Select network type:");

                for (int i = 0; i < networkTypes.length; i++) {
                    System.out.println((i + 1) + ". " + networkTypes[i]);
                }

                System.out.print(" > ");

                // Read user input
                do {
                    String input = scanner.nextLine().trim();
                    if (input.equals("q")) {
                        System.exit(0);
                    } else if (input.equals("1")) {
                        break;
                    } else if (input.equals("2")) {
                        selectedIndex = 1;
                        break;
                    }
                } while (true);

                // Final selected network type
                String selectedNetworkType = networkTypes[selectedIndex];

                // Ask for address and port
                System.out.print("Insert server address (leave blank for default):\n > ");
                String address = scanner.nextLine().trim();
                System.out.print("Insert server port (leave blank for default):\n > ");
                String port = scanner.nextLine().trim();

                System.out.println("Trying connection...");

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                // Establish the connection based on user input
                if (address.isBlank()) {
                    client = NetworkManager.initConnection(selectedNetworkType);
                } else if (port.isBlank()) {
                    client = NetworkManager.initConnection(selectedNetworkType, address);
                } else {
                    client = NetworkManager.initConnection(selectedNetworkType, address, Integer.parseInt(port));
                }

                if (client == null || !client.isConnected()) {
                    System.out.println("\033[31mConnection failed. Press any key to try again, type [q] to exit.\033[0m");

                    String retry = scanner.nextLine().trim();
                    if (retry.equals("q")) {
                        System.exit(0);
                    }
                    client = null;
                }
                clearConsole();
            } while (client == null || !client.isConnected());

                // Connection established

                System.out.println("\033[32mConnection established with server at " + client.getAddress() + ":" + client.getPort() + "\033[0m");
                System.out.println("Use [q] to quit the application at any time (works in every menu).");

                wait(2);

                clearConsole();
        } catch (Exception e) {
            System.out.println("\033[31mAn error occurred: " + e.getMessage() + "\033[0m");
        }
    }

    public void login() {
        clearConsole();
        System.out.print("Insert username (or type [q] to quit):\n > ");
        String inputUsername = scanner.nextLine().trim();

        if (inputUsername.equalsIgnoreCase("q")) {
            shutdown();
            System.exit(0);
        }

        if (inputUsername.isBlank() || inputUsername.equals("__BROADCAST__")) {
            System.out.println("\033[31mUsername not valid. Please try again.\033[0m");
        } else {
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

    //Display the menu after we get an error, it does not change the state, simply returns to last menu
    public void display(String message) {
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

    public void branchMenu(){
        MenuState menu = new BranchMenu();
        display(menu);
    }

    public void buildingMenu(List<ViewAdventureCard> adventureCards){
        MenuState menu = new BuildingMenu(adventureCards);
        display(menu);
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
        cargoToGain.forEach(cargoColor -> {
            cargoMap.put(cargoColor, cargoMap.getOrDefault(cargoColor, 0) + 1);
        });
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
        //TODO: implement this method
    }

    public void shieldsMenu(FireType fireType, int direction, int line){
        String[] directions = {"UP", "RIGHT", "DOWN", "LEFT"};
        String Message = fireType + " from " + directions[direction]  +  " at line " + line;
        MenuState state = new ShieldsMenu(Message);
        // ClientGameModel.display(state);
    }

    public void rollDiceMenu(FireType fireType, int direction){
        //TODO: implement this method
    }

    public void cargoMenu(int cargoNum){
        // TODO: implement this method
    }

    public void loseCrewMenu(int crewNum){
        // TODO: implement this method
    }

    public void removeBatteryMenu(int batteryNum){
        // TODO: implement this method
    }

    public void placeComponentMenu(){
        // TODO: implement this method
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
