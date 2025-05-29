package it.polimi.ingsw.gc20.client.view.common.localmodel.board;

import it.polimi.ingsw.gc20.client.view.TUI.TUI;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ViewPlayer;
import it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards.ViewAbandonedShip;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.server.model.player.PlayerColor;

import java.io.Serializable;
import java.rmi.RemoteException;
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


    public int hourglassRemainingTime() {
        return System.currentTimeMillis() - timeStampOfLastHourglassRotation < 90 ? 90 - (int) (System.currentTimeMillis() - timeStampOfLastHourglassRotation) : 0;
    }

    public ViewBoard(boolean isLearner, ViewPlayer[] players) {
        this.isLearner = isLearner;
        this.players = Arrays.copyOf(players, players.length);
    }

    public String learnerPrint(Map<Integer, ViewPlayer> positions) {
        StringBuilder sb = new StringBuilder();
        // Add the top row of the board
        learnerTopRow(sb, positions);
        sb.append(EMPTY_ROW).append("\n");
        sb.append("│").append(" ".repeat(13)).append("↑↑").append(" ".repeat(88)).append("↓↓").append(" ".repeat(13)).append("│").append("\n");
        sb.append("│").append(" ".repeat(47)).append(ClientGameModel.getInstance().getCurrentCard().toLine(0)).append(" ".repeat(47)).append("│").append("\n");
        // Add the middle upper row of the board
        learnerMiddleUpper(sb, positions, 19, 8);
        learnerMiddle(sb);
        String leftFilling;
        String rightFilling;
        // Add the middle lower row of the board
        leftFilling = positions.containsKey(18) ? " " + positions.get(18).playerColor.TUIPrint() + " "  : "    ";
        rightFilling = positions.containsKey(9) ? " " + positions.get(9).playerColor.TUIPrint() + " "  : "    ";
        learnerMiddleLower(sb, leftFilling, rightFilling);
        // Add the bottom row of the board

        learnerBottomRow(sb, positions);

        return sb.toString();
    }

    private void learnerMiddleLower(StringBuilder sb, String leftFilling, String rightFilling) {
        sb.append("│").append(" ".repeat(11)).append("│").append(leftFilling).append("│").append(" ".repeat(30)).append(ClientGameModel.getInstance().getCurrentCard().toLine(8)).append(" ".repeat(30)).append("│").append(rightFilling).append("│").append(" ".repeat(11)).append("│").append("\n");
        sb.append("│").append(" ".repeat(11)).append("╰────╯").append(" ".repeat(30)).append(ClientGameModel.getInstance().getCurrentCard().toLine(9)).append(" ".repeat(30)).append("╰────╯").append(" ".repeat(11)).append("│").append("\n");
        sb.append("│").append(" ".repeat(47)).append(ClientGameModel.getInstance().getCurrentCard().toLine(10)).append(" ".repeat(47)).append("│").append("\n");
        sb.append("│").append(" ".repeat(13)).append("↑↑").append(" ".repeat(88)).append("↓↓").append(" ".repeat(13)).append("│").append("\n");

        sb.append(EMPTY_ROW).append("\n");
    }

    private void learnerMiddle(StringBuilder sb) {
        sb.append("│").append(" ".repeat(47)).append(ClientGameModel.getInstance().getCurrentCard().toLine(4)).append(" ".repeat(47)).append("│").append("\n");
        sb.append("│").append(" ".repeat(13)).append("↑↑").append(" ".repeat(32)).append(ClientGameModel.getInstance().getCurrentCard().toLine(5)).append(" ".repeat(32)).append("↓↓").append(" ".repeat(13)).append("│").append("\n");
        sb.append("│").append(" ".repeat(47)).append(ClientGameModel.getInstance().getCurrentCard().toLine(6)).append(" ".repeat(47)).append("│").append("\n");
        sb.append("│").append(" ".repeat(11)).append("╭────╮").append(" ".repeat(30)).append(ClientGameModel.getInstance().getCurrentCard().toLine(7)).append(" ".repeat(30)).append("╭────╮").append(" ".repeat(11)).append("│").append("\n");
    }

    private static void learnerMiddleUpper(StringBuilder sb, Map<Integer, ViewPlayer> positions, int key, int key1) {
        sb.append("│").append(" ".repeat(11)).append("╭────╮").append(" ".repeat(30)).append(ClientGameModel.getInstance().getCurrentCard().toLine(1)).append(" ".repeat(30)).append("╭────╮").append(" ".repeat(11)).append("│").append("\n");
        String leftFilling = positions.containsKey(key) ? " " + positions.get(key).playerColor.TUIPrint() + " " : "    ";
        String rightFilling = positions.containsKey(key1) ? " " + positions.get(key1).playerColor.TUIPrint() + " " : "    ";
        sb.append("│").append(" ".repeat(11)).append("│").append(leftFilling).append("│").append(" ".repeat(30)).append(ClientGameModel.getInstance().getCurrentCard().toLine(2)).append(" ".repeat(30)).append("│").append(rightFilling).append("│").append(" ".repeat(11)).append("│").append("\n");

        sb.append("│").append(" ".repeat(11)).append("╰────╯").append(" ".repeat(30)).append(ClientGameModel.getInstance().getCurrentCard().toLine(3)).append(" ".repeat(30)).append("╰────╯").append(" ".repeat(11)).append("│").append("\n");
    }

    private static void learnerBottomRow(StringBuilder sb, Map<Integer, ViewPlayer> positions) {
        sb.append("│").append(" ".repeat(11)).append("╭────╮         ".repeat(7)).append("  │").append("\n");

        sb.append("│").append(" ".repeat(11));
        for (int i = 10; i < 17; i++) {
            String filling = positions.containsKey(i) ? " " + positions.get(i).playerColor.TUIPrint() + " "  : "    ";
            sb.append("│").append(filling).append("│   ").append(i == 16 ? "  " : "←←").append("    ");
        }
        sb.append("  │").append("\n");
        sb.append("│").append(" ".repeat(11)).append("╰────╯         ".repeat(7)).append("  │").append("\n");
    }

    private static void learnerTopRow(StringBuilder sb, Map<Integer, ViewPlayer> positions) {
        sb.append("│").append(" ".repeat(11)).append("╭────╮         ".repeat(7)).append("  │").append("\n");

        sb.append("│").append(" ".repeat(11));
        for (int i = 0; i < 7; i++) {
            String filling = positions.containsKey(i) ? " " + positions.get(i).playerColor.TUIPrint() + " "  : "    ";
            sb.append("│").append(filling).append("│   ").append(i == 6 ? "  " : "→→").append("    ");
        }
        sb.append("  │").append("\n");
        sb.append("│").append(" ".repeat(11)).append("╰────╯         ".repeat(7)).append("  │").append("\n");
    }

    public String normalPrint(Map<Integer, ViewPlayer> positions) {
        StringBuilder sb = new StringBuilder();
        sb.append(EMPTY_ROW).append("\n");
        // Add the top row of the board
        normalTopRow(sb, positions);
        sb.append(EMPTY_ROW).append("\n");
        sb.append("│").append(" ".repeat(13)).append("↑↑").append(" ".repeat(88)).append("↓↓").append(" ".repeat(13)).append("│").append("\n");
        sb.append("│").append(" ".repeat(47)).append(ClientGameModel.getInstance().getCurrentCard().toLine(0)).append(" ".repeat(47)).append("│").append("\n");
        // Add the middle upper row of the board
        learnerMiddleUpper(sb, positions, 23, 10);
        String leftFilling;
        String rightFilling;
        learnerMiddle(sb);
        // Add the middle lower row of the board
        leftFilling = positions.containsKey(22) ? " " + positions.get(22).playerColor.TUIPrint() + " "  : "    ";
        rightFilling = positions.containsKey(11) ? " " + positions.get(11).playerColor.TUIPrint() + " "  : "    ";
        learnerMiddleLower(sb, leftFilling, rightFilling);

        // Add the bottom row of the board
        normalBottomRow(sb, positions);
        return sb.toString();
    }

    private static void normalBottomRow(StringBuilder sb, Map<Integer, ViewPlayer> positions) {
        sb.append("│").append(" ".repeat(11)).append("╭────╮    ".repeat(10)).append("       │").append("\n");

        sb.append("│").append(" ".repeat(11));
        for (int i = 12; i < 22; i++) {
            String filling = positions.containsKey(i) ? " " + positions.get(i).playerColor.TUIPrint() + " "  : "    ";
            sb.append("│").append(filling).append("│ ").append(i == 21 ? "  " : "←←").append(" ");
        }
        sb.append("       │").append("\n");
        sb.append("│").append(" ".repeat(11)).append("╰────╯    ".repeat(10)).append("       │").append("\n");
    }

    private static void normalTopRow(StringBuilder sb, Map<Integer, ViewPlayer> positions) {
        sb.append("│").append(" ".repeat(11)).append("╭────╮    ".repeat(10)).append("       │").append("\n");

        sb.append("│").append(" ".repeat(11));
        for (int i = 0; i < 10; i++) {
            String filling = positions.containsKey(i) ? " " + positions.get(i).playerColor.TUIPrint() + " "  : "    ";
            sb.append("│").append(filling).append("│ ").append(i == 9 ? "  " : "→→").append(" ");
        }
        sb.append("       │").append("\n");
        sb.append("│").append(" ".repeat(11)).append("╰────╯    ".repeat(10)).append("       │").append("\n");
    }

    public String learnerAssemblingPrint(Map<Integer, ViewPlayer> positions) {
        StringBuilder sb = new StringBuilder();
        // Add the top row of the board
        learnerTopRow(sb, positions);
        sb.append(EMPTY_ROW).append("\n");
        sb.append("│").append(" ".repeat(13)).append("↑↑").append(" ".repeat(88)).append("↓↓").append(" ".repeat(13)).append("│").append("\n");
        sb.append(EMPTY_ROW).append("\n");
        // Add the middle upper row of the board
        sb.append("│").append(" ".repeat(11)).append("╭────╮").append(" ".repeat(84)).append("╭────╮").append(" ".repeat(11)).append("│").append("\n");
        String leftFilling = positions.containsKey(19) ? " " + positions.get(19).playerColor.TUIPrint() + " "  : "    ";
        String rightFilling = positions.containsKey(8) ? " " + positions.get(8).playerColor.TUIPrint() + " "  : "    ";
        learnerMiddleAssembling(sb, leftFilling, rightFilling);
        // Add the middle lower row of the board
        leftFilling = positions.containsKey(18) ? " " + positions.get(18).playerColor.TUIPrint() + " "  : "    ";
        rightFilling = positions.containsKey(9) ? " " + positions.get(9).playerColor.TUIPrint() + " "  : "    ";
        learnerEnding(sb, leftFilling, rightFilling);
        // Add the bottom row of the board

        learnerBottomRow(sb, positions);

        return sb.toString();
    }

    private void learnerMiddleAssembling(StringBuilder sb, String leftFilling, String rightFilling) {
        middleFilling(sb, leftFilling, rightFilling);
        sb.append("│").append(" ".repeat(13)).append("↑↑").append(" ".repeat(39)).append("Assembling").append(" ".repeat(39)).append("↓↓").append(" ".repeat(13)).append("│").append("\n");
        sb.append(EMPTY_ROW).append("\n");
        sb.append("│").append(" ".repeat(11)).append("╭────╮").append(" ".repeat(84)).append("╭────╮").append(" ".repeat(11)).append("│").append("\n");
    }

    private void middleFilling(StringBuilder sb, String leftFilling, String rightFilling) {
        sb.append("│").append(" ".repeat(11)).append("│").append(leftFilling).append("│").append(" ".repeat(84)).append("│").append(rightFilling).append("│").append(" ".repeat(11)).append("│").append("\n");

        sb.append("│").append(" ".repeat(11)).append("╰────╯").append(" ".repeat(84)).append("╰────╯").append(" ".repeat(11)).append("│").append("\n");
        sb.append(EMPTY_ROW).append("\n");
    }

    private void learnerEnding(StringBuilder sb, String leftFilling, String rightFilling) {
        middleFilling(sb, leftFilling, rightFilling);
        sb.append("│").append(" ".repeat(13)).append("↑↑").append(" ".repeat(88)).append("↓↓").append(" ".repeat(13)).append("│").append("\n");
        sb.append(EMPTY_ROW).append("\n");
    }

    public String normalAssemblingPrint(Map<Integer, ViewPlayer> positions) {
        StringBuilder sb = new StringBuilder();
        sb.append("│").append(" ".repeat(51)).append("Remaining time: \u001b[5m").append(hourglassRemainingTime()).append((hourglassRemainingTime() >= 10 ? "\u001b[25m" : " \u001b[25m")).append(" ".repeat(49)).append("│").append("\n");
        sb.append(SEPARATOR).append("\n");
        // Add the top row of the board
        normalTopRow(sb, positions);
        sb.append(EMPTY_ROW).append("\n");
        sb.append("│").append(" ".repeat(13)).append("↑↑").append(" ".repeat(88)).append("↓↓").append(" ".repeat(13)).append("│").append("\n");
        sb.append(EMPTY_ROW).append("\n");
        // Add the middle upper row of the board
        sb.append("│").append(" ".repeat(11)).append("╭────╮").append(" ".repeat(84)).append("╭────╮").append(" ".repeat(11)).append("│").append("\n");
        String leftFilling = positions.containsKey(23) ? " " + positions.get(23).playerColor.TUIPrint() + " "  : "    ";
        String rightFilling = positions.containsKey(10) ? " " + positions.get(10).playerColor.TUIPrint() + " "  : "    ";
        learnerMiddleAssembling(sb, leftFilling, rightFilling);
        // Add the middle lower row of the board
        leftFilling = positions.containsKey(22) ? " " + positions.get(22).playerColor.TUIPrint() + " "  : "    ";
        rightFilling = positions.containsKey(11) ? " " + positions.get(11).playerColor.TUIPrint() + " "  : "    ";
        learnerEnding(sb, leftFilling, rightFilling);

        // Add the bottom row of the board

        normalBottomRow(sb, positions);

        return sb.toString();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        Map<Integer, ViewPlayer> positions = new HashMap<>();
        for (ViewPlayer viewPlayer : players) {
            if (viewPlayer != null) {
                positions.put(viewPlayer.position, viewPlayer);
            }
        }
        sb.append(TOPPER).append("\n");

        sb.append(EMPTY_ROW).append("\n");
        for (int i = 0; i < players.length; i++) {
            ViewPlayer player = players[players.length - i - 1];
            if (player != null) {
                sb.append("│         ").append(player.playerColor.TUIPrint()).append(": ").append(player.username).append((i == 0 ? "\u001B[1m (Leader) \u001B[22m" : "          ")).append(" ".repeat(120-2-13-10 - player.username.length())).append("│").append("\n");
            }
        }
        sb.append(EMPTY_ROW).append("\n");
        sb.append(SEPARATOR).append("\n");

        if (assemblingState) {
            sb.append(isLearner ? learnerAssemblingPrint(positions) : normalAssemblingPrint(positions));
        }
        else {
            sb.append(isLearner ? learnerPrint(positions) : normalPrint(positions));
        }

        sb.append(EMPTY_ROW).append("\n");
        sb.append(EMPTY_ROW).append("\n");
        sb.append(EMPTY_ROW).append("\n");

        sb.append(BOTTOM).append("\n");

        return sb.toString();

    }

    public static void main(String[] args) throws RemoteException {
        ViewPlayer[] players = new ViewPlayer[4];
        players[0] = new ViewPlayer("Stefano", PlayerColor.RED, 6);
        players[1] = new ViewPlayer("SolaNasone", PlayerColor.BLUE, 10);
        players[2] = new ViewPlayer("Tave", PlayerColor.GREEN, 18);
        players[3] = new ViewPlayer("Verri", PlayerColor.YELLOW, 20);
        ViewBoard board = new ViewBoard(false, players);
        board.assemblingState = true;

        System.out.println(board);

        ViewBoard learnerBoard = new ViewBoard(true, players);
        learnerBoard.assemblingState = true;
        System.out.println(learnerBoard);

        learnerBoard.assemblingState = false;
        ClientGameModel.setInstance(new TUI());
        ClientGameModel.getInstance().setCurrentCard(new ViewAbandonedShip());
        System.out.println(learnerBoard);

        board.timeStampOfLastHourglassRotation = System.currentTimeMillis() - 50;
        System.out.println(board);

        board.assemblingState = false;
        board.timeStampOfLastHourglassRotation = System.currentTimeMillis() - 100;
        System.out.println(board);

    }
}
