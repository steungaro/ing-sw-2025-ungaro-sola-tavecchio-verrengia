package it.polimi.ingsw.gc20.client.view.TUI;

public class LobbyListMenu implements MenuState{
    private final MenuContext menuContext;
    private final String stateName = "Lobby List Menu";

    public LobbyListMenu(MenuContext menuContext) {
        this.menuContext = menuContext;
    }

    public void displayMenu() {
        System.out.println("Available lobbies:");
        // Display the list of available lobbies
        //TODO get the list of lobbies from the server
    }


    public boolean handleInput(){
  return false;
    }

    public String getStateName() {
        return stateName;
    }

}
