package it.polimi.ingsw.gc20.client.view.TUI;


import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.server.model.cards.Planet;

import java.rmi.RemoteException;
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
        System.out.println("\u001B[1mPlanets Menu\u001B[22m");
        System.out.println("1. Land on a planet");
        System.out.println("2. Don't land on a planet");
        System.out.println("v. Viewing game options");
        System.out.print(" > ");
    }

    @Override
    public void displayMenu(String errorMessage) {
        System.out.println("\u001B[31m" + errorMessage + "\u001B[0m");
        displayMenu();
    }

    public void handleInput(String choice) throws RemoteException {
        // Handle user input from the planet menu
        ClientGameModel.getInstance().setBusy();
        switch (choice) {
            case "1":
                System.out.println("Available planets:");
                for (int i = 0; i < planets.size(); i++) {
                    System.out.println(" " + (i + 1) + ". " + planets.get(i));
                }
                int planetIndex;
                do {
                    System.out.println("Type the index of the planet you want to land on:");
                    System.out.print(" > ");
                    String planetInput = scanner.nextLine().trim();
                    if (planetInput.equals("q")) {
                        ClientGameModel.getInstance().shutdown();
                        return;
                    }
                    try {
                        planetIndex = Integer.parseInt(planetInput) - 1;
                    } catch (NumberFormatException e) {
                        System.out.println("\u001B[31mInvalid input. Please enter a valid integer.\u001B[0m");
                        planetIndex = -1;
                    }
                } while (planetIndex < 0 || planetIndex >= planets.size());
                ClientGameModel.getInstance().getClient().landOnPlanet(username, planetIndex);
                break;
            case "2":
                ClientGameModel.getInstance().getClient().endMove(username);
                break;
            case "q":
                ClientGameModel.getInstance().shutdown();
                break;
            case "v":
                TUI.viewOptionsMenu();
            default:
                System.out.println("\u001B[31mInvalid input. Please try again.\u001B[0m");
        }
        ClientGameModel.getInstance().setFree();
    }

    public String getStateName() {
        return "Planet Menu";
    }
}
