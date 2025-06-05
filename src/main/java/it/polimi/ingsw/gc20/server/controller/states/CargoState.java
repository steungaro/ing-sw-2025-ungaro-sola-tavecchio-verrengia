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

    public CargoState(GameModel model, GameController controller) {
        super(model, controller);
    }

    /**
     * This method is used to load a cargo from a cargo hold to the player's cargo hold
     * @param player the player who is loading the cargo
     * @param loaded the color of the cargo to be loaded
     * @param chTo the cargo hold to which the cargo is loaded
     * @throws CargoException if the cargo is not loadable
     * @throws CargoFullException if the cargo hold is full
     */
    @Override
    public void loadCargo(Player player, CargoColor loaded, Pair<Integer, Integer> chTo) throws CargoNotLoadable, CargoFullException, InvalidStateException, CargoException, InvalidTurnException, ComponentNotFoundException {
        getController().getMessageManager().broadcastUpdate(Ship.messageFromShip(player.getUsername(), player.getShip(), "loaded cargo"));
        getModel().addCargo(player, loaded, Translator.getComponentAt(player, chTo, CargoHold.class));
    }

    /**
     * This method is used to unload a cargo from the player's cargo hold
     * @param player the player who is unloading the cargo
     * @param unloaded the color of the cargo to be unloaded
     * @param ch the cargo hold from which the cargo is unloaded
     * @throws InvalidTurnException if it's not the player's turn
     * @throws InvalidStateException if the player is not in the correct phase
     * @throws InvalidCargoException if the cargo is not in the player's cargo hold
     */
    @Override
    public void unloadCargo(Player player, CargoColor unloaded, Pair<Integer, Integer> ch) throws InvalidTurnException,  InvalidStateException, InvalidCargoException, ComponentNotFoundException{
        try {
            getModel().MoveCargo(player, unloaded, Translator.getComponentAt(player, ch, CargoHold.class), null);
            getController().getMessageManager().broadcastUpdate(Ship.messageFromShip(player.getUsername(), player.getShip(), "unloaded cargo"));
        } catch (CargoNotLoadable | CargoFullException _) {
            //ignore this exception, we are unloading the cargo
        }
    }

    /**
     * This method is used to move a cargo from one cargo hold to another
     * @param player the player who is moving the cargo
     * @param loaded the color of the cargo to be moved
     * @param chFrom the cargo hold from which the cargo is moved
     * @param chTo the cargo hold to which the cargo is moved
     * @throws InvalidTurnException if it's not the player's turn
     * @throws InvalidStateException if the player is not in the correct phase
     * @throws InvalidCargoException if the cargo is not in the player's cargo hold
     * @throws CargoNotLoadable if the cargo cannot be loaded
     * @throws CargoFullException if the cargo hold is full
     */
    @Override
    public void moveCargo(Player player, CargoColor loaded, Pair<Integer, Integer> chFrom, Pair<Integer, Integer> chTo) throws InvalidTurnException, InvalidStateException, InvalidCargoException, CargoNotLoadable, CargoFullException, ComponentNotFoundException {
        getModel().MoveCargo(player, loaded, Translator.getComponentAt(player, chFrom, CargoHold.class), Translator.getComponentAt(player, chTo, CargoHold.class));
        getController().getMessageManager().broadcastUpdate(Ship.messageFromShip(player.getUsername(), player.getShip(), "moved cargo"));
    }

    /**
     * This method is used to lose energy from the player's ship when not having cargo
     * @param player the player who is losing energy
     * @param battery the battery to be lost
     * @throws InvalidTurnException if it's not the player's turn
     * @throws IllegalStateException if the player has cargo available
     */
    @Override
    public void loseEnergy(Player player, Pair<Integer, Integer> battery) throws InvalidStateException, InvalidTurnException, EnergyException, ComponentNotFoundException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        if (player.getShip().getCargo().values().stream().mapToInt(v -> v).sum() != 0) {
            throw new InvalidStateException("Cannot lose energy if having cargo available");
        }
        List<Battery> batteries = new ArrayList<>();
        batteries.add(Translator.getComponentAt(player, battery, Battery.class));
        getModel().removeEnergy(player, batteries);
        getController().getMessageManager().broadcastUpdate(Ship.messageFromShip(player.getUsername(), player.getShip(), "lost energy"));
    }
}
