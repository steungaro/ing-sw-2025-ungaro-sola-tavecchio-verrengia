package it.polimi.ingsw.gc20.client.view.TUI;

import java.rmi.RemoteException;

public class MainTUI {
    /**
     * The main method serves as the entry point for the application.
     * It initializes the TUI instance and starts it in a separate thread.
     *
     * @param args the command-line arguments passed to the program
     * @throws RemoteException if a remote invocation error occurs during initialization
     */
    public static void main(String[] args) throws RemoteException {
        TUI.setInstance(new TUI());
        TUI tui = (TUI) TUI.getInstance();

        // run the view in a separate thread

        new Thread(() -> {
            try {
                // Start the TUI
                tui.init();
                tui.initNetwork();
                tui.login();
            } catch (Exception e) {
                // Handle other exceptions
                System.out.println("\nAn error occurred: " + e.getMessage());
                tui.shutdown();
            }
        }).start();
    }
}
