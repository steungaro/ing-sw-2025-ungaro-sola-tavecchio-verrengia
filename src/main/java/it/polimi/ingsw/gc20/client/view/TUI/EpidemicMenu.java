package it.polimi.ingsw.gc20.client.view.TUI;

public class EpidemicMenu implements MenuState{
    private final MenuContext menuContext;
    private final String stateName = "Epidemic Menu";

    public EpidemicMenu(MenuContext menuContext) {
        this.menuContext = menuContext;
    }

    public void displayMenu() {
        System.out.println("An epidemic has occurred in your ship! This is the aftermath:");
        menuContext.getShip().view();
    }

    public boolean handleInput(){
        return true;
    }

    public String getStateName() {
        return stateName;
    }
}
