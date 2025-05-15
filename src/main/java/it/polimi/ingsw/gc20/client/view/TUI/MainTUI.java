package it.polimi.ingsw.gc20.client.view.TUI;

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
                tui.login();
                tui.lobbyLoop();
                //tui.gameLoop();
                tui.shutdown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
