package it.polimi.ingsw.gc20.server.controller;

import it.polimi.ingsw.gc20.common.interfaces.GameControllerInterface;
import it.polimi.ingsw.gc20.common.message_protocol.toclient.*;
import it.polimi.ingsw.gc20.server.controller.states.*;
import it.polimi.ingsw.gc20.server.exceptions.EmptyDeckException;
import it.polimi.ingsw.gc20.server.exceptions.InvalidStateException;
import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.server.model.components.AlienColor;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.server.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.server.model.player.Player;
import it.polimi.ingsw.gc20.server.model.ship.Ship;
import it.polimi.ingsw.gc20.server.network.NetworkService;
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

    /**
     * Default constructor
     *
     * @param id        unique identifier for this game
     * @param usernames list of player usernames
     * @param level     game difficulty level
     * @throws IllegalArgumentException if the number of players is not between 2 and 4
     */
    public GameController(String id, List<String> usernames, int level) throws InvalidStateException{
        if(usernames.size() > 4 || usernames.size() < 2) {
            this.gameID = null;
            this.model = null;
            NetworkService.getInstance().sendToClient(usernames.getFirst(), new ErrorMessage("The number of players must be between 2 and 4"));
            throw new InvalidStateException("The number of players must be between 2 and 4");
        } else {

            this.gameID = id;
            this.model = new GameModel();

            try {
                model.startGame(level, usernames, gameID);
                connectedPlayers.addAll(usernames);
                //TODO: notify players of game start
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error starting game", e);
                //notify all players of the error
                for (String username : usernames) {
                    NetworkService.getInstance().sendToClient(username, new ErrorMessage("Error starting game: " + e.getMessage()));
                }
            }
        }
    }

    public void startGame() {
        state = new AssemblingState(model, this);
        setState(state);
    }
    /**
     * Changes the game state and notifies players of the change
     * @param state is the new state of the game
     */
    public void setState(State state) {
        this.state = state;
        //TODO: notify players of state change
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
     * Handles the card drawing phase of the game
     */
    public void drawCard() {
        try {
            AdventureCard card = model.drawCard();
            //notify players of the card drawn
            for (Player player : getPlayers()){
                NetworkService.getInstance().sendToClient(player.getUsername(), new CardDrawnMessage(card));
            }
            card.setState(this, model);
        } catch (EmptyDeckException e) {
            state = new EndgameState(state.getController());
        }
    }

    /**
     * Returns the current game state
     * @return the current game state
     */
    public String getState() {
        return state.toString();
    }

    /**
     * Accepts a planet card and lands on the planet
     * @param username is the username of the player that wants to land on the planet
     * @param planetIndex is the index of the planet card in the player's hand
     */
    @Override
    public void landOnPlanet(String username, int planetIndex) {
        try{
            state.landOnPlanet(getPlayerByID(username), planetIndex);
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, "Error landing on planet", e);
            //notify the player of the error
            NetworkService.getInstance().sendToClient(username, new ErrorMessage("Error landing on planet: " + e.getMessage()));
        }

    }

    /**
     * Loads cargo onto the player's ship
     * @param username is the username of the player that wants to load the cargo
     * @param loaded the cargo that the player wants to load
     * @param ch are the coordinates of the cargo hold where the player wants to load the cargo
     * @apiNote To be used after accepting a planet, accepting a smuggler, accepting an abandoned station
     */
    @Override
    public void loadCargo(String username, CargoColor loaded, Pair<Integer, Integer> ch) {
        try {
            Player player = getPlayerByID(username);
            state.loadCargo(player, loaded, ch);
            //notify players of the cargo loaded
            for (Player p:  getPlayers()){
                NetworkService.getInstance().sendToClient(p.getUsername(), Ship.messageFromShip(username, getPlayerByID(username).getShip(), "loaded cargo"));
            }
        } catch (Exception e){
            logger.log(Level.SEVERE, "Error loading cargo", e);
            //notify the player of the error
            NetworkService.getInstance().sendToClient(username, new ErrorMessage("Error loading cargo: " + e.getMessage()));
        }
    }

    /**
     * Unloads cargo from the player's ship
     * @param username is the username of the player that wants to unload the cargo
     * @param lost the cargo that the player wants to unload
     * @param ch are the coordinates of the cargo hold where the player wants to unload the cargo
     * @apiNote To be used after accepting a planet, accepting a smuggler,
     * accepting an abandoned station (to unload without limits) or after smugglers, combat zone
     * (to remove the most valuable one)
     */
    @Override
    public void unloadCargo(String username, CargoColor lost, Pair<Integer, Integer> ch) {
        try{
            state.unloadCargo(getPlayerByID(username), lost, ch);
            //notify players of the cargo unloaded
            for (Player p:  getPlayers()){
                NetworkService.getInstance().sendToClient(p.getUsername(), Ship.messageFromShip(username, getPlayerByID(username).getShip(), "unloaded cargo"));
            }
        } catch (Exception e){
            //notify the player of the error
            NetworkService.getInstance().sendToClient(username, new ErrorMessage("Error unloading cargo: " + e.getMessage()));
            logger.log(Level.SEVERE, "Error unloading cargo", e);
        }
    }

    /**
     * Moves cargo from one cargo hold to another
     * @param username is the username of the player that wants to move the cargo
     * @param cargo is the cargo that the player wants to move?
     * @param from are the coordinates of the cargo hold where the player wants to move the cargo from
     * @param to are the coordinates of the cargo hold where the player wants to move the cargo to
     * @apiNote To be used in losing/gaining cargo
     */
    @Override
    public void moveCargo(String username, CargoColor cargo, Pair<Integer, Integer> from, Pair<Integer, Integer> to) {
        try{
            state.moveCargo(getPlayerByID(username), cargo, from, to);
            //notify players of the cargo moved
            for (Player p:  getPlayers()){
                NetworkService.getInstance().sendToClient(p.getUsername(), Ship.messageFromShip(username, getPlayerByID(username).getShip(), "moved cargo"));
            }
        } catch (Exception e){
            //notify the player of the error
            NetworkService.getInstance().sendToClient(username, new ErrorMessage("Error moving cargo: " + e.getMessage()));
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
            NetworkService.getInstance().sendToClient(username, new ErrorMessage("Error accepting card: " + e.getMessage()));
        }
    }

    public int getOnlinePlayers() {
            return connectedPlayers.size();
    }

    /**
     * To be called when a player wants to activate their cannons
     * @param username is the username of the player that wants to activate their cannons
     * @param cannons the list of coordinates of the cannons that the player wants to activate
     * @param batteries the list of coordinates of the batteries that the player wants to use to activate the cannons
     */
    @Override
    public void activateCannons(String username, List<Pair<Integer, Integer>> cannons, List<Pair<Integer, Integer>> batteries) {
        try{
            state.activateCannons(getPlayerByID(username), cannons, batteries);
            for (Player p:  getPlayers()){
                NetworkService.getInstance().sendToClient(p.getUsername(), Ship.messageFromShip(username, getPlayerByID(username).getShip(), "activated cannons"));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error activating cannons", e);
            //notify the player of the error
            NetworkService.getInstance().sendToClient(username, new ErrorMessage("Error activating cannons: " + e.getMessage()));
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
            for (Player player : getPlayers()) {
                NetworkService.getInstance().sendToClient(player.getUsername(), new ErrorMessage("Error getting score: " + e.getMessage()));
            }
        }
    }

    /**
     * To be called when a player wants to lose crew
     * @param username is the username of the player that wants to lose crew
     * @param cabins the list of coordinates of the cabins that the player wants to lose crew from
     */
    @Override
    public void loseCrew(String username, List<Pair<Integer, Integer>> cabins) {
        try{
            state.loseCrew(getPlayerByID(username), cabins);
            //notify players of the crew lost
            for (Player p:  getPlayers()){
                NetworkService.getInstance().sendToClient(p.getUsername(), Ship.messageFromShip(username, getPlayerByID(username).getShip(), "lost crew"));
            }
        } catch (Exception e) {
            //notify the player of the error
            NetworkService.getInstance().sendToClient(username, new ErrorMessage("Error losing crew: " + e.getMessage()));
            logger.log(Level.SEVERE, "Error losing crew", e);
        }
    }

    /**
     * To be called when a light_fire/meteor is firing the player
     * @param username is the username of the player that wants to activate the shield
     * @param shieldComp are the coordinates of the shield component that the player wants to activate
     * @param batteryComp are the coordinates of the energy component that the player wants to use to activate the shield
     * @apiNote Ship may need to be validated
     */
    @Override
    public void activateShield(String username, Pair<Integer, Integer> shieldComp, Pair<Integer, Integer> batteryComp) {
        try{
            state.activateShield(getPlayerByID(username), shieldComp, batteryComp);
            //notify players of the battery used
            for (Player p:  getPlayers()){
                NetworkService.getInstance().sendToClient(p.getUsername(), Ship.messageFromShip(username, getPlayerByID(username).getShip(), "activated shield"));
            }
        } catch (Exception e) {
            //notify the player of the error
            NetworkService.getInstance().sendToClient(username, new ErrorMessage("Error activating shield: " + e.getMessage()));
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

    /**
     * To be called when a player terminates their turn. Based on the type of card, the game will move to the next player or the next phase
     * @param username is the username of the player that wants to terminate their turn
     */
    @Override
    public void endMove(String username) {
        try{
            state.endMove(getPlayerByID(username));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error ending move", e);
            //notify the player of the error
            NetworkService.getInstance().sendToClient(username, new ErrorMessage("Error ending move: " + e.getMessage()));
        }
    }

    /**
     * To be called when a player activates their engines
     * @param username is the username of the player that wants to activate their engines
     * @param engines the list of coordinates of the engines that the player wants to activate
     * @param batteries the list of coordinates of the batteries that the player wants to use to activate the engines
     */
    @Override
    public void activateEngines(String username, List<Pair<Integer, Integer>> engines, List<Pair<Integer, Integer>> batteries) {
        try{
            state.activateEngines(getPlayerByID(username), engines, batteries);
            //notify players of the engines activated
            for (Player p:  getPlayers()){
                NetworkService.getInstance().sendToClient(p.getUsername(), Ship.messageFromShip(username, getPlayerByID(username).getShip(), "activated engines"));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error activating engines", e);
            //notify the player of the error
            NetworkService.getInstance().sendToClient(username, new ErrorMessage("Error activating engines: " + e.getMessage()));
        }
    }

    /**
     * To be called when a player needs to choose one of the two branches, a ship is divided into after a fire.
     * @param username is the username of the player that wants to choose a branch
     * @param coordinates are the coordinates of the branch that the player wants to choose
     */
    @Override
    public void chooseBranch(String username, Pair<Integer, Integer> coordinates) {
        try{
            state.chooseBranch(getPlayerByID(username), coordinates);
            //notify players of the branch chosen
            for (Player p:  getPlayers()){
                NetworkService.getInstance().sendToClient(p.getUsername(), Ship.messageFromShip(username, getPlayerByID(username).getShip(), "chose branch"));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error choosing branch", e);
            //notify the player of the error
            NetworkService.getInstance().sendToClient(username, new ErrorMessage("Error choosing branch: " + e.getMessage()));
        }
    }

    /**
     * Handles the event of a player giving up
     * @param username is the username of the player that wants to give up
     */
    @Override
    public void giveUp(String username) {
        if(!Objects.equals(state.toString(), "PreDrawState")){
            NetworkService.getInstance().sendToClient(username, new ErrorMessage("Can only give up when the turn has ended"));
        }
        getPlayerByID(username).setGameStatus(false);
        //notify players of the player that gave up
        for (Player player : getPlayers()) {
            NetworkService.getInstance().sendToClient(player.getUsername(), new PlayerUpdateMessage(username,
                    0,
                    false,
                    getPlayerByID(username).getColor(),
                    (getPlayerByID(username).getPosition()%model.getGame().getBoard().getSpaces())));
        }
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
            }else if(connectedPlayers.isEmpty()){
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
                    NetworkService.getInstance().sendToClient(username, Ship.messageFromShip(p.getUsername(), p.getShip(), "reconnection"));
                }
                NetworkService.getInstance().sendToClient(username, Ship.messageFromShip(username, getPlayerByID(username).getShip(), "reconnection"));
                if (connectedPlayers.size() == 1) {
                    connectedPlayers.add(username);
                    state.resume(username);
                } else {
                    connectedPlayers.add(username);
                    state.resume(username);
                }
            } else {
                pendingPlayers.add(username);
                for (Player p:  getPlayers()){
                    NetworkService.getInstance().sendToClient(username, Ship.messageFromShip(p.getUsername(), p.getShip(), "reconnection"));
                }
                NetworkService.getInstance().sendToClient(username, BoardUpdateMessage.fromBoard(getModel().getGame().getBoard(), getModel().getGame().getPlayers(), false));
                NetworkService.getInstance().sendToClient(username, new CardDrawnMessage(getActiveCard()));
                NetworkService.getInstance().sendToClient(username, new StandbyMessage("You have reconnected, please wait for the current card to be played."));
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
            //needs to update the local model of the player with all the ship of the other players
            for (Player player: getPlayers()) {
                NetworkService.getInstance().sendToClient(username, Ship.messageFromShip(player.getUsername(), player.getShip(), "init all ship"));
            }
            NetworkService.getInstance().sendToClient(username, BoardUpdateMessage.fromBoard(getModel().getGame().getBoard(), getModel().getGame().getPlayers(), false));
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

    /**
     * Takes a component from the unviewed pile and adds it to the player's hand
     *
     * @param username Username of the player taking the component
     * @param index Index of the component in the unviewed pile
     */
    @Override
    public synchronized void takeComponentFromUnviewed(String username, int index) {
        try{
            state.takeComponentFromUnviewed(getPlayerByID(username), index);
            // notify players of the pile update
            for (Player player: getPlayers()) {
                NetworkService.getInstance().sendToClient(player.getUsername(), PileUpdateMessage.fromComponent(username,
                        getModel().getGame().getPile().getUnviewed().size(),
                        getModel().getGame().getPile().getViewed(),
                        "taken from unviewed"));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error taking component from unviewed", e);
            //notify the player of the error
            NetworkService.getInstance().sendToClient(username, new ErrorMessage("Error taking component from unviewed: " + e.getMessage()));
        }
    }

    /**
     * Takes a component from the viewed pile and adds it to the player's hand
     *
     * @param username Username of the player taking the component
     * @param index Index of the component in the viewed pile
     * @throws IllegalStateException if the game is not in ASSEMBLING state
     * @throws NoSuchElementException if the component is not in the viewed pile
     * @throws IllegalArgumentException if the player already has a component in the hand
     */
    @Override
    public synchronized void takeComponentFromViewed(String username, int index) {
        try{
            state.takeComponentFromViewed(getPlayerByID(username), index);
            //notify players of the component taken
            for (Player player: getPlayers()) {
                NetworkService.getInstance().sendToClient(player.getUsername(), PileUpdateMessage.fromComponent(username,
                        getModel().getGame().getPile().getUnviewed().size(),
                        getModel().getGame().getPile().getViewed(),
                        "taken from viewed"));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error taking component from view", e);
            //notify the player of the error
            NetworkService.getInstance().sendToClient(username, new ErrorMessage("Error taking component from viewed: " + e.getMessage()));
        }
    }

    /**
     * Takes a component that was previously booked by a player (Level 2 only) and adds it to the player's hand
     *
     * @param username Username of the player taking the component
     * @param index Index of the component in the booked list
     */
    @Override
    public void takeComponentFromBooked(String username, int index) {
        try{
            if (model.getLevel() != 2) {
                throw new IllegalStateException("Booking components is only available in level 2 games");
            }
            state.takeComponentFromBooked(getPlayerByID(username), index);
            // notify players of a component taken and update the ship
            for (Player player: getPlayers()) {
                NetworkService.getInstance().sendToClient(player.getUsername(), Ship.messageFromShip(username, getPlayerByID(username).getShip(), "took component from booked"));
            }
        } catch (Exception e) {
            //notify the player of the error
            NetworkService.getInstance().sendToClient(username, new ErrorMessage("Error taking component from booked: " + e.getMessage()));
            logger.log(Level.SEVERE, "Error taking component from booked", e);
        }
    }

    /**
     * Adds the component in the hand to the player's booked list (Level 2 only)
     *
     * @param username Username of the player booking the component
     */
    @Override
    public void addComponentToBooked(String username) {
        try{
            if (model.getLevel() != 2) {
                throw new IllegalStateException("Booking components is only available in level 2 games");
            }
            state.addComponentToBooked(getPlayerByID(username));
            // notify players of a component booked and update the ship
            for (Player player: getPlayers()) {
                NetworkService.getInstance().sendToClient(player.getUsername(), Ship.messageFromShip(username, getPlayerByID(username).getShip(), "added component to booked"));
            }
        } catch (Exception e) {
            //notify the player of the error
            NetworkService.getInstance().sendToClient(username, new ErrorMessage("Error adding component to booked: " + e.getMessage()));
            logger.log(Level.SEVERE, "Error adding component to booked", e);
        }
    }

    /**
     * Adds the component in the hand to the viewed pile so other players can see it
     *
     * @param username Username of the player adding the component
     */
    @Override
    public synchronized void addComponentToViewed(String username) {
        try{
            state.addComponentToViewed(getPlayerByID(username));
            //notify the players of the component added and update the pile
            for (Player player: getPlayers()) {
                NetworkService.getInstance().sendToClient(player.getUsername(), PileUpdateMessage.fromComponent(username,
                        getModel().getGame().getPile().getUnviewed().size(),
                        getModel().getGame().getPile().getViewed(),
                        "added to viewed"));
            }
        } catch (Exception e) {
            //notify the player of the error
            NetworkService.getInstance().sendToClient(username, new ErrorMessage("Error adding component to viewed: " + e.getMessage()));
            logger.log(Level.SEVERE, "Error adding component to view", e);
        }
    }

    /**
     * Places the component in the hand on the player's ship at specified coordinates
     *
     * @param username Username of the player placing the component
     * @param coordinates Coordinates where the component will be placed
     */
    @Override
    public void placeComponent(String username, Pair<Integer, Integer> coordinates) {
        try{
            state.placeComponent(getPlayerByID(username), coordinates);

        } catch (Exception e) {
            //notify the player of the error
            NetworkService.getInstance().sendToClient(username, new ErrorMessage("Error placing component: " + e.getMessage()));
            logger.log(Level.SEVERE, "Error placing component", e);
        }
    }

    /**
     * Rotates the component in the hand clockwise
     *
     * @param username Username of the player rotating the component
     */
    @Override
    public void rotateComponentClockwise(String username) {
        try{
            state.rotateComponentClockwise(getPlayerByID(username));
        } catch (Exception e) {
            //notify the player of the error
            NetworkService.getInstance().sendToClient(username, new ErrorMessage("Error rotating component clockwise: " + e.getMessage()));
            logger.log(Level.SEVERE, "Error rotating component clockwise", e);
        }
    }

    /**
     * Rotates the component in the hand counterclockwise
     *
     * @param username Username of the player rotating the component
     */
    @Override
    public void rotateComponentCounterclockwise(String username) {
        try{
            state.rotateComponentCounterclockwise(getPlayerByID(username));
        } catch (Exception e) {
            //notify the player of the error
            NetworkService.getInstance().sendToClient(username, new ErrorMessage("Error rotating component counterclockwise: " + e.getMessage()));
            logger.log(Level.SEVERE, "Error rotating component clockwise", e);
        }
    }

    /**
     * Removes a component from the player's ship (only during the validating phase and if the ship is not valid)
     *
     * @param username Username of the player removing the component
     * @param coordinates Coordinates of the component to be removed
     * @apiNote view must call this function until the ship is valid
     */
    @Override
    public void removeComponentFromShip(String username, Pair<Integer, Integer> coordinates) {
        try{
            state.removeComp(getPlayerByID(username), coordinates);
            //notify the players of the ship changes
            for (Player player : getPlayers()) {
                NetworkService.getInstance().sendToClient(player.getUsername(), Ship.messageFromShip(username, getPlayerByID(username).getShip(), "removed component"));
            }
        } catch (Exception e) {
            //notify the player of the error
            NetworkService.getInstance().sendToClient(username, new ErrorMessage("Error removing component from ship: " + e.getMessage()));
            logger.log(Level.SEVERE, "Error removing component from ship", e);
        }
    }

    /**
     * Stops the assembling phase for a player
     * @param username is the username of the player that wants to stop assembling
     * @param position is the relative position on board where the player wants to put their rocket
     * @implNote performs state change to VALIDATING when all players have completed assembling
     */
    public void stopAssembling(String username, int position) {
        try{
            state.stopAssembling(getPlayerByID(username), position);
            if (state.allAssembled()) {
                state = new ValidatingShipState(model, this);
            }
        } catch (Exception e) {
            //notify the player of the error
            NetworkService.getInstance().sendToClient(username, new ErrorMessage("Error stopping assembling: " + e.getMessage()));
            logger.log(Level.SEVERE, "Error stopping assembling", e);
        }
    }

    /**
     * Turns the hourglass for a player
     *
     * @param username Username of the player turning the hourglass
     */
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
            NetworkService.getInstance().sendToClient(username, new ErrorMessage("Error turning hourglass: " + e.getMessage()));
            logger.log(Level.SEVERE, "Error turning hourglass", e);
        }
    }

    /**
     * Peeks at the selected deck
     *
     * @param username Username of the player peeking at the deck
     * @param num number of the deck to peek at
     */
    public void peekDeck(String username, int num) {
        try{
            if (model.getLevel() != 2) {
                throw new IllegalStateException("Decks are only available in level 2 games");
            }
            if (isPlayerDisconnected(username)) {
                throw new IllegalArgumentException("Cannot view deck for not connected player");
            }
            //notify player of the deck peeked
            NetworkService.getInstance().sendToClient(username, new DeckPeekedMessage(username, state.peekDeck(getPlayerByID(username), num)));
        } catch (Exception e) {
            //notify the player of the error
            NetworkService.getInstance().sendToClient(username, new ErrorMessage("Error peeking deck: " + e.getMessage()));
            logger.log(Level.SEVERE, "Error peeking deck", e);
        }
    }

    /**
     * Adds an alien to the player's ship
     *
     * @param username Username of the player adding the alien
     * @param color Color of the alien
     * @param cabin Cabin where the alien will be placed (coordinates)
     */
    @Override
    public void addAlien(String username, AlienColor color, Pair<Integer, Integer> cabin) {
        try{
            if(model.getLevel() != 2){
                throw new IllegalArgumentException("Aliens are only available in level 2 games");
            }
            state.addAlien(getPlayerByID(username), color, cabin);
            //notify players of the alien added, update ship
            for (Player player : getPlayers()) {
                NetworkService.getInstance().sendToClient(player.getUsername(), Ship.messageFromShip(username, getPlayerByID(username).getShip(), "added alien"));
            }
        } catch (Exception e) {
            //notify the player of the error
            NetworkService.getInstance().sendToClient(username, new ErrorMessage("Error adding alien: " + e.getMessage()));
            logger.log(Level.SEVERE, "Error adding alien", e);
        }
    }

    /**
     * method to roll the dice for a player
     * @param username is the username of the player that wants to roll the dice
     */
    @Override
    public void rollDice(String username) {
        try{
            state.rollDice(getPlayerByID(username));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error rolling dice", e);
            //notify the player of the error
            NetworkService.getInstance().sendToClient(username, new ErrorMessage("Error rolling dice: " + e.getMessage()));
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

    /**
     * This method is called when a player loses energy
     * @param username is the username of the player that wants to lose energy
     * @param coordinates are the coordinates of the component where the player wants to lose energy
     */
    @Override
    public void loseEnergy(String username, Pair<Integer, Integer> coordinates) {
        try {
            state.loseEnergy(getPlayerByID(username), coordinates);
            //notify players of the energy lost
            for (Player p:  getPlayers()){
                NetworkService.getInstance().sendToClient(p.getUsername(), Ship.messageFromShip(username, getPlayerByID(username).getShip(), "lost energy"));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error losing energy", e);
            //notify the player of the error
            NetworkService.getInstance().sendToClient(username, new ErrorMessage("Error losing energy: " + e.getMessage()));
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