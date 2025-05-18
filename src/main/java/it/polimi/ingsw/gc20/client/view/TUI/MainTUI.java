package it.polimi.ingsw.gc20.client.view.TUI;

import org.jline.reader.UserInterruptException;

public class MainTUI {
    public static void main(String[] args) {
        TUI.setInstance(new TUI());
        TUI tui = (TUI) TUI.getInstance();

        // run the view in a separate thread

        new Thread(() -> {
            try {
                // Start the TUI
                tui.init();
                tui.initNetwork();
                tui.wait(1);
                tui.login();
                tui.lobbyLoop();
                //tui.gameLoop();
                tui.shutdown();
            } catch (UserInterruptException e) {
                // Handle user interrupt (Ctrl+C)
                tui.getTerminal().writer().println("\nUser interrupted the application.");
                tui.shutdown();
            } catch (Exception e) {
                // Handle other exceptions
                tui.getTerminal().writer().println("\nAn error occurred: " + e.getMessage());
                tui.shutdown();
            } finally {
                // Ensure the terminal is closed properly
                try {
                    tui.getTerminal().close();
                } catch (Exception e) {
                    tui.getTerminal().writer().println("Error closing terminal: " + e.getMessage());
                }
            }
        }).start();
    }
}
