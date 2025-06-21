package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.controller.managers.Translator;
import it.polimi.ingsw.gc20.server.exceptions.*;
import it.polimi.ingsw.gc20.server.model.components.Battery;
import it.polimi.ingsw.gc20.server.model.components.CargoHold;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.server.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.server.model.player.Player;
import it.polimi.ingsw.gc20.server.model.ship.Ship;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.List;

public abstract class CargoState extends PlayingState {

    /**
     * Constructs a CargoState object, which is a specific state in the game where players can
     * load and unload cargo. This state is initialized with the provided game model and controller.
     *
     * @param model      the game model that provides the data structure and logic for the game
     * @param controller the game controller that manages game flow and interactions
     */
    public CargoState(GameModel model, GameController controller) {
        super(model, controller);
    }

    @Override
    public void loadCargo(Player player, CargoColor loaded, Pair<Integer, Integer> chTo) throws CargoNotLoadable, CargoFullException, InvalidStateException, CargoException, InvalidTurnException, ComponentNotFoundException {
        getModel().addCargo(player, loaded, Translator.getComponentAt(player, chTo, CargoHold.class));
        getController().getMessageManager().broadcastUpdate(Ship.messageFromShip(player.getUsername(), player.getShip(), "loaded cargo"));
    }

    @Override
    public void unloadCargo(Player player, CargoColor unloaded, Pair<Integer, Integer> ch) throws InvalidTurnException,  InvalidStateException, InvalidCargoException, ComponentNotFoundException{
        try {
            getModel().moveCargo(player, unloaded, Translator.getComponentAt(player, ch, CargoHold.class), null);
            getController().getMessageManager().broadcastUpdate(Ship.messageFromShip(player.getUsername(), player.getShip(), "unloaded cargo"));
        } catch (CargoNotLoadable | CargoFullException _) {
            //ignore this exception, we are unloading the cargo
        }
    }

    @Override
    public void moveCargo(Player player, CargoColor loaded, Pair<Integer, Integer> chFrom, Pair<Integer, Integer> chTo) throws InvalidTurnException, InvalidStateException, InvalidCargoException, CargoNotLoadable, CargoFullException, ComponentNotFoundException {
        getModel().moveCargo(player, loaded, Translator.getComponentAt(player, chFrom, CargoHold.class), Translator.getComponentAt(player, chTo, CargoHold.class));
        getController().getMessageManager().broadcastUpdate(Ship.messageFromShip(player.getUsername(), player.getShip(), "moved cargo"));
    }

    @Override
    public void loseEnergy(Player player, Pair<Integer, Integer> battery) throws InvalidStateException, InvalidTurnException, EnergyException, ComponentNotFoundException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn!");
        }
        if (player.getShip().getCargo().values().stream().mapToInt(v -> v).sum() != 0) {
            throw new InvalidStateException("Cannot lose energy if having cargo available. Please unload your cargo first.");
        }
        List<Battery> batteries = new ArrayList<>();
        batteries.add(Translator.getComponentAt(player, battery, Battery.class));
        getModel().removeEnergy(player, batteries);
        getController().getMessageManager().broadcastUpdate(Ship.messageFromShip(player.getUsername(), player.getShip(), "lost energy"));
    }
}
