package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import org.javatuples.Pair;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;

import java.rmi.RemoteException;

public class BranchMenu implements MenuState {
    private final Terminal terminal;
    private final LineReader lineReader;
    private final String username = ClientGameModel.getInstance().getUsername();

    public BranchMenu(Terminal terminal) {
        this.terminal = terminal;
        this.lineReader = LineReaderBuilder.builder().terminal(terminal).build();
    }

    public void displayMenu() {
        TUI.clearConsole(terminal);
        terminal.writer().println("Branch Menu");
        terminal.writer().println("Your ship has split into two branches.");
        terminal.flush();
    }

    public boolean handleInput() throws RemoteException {
        terminal.writer().println("Type the coordinates of the branch you want to keep (x y):");
        terminal.writer().print(" > ");
        String branchInput = lineReader.readLine().trim();
        int x = Integer.parseInt(branchInput.split(" ")[0]) - 5;
        int y = Integer.parseInt(branchInput.split(" ")[1]) - 4;
        ClientGameModel.getInstance().getClient().chooseBranch(username, new Pair<>(x, y) );
        return true;
    }

    public String getStateName() {
        return "BranchMenu";
    }
}
