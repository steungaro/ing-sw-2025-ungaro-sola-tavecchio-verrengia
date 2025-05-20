package it.polimi.ingsw.gc20.client.view.TUI;


import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.server.model.cards.Planet;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;


public class PlanetMenu implements MenuState{
    private final Scanner scanner = new Scanner(System.in);
    private final String username = ClientGameModel.getInstance().getUsername();
    private final List<Planet> planets;

    public PlanetMenu(List<Planet> planets) {
        this.planets = planets;
    }

    public void displayMenu() {
        TUI.clearConsole();
        System.out.println("Planet Menu");
        System.out.println("1. Land on a planet");
        System.out.println("2. Don't land on a planet");
        System.out.print(" > ");
    }

    public boolean handleInput() throws IOException {
        String choice = scanner.nextLine().trim();
        // Handle user input for the planet menu
        switch (choice) {
            case "1":
                System.out.println("Available planets:");
                for (int i = 0; i < planets.size(); i++) {
                    System.out.println(" " + (i + 1) + ". " + planets.get(i));
                }
                System.out.println("Type the index of the planet you want to land on:");
                System.out.print(" > ");
                String planetInput = scanner.nextLine().trim();
                int planetIndex = Integer.parseInt(planetInput) - 1;
                ClientGameModel.getInstance().getClient().landOnPlanet(username, planetIndex);
                break;
            case "2":
                ClientGameModel.getInstance().getClient().endMove(username);
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
        return "Planet Menu";
    }
}
