package it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards;

import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;

import java.util.List;

@SuppressWarnings("unused") // The client uses this class to view planets in an adventure card
public class ViewPlanets extends ViewAdventureCard {

    List<int[]> planets; // red, yellow, green, blue
    int lostDays;

    /**
     * Constructs a new instance of ViewPlanets by initializing it with the provided AdventureCard.
     * The method extracts the planet rewards from the AdventureCard, organizes them into a format suitable for display,
     * and sets the lostDays property.
     *
     * @param adventureCard the AdventureCard object used to initialize the ViewPlanets instance.
     *                      It contains information about the planets, their rewards, and lost days.
     */
    public ViewPlanets(AdventureCard adventureCard) {
        super.initialize(adventureCard);
        this.planets = adventureCard.getPlanets().stream().map(planet -> {
            int[] planetReward = new int[4];
            for (int i = 0; i < planet.getReward().size(); i++) {
                switch (planet.getReward().get(i)) {
                    case RED -> planetReward[0]++;
                    case YELLOW -> planetReward[1]++;
                    case GREEN -> planetReward[2]++;
                    case BLUE -> planetReward[3]++;
                }
            }
            return planetReward;
        }).toList();
        this.lostDays = adventureCard.getLostDays();
    }

    public ViewPlanets() {
        super();
    }

    @Override
    public String toLine(int i) {
        String string = toString();
        String[] lines = string.split("\n");
        if (i < 0 || i >= lines.length) {
            return "";
        }
        return lines[i];
    }

    @Override
    public String toString(){
        return
        UP + "\n" +
                LATERAL + EMPTY_ROW + LATERAL + "\n" +
                LATERAL + "\u001B[1m       Planets        \u001B[0m" + LATERAL + "\n" +
                LATERAL + EMPTY_ROW + LATERAL + "\n" +
                planets() +
                LATERAL + "  LostDays: \u001B[31m" + lostDays + "\u001B[0m         " + LATERAL + "\n" +
                emptyRows() +
                DOWN;
    }

    /**
     * Constructs a formatted string representation of the planets and their respective rewards.
     * Each planet is displayed with its associated rewards, which are aligned for visual clarity.
     *
     * @return A string that represents the planets along with their rewards in a readable format.
     */
    private String planets(){
        StringBuilder result = new StringBuilder();

        for(int i=0; i<planets.size(); i++){
            result.append(LATERAL + "  ");
            result.append("\u001B[36mP").append(i+1).append("\u001B[0m: ");

            result.append(reward(planets.get(i)[0], planets.get(i)[1], planets.get(i)[2], planets.get(i)[3]));
            result.append(" ".repeat(16 - rewardSize(planets.get(i)[0], planets.get(i)[1], planets.get(i)[2], planets.get(i)[3])));
            result.append(LATERAL + "\n");
        }


        return result.toString();
    }

    /**
     * Constructs a formatted string representation of cargo rewards, using ANSI escape codes
     * for coloring the text. Each cargo type is represented with a specific color, and their counts
     * are concatenated into a single visually formatted string.
     *
     * @param redCargo the number of red cargo units to be displayed, represented in red color
     * @param yellowCargo the number of yellow cargo units to be displayed, represented in yellow color
     * @param greenCargo the number of green cargo units to be displayed, represented in green color
     * @param blueCargo the number of blue cargo units to be displayed, represented in blue color
     * @return A formatted string combining all cargo units, separated by spaces and colored appropriately
     */
    private String reward(int redCargo, int yellowCargo, int greenCargo, int blueCargo){
        StringBuilder result = new StringBuilder();
        int j = 0;
        for(int i = 0; i < redCargo; i++){
            if(i==0)
                result.append("\u001B[31mR\u001B[0m");
            else
                result.append("\u001B[31m R\u001B[0m");
            j++;
        }
        for(int i = 0; i < yellowCargo; i++){
            if(j==0)
                result.append("\u001B[33mY\u001B[0m");
            else
                result.append("\u001B[33m Y\u001B[0m");
            j++;
        }
        for(int i = 0; i < greenCargo; i++){
            if(j==0)
                result.append("\u001B[32mG\u001B[0m");
            else
                result.append("\u001B[32m G\u001B[0m");
            j++;
        }
        for(int i = 0; i < blueCargo; i++){
            if(j==0)
                result.append("\u001B[34mB\u001B[0m");
            else
                result.append("\u001B[34m B\u001B[0m");
            j++;
        }
        return result.toString();
    }

    /**
     * Calculates the size of a reward based on the quantities of different cargo types.
     *
     * @param redCargo the number of red cargo units
     * @param yellowCargo the number of yellow cargo units
     * @param greenCargo the number of green cargo units
     * @param blueCargo the number of blue cargo units
     * @return the calculated reward size, determined by combining the total cargo units
     *         and applying the formula (total - 1) * 2 + 1
     */
    private int rewardSize(int redCargo, int yellowCargo, int greenCargo, int blueCargo){
        int result = redCargo + yellowCargo + greenCargo + blueCargo;

        return (result-1)*2 + 1;
    }

    /**
     * Generates a string consisting of a number of empty rows formatted with
     * lateral borders and a newline. The number of rows is determined by the
     * difference between a fixed value (5) and the size of the `planets` list,
     * ensuring a non-negative count of rows.
     *
     * @return A string representation of empty rows, repeated based on the
     *         size of the `planets` list, formatted with lateral borders and newlines.
     */
    private String emptyRows(){
        return (LATERAL + EMPTY_ROW + LATERAL + "\n").repeat(Math.max(0, 5 - planets.size()));
    }
}
