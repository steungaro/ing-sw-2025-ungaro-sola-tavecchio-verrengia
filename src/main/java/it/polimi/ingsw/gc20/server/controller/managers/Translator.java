package it.polimi.ingsw.gc20.server.controller.managers;

import it.polimi.ingsw.gc20.server.exceptions.ComponentNotFoundException;
import it.polimi.ingsw.gc20.server.model.components.Component;
import it.polimi.ingsw.gc20.server.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.server.model.player.Player;
import it.polimi.ingsw.gc20.server.model.ship.NormalShip;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for translating or retrieving components from coordinates.
 */
public class Translator {
    /**
     * Retrieves a component of the specified type at the given coordinates from a player's ship.
     *
     * @param player The player object whose ship contains the desired component.
     * @param coordinates The coordinates (as a pair of integers) representing the location of the component on the ship.
     * @param classType The class type of the component to be retrieved.
     * @param <T> The type of the component to retrieve, which must extend the Component class.
     * @return The component at the specified coordinates cast to the specified type, or null if the coordinates are null.
     * @throws ComponentNotFoundException If the component cannot be found or cannot be cast to the specified type.
     */
    public static <T extends Component> T getComponentAt(Player player, Pair<Integer, Integer> coordinates, Class<T> classType) throws ComponentNotFoundException{
        if(coordinates == null){
            return null;
        }
        try {
            return classType.cast(player.getShip().getComponentAt(coordinates.getValue0(), coordinates.getValue1()));
        } catch (Exception e){
            throw new ComponentNotFoundException("IncorrectComponentSelected");
        }
    }

    /**
     * Retrieves a list of components of the specified type at the given coordinates from a player's ship.
     *
     * @param player The player object whose ship contains the desired components.
     * @param coordinates A list of coordinate pairs (represented as pairs of integers) specifying the locations of the components on the ship.
     * @param classType The class type of the components to be retrieved.
     * @param <T> The type of the components to retrieve, which must extend the Component class.
     * @return A list of components at the specified coordinates cast to the specified type, or null if the coordinates list is null or empty.
     * @throws ComponentNotFoundException If any component cannot be found or cannot be cast to the specified type.
     */
    public static <T extends Component> List<T> getComponentAt(Player player, List<Pair<Integer, Integer>> coordinates, Class<T> classType) throws ComponentNotFoundException {
        if(coordinates == null || coordinates.isEmpty()){
            return null;
        }
        List<T> components = new ArrayList<>();
        for (Pair<Integer, Integer> coordinate : coordinates) {
            components.add(getComponentAt(player, coordinate, classType));
        }
        return components;
    }

    /**
     * Retrieves a component from the viewed pile of the game's current state at the specified index.
     *
     * @param model The game model containing the game state and its components.
     * @param index The index of the component to retrieve from the viewed pile.
     * @return The component located at the specified index within the viewed pile.
     */
    public static Component getFromViewed(GameModel model, int index) {
        return model.getGame().getPile().getViewed().get(index);
    }

    /**
     * Retrieves a component from the unviewed pile of the game's current state at the specified index.
     *
     * @param model The game model containing the game state and its components.
     * @param index The index of the component to retrieve from the unviewed pile.
     * @return The component located at the specified index within the unviewed pile.
     */
    public static Component getFromUnviewed(GameModel model, int index) {
        return model.getGame().getPile().getUnviewed().get(index);
    }

    /**
     * Retrieves a component from the booked list of the specified player's ship at the given index.
     *
     * @param player The player whose ship contains the booked list.
     * @param index The index of the component to retrieve from the booked list.
     * @return The component located at the specified index in the booked list.
     */
    public static Component getFromBooked(Player player, int index) {
        return ((NormalShip)player.getShip()).getBooked().get(index);
    }
}
