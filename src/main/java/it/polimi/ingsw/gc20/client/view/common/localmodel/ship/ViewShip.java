package it.polimi.ingsw.gc20.client.view.common.localmodel.ship;

import it.polimi.ingsw.gc20.client.view.common.localmodel.components.*;
import it.polimi.ingsw.gc20.server.model.components.AlienColor;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.util.ArrayList;
import java.util.List;

public class ViewShip {
    private static final String TOPPER = "╭" + "─".repeat(117) + "╮";
    private static final String BOTTOM = "╰" + "─".repeat(117) + "╯";
    private static final String SEPARATOR = "├" + "─".repeat(117) + "┤";
    public int baseFirepower;
    public int baseEnginePower;
    public int astronauts;
    public AlienColor aliens;
    public boolean isLearner;

    private boolean isValid;
    private ViewComponent[][] components;
    private ViewComponent[] booked;
    private List<ViewComponent> waste;

    public void setValid(boolean isValid) {
        this.isValid = isValid;
    }

    public boolean isValid() {
        return isValid;
    }

    public ViewComponent getComponent(int i, int j) {
        return components[i][j];
    }

    public void setComponent(int i, int j, ViewComponent component) {
        components[i][j] = component;
    }

    public ViewComponent getBooked(int i) {
        return booked[i];
    }

    public void setBooked(int i, ViewComponent component) {
        booked[i] = component;
    }

    public List<ViewComponent> getWaste() {
        return waste;
    }

    public void setWaste(List<ViewComponent> waste) {
        this.waste = waste;
    }

    private String formatFixedLength(String lineContent) {
        int targetLength = 118; // Desired fixed length
        int currentLength = lineContent.length();
        if (currentLength < targetLength) {
            // Add padding spaces to reach 120 characters
            return lineContent + " ".repeat(targetLength - currentLength);
        } else if (currentLength > targetLength) {
            // If content exceeds, truncate it to fit
            return lineContent.substring(0, targetLength);
        }
        return lineContent; // Already the correct length
    }

