package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.exceptions.*;
import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.server.model.components.AlienColor;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.server.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.server.model.player.Player;
import org.javatuples.Pair;

import java.util.List;
import java.util.Map;

public abstract class State{
    private final GameModel model;
    private final GameController controller;
    protected StatePhase phase;
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

    private void exception() throws InvalidStateException {
        throw new InvalidStateException("Cannot perform this action in " + this + " state");
    }
    
    public void takeComponentFromUnviewed(Player player, int index) throws InvalidStateException, ComponentNotFoundException {
        exception();
    }
    public void takeComponentFromViewed(Player player, int index)throws InvalidStateException, ComponentNotFoundException {
        exception();
    }
    public void takeComponentFromBooked(Player player, int index) throws InvalidStateException, ComponentNotFoundException {
        exception();
    }
    public void addComponentToBooked(Player player) throws InvalidStateException, NoSpaceException {
        exception();
    }
    public void addComponentToViewed(Player player) throws InvalidStateException, DuplicateComponentException {
        exception();
    }
    public void placeComponent(Player player, Pair<Integer, Integer> coordinates) throws InvalidStateException, InvalidTileException{
        exception();
    }
    public void rotateComponentClockwise(Player player) throws InvalidStateException{
        exception();
    }
    public void rotateComponentCounterclockwise(Player player) throws InvalidStateException{
        exception();
    }
    public void stopAssembling(Player player, int position) throws InvalidIndexException, InvalidStateException {
        exception();
    }
    public List<AdventureCard> peekDeck(Player player, int num) throws InvalidIndexException, InvalidStateException {
        exception();
        return null;
    }
    public int getHourglassTime(Player player) throws InvalidStateException{
        exception();
        return 0;
    }
    public void turnHourglass(Player player) throws HourglassException, InvalidStateException {
        exception();
    }
    public boolean isShipValid(Player player) throws InvalidStateException {
        exception();
        return false;
    }
    public void removeComp(Player player, Pair<Integer, Integer> coordinates) throws ComponentNotFoundException, InvalidStateException {
        exception();
    }

    public boolean allShipsValidated() throws InvalidStateException{
        exception();
        return false;
    }

    public void addAlien(Player player, AlienColor color, Pair<Integer, Integer> cabin) throws InvalidAlienPlacement, InvalidStateException {
        exception();
    }
    public void initAllShips() throws InvalidStateException {
        exception();
    }

    public void loadCargo(Player player, CargoColor loaded, Pair<Integer, Integer> ch) throws InvalidStateException, InvalidTurnException, CargoException, CargoNotLoadable, CargoFullException {
        exception();
    }
    public void unloadCargo(Player player, CargoColor lost, Pair<Integer, Integer> ch) throws InvalidStateException, InvalidTurnException, CargoException, CargoNotLoadable, CargoFullException, InvalidCargoException {
        exception();
    }
    public void moveCargo(Player player, CargoColor cargo, Pair<Integer, Integer> from, Pair<Integer, Integer> to) throws InvalidStateException, InvalidTurnException, CargoException, CargoNotLoadable, CargoFullException, InvalidCargoException {
        exception();
    }
    public void loseEnergy(Player player, Pair<Integer, Integer> battery) throws InvalidStateException, InvalidTurnException, EnergyException {
        exception();
    }
    public void landOnPlanet(Player player, int planetIndex) throws InvalidStateException, InvalidTurnException {
        exception();
    }
    public void acceptCard(Player player) throws InvalidStateException, InvalidTurnException {
        exception();
    }

    public void loseCrew(Player player, List<Pair<Integer, Integer>> cabins) throws InvalidStateException, InvalidTurnException, EmptyCabinException {
        exception();
    }
    public void endMove(Player player) throws InvalidStateException, InvalidTurnException, InvalidShipException {
        exception();
    }

    public void activateEngines(Player player, List<Pair<Integer, Integer>> engines, List<Pair<Integer, Integer>> batteries) throws InvalidStateException, InvalidTurnException, InvalidShipException, InvalidEngineException, EnergyException, DieNotRolledException {
        exception();
    }
    public void activateShield(Player player, Pair<Integer, Integer> shield, Pair<Integer, Integer> battery) throws InvalidStateException, InvalidTurnException, InvalidShipException, EnergyException, DieNotRolledException {
        exception();
    }
    public int shootEnemy(Player player, List<Pair<Integer, Integer>> cannons, List<Pair<Integer, Integer>> batteries) throws InvalidStateException, InvalidTurnException, InvalidCannonException, EnergyException {
        exception();
        return 0;
    }
    public void activateCannons(Player player, List<Pair<Integer, Integer>> cannons, List<Pair<Integer, Integer>> batteries) throws InvalidStateException, InvalidTurnException, InvalidShipException, InvalidCannonException, EnergyException, DieNotRolledException {
        exception();
    }

    public boolean allAssembled() throws InvalidStateException {
        exception();
        return false;
    }

    public boolean allShipsReadyToFly() throws InvalidStateException {
        exception();
        return false;
    }

    public void readyToFly(Player player) throws InvalidStateException, InvalidShipException {
        exception();
    }

    public void automaticAction() throws InvalidStateException {
        exception();
    }

    public void resume() throws InvalidStateException {
        exception();
    }

    public void killGame() throws InvalidStateException {
        exception();
    }

    public Map<String, Integer> getScore() throws InvalidStateException {
        exception();
        return null;
    }

    public void chooseBranch(Player player, Pair<Integer, Integer> coordinates) throws InvalidTurnException, InvalidStateException {
        exception();
    }

    public void setCurrentPlayer(String currentPlayer) throws InvalidStateException {
        exception();
    }

    public void nextPlayer() throws InvalidStateException{
        exception();
    }

    public int rollDice(Player player) throws InvalidStateException, InvalidTurnException, InvalidShipException, DieNotRolledException {
        exception();
        return 0;
    }

    public void nextRound(Player player) throws InvalidStateException{
        exception();
    }

    public String getCurrentPlayer() throws InvalidStateException{
        exception();
        return null;
    }

    public void currentQuit(Player playerByID) throws InvalidTurnException, InvalidShipException, InvalidStateException {
        exception();
    }

    public void endTurn()throws InvalidStateException{
        exception();
    }
}