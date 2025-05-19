package it.polimi.ingsw.gc20.client.view.TUI;

import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import org.javatuples.Pair;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;

import java.io.IOException;
import java.util.Map;

public class CargoMenu implements MenuState{
    private final Terminal terminal;
    private final LineReader lineReader;
    private final String username = ClientGameModel.getInstance().getUsername();
    private final String message;
    private final int cargoToLose;
    private final Map<CargoColor, Integer> cargoToGain;

    public CargoMenu(Terminal terminal, String message, int cargoToLose, Map<CargoColor, Integer> cargoToGain) {
        this.terminal = terminal;
        this.lineReader = LineReaderBuilder.builder().terminal(terminal).build();
        this.message = message;
        this.cargoToLose = cargoToLose;
        this.cargoToGain = cargoToGain;
    }

    @Override
    public void displayMenu() {
        TUI.clearConsole(terminal);
        terminal.writer().println("Cargo Menu");
        terminal.writer().println(message);
        if (cargoToLose > 0) {
            terminal.writer().println("You have to lose " + cargoToLose + " cargo.");
            terminal.writer().println("1. Lose cargo");
            terminal.writer().println("2. Move cargo");
            terminal.writer().println("3. Lose energy (only if you don't have enough cargo to lose)");
            terminal.writer().println("4. End turn");
        } else {
            terminal.writer().println("You have to gain " +
                    cargoToGain.entrySet().stream().map(e -> e.getKey().toString() + " " + e.getValue()) +
                    " cargo.");
            terminal.writer().println("1. Unload cargo");
            terminal.writer().println("2. Move cargo");
            terminal.writer().println("3. Load cargo");
            terminal.writer().println("4. End turn");
        }
        terminal.flush();
    }


    public boolean handleInput() throws IOException {
        // Hide cursor
        TUI.hideCursor(terminal);
        int choice = terminal.reader().read();
        // Show cursor
        TUI.showCursor(terminal);
        // Handle user input for the cargo menu
        switch (choice) {
            case 1:
                terminal.writer().println("Type the coordinates of the cargo you want to lose (x y):");
                terminal.writer().print(" > ");
                String cargoInput = lineReader.readLine().trim();
                int x = Integer.parseInt(cargoInput.split(" ")[0]) - 5;
                int y = Integer.parseInt(cargoInput.split(" ")[1]) - 4;
                Pair<Integer, Integer> coordinates = new Pair<>(x, y);

                terminal.writer().println("Type the color of the cargo you want to lose (RED/YELLOW/GREEN/BLUE):");
                terminal.writer().print(" > ");
                String cargoColorInput = lineReader.readLine().trim();
                CargoColor cargoColor = CargoColor.valueOf(cargoColorInput.toUpperCase());
                ClientGameModel.getInstance().getClient().unloadCargo(username, cargoColor, coordinates);
                break;
            case 2:
                terminal.writer().println("Type the coordinates of the cargo you want to move from (x y):");
                terminal.writer().print(" > ");
                String moveInput = lineReader.readLine().trim();
                int moveX = Integer.parseInt(moveInput.split(" ")[0]) - 5;
                int moveY = Integer.parseInt(moveInput.split(" ")[1]) - 4;
                Pair<Integer, Integer> moveCoordinates = new Pair<>(moveX, moveY);
                terminal.writer().println("Type the coordinates of the cargo you want to move to (x y):");
                terminal.writer().print(" > ");
                String moveToInput = lineReader.readLine().trim();
                int moveToX = Integer.parseInt(moveToInput.split(" ")[0]) - 5;
                int moveToY = Integer.parseInt(moveToInput.split(" ")[1]) - 4;
                Pair<Integer, Integer> moveToCoordinates = new Pair<>(moveToX, moveToY);
                terminal.writer().println("Type the color of the cargo you want to move (RED/YELLOW/GREEN/BLUE):");
                terminal.writer().print(" > ");
                String moveCargoColorInput = lineReader.readLine().trim();
                CargoColor moveCargoColor = CargoColor.valueOf(moveCargoColorInput.toUpperCase());
                ClientGameModel.getInstance().getClient().moveCargo(username, moveCargoColor, moveCoordinates, moveToCoordinates);
                break;
            case 3:
                if (cargoToLose > 0) {
                    terminal.writer().println("Type the coordinates of the battery you want to lose energy from (x y):");
                    terminal.writer().print(" > ");
                    String batteryInput = lineReader.readLine().trim();
                    Pair<Integer, Integer> batteryCoordinates = new Pair<>(Integer.parseInt(batteryInput.split(" ")[0]) - 5, Integer.parseInt(batteryInput.split(" ")[1]) - 4);
                    ClientGameModel.getInstance().getClient().loseEnergy(username, batteryCoordinates);
                } else {
                    terminal.writer().println("Type the coordinates of the cargo hold you want to load to (x y):");
                    terminal.writer().print(" > ");
                    String loadInput = lineReader.readLine().trim();
                    int loadX = Integer.parseInt(loadInput.split(" ")[0]) - 5;
                    int loadY = Integer.parseInt(loadInput.split(" ")[1]) - 4;
                    Pair<Integer, Integer> loadCoordinates = new Pair<>(loadX, loadY);
                    terminal.writer().println("Type the color of the cargo you want to load (RED/YELLOW/GREEN/BLUE):");
                    terminal.writer().print(" > ");
                    String loadCargoColorInput = lineReader.readLine().trim();
                    CargoColor loadCargoColor = CargoColor.valueOf(loadCargoColorInput.toUpperCase());
                    ClientGameModel.getInstance().getClient().loadCargo(username, loadCargoColor, loadCoordinates);
                }
                break;
            case 4:
                ClientGameModel.getInstance().getClient().endMove(username);
                break;
            case 'q':
                ClientGameModel.getInstance().shutdown();
                break;
            default:
                terminal.writer().println("Invalid choice. Please try again.");
                return false;
        }
        return true;
    }

    @Override
    public String getStateName() {
        return "Cargo Menu";
    }
}
