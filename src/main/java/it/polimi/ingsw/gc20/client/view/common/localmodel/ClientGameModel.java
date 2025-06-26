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
import it.polimi.ingsw.gc20.server.model.player.PlayerColor;

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
    private ViewLobby currentLobby;
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
    private final List<LobbyListListener> lobbyListListeners = new ArrayList<>();

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
        notifyLobbyListListeners();
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
     * Sets the player information in the game model.
     * This method updates the player's credits, in-game status, color, and position on the board.
     *
     * @param username the username of the player whose information is to be updated
     * @param creditsAdded the number of credits to add to the player's current credits
     * @param inGame whether the player is currently in-game
     * @param color the color of the player
     * @param posInBoard the position of the player on the board
     */
    public void setPlayerInfo(String username, int creditsAdded, boolean inGame, PlayerColor color, int posInBoard) {
        for (ViewPlayer player : board.players) {
            if (player.username.equals(username)) {
                player.credits += creditsAdded;
                player.inGame = inGame;
                player.playerColor = color;
                player.position = posInBoard;
                break;
            }
        }
        for (GameModelListener listener : listeners) {
            listener.onBoardUpdated(this.board);
        }
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
     * @param listener the LobbyListListener to add
     */
    public void addLobbyListListener(LobbyListListener listener) {
        if (!lobbyListListeners.contains(listener)) {
            lobbyListListeners.add(listener);
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
    protected void notifyLobbyListListeners() {
        for (LobbyListListener observer : lobbyListListeners) {
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
     * It prints the current adventure card being viewed.
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
    /**
     * Returns the current lobby.
     * This method provides access to the lobby in which the player is currently participating.
     *
     * @return the ViewLobby representing the current lobby
     */
    public ViewLobby getCurrentLobby() { return currentLobby; }

    /**
     * Returns the game players.
     *
     * @return an array of ViewPlayer objects representing the players in the game
     */
    public ViewPlayer[] getPlayers() {
        return board.players;
    }

    /**
     * Returns the error message.
     * This method provides access to the last error message set in the model.
     *
     * @return the error message as a String
     */
    public String getErrorMessage() { return errorMessage; }

    /**
     * Returns the client associated with this game model.
     * This method provides access to the client that is used for communication with the server.
     *
     * @return the Client instance associated with this game model
     */
    public Client getClient() {
        return client;
    }

    /**
     * Shuts down the game model.
     */
    public abstract void shutdown();

    /**
     * This method is called whenever a branching menu is needed.
     */
    public abstract void branchMenu();

    /**
     * Displays the building menu with the given list of adventure cards (viewed deck)
     *
     * @param cards the list of ViewAdventureCard objects to display in the building menu
     */
    public abstract void buildingMenu(List<ViewAdventureCard> cards);

    /**
     * Displays the cannon menu with the given message.
     * This method is used to show options related to cannons in the game.
     *
     * @param message the message to display in the cannon menu
     */
    public abstract void cannonsMenu(String message);

    /**
     * Displays the card acceptance menu with the given message.
     * This method is used to prompt the player for card acceptance decisions.
     *
     * @param message the message to display in the card acceptance menu
     */
    public abstract void cardAcceptanceMenu(String message);

    /**
     * Displays the cargo menu with the given message, cargo to lose, and cargo to gain.
     * This method is used to manage cargo-related actions in the game.
     *
     * @param message the message to display in the cargo menu
     * @param cargoToLose the amount of cargo to lose
     * @param cargoToGain the list of CargoColor objects representing the cargo to gain
     * @param losing whether the player is losing cargo
     */
    public abstract void cargoMenu(String message, int cargoToLose, List<CargoColor> cargoToGain, boolean losing);

    /**
     * Displays the engine menu with the given message.
     * This method is used to show options related to the ship's engine activation in the game.
     *
     * @param message the message to display in the engine menu
     */
    public abstract void engineMenu(String message);

    /**
     * Displays the menu of a player in a lobby.
     */
    public abstract void inLobbyMenu();

    /**
     * Displays the main menu state.
     * This method is used to show the main menu options to the player.
     */
    public abstract void mainMenuState();

    /**
     * Displays the planet menu with the given list of planets.
     * This method is used to show options related to planets in the game.
     *
     * @param planets the list of Planet objects to display in the planet menu
     */
    public abstract void planetMenu(List<Planet> planets);

    /**
     * Displays the populating ship menu.
     * This method is used to add aliens to the ship.
     */
    public abstract void populateShipMenu();

    /**
     * Displays the automatic action menu with the given message.
     * This method is used to let the player know that an automatic action is being performed.
     *
     * @param message the message to display in the automatic action menu
     */
    public abstract void automaticAction(String message);

    /**
     * Displays the validation menu.
     * This method is used to validate the player's ship.
     */
    public abstract void validationMenu();

    /**
     * Initializes the game model.
     * This method is called to set up the initial state of the game model.
     * It should be implemented by subclasses to perform any necessary initialization.
     */
    public abstract void init();

    /**
     * Displays the shields-menu with the given message.
     * This method is used to show options related to the shields' activation in the game.
     *
     * @param message the message to display in the shields-menu
     */
    public abstract void shieldsMenu(String message);

    /**
     * Displays the roll dice menu with the given message.
     * This method is used to prompt the player to roll the dice in the game.
     *
     * @param message the message to display in the roll dice menu
     */
    public abstract void rollDiceMenu(String message);

    /**
     * Displays the cargo menu with the given cargo number.
     * This method is used to manage cargo-related actions in the game.
     *
     * @param cargoNum the number of cargo items to display in the menu
     */
    public abstract void cargoMenu(int cargoNum);

    /**
     * Displays the lose-crew menu with the given crew number.
     * This method is used to prompt the player about losing crew members in the game.
     *
     * @param crewNum the number of crew members to display in the menu
     */
    public abstract void loseCrewMenu(int crewNum);

    /**
     * Displays the battery menu with the given battery number.
     * This method is used to manage battery-related actions in the game.
     *
     * @param batteryNum the number of batteries to display in the menu
     */
    public abstract void removeBatteryMenu(int batteryNum);

    /**
     * Displays the assembling state menu.
     * This method is used to show the building options for the ship's components.
     */
    public abstract void assemblingStateMenu();

    /**
     * Displays the leader board menu with the given leader board data.
     * This method is used to show the final scores of players in the game.
     *
     * @param leaderBoard a map containing player usernames and their scores
     */
    public abstract void leaderBoardMenu(Map<String, Integer> leaderBoard);

    /**
     * Displays the login successful message with the given username.
     * This method is used to notify the player that they have successfully logged in.
     *
     * @param username the username of the player who logged in
     */
    public abstract void loginSuccessful(String username);

    /**
     * Displays the login failed message with the given username.
     * This method is used to notify the player that their login attempt has failed.
     *
     * @param username the username of the player who attempted to log in
     */
    public abstract void loginFailed(String username);

    /**
     * Displays the idle menu with the given message.
     * This method is used to let the player know that it's not their turn, and they can wait for the next action.
     *
     * @param message the message to display in the idle menu
     */
    public abstract void idleMenu(String message);

    /**
     * Displays the keep playing menu.
     * This method is used to prompt the player to decide whether they want to continue playing or not.
     */
    public abstract void keepPlayingMenu();
}
