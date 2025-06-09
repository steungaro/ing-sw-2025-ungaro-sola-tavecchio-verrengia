package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.common.message_protocol.toclient.*;
import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.controller.managers.Translator;
import it.polimi.ingsw.gc20.server.exceptions.*;
import it.polimi.ingsw.gc20.server.model.components.Component;
import it.polimi.ingsw.gc20.server.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.server.model.player.Player;
import it.polimi.ingsw.gc20.server.model.ship.Ship;
import org.javatuples.Pair;

import java.util.HashMap;
import java.util.Map;

public class AssemblingState extends State {
    private final Map<Player, Boolean> assembled = new HashMap<>();
    private final Map<Player, Component> componentsInHand = new HashMap<>();
    private final Map<Integer, Player> deckPeeked = new HashMap<>();
    boolean fromBooked = false;
    /**
     * Default constructor
     */
    public AssemblingState(GameModel model, GameController controller) {
        super(model, controller);
        //init the state
        getModel().initCountdown();
        phase = StatePhase.ASSEMBLING_PHASE;
        for (Player player : getController().getPlayers()) {
            assembled.put(player, false);
            componentsInHand.put(player, null);

            //init all the ship of the clients
            getController().getMessageManager().broadcastUpdate(Ship.messageFromShip(player.getUsername(), player.getShip(), "init all ship"));
            if (model.getLevel() == 2) {
                getController().getMessageManager().sendToPlayer(player.getUsername(), new HourglassMessage(getModel().getTurnedHourglass(), getModel().getHourglassTimestamp()));
            }
        }
        //init the boards of the clients
        getController().getMessageManager().broadcastUpdate(BoardUpdateMessage.fromBoard(getModel().getGame().getBoard(), getModel().getGame().getPlayers(), true));
        //init the component piles of the clients
        getController().getMessageManager().broadcastUpdate(PileUpdateMessage.fromComponent("init", 152, getModel().getGame().getPile().getViewed(), "init unviewed pile"));
        //set the phase of the client with an AssemblingMessage
        getController().getMessageManager().broadcastPhase(new AssemblingMessage(null));
        for (int i = 1; i < 4; i++) {
            deckPeeked.put(i, null);
        }

    }

    @Override
    public String toString() {
        return "AssemblingState{ " +
                "assembled=" + assembled +
                " }";
    }

    /**
     * This method is used to take a component from the unviewed pile.
     * @param player the player who is taking the component
     * @param index the index of the component in the unviewed pile
     * @throws InvalidStateException if the player is not in the TAKE_COMPONENT phase
     * @throws ComponentNotFoundException if the component is not found in the unviewed pile
     */

    @Override
    public void takeComponentFromUnviewed(Player player, int index) throws InvalidStateException, ComponentNotFoundException {
        // check if the player is in the TAKE_COMPONENT phase
        if (componentsInHand.get(player) != null) {
            throw new InvalidStateException("Player is not in the correct phase");
        }
        checkHourglass();
        // take the component from the unviewed pile
        Component component =Translator.getFromUnviewed(getModel(), index);
        getModel().componentFromUnviewed(component);
        pileUpdate(player.getUsername());
        takeComponent(player, component);
        //if the player has peeked a deck, remove the peek so others can peek
        for (int i = 1; i < 4; i++) {
            if (deckPeeked.get(i) == player) {
                deckPeeked.put(i, null);
            }
        }
        fromBooked = false;
    }

    /**
     * This method is used to take a component from the viewed pile.
     * @param player the player who is taking the component
     * @param index the index of the component in the viewed pile
     * @throws InvalidStateException if the player is not in the TAKE_COMPONENT phase
     * @throws ComponentNotFoundException if the component is not found in the viewed pile
     */
    @Override
    public void takeComponentFromViewed(Player player, int index) throws InvalidStateException, ComponentNotFoundException{
        //check if the player is in the TAKE_COMPONENT phase
        if (componentsInHand.get(player) != null) {
            throw new InvalidStateException("Player is not in the correct phase");
        }
        checkHourglass();
        //take the component from the viewed pile
        Component component =Translator.getFromViewed(getModel(), index);
        getModel().componentFromViewed(component);
        pileUpdate(player.getUsername());
        takeComponent(player, component);
        //if the player has peeked a deck, remove the peek so others can peek
        for (int i = 1; i < 4; i++) {
            if (deckPeeked.get(i) == player) {
                deckPeeked.put(i, null);
            }
        }
        fromBooked = false;

    }
    /**
     * This method is used to take a component from the booked pile.
     * @param player the player who is taking the component
     * @param index the index of the component in the booked pile
     * @throws InvalidStateException if the player is not in the TAKE_COMPONENT phase
     * @throws ComponentNotFoundException if the component is not found in the booked pile
     */
    @Override
    public void takeComponentFromBooked(Player player, int index) throws InvalidStateException, ComponentNotFoundException {
        // check if the player is in the TAKE_COMPONENT phase
        if (componentsInHand.get(player) != null) {
            throw new InvalidStateException("Player is not in the TAKE_COMPONENT phase");
        }
        checkHourglass();
        // take the component from the booked pile
        Component component =Translator.getFromBooked(player, index);
        getModel().componentFromBooked(component, player);
        getController().getMessageManager().broadcastUpdate(Ship.messageFromShip(player.getUsername(), player.getShip(), "took component from booked"));
        takeComponent(player, component);
        //if the player has peeked a deck, remove the peek so others can peek
        for (int i = 1; i < 4; i++) {
            if (deckPeeked.get(i) == player) {
                deckPeeked.put(i, null);
            }
        }
        fromBooked = true;
    }

    /**
     * This method is used to add a component to the booked pile.
     * @param player the player who is adding the component
     * @throws InvalidStateException if the player is not in the PLACE_COMPONENT phase
     * @throws NoSpaceException if there is no space in the booked pile
     */
    @Override
    public void addComponentToBooked(Player player) throws InvalidStateException, NoSpaceException {
        if (componentsInHand.get(player) == null) {
            throw new InvalidStateException("Player is not in the PLACE_COMPONENT phase");
        }
        //put the component in the booked pile
        getModel().componentToBooked(componentsInHand.get(player), player);
        getController().getMessageManager().broadcastUpdate(Ship.messageFromShip(player.getUsername(), player.getShip(), "added component from booked"));
        // Remove component from player's hand
        componentsInHand.put(player, null);
        //notify the player that they go to the TAKE_COMPONENT phase
        getController().getMessageManager().sendToPlayer(player.getUsername(), new AssemblingMessage(null));
    }

    /**
     * This method is used to add a component to the viewed pile.
     * @param player the player who is adding the component
     * @throws InvalidStateException if the player is not in the PLACE_COMPONENT phase
     * @throws DuplicateComponentException if the component is already in the viewed pile
     */
    @Override
    public void addComponentToViewed(Player player) throws InvalidStateException, DuplicateComponentException {
        // check if the player is in the PLACE_COMPONENT phase
        if (componentsInHand.get(player) == null) {
            throw new InvalidStateException("Player is not in the PLACE_COMPONENT phase");
        }
        if (fromBooked){
            throw new InvalidStateException("Cannot add component to viewed pile after taking it from booked pile");
        }
        // add the component to the viewed pile
        getModel().componentToViewed(componentsInHand.get(player));
        pileUpdate(player.getUsername());
        // Remove component from player's hand
        componentsInHand.put(player, null);
        getController().getMessageManager().sendToPlayer(player.getUsername(), new AssemblingMessage(null));
    }
    /**
     * This method is used to place a component on the ship.
     * @param player the player who is placing the component
     * @param coordinates the coordinates of the component on the ship
     */
    @Override
    public void placeComponent(Player player, Pair<Integer, Integer> coordinates) throws InvalidStateException, InvalidTileException {
        //check if the player is in the PLACE_COMPONENT phase
        if (componentsInHand.get(player) == null) {
            throw new InvalidStateException("Player is not in the PLACE_COMPONENT phase");
        }
        //add the component in the hand to the ship
        getModel().addToShip(componentsInHand.get(player), player, coordinates.getValue0(), coordinates.getValue1());
        // Remove component from player's hand
        componentsInHand.put(player, null);
        //notify the players of the ship changes
        getController().getMessageManager().broadcastUpdate(Ship.messageFromShip(player.getUsername(), player.getShip(), "placed component"));
        //notify the player that they go to the TAKE_COMPONENT phase
        getController().getMessageManager().sendToPlayer(player.getUsername(), new AssemblingMessage(null));
    }
    /**
     * This method is used to rotate a component clockwise.
     * @param player the player who is rotating the component
     * @throws InvalidStateException if the player is not in the PLACE_COMPONENT phase
     */
    @Override
    public void rotateComponentClockwise(Player player) throws InvalidStateException{
        //check if the player has a component in the hand
        if (componentsInHand.get(player) == null) {
            throw new InvalidStateException("Player is not in the PLACE_COMPONENT phase");
        }
        //rotate the component clockwise
        getModel().RotateClockwise(componentsInHand.get(player));
        //notify the player that they go to the PLACE_COMPONENT phase and update their components in the hand
        getController().getMessageManager().sendToPlayer(player.getUsername(), new AssemblingMessage(componentsInHand.get(player).createViewComponent()));
    }
    /**
     * This method is used to rotate a component counterclockwise.
     * @param player the player who is rotating the component
     * @throws InvalidStateException if the player is not in the PLACE_COMPONENT phase
     */
    @Override
    public void rotateComponentCounterclockwise(Player player) throws InvalidStateException {
        if (componentsInHand.get(player) == null) {
            throw new InvalidStateException("Player is not in the PLACE_COMPONENT phase");
        }
        //rotate the component counterclockwise
        getModel().RotateCounterclockwise(componentsInHand.get(player));
        //notify the player that they go to the PLACE_COMPONENT phase and update their components in the hand
        getController().getMessageManager().sendToPlayer(player.getUsername(), new AssemblingMessage(componentsInHand.get(player).createViewComponent()));
    }

    /** this method is used to stop assembling the ship
     * @param player the player who is stopping assembling
     * @param position the position of the player in the game (1-4), then the player will be placed in the correct position
     *                 (based on the level of the game)
     */
    @Override
    public void stopAssembling(Player player, int position) throws InvalidIndexException, InvalidStateException {
        //check if the player is in the PLACE_COMPONENT phase
        if (componentsInHand.get(player) != null) {
            throw new InvalidStateException("Place the component before stopping assembling");
        }
        //end the assembling phase for the player and set the player in the correct position
        getModel().stopAssembling(player, position);
        //notify the player that the position has been set
        getController().getMessageManager().broadcastUpdate(new PlayerUpdateMessage(player.getUsername(), 0, true, player.getColor(), (player.getPosition()%getModel().getGame().getBoard().getSpaces())));
        //mark the player as assembled
        assembled.put(player, true);
        //notify the player that they are in standby phase
        getController().getMessageManager().sendToPlayer(player.getUsername(), new StandbyMessage("Waiting for others to finish assembling"));
    }

    /**
     * This method is used to check if all players have assembled their ship
     * @return true if all players have assembled their ship, false otherwise
     */
    @Override
    public boolean allAssembled() {
        return assembled.values().stream().allMatch(Boolean::booleanValue);
    }

    /**
     * This method is used to peek a deck of cards
     * @param player the player who is peeking the deck
     * @param num the number of the deck to peek (1-3)
     * @implNote note that the phase does not change, so the player can still take a component,
     *          and when it does, the deck will not be peeked anymore
     */
    @Override
    public void peekDeck(Player player, int num) throws InvalidIndexException, InvalidStateException {
        if (componentsInHand.get(player) != null) {
            throw new InvalidStateException("cannot peek deck in this phase");
        }
        checkHourglass();
        //before doing anything, check if the index is correct
        if (num < 1 || num > 3) {
            throw new InvalidIndexException("Invalid deck number");
        }
        //check if the player has peeked a deck already
        for (int i = 1; i < 4; i++) {
            if (deckPeeked.get(i) == player) {
                //if so, remove the peek so others can peek
                deckPeeked.put(i, null);
            }
        }

        //check if the deck has already been peeked
        if (deckPeeked.get(num) == null) {
            deckPeeked.put(num, player);
        } else {
            throw new InvalidIndexException("Deck already peeked");
        }
        getController().getMessageManager().sendToPlayer(player.getUsername(), new DeckPeekedMessage(player.getUsername(), getModel().viewDeck(num)));
    }

