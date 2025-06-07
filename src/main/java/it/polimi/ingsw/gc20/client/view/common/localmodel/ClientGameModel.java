package it.polimi.ingsw.gc20.client.view.common.localmodel;

import it.polimi.ingsw.gc20.client.network.common.Client;
import it.polimi.ingsw.gc20.client.view.TUI.MenuState;
import it.polimi.ingsw.gc20.client.view.TUI.TUI;
import it.polimi.ingsw.gc20.client.view.common.ViewLobby;
import it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards.*;
import it.polimi.ingsw.gc20.client.view.common.localmodel.board.ViewBoard;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
import it.polimi.ingsw.gc20.common.interfaces.ViewInterface;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;
import it.polimi.ingsw.gc20.server.model.cards.Planet;
import it.polimi.ingsw.gc20.server.model.cards.Projectile;
import it.polimi.ingsw.gc20.server.model.components.Direction;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;
import it.polimi.ingsw.gc20.server.model.cards.FireType;
import it.polimi.ingsw.gc20.client.view.common.localmodel.LobbyListObserver;
import java.util.ArrayList;

public abstract class ClientGameModel extends UnicastRemoteObject implements ViewInterface {
    private static final Logger LOGGER = Logger.getLogger(ClientGameModel.class.getName());
    private static ClientGameModel instance;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private ViewShip playerShip;
    private ViewLobby currentLobby;
    private GamePhase currentPhase;
    private String errorMessage;
    public boolean loggedIn;
    protected String username;
    protected Client client;
    protected ViewBoard board;
    protected Map<String, ViewShip> ships;
    protected ViewAdventureCard currentCard;
    private final List<GameModelListener> listeners = new ArrayList<>();
    private ViewComponent componentInHand;
    private List<ViewLobby> lobbyList;
    private MenuState currentMenuState;
    public boolean busy;
    private final BlockingQueue<MenuState> menuStateQueue = new LinkedBlockingQueue<>();
    private final List<LobbyListObserver> lobbyListObservers = new ArrayList<>();

    public ClientGameModel() throws RemoteException {
        super();
        // Initialize default state if necessary
        this.loggedIn = false;
        this.username = null;
        this.client = null;
        this.ships = new HashMap<>();
    }
    public ViewAdventureCard getCurrentCard() {
        return currentCard;
    }

    public void setCurrentCard(ViewAdventureCard currentCard) {
        this.currentCard = currentCard;
    }

    public MenuState getCurrentMenuState() {
        return currentMenuState;
    }
    public void setBusy() {
        busy = true;
    }
    public void setFree(){
        busy = false;
        while (!menuStateQueue.isEmpty()) {
            currentMenuState = menuStateQueue.poll();
            if (currentMenuState != null) {
                TUI.clearConsole();
                currentMenuState.displayMenu();
            }
        }
    }
    /**
     * Sets the current menu state and displays it if the model is not busy.
     * If the model is busy, the new state is added to a queue to be processed later.
     *
     * @param currentMenuState The new menu state to set.
     * @apiNote This method is used to change the current menu state in the game model. GUI NEED TO REIMPLEMENT THIS METHOD
     */
    public void setCurrentMenuState(MenuState currentMenuState) {
        if (busy){
            menuStateQueue.add(currentMenuState);
        } else {
            if (menuStateQueue.isEmpty()) {
                this.currentMenuState = currentMenuState;
                TUI.clearConsole();
                this.currentMenuState.displayMenu();
            } else {
                menuStateQueue.add(currentMenuState);
            }
        }
    }

    public abstract void displayErrorMessage(String message);

    public void setLobbyList(List<ViewLobby> lobbyList) {
        this.lobbyList = new ArrayList<>(lobbyList);
        notifyLobbyListObservers();
        LOGGER.fine("Lobby list updated in model.");
    }

    public List<ViewLobby> getLobbyList() {
        return lobbyList;
    }

    public String getUsername() {
        return username;
    }

    public ViewComponent getComponentInHand() {
        return componentInHand;
    }

    public void setComponentInHand(ViewComponent componentInHand) {
        this.componentInHand = componentInHand;
        LOGGER.fine("Component in hand updated in model.");
        for (GameModelListener listener : listeners) {
            listener.onComponentInHandUpdated(this.componentInHand);
        }
    }
    public ViewBoard getBoard() {
        return board;
    }

    public void setBoard(ViewBoard board) {
        this.board = board;
    }

    public Map<String, ViewShip> getShips() {
        return ships;
    }

    public void setShips(Map<String, ViewShip> ships) {
        this.ships = ships;
    }

    public ViewShip getShip (String username) {
        return ships.get(username);
    }

    public void setShip (String username, ViewShip ship) {
        ships.put(username, ship);
    }

    public void ping() {
        client.pong(username);
    }

    public void addListener(GameModelListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void addLobbyListObserver(LobbyListObserver observer) {
        if (!lobbyListObservers.contains(observer)) {
            lobbyListObservers.add(observer);
        }
    }

    public void removeListener(GameModelListener listener) {
        listeners.remove(listener);
    }

    protected void notifyLobbyListObservers() {
        for (LobbyListObserver observer : lobbyListObservers) {
            observer.onLobbyListChanged();
        }
    }

    public static ClientGameModel getInstance() {
        return instance;
    }

    public static void setInstance(ClientGameModel instance) {
        ClientGameModel.instance = instance;
    }

    public void printShip(String username) {
        ViewShip ship = ships.get(username);
        if (ship != null) {
            System.out.println(ship);
        } else {
            LOGGER.warning("No ship found for " + username);
        }
    }

    public void printBoard() {
        if (board != null) {
            System.out.println(board);
        } else {
            LOGGER.warning("No board found.");
        }
    }

    public void printCardsInLine(List<ViewAdventureCard> cards) {
        if (cards == null || cards.isEmpty()) {
            return;
        }

        final int cardsPerRow = 10;

        int numCardRows = (int) Math.ceil((double) cards.size() / cardsPerRow);

        StringBuilder finalResult = new StringBuilder();

        for (int cardRow = 0; cardRow < numCardRows; cardRow++) {
            int startIdx = cardRow * cardsPerRow;
            int endIdx = Math.min(startIdx + cardsPerRow, cards.size());
            List<ViewAdventureCard> rowCards = cards.subList(startIdx, endIdx);
            for (int i = 0; i < 11; i++) {
                for (ViewAdventureCard rowCard : rowCards) {
                    finalResult.append(rowCard.toLine(i));
                    finalResult.append("  ");
                }
                finalResult.append("\n");
            }
            finalResult.append("\n");
        }
        System.out.println(finalResult);
    }

    public void printCurrentCard() {
        if (currentCard != null) {
            System.out.println(currentCard);
        } else {
            System.out.println("No active card.");
        }
    }

    public void printViewedPile(){
        if (board != null) {
            List<ViewComponent> comps = board.viewedPile;
            if (comps != null && !comps.isEmpty()) {
                String out = printComponentsInLine(comps);
                System.out.println(out);
            } else {
                System.out.println("No components here.");
            }
        } else {
            LOGGER.warning("No board found.");
        }
    }

    public String printComponentsInLine(List<ViewComponent> components) {
        if (components == null || components.isEmpty()) {
            return "";
        }

        final int componentsPerRow = 10;

        int numComponentRows = (int) Math.ceil((double) components.size() / componentsPerRow);

        StringBuilder finalResult = new StringBuilder();

        for (int componentRow = 0; componentRow < numComponentRows; componentRow++) {
            int startIdx = componentRow * componentsPerRow;
            int endIdx = Math.min(startIdx + componentsPerRow, components.size());
            List<ViewComponent> rowComponents = components.subList(startIdx, endIdx);
            for (int i = 0; i < 5; i++) {
                for (ViewComponent rowComponent : rowComponents) {
                    finalResult.append(rowComponent.toLine(i));
                    finalResult.append("  ");
                }
                finalResult.append("\n");
            }
        }
        return finalResult.toString();
    }

    @Override
    public void updateView(Message message) throws RemoteException {
        // Submit message handling to the executor for asynchronous processing
        executor.submit(() -> {
            try {
                message.handleMessage();
            } catch (Exception e) {
                LOGGER.warning("Error while handling message: " + e.getMessage());
            }
        });
    }

    public void updatePlayerShip(ViewShip ship) {
        this.playerShip = ship;
        LOGGER.fine("Player ship updated in model.");
        for (GameModelListener listener : listeners) {
            listener.onShipUpdated(this.playerShip);
        }
    }

    public void updateLobby(ViewLobby lobby) {
        this.currentLobby = lobby;
        LOGGER.fine("Lobby updated in model: " + (lobby != null ? lobby.getID() : "null"));
        for (GameModelListener listener : listeners) {
            listener.onLobbyUpdated(this.currentLobby);
        }
    }

    public void setErrorMessage(String message) {
        this.errorMessage = message;
        LOGGER.warning("Error message set in model: " + message);
        for (GameModelListener listener: listeners) {
            listener.onErrorMessageReceived(message);
        }
    }

    public abstract void login();

    public void setUsername(String username){
        this.username = username;
    }

    // --- Getters ---
    public ViewShip getPlayerShip() { return playerShip; }
    public ViewLobby getCurrentLobby() { return currentLobby; }
    public GamePhase getCurrentPhase() { return currentPhase; }
    public ViewPlayer[] getPlayers() {
        return board.players;
    }
    public String getErrorMessage() { return errorMessage; }


    public Client getClient() {
        return client;
    }

    public abstract void shutdown();
    public abstract void branchMenu();
    public abstract void buildingMenu(List<ViewAdventureCard> cards);
    public abstract void cannonsMenu(String message);
    public abstract void cardAcceptanceMenu(String message);
    public abstract void cargoMenu(String message, int cargoToLose, List<CargoColor> cargoToGain, boolean losing);
    public abstract void engineMenu(String message);
    public abstract void inLobbyMenu();
    public abstract void mainMenuState();
    public abstract void planetMenu(List<Planet> planets);
    public abstract void populateShipMenu();
    public abstract void automaticAction(String message);
    public abstract void validationMenu();
    public abstract void init();
    public abstract void shieldsMenu(String message);
    public abstract void rollDiceMenu(String message);
    public abstract void cargoMenu(int cargoNum);
    public abstract void loseCrewMenu(int crewNum);
    public abstract void removeBatteryMenu(int batteryNum);
    public abstract void AssemblingStateMenu();
    public abstract void leaderBoardMenu(Map<String, Integer> leaderBoard);
    public abstract void loginSuccessful(String username);
    public abstract void loginFailed(String username);
    public abstract void idleMenu(String message);
    public abstract void keepPlayingMenu();
}