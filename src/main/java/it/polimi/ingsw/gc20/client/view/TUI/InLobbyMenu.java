package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;

import java.rmi.RemoteException;

public class InLobbyMenu implements MenuState {
    private final String username = ClientGameModel.getInstance().getUsername();

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
        System.out.print(" > ");
        }
    }

    public void handleInput(String choice) throws RemoteException {
        // Handle user input for the in lobby menu
        ClientGameModel.getInstance().setBusy();
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
                System.out.println("\u001B[31mInvalid input. Please try again.\u001B[0m");
        }
        ClientGameModel.getInstance().setFree();
    }

    public String getStateName() {
        return "In Lobby Menu";
    }
}
