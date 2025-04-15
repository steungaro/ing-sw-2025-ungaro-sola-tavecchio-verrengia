package it.polimi.ingsw.gc20.interfaces;

import it.polimi.ingsw.gc20.exceptions.*;
import it.polimi.ingsw.gc20.model.cards.*;
import it.polimi.ingsw.gc20.model.components.*;
import it.polimi.ingsw.gc20.model.player.*;
import it.polimi.ingsw.gc20.model.gamesets.*;

import java.rmi.Remote;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public interface GameControllerInterface extends Remote {

    // Player management
    void giveUp(String username);
    void disconnectPlayer(String username);
    boolean reconnectPlayer(String username);
    List<String> getAllUsernames();
    List<String> getDisconnectedPlayers();
    boolean isPlayerDisconnected(String username);
    void setPlayerColor(String username, PlayerColor color);


    List<PlayerColor> getAvailableColors();
    Player getPlayerData(String asker, String asked);

    // Game state
    String getState();
    Map<String, Integer> getPlayerScores();

    // Ship assembly
    Component takeComponentFromUnviewed(String username, Component component);
    Component takeComponentFromViewed(String username, Component component);
    Component takeComponentFromBooked(String username, Component component);
    void addComponentToBooked(String username, Component component);
    void addComponentToViewed(String username, Component component);
    void placeComponent(String username, Component component, int x, int y);

    void rotateComponentClockwise(Component component);
    void rotateComponentCounterclockwise(Component component);

    void stopAssembling(String username, int position);
    List<AdventureCard> peekDeck(String username, int num);
    int getHourglassTime(String username);
    void turnHourglass(String username) throws HourglassException;

    // Ship validating
    boolean validateShip(String username);
    void removeComponentFromShip(String username, Component component);
    void addAlien(String username, AlienColor color, Cabin cabin);
    void readyToFly(String username);

    // Gameplay
    void chooseBranch(String username, int col, int row) throws InvalidTurnException, InvalidShipException;
    int rollDice(String username) throws IllegalStateException, InvalidTurnException;
    int lastRolledDice(String username) throws IllegalStateException, InvalidTurnException;
    AdventureCard getActiveCard();
    void landOnPlanet(String username, int planetIndex) throws IllegalStateException, InvalidTurnException;
    void loadCargo(String username, CargoColor loaded, CargoHold ch) throws IllegalStateException, InvalidTurnException, CargoException;
    void unloadCargo(String username, CargoColor lost, CargoHold ch) throws IllegalStateException, InvalidTurnException, CargoException;
    void moveCargo(String username, CargoColor cargo, CargoHold from, CargoHold to) throws IllegalStateException, InvalidTurnException, CargoException;
    void acceptCard(String username) throws IllegalStateException, InvalidTurnException;
    void loseCrew(String username, List<Cabin> cabins) throws IllegalStateException, InvalidTurnException;
    void endMove(String username) throws IllegalStateException, InvalidTurnException, InvalidShipException;
    int shootEnemy(String username, List<Cannon> cannons, List<Battery> batteries) throws IllegalStateException, InvalidTurnException, InvalidShipException;

    // Activate ship components
    void activateEngines(String username, List<Engine> engines, List<Battery> batteries) throws IllegalStateException, InvalidTurnException, InvalidShipException;
    void activateShield(String username, Shield shield, Battery battery) throws IllegalStateException, InvalidTurnException, InvalidShipException;
    void activateCannons(String username, List<Cannon> cannons, List<Battery> batteries) throws IllegalStateException, InvalidTurnException, InvalidShipException;

    // Game end
    Map<String, Integer> getScore() throws IllegalStateException;
}