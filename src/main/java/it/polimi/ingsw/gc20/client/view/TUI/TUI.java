package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.client.network.NetworkManager;
import it.polimi.ingsw.gc20.client.view.common.View;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;

import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

public class TUI extends View {
    private static final Logger LOGGER = Logger.getLogger(TUI.class.getName());
    Scanner scanner;

    private ReentrantLock writingLock = new ReentrantLock();

    public TUI() {
        LOGGER.info("TUI created");
    }

    @Override
    public void updateView(Message message) throws RemoteException {
        // Your implementation
    }

    @Override
    public void notifyDisconnection() throws RemoteException {
        // Handle disconnection
    }

    public void init() {
        System.out.println("Welcome to Galaxy Trucker!");
        scanner = new Scanner(System.in);

        printLogo();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        clearConsole();
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
    }

    public void initNetwork() {
        String type;
        String[] networkTypes = {"RMI", "Socket"};

        writingLock.lock();

        do {
            do {
                System.out.println("Select network type:");
                for (int i = 0; i < networkTypes.length; i++) {
                    System.out.println((i + 1) + ". " + networkTypes[i]);
                }
                System.out.print(" > ");

                type = scanner.nextLine().trim();

            } while (!type.matches("[1-2]"));

            System.out.println("Insert server address (leave blank for default):");
            System.out.print(" > ");
            String address = scanner.nextLine().trim();

            System.out.println("Insert server port (leave blank for default):");
            System.out.print(" > ");
            String port = scanner.nextLine().trim();

            System.out.println("Trying connection...");

            try {
                Thread.sleep(1000); // Simulate delay
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
                // set the console color to red
                System.out.print("\033[31m");
                System.out.println("Connection failed. Type [1] to try again, type [2] to exit.");
                // reset the console color to default
                System.out.print("\033[0m");
                System.out.print(" > ");
                String retry = scanner.nextLine().trim();
                if ("2".equals(retry)) {
                    System.exit(0);
                }
                client = null;
            }
        } while (client == null || !client.isConnected());

        // set the console color to green
        System.out.print("\033[32m");
        System.out.println("Connection established with server at " + client.getAddress() + ":" + client.getPort());
        // reset the console color to default
        System.out.print("\033[0m");

        System.out.println("Use [q] to quit the application at any time (works in every menu).");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        clearConsole();

        writingLock.unlock();
    }

    public void login() {
        do {
            writingLock.lock();

            System.out.println("Insert username:");
            System.out.print(" > ");
            String username = scanner.nextLine().trim();

            if (username.equalsIgnoreCase("q")) {
                System.exit(0);
            }

            if (username.isBlank() || username.equals("__BROADCAST__")) {
                // set the console color to red
                System.out.print("\033[31m");
                System.out.println("Username not valid. Please try again.");
                // reset the console color to default
                System.out.print("\033[0m");
                continue;
            }

            client.login(username);
        } while (!loggedIn);

        // set the console color to green
        System.out.print("\033[32m");
        System.out.println("Logged in as: " + username);
        // reset the console color to default
        System.out.print("\033[0m");

        this.username = username;
    }
}