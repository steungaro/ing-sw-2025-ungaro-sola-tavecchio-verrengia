package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.client.network.NetworkManager;
import it.polimi.ingsw.gc20.client.view.common.View;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;

import java.rmi.RemoteException;
import java.util.logging.Logger;

public class TUI extends View {
    private static final Logger LOGGER = Logger.getLogger(TUI.class.getName());

    private Terminal terminal;
    private LineReader reader;

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

    public void clearConsole() {
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
    }

    public void shutdown() {
        terminal.writer().println("Application shutting down.");
        terminal.flush();
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public void initNetwork() {
        String type;
        String[] networkTypes = {"RMI", "Socket"};

        do {
            do {
                terminal.writer().println("Select network type:");
                for (int i = 0; i < networkTypes.length; i++) {
                    terminal.writer().println((i + 1) + ". " + networkTypes[i]);
                }
                terminal.flush();

                type = reader.readLine(" > ").trim();
            } while (!type.matches("[1-2]"));

            String address = reader.readLine("Insert server address (leave blank for default):\n > ").trim();
            String port = reader.readLine("Insert server port (leave blank for default):\n > ").trim();

            terminal.writer().println("Trying connection...");
            terminal.flush();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (address.isBlank()) {
                client = NetworkManager.initConnection(networkTypes[Integer.parseInt(type) - 1]);
            } else if (port.isBlank()) {
                client = NetworkManager.initConnection(networkTypes[Integer.parseInt(type) - 1], address);
            } else {
                client = NetworkManager.initConnection(networkTypes[Integer.parseInt(type) - 1], address, Integer.parseInt(port));
            }

            if (client == null || !client.isConnected()) {
                terminal.writer().print("\033[31mConnection failed. Type [1] to try again, [2] to exit.\033[0m\n > ");
                terminal.flush();
                String retry = reader.readLine("").trim();
                if ("2".equals(retry)) {
                    System.exit(0);
                }
                client = null;
            }

        } while (client == null || !client.isConnected());

        terminal.writer().println("\033[32mConnection established with server at " + client.getAddress() + ":" + client.getPort() + "\033[0m");
        terminal.writer().println("Use [q] to quit the application at any time (works in every menu).");
        terminal.flush();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        clearConsole();
    }

    public void login() {
        do {
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
}
