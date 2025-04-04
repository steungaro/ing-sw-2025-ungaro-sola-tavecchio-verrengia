package it.polimi.ingsw.gc20.controller.states;

import it.polimi.ingsw.gc20.exceptions.HourglassException;
import it.polimi.ingsw.gc20.exceptions.InvalidTurnException;
import it.polimi.ingsw.gc20.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.model.components.*;
import it.polimi.ingsw.gc20.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.model.player.Player;

import java.util.List;

public abstract class State{
    private void exception() {
        throw new IllegalStateException("Cannot perform this action in " + this.toString() + " state");
    }
    
    public Component takeComponentFromUnviewed(Player player, Component component) {
        exception();
        return null;
    }
    public Component takeComponentFromViewed(Player player, Component component) {
        exception();
        return null;
    }
    public Component takeComponentFromBooked(Player player, Component component) {
        exception();
        return null;
    }
    public void addComponentToBooked(Player player, Component component) {
        exception();
    }
    public void addComponentToViewed(Component component) {
        exception();
    }
    public void placeComponent(Player player, Component component, int x, int y) {
        exception();
    }
    public void rotateComponentClockwise(Component component) {
        exception();
    }
    public void rotateComponentCounterclockwise(Component component) {
        exception();
    }
    public void stopAssembling(Player player, int position) {
        exception();
    }
    public List<AdventureCard> peekDeck(Player player, int num) {
        exception();
        return null;
    }
    public int getHourglassTime(Player player) {
        exception();
        return 0;
    }
    public void turnHourglass(Player player) throws HourglassException {
        exception();
    }
    public boolean isShipValid(Player player) {
        exception();
        return false;
    }
    public void removeComp(Player player, Component component) {
        exception();
    }
    public void addAlien(Player player, AlienColor color, Cabin cabin) {
        exception();
    }
    public void initAllShips() {
        exception();
    }
    public void landOnPlanet(Player player, int planetIndex) throws IllegalStateException, InvalidTurnException {
        exception();
    }
    public void loadCargo(Player player, CargoColor loaded, CargoHold ch) throws IllegalStateException, InvalidTurnException {
        exception();
    }
    public void unloadCargo(Player player, CargoColor lost, CargoHold ch) throws IllegalStateException, InvalidTurnException {
        exception();
    }
    public void moveCargo(Player player, CargoColor cargo, CargoHold from, CargoHold to) throws IllegalStateException, InvalidTurnException {
        exception();
    }
    public void acceptCard(Player player) throws IllegalStateException, InvalidTurnException {
        exception();
    }
    public void loseCrew(Player player, List<Cabin> cabins) throws IllegalStateException, InvalidTurnException {
        exception();
    }
    public void endMove(Player player) throws IllegalStateException, InvalidTurnException {
        exception();
    }

    public void activateEngines(Player player, List<Engine> engines, List<Battery> batteries) throws IllegalStateException, InvalidTurnException {
        exception();
    }
    public void activateShield(Player player, Shield shield, Battery battery) throws IllegalStateException, InvalidTurnException {
        exception();
    }
    public void activateCannonForProjectile(Player player, Cannon cannon, Battery battery) throws IllegalStateException, InvalidTurnException {
        exception();
    }
    public int shootEnemy(Player player, List<Cannon> cannons, List<Battery> batteries) throws IllegalStateException, InvalidTurnException {
        exception();
        return 0;
    }
    public void activateCannonsCombatZone(Player player, List<Cannon> cannons, List<Battery> batteries) throws IllegalStateException, InvalidTurnException {
        exception();
    }

    public boolean allAssembled() {
        exception();
        return false;
    }

    public boolean allShipsReady() {
        exception();
        return false;
    }

    public void readyToFly(Player player) {
        exception();
    }

}
