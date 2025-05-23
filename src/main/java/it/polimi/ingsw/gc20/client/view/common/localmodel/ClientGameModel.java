package it.polimi.ingsw.gc20.client.view.common.localmodel;

import it.polimi.ingsw.gc20.client.network.common.Client;
import it.polimi.ingsw.gc20.client.view.TUI.MenuState;
import it.polimi.ingsw.gc20.client.view.common.ViewLobby;
import it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards.ViewAdventureCard;
import it.polimi.ingsw.gc20.client.view.common.localmodel.board.ViewBoard;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
import it.polimi.ingsw.gc20.common.interfaces.ViewInterface;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;
import it.polimi.ingsw.gc20.server.model.cards.Planet;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import it.polimi.ingsw.gc20.server.model.cards.FireType;


public abstract class ClientGameModel extends UnicastRemoteObject implements ViewInterface {
    private static final Logger LOGGER = Logger.getLogger(ClientGameModel.class.getName());
    private static ClientGameModel instance;
    private final ExecutorService executor = Executors.newFixedThreadPool(2);
    private ViewShip playerShip;
    private ViewLobby currentLobby;
    private GamePhase currentPhase;
    private List<ViewPlayer> players;
    private String errorMessage;
    public boolean loggedIn;
    protected String username;
    protected Client client;
    private ViewBoard board;
    private Map<String, ViewShip> ships;
    protected ViewAdventureCard currentCard;
    private final List<GameModelListener> listeners = new ArrayList<>();
    private ViewComponent componentInHand;
    private List<ViewLobby> lobbyList;

    public ClientGameModel() throws RemoteException {
        super();
        // Initialize default state if necessary
        this.players = new ArrayList<>();
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

    public void setLobbyList(List<ViewLobby> lobbyList) {
        this.lobbyList = lobbyList;
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

    public void setPlayers(List<ViewPlayer> players) {
        this.players = players;
    }

    public void ping() {
        client.pong(username);
    }

    public void addListener(GameModelListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(GameModelListener listener) {
        listeners.remove(listener);
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

//    public void printDeck(int index) {
//        if (board != null) {
//            List<ViewAdventureCard> cards = board.decks.get(index);
//            if (cards != null) {
//                String out = printCardsInLine(cards);
//                System.out.println(out);
//                LOGGER.info("Deck " + index + ":\n");
//            } else {
//                LOGGER.warning("No card found at index " + index);
//            }
//        } else {
//            LOGGER.warning("No deck found at index " + index);
//        }
//    }

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
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < rowCards.size(); j++) {
                    finalResult.append(rowCards.get(j).toLine(i));
                    finalResult.append("  ");
                    if (j == rowCards.size() - 1) {
                        finalResult.append("\n");
                    }
                }
            }
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
            if (comps != null) {
                String out = printComponentsInLine(comps);
                System.out.println(out);
            } else {
                System.out.println("No view pile found.");
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
            for (int j = 0; j < 5; j++) {
                for (int i = 0; i <rowComponents.size(); i++) {
                    finalResult.append(rowComponents.get(i).toLine(j));
                    finalResult.append("  ");
                    if (j == rowComponents.size() - 1) {
                        finalResult.append("\n");
                    }
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

    // --- Updaters that notify listeners ---
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

    public void updateGamePhase(GamePhase newPhase) {
        this.currentPhase = newPhase;
        LOGGER.fine("Game phase updated in model to: " + newPhase);
        for (GameModelListener listener : listeners) {
            listener.onPhaseChanged(this.currentPhase);
        }
    }
    
    public void setErrorMessage(String message) {
        this.errorMessage = message;
        LOGGER.warning("Error message set in model: " + message);
        for (GameModelListener listener: listeners) {
            listener.onErrorMessageReceived(message);
        }
    }

    // --- Getters ---
    public ViewShip getPlayerShip() { return playerShip; }
    public ViewLobby getCurrentLobby() { return currentLobby; }
    public GamePhase getCurrentPhase() { return currentPhase; }
    public List<ViewPlayer> getPlayers() { return players; }
    public String getErrorMessage() { return errorMessage; }


    public abstract void display(String message);

    public abstract void display(MenuState menuState);


    public Client getClient() {
        return client;
    }

    public abstract void shutdown();

    public abstract void branchMenu();
    public abstract void buildingMenu(List<ViewAdventureCard> cards);
    public abstract void cannonsMenu(String message);
    public abstract void cardAcceptanceMenu(String message);
    public abstract void cargoMenu(String message, int cargoToLose, List<CargoColor> cargoToGain);
    public abstract void engineMenu(String message);
    public abstract void inLobbyMenu();
    public abstract void mainMenuState();
    public abstract void planetMenu(List<Planet> planets);
    public abstract void populateShipMenu();
    public abstract void automaticAction(String message);
    public abstract void validationMenu();
    public abstract void takeComponentMenu();
    public abstract void init();
    public abstract void shieldsMenu(FireType fireType, int direction, int line);
    public abstract void rollDiceMenu(FireType fireType, int direction);
    public abstract void cargoMenu(int cargoNum);
    public abstract void loseCrewMenu(int crewNum);
    public abstract void removeBatteryMenu(int batteryNum);
    public abstract void placeComponentMenu();
    public abstract void leaderBoardMenu(Map<String, Integer> leaderBoard);
    public abstract void loginSuccessful(String username);
    public abstract void loginFailed(String username);
}