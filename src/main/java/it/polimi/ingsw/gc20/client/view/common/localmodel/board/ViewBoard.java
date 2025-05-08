package it.polimi.ingsw.gc20.client.view.common.localmodel.board;

import java.util.Map;

public class ViewBoard {
    private static final String TOPPER = "╭" + "─".repeat(117) + "╮";
    private static final String BOTTOM = "╰" + "─".repeat(117) + "╯";
    private static final String SEPARATOR = "├" + "─".repeat(117) + "┤";
    private Map<String, Integer> positions;
    public boolean isLearner;

    public int getPosition(String player) {
        return positions.get(player);
    }

    public void setPosition(String player, int position) {
        positions.put(player, position);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(TOPPER).append("\n");

        sb.append("│").append(" ".repeat(117)).append("│").append("\n");
        sb.append("│").append(" ".repeat(117)).append("│").append("\n");
        sb.append("│").append(" ".repeat(117)).append("│").append("\n");

        sb.append("│").append(" ".repeat(10)).append("╭────╮       ".repeat(8)).append("   │").append("\n");
        sb.append("│").append(" ".repeat(10)).append("│    │       ".repeat(8)).append("   │").append("\n");
        sb.append("│").append(" ".repeat(10)).append("╰────╯       ".repeat(8)).append("   │").append("\n");

        sb.append("│").append(" ".repeat(117)).append("│").append("\n");

        sb.append("│").append(" ".repeat(5)).append("╭────╮").append(" ".repeat(95)).append("╭────╮").append(" ".repeat(5)).append("│").append("\n");
        sb.append("│").append(" ".repeat(5)).append("│    │").append(" ".repeat(95)).append("│    │").append(" ".repeat(5)).append("│").append("\n");
        sb.append("│").append(" ".repeat(5)).append("╰────╯").append(" ".repeat(95)).append("╰────╯").append(" ".repeat(5)).append("│").append("\n");

        sb.append("│").append(" ".repeat(117)).append("│").append("\n");


        sb.append("│").append(" ".repeat(5)).append("╭────╮").append(" ".repeat(95)).append("╭────╮").append(" ".repeat(5)).append("│").append("\n");
        sb.append("│").append(" ".repeat(5)).append("│    │").append(" ".repeat(95)).append("│    │").append(" ".repeat(5)).append("│").append("\n");
        sb.append("│").append(" ".repeat(5)).append("╰────╯").append(" ".repeat(95)).append("╰────╯").append(" ".repeat(5)).append("│").append("\n");

        sb.append("│").append(" ".repeat(117)).append("│").append("\n");

        sb.append("│").append(" ".repeat(10)).append("╭────╮       ".repeat(8)).append("   │").append("\n");
        sb.append("│").append(" ".repeat(10)).append("│    │       ".repeat(8)).append("   │").append("\n");
        sb.append("│").append(" ".repeat(10)).append("╰────╯       ".repeat(8)).append("   │").append("\n");

        sb.append("│").append(" ".repeat(117)).append("│").append("\n");
        sb.append("│").append(" ".repeat(117)).append("│").append("\n");

        sb.append(BOTTOM).append("\n");

        return sb.toString();

    }

    public static void main(String[] args) {
        ViewBoard board = new ViewBoard();
        System.out.println(board.toString());
    }
}
