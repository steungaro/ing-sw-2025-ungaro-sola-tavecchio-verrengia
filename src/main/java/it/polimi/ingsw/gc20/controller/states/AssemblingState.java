package it.polimi.ingsw.gc20.controller.states;

import it.polimi.ingsw.gc20.exceptions.HourglassException;
import it.polimi.ingsw.gc20.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.model.components.Component;
import it.polimi.ingsw.gc20.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.model.player.Player;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class AssemblingState extends State {
    GameModel model;
    Map<Player, Boolean> assembled = new HashMap<>();
    
    /**
     * Default constructor
     */
    public AssemblingState(GameModel model) {
        super();
        this.model = model;
        for (Player player : model.getInGamePlayers()) {
            assembled.put(player, false);
        }
        model.initCountdown();
    }

    @Override
    public String toString() {
        return "AssemblingState";
    }

    @Override
    public Component takeComponentFromUnviewed(Player player, Component component) {
        try {
            model.componentFromUnviewed(component);
            // Add component to viewed pile so other players can see it
            model.componentToViewed(component);
            return component;
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Component not found in unviewed pile");
        }
    }
    @Override
    public Component takeComponentFromViewed(Player player, Component component) {
        try {
            model.componentFromViewed(component);
            return component;
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Component not found in viewed pile");
        }
    }
    @Override
    public Component takeComponentFromBooked(Player player, Component component) {
        try {
            model.componentFromBooked(component, player);
            return component;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Component not found in player's booked list");
        }
    }
    @Override
    public void addComponentToBooked(Player player, Component component) {
        try {
            model.componentToBooked(component, player);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Component not found in player's booked list");
        }
    }
    @Override
    public void addComponentToViewed(Component component) {
        model.componentToViewed(component);
    }
    @Override
    public void placeComponent(Player player, Component component, int x, int y) {
        try {
            model.addToShip(component, player, x, y);
        } catch (Exception e) {
            // Ship implementation will throw appropriate exceptions if placement is invalid
            throw new InvalidParameterException("Cannot place component at specified location: " + e.getMessage());
        }
    }
    @Override
    public void rotateComponentClockwise(Component component) {
        model.RotateClockwise(component);
    }
    @Override
    public void rotateComponentCounterclockwise(Component component) {
        model.RotateCounterclockwise(component);
    }
    @Override
    public void stopAssembling(Player player, int position) {
        model.stopAssembling(player, position);
        assembled.put(player, true);
    }

    @Override
    public boolean allAssembled() {
        return assembled.values().stream().allMatch(Boolean::booleanValue);
    }

    @Override
    public List<AdventureCard> peekDeck(Player player, int num) {
        return model.viewDeck(num);
    }

    @Override
    public int getHourglassTime(Player player) {
        return model.getRemainingTime();
    }

    @Override
    public void turnHourglass(Player player) throws HourglassException {
        if (model.getTurnedHourglass() == 1 && !assembled.get(player)) {
            throw new HourglassException("Cannot turn hourglass for the last time for a player that has not completed assembling yet");
        }
        if (model.getRemainingTime() != 0) {
            throw new HourglassException("Cannot turn hourglass if time is not 0");
        }
        model.turnHourglass();
    }
}
