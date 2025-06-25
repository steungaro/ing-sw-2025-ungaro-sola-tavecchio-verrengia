package it.polimi.ingsw.gc20.client.view.common.localmodel.ship;

import it.polimi.ingsw.gc20.client.view.common.localmodel.components.*;
import it.polimi.ingsw.gc20.server.model.components.AlienColor;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ViewShip {
    /* -------------------------------------------------
       Listener infrastructure
       ------------------------------------------------- */
    public interface Listener {
        void onShipChanged(ViewShip ship);
    }

    private final transient List<Listener> listeners = new CopyOnWriteArrayList<>();

    public void addListener(Listener l) {
        if (l != null) listeners.add(l);
    }

    public void removeListener(Listener l) {
        listeners.remove(l);
    }

    private void fireChange() {
        for (Listener l : listeners) {
            l.onShipChanged(this);
        }
    }

    /* -------------------------------------------------
       ViewShip
       ------------------------------------------------- */
    private static final String TOPPER = "╭" + "─".repeat(117) + "╮";
    private static final String BOTTOM = "╰" + "─".repeat(117) + "╯";
    private static final String SEPARATOR = "├" + "─".repeat(117) + "┤";
    public float baseFirepower;
    public int baseEnginePower;
    public int astronauts;
    public AlienColor aliens;
    public boolean isLearner;

    private boolean isValid;
    private ViewComponent[][] components;
    private final ViewComponent[] booked;
    private List<ViewComponent> waste;

    /**
     * Default constructor for ViewShip.
     * Initializes the ship with default values.
     */
    public ViewShip() {
        this.baseFirepower = 0;
        this.baseEnginePower = 0;
        this.astronauts = 0;
        this.aliens = AlienColor.NONE;
        this.isLearner = false;
        this.isValid = false;
        this.components = new ViewComponent[5][7];
        this.booked = new ViewComponent[2];
        this.waste = new ArrayList<>();
    }

    /**
     * Sets the validity of the ship.
     *
     * @param isValid true if the ship is valid, false otherwise.
     */
    public void setValid(boolean isValid) {
        this.isValid = isValid;
        fireChange();
    }

    /**
     * Sets the components of the ship.
     *
     * @param components A 2D array of ViewComponent representing the ship's components.
     */
    public void setComponents(ViewComponent[][] components) {
        this.components = components;
        fireChange();
    }

    /**
     * Returns whether the ship is valid.
     * @return true if the ship is valid, false otherwise.
     */
    public boolean isValid() {
        return isValid;
    }

    /**
     * Returns the components of the ship.
     * This method provides access to the component array.
     *
     * @return A 2D array of ViewComponent representing the ship's components.
     */
    public ViewComponent getComponent(int i, int j) {
        return components[i][j];
    }

    /**
     * Returns the booked component at the specified index.
     * This method provides access to the booked components array.
     *
     * @param i The index of the booked component to retrieve.
     * @return The ViewComponent at the specified index in the booked array.
     */
    public ViewComponent getBooked(int i) {
        return booked[i];
    }

    /**
     * Sets a booked component on the ship.
     * This method updates the booked component at the specified index and notifies listeners of the change.
     *
     * @param i The index of the booked component to set.
     * @param component The ViewComponent to set as booked.
     */
    public void setBooked(int i, ViewComponent component) {
        booked[i] = component;
        fireChange();
    }

    /**
     * Returns the list of booked components in the ship.
     * This method provides access to the booked components array.
     *
     * @return An array of booked components.
     */
    public List<ViewComponent> getWaste() {
        return waste;
    }

    /**
     * Sets the waste list of components in the ship.
     * This method updates the waste list and notifies listeners of the change.
     *
     * @param waste The new list of components in waste.
     */
    public void setWaste(List<ViewComponent> waste) {
        this.waste = waste;
        fireChange();
    }

    /**
     * Formats a line of text to a fixed length of 118 characters, ensuring that ANSI escape sequences are handled correctly.
     * If the line is shorter than 118 characters, it pads with spaces. If it's longer, it truncates while preserving ANSI codes.
     *
     * @param lineContent The content of the line to format.
     * @return A formatted string with a fixed length of 118 characters.
     */
    private String formatFixedLength(String lineContent) {
        int targetLength = 118; // Desired fixed length
        String strippedContent = lineContent.replaceAll("\u001B\\[[;\\d]*m", ""); // Remove ANSI escape sequences
        int currentLength = strippedContent.length();

        if (currentLength < targetLength) {
            // Add padding spaces to reach the target length
            return lineContent + " ".repeat(targetLength - currentLength);
        } else if (currentLength > targetLength) {
            // If content exceeds the target length, truncate while considering ANSI codes
            String truncatedContent = strippedContent.substring(0, targetLength);
            int visibleLength = truncatedContent.length();
            // Include the matching ANSI codes from the original content
            return lineContent.substring(0, lineContent.indexOf(truncatedContent) + visibleLength);
        }

        return lineContent; // Already at the correct length
    }

    /**
     * Returns a string representation of the ship's components in a level 2 layout.
     * It includes the components arranged in rows and columns, with appropriate formatting.
     *
     * @return A formatted string representing the ship's components in a level 2 layout.
     */
    private String level2Ship() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < components.length; i++) {
            for (int j = 0; j < 5; j++) {
                sb.append("│ ").append(j == 2 ? i + 5 : " ").append(" ");
                for (int k = 0; k < components[i].length; k++) {
                    if (components[i][k] == null) {
                        if ((i == 0 && (k == 0 || k == 1 || k == 3 || k == 5 || k == 6)) ||
                                (i == 4 && k == 3) ||
                                (i == 1 && (k == 0 || k == 6))) {
                            sb.append(" ".repeat(15));
                        } else {
                            sb.append(ViewComponent.coveredLine(j));
                        }

                    } else {
                        sb.append(components[i][k].toLine(j));
                    }
                    sb.append(" ");
                }
                sb.append(j == 2 ? i + 5 : " ").append(" │\n");
            }
        }
        return sb.toString();
    }

    /**
     * Returns a string representation of the ship's components in a level learner layout.
     * It includes the components arranged in rows and columns, with appropriate formatting.
     *
     * @return A formatted string representing the ship's components in a level learner layout.
     */
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
                sb.append(j == 2 ? i + 5 : " ").append(" │\n");
            }
        }
        return sb.toString();
    }

    /**
     * Returns a string representation of the booked components and waste section of the ship.
     * It includes the booked components in a formatted manner and the number of components in waste.
     *
     * @return A formatted string representing the booked components and waste section.
     */
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

    /**
     * Returns a string representation of the waste section of the ship.
     * It includes the number of components in waste and formats the output accordingly.
     *
     * @return A formatted string representing the waste section.
     */
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


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(TOPPER).append("\n");

        String line = "│        Base Firepower: " + baseFirepower +
                "      │      Base Engine Power: " + baseEnginePower +
                "      │      Astronauts: " + astronauts +
                "      │      Aliens: " + aliens.getColorChar();
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

    /**
     * Returns a list of triplets representing the coordinates and number of occupants in the cabins of the ship.
     * Each triplet contains the row index, column index, and number of occupants in the cabin.
     *
     * @return List of triplets containing coordinates and occupants in cabins.
     */
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

    /**
     * Returns a list of triplets representing the coordinates and color of the cargo in the ship.
     * Each triplet contains the row index, column index, and color of the cargo.
     *
     * @return List of triplets containing coordinates and color of cargo.
     */
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

    /**
     * Returns a list of pairs representing the coordinates of the cannons in the ship.
     * Each pair contains the row and column indices of an engine component.
     * Only cannons with power level 2 are included.
     *
     * @return List of pairs of integers representing the coordinates of cannons.
     */
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

    /**
     * Returns a list of pairs representing the coordinates of the engines in the ship.
     * Each pair contains the row and column indices of an engine component.
     *
     * @return List of pairs of integers representing the coordinates of engines.
     */
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

    /**
     * Returns a list of pairs representing the coordinates of the batteries in the ship.
     * Each pair contains the row and column indices of a battery component.
     *
     * @return List of pairs of integers representing the coordinates of batteries.
     */
    public List<Pair<Integer, Integer>> getBatteries() {
        List<Pair<Integer, Integer>> batteries = new ArrayList<>();
        for (int i = 0; i < components.length; i++) {
            for (int j = 0; j < components[i].length; j++) {
                if (components[i][j].isBattery()) {
                    Pair<Integer, Integer> pair = new Pair<>(i, j);
                    batteries.add(pair);
                }
            }
        }
        return batteries;
    }
}