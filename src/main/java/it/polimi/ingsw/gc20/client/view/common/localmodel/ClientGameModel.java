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
import it.polimi.ingsw.gc20.common.message_protocol.Message;
import it.polimi.ingsw.gc20.server.model.cards.Planet;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;
import java.util.ArrayList;

/**
 * Abstract class representing the client-side game model.
 * It handles the game state, player actions, and communication with the server.
 * This class is designed to be extended by specific implementations for different game modes or interfaces.
 */
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

    /**
     * Constructor for the ClientGameModel.
     * Initializes the game model with default values and prepares it for use.
     *
     * @throws RemoteException if there is an issue with remote method invocation
     */
    public ClientGameModel() throws RemoteException {
        super();
        // Initialize the default state if necessary
        this.loggedIn = false;
        this.username = null;
        this.client = null;
        this.ships = new HashMap<>();
    }

    /**
     * Returns the current adventure card being viewed.
     * If no card is set, it returns a default empty card representation.
     *
     * @return the current adventure card or an empty card if none is set
     */
    public ViewAdventureCard getCurrentCard() {
        if (currentCard == null) {
            return new ViewAdventureCard(
            ) {
                /**
                 * Converts a specific part of the card to its string representation
                 * based on the given line index.
                 *
                 * @param i the index of the line to retrieve as a string representation
                 * @return the string representation of the specified line; returns an empty string if the line index is invalid
                 */
                @Override
                public String toLine(int i) {
                    return switch (i) {
                        case 0 -> UP;
                        case 1, 2, 3, 4, 5, 6, 7, 8, 9 -> LATERAL + EMPTY_ROW + LATERAL;
                        case 10 -> DOWN;
                        default -> "";
                    };
                }
            };
        }
        return currentCard;
    }

    /**
     * Sets the current adventure card being viewed.
     * This method updates the current card and notifies listeners about the change.
     *
     * @param currentCard the new adventure card to set as the current card
     */
    public void setCurrentCard(ViewAdventureCard currentCard) {
        this.currentCard = currentCard;
        for (GameModelListener listener : listeners) {
            listener.onCurrentCardUpdated(this.currentCard);
        }
    }

    /**
     * Returns the current menu state of the game model.
     * This method is used to retrieve the current state of the menu being displayed to the user.
     *
     * @return the current MenuState
     */
    public MenuState getCurrentMenuState() {
        return currentMenuState;
    }

    /**
     * Sets the model to a busy state, indicating that it is currently processing an operation.
     * This method is typically used to prevent new screen updates on the TUI while an operation is in progress.
     */
    public void setBusy() {
        busy = true;
    }

    /**
     * Sets the model to a free state, allowing it to process new menu states.
     * If there are any queued menu states, it processes them in order.
     * This method is typically called after an operation is completed to allow the TUI to update.
     */
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

    /**
     * Sets the current menu state without clearing any queued menu states. If the system is busy,
     * the specified menu state is added to a queue. Otherwise, it is immediately set as the current
     * menu state and displayed.
     *
     * @param currentMenuState menu state to set or queue.
     */
    public void setCurrentMenuStateNoClear(MenuState currentMenuState) {
        if (busy){
            menuStateQueue.add(currentMenuState);
        } else {
            if (menuStateQueue.isEmpty()) {
                this.currentMenuState = currentMenuState;
                this.currentMenuState.displayMenu();
            } else {
                menuStateQueue.add(currentMenuState);
            }
        }
    }

    /**
     * Displays an error message to the user.
     * This method is abstract and should be implemented by subclasses to provide specific error handling.
     *
     * @param message the error message to display
     */
    public abstract void displayErrorMessage(String message);

    /**
     * Sets the list of lobbies available in the game.
     * This method updates the lobby list and notifies all observers about the change.
     *
     * @param lobbyList the new list of lobbies to set
     */
    public void setLobbyList(List<ViewLobby> lobbyList) {
        this.lobbyList = new ArrayList<>(lobbyList);
        notifyLobbyListObservers();
        LOGGER.fine("Lobby list updated in model.");
    }

    /**
     * Returns the list of lobbies available in the game.
     * This method provides access to the current list of lobbies.
     *
     * @return the list of ViewLobby objects representing the available lobbies
     */
    public List<ViewLobby> getLobbyList() {
        return lobbyList;
    }

    /**
     * Returns the username of the player.
     * This method provides access to the player's username.
     *
     * @return the username of the player
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the component currently held in hand.
     *
     * @return the ViewComponent currently in hand, or null if none is held
     */
    public ViewComponent getComponentInHand() {
        return componentInHand;
    }

    /**
     * Sets the component currently held in hand.
     * This method updates the component in the hand and notifies all listeners about the change.
     *
     * @param componentInHand the ViewComponent to set as the current component in hand
     */
    public void setComponentInHand(ViewComponent componentInHand) {
        this.componentInHand = componentInHand;
        LOGGER.fine("Component in hand updated in model.");
        for (GameModelListener listener : listeners) {
            listener.onComponentInHandUpdated(this.componentInHand);
        }
    }

    /**
     * Returns the game board.
     * This method provides access to the current game board.
     *
     * @return the ViewBoard representing the game board
     */
    public ViewBoard getBoard() {
        return board;
    }


    /**
     * Sets the game board.
     * This method updates the game board and notifies all listeners about the change.
     *
     * @param board the ViewBoard to set as the current game board
     */
    public void setBoard(ViewBoard board) {
        this.board = board;
        for (GameModelListener listener : listeners) {
            listener.onBoardUpdated(this.board);
        }

    }

    /**
     * Returns the map of ships, where the key is the username and the value is the ViewShip.
     * This method provides access to all ships in the game.
     *
     * @return a map of usernames to their corresponding ViewShip objects
     */
    public Map<String, ViewShip> getShips() {
        return ships;
    }

    /**
     * Sets the map of ships in the game.
     *
     * @param ships a map of usernames to their corresponding ViewShip objects
     */
    public void setShips(Map<String, ViewShip> ships) {
        this.ships = ships;
    }

    /**
     * Returns the ship associated with the given username.
     * This method retrieves the ViewShip for a specific player.
     *
     * @param username the username of the player whose ship is to be retrieved
     * @return the ViewShip associated with the given username, or null if not found
     */
    public ViewShip getShip (String username) {
        return ships.get(username);
    }

    /**
     * Sets the ship for a specific player.
     * This method updates the ship for the given username and notifies all listeners about the change.
     *
     * @param username the username of the player whose ship is to be set
     * @param ship the ViewShip to set for the player
     */
    public void setShip (String username, ViewShip ship) {
        ships.put(username, ship);
        for (GameModelListener listener : listeners) {
            listener.onShipUpdated(this.ships.get(username));
        }
    }

    /**
     * Sends a ping to the server to check connectivity.
     * This method is used to keep the connection alive and ensure the server is responsive.
     */
    public void ping() {
        client.pong(username);
    }

    /**
     * Adds a listener to the game model.
     * This method allows other components to listen for changes in the game model.
     *
     * @param listener the GameModelListener to add
     */
    public void addListener(GameModelListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     * Adds a lobby list listener to the game model.
     * This method allows listeners to be notified when the lobby list changes.
     *
     * @param observer the LobbyListListener to add
     */
    public void addLobbyListObserver(LobbyListObserver observer) {
        if (!lobbyListObservers.contains(observer)) {
            lobbyListObservers.add(observer);
        }
    }

    /**
     * Removes a lobby list listener from the game model.
     * This method allows listeners to stop receiving notifications about lobby list changes.
     *
     * @param listener the LobbyListListener to remove
     */
    public void removeListener(GameModelListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifies all lobby list observers that the lobby list has changed.
     */
    protected void notifyLobbyListObservers() {
        for (LobbyListObserver observer : lobbyListObservers) {
            observer.onLobbyListChanged();
        }
    }

    /**
     * Returns the singleton instance of ClientGameModel.
     * This method provides access to the single instance of the game model.
     *
     * @return the singleton instance of ClientGameModel
     */
    public static ClientGameModel getInstance() {
        return instance;
    }

    /**
     * Sets the singleton instance of ClientGameModel.
     * This method is used to initialize the game model instance.
     *
     * @param instance the ClientGameModel instance to set
     */
    public static void setInstance(ClientGameModel instance) {
        ClientGameModel.instance = instance;
    }

    /**
     * Prints the ship of the player with the given username.
     * This method retrieves the ship from the map and prints its details. It's used in TUI to display the player's ship.
     *
     * @param username the username of the player whose ship is to be printed
     */
    public void printShip(String username) {
        ViewShip ship = ships.get(username);
        if (ship != null) {
            System.out.println(ship);
        } else {
            LOGGER.warning("No ship found for " + username);
        }
    }

    /**
     * Prints the current game board.
     * This method displays the state of the game board in the console.
     * If no board is set, it logs a warning message.
     */
    public void printBoard() {
        if (board != null) {
            System.out.println(board);
        } else {
            LOGGER.warning("No board found.");
        }
    }

    /**
     * Prints the adventure cards in a formatted line.
     * This method displays the adventure cards in a grid-like format, with a maximum of 10 cards per row.
     *
     * @param cards the list of ViewAdventureCard objects to print
     */
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

    /**
     * Prints the current adventure card being viewed.
     * This method displays the details of the current card in the console.
     * If no card is set, it prints a message indicating that there is no active card.
     */
    public void printCurrentCard() {
        if (currentCard != null) {
            System.out.println(currentCard);
        } else {
            System.out.println("No active card.");
        }
    }

    /**
     * Prints the viewed pile of components.
     * This method retrieves the viewed pile from the board and prints its components in a formatted line.
     * If no components are present, it prints a message indicating that there are no components.
     */
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

    /**
     * Prints the components in a formatted line.
     * This method displays the components in a grid-like format, with a maximum of 10 parts per row.
     *
     * @param components the list of ViewComponent objects to print
     * @return a string representation of the components formatted in lines
     */
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

    /**
     * Updates the player ship in the model.
     * This method sets the player's ship and notifies all listeners about the update.
     *
     * @param ship the ViewShip to set as the player's ship
     */
    public void updatePlayerShip(ViewShip ship) {
        this.playerShip = ship;
        LOGGER.fine("Player ship updated in model.");
        for (GameModelListener listener : listeners) {
            listener.onShipUpdated(this.playerShip);
        }
    }

    /**
     * Updates the current lobby in the model.
     * This method sets the current lobby and notifies all listeners about the update.
     *
     * @param lobby the ViewLobby to set as the current lobby
     */
    public void updateLobby(ViewLobby lobby) {
        this.currentLobby = lobby;
        LOGGER.fine("Lobby updated in model: " + (lobby != null ? lobby.getID() : "null"));
        for (GameModelListener listener : listeners) {
            listener.onLobbyUpdated(this.currentLobby);
        }
    }

    /**
     * Sets an error message in the model.
     * This method updates the error message and notifies all listeners about the error.
     *
     * @param message the error message to set
     */
    public void setErrorMessage(String message) {
        this.errorMessage = message;
        LOGGER.warning("Error message set in model: " + message);
        for (GameModelListener listener: listeners) {
            listener.onErrorMessageReceived(message);
        }
    }

    /**
     * Abstract method for logging in the player.
     * This method should be implemented by subclasses to handle the login process.
     */
    public abstract void login();

    /**
     * Sets the username of the player.
     * This method updates the username and notifies all listeners about the change.
     *
     * @param username the username to set for the player
     */
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
