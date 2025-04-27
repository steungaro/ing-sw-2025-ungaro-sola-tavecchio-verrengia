package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.server.controller.managers.Translator;
import it.polimi.ingsw.gc20.server.exceptions.*;
import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.server.model.components.Component;
import it.polimi.ingsw.gc20.server.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.server.model.player.Player;
import org.javatuples.Pair;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class AssemblingState extends State {
    private final Map<Player, Boolean> assembled = new HashMap<>();
    private final Map<Player, Component> componentsInHand = new HashMap<>();
    
    /**
     * Default constructor
     */
    public AssemblingState(GameModel model) {
        super(model);
        for (Player player : getModel().getInGamePlayers()) {
            assembled.put(player, false);
            componentsInHand.put(player, null);
        }
        getModel().initCountdown();
    }

    @Override
    public String toString() {
        return "AssemblingState{ " +
                "assembled=" + assembled +
                " }";
    }

    @Override
    public void takeComponentFromUnviewed(Player player, int index) {
        if (componentsInHand.get(player) != null) {
            throw new InvalidParameterException("Player already has a component in hand");
        }
        try {
            Component component =Translator.getFromUnviewed(getModel(), index);
            getModel().componentFromUnviewed(component);
            // Add component to player's hand
            componentsInHand.put(player, component);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Component not found in unviewed pile");
        } catch (ComponentNotFoundException e){
            throw new NoSuchElementException("Component not found in unviewed pile");
        }
    }
    @Override
    public void takeComponentFromViewed(Player player, int index) {
        if (componentsInHand.get(player) != null) {
            throw new InvalidParameterException("Player already has a component in hand");
        }
        try {
            Component component =Translator.getFromViewed(getModel(), index);
            getModel().componentFromViewed(component);
            // Add component to player's hand
            componentsInHand.put(player, component);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("No component found in viewed pile");
        } catch (ComponentNotFoundException e) {
            throw new NoSuchElementException("Component not found in viewed pile");
        }
    }
    @Override
    public void takeComponentFromBooked(Player player, int index) {
        if (componentsInHand.get(player) != null) {
            throw new InvalidParameterException("Player already has a component in hand");
        }
        try {
            Component component =Translator.getFromBooked(player, index);
            getModel().componentFromBooked(component, player);
            // Add component to player's hand
            componentsInHand.put(player, component);
        } catch (ComponentNotFoundException e) {
            throw new NoSuchElementException("Component not found in player's booked list");
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("No component found in player's booked list");
        }
    }
    @Override
    public void addComponentToBooked(Player player) throws NoSpaceException {
        if (componentsInHand.get(player) == null) {
            throw new InvalidParameterException("Player does not have a component in hand");
        }
        getModel().componentToBooked(componentsInHand.get(player), player);
        // Remove component from player's hand
        componentsInHand.put(player, null);
    }
    @Override
    public void addComponentToViewed(Player player) throws DuplicateComponentException {
        if (componentsInHand.get(player) == null) {
            throw new InvalidParameterException("Player does not have a component in hand");
        }
        getModel().componentToViewed(componentsInHand.get(player));
        // Remove component from player's hand
        componentsInHand.put(player, null);
    }
    @Override
    public void placeComponent(Player player, Pair<Integer, Integer> coordinates) {
        if (componentsInHand.get(player) == null) {
            throw new InvalidParameterException("Player does not have a component in hand");
        }
        try {
            getModel().addToShip(componentsInHand.get(player), player, coordinates.getValue0(), coordinates.getValue1());
            // Remove component from player's hand
            componentsInHand.put(player, null);
        } catch (Exception e) {
            // Ship implementation will throw appropriate exceptions if placement is invalid
            throw new InvalidParameterException("Cannot place component at specified location: " + e.getMessage());
        }
    }
    @Override
    public void rotateComponentClockwise(Player player) {
        getModel().RotateClockwise(componentsInHand.get(player));
    }
    @Override
    public void rotateComponentCounterclockwise(Player player) {
        getModel().RotateCounterclockwise(componentsInHand.get(player));
    }
    @Override
    public void stopAssembling(Player player, int position) throws InvalidIndexException {
        if (componentsInHand.get(player) != null) {
            throw new InvalidParameterException("Place component somewhere before stopping assembling");
        }
        getModel().stopAssembling(player, position);
        assembled.put(player, true);
    }

    @Override
    public boolean allAssembled() {
        return assembled.values().stream().allMatch(Boolean::booleanValue);
    }

    @Override
    public List<AdventureCard> peekDeck(Player player, int num) throws InvalidIndexException {
        if (componentsInHand.get(player) != null) {
            throw new InvalidParameterException("Player has a component in hand, cannot peek deck");
        }
        return getModel().viewDeck(num);
    }

    @Override
    public int getHourglassTime(Player player) {
        return getModel().getRemainingTime();
    }

    @Override
    public void turnHourglass(Player player) throws HourglassException {
        if (getModel().getTurnedHourglass() == 1 && !assembled.get(player)) {
            throw new HourglassException("Cannot turn hourglass for the last time for a player that has not completed assembling yet");
        }
        if (getModel().getRemainingTime() != 0) {
            throw new HourglassException("Cannot turn hourglass if time is not 0");
        }
        getModel().turnHourglass();
    }
}
