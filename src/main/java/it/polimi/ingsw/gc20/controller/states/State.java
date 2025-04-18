package it.polimi.ingsw.gc20.controller.states;

import it.polimi.ingsw.gc20.controller.GameController;
import it.polimi.ingsw.gc20.exceptions.CargoException;
import it.polimi.ingsw.gc20.exceptions.HourglassException;
import it.polimi.ingsw.gc20.exceptions.InvalidShipException;
import it.polimi.ingsw.gc20.exceptions.InvalidTurnException;
import it.polimi.ingsw.gc20.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.model.components.*;
import it.polimi.ingsw.gc20.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.model.player.Player;

import java.util.List;
import java.util.Map;

public abstract class State{
    private final GameModel model;
    private final GameController controller;

    /**
     * Default constructor
     */
    public State(GameModel model, GameController controller) {
        this.model = model;
        this.controller = controller;
    }
    public State(GameController controller) {
        this.model = null;
        this.controller = controller;
    }
    public State(GameModel model) {
        this.model = model;
        this.controller = null;
    }

    public GameModel getModel() {
        return model;
    }

    public GameController getController() {
        return controller;
    }

    private void exception() {
        throw new IllegalStateException("Cannot perform this action in " + this + " state");
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

    public boolean allShipsValidated() {;
        exception();
        return false;
    }

    public void addAlien(Player player, AlienColor color, Cabin cabin) {
        exception();
    }
    public void initAllShips() {
        exception();
    }

    public void loadCargo(Player player, CargoColor loaded, CargoHold ch) throws IllegalStateException, InvalidTurnException, CargoException {
        exception();
    }
    public void unloadCargo(Player player, CargoColor lost, CargoHold ch) throws IllegalStateException, InvalidTurnException, CargoException {
        exception();
    }
    public void moveCargo(Player player, CargoColor cargo, CargoHold from, CargoHold to) throws IllegalStateException, InvalidTurnException, CargoException {
        exception();
    }
    public void loseEnergy(Player player, Battery battery) throws IllegalStateException, InvalidTurnException {
        exception();
    }
    public void landOnPlanet(Player player, int planetIndex) throws IllegalStateException, InvalidTurnException {
        exception();
    }
    public void acceptCard(Player player) throws IllegalStateException, InvalidTurnException {
        exception();
    }
    public void loseCrew(Player player, List<Cabin> cabins) throws IllegalStateException, InvalidTurnException {
        exception();
    }
    public void endMove(Player player) throws IllegalStateException, InvalidTurnException, InvalidShipException {
        exception();
    }

    public void activateEngines(Player player, List<Engine> engines, List<Battery> batteries) throws IllegalStateException, InvalidTurnException, InvalidShipException {
        exception();
    }
    public void activateShield(Player player, Shield shield, Battery battery) throws IllegalStateException, InvalidTurnException, InvalidShipException {
        exception();
    }
    public int shootEnemy(Player player, List<Cannon> cannons, List<Battery> batteries) throws IllegalStateException, InvalidTurnException, InvalidShipException {
        exception();
        return 0;
    }
    public void activateCannons(Player player, List<Cannon> cannons, List<Battery> batteries) throws IllegalStateException, InvalidTurnException, InvalidShipException {
        exception();
    }

    public boolean allAssembled() {
        exception();
        return false;
    }

    public boolean allShipsReadyToFly() {
        exception();
        return false;
    }

    public void readyToFly(Player player) {
        exception();
    }

    public void automaticAction() {
        exception();
    }

    public void resume() {
        exception();
    }

    public Map<String, Integer> getScore(){
        exception();
        return null;
    }

    public void chooseBranch(Player player, int col, int row) throws InvalidTurnException, InvalidShipException {
        exception();
    }

    public void rollDice(Player player) throws IllegalStateException, InvalidTurnException, InvalidShipException {
        exception();
    }
}