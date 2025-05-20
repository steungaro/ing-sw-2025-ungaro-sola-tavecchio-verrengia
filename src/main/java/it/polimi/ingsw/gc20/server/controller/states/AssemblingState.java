package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.common.message_protocol.toclient.*;
import it.polimi.ingsw.gc20.server.controller.managers.Translator;
import it.polimi.ingsw.gc20.server.exceptions.*;
import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.server.model.components.Component;
import it.polimi.ingsw.gc20.server.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.server.model.player.Player;
import it.polimi.ingsw.gc20.server.network.NetworkService;
import org.javatuples.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssemblingState extends State {
    private final Map<Player, Boolean> assembled = new HashMap<>();
    private final Map<Player, Component> componentsInHand = new HashMap<>();
    private final Map<Integer, Player> deckPeeked = new HashMap<>();
    private final Map<Player, StatePhase> playersPhase = new HashMap<>(); // map of players to their current phase
    /**
     * Default constructor
     */
    public AssemblingState(GameModel model) {
        super(model);
        for (Player player : getModel().getInGamePlayers()) {
            assembled.put(player, false);
            componentsInHand.put(player, null);
            playersPhase.put(player, StatePhase.TAKE_COMPONENT);
            // notify each player of the phase they are in
            NetworkService.getInstance().sendToClient(player.getUsername(), new TakeComponentMessage());
        }
        for (int i = 1; i < 4; i++) {
            deckPeeked.put(i, null);
        }
        getModel().initCountdown();
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
        if (playersPhase.get(player) != StatePhase.TAKE_COMPONENT) {
            throw new InvalidStateException("Player is not in the TAKE_COMPONENT phase");
        }
        // take the component from the unviewed pile
        Component component =Translator.getFromUnviewed(getModel(), index);
        getModel().componentFromUnviewed(component);
        // Add component to player's hand
        componentsInHand.put(player, component);
        // Set the player's phase to PLACE_COMPONENT
        playersPhase.put(player, StatePhase.PLACE_COMPONENT);
        //notify the player that they go to the PLACE_COMPONENT phase
        NetworkService.getInstance().sendToClient(player.getUsername(), new PlaceComponentMessage(component.createViewComponent()));
        // if the player has peeked at the deck, remove the peek so others can peek
        for (int i = 1; i < 4; i++) {
            if (deckPeeked.get(i) == player) {
                deckPeeked.put(i, null);
            }
        }
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
        if (playersPhase.get(player) != StatePhase.TAKE_COMPONENT) {
            throw new InvalidStateException("Player is not in the TAKE_COMPONENT phase");
        }
        //take the component from the viewed pile
        Component component =Translator.getFromViewed(getModel(), index);
        getModel().componentFromViewed(component);
        // Add component to player's hand
        componentsInHand.put(player, component);
        // Set the player's phase to PLACE_COMPONENT
        playersPhase.put(player, StatePhase.PLACE_COMPONENT);
        //notify the player that they go to the PLACE_COMPONENT phase
        NetworkService.getInstance().sendToClient(player.getUsername(), new PlaceComponentMessage(component.createViewComponent()));
        // if the player has peeked at the deck, remove the peek so others can peek
        for (int i = 1; i < 4; i++) {
            if (deckPeeked.get(i) == player) {
                deckPeeked.put(i, null);
            }
        }

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
        if (playersPhase.get(player) != StatePhase.TAKE_COMPONENT) {
            throw new InvalidStateException("Player is not in the TAKE_COMPONENT phase");
        }
        // take the component from the booked pile
        Component component =Translator.getFromBooked(player, index);
        getModel().componentFromBooked(component, player);
        // Add component to player's hand
        componentsInHand.put(player, component);
        // Set the player's phase to PLACE_COMPONENT
        playersPhase.put(player, StatePhase.PLACE_COMPONENT);
        //notify the player that they go to the PLACE_COMPONENT phase
        NetworkService.getInstance().sendToClient(player.getUsername(), new PlaceComponentMessage(component.createViewComponent()));
        // if the player has peeked at the deck, remove the peek so others can peek
        for (int i = 1; i < 4; i++) {
            if (deckPeeked.get(i) == player) {
                deckPeeked.put(i, null);
            }
        }
    }

    /**
     * This method is used to add a component to the booked pile.
     * @param player the player who is adding the component
     * @throws InvalidStateException if the player is not in the PLACE_COMPONENT phase
     * @throws NoSpaceException if there is no space in the booked pile
     */
    @Override
    public void addComponentToBooked(Player player) throws InvalidStateException, NoSpaceException {
        if (playersPhase.get(player) != StatePhase.PLACE_COMPONENT) {
            throw new InvalidStateException("Player is not in the PLACE_COMPONENT phase");
        }
        //put the component in the booked pile
        getModel().componentToBooked(componentsInHand.get(player), player);
        // Remove component from player's hand
        componentsInHand.put(player, null);
        // Set the player's phase to TAKE_COMPONENT
        playersPhase.put(player, StatePhase.TAKE_COMPONENT);
        //notify the player that they go to the TAKE_COMPONENT phase
        NetworkService.getInstance().sendToClient(player.getUsername(), new TakeComponentMessage());
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
        if (playersPhase.get(player) != StatePhase.PLACE_COMPONENT) {
            throw new InvalidStateException("Player is not in the PLACE_COMPONENT phase");
        }
        // add the component to the viewed pile
        getModel().componentToViewed(componentsInHand.get(player));

        // Remove component from player's hand
        componentsInHand.put(player, null);
        // Set the player's phase to TAKE_COMPONENT
        playersPhase.put(player, StatePhase.TAKE_COMPONENT);
        //notify the player that they go to the TAKE_COMPONENT phase
        NetworkService.getInstance().sendToClient(player.getUsername(), new TakeComponentMessage());
    }
    /**
     * This method is used to place a component on the ship.
     * @param player the player who is placing the component
     * @param coordinates the coordinates of the component on the ship
     */
    @Override
    public void placeComponent(Player player, Pair<Integer, Integer> coordinates) throws InvalidStateException, InvalidTileException {
        //check if the player is in the PLACE_COMPONENT phase
        if (playersPhase.get(player) != StatePhase.PLACE_COMPONENT) {
            throw new InvalidStateException("Player is not in the PLACE_COMPONENT phase");
        }
        //add the component in the hand to the ship
        getModel().addToShip(componentsInHand.get(player), player, coordinates.getValue0(), coordinates.getValue1());
        // Remove component from player's hand
        componentsInHand.put(player, null);
        // Set the player's phase to TAKE_COMPONENT
        playersPhase.put(player, StatePhase.TAKE_COMPONENT);
        //notify the player that they go to the TAKE_COMPONENT phase
        NetworkService.getInstance().sendToClient(player.getUsername(), new TakeComponentMessage());
    }
    /**
     * This method is used to rotate a component clockwise.
     * @param player the player who is rotating the component
     * @throws InvalidStateException if the player is not in the PLACE_COMPONENT phase
     */
    @Override
    public void rotateComponentClockwise(Player player) throws InvalidStateException{
        //check if the player has a component in the hand
        if (playersPhase.get(player) != StatePhase.PLACE_COMPONENT) {
            throw new InvalidStateException("Player is not in the PLACE_COMPONENT phase");
        }
        //rotate the component clockwise
        getModel().RotateClockwise(componentsInHand.get(player));
        //notify the player that they go to the PLACE_COMPONENT phase and update their components in the hand
        NetworkService.getInstance().sendToClient(player.getUsername(), new PlaceComponentMessage(componentsInHand.get(player).createViewComponent()));
    }
    /**
     * This method is used to rotate a component counterclockwise.
     * @param player the player who is rotating the component
     * @throws InvalidStateException if the player is not in the PLACE_COMPONENT phase
     */
    @Override
    public void rotateComponentCounterclockwise(Player player) throws InvalidStateException {
        if (playersPhase.get(player) != StatePhase.PLACE_COMPONENT) {
            throw new InvalidStateException("Player is not in the PLACE_COMPONENT phase");
        }
        //rotate the component counterclockwise
        getModel().RotateCounterclockwise(componentsInHand.get(player));
        //notify the player that they go to the PLACE_COMPONENT phase and update their components in the hand
        NetworkService.getInstance().sendToClient(player.getUsername(), new PlaceComponentMessage(componentsInHand.get(player).createViewComponent()));
    }

    /** this method is used to stop assembling the ship
     * @param player the player who is stopping assembling
     * @param position the position of the player in the game (1-4), then the player will be placed in the correct position
     *                 (based on the level of the game)
     */
    @Override
    public void stopAssembling(Player player, int position) throws InvalidIndexException, InvalidStateException {
        //check if the player is in the PLACE_COMPONENT phase
        if (playersPhase.get(player) == StatePhase.PLACE_COMPONENT) {
            throw new InvalidStateException("Place the component before stopping assembling");
        }
        //end the assembling phase for the player and set the player in the correct position
        getModel().stopAssembling(player, position);
        //notify the player that the position has been set
        for (String username : getController().getInGameConnectedPlayers()){
            NetworkService.getInstance().sendToClient(username, new PlayerUpdateMessage(player.getUsername(), 0, true, player.getColor(), (player.getPosition()%getModel().getGame().getBoard().getSpaces())));
        }
        //mark the player as assembled
        assembled.put(player, true);
        //put the player in standby phase
        playersPhase.put(player, StatePhase.STANDBY_PHASE);
        //notify the player that they are in standby phase
        NetworkService.getInstance().sendToClient(player.getUsername(), new StandbyMessage("Waiting for others to finish assembling"));
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
     * @return List<AdventureCard> the list of cards in the deck
     * @implNote note that the phase does not change, so the player can still take a component,
     *          and when it does, the deck will not be peeked anymore
     */
    @Override
    public List<AdventureCard> peekDeck(Player player, int num) throws InvalidIndexException, InvalidStateException {
        if (playersPhase.get(player) != StatePhase.TAKE_COMPONENT) {
            throw new InvalidStateException("cannot peek deck in this phase");
        }
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
        return getModel().viewDeck(num);
    }

    /**
     * this method is used to get the time left in the hourglass
     * @param player the player who is asking for the time
     * @return the time left in the hourglass
     */
    @Override
    public int getHourglassTime(Player player) {
        NetworkService.getInstance().sendToClient(player.getUsername(), new HourglassMessage(getModel().getRemainingTime(), getModel().getTurnedHourglass()));
        return getModel().getRemainingTime();
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
        //turn the hourglass
        getModel().turnHourglass();
        //if the player has peeked a deck, remove the peek so others can peek
        for (int i = 1; i < 4; i++) {
            if (deckPeeked.get(i) == player) {
                deckPeeked.put(i, null);
            }
        }
    }
}
