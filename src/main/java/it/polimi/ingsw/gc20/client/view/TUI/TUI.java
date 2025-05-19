package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.client.network.NetworkManager;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.server.model.cards.FireType;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import org.jline.reader.LineReader;
import it.polimi.ingsw.gc20.server.model.cards.Planet;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Attributes;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;

import java.io.IOException;
import java.io.Reader;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class TUI extends ClientGameModel {
    private static final Logger LOGGER = Logger.getLogger(TUI.class.getName());

    private Terminal terminal;
    private LineReader reader;
    private MenuState currentState;

    public TUI() {
        LOGGER.info("TUI created");
    }

    @Override
    public void notifyDisconnection() throws RemoteException {
        terminal.writer().println("\033[31mDisconnected from server.\033[0m");
        terminal.flush();
    }

    public void init() {
        System.out.println("Welcome to Galaxy Trucker!");

        try {
            terminal = TerminalBuilder.builder().system(true).build();
            reader = LineReaderBuilder.builder().terminal(terminal).build();
        } catch (Exception e) {
            LOGGER.warning("Error while initializing terminal: " + e.getMessage());
        }

        printLogo();
    }

    public static void clearConsole(Terminal terminal) {
        terminal.puts(InfoCmp.Capability.clear_screen);
        terminal.flush();
    }

    private void printLogo() {
        terminal.writer().println(" ██████╗  █████╗ ██╗      █████╗ ██╗  ██╗██╗   ██╗    ████████╗██████╗ ██╗   ██╗ ██████╗██╗  ██╗███████╗██████╗ ");
        terminal.writer().println("██╔════╝ ██╔══██╗██║     ██╔══██╗╚██╗██╔╝╚██╗ ██╔╝    ╚══██╔══╝██╔══██╗██║   ██║██╔════╝██║ ██╔╝██╔════╝██╔══██╗");
        terminal.writer().println("██║  ███╗███████║██║     ███████║ ╚███╔╝  ╚████╔╝        ██║   ██████╔╝██║   ██║██║     █████╔╝ █████╗  ██████╔╝");
        terminal.writer().println("██║   ██║██╔══██║██║     ██╔══██║ ██╔██╗   ╚██╔╝         ██║   ██╔══██╗██║   ██║██║     ██╔═██╗ ██╔══╝  ██╔══██╗");
        terminal.writer().println("╚██████╔╝██║  ██║███████╗██║  ██║██╔╝ ██╗   ██║          ██║   ██║  ██║╚██████╔╝╚██████╗██║  ██╗███████╗██║  ██║");
        terminal.writer().println(" ╚═════╝ ╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝          ╚═╝   ╚═╝  ╚═╝ ╚═════╝  ╚═════╝╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝");
        terminal.flush();
        terminal.writer().println("Press any key to continue...");
        terminal.flush();
        try {
            // Hide cursor
            terminal.puts(InfoCmp.Capability.cursor_invisible);
            terminal.reader().read();
        } catch (Exception e) {
            LOGGER.warning("Error while waiting for user input: " + e.getMessage());
        }
    }

    public void shutdown() {
        terminal.writer().println("Application shutting down.");
        terminal.flush();
        client.stop();
        try {
            terminal.close();
        } catch (Exception e) {
            LOGGER.warning("Error while closing terminal: " + e.getMessage());
        }
        System.exit(0);
    }

    public void wait(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            LOGGER.warning("Error while waiting: " + e.getMessage());
        }
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public static void hideCursor(Terminal terminal) {
        terminal.puts(InfoCmp.Capability.cursor_invisible);
        Attributes attributes = terminal.getAttributes();
        attributes.setLocalFlag(Attributes.LocalFlag.ECHO, false);
        terminal.setAttributes(attributes);
    }

    public static void showCursor(Terminal terminal) {
        terminal.puts(InfoCmp.Capability.cursor_visible);
        Attributes attributes = terminal.getAttributes();
        attributes.setLocalFlag(Attributes.LocalFlag.ECHO, true);
        terminal.setAttributes(attributes);
    }

    public void initNetwork() {
        String[] networkTypes = {"RMI", "Socket"};
        int selectedIndex = 0; // To track the currently highlighted menu option

        try {

            do {
                terminal.enterRawMode();
                clearConsole(terminal);
                terminal.writer().println("Select network type:");

                for (int i = 0; i < networkTypes.length; i++) {
                    terminal.writer().println((i + 1) + ". " + networkTypes[i]);
                }
                terminal.flush();

                // Hide cursor
                hideCursor(terminal);

                Reader treader = terminal.reader();

                // Read user input
                do {
                    int key = treader.read();
                    if (key == 'q') {
                        System.exit(0);
                    } else if (key == '1') {
                        break;
                    } else if (key == '2') {
                        selectedIndex = 1;
                        break;
                    }
                } while (true);

                // Reset terminal attributes
                showCursor(terminal);

                // Final selected network type
                String selectedNetworkType = networkTypes[selectedIndex];

                // Show cursor
                terminal.puts(InfoCmp.Capability.cursor_visible);

                // Ask for address and port
                String address = reader.readLine("Insert server address (leave blank for default):\n > ").trim();
                String port = reader.readLine("Insert server port (leave blank for default):\n > ").trim();

                terminal.writer().println("Trying connection...");
                terminal.flush();

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
                    // Hide cursor
                    hideCursor(terminal);
                    terminal.writer().println("\033[31mConnection failed. Press any key to try again, type [q] to exit.\033[0m");
                    terminal.flush();

                    int retry = treader.read();
                    if (retry == 'q') {
                        System.exit(0);
                    }
                    client = null;
                }
            } while (client == null || !client.isConnected());

                // Connection established

                terminal.writer().println("\033[32mConnection established with server at " + client.getAddress() + ":" + client.getPort() + "\033[0m");
                terminal.writer().println("Use [q] to quit the application at any time (works in every menu).");
                terminal.flush();

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                clearConsole(terminal);
        } catch (Exception e) {
            terminal.writer().println("\033[31mAn error occurred: " + e.getMessage() + "\033[0m");
            terminal.flush();
        } finally {
            try {
                terminal.close();
            } catch (Exception ex) {
                terminal.writer().println("Terminal failed to close properly.");
            }
        }
    }

    public void login() {
        do {
            clearConsole(terminal);
            String inputUsername = reader.readLine("Insert username:\n > ").trim();

            if (inputUsername.equalsIgnoreCase("q")) {
                System.exit(0);
            }

            if (inputUsername.isBlank() || inputUsername.equals("__BROADCAST__")) {
                terminal.writer().println("\033[31mUsername not valid. Please try again.\033[0m");
                terminal.flush();
                continue;
            }

            client.login(inputUsername);
            this.username = inputUsername;

        } while (!loggedIn);

        terminal.writer().println("\033[32mLogged in as: " + username + "\033[0m");
        terminal.flush();
    }

    public void display(MenuState menu) {
        currentState = menu;
        boolean input = false;
        while (client.isConnected() && !input) {
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
        while (client.isConnected() && !input) {
            try {
                currentState.displayMenu();
                input = currentState.handleInput();
            } catch (IOException e){
                LOGGER.warning("Error while handling input: " + e.getMessage());
            }
        }
    }

    public void branchMenu(){
        MenuState menu = new BranchMenu(terminal);
        display(menu);
    }

    public void buildingMenu(){
        MenuState menu = new BuildingMenu(terminal);
        display(menu);
    }

    public void inLobbyMenu(){
        MenuState menu = new InLobbyMenu(terminal);
        display(menu);
    }

    public void cannonsMenu(String message){
        MenuState menu = new CannonsMenu(terminal, message);
        display(menu);
    }

    public void cardAcceptanceMenu(String message){
        MenuState menu = new CardAcceptanceMenu(terminal, message);
        display(menu);
    }

    public void engineMenu(String message){
        MenuState menu = new EngineMenu(terminal, message);
        display(menu);
    }

    public void cargoMenu(String message, int cargoToLose, List<CargoColor> cargoToGain){
        //conversion from list CargoColor to Map<Integer, CargoColor>
        Map<CargoColor, Integer> cargoMap = new HashMap<>();
        cargoToGain.forEach(cargoColor -> {
            cargoMap.put(cargoColor, cargoMap.getOrDefault(cargoColor, 0) + 1);
        });
        MenuState menu = new CargoMenu(terminal, message, cargoToLose, cargoMap);
        display(menu);
    }

    public void planetMenu(List<Planet> planets){
        MenuState menu = new PlanetMenu(terminal, planets);
        display(menu);
    }

    public void populateShipMenu(){
        MenuState menu = new PopulateShipMenu(terminal);
        display(menu);
    }

    public void validationMenu(){
        MenuState menu = new ValidationMenu(terminal);
        display(menu);
    }

    public void automaticAction(String message){
        System.out.println(message);
    }

    public void mainMenuState(){
        MenuState menu = new MainMenuState(terminal);
        display(menu);
    }

    public void takeComponentMenu(){
        //TODO: implement this method
    }

    public void shieldsMenu(FireType fireType, int direction, int line){
        String[] directions = {"UP", "RIGHT", "DOWN", "LEFT"};
        String Message = fireType + " from " + directions[direction]  +  " at line " + line;
        MenuState state = new ShieldsMenu(getTerminal(), Message);
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
}
