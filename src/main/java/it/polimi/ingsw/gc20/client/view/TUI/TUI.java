package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.client.network.NetworkManager;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards.ViewAdventureCard;
import it.polimi.ingsw.gc20.server.model.cards.FireType;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.server.model.cards.Planet;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;

public class TUI extends ClientGameModel {
    private static final Logger LOGGER = Logger.getLogger(TUI.class.getName());
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
        System.out.println("b. Back to the menu");
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
            default -> {
                ClientGameModel.getInstance().getCurrentMenuState().displayMenu("Invalid option. Back to the main menu.");
                return;
            }
        }
        ClientGameModel.getInstance().getCurrentMenuState().displayMenu();
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

    public void branchMenu(){
        ClientGameModel.getInstance().setCurrentMenuState(new BranchMenu());
    }

    public void buildingMenu(List<ViewAdventureCard> adventureCards){
        ClientGameModel.getInstance().setCurrentMenuState(new BuildingMenu(adventureCards));
    }

    public void inLobbyMenu(){
        ClientGameModel.getInstance().setCurrentMenuState(new InLobbyMenu());
    }

    public void cannonsMenu(String message){
        ClientGameModel.getInstance().setCurrentMenuState(new CannonsMenu(message));
    }

    public void cardAcceptanceMenu(String message){
        ClientGameModel.getInstance().setCurrentMenuState(new CardAcceptanceMenu(message));
    }

    public void engineMenu(String message){
        ClientGameModel.getInstance().setCurrentMenuState(new EngineMenu(message));
    }

    public void cargoMenu(String message, int cargoToLose, List<CargoColor> cargoToGain, boolean losing){
        //conversion from list CargoColor to Map<Integer, CargoColor>
        Map<CargoColor, Integer> cargoMap = new HashMap<>();
        cargoToGain.forEach(cargoColor -> cargoMap.put(cargoColor, cargoMap.getOrDefault(cargoColor, 0) + 1));
        ClientGameModel.getInstance().setCurrentMenuState(new CargoMenu(message, cargoToLose, cargoMap, losing));
    }

    public void planetMenu(List<Planet> planets){
        ClientGameModel.getInstance().setCurrentMenuState(new PlanetMenu(planets));
    }

    public void populateShipMenu(){
        ClientGameModel.getInstance().setCurrentMenuState(new PopulateShipMenu());
    }

    public void validationMenu(){
        ClientGameModel.getInstance().setCurrentMenuState(new ValidationMenu());
    }

    public void automaticAction(String message){
        System.out.println(message);
    }

    public void mainMenuState(){
        ClientGameModel.getInstance().setCurrentMenuState(new MainMenu());
    }

    public void AssemblingStateMenu(){
        ClientGameModel.getInstance().setCurrentMenuState(new BuildingMenu(null));
    }

    public void shieldsMenu(FireType fireType, int direction, int line){
        String[] directions = {"UP", "RIGHT", "DOWN", "LEFT"};
        String Message = fireType + " from " + directions[direction]  +  " at line " + line;
        ClientGameModel.getInstance().setCurrentMenuState(new ShieldsMenu(Message));
    }

    public void rollDiceMenu(FireType fireType, int direction){
        String[] directions = {"UP", "RIGHT", "DOWN", "LEFT"};
        String Message = fireType + " from " + directions[direction];
        ClientGameModel.getInstance().setCurrentMenuState(new RollDiceMenu(Message));
    }

    public void cargoMenu(int cargoNum){
        ClientGameModel.getInstance().setCurrentMenuState(new CargoMenu(null, cargoNum, new HashMap<>(), true));
    }

    public void loseCrewMenu(int crewNum){
        ClientGameModel.getInstance().setCurrentMenuState(new LoseCrewMenu(crewNum));
    }

    public void removeBatteryMenu(int batteryNum){
        ClientGameModel.getInstance().setCurrentMenuState(new LoseEnergyMenu(batteryNum));
    }

    public void leaderBoardMenu(Map<String, Integer> leaderBoard){
        ClientGameModel.getInstance().setCurrentMenuState(new EndGameMenu(leaderBoard));
    }

    public void idleMenu(String message) {
        ClientGameModel.getInstance().setCurrentMenuState(new IdleMenu(message));
    }

    public void displayErrorMessage(String errorMessage) {
        clearConsole();
        ClientGameModel.getInstance().getCurrentMenuState().displayMenu(errorMessage);
    }

    public void keepPlayingMenu(){
        ClientGameModel.getInstance().setCurrentMenuState(new KeepPlayingMenu());
    }

    @Override
    public void loginSuccessful(String username) {
        clearConsole();
        System.out.println("\033[32mLogged in as: " + username + "\033[0m");
        Thread inputThread = new Thread(() -> {
            while (true) {
                try {
                    String input = scanner.nextLine().trim();
                    if (input.equals("q")) {
                        shutdown();
                    } else {
                        ClientGameModel.getInstance().getCurrentMenuState().handleInput(input);
                    }
                } catch (RemoteException e) {
                    LOGGER.warning("Error handling input: " + e.getMessage());
                }
            }
        });
        inputThread.start();
        // add a shutdown hook to ensure the application can be closed gracefully
        Runtime.getRuntime().addShutdownHook(new Thread(inputThread::interrupt));
        wait(1);
    }

    @Override
    public void loginFailed(String username) {
        clearConsole();
        System.out.println("\033[31mLogin failed for user: " + username + "\033[0m");
        login();
    }
}