    private String level2Ship() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < components.length; i++) {
            for (int j = 0; j < 5; j++) {
                sb.append("│ ").append(j == 2 ? i + 5 : " ").append(" ");
                for (int k = 0; k < components[i].length; k++) {
                    if (components[i][k] == null) {
                        if ((i == 0 && (k == 0 || k == 1 || k == 3 || k == 5 || k == 6)) ||
                                (i == 4 && k == 3)) {
                            sb.append(" ".repeat(15));
                        } else {
                            sb.append(ViewComponent.coveredLine(j));
                        }

                    } else {
                        sb.append(components[i][k].toLine(j));
                    }
                    sb.append(" ");
                }
                sb.append(j == 2 ? i + 4 : " ").append(" │\n");
            }
        }
        return sb.toString();
    }

    private String levelLShip() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < components.length; i++) {
            for (int j = 0; j < 5; j++) {
                sb.append("│ ").append(j == 2 ? i + 5 : " ").append(" ");
                for (int k = 0; k < components[i].length; k++) {
                    if (components[i][k] == null) {
                        if (    k == 0 || // First column
                                k == components[i].length - 1 || // Last column
                                ((i == 0) && (k == 1 || k == 2 || k ==4 || k == 5)) ||
                                (i == 1 && (k == 1 || k == 5)) ||
                                (i == components.length - 1 && k== 3)
                                ) {
                            sb.append(" ".repeat(15));
                        } else {
                            sb.append(ViewComponent.coveredLine(j));
                        }

                    } else {
                        sb.append(components[i][k].toLine(j));
                    }
                    sb.append(" ");
                }
                sb.append(j == 2 ? i + 4 : " ").append(" │\n");
            }
        }
        return sb.toString();
    }

    private String bookedAndWaste() {
        StringBuilder sb = new StringBuilder();
        // For each textual row of the booked components
        for (int i = 0; i < 5; i++) {
            sb.append("│").append(" ".repeat(9)).append(i == 2 ? "Booked components" : " ".repeat(17)).append(" ".repeat(9));
            for (ViewComponent component : booked) {
                if (component == null) {
                    sb.append(ViewComponent.coveredLine(i));
                } else {
                    sb.append(component.toLine(i));
                }
                sb.append(" ");
            }
            if (i == 2) {
                sb.append(" ".repeat(13)).append("Components in waste: ").append(waste == null ? "0" : waste.size()).append(" ".repeat(14));
                if (waste.size() >= 10) {
                    sb.deleteCharAt(sb.length() - 1);
                }
                if (waste.size() >= 100) {
                    sb.deleteCharAt(sb.length() - 2);
                }
            } else {
                sb.append(" ".repeat(49));
            }
            sb.append(" │\n");
        }
        return sb.toString();
    }

    private String waste() {
        StringBuilder sb = new StringBuilder();
        // For each textual row of the booked components
        sb.append("│").append(" ".repeat(47)).append("Components in waste: ").append(waste == null ? "0" : waste.size()).append(" ".repeat(48));
        if (waste.size() >= 10) {
            sb.deleteCharAt(sb.length() - 1);
        }
        if (waste.size() >= 100) {
            sb.deleteCharAt(sb.length() - 2);
        }
        sb.append("│\n");
        return sb.toString();
    }


    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(TOPPER).append("\n");

        String line = "│       Base Firepower: " + baseFirepower +
                "       │       Base Engine Power: " + baseEnginePower +
                "       │       Astronauts: " + astronauts +
                "       │       Aliens: " + aliens.getColorChar();
        sb.append(formatFixedLength(line)).append("│\n");
        sb.append(SEPARATOR).append("\n");

        if (isLearner) {
            sb.append(waste());
        } else {
            sb.append(bookedAndWaste());
        }

        sb.append(SEPARATOR).append("\n");

        sb.append("│   ");
        for (int i = 0; i < 7; i++) {
            sb.append("       ").append(i + 4).append("        ");
        }
        sb.append(" │\n");

        if (isLearner) {
            sb.append(levelLShip());
        } else {
            sb.append(level2Ship());
        }
        sb.append("│   ");
        for (int i = 0; i < 7; i++) {
            sb.append("       ").append(i + 4).append("        ");
        }
        sb.append(" │\n");
        sb.append(BOTTOM);
        return sb.toString();
    }

    public static void main(String[] args) {
        ViewShip viewShip = new ViewShip();
        viewShip.baseFirepower = 5;
        viewShip.baseEnginePower = 9;
        viewShip.astronauts = 3;
        viewShip.aliens = AlienColor.PURPLE;
        viewShip.isLearner = false;

        // Initialize components and booked
        viewShip.components = new ViewComponent[5][7];
        viewShip.booked = new ViewComponent[2];
        viewShip.waste = List.of(new ViewCargoHold(), new ViewStartingCabin(), new ViewEngine(), new ViewShield(), new ViewCargoHold(), new ViewStartingCabin(), new ViewEngine(), new ViewShield(), new ViewCargoHold(), new ViewStartingCabin(), new ViewEngine(), new ViewShield());

        viewShip.booked[0] = new ViewShield();
        viewShip.booked[0].upConnectors = 3;
        viewShip.booked[0].downConnectors = 0;
        viewShip.booked[0].leftConnectors = 2;
        viewShip.booked[0].rightConnectors = 1;
        viewShip.booked[1] = new ViewBattery();
        viewShip.booked[1].upConnectors = 0;
        viewShip.booked[1].downConnectors = 1;
        viewShip.booked[1].leftConnectors = 3;
        viewShip.booked[1].rightConnectors = 2;


        // Example components
        viewShip.components[2][3] = new ViewStartingCabin();
        viewShip.components[2][3].downConnectors = 3;
        viewShip.components[2][3].upConnectors = 3;
        viewShip.components[2][3].leftConnectors = 3;
        viewShip.components[2][3].rightConnectors = 3;
        viewShip.components[2][0] = new ViewEngine();
        viewShip.components[2][1] = new ViewEngine();
        viewShip.components[2][1].upConnectors = 1;
        viewShip.components[2][4] = new ViewPipes();
        viewShip.components[2][4].leftConnectors = 1;
        viewShip.components[3][0] = new ViewLifeSupport();
        viewShip.components[3][1] = new ViewCabin();
        viewShip.components[3][2] = new ViewCabin();


        System.out.println(viewShip);

        viewShip = new ViewShip();
        viewShip.baseFirepower = 5;
        viewShip.baseEnginePower = 9;
        viewShip.astronauts = 3;
        viewShip.aliens = AlienColor.PURPLE;
        viewShip.isLearner = true;

        // Initialize components and booked
        viewShip.components = new ViewComponent[5][7];
        viewShip.booked = new ViewComponent[2];
        viewShip.waste = List.of(new ViewCargoHold(), new ViewStartingCabin(), new ViewEngine(), new ViewShield(), new ViewCargoHold(), new ViewStartingCabin(), new ViewEngine(), new ViewShield(), new ViewCargoHold(), new ViewStartingCabin(), new ViewEngine(), new ViewShield());

        System.out.println(viewShip);
    }

    
    public List<Triplet<Integer, Integer, Integer>> getCrew() {
        List<Triplet<Integer, Integer, Integer>> crew = new ArrayList<>();
        for (int i = 0; i < components.length; i++) {
            for (int j = 0; j < components[i].length; j++) {
                if (components[i][j].isCabin()) {
                    Triplet<Integer, Integer, Integer> triplet = new Triplet<>(i, j, ((ViewCabin) components[i][j]).getOccupants());
                    crew.add(triplet);
                }
            }
        }
        return crew;
    }

    public List<Triplet<Integer, Integer, CargoColor>> getCargo() {
        List<Triplet<Integer, Integer, CargoColor>> cargo = new ArrayList<>();
        for (int i = 0; i < components.length; i++) {
            for (int j = 0; j < components[i].length; j++) {
                if (components[i][j].isCargoHold()) {
                    for (int k = 0; k < ((ViewCargoHold) components[i][j]).getCargo().size(); k++) {
                        Triplet<Integer, Integer, CargoColor> triplet = new Triplet<>(i, j, ((ViewCargoHold) components[i][j]).getCargo().get(k));
                        cargo.add(triplet);
                    }
                }
            }
        }
        return cargo;
    }

    public List<Pair<Integer, Integer>> getCannons() {
        List<Pair<Integer, Integer>> cannons = new ArrayList<>();
        for (int i = 0; i < components.length; i++) {
            for (int j = 0; j < components[i].length; j++) {
                if (components[i][j].isCannon() && ((ViewCannon)components[i][j]).power == 2) {
                    Pair<Integer, Integer> pair = new Pair<>(i, j);
                    cannons.add(pair);
                }
            }
        }
        return cannons;
    }

    public List<Pair<Integer, Integer>> getEngines() {
        List<Pair<Integer, Integer>> engines = new ArrayList<>();
        for (int i = 0; i < components.length; i++) {
            for (int j = 0; j < components[i].length; j++) {
                if (components[i][j].isEngine() && ((ViewEngine)components[i][j]).power == 2) {
                    Pair<Integer, Integer> pair = new Pair<>(i, j);
                    engines.add(pair);
                }
            }
        }
        return engines;
    }
}
