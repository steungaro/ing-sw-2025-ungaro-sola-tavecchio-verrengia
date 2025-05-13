package it.polimi.ingsw.gc20.client.view.common.localmodel.board;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ViewPlayer;
import it.polimi.ingsw.gc20.server.model.player.PlayerColor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ViewBoard {
    private static final String TOPPER = "╭" + "─".repeat(118) + "╮";
    private static final String BOTTOM = "╰" + "─".repeat(118) + "╯";
    private static final String SEPARATOR = "├" + "─".repeat(118) + "┤";
    private static final String EMPTY_ROW = "│" + " ".repeat(118) + "│";
    public ViewPlayer[] players;
    public boolean isLearner;
    public boolean assemblingState;
    public boolean[] deckAvailability = new boolean[3];
    public int remainingTime;

    public ViewBoard(boolean isLearner, ViewPlayer[] players) {
        this.isLearner = isLearner;
        Arrays.fill(deckAvailability, true);
        this.players = Arrays.copyOf(players, players.length);
    }

    public String learnerPrint() {
        Map<Integer, ViewPlayer> positions = new HashMap<>();
        for (int i = 0; i < players.length; i++) {
            if (players[i] != null) {
                positions.put(i, players[i]);
            }
        }
        StringBuilder sb = new StringBuilder();
        // Add the top row of the board
        sb.append("│").append(" ".repeat(11)).append("╭────╮         ".repeat(7)).append("  │").append("\n");

        sb.append("│").append(" ".repeat(11));
        for (int i = 0; i < 7; i++) {
            String filling = positions.containsKey(i) ? " " + positions.get(i).playerColor.TUIPrint() + " "  : "    ";
            sb.append("│").append(filling).append("│   ").append(i == 6 ? "  " : "→→").append("    ");
        }
        sb.append("  │").append("\n");
        sb.append("│").append(" ".repeat(11)).append("╰────╯         ".repeat(7)).append("  │").append("\n");
        sb.append("│").append(" ".repeat(13)).append("↑↑").append(" ".repeat(88)).append("↓↓").append(" ".repeat(13)).append("│").append("\n");
        // Add the middle upper row of the board
        sb.append("│").append(" ".repeat(11)).append("╭────╮").append(" ".repeat(84)).append("╭────╮").append(" ".repeat(11)).append("│").append("\n");
        String leftFilling = positions.containsKey(19) ? " " + positions.get(19).playerColor.TUIPrint() + " "  : "    ";
        String rightFilling = positions.containsKey(8) ? " " + positions.get(8).playerColor.TUIPrint() + " "  : "    ";
        sb.append("│").append(" ".repeat(11)).append("│").append(leftFilling).append("│").append(" ".repeat(84)).append("│").append(rightFilling).append("│").append(" ".repeat(11)).append("│").append("\n");

        sb.append("│").append(" ".repeat(11)).append("╰────╯").append(" ".repeat(84)).append("╰────╯").append(" ".repeat(11)).append("│").append("\n");

        sb.append("│").append(" ".repeat(13)).append("↑↑").append(" ".repeat(88)).append("↓↓").append(" ".repeat(13)).append("│").append("\n");

        sb.append("│").append(" ".repeat(11)).append("╭────╮").append(" ".repeat(84)).append("╭────╮").append(" ".repeat(11)).append("│").append("\n");
        // Add the middle lower row of the board
        leftFilling = positions.containsKey(18) ? " " + positions.get(18).playerColor.TUIPrint() + " "  : "    ";
        rightFilling = positions.containsKey(9) ? " " + positions.get(9).playerColor.TUIPrint() + " "  : "    ";
        sb.append("│").append(" ".repeat(11)).append("│").append(leftFilling).append("│").append(" ".repeat(84)).append("│").append(rightFilling).append("│").append(" ".repeat(11)).append("│").append("\n");
        sb.append("│").append(" ".repeat(11)).append("╰────╯").append(" ".repeat(84)).append("╰────╯").append(" ".repeat(11)).append("│").append("\n");
        sb.append("│").append(" ".repeat(13)).append("↑↑").append(" ".repeat(88)).append("↓↓").append(" ".repeat(13)).append("│").append("\n");

        // Add the bottom row of the board

        sb.append("│").append(" ".repeat(11)).append("╭────╮         ".repeat(7)).append("  │").append("\n");

        sb.append("│").append(" ".repeat(11));
        for (int i = 10; i < 17; i++) {
            String filling = positions.containsKey(i) ? " " + positions.get(i).playerColor.TUIPrint() + " "  : "    ";
            sb.append("│").append(filling).append("│   ").append(i == 16 ? "  " : "←←").append("    ");
        }
        sb.append("  │").append("\n");
        sb.append("│").append(" ".repeat(11)).append("╰────╯         ".repeat(7)).append("  │").append("\n");

        return sb.toString();
    }

    public String normalPrint() {
        Map<Integer, ViewPlayer> positions = new HashMap<>();
        for (int i = 0; i < players.length; i++) {
            if (players[i] != null) {
                positions.put(i, players[i]);
            }
        }
        StringBuilder sb = new StringBuilder();
        // Add the top row of the board
        sb.append("│").append(" ".repeat(11)).append("╭────╮    ".repeat(10)).append("       │").append("\n");

        sb.append("│").append(" ".repeat(11));
        for (int i = 0; i < 10; i++) {
            String filling = positions.containsKey(i) ? " " + positions.get(i).playerColor.TUIPrint() + " "  : "    ";
            sb.append("│").append(filling).append("│ ").append(i == 9 ? "  " : "→→").append(" ");
        }
        sb.append("       │").append("\n");
        sb.append("│").append(" ".repeat(11)).append("╰────╯    ".repeat(10)).append("       │").append("\n");
        sb.append("│").append(" ".repeat(13)).append("↑↑").append(" ".repeat(88)).append("↓↓").append(" ".repeat(13)).append("│").append("\n");
        // Add the middle upper row of the board
        sb.append("│").append(" ".repeat(11)).append("╭────╮").append(" ".repeat(84)).append("╭────╮").append(" ".repeat(11)).append("│").append("\n");
        String leftFilling = positions.containsKey(23) ? " " + positions.get(23).playerColor.TUIPrint() + " "  : "    ";
        String rightFilling = positions.containsKey(10) ? " " + positions.get(10).playerColor.TUIPrint() + " "  : "    ";
        sb.append("│").append(" ".repeat(11)).append("│").append(leftFilling).append("│").append(" ".repeat(84)).append("│").append(rightFilling).append("│").append(" ".repeat(11)).append("│").append("\n");

        sb.append("│").append(" ".repeat(11)).append("╰────╯").append(" ".repeat(84)).append("╰────╯").append(" ".repeat(11)).append("│").append("\n");

        sb.append("│").append(" ".repeat(13)).append("↑↑").append(" ".repeat(88)).append("↓↓").append(" ".repeat(13)).append("│").append("\n");

        sb.append("│").append(" ".repeat(11)).append("╭────╮").append(" ".repeat(84)).append("╭────╮").append(" ".repeat(11)).append("│").append("\n");
        // Add the middle lower row of the board
        leftFilling = positions.containsKey(22) ? " " + positions.get(22).playerColor.TUIPrint() + " "  : "    ";
        rightFilling = positions.containsKey(11) ? " " + positions.get(11).playerColor.TUIPrint() + " "  : "    ";
        sb.append("│").append(" ".repeat(11)).append("│").append(leftFilling).append("│").append(" ".repeat(84)).append("│").append(rightFilling).append("│").append(" ".repeat(11)).append("│").append("\n");
        sb.append("│").append(" ".repeat(11)).append("╰────╯").append(" ".repeat(84)).append("╰────╯").append(" ".repeat(11)).append("│").append("\n");
        sb.append("│").append(" ".repeat(13)).append("↑↑").append(" ".repeat(88)).append("↓↓").append(" ".repeat(13)).append("│").append("\n");


        // Add the bottom row of the board

        sb.append("│").append(" ".repeat(11)).append("╭────╮    ".repeat(10)).append("       │").append("\n");

        sb.append("│").append(" ".repeat(11));
        for (int i = 12; i < 22; i++) {
            String filling = positions.containsKey(i) ? " " + positions.get(i).playerColor.TUIPrint() + " "  : "    ";
            sb.append("│").append(filling).append("│ ").append(i == 21 ? "  " : "←←").append(" ");;
        }
        sb.append("       │").append("\n");
        sb.append("│").append(" ".repeat(11)).append("╰────╯    ".repeat(10)).append("       │").append("\n");

        return sb.toString();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(TOPPER).append("\n");

        sb.append(EMPTY_ROW).append("\n");
        sb.append("│").append(" ".repeat(29)).append("       Player 1       ").append("       Player 2       ").append("       Player 3       ").append("       Player 4       ").append(" ".repeat(29)).append("│").append("\n");
        sb.append(EMPTY_ROW).append("\n");
        sb.append(isLearner ? learnerPrint() : normalPrint());

        sb.append(EMPTY_ROW).append("\n");
        sb.append(EMPTY_ROW).append("\n");


        if (!isLearner && assemblingState) {
            sb.append(SEPARATOR).append("\n");
            sb.append(EMPTY_ROW).append("\n");

            sb.append("│").append(" ".repeat(29)).append("       Deck 1       ").append("       Deck 2       ").append("       Deck 3       ").append(" ".repeat(29)).append("│").append("\n");
            sb.append("│").append(" ".repeat(29)).append("     ").append(deckAvailability[0] ? " Available " : "Unavailable").append("     ").append("    ").append(deckAvailability[1] ? " Available " : "Unavailable").append("     ").append("    ").append(deckAvailability[2] ? " Available " : "Unavailable").append("    ").append(" ".repeat(29)).append("│").append("\n");
        }
        sb.append(EMPTY_ROW).append("\n");

        sb.append(BOTTOM).append("\n");

        return sb.toString();

    }

    public static void main(String[] args) {
        ViewPlayer[] players = new ViewPlayer[4];
        players[0] = new ViewPlayer("Player1", PlayerColor.RED, 0);
        players[1] = new ViewPlayer("Player2", PlayerColor.BLUE, 1);
        players[2] = new ViewPlayer("Player3", PlayerColor.GREEN, 2);
        players[3] = new ViewPlayer("Player4", PlayerColor.YELLOW, 3);
        ViewBoard board = new ViewBoard(false, players);
        board.deckAvailability[0] = false;

        System.out.println(board.toString());

        ViewBoard learnerBoard = new ViewBoard(true, players);
        System.out.println(learnerBoard.toString());
    }
}
