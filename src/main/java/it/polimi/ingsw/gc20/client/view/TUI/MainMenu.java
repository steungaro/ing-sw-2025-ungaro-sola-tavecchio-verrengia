package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.client.view.common.ViewLobby;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;

import java.rmi.RemoteException;
import java.util.Objects;
import java.util.Scanner;

public class MainMenu implements MenuState{
    private final Scanner scanner = new Scanner(System.in);
    private final String username = ClientGameModel.getInstance().getUsername();

    public MainMenu() {
    }

    @Override
    public void displayMenu() {
        System.out.println("Welcome to the game! These are the active lobbies:");
        System.out.println("Your username: " + username);
        int lobbyMaxLength = ClientGameModel.getInstance().getLobbyList().stream()
                .map(ViewLobby::toString)
                .mapToInt(String::length)
                .max()
                .orElse(50);
        System.out.println      ("╭" + "─".repeat(lobbyMaxLength + 2) + "╮");
        ClientGameModel.getInstance().getLobbyList().forEach(lobby -> {System.out.println("│ " + lobby.toString() + " │");});
        if (ClientGameModel.getInstance().getLobbyList().isEmpty()) {
            System.out.println  ("│               (No lobbies available)               │");
        }
        System.out.println      ("╰" + "─".repeat(lobbyMaxLength + 2) + "╯");
        System.out.println("1. Join a lobby");
        System.out.println("2. Create a new lobby");
        System.out.println("3. Refresh lobby list");
        System.out.print(" > ");
    }

    @Override
    public void displayMenu(String errorMessage) {
        System.out.println("\u001B[31m" + errorMessage + "\u001B[0m");
        displayMenu();
    }


    @Override
    public void handleInput(String choice) throws RemoteException {
        ClientGameModel.getInstance().setBusy();
        switch(choice) {
            case "1":
                String lobbyName;
                do {
                    System.out.println("Type the name of the lobby you want to join or [b] to go back:");
                    System.out.print(" > ");
                    lobbyName = scanner.nextLine().trim();
                    if (lobbyName.equals("b")) {
                        TUI.clearConsole();
                        displayMenu();
                        ClientGameModel.getInstance().setFree();
                        return;
                    }
                    if (lobbyName.equals("q")) {
                        ClientGameModel.getInstance().shutdown();
                        return;
                    }
                    if (lobbyName.isEmpty() || !ClientGameModel.getInstance().getLobbyList().stream().map(ViewLobby::getID).toList().contains(lobbyName)) {
                        System.out.println("Lobby not found. Please try again.");
                    } else {
                        break;
                    }
                } while (true);

                ClientGameModel.getInstance().getClient().joinLobby(lobbyName, username);
                break;
            case "2":
                String lobby;
                do {
                    System.out.println("Type the name of the lobby you want to create or [b] to go back:");
                    System.out.print(" > ");
                    lobby = scanner.nextLine().trim();
                    if (lobby.equals("b")) {
                        TUI.clearConsole();
                        displayMenu();
                        ClientGameModel.getInstance().setFree();
                        return;
                    }
                    if (lobby.equals("q")) {
                        ClientGameModel.getInstance().shutdown();
                        return;
                    }
                    if (lobby.isEmpty() || ClientGameModel.getInstance().getLobbyList().stream().map(ViewLobby::getID).toList().contains(lobby)) {
                        System.out.println("Lobby name already in use. Please try again.");
                    } else {
                        break;
                    }
                } while(lobby.isEmpty() || ClientGameModel.getInstance().getLobbyList().stream().map(ViewLobby::getID).toList().contains(lobby));

                int numPlayers;

                do {
                    System.out.println("Type the number of players that can play [2-4]:");
                    System.out.print(" > ");
                    String input = scanner.nextLine().trim();
                    if (input.equals("b")) {
                        TUI.clearConsole();
                        displayMenu();
                        ClientGameModel.getInstance().setFree();
                        return;
                    }
                    if (input.equals("q")) {
                        ClientGameModel.getInstance().shutdown();
                        return;
                    }
                    try {
                        numPlayers = Integer.parseInt(input);
                    } catch (NumberFormatException e) {
                        numPlayers = 0;
                    }
                } while(numPlayers < 2 || numPlayers > 4);

                int level;

                do {
                    System.out.println("Type the level of the game [L/2]:");
                    System.out.print(" > ");
                    String input = scanner.nextLine().trim();
                    if (input.equals("b")) {
                        TUI.clearConsole();
                        displayMenu();
                        ClientGameModel.getInstance().setFree();
                        return;
                    }
                    if (input.equals("q")) {
                        ClientGameModel.getInstance().shutdown();
                        return;
                    }
                    try {
                        level = Objects.equals(input, "L") ? 0 : Integer.parseInt(input);
                    } catch (NumberFormatException e) {
                        level = -1;
                    }
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
                System.out.println("\u001B[31mInvalid choice. Please try again.\u001B[0m");
                System.out.print(" > ");
        }
        ClientGameModel.getInstance().setFree();
    }

    @Override
    public String getStateName() {
        return "Main Menu";
    }
}