    /**
     * this method is used to turn the hourglass
     * @param player the player who is turning the hourglass
     * @throws HourglassException if the hourglass cannot be turned
     */
    @Override
    public void turnHourglass(Player player) throws HourglassException {
        //check if the hourglass can be turned
        //note that to turn the hourglass the last time, you have to have finished the assembling
        if (getModel().getTurnedHourglass() == 1 && !assembled.get(player)) {
            throw new HourglassException("Cannot turn hourglass for the last time for a player that has not completed assembling yet");
        }
        //check if the hourglass has no remaining time
        if (getModel().getRemainingTime() != 0) {
            throw new HourglassException("Cannot turn hourglass if time is not 0");
        }
        getModel().turnHourglass();
        getController().getMessageManager().broadcastUpdate(new HourglassMessage(getModel().getTurnedHourglass(), getModel().getHourglassTimestamp()));
        //if the player has peeked a deck, remove the peek so others can peek
        for (int i = 1; i < 4; i++) {
            if (deckPeeked.get(i) == player) {
                deckPeeked.put(i, null);
            }
        }
        if (componentsInHand.get(player) != null) {
            //notify the player that they go to the PLACE_COMPONENT phase
            getController().getMessageManager().sendToPlayer(player.getUsername(), new AssemblingMessage(componentsInHand.get(player).createViewComponent()));
        } else {
            //notify the player that they go to the TAKE_COMPONENT phase
            getController().getMessageManager().sendToPlayer(player.getUsername(), new AssemblingMessage(null));
        }
    }

    private void takeComponent (Player player, Component component){
        // Add component to player's hand
        componentsInHand.put(player, component);
        //notify the player that they go to the PLACE_COMPONENT phase
        getController().getMessageManager().sendToPlayer(player.getUsername(), new AssemblingMessage(component.createViewComponent()));
        // if the player has peeked at the deck, remove the peek so others can peek
        for (int i = 1; i < 4; i++) {
            if (deckPeeked.get(i) == player) {
                deckPeeked.put(i, null);
            }
        }
    }

    public boolean isConcurrent(){
        return true;
    }

    public void rejoin(String username) {
        //notify the player that they are in the TAKE_COMPONENT phase after updating the model
        getController().getMessageManager().sendToPlayer(username, BoardUpdateMessage.fromBoard(getModel().getGame().getBoard(), getModel().getGame().getPlayers(), true));
        getController().getMessageManager().sendToPlayer(username, PileUpdateMessage.fromComponent(username, getModel().getGame().getPile().getUnviewed().size(), getModel().getGame().getPile().getViewed(), "init unviewed pile"));
        if (getModel().getLevel() == 2) {
            getController().getMessageManager().sendToPlayer(username, new HourglassMessage(getModel().getTurnedHourglass(), getModel().getHourglassTimestamp()));
        }
        if (assembled.get(getController().getPlayerByID(username))) {
            getController().getMessageManager().sendToPlayer(username, new StandbyMessage("Waiting for others to finish assembling"));
        }else if (componentsInHand.get(getController().getPlayerByID(username))==null){
            getController().getMessageManager().sendToPlayer(username, new AssemblingMessage(null));
        }else {
            getController().getMessageManager().sendToPlayer(username, new AssemblingMessage(componentsInHand.get(getController().getPlayerByID(username)).createViewComponent()));
        }

    }

    /**
     * this method is called to resume the game after has been paused
     */
    public void resume(String username){
        //check the assembled status of the players
        for (Player player : getController().getPlayers()) {
            if (player.getUsername().equals(username)) {
                rejoin(username);
            } else if (assembled.get(player)) {
                getController().getMessageManager().sendToPlayer(player.getUsername(), new StandbyMessage("Waiting for " + username + " to finish assembling"));
            } else if (componentsInHand.get(player) == null){
                getController().getMessageManager().sendToPlayer(player.getUsername(), new AssemblingMessage(null));
            } else {
                getController().getMessageManager().sendToPlayer(player.getUsername(), new AssemblingMessage(componentsInHand.get(player).createViewComponent()));
            }
        }
    }

    private void pileUpdate(String username){
        getController().getMessageManager().broadcastUpdate(PileUpdateMessage.fromComponent(username,
                getModel().getGame().getPile().getUnviewed().size(),
                getModel().getGame().getPile().getViewed(),
                "taken from unviewed"));
    }

    private void checkHourglass() throws InvalidStateException{
        //check if the hourglass has been turned
        if (getModel().getLevel()==2 && getModel().getTurnedHourglass() == 2 && getModel().getRemainingTime() == 0 ) {
            throw new InvalidStateException("cannot take a component if the reamining time is 0");
        }
    }
}
