package it.polimi.ingsw.gc20.server.controller.states;


import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.exceptions.*;
import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.server.model.cards.Planet;
import it.polimi.ingsw.gc20.server.model.components.AlienColor;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.server.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.server.model.player.Player;
import org.javatuples.Pair;

import java.util.List;

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

    public StatePhase getPhase() {
        return phase;
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
    public void placeComponent(Player player, Pair<Integer, Integer> coordinates) throws InvalidStateException, InvalidTileException, ComponentNotFoundException{
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
    public void turnHourglass(Player player) throws HourglassException, InvalidStateException {
        exception();
    }
    public boolean isShipValid(Player player) throws InvalidStateException {
        exception();
        return false;
    }
    public void removeComp(Player player, Pair<Integer, Integer> coordinates) throws InvalidStateException, InvalidTileException, ComponentNotFoundException {
        exception();
    }
    public boolean allShipsValidated() throws InvalidStateException{
        exception();
        return false;
    }
    public void addAlien(Player player, AlienColor color, Pair<Integer, Integer> cabin) throws InvalidAlienPlacement, InvalidStateException, ComponentNotFoundException {
        exception();
    }
    public void initAllShips() throws InvalidStateException {
        exception();
    }
    public void loadCargo(Player player, CargoColor loaded, Pair<Integer, Integer> ch) throws InvalidStateException, InvalidTurnException, CargoException, CargoNotLoadable, CargoFullException, ComponentNotFoundException {
        exception();
    }
    public void unloadCargo(Player player, CargoColor lost, Pair<Integer, Integer> ch) throws ComponentNotFoundException, InvalidStateException, InvalidTurnException, CargoException, CargoNotLoadable, CargoFullException, InvalidCargoException {
        exception();
    }
    public void moveCargo(Player player, CargoColor cargo, Pair<Integer, Integer> from, Pair<Integer, Integer> to) throws InvalidStateException, InvalidTurnException, CargoException, CargoNotLoadable, CargoFullException, InvalidCargoException, ComponentNotFoundException {
        exception();
    }
    public void loseEnergy(Player player, Pair<Integer, Integer> battery) throws InvalidStateException, InvalidTurnException, EnergyException, ComponentNotFoundException {
        exception();
    }
    public void landOnPlanet(Player player, int planetIndex) throws InvalidStateException, InvalidTurnException {
        exception();
    }
    public void acceptCard(Player player) throws InvalidStateException, InvalidTurnException {
        exception();
    }
    public void loseCrew(Player player, List<Pair<Integer, Integer>> cabins) throws InvalidStateException, InvalidTurnException, EmptyCabinException, ComponentNotFoundException {
        exception();
    }
    public void endMove(Player player) throws InvalidStateException, InvalidTurnException, InvalidShipException {
        exception();
    }
    public void activateEngines(Player player, List<Pair<Integer, Integer>> engines, List<Pair<Integer, Integer>> batteries) throws InvalidStateException, InvalidTurnException, InvalidShipException, InvalidEngineException, EnergyException, DieNotRolledException, ComponentNotFoundException {
        exception();
    }
    public void activateShield(Player player, Pair<Integer, Integer> shield, Pair<Integer, Integer> battery) throws InvalidStateException, InvalidTurnException, InvalidShipException, EnergyException, DieNotRolledException, ComponentNotFoundException {
        exception();
    }
    public int shootEnemy(Player player, List<Pair<Integer, Integer>> cannons, List<Pair<Integer, Integer>> batteries) throws InvalidStateException, InvalidTurnException, InvalidCannonException, EnergyException, ComponentNotFoundException {
        exception();
        return 0;
    }
    public void activateCannons(Player player, List<Pair<Integer, Integer>> cannons, List<Pair<Integer, Integer>> batteries) throws InvalidStateException, InvalidTurnException, InvalidShipException, InvalidCannonException, EnergyException, DieNotRolledException, ComponentNotFoundException {
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
    public void automaticAction() throws InvalidStateException {
        exception();
    }
    public void resume() throws InvalidStateException {
        exception();
    }
    public void killGame() throws InvalidStateException {
        exception();
    }
    public void getScore() throws InvalidStateException {
        exception();
    }
    public void chooseBranch(Player player, Pair<Integer, Integer> coordinates) throws InvalidTurnException, InvalidStateException, ComponentNotFoundException {
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
    public void nextRound() throws InvalidStateException{
        exception();
    }
    public String getCurrentPlayer() throws InvalidStateException{
        return null;
    }
    public void currentQuit(Player playerByID) throws InvalidTurnException, InvalidShipException, InvalidStateException {
        exception();
    }
    public void endTurn()throws InvalidStateException{
        exception();
    }

    public boolean isConcurrent() {
        return false;
    }

    public void rejoin(String username) throws InvalidStateException {
        exception();
    }

    public String createsCannonsMessage(){
        return null;
    }

    public String createsEnginesMessage(){
        return null;
    }

    public String createsShieldMessage(){
        return null;
    }

    public String createsRollDiceMessage(){
        return null;
    }

    public int getCrew(){
        return 0;
    }

    public String getAutomaticActionMessage() {
        return "automatic action is taking place";
    }

    public int cargoToRemove(){
        return 0;
    }

    public List<CargoColor> cargoReward() {
        return null;
    }

    public List<Planet> getPlanets() {
        return null;
    }
}