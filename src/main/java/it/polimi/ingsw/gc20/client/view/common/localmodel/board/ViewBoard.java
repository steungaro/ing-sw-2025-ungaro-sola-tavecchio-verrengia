package it.polimi.ingsw.gc20.client.view.common.localmodel.board;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ViewPlayer;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;

import java.io.Serializable;
import java.util.*;

public class ViewBoard  implements Serializable {
    private static final String TOPPER = "╭" + "─".repeat(118) + "╮";
    private static final String BOTTOM = "╰" + "─".repeat(118) + "╯";
    private static final String SEPARATOR = "├" + "─".repeat(118) + "┤";
    private static final String EMPTY_ROW = "│" + " ".repeat(118) + "│";
    public ViewPlayer[] players;
    public boolean isLearner;
    public boolean assemblingState;
    public int numberOfRotations;
    public int unviewedPile;
    public List<ViewComponent> viewedPile;
    public long timeStampOfLastHourglassRotation;


    /*
     * This method returns the remaining time for the hourglass.
     */
    public int hourglassRemainingTime() {
        return System.currentTimeMillis() - timeStampOfLastHourglassRotation < 90000 ? 90 - ((int) (System.currentTimeMillis() - timeStampOfLastHourglassRotation))/1000 : 0;
    }

    /**
     * Constructor for the ViewBoard class.
     * Initializes the board with the given parameters.
     * @param isLearner Indicates if the board is in learner mode.
     * @param players An array of ViewPlayer objects representing the players on the board.
     */
    public ViewBoard(boolean isLearner, ViewPlayer[] players) {
        this.isLearner = isLearner;
        this.players = Arrays.copyOf(players, players.length);
    }

