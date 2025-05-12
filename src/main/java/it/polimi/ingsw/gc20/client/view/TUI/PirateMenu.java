package it.polimi.ingsw.gc20.client.view.TUI;

public class PirateMenu implements MenuState{
    private final MenuContext menuContext;
    private boolean pirateship = true;
    public PirateMenu(MenuContext menuContext) {
        this.menuContext = menuContext;
    }

    public void displayMenu() {
        if(pirateship) {
            System.out.println("You have been attacked by a pirate ship! You need to protect your ship from them");
            System.out.println(menuContext.getShip().toString());
            CannonsMenu cannonsMenu = new CannonsMenu(menuContext);
            cannonsMenu.display();
        } else{
            System.out.println("you have been defeated by the pirates");
            System.out.println("You are getting shot at, defend from enemy fire");

        }

    }

    public boolean handleInput() {
        // Handle user input for the pirate menu
        //TODO
        return true;
    }

    public String getStateName() {
        return "Pirate Menu";
    }

}
