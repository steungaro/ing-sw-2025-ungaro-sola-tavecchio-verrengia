package it.polimi.ingsw.gc20.client.view.TUI;

import org.jline.reader.UserInterruptException;

import java.rmi.RemoteException;

public class MainTUI {
    public static void main(String[] args) throws RemoteException {
        TUI.setInstance(new TUI());
        TUI tui = (TUI) TUI.getInstance();

        // run the view in a separate thread

        new Thread(() -> {
            try {
                // Start the TUI
                tui.init();
                tui.initNetwork();
                tui.wait(2);
                tui.login();
            } catch (UserInterruptException e) {
                // Handle user interrupt (Ctrl+C)
                System.out.println("\nUser interrupted the application.");
                tui.shutdown();
            } catch (Exception e) {
                // Handle other exceptions
                System.out.println("\nAn error occurred: " + e.getMessage());
                tui.shutdown();
            }
        }).start();
    }
}