    /**
     * This method returns the string representation of the learner board.
     * It shows the players' positions and the current card in the learner state.
     * @return The string representation of the board.
     */
    private String learnerPrint() {
        Map<Integer, ViewPlayer> positions = new HashMap<>();
        for (ViewPlayer player : players) {
            if (player != null && player.inGame) {
                positions.put(player.position, player);
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
        sb.append(EMPTY_ROW).append("\n");
        sb.append("│").append(" ".repeat(13)).append("↑↑").append(" ".repeat(88)).append("↓↓").append(" ".repeat(13)).append("│").append("\n");
        sb.append("│").append(" ".repeat(47)).append(ClientGameModel.getInstance().getCurrentCard().toLine(0)).append(" ".repeat(47)).append("│").append("\n");
        // Add the middle upper row of the board
        sb.append("│").append(" ".repeat(11)).append("╭────╮").append(" ".repeat(30)).append(ClientGameModel.getInstance().getCurrentCard().toLine(1)).append(" ".repeat(30)).append("╭────╮").append(" ".repeat(11)).append("│").append("\n");
        String leftFilling = positions.containsKey(19) ? " " + positions.get(19).playerColor.TUIPrint() + " "  : "    ";
        String rightFilling = positions.containsKey(8) ? " " + positions.get(8).playerColor.TUIPrint() + " "  : "    ";
        sb.append("│").append(" ".repeat(11)).append("│").append(leftFilling).append("│").append(" ".repeat(30)).append(ClientGameModel.getInstance().getCurrentCard().toLine(2)).append(" ".repeat(30)).append("│").append(rightFilling).append("│").append(" ".repeat(11)).append("│").append("\n");

        sb.append("│").append(" ".repeat(11)).append("╰────╯").append(" ".repeat(30)).append(ClientGameModel.getInstance().getCurrentCard().toLine(3)).append(" ".repeat(30)).append("╰────╯").append(" ".repeat(11)).append("│").append("\n");
        sb.append("│").append(" ".repeat(47)).append(ClientGameModel.getInstance().getCurrentCard().toLine(4)).append(" ".repeat(47)).append("│").append("\n");
        sb.append("│").append(" ".repeat(13)).append("↑↑").append(" ".repeat(32)).append(ClientGameModel.getInstance().getCurrentCard().toLine(5)).append(" ".repeat(32)).append("↓↓").append(" ".repeat(13)).append("│").append("\n");
        sb.append("│").append(" ".repeat(47)).append(ClientGameModel.getInstance().getCurrentCard().toLine(6)).append(" ".repeat(47)).append("│").append("\n");
        sb.append("│").append(" ".repeat(11)).append("╭────╮").append(" ".repeat(30)).append(ClientGameModel.getInstance().getCurrentCard().toLine(7)).append(" ".repeat(30)).append("╭────╮").append(" ".repeat(11)).append("│").append("\n");
        // Add the middle lower row of the board
        leftFilling = positions.containsKey(18) ? " " + positions.get(18).playerColor.TUIPrint() + " "  : "    ";
        rightFilling = positions.containsKey(9) ? " " + positions.get(9).playerColor.TUIPrint() + " "  : "    ";
        sb.append("│").append(" ".repeat(11)).append("│").append(leftFilling).append("│").append(" ".repeat(30)).append(ClientGameModel.getInstance().getCurrentCard().toLine(8)).append(" ".repeat(30)).append("│").append(rightFilling).append("│").append(" ".repeat(11)).append("│").append("\n");
        sb.append("│").append(" ".repeat(11)).append("╰────╯").append(" ".repeat(30)).append(ClientGameModel.getInstance().getCurrentCard().toLine(9)).append(" ".repeat(30)).append("╰────╯").append(" ".repeat(11)).append("│").append("\n");
        sb.append("│").append(" ".repeat(47)).append(ClientGameModel.getInstance().getCurrentCard().toLine(10)).append(" ".repeat(47)).append("│").append("\n");
        sb.append("│").append(" ".repeat(13)).append("↑↑").append(" ".repeat(88)).append("↓↓").append(" ".repeat(13)).append("│").append("\n");

        sb.append(EMPTY_ROW).append("\n");
        // Add the bottom row of the board

        sb.append("│").append(" ".repeat(11)).append("╭────╮         ".repeat(7)).append("  │").append("\n");

        sb.append("│").append(" ".repeat(11));
        for (int i = 16; i >= 10; i--) {
            String filling = positions.containsKey(i) ? " " + positions.get(i).playerColor.TUIPrint() + " "  : "    ";
            sb.append("│").append(filling).append("│   ").append(i == 16 ? "  " : "←←").append("    ");
        }
        sb.append("  │").append("\n");
        sb.append("│").append(" ".repeat(11)).append("╰────╯         ".repeat(7)).append("  │").append("\n");

        return sb.toString();
    }

    /**
     * This method returns the string representation of the normal board.
     * It shows the players' positions and the current card in the normal state.
     * @return The string representation of the board in the normal state.
     */
    private String normalPrint() {
        Map<Integer, ViewPlayer> positions = new HashMap<>();
        for (ViewPlayer player : players) {
            if (player != null && player.inGame) {
                positions.put(player.position, player);
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append(EMPTY_ROW).append("\n");
        // Add the top row of the board
        sb.append("│").append(" ".repeat(11)).append("╭────╮    ".repeat(10)).append("       │").append("\n");

        sb.append("│").append(" ".repeat(11));
        for (int i = 0; i < 10; i++) {
            String filling = positions.containsKey(i) ? " " + positions.get(i).playerColor.TUIPrint() + " "  : "    ";
            sb.append("│").append(filling).append("│ ").append(i == 9 ? "  " : "→→").append(" ");
        }
        sb.append("       │").append("\n");
        sb.append("│").append(" ".repeat(11)).append("╰────╯    ".repeat(10)).append("       │").append("\n");
        sb.append(EMPTY_ROW).append("\n");
        sb.append("│").append(" ".repeat(13)).append("↑↑").append(" ".repeat(88)).append("↓↓").append(" ".repeat(13)).append("│").append("\n");
        sb.append("│").append(" ".repeat(47)).append(ClientGameModel.getInstance().getCurrentCard().toLine(0)).append(" ".repeat(47)).append("│").append("\n");
        // Add the middle upper row of the board
        sb.append("│").append(" ".repeat(11)).append("╭────╮").append(" ".repeat(30)).append(ClientGameModel.getInstance().getCurrentCard().toLine(1)).append(" ".repeat(30)).append("╭────╮").append(" ".repeat(11)).append("│").append("\n");
        String leftFilling = positions.containsKey(23) ? " " + positions.get(23).playerColor.TUIPrint() + " "  : "    ";
        String rightFilling = positions.containsKey(10) ? " " + positions.get(10).playerColor.TUIPrint() + " "  : "    ";
        sb.append("│").append(" ".repeat(11)).append("│").append(leftFilling).append("│").append(" ".repeat(30)).append(ClientGameModel.getInstance().getCurrentCard().toLine(2)).append(" ".repeat(30)).append("│").append(rightFilling).append("│").append(" ".repeat(11)).append("│").append("\n");

        sb.append("│").append(" ".repeat(11)).append("╰────╯").append(" ".repeat(30)).append(ClientGameModel.getInstance().getCurrentCard().toLine(3)).append(" ".repeat(30)).append("╰────╯").append(" ".repeat(11)).append("│").append("\n");
        sb.append("│").append(" ".repeat(47)).append(ClientGameModel.getInstance().getCurrentCard().toLine(4)).append(" ".repeat(47)).append("│").append("\n");
        sb.append("│").append(" ".repeat(13)).append("↑↑").append(" ".repeat(32)).append(ClientGameModel.getInstance().getCurrentCard().toLine(5)).append(" ".repeat(32)).append("↓↓").append(" ".repeat(13)).append("│").append("\n");
        sb.append("│").append(" ".repeat(47)).append(ClientGameModel.getInstance().getCurrentCard().toLine(6)).append(" ".repeat(47)).append("│").append("\n");
        sb.append("│").append(" ".repeat(11)).append("╭────╮").append(" ".repeat(30)).append(ClientGameModel.getInstance().getCurrentCard().toLine(7)).append(" ".repeat(30)).append("╭────╮").append(" ".repeat(11)).append("│").append("\n");
        // Add the middle lower row of the board
        leftFilling = positions.containsKey(22) ? " " + positions.get(22).playerColor.TUIPrint() + " "  : "    ";
        rightFilling = positions.containsKey(11) ? " " + positions.get(11).playerColor.TUIPrint() + " "  : "    ";
        sb.append("│").append(" ".repeat(11)).append("│").append(leftFilling).append("│").append(" ".repeat(30)).append(ClientGameModel.getInstance().getCurrentCard().toLine(8)).append(" ".repeat(30)).append("│").append(rightFilling).append("│").append(" ".repeat(11)).append("│").append("\n");
        sb.append("│").append(" ".repeat(11)).append("╰────╯").append(" ".repeat(30)).append(ClientGameModel.getInstance().getCurrentCard().toLine(9)).append(" ".repeat(30)).append("╰────╯").append(" ".repeat(11)).append("│").append("\n");
        sb.append("│").append(" ".repeat(47)).append(ClientGameModel.getInstance().getCurrentCard().toLine(10)).append(" ".repeat(47)).append("│").append("\n");
        sb.append("│").append(" ".repeat(13)).append("↑↑").append(" ".repeat(88)).append("↓↓").append(" ".repeat(13)).append("│").append("\n");
        sb.append(EMPTY_ROW).append("\n");

        // Add the bottom row of the board

        sb.append("│").append(" ".repeat(11)).append("╭────╮    ".repeat(10)).append("       │").append("\n");

        sb.append("│").append(" ".repeat(11));
        for (int i = 21; i >= 12; i--) {
            String filling = positions.containsKey(i) ? " " + positions.get(i).playerColor.TUIPrint() + " "  : "    ";
            sb.append("│").append(filling).append("│ ").append(i == 21 ? "  " : "←←").append(" ");
        }
        sb.append("       │").append("\n");
        sb.append("│").append(" ".repeat(11)).append("╰────╯    ".repeat(10)).append("       │").append("\n");

        return sb.toString();
    }

    /**
     * This method returns the string representation of the learner board in the assembling state.
     * It shows the players' positions and the current card in the assembling state.
     * @return The string representation of the board in the assembling state.
     */
    private String learnerAssemblingPrint() {
        Map<Integer, ViewPlayer> positions = new HashMap<>();
        for (ViewPlayer player : players) {
            if (player != null && player.inGame) {
                positions.put(player.position, player);
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
        sb.append(EMPTY_ROW).append("\n");
        sb.append("│").append(" ".repeat(13)).append("↑↑").append(" ".repeat(88)).append("↓↓").append(" ".repeat(13)).append("│").append("\n");
        sb.append(EMPTY_ROW).append("\n");
        // Add the middle upper row of the board
        sb.append("│").append(" ".repeat(11)).append("╭────╮").append(" ".repeat(84)).append("╭────╮").append(" ".repeat(11)).append("│").append("\n");
        String leftFilling = positions.containsKey(19) ? " " + positions.get(19).playerColor.TUIPrint() + " "  : "    ";
        String rightFilling = positions.containsKey(8) ? " " + positions.get(8).playerColor.TUIPrint() + " "  : "    ";
        sb.append("│").append(" ".repeat(11)).append("│").append(leftFilling).append("│").append(" ".repeat(84)).append("│").append(rightFilling).append("│").append(" ".repeat(11)).append("│").append("\n");

        sb.append("│").append(" ".repeat(11)).append("╰────╯").append(" ".repeat(84)).append("╰────╯").append(" ".repeat(11)).append("│").append("\n");
        sb.append(EMPTY_ROW).append("\n");
        sb.append("│").append(" ".repeat(13)).append("↑↑").append(" ".repeat(39)).append("Assembling").append(" ".repeat(39)).append("↓↓").append(" ".repeat(13)).append("│").append("\n");
        sb.append(EMPTY_ROW).append("\n");
        sb.append("│").append(" ".repeat(11)).append("╭────╮").append(" ".repeat(84)).append("╭────╮").append(" ".repeat(11)).append("│").append("\n");
        // Add the middle lower row of the board
        leftFilling = positions.containsKey(18) ? " " + positions.get(18).playerColor.TUIPrint() + " "  : "    ";
        rightFilling = positions.containsKey(9) ? " " + positions.get(9).playerColor.TUIPrint() + " "  : "    ";
        sb.append("│").append(" ".repeat(11)).append("│").append(leftFilling).append("│").append(" ".repeat(84)).append("│").append(rightFilling).append("│").append(" ".repeat(11)).append("│").append("\n");
        sb.append("│").append(" ".repeat(11)).append("╰────╯").append(" ".repeat(84)).append("╰────╯").append(" ".repeat(11)).append("│").append("\n");
        sb.append(EMPTY_ROW).append("\n");
        sb.append("│").append(" ".repeat(13)).append("↑↑").append(" ".repeat(88)).append("↓↓").append(" ".repeat(13)).append("│").append("\n");
        sb.append(EMPTY_ROW).append("\n");
        // Add the bottom row of the board

        sb.append("│").append(" ".repeat(11)).append("╭────╮         ".repeat(7)).append("  │").append("\n");

        sb.append("│").append(" ".repeat(11));
        for (int i = 16; i >= 10; i--) {
            String filling = positions.containsKey(i) ? " " + positions.get(i).playerColor.TUIPrint() + " "  : "    ";
            sb.append("│").append(filling).append("│   ").append(i == 16 ? "  " : "←←").append("    ");
        }
        sb.append("  │").append("\n");
        sb.append("│").append(" ".repeat(11)).append("╰────╯         ".repeat(7)).append("  │").append("\n");

        return sb.toString();
    }

    /**
     * This method returns the string representation of the normal board in the assembling state.
     * It shows the players' positions and the current card in the assembling state.
     * @return The string representation of the board in the assembling state.
     */
    private String normalAssemblingPrint() {
        Map<Integer, ViewPlayer> positions = new HashMap<>();
        for (ViewPlayer player : players) {
            if (player != null && player.inGame) {
                positions.put(player.position, player);
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("│").append(" ".repeat(51)).append("Remaining time: \u001b[5m").append(hourglassRemainingTime()).append((hourglassRemainingTime() >= 10 ? "\u001b[25m" : " \u001b[25m")).append(" ".repeat(49)).append("│").append("\n");
        sb.append(SEPARATOR).append("\n");
        // Add the top row of the board
        sb.append("│").append(" ".repeat(11)).append("╭────╮    ".repeat(10)).append("       │").append("\n");

        sb.append("│").append(" ".repeat(11));
        for (int i = 0; i < 10; i++) {
            String filling = positions.containsKey(i) ? " " + positions.get(i).playerColor.TUIPrint() + " "  : "    ";
            sb.append("│").append(filling).append("│ ").append(i == 9 ? "  " : "→→").append(" ");
        }
        sb.append("       │").append("\n");
        sb.append("│").append(" ".repeat(11)).append("╰────╯    ".repeat(10)).append("       │").append("\n");
        sb.append(EMPTY_ROW).append("\n");
        sb.append("│").append(" ".repeat(13)).append("↑↑").append(" ".repeat(88)).append("↓↓").append(" ".repeat(13)).append("│").append("\n");
        sb.append(EMPTY_ROW).append("\n");
        // Add the middle upper row of the board
        sb.append("│").append(" ".repeat(11)).append("╭────╮").append(" ".repeat(84)).append("╭────╮").append(" ".repeat(11)).append("│").append("\n");
        String leftFilling = positions.containsKey(23) ? " " + positions.get(23).playerColor.TUIPrint() + " "  : "    ";
        String rightFilling = positions.containsKey(10) ? " " + positions.get(10).playerColor.TUIPrint() + " "  : "    ";
        sb.append("│").append(" ".repeat(11)).append("│").append(leftFilling).append("│").append(" ".repeat(84)).append("│").append(rightFilling).append("│").append(" ".repeat(11)).append("│").append("\n");

        sb.append("│").append(" ".repeat(11)).append("╰────╯").append(" ".repeat(84)).append("╰────╯").append(" ".repeat(11)).append("│").append("\n");
        sb.append(EMPTY_ROW).append("\n");
        sb.append("│").append(" ".repeat(13)).append("↑↑").append(" ".repeat(39)).append("Assembling").append(" ".repeat(39)).append("↓↓").append(" ".repeat(13)).append("│").append("\n");
        sb.append(EMPTY_ROW).append("\n");
        sb.append("│").append(" ".repeat(11)).append("╭────╮").append(" ".repeat(84)).append("╭────╮").append(" ".repeat(11)).append("│").append("\n");
        // Add the middle lower row of the board
        leftFilling = positions.containsKey(22) ? " " + positions.get(22).playerColor.TUIPrint() + " "  : "    ";
        rightFilling = positions.containsKey(11) ? " " + positions.get(11).playerColor.TUIPrint() + " "  : "    ";
        sb.append("│").append(" ".repeat(11)).append("│").append(leftFilling).append("│").append(" ".repeat(84)).append("│").append(rightFilling).append("│").append(" ".repeat(11)).append("│").append("\n");
        sb.append("│").append(" ".repeat(11)).append("╰────╯").append(" ".repeat(84)).append("╰────╯").append(" ".repeat(11)).append("│").append("\n");
        sb.append(EMPTY_ROW).append("\n");
        sb.append("│").append(" ".repeat(13)).append("↑↑").append(" ".repeat(88)).append("↓↓").append(" ".repeat(13)).append("│").append("\n");
        sb.append(EMPTY_ROW).append("\n");

        // Add the bottom row of the board

        sb.append("│").append(" ".repeat(11)).append("╭────╮    ".repeat(10)).append("       │").append("\n");

        sb.append("│").append(" ".repeat(11));
        for (int i = 21; i >= 12; i--) {
            String filling = positions.containsKey(i) ? " " + positions.get(i).playerColor.TUIPrint() + " "  : "    ";
            sb.append("│").append(filling).append("│ ").append(i == 21 ? "  " : "←←").append(" ");
        }
        sb.append("       │").append("\n");
        sb.append("│").append(" ".repeat(11)).append("╰────╯    ".repeat(10)).append("       │").append("\n");

        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(TOPPER).append("\n");

        sb.append(EMPTY_ROW).append("\n");
        for (ViewPlayer player : players) {
            if (player != null) {
                sb.append("│         ").append(player.playerColor.TUIPrint()).append(": ").append(player.username).append(" ".repeat(120-2-13 - player.username.length())).append("│").append("\n");
            }
        }

        sb.append(EMPTY_ROW).append("\n");

        if (assemblingState) {
            sb.append(isLearner ? learnerAssemblingPrint() : normalAssemblingPrint());
        }
        else {
            sb.append(isLearner ? learnerPrint() : normalPrint());
        }

        sb.append(EMPTY_ROW).append("\n");
        sb.append(EMPTY_ROW).append("\n");
        sb.append(EMPTY_ROW).append("\n");

        sb.append(BOTTOM).append("\n");

        return sb.toString();

    }
}
