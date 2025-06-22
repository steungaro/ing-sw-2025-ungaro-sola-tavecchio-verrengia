package it.polimi.ingsw.gc20.server.controller;

import it.polimi.ingsw.gc20.common.interfaces.GameControllerInterface;
import it.polimi.ingsw.gc20.common.message_protocol.toclient.*;
import it.polimi.ingsw.gc20.server.controller.managers.MessageManager;
import it.polimi.ingsw.gc20.server.controller.states.*;
import it.polimi.ingsw.gc20.server.exceptions.EmptyDeckException;
import it.polimi.ingsw.gc20.server.exceptions.InvalidStateException;
import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.server.model.components.AlienColor;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.server.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.server.model.player.Player;
import it.polimi.ingsw.gc20.server.model.ship.Ship;
import org.javatuples.Pair;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


/**
 * Controller class for managing the game flow and player interactions
 */
public class GameController implements GameControllerInterface {
    private final GameModel model;
    private State state;
    private final String gameID;
    private final List<String> connectedPlayers = new ArrayList<>();
    private final List<String> disconnectedPlayers = new ArrayList<>();
    private final List<String> pendingPlayers = new ArrayList<>();
    private final Logger logger = Logger.getLogger(GameController.class.getName());
    private final MessageManager messageManager;

    /**
     * Constructor for the GameController class.
     * Initializes the game with the provided name, id, usernames, and level.
     * Validates the number of players and starts the game if valid.
     *
     * @param name       the name of the game
     * @param id         the unique identifier for the game
     * @param usernames  a list of usernames of players in the game
     * @param level      the difficulty level of the game
     * @throws InvalidStateException if the number of players is not between 2 and 4
     */
    public GameController(String name, String id, List<String> usernames, int level) throws InvalidStateException{
        this.messageManager = new MessageManager(this);
        if(usernames.size() > 4 || usernames.size() < 2) {
            this.gameID = null;
            this.model = null;
            getMessageManager().sendToPlayer(usernames.getFirst(), new ErrorMessage("The number of players must be between 2 and 4"));
            throw new InvalidStateException("The number of players must be between 2 and 4");
        } else {
            this.gameID = id;
            this.model = new GameModel();

            try {
                model.startGame(level, usernames, gameID);
                connectedPlayers.addAll(usernames);
                if (name.equals("demo")){
                    model.createDemoShips();
                }
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error starting game", e);
                //notify all players of the er
                for (String username : usernames) {
                    messageManager.sendToPlayer(username, new ErrorMessage("Error starting game: " + e.getMessage()));
                }
            }
        }
    }

    /**
     * Starts the game by initializing the game state to AssemblingState.
     */
    public void startGame() {
        state = new AssemblingState(model, this);
        setState(state);
    }
    /**
     * Changes the new state of the game
     * @param state is the new state of the game
     */
    public void setState(State state) {
        this.state = state;
    }

    /**
     * Getter method for the message manager
     * @return the message manager instance
     */
    public MessageManager getMessageManager() {
        return messageManager;
    }

    /** Getter method for the gameID
     * @return game id
     */
    public String getGameID() {
        return gameID;
    }

    /**
     * @return the first player in the list of players that is online
     */
    public String getFirstOnlinePlayer() {
            return model.getInGamePlayers().stream()
                    .map(Player::getUsername)
                    .filter(connectedPlayers::contains)
                    .findFirst()
                    .orElse(null);
    }

    /**
     * Handles the event of a player drawing a card.
     * This method attempts to draw a card from the game model.
     * If the deck is empty, it transitions the game to an EndgameState.
     */
    public void drawCard() {
        try {
            AdventureCard card = model.drawCard();
            //notify players of the card drawn
            getMessageManager().broadcastUpdate(new CardDrawnMessage(card));
            card.setState(this, model);
        } catch (EmptyDeckException e) {
            state = new EndgameState(state.getController());
        }
    }

    /**
     * Returns the current game state
     * @return the current game state
     */
    public State getState() {
        return state;
    }

    @Override
    public void landOnPlanet(String username, int planetIndex) {
        try{
            state.landOnPlanet(getPlayerByID(username), planetIndex);
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, "Error landing on planet", e);
            //notify the player of the error
            getMessageManager().sendToPlayer(username, new ErrorMessage("Error landing on planet: " + e.getMessage()));
        }

    }

    @Override
    public void loadCargo(String username, CargoColor loaded, Pair<Integer, Integer> ch) {
        try {
            Player player = getPlayerByID(username);
            state.loadCargo(player, loaded, ch);
        } catch (Exception e){
            logger.log(Level.SEVERE, "Error loading cargo", e);
            //notify the player of the error
            getMessageManager().sendToPlayer(username, new ErrorMessage("Error loading cargo: " + e.getMessage()));
        }
    }

    @Override
    public void unloadCargo(String username, CargoColor lost, Pair<Integer, Integer> ch) {
        try{
            state.unloadCargo(getPlayerByID(username), lost, ch);
        } catch (Exception e){
            //notify the player of the error
            getMessageManager().sendToPlayer(username, new ErrorMessage("Error unloading cargo: " + e.getMessage()));
            logger.log(Level.SEVERE, "Error unloading cargo", e);
        }
    }

    @Override
    public void moveCargo(String username, CargoColor cargo, Pair<Integer, Integer> from, Pair<Integer, Integer> to) {
        try{
            state.moveCargo(getPlayerByID(username), cargo, from, to);
        } catch (Exception e){
            //notify the player of the error
            getMessageManager().sendToPlayer(username, new ErrorMessage("Error moving cargo: " + e.getMessage()));
            logger.log(Level.SEVERE, "Error moving cargo", e);
        }
    }

    /**
     * Accepts a card and performs the action associated with it
     * @param username is the username of the player that wants to accept the card
     */
    @Override
    public void acceptCard(String username) {
        try{
            state.acceptCard(getPlayerByID(username));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error accepting card", e);
            //notify the player of the error
            getMessageManager().sendToPlayer(username, new ErrorMessage("Error accepting card: " + e.getMessage()));
        }
    }

    /**
     * Returns the number of online players in the game
     * @return the number of online players
     */
    public int getOnlinePlayers() {
            return connectedPlayers.size();
    }

    @Override
    public void activateCannons(String username, List<Pair<Integer, Integer>> cannons, List<Pair<Integer, Integer>> batteries) {
        try{
            state.activateCannons(getPlayerByID(username), cannons, batteries);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error activating cannons", e);
            //notify the player of the error
            getMessageManager().sendToPlayer(username, new ErrorMessage("Error activating cannons: " + e.getMessage()));
        }
    }

    /**
     * this method gets the score of the game
     */
    public void getScore() {
        try{
            state.getScore();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error getting score", e);
            //notify players of the error
            getMessageManager().broadcastUpdate(new ErrorMessage("Error getting score: " + e.getMessage()));
        }
    }

    @Override
    public void loseCrew(String username, List<Pair<Integer, Integer>> cabins) {
        try{
            state.loseCrew(getPlayerByID(username), cabins);
        } catch (Exception e) {
            //notify the player of the error
            getMessageManager().sendToPlayer(username, new ErrorMessage("Error losing crew: " + e.getMessage()));
            logger.log(Level.SEVERE, "Error losing crew", e);
        }
    }

    @Override
    public void activateShield(String username, Pair<Integer, Integer> shieldComp, Pair<Integer, Integer> batteryComp) {
        try{
            state.activateShield(getPlayerByID(username), shieldComp, batteryComp);
        } catch (Exception e) {
            //notify the player of the error
            getMessageManager().sendToPlayer(username, new ErrorMessage("Error activating shield: " + e.getMessage()));
            logger.log(Level.SEVERE, "Error activating shield", e);
        }
    }

    /**
     * Returns the active adventure card
     * @return the active adventure card
     */
    public AdventureCard getActiveCard() {
            return model.getActiveCard();
    }

    @Override
    public void endMove(String username) {
        try{
            state.endMove(getPlayerByID(username));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error ending move", e);
            //notify the player of the error
            getMessageManager().sendToPlayer(username, new ErrorMessage("Error ending move: " + e.getMessage()));
        }
    }

    @Override
    public void activateEngines(String username, List<Pair<Integer, Integer>> engines, List<Pair<Integer, Integer>> batteries) {
        try{
            state.activateEngines(getPlayerByID(username), engines, batteries);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error activating engines", e);
            //notify the player of the error
            getMessageManager().sendToPlayer(username, new ErrorMessage("Error activating engines: " + e.getMessage()));
        }
    }

    @Override
    public void chooseBranch(String username, Pair<Integer, Integer> coordinates) {
        try{
            state.chooseBranch(getPlayerByID(username), coordinates);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error choosing branch", e);
            //notify the player of the error
            getMessageManager().sendToPlayer(username, new ErrorMessage("Error choosing branch: " + e.getMessage()));
        }
    }

    @Override
    public void giveUp(String username) {
        if(!Objects.equals(state.toString(), "PreDrawState")){
            getMessageManager().sendToPlayer(username, new ErrorMessage("Can only give up when the turn has ended"));
        }
        getPlayerByID(username).setGameStatus(false);
        //notify players of the player that gave up
        getMessageManager().broadcastUpdate(new PlayerUpdateMessage(username,
                0,
                false,
                getPlayerByID(username).getColor(),
                ((getPlayerByID(username).getPosition() % getModel().getGame().getBoard().getSpaces() + getModel().getGame().getBoard().getSpaces()) % getModel().getGame().getBoard().getSpaces())));
    }

    /**
     * Handles a player leaving the game mid-session
     *
     * @param username Username of the exiting player
     */
    public void disconnectPlayer(String username) {
        try{
            // Find player with matching username
            if (connectedPlayers.contains(username)) {
                if (state.getCurrentPlayer() != null) {
                    if(state.getCurrentPlayer().equals(username)) {
                        state.currentQuit(getPlayerByID(username));
                    }
                }
                connectedPlayers.remove(username);
                disconnectedPlayers.add(username);
            } else {
                throw new IllegalArgumentException("Player not found in game, not connected or never joined");
            }
            if(connectedPlayers.size() == 1){
                state = new PausedState(state, model, this);
            }else if(connectedPlayers.isEmpty() && pendingPlayers.isEmpty()){
                killGame();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error disconnecting player", e);
        }
    }

    /**
     * Handles reconnection of a previously disconnected player
     *
     * @param username Username of the player attempting to reconnect
     *
     */
    public void reconnectPlayer(String username) {
        try{
            // Check if the player was originally in this game
            if (getPlayerByID(username) == null) {
                throw new IllegalArgumentException("Player was never part of this game");
            }

            // Check if the player is in the disconnected list
            if (!isPlayerDisconnected(username)) {
                // Player isn't disconnected, nothing to do
                return;
            }

            // Remove player from the disconnected list
            disconnectedPlayers.remove(username);
            if (state.isConcurrent()){
                //init all the local model of the view
                for (Player p:  getPlayers()){
                    getMessageManager().sendToPlayer(username, Ship.messageFromShip(p.getUsername(), p.getShip(), "init all ship"));
                }
                getMessageManager().sendToPlayer(username, Ship.messageFromShip(username, getPlayerByID(username).getShip(), "init all ship"));
                if (connectedPlayers.size() == 1) {
                    connectedPlayers.add(username);
                    state.resume(username);
                } else {
                    connectedPlayers.add(username);
                    state.rejoin(username);
                }
            } else {
                pendingPlayers.add(username);
                for (Player p:  getPlayers()){
                    getMessageManager().sendToPlayer(username, Ship.messageFromShip(p.getUsername(), p.getShip(), "init all ship"));
                }
                getMessageManager().sendToPlayer(username, BoardUpdateMessage.fromBoard(getModel().getGame().getBoard(), getModel().getGame().getPlayers(), false));
                getMessageManager().sendToPlayer(username, new CardDrawnMessage(getActiveCard()));
                getMessageManager().sendToPlayer(username, new StandbyMessage("You have reconnected, please wait for the current card to be played."));
            }

            if(connectedPlayers.size() == 1){
                connectedPlayers.add(username);
                state.resume(username);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error reconnecting player", e);
        }
    }

    /**
     * This method is called when a pending player is reconnected to the game in the pre-draw state.
     */
    public void preDrawConnect(){
        for (String username : pendingPlayers){
            connectedPlayers.addLast(username);
            for (Player p:  getPlayers()){
                getMessageManager().sendToPlayer(username, Ship.messageFromShip(p.getUsername(), p.getShip(), "init all ship"));
            }
            getMessageManager().sendToPlayer(username, BoardUpdateMessage.fromBoard(getModel().getGame().getBoard(), getModel().getGame().getPlayers(), false));
        }
    }

    /**
     * Retrieves player data by username
     *
     * @param username Username of the player
     * @return Player object containing player data
     */
    public Player getPlayerByID(String username){
        try{
            return model.getGame().getPlayers()
                    .stream()
                    .filter(player -> player.getUsername().equals(username))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Player not found"));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error getting player by ID", e);
        }
        return null;
    }

    /**
     * Checks if a player is currently disconnected
     *
     * @param username Username to check
     * @return true if the player is disconnected, false otherwise
     */
    public boolean isPlayerDisconnected(String username) {
        return disconnectedPlayers.contains(username);
    }

    @Override
    public void takeComponentFromUnviewed(String username, int index) {
        try{
            state.takeComponentFromUnviewed(getPlayerByID(username), index);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error taking component from unviewed", e);
            //notify the player of the error
            getMessageManager().sendToPlayer(username, new ErrorMessage("Error taking component from unviewed: " + e.getMessage()));
        }
    }

    @Override
    public void takeComponentFromViewed(String username, int index) {
        try{
            state.takeComponentFromViewed(getPlayerByID(username), index);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error taking component from view", e);
            //notify the player of the error
            getMessageManager().sendToPlayer(username, new ErrorMessage("Error taking component from view: " + e.getMessage()));
        }
    }

    @Override
    public void takeComponentFromBooked(String username, int index) {
        try{
            if (model.getLevel() != 2) {
                throw new IllegalStateException("Booking components is only available in level 2 games");
            }
            state.takeComponentFromBooked(getPlayerByID(username), index);
        } catch (Exception e) {
            //notify the player of the error
            getMessageManager().sendToPlayer(username, new ErrorMessage("Error taking component from booked: " + e.getMessage()));
            logger.log(Level.SEVERE, "Error taking component from booked", e);
        }
    }

    @Override
    public void addComponentToBooked(String username) {
        try{
            if (model.getLevel() != 2) {
                throw new IllegalStateException("Booking components is only available in level 2 games");
            }
            state.addComponentToBooked(getPlayerByID(username));
        } catch (Exception e) {
            //notify the player of the error
            getMessageManager().sendToPlayer(username, new ErrorMessage("Error adding component to booked: " + e.getMessage()));
            logger.log(Level.SEVERE, "Error adding component to booked", e);
        }
    }

    @Override
    public synchronized void addComponentToViewed(String username) {
        try{
            state.addComponentToViewed(getPlayerByID(username));
        } catch (Exception e) {
            //notify the player of the error
            getMessageManager().sendToPlayer(username, new ErrorMessage("Error adding component to viewed: " + e.getMessage()));
            logger.log(Level.SEVERE, "Error adding component to view", e);
        }
    }

    @Override
    public void placeComponent(String username, Pair<Integer, Integer> coordinates) {
        try{
            state.placeComponent(getPlayerByID(username), coordinates);
        } catch (Exception e) {
            //notify the player of the error
            getMessageManager().sendToPlayer(username, new ErrorMessage("Error placing component: " + e.getMessage()));
            logger.log(Level.SEVERE, "Error placing component", e);
        }
    }

    @Override
    public void rotateComponentClockwise(String username) {
        try{
            state.rotateComponentClockwise(getPlayerByID(username));
        } catch (Exception e) {
            //notify the player of the error
            getMessageManager().sendToPlayer(username, new ErrorMessage("Error rotating component clockwise: " + e.getMessage()));
            logger.log(Level.SEVERE, "Error rotating component clockwise", e);
        }
    }

    @Override
    public void rotateComponentCounterclockwise(String username) {
        try{
            state.rotateComponentCounterclockwise(getPlayerByID(username));
        } catch (Exception e) {
            //notify the player of the error
            getMessageManager().sendToPlayer(username, new ErrorMessage("Error rotating component counterclockwise: " + e.getMessage()));
            logger.log(Level.SEVERE, "Error rotating component clockwise", e);
        }
    }

    @Override
    public void removeComponentFromShip(String username, Pair<Integer, Integer> coordinates) {
        try{
            state.removeComp(getPlayerByID(username), coordinates);
        } catch (Exception e) {
            //notify the player of the error
            getMessageManager().sendToPlayer(username, new ErrorMessage("Error removing component from ship: " + e.getMessage()));
            logger.log(Level.SEVERE, "Error removing component from ship", e);
        }
    }

    @Override
    public void stopAssembling(String username, int position) {
        try{
            state.stopAssembling(getPlayerByID(username), position);
            if (state.allAssembled()) {
                state = new ValidatingShipState(model, this);
            }
        } catch (Exception e) {
            //notify the player of the error
            getMessageManager().sendToPlayer(username, new ErrorMessage("Error stopping assembling: " + e.getMessage()));
            logger.log(Level.SEVERE, "Error stopping assembling", e);
        }
    }

    @Override
    public void turnHourglass(String username) {
        try{
            if (isPlayerDisconnected(username)) {
                throw new IllegalArgumentException("Cannot turn hourglass for not connected player");
            }
            if (model.getLevel() != 2) {
                throw new InvalidStateException("Hourglass is only available in level 2 games");
            }
            state.turnHourglass(getPlayerByID(username));
        } catch (Exception e) {
            //notify the player of the error
            getMessageManager().sendToPlayer(username, new ErrorMessage("Error turning hourglass: " + e.getMessage()));
            logger.log(Level.SEVERE, "Error turning hourglass", e);
        }
    }

    @Override
    public void peekDeck(String username, int num) {
        try{
            if (model.getLevel() != 2) {
                throw new IllegalStateException("Decks are only available in level 2 games");
            }
            if (isPlayerDisconnected(username)) {
                throw new IllegalArgumentException("Cannot view deck for not connected player");
            }
            //notify player of the deck peeked
            state.peekDeck(getPlayerByID(username), num);
        } catch (Exception e) {
            //notify the player of the error
            getMessageManager().sendToPlayer(username, new ErrorMessage("Error peeking deck: " + e.getMessage()));
            logger.log(Level.SEVERE, "Error peeking deck", e);
        }
    }

    @Override
    public void addAlien(String username, AlienColor color, Pair<Integer, Integer> cabin) {
        try{
            if(model.getLevel() != 2){
                throw new IllegalArgumentException("Aliens are only available in level 2 games");
            }
            state.addAlien(getPlayerByID(username), color, cabin);
        } catch (Exception e) {
            //notify the player of the error
            getMessageManager().sendToPlayer(username, new ErrorMessage("Error adding alien: " + e.getMessage()));
            logger.log(Level.SEVERE, "Error adding alien", e);
        }
    }

    @Override
    public void rollDice(String username) {
        try{
            state.rollDice(getPlayerByID(username));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error rolling dice", e);
            //notify the player of the error
            getMessageManager().sendToPlayer(username, new ErrorMessage("Error rolling dice: " + e.getMessage()));
        }
    }

    /**
     * getter method for the model attribute
     */
    public GameModel getModel() {
            return model;
    }


    /**
     * Returns the list of players that are currently playing in the game
     *
     * @return usernames of connected players
     */
    public List<String> getInGameConnectedPlayers() {
        return model.getInGamePlayers().stream()
                .map(Player::getUsername)
                .filter(connectedPlayers::contains)
                .collect(Collectors.toList());
    }

    /**
     * this method is called to kill a game if there are no players left
     */
    public void killGame() {
        MatchController.getInstance().endGame(this.getGameID());
    }

    @Override
    public void loseEnergy(String username, Pair<Integer, Integer> coordinates) {
        try {
            state.loseEnergy(getPlayerByID(username), coordinates);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error losing energy", e);
            //notify the player of the error
            getMessageManager().sendToPlayer(username, new ErrorMessage("Error losing energy: " + e.getMessage()));
        }
    }

    /**
     * Retrieves the list of players in the game that are currently connected or pending connection
     *
     * @return List of Player objects representing all players in the game
     */
    public List<Player> getPlayers() {
        return model.getGame().getPlayers().stream()
                .filter(player -> connectedPlayers.contains(player.getUsername()) ||
                        pendingPlayers.contains(player.getUsername()))
                .collect(Collectors.toList());
    }
}