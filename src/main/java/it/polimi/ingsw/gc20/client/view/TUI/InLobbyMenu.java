package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;

import java.io.IOException;
import java.util.Scanner;

public class InLobbyMenu implements MenuState {
    private final String username = ClientGameModel.getInstance().getUsername();
    private final Scanner scanner = new Scanner(System.in);

    public InLobbyMenu() {
    }

    public void displayMenu() {
        TUI.clearConsole();
        System.out.println("\u001B[1mLobby menu\u001B[22m");
        System.out.println("--------------------------------------------------");
        System.out.println("Current lobby: " + ClientGameModel.getInstance().getCurrentLobby());
        System.out.println("--------------------------------------------------");
        if(ClientGameModel.getInstance().getCurrentLobby().getOwner().equals(username)){
            System.out.println("1. Start game");
            System.out.println("2. Kill lobby");
        } else {
            System.out.println("1. Leave lobby");
        }
    }

    public boolean handleInput() throws IOException {
        System.out.print(" > ");
        String choice = scanner.nextLine().trim();
        // Handle user input for the in lobby menu
        switch (choice) {
            case "1":
                if(ClientGameModel.getInstance().getCurrentLobby().getOwner().equals(username)){
                    ClientGameModel.getInstance().getClient().startLobby(username);
                } else {
                    ClientGameModel.getInstance().getClient().leaveLobby(username);
                }
                break;
            case "2":
                if(ClientGameModel.getInstance().getCurrentLobby().getOwner().equals(username)){
                    ClientGameModel.getInstance().getClient().killLobby(username);
                }
                break;
            case "q":
                ClientGameModel.getInstance().shutdown();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                return false;
        }
        return true;
    }

    public String getStateName() {
        return "In Lobby Menu";
    }
}
