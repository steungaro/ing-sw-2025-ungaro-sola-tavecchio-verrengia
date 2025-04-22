package it.polimi.ingsw.gc20.controller.states;

import it.polimi.ingsw.gc20.controller.GameController;
import it.polimi.ingsw.gc20.exceptions.*;
import it.polimi.ingsw.gc20.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.model.components.*;
import it.polimi.ingsw.gc20.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.model.player.Player;
import org.javatuples.Pair;

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
        this.model = controller.getModel();
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
    
    public void takeComponentFromUnviewed(Player player, int index) {
        exception();
    }
    public void takeComponentFromViewed(Player player, int index) {
        exception();
    }
    public void takeComponentFromBooked(Player player, int index) {
        exception();
    }
    public void addComponentToBooked(Player player) throws NoSpaceException {
        exception();
    }
    public void addComponentToViewed(Player player) throws DuplicateComponentException {
        exception();
    }
    public void placeComponent(Player player, Pair<Integer, Integer> coordinates) {
        exception();
    }
    public void rotateComponentClockwise(Player player) {
        exception();
    }
    public void rotateComponentCounterclockwise(Player player) {
        exception();
    }
    public void stopAssembling(Player player, int position) {
        exception();
    }
    public List<AdventureCard> peekDeck(Player player, int num) throws InvalidIndexException {
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
    public void removeComp(Player player, Component component) throws DeadAlienException {
        exception();
    }

    public boolean allShipsValidated() {
        exception();
        return false;
    }

    public void addAlien(Player player, AlienColor color, Cabin cabin) throws InvalidAlienPlacement {
        exception();
    }
    public void initAllShips() {
        exception();
    }

    public void loadCargo(Player player, CargoColor loaded, Pair<Integer, Integer> ch) throws IllegalStateException, InvalidTurnException, CargoException, CargoNotLoadable, CargoFullException {
        exception();
    }
    public void unloadCargo(Player player, CargoColor lost, Pair<Integer, Integer> ch) throws IllegalStateException, InvalidTurnException, CargoException, CargoNotLoadable, CargoFullException, InvalidCargoException {
        exception();
    }
    public void moveCargo(Player player, CargoColor cargo, Pair<Integer, Integer> from, Pair<Integer, Integer> to) throws IllegalStateException, InvalidTurnException, CargoException, CargoNotLoadable, CargoFullException, InvalidCargoException {
        exception();
    }
    public void loseEnergy(Player player, Pair<Integer, Integer> battery) throws IllegalStateException, InvalidTurnException, EnergyException {
        exception();
    }
    public void landOnPlanet(Player player, int planetIndex) throws IllegalStateException, InvalidTurnException {
        exception();
    }
    public void acceptCard(Player player) throws IllegalStateException, InvalidTurnException, EmptyDeckException {
        exception();
    }
    public void loseCrew(Player player, List<Pair<Integer, Integer>> cabins) throws IllegalStateException, InvalidTurnException, EmptyCabinException, EmptyDeckException {
        exception();
    }
    public void endMove(Player player) throws IllegalStateException, InvalidTurnException, InvalidShipException, EmptyDeckException {
        exception();
    }

    public void activateEngines(Player player, List<Pair<Integer, Integer>> engines, List<Pair<Integer, Integer>> batteries) throws IllegalStateException, InvalidTurnException, InvalidShipException, InvalidEngineException, EnergyException, DeadAlienException, DieNotRolledException, EmptyDeckException {
        exception();
    }
    public void activateShield(Player player, Pair<Integer, Integer> shield, Pair<Integer, Integer> battery) throws IllegalStateException, InvalidTurnException, InvalidShipException, EmptyDeckException, EnergyException, DeadAlienException, DieNotRolledException {
        exception();
    }
    public int shootEnemy(Player player, List<Pair<Integer, Integer>> cannons, List<Pair<Integer, Integer>> batteries) throws IllegalStateException, InvalidTurnException, InvalidCannonException, EnergyException, EmptyDeckException {
        exception();
        return 0;
    }
    public void activateCannons(Player player, List<Pair<Integer, Integer>> cannons, List<Pair<Integer, Integer>> batteries) throws IllegalStateException, InvalidTurnException, InvalidShipException, InvalidCannonException, EnergyException, EmptyDeckException {
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

    public void automaticAction() throws EmptyDeckException {
        exception();
    }

    public void resume() {
        exception();
    }

    public Map<String, Integer> getScore(){
        exception();
        return null;
    }

    public void chooseBranch(Player player, Pair<Integer, Integer> coordinates) throws InvalidTurnException, InvalidShipException, EmptyDeckException {
        exception();
    }

    public int rollDice(Player player) throws IllegalStateException, InvalidTurnException, InvalidShipException, EmptyDeckException, DeadAlienException, DieNotRolledException {
        exception();
        return 0;
    }
}