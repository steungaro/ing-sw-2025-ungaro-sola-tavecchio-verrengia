package it.polimi.ingsw.gc20.client.view.TUI;

public class SwarnMenu implements MenuState{
    private final MenuContext menuContext;
    private final String stateName = "Swarn Menu";
    private String currentMeteor = null;
    public SwarnMenu(MenuContext menuContext) {
        this.menuContext = menuContext;
    }

    public void displayMenu() {
        if(currentMeteor == null) {
            System.out.println("You have been attacked by a swarm of meteors! You need to protect your ship from them");//TODO
            menuContext.getShip().view();
        }

    }

    public boolean handleInput(){
        return true;
    }

    public String getStateName() {
        return stateName;
    }
}
