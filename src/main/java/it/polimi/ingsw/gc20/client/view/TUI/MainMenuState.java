package it.polimi.ingsw.gc20.client.view.TUI;

import java.rmi.RemoteException;

public class MainMenuState implements MenuState{
    private final String stateName = "Main Menu";
    private final MenuContext menuContext;

    public MainMenuState(MenuContext  menuContext) {
        this.menuContext = menuContext;
    }

    @Override
    public void displayMenu() {
        System.out.println("Welcome to the game!");
        System.out.println("1. Get list of available lobbies");
        System.out.println("2. Create  a new lobby with the arguments: <Lobby name> <username> <maxPlayers> <level>");
    }


    @Override
    public boolean handleInput() throws RemoteException {
        int choice = menuContext.getScanner().nextInt();
        // Handle user input for the main menu
        switch (choice) {
            case 1:
                menuContext.setState(new LobbyListMenu(menuContext));
                break;
            case 2:
                //parse input using get scanner
                String name = menuContext.getScanner().next();
                String user = menuContext.getUsername();
                int maxPlayers = menuContext.getScanner().nextInt();
                int level = menuContext.getScanner().nextInt();
                menuContext.createLobby(name, user, maxPlayers, level);
                menuContext.setState(new InLobbyMenu(menuContext));
            default:
                System.out.println("Invalid choice. Please try again.");
                return false;
        }
        return true;
    }

    @Override
    public String getStateName() {
        return stateName;
    }
}
