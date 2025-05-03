package it.polimi.ingsw.gc20.client.view.TUI;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;
import it.polimi.ingsw.gc20.client.network.NetworkManager;
import it.polimi.ingsw.gc20.client.view.common.View;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class TUI extends View {
    private SwingTerminalFrame terminal;
    private static final Logger LOGGER = Logger.getLogger(TUI.class.getName());
    private boolean running = true;
    private TextGraphics tg;

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
        // User SwingTerminalFrame for TUI
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        // Set the terminal size
        terminalFactory.setInitialTerminalSize(new com.googlecode.lanterna.TerminalSize(120, 40));

        terminal = terminalFactory.createSwingTerminal();

        // Set the title of the terminal window
        terminal.setTitle("Galaxy Trucker");

        // Set the default close operation to dispose the frame when closed
        terminal.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);

        tg = terminal.newTextGraphics();

        terminal.setCursorVisible(false);

        // Set the terminal to be visible
        terminal.setVisible(true);

        // Set the terminal to be resizable
        terminal.setResizable(true);

        // Sleep for a short time to allow the terminal to initialize
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Set the terminal to be in private mode
        terminal.enterPrivateMode();

        try {
            // Write a message to the terminal
            String[] lines = {
                    " ██████╗  █████╗ ██╗      █████╗ ██╗  ██╗██╗   ██╗    ████████╗██████╗ ██╗   ██╗ ██████╗██╗  ██╗███████╗██████╗ ",
                    "██╔════╝ ██╔══██╗██║     ██╔══██╗╚██╗██╔╝╚██╗ ██╔╝    ╚══██╔══╝██╔══██╗██║   ██║██╔════╝██║ ██╔╝██╔════╝██╔══██╗",
                    "██║  ███╗███████║██║     ███████║ ╚███╔╝  ╚████╔╝        ██║   ██████╔╝██║   ██║██║     █████╔╝ █████╗  ██████╔╝",
                    "██║   ██║██╔══██║██║     ██╔══██║ ██╔██╗   ╚██╔╝         ██║   ██╔══██╗██║   ██║██║     ██╔═██╗ ██╔══╝  ██╔══██╗",
                    "╚██████╔╝██║  ██║███████╗██║  ██║██╔╝ ██╗   ██║          ██║   ██║  ██║╚██████╔╝╚██████╗██║  ██╗███████╗██║  ██║",
                    " ╚═════╝ ╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝          ╚═╝   ╚═╝  ╚═╝ ╚═════╝  ╚═════╝╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝"
            };

            tg.setForegroundColor(TextColor.ANSI.YELLOW);
            tg.setBackgroundColor(TextColor.ANSI.BLACK);

            for (int i = 0; i < lines.length; i++) {
                tg.putString(4, i + 17, lines[i], SGR.BOLD);
            }

            // Flush the terminal to ensure the message is displayed
            terminal.flush();

            // Wait for user input
            terminal.readInput();

            // Clear the terminal
            terminal.clearScreen();

            initNetwork();


            shutdown();

            // Add a main loop to keep the application running
            while (terminal.isVisible() && running) {
                // Process user input or update UI as needed
                KeyStroke key = terminal.pollInput();
                if (key != null) {
                    // Handle input
                }

                // Sleep to avoid high CPU usage
                Thread.sleep(100);
            }

        } catch (Exception e) {
            LOGGER.severe("Error in terminal: " + e.getMessage());
        } finally {
            // Exit private mode when the application is shutting down
            if (terminal != null) {
                terminal.exitPrivateMode();
            }
        }
    }

    // Gracefully close the terminal
    public void shutdown() {
        running = false;
        if (terminal != null) {
            terminal.exitPrivateMode();
            terminal.close();
        }
    }

    public void initNetwork() {
        tg.setForegroundColor(TextColor.ANSI.WHITE);

        do {
            // Display network type menu
            String[] networkTypes = {"RMI", "Socket"};
            terminal.setCursorVisible(true);
            tg.putString(4, 5, "Select network type:", SGR.BOLD);
            for (int i = 0; i < networkTypes.length; i++) {
                tg.putString(4, i + 7, (i + 1) + ". " + networkTypes[i], SGR.BOLD);
            }
            tg.putString(4, 11, " > ", SGR.BOLD);
            terminal.flush();
            // Read user input
            String type = readString(terminal, 7, 11);

            tg.putString(4, 13, "Insert server address (leave blank for default)", SGR.BOLD);
            tg. putString(4, 14, " > ", SGR.BOLD);
            terminal.flush();

            // Read server address
            String address = readString(terminal, 7, 14);

            tg.putString(4, 16, "Insert server port (leave blank for default)", SGR.BOLD);
            tg.putString(4, 17, " > ", SGR.BOLD);
            terminal.flush();

            // Read server port
            String port = readString(terminal, 7, 17);

            if (address.isBlank()) {
                client = NetworkManager.initConnection(networkTypes[Integer.parseInt(type) - 1]);
            } else if (port.isBlank()) {
                client = NetworkManager.initConnection(networkTypes[Integer.parseInt(type) - 1], address);
            } else {
                client = NetworkManager.initConnection(networkTypes[Integer.parseInt(type) - 1], address, Integer.parseInt(port));
            }
            if (client == null || !client.isConnected()) {
                tg.putString(4, 19, "Invalid choice. Please try again.", SGR.BOLD);
                terminal.flush();
            } else {
                tg.putString(4, 20, "Connecting to server...", SGR.BOLD);
                terminal.flush();
            }
        } while (client == null);

        // Clear the terminal
        terminal.clearScreen();

        // Display the connection status
        //TODO
    }

    public String readString(SwingTerminalFrame terminal, int x, int y) {
        List<Character> result = new ArrayList<>();
        int cursorPosition = x;
        TextGraphics tg = terminal.newTextGraphics();
        
        // Make the cursor visible
        terminal.setCursorVisible(true);
        terminal.setCursorPosition(x, y);
        terminal.flush();
        
        while (true) {
            KeyStroke keyStroke = terminal.readInput();
            
            if (keyStroke.getKeyType() == KeyType.Enter) {
                break;
            }
            else if (keyStroke.getKeyType() == KeyType.Backspace && !result.isEmpty()) {
                // Backspace
                result.removeLast();
                cursorPosition--;

                tg.setCharacter(cursorPosition, y, ' ');

                terminal.setCursorPosition(cursorPosition, y);
                terminal.flush();
            }
            else if (keyStroke.getKeyType() == KeyType.Character) {
                // Normal
                char c = keyStroke.getCharacter();
                result.add(c);

                tg.setCharacter(cursorPosition, y, c);
                
                cursorPosition++;
                terminal.setCursorPosition(cursorPosition, y);
                terminal.flush();
            }
            // Ignore other key types
        }
        
        // Hide the cursor after input
        terminal.setCursorVisible(false);
        terminal.flush();
        
        // Convert the result list to a string
        StringBuilder stringBuilder = new StringBuilder(result.size());
        for (Character c : result) {
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }
}