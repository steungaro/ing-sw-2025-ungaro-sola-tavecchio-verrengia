package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.client.network.NetworkManager;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards.ViewAdventureCard;
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

    /**
     * Constructor for the TUI class.
     */
    public TUI() throws RemoteException {
        super();
        LOGGER.info("TUI created");
    }

    @Override
    public void notifyDisconnection() throws RemoteException {
        client.stop();
        System.out.println("\033[31mDisconnected from server.\033[0m");
    }

    @Override
    public void init() {
        clearConsole();
        System.out.println("Welcome to Galaxy Trucker!");
        printLogo();
    }

    /**
     * Clears the console by printing escape sequences.
     * This method is used to clear the console output in the TUI.
     */
    public static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println();
        System.out.println();
    }

    /**
     * Prints the logo of the game in the console.
     * This method is used to display the game's logo when the TUI is initialized.
     */
    private void printLogo() {
        System.out.println(" ██████╗  █████╗ ██╗      █████╗ ██╗  ██╗██╗   ██╗    ████████╗██████╗ ██╗   ██╗ ██████╗██╗  ██╗███████╗██████╗ ");
        System.out.println("██╔════╝ ██╔══██╗██║     ██╔══██╗╚██╗██╔╝╚██╗ ██╔╝    ╚══██╔══╝██╔══██╗██║   ██║██╔════╝██║ ██╔╝██╔════╝██╔══██╗");
        System.out.println("██║  ███╗███████║██║     ███████║ ╚███╔╝  ╚████��╝        ██║   ██████╔╝██║   ██║██║     █████╔╝ █████╗  ██████╔╝");
        System.out.println("██║   ██║██╔══██║██║     ██╔══██║ ██╔██╗   ╚██╔╝         ██║   ██╔══██╗██║   ██║██║     ██╔═██╗ ██╔══╝  ██╔══██╗");
        System.out.println("╚██████╔╝██║  ██║███████╗██║  ██║██╔╝ ██╗   ██║          ██║   ██║  ██║╚██████╔╝╚██████╗██║  ██╗███████╗██║  ██║");
        System.out.println(" ╚═════╝ ╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝          ╚═╝   ╚═╝  ╚═╝ ╚═════╝  ╚═════╝╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝");
    }

    @Override
    public void shutdown() {
        System.out.println("Application shutting down.");
        client.stop();
        System.exit(0);
    }


    /**
     * Initializes the network connection for the TUI.
     * It prompts the user to select a network type (RMI or Socket) and enter the server address and port.
     * The method will keep retrying until a successful connection is established.
     */
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

    /**
     * Displays the view-related options menu for the TUI.
     * This method allows the user to view the game board, a player's ship, or the current card.
     * It also provides an option to go back to the main menu.
     */
    public static void viewOptionsMenu() {
        clearConsole();
        System.out.println("\u001B[1mViewing options:\u001B[0m");
        System.out.println("1. View game board");
        System.out.println("2. View a player's ship");
        System.out.println("3. View current card");
        System.out.println("4. View my ship");
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
            case "3" -> ClientGameModel.getInstance().printCurrentCard();
            case "4" -> ClientGameModel.getInstance().printShip(ClientGameModel.getInstance().getUsername());
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

    @Override
    public void branchMenu() {
        ClientGameModel.getInstance().setCurrentMenuState(new BranchMenu());
    }

    @Override
    public void buildingMenu(List<ViewAdventureCard> adventureCards) {
        ClientGameModel.getInstance().setCurrentMenuState(new BuildingMenu(adventureCards));
    }

    @Override
    public void inLobbyMenu() {
        ClientGameModel.getInstance().setCurrentMenuState(new InLobbyMenu());
    }

    @Override
    public void cannonsMenu(String message) {
        ClientGameModel.getInstance().setCurrentMenuState(new CannonsMenu(message));
    }

    @Override
    public void cardAcceptanceMenu(String message) {
        ClientGameModel.getInstance().setCurrentMenuState(new CardAcceptanceMenu(message));
    }

    @Override
    public void engineMenu(String message) {
        ClientGameModel.getInstance().setCurrentMenuState(new EngineMenu(message));
    }

    @Override
    public void cargoMenu(String message, int cargoToLose, List<CargoColor> cargoToGain, boolean losing) {
        Map<CargoColor, Integer> cargoMap = new HashMap<>();
        cargoToGain.forEach(cargoColor -> cargoMap.put(cargoColor, cargoMap.getOrDefault(cargoColor, 0) + 1));
        ClientGameModel.getInstance().setCurrentMenuState(new CargoMenu(message, cargoToLose, cargoMap, losing));
    }

    @Override
    public void planetMenu(List<Planet> planets) {
        ClientGameModel.getInstance().setCurrentMenuState(new PlanetMenu(planets));
    }

    @Override
    public void populateShipMenu() {
        ClientGameModel.getInstance().setCurrentMenuState(new PopulateShipMenu());
    }

    @Override
    public void validationMenu() {
        ClientGameModel.getInstance().setCurrentMenuState(new ValidationMenu());
    }

    @Override
    public void automaticAction(String message) {
        clearConsole();
        ClientGameModel.getInstance().printBoard();
        System.out.println(message);
    }

    @Override
    public void mainMenuState() {
        ClientGameModel.getInstance().setCurrentMenuState(new MainMenu());
    }

    @Override
    public void assemblingStateMenu() {
        ClientGameModel.getInstance().setCurrentMenuState(new BuildingMenu(null));
    }

    @Override
    public void shieldsMenu(String message) {
        ClientGameModel.getInstance().setCurrentMenuState(new ShieldsMenu(message));
    }

    @Override
    public void rollDiceMenu(String message) {
        ClientGameModel.getInstance().setCurrentMenuState(new RollDiceMenu(message));
    }

    @Override
    public void cargoMenu(int cargoNum) {
        ClientGameModel.getInstance().setCurrentMenuState(new CargoMenu(null, cargoNum, new HashMap<>(), true));
    }

    @Override
    public void loseCrewMenu(int crewNum) {
        ClientGameModel.getInstance().setCurrentMenuState(new LoseCrewMenu(crewNum));
    }

    @Override
    public void removeBatteryMenu(int batteryNum) {
        ClientGameModel.getInstance().setCurrentMenuState(new LoseEnergyMenu(batteryNum));
    }

    @Override
    public void leaderBoardMenu(Map<String, Integer> leaderBoard) {
        ClientGameModel.getInstance().setCurrentMenuState(new EndGameMenu(leaderBoard));
    }

    @Override
    public void idleMenu(String message) {
        ClientGameModel.getInstance().setCurrentMenuState(new IdleMenu(message));
    }

    @Override
    public void displayErrorMessage(String errorMessage) {
        clearConsole();
        ClientGameModel.getInstance().getCurrentMenuState().displayMenu(errorMessage);
    }

    @Override
    public void keepPlayingMenu() {
        ClientGameModel.getInstance().setCurrentMenuStateNoClear(new KeepPlayingMenu());
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
        Runtime.getRuntime().addShutdownHook(new Thread(inputThread::interrupt));
    }

    @Override
    public void loginFailed(String username) {
        clearConsole();
        System.out.println("\033[31mLogin failed for user: " + username + "\033[0m");
        login();
    }
}
