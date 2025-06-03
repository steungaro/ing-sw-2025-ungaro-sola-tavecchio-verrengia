package it.polimi.ingsw.gc20.server.controller.managers;

import it.polimi.ingsw.gc20.server.exceptions.ComponentNotFoundException;
import it.polimi.ingsw.gc20.server.model.components.Component;
import it.polimi.ingsw.gc20.server.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.server.model.player.Player;
import it.polimi.ingsw.gc20.server.model.ship.NormalShip;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.List;

public class Translator {
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

    public static Component getFromViewed(GameModel model, int index) {
        return model.getGame().getPile().getViewed().get(index);
    }

    public static Component getFromUnviewed(GameModel model, int index) {
        return model.getGame().getPile().getUnviewed().get(index);
    }

    public static Component getFromBooked(Player player, int index) {
        return ((NormalShip)player.getShip()).getBooked().get(index);
    }
}
