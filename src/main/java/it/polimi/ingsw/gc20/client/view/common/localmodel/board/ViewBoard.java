package it.polimi.ingsw.gc20.client.view.common.localmodel.board;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ViewPlayer;
import it.polimi.ingsw.gc20.server.model.player.PlayerColor;

public class ViewBoard {
    private static final String TOPPER = "╭" + "─".repeat(117) + "╮";
    private static final String BOTTOM = "╰" + "─".repeat(117) + "╯";
    private static final String SEPARATOR = "├" + "─".repeat(117) + "┤";
    public final ViewPlayer[] positions = new ViewPlayer[20];  //position is to be intended as 0: top left, 7: top right, 10: bottom right, 17: bottom left
    public boolean isLearner;

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(TOPPER).append("\n");

        sb.append("│").append(" ".repeat(117)).append("│").append("\n");
        sb.append("│").append(" ".repeat(117)).append("│").append("\n");
        sb.append("│").append(" ".repeat(117)).append("│").append("\n");

        // Add the top row of the board
        sb.append("│").append(" ".repeat(10)).append("╭────╮       ".repeat(8)).append("   │").append("\n");

        sb.append("│").append(" ".repeat(10));
        for (int i = 0; i < 8; i++) {
            String filling = positions[i] != null ? " " + positions[i].playerColor.TUIPrint() + " "  : "    ";
            sb.append("│").append(filling).append("│       ");
        }
        sb.append("   │").append("\n");
        sb.append("│").append(" ".repeat(10)).append("╰────╯       ".repeat(8)).append("   │").append("\n");

        sb.append("│").append(" ".repeat(117)).append("│").append("\n");

        // Add the middle upper row of the board
        sb.append("│").append(" ".repeat(5)).append("╭────╮").append(" ".repeat(95)).append("╭────╮").append(" ".repeat(5)).append("│").append("\n");
        String leftFilling = positions[19] != null ? " " + positions[19].playerColor.TUIPrint() + " "  : "    ";
        String rightFilling = positions[8] != null ? " " + positions[8].playerColor.TUIPrint() + " "  : "    ";
        sb.append("│").append(" ".repeat(5)).append("│").append(leftFilling).append("│").append(" ".repeat(95)).append("│").append(rightFilling).append("│").append(" ".repeat(5)).append("│").append("\n");

        sb.append("│").append(" ".repeat(5)).append("╰────╯").append(" ".repeat(95)).append("╰────╯").append(" ".repeat(5)).append("│").append("\n");

        sb.append("│").append(" ".repeat(117)).append("│").append("\n");

        sb.append("│").append(" ".repeat(5)).append("╭────╮").append(" ".repeat(95)).append("╭────╮").append(" ".repeat(5)).append("│").append("\n");
        // Add the middle lower row of the board
        leftFilling = positions[18] != null ? " " + positions[18].playerColor.TUIPrint() + " "  : "    ";
        rightFilling = positions[9] != null ? " " + positions[9].playerColor.TUIPrint() + " "  : "    ";
        sb.append("│").append(" ".repeat(5)).append("│").append(leftFilling).append("│").append(" ".repeat(95)).append("│").append(rightFilling).append("│").append(" ".repeat(5)).append("│").append("\n");
        sb.append("│").append(" ".repeat(5)).append("╰────╯").append(" ".repeat(95)).append("╰────╯").append(" ".repeat(5)).append("│").append("\n");


        sb.append("│").append(" ".repeat(117)).append("│").append("\n");

        // Add the bottom row of the board

        sb.append("│").append(" ".repeat(10)).append("╭────╮       ".repeat(8)).append("   │").append("\n");

        sb.append("│").append(" ".repeat(10));
        for (int i = 10; i < 18; i++) {
            String filling = positions[i] != null ? " " + positions[i].playerColor.TUIPrint() + " "  : "    ";
            sb.append("│").append(filling).append("│       ");
        }
        sb.append("   │").append("\n");
        sb.append("│").append(" ".repeat(10)).append("╰────╯       ".repeat(8)).append("   │").append("\n");

        sb.append("│").append(" ".repeat(117)).append("│").append("\n");
        sb.append("│").append(" ".repeat(117)).append("│").append("\n");

        sb.append(BOTTOM).append("\n");

        return sb.toString();

    }

    public static void main(String[] args) {
        ViewBoard board = new ViewBoard();

        board.positions[1] = new ViewPlayer();
        board.positions[1].playerColor = PlayerColor.RED;

        board.positions[8] = new ViewPlayer();
        board.positions[8].playerColor = PlayerColor.BLUE;

        board.positions[17] = new ViewPlayer();
        board.positions[17].playerColor = PlayerColor.GREEN;

        board.positions[18] = new ViewPlayer();
        board.positions[18].playerColor = PlayerColor.YELLOW;

        System.out.println(board.toString());
    }
}
