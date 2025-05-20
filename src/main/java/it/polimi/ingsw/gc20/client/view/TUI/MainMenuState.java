package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class MainMenuState implements MenuState{
    private final Scanner scanner = new Scanner(System.in);
    private final String username = ClientGameModel.getInstance().getUsername();

    public MainMenuState() {
    }

    @Override
    public void displayMenu() {
        TUI.clearConsole();
        System.out.println("Welcome to the game! These are the active lobbies:");
        ClientGameModel.getInstance().getLobbyList().forEach(lobby -> System.out.println(lobby.toString()));
        System.out.println("1. Join a lobby");
        System.out.println("2. Create a new lobby");
        System.out.println("3. Refresh lobby list");
    }


    @Override
    public boolean handleInput() throws IOException {
        String choice = scanner.nextLine().trim();
        switch(choice) {
            case "1":
                System.out.println("Type the name of the lobby you want to join:");
                System.out.print(" > ");
                String lobbyName = scanner.nextLine().trim();
                ClientGameModel.getInstance().getClient().joinLobby(lobbyName, username);
                break;
            case "2":
                System.out.println("Type the name of the lobby you want to create:");
                System.out.print(" > ");
                String lobby = scanner.nextLine().trim();

                int numPlayers;

                do {
                    System.out.println("Type the number of players that can play [2-4]:");
                    System.out.print(" > ");
                    numPlayers = Integer.parseInt(scanner.nextLine().trim());
                } while(numPlayers < 2 || numPlayers > 4);

                int level;

                do {
                    System.out.println("Type the level of the game [L/2]:");
                    System.out.print(" > ");
                    String input = scanner.nextLine().trim();
                    level = Objects.equals(input, "L") ? 0 : Integer.parseInt(input);
                } while(level != 0 && level != 2);

                ClientGameModel.getInstance().getClient().createLobby(lobby, username, numPlayers, level);
                break;

            case "3":
                ClientGameModel.getInstance().getClient().getLobbies(username);
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

    @Override
    public String getStateName() {
        return "Main Menu";
    }
}
