package it.polimi.ingsw.gc20.client.view.TUI;

public class Main {
    public static void main(String[] args) {
        // Create the view object (e.g., GUI or console)
        // run the view in a separate thread
        // the view will handle connection to the server using the NetworkManager and user input
        TUI.setInstance(new TUI());
        TUI tui = (TUI) TUI.getInstance();

        // run the view in a separate thread

        new Thread(() -> {
            try {
                // Start the TUI
                tui.init();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
