package it.polimi.ingsw.gc20.client.view.common.localmodel.board;

import it.polimi.ingsw.gc20.client.view.TUI.TUI;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ViewPlayer;
import it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards.ViewAbandonedShip;
import it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards.ViewAdventureCard;
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
    public int remainingTime;
    public int numberOfRotations;
    public List<List<ViewAdventureCard>> decks;
    public int unviewedPile;
    public List<ViewComponent> viewedPile;
    public int hourglassRemainingTime;


    public ViewBoard(boolean isLearner, ViewPlayer[] players) {
        this.isLearner = isLearner;
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
        sb.append("│").append(" ".repeat(51)).append("Hourglass time: ").append(hourglassRemainingTime).append((hourglassRemainingTime >= 10 ? " " : "")).append(" ".repeat(50)).append("│").append("\n");
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
        for (int i = 12; i < 22; i++) {
            String filling = positions.containsKey(i) ? " " + positions.get(i).playerColor.TUIPrint() + " "  : "    ";
            sb.append("│").append(filling).append("│ ").append(i == 21 ? "  " : "←←").append(" ");;
        }
        sb.append("       │").append("\n");
        sb.append("│").append(" ".repeat(11)).append("╰────╯    ".repeat(10)).append("       │").append("\n");

        return sb.toString();
    }

    public String learnerAssemblingPrint() {
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
        for (int i = 10; i < 17; i++) {
            String filling = positions.containsKey(i) ? " " + positions.get(i).playerColor.TUIPrint() + " "  : "    ";
            sb.append("│").append(filling).append("│   ").append(i == 16 ? "  " : "←←").append("    ");
        }
        sb.append("  │").append("\n");
        sb.append("│").append(" ".repeat(11)).append("╰────╯         ".repeat(7)).append("  │").append("\n");

        return sb.toString();
    }

    public String normalAssemblingPrint() {
        Map<Integer, ViewPlayer> positions = new HashMap<>();
        for (int i = 0; i < players.length; i++) {
            if (players[i] != null) {
                positions.put(i, players[i]);
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("│").append(" ".repeat(51)).append("Hourglass time: \u001b[5m").append(hourglassRemainingTime).append((hourglassRemainingTime >= 10 ? "\u001b[25m" : " \u001b[25m")).append(" ".repeat(49)).append("│").append("\n");
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


        if (!isLearner && assemblingState) {
            sb.append(SEPARATOR).append("\n");
            sb.append(EMPTY_ROW).append("\n");

            sb.append("│").append(" ".repeat(29)).append("       Deck 1       ").append("       Deck 2       ").append("       Deck 3       ").append(" ".repeat(29)).append("│").append("\n");
            sb.append("│").append(" ".repeat(29)).append(decks.get(0) != null ? "\u001B[32m      Available     \u001B[0m":"\u001B[31m     Unavailable    \u001B[0m").append(decks.get(1) != null ? "\u001B[32m      Available     \u001B[0m":"\u001B[31m     Unavailable    \u001B[0m").append(decks.get(2) != null ? "\u001B[32m      Available     \u001B[0m":"\u001B[31m     Unavailable    \u001B[0m").append(" ".repeat(29)).append("│").append("\n");
        }
        sb.append(EMPTY_ROW).append("\n");

        sb.append(BOTTOM).append("\n");

        return sb.toString();

    }

    public static void main(String[] args) throws RemoteException {
        ViewPlayer[] players = new ViewPlayer[4];
        players[0] = new ViewPlayer("Stefano", PlayerColor.RED, 0);
        players[1] = new ViewPlayer("SolaNasone", PlayerColor.BLUE, 1);
        players[2] = new ViewPlayer("Tave", PlayerColor.GREEN, 2);
        players[3] = new ViewPlayer("Verri", PlayerColor.YELLOW, 3);
        ViewBoard board = new ViewBoard(false, players);
        board.decks = new ArrayList<>();
        board.decks.add(null);
        board.decks.add(null);
        board.decks.add(null);
        board.assemblingState = true;

        System.out.println(board.toString());

        ViewBoard learnerBoard = new ViewBoard(true, players);
        learnerBoard.assemblingState = true;
        System.out.println(learnerBoard.toString());

        learnerBoard.assemblingState = false;
        ClientGameModel.setInstance(new TUI());
        ClientGameModel.getInstance().setCurrentCard(new ViewAbandonedShip());
        System.out.println(learnerBoard.toString());

        board.hourglassRemainingTime = 10;
        System.out.println(board.toString());

        board.assemblingState = false;
        board.hourglassRemainingTime = 5;
        System.out.println(board.toString());

    }
}
