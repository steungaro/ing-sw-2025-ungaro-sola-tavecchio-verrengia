package it.polimi.ingsw.gc20.client.view.common.localmodel.ship;

import it.polimi.ingsw.gc20.client.view.common.localmodel.components.*;

import java.util.List;

public class ViewShip {
    private static final String TOPPER = "╭" + "─".repeat(117) + "╮";
    private static final String BOTTOM = "╰" + "─".repeat(117) + "╯";
    private static final String SEPARATOR = "├" + "─".repeat(117) + "┤";
    public int baseFirepower;
    public int baseEnginePower;
    public int astronauts;
    public AlienColor aliens;

    private Component[][] components;
    private Component[] booked;
    private List<Component> waste;

    public Component getComponent(int i, int j) {
        return components[i][j];
    }

    public void setComponent(int i, int j, Component component) {
        components[i][j] = component;
    }

    public Component getBooked(int i) {
        return booked[i];
    }

    public void setBooked(int i, Component component) {
        booked[i] = component;
    }

    public List<Component> getWaste() {
        return waste;
    }

    public void setWaste(List<Component> waste) {
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


    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(TOPPER).append("\n");

        String line = "│       Base Firepower: " + baseFirepower +
                "       │       Base Engine Power: " + baseEnginePower +
                "       │       Astronauts: " + astronauts +
                "       │       Aliens: " + aliens.getColorChar();
        sb.append(formatFixedLength(line)).append("│\n");
        sb.append(SEPARATOR).append("\n");

        // For each textual row of the booked components
        for (int i = 0; i < 5; i++) {
            sb.append("│").append(" ".repeat(9)).append(i == 2 ? "Booked components" : " ".repeat(17)).append(" ".repeat(9));
            for (int j = 0; j < booked.length; j++) {
                if (booked[j] == null) {
                    sb.append(Component.coveredLine(i));
                } else {
                    sb.append(booked[j].toLine(i));
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

        sb.append(SEPARATOR).append("\n");

        // For each row in the components' matrix
        // For each textual row of the component
        // For each component in the row
        sb.append("│   ");
        for (int i = 0; i < 7; i++) {
            sb.append("       ").append(i + 4).append("        ");
        }
        sb.append(" │\n");
        for (int i = 0; i < components.length; i++) {
            for (int j = 0; j < 5; j++) {
                sb.append("│ ").append(j == 2 ? i + 5 : " ").append(" ");
                for (int k = 0; k < components[i].length; k++) {
                    if (components[i][k] == null) {
                        if ((i == 0 && (k == 0 || k == 1 || k == 3 || k == 5 || k == 6)) ||
                                (i == 4 && k == 3)) {
                            sb.append(" ".repeat(15));
                        } else {
                            sb.append(Component.coveredLine(j));
                        }

                    } else {
                        sb.append(components[i][k].toLine(j));
                    }
                    sb.append(" ");
                }
                sb.append(j == 2 ? i + 4 : " ").append(" │\n");
            }
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

        // Initialize components and booked
        viewShip.components = new Component[5][7];
        viewShip.booked = new Component[2];
        viewShip.waste = List.of(new CargoHold(), new StartingCabin(), new Engine(), new Shield(), new CargoHold(), new StartingCabin(), new Engine(), new Shield(), new CargoHold(), new StartingCabin(), new Engine(), new Shield());

        viewShip.booked[0] = new Shield();
        viewShip.booked[0].upConnectors = 3;
        viewShip.booked[0].downConnectors = 0;
        viewShip.booked[0].leftConnectors = 2;
        viewShip.booked[0].rightConnectors = 1;
        viewShip.booked[1] = new Battery();
        viewShip.booked[1].upConnectors = 0;
        viewShip.booked[1].downConnectors = 1;
        viewShip.booked[1].leftConnectors = 3;
        viewShip.booked[1].rightConnectors = 2;



        // Example components
        viewShip.components[2][3] = new StartingCabin();
        viewShip.components[2][3].downConnectors = 3;
        viewShip.components[2][3].upConnectors = 3;
        viewShip.components[2][3].leftConnectors = 3;
        viewShip.components[2][3].rightConnectors = 3;
        viewShip.components[2][0] = new Engine();
        viewShip.components[2][1] = new Engine();
        viewShip.components[2][1].upConnectors = 1;
        viewShip.components[2][4] = new Pipes();
        viewShip.components[2][4].leftConnectors = 1;
        viewShip.components[3][0] = new LifeSupport();
        viewShip.components[3][1] = new Cabin();
        viewShip.components[3][2] = new Cabin();


        System.out.println(viewShip);
    }

}
