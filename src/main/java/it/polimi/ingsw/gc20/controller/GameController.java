package it.polimi.ingsw.gc20.controller;

import it.polimi.ingsw.gc20.controller.event.Event;
import it.polimi.ingsw.gc20.controller.event.EventType;
import it.polimi.ingsw.gc20.model.cards.*;
import it.polimi.ingsw.gc20.model.components.*;
import it.polimi.ingsw.gc20.model.gamesets.*;
import it.polimi.ingsw.gc20.model.player.*;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Controller class for managing the game flow and player interactions
 */
public class GameController {
    private final GameModel model;
    private State state;
    private final String gameID;
    private final List<String> connectedPlayers = new ArrayList<>();
    private final List<String> disconnectedPlayers = new ArrayList<>();
    private final Map<EventType<? extends Event>, List<EventHandler<? extends Event>>> eventHandlers = new HashMap<>();
    private final Map<String, Boolean> assemblingComplete = new HashMap<>();
    private final Map<String, Boolean> readyToFly = new HashMap<>();
    private String currentPlayer;
    private List<CargoColor> cargo;
    private List<Projectile> projectiles;

    /**
     * Default constructor
     *
     * @param id        unique identifier for this game
     * @param usernames list of player usernames
     * @param level     game difficulty level
     * @throws IllegalArgumentException if number of players is not between 2 and 4
     */
    public GameController(String id, List<String> usernames, int level) {
        if(usernames.size() > 4 || usernames.size() < 2) {
            throw new IllegalArgumentException("The number of players must be between 2 and 4");
        }
        gameID = id;
        model = new GameModel();
        model.startGame(level, usernames, gameID);
        state = State.ASSEMBLING;
        currentPlayer = "";
        connectedPlayers.addAll(usernames);
        usernames.forEach(username -> assemblingComplete.put(username, false));
        usernames.forEach(username -> readyToFly.put(username, false));
        //TODO: notify players of game start
    }

    /**
     * Private function that moves the state forward to the next state in sequence
     */
    private void nextState() {
        state = State.values()[(state.ordinal() + 1) % State.values().length];
    }

    /**
     * Sets the game state to assembling phase
     */
    public boolean assemblingStateComplete(){
        //TODO: Implement assembling phase
        return false;
    }

    /**
     * Handles the card drawing phase of the game
     * Processes the card based on its type
     */
    private void drawCard(){
        if (state != State.FLIGHT) {
            throw new IllegalStateException("Cannot draw cards outside the flying phase");
        }
        AdventureCard card = model.drawCard();
        if (card == null) {
            state = State.ENDGAME;
            //TODO: notify players of state change
            //TODO: calculate final scores
        }

        if (card instanceof Planets) {
            state = State.WAITING_PLANET;
            //TODO: notify players of state change

        } else if (card instanceof AbandonedShip) {
            state = State.WAITING_CREW;
            //TODO: notify players of state change

        } else if (card instanceof AbandonedStation) {
            state = State.WAITING_ACCEPTANCE;
            //TODO: notify players of state change

        } else if (card instanceof CombatZone) {

        } else if (card instanceof Epidemic) {
            for (Player p : model.getGame().getPlayers()) {
                ((Epidemic) card).Effect(p);
            }
            //TODO: notify players of state change
            drawCard();

        } else if (card instanceof MeteorSwarm) {
            state = State.FIRING;
            projectiles = ((MeteorSwarm) card).getMeteors();
            //TODO: notify players of state change

        } else if (card instanceof OpenSpace) {
            state = State.WAITING_ENGINES;
            //TODO: notify players of state change

        } else if (card instanceof Enemy) {
            state = State.WAITING_CANNONS;
            //TODO: notify players of state change

        } else if (card instanceof Stardust) {
            for (Player p : model.getGame().getPlayers()) {
                model.Stardust(p);
            }
            //TODO: notify players of state change
        }
        currentPlayer = model.getGame().getPlayers().stream().filter(p -> connectedPlayers.contains(p.getUsername())).findFirst().get().getUsername();
    }

    public List<CargoColor> landOnPlanet(String username, int planetIndex) {
        if (state != State.WAITING_PLANET) {
            throw new IllegalStateException("Cannot land on a planet outside the planet phase");
        }
        Player player = getPlayerByID(username);
        state = State.WAITING_CARGO_GAIN;
        return model.PlanetLand(player, planetIndex);
    }

    public void loadCargo(String username, CargoColor loaded, int component) {
        if (state != State.WAITING_CARGO_GAIN) {
            throw new IllegalStateException("Cannot load cargo outside the cargo phase");
        }
        Player player = getPlayerByID(username);
        //TODO check if cargo is valid
        if (getComponentByID(component) == null) {
            throw new IllegalArgumentException("Component not found");
        } else {
            if (!cargo.contains(loaded)) {
                throw new IllegalArgumentException("Cargo does not contain the loaded component");
            }
            cargo.remove(loaded);
            model.addCargo(getPlayerByID(username), loaded, (CargoHold) getComponentByID(component));
        }
    }

    public void unloadCargo(String username, CargoColor lost, int component) {
        if (state != State.WAITING_CARGO_GAIN && state != State.WAITING_CARGO_LOST) {
            throw new IllegalStateException("Cannot unload cargo outside the cargo phase");
        }
        Player player = getPlayerByID(username);
        if (getComponentByID(component) == null) {
            throw new IllegalArgumentException("Component not found");
        } else {
            if (!cargo.isEmpty() && cargo.contains(lost)) {
                throw new IllegalArgumentException("Cargo does not contain the lost component");
            } else if (!cargo.isEmpty()) {
                cargo.remove(lost);
            }
            model.MoveCargo(getPlayerByID(username), lost, (CargoHold) getComponentByID(component), null);
        }
    }

    public void moveCargo(String username, CargoColor cargo, int from, int to) {
        if (state != State.WAITING_CARGO_GAIN && state != State.WAITING_CARGO_LOST) {
            throw new IllegalStateException("Cannot move cargo outside the cargo phase");
        }
        Player player = getPlayerByID(username);
        if (getComponentByID(from) == null || getComponentByID(to) == null) {
            throw new IllegalArgumentException("Component not found");
        } else {
            model.MoveCargo(getPlayerByID(username), cargo, (CargoHold) getComponentByID(from), (CargoHold) getComponentByID(to));
        }
    }

    public void abandonedShip(String username, List<Integer> components) {
        if (state != State.WAITING_CREW && model.getActiveCard() instanceof AbandonedShip) {
            throw new IllegalStateException("Cannot abandon ship outside the abandoned ship card");
        }
        if (!username.equals(currentPlayer)) {
            throw new IllegalArgumentException("Not your turn");
        }
        if (model.getCrew(getPlayerByID(username)) < ((AbandonedShip)model.getActiveCard()).getLostCrew()) {
            throw new IllegalStateException("Cannot abandon ship with insufficient crew");
        }
        Player player = getPlayerByID(username);
        List<Cabin> cabins =  components.stream().map(id -> (Cabin) getComponentByID(id)).toList();
        model.AbandonedShip(getPlayerByID(username), cabins);
    }

    public List<CargoColor> abandonedStation(String username) {
        AbandonedStation card = (AbandonedStation) model.getActiveCard();
        if(state != State.WAITING_ACCEPTANCE){
            throw new IllegalStateException("Cannot land on station outside the abandoned station phase");
        } else if (getPlayerByID(username).getShip().crew() < card.getCrewNeeded()) {
            throw new IllegalStateException("Cannot invade station with insufficient crew");
        }
        state = State.WAITING_CARGO_GAIN;
        return model.AbandonedStation(getPlayerByID(username));
    }

    public int shootEnemy(String username, List<String> componentIDs, int energy){
        if (state != State.WAITING_CANNONS) {
            throw new IllegalStateException("Cannot activate cannons now");
        }

        Player player = getPlayerByID(username);
        Set<Cannon> components = componentIDs.stream().map(id -> (Cannon) getComponentByID(Integer.parseInt(id))).collect(Collectors.toSet());
        if(player.getShip().firePower(components, energy) > ((Enemy) model.getActiveCard()).getFirePower()){
            model.getActiveCard().playCard();
            state = State.WAITING_ACCEPTANCE;
            return 1;
        } else if (player.getShip().firePower(components, energy) == ((Enemy) model.getActiveCard()).getFirePower()){
            endMove(username);
            return 0;
        } else {
            if (model.getActiveCard() instanceof Pirates) {
                state = State.FIRING;
            } else if (model.getActiveCard() instanceof Slavers) {
                state = State.WAITING_CREW;
            } else if (model.getActiveCard() instanceof Smugglers) {
                state = State.WAITING_CARGO_LOST;
            }
            return -1;
        }
    }

    public List<CargoColor> acceptSmugglers(String username) {
        if (state != State.WAITING_ACCEPTANCE) {
            throw new IllegalStateException("Cannot accept smugglers now");
        }
        if (!model.getActiveCard().isPlayed()) {
            throw new IllegalStateException("Card not defeated");
        }
        state = State.WAITING_CARGO_GAIN;
        return model.smugglersSuccess(getPlayerByID(username));
    }

    public void acceptPirates(String username) {
        if (state != State.WAITING_ACCEPTANCE) {
            throw new IllegalStateException("Cannot accept pirates now");
        }
        if (!model.getActiveCard().isPlayed()) {
            throw new IllegalStateException("Card not defeated");
        }
        model.piratesSuccess(getPlayerByID(username));
    }

    public void acceptSlavers(String username) {
        if (state != State.WAITING_ACCEPTANCE) {
            throw new IllegalStateException("Cannot accept slavers now");
        }
        if (!model.getActiveCard().isPlayed()) {
            throw new IllegalStateException("Card not defeated");
        }
        model.slaversSuccess(getPlayerByID(username));
    }

    private void nextFire() {
        projectiles.removeFirst();
        if (projectiles.isEmpty()) {
            return;
        } else if (projectiles.getFirst().getFireType() == FireType.LIGHT_METEOR) {
            state = State.WAITING_SHIELDS;
        } else if (projectiles.getFirst().getFireType() == FireType.HEAVY_METEOR) {
            state = State.WAITING_CANNONS;
        } else if (projectiles.getFirst().getFireType() == FireType.HEAVY_FIRE) {
            //TODO: fire heavy fire (not parabile)
        } else if (projectiles.getFirst().getFireType() == FireType.LIGHT_FIRE) {
            state = State.WAITING_SHIELDS;
        }
    }

    public void activateShield(String username, int shieldComp, int energyComp) {
        //TODO: maybe if shield is 0/0 fire without shield
        if (state != State.WAITING_SHIELDS && (projectiles.getFirst().getFireType() != FireType.LIGHT_METEOR || projectiles.getFirst().getFireType() != FireType.LIGHT_METEOR)) {
            throw new IllegalStateException("Cannot activate shields now");
        }
        Player player = getPlayerByID(username);
        Shield shield = (Shield) getComponentByID(shieldComp);
        //TODO: verify shield is valid (activate fire or discard it)
        if (projectiles.size() > 1) {
            nextFire();
        } else {
            endMove(username);
        }
    }

    public void activateCannons(String username, int cannonComp, int energy) {
        if (state != State.WAITING_CANNONS) {
            throw new IllegalStateException("Cannot activate cannons now");
        }
        Player player = getPlayerByID(username);
        Set<Cannon> components = componentIDs.stream().map(id -> (Cannon) getComponentByID(Integer.parseInt(id))).collect(Collectors.toSet());
        if (player.getShip().firePower(components, energy) >= projectiles.getFirst().getFirePower()) {
            nextFire();
        } else {
            endMove(username);
        }
    }



    public void endMove(String username) {
        if (state != State.WAITING_CARGO && state != State.WAITING_CREW && state != State.WAITING_ENGINES && state != State.WAITING_CANNONS && state != State.WAITING_SHIELDS && state != State.WAITING_PLANET) {
            throw new IllegalStateException("Cannot end move outside the card phase");
        }
        if (!username.equals(currentPlayer)) {
            throw new IllegalArgumentException("Not your turn");
        }
        // TODO select only active players
        currentPlayer = model.getGame().getPlayers().get((model.getGame().getPlayers().indexOf(getPlayerByID(currentPlayer)) + 1) % model.getGame().getPlayers().size()).getUsername();
        if (model.getActiveCard() instanceof Planets) {
            state = State.WAITING_PLANET;
        }




        if (Objects.equals(currentPlayer, model.getGame().getPlayers().getFirst().getUsername())) { //last player played their turn
            if (model.getActiveCard() instanceof Planets) {
                model.movePlayerReverse();
            }
            state = State.FLIGHT;
            drawCard();
        }
    }

    /**
     * Gets a list of player colors that aren't currently assigned to any player
     *
     * @return List of available player colors
     */
    public List<PlayerColor> getAvailableColors(){
        List<PlayerColor> availableColors = new ArrayList<>(Arrays.asList(PlayerColor.values()));

        for (Player p: model.getGame().getPlayers()){
            PlayerColor color = p.getColor();
            availableColors.remove(color);
        }
        return availableColors;
    }

    public void initPlayersShip(){
        for (Player p: model.getGame().getPlayers()){
            if(!model.shipValidating(p)){
                //TODO: ship loop until bro gives us a valid ship
            }
        }

    }

    /**
     * Sets the color for a specific player
     *
     * @param username Username of the player to update
     * @param color Color to assign to the player
     * @throws IllegalArgumentException if player is not found or color is already taken
     */
    public void setPlayerColor(String username, PlayerColor color) {
        // Check if color is available
        List<PlayerColor> availableColors = getAvailableColors();
        if (!availableColors.contains(color)) {
            throw new IllegalArgumentException("Color is already taken by another player");
        }

        // Find player with matching username
        Player targetPlayer = null;
        for (Player p : model.getGame().getPlayers()) {
            if (p.getUsername().equals(username)) {
                targetPlayer = p;
                break;
            }
        }

        if (targetPlayer == null) {
            throw new IllegalArgumentException("Player not found in game");
        }

        // Set the color for the player
        targetPlayer.setColor(color);
    }

    public List<AdventureCard> peekDeck(int num){
        return model.viewDeck(num);
    }
    /**
     * Calculates and returns the final scores for all players
     *
     * @return Map associating each player with their score
     */
    public Map<Player, Integer> getPlayerScores(){
        return model.calculateScore();
    }

    /**
     * Handles a player leaving the game mid-session
     *
     * @param username Username of the exiting player
     * @throws IllegalArgumentException if player with given username is not found
     */
    public void disconnectPlayer(String username) {
        // Find player with matching username
        Player exitingPlayer = null;
        for (Player p : model.getGame().getPlayers()) {
            if (p.getUsername().equals(username)) {
                exitingPlayer = p;
                connectedPlayers.remove(username);
                break;
            }
        }

        if (exitingPlayer == null) {
            throw new IllegalArgumentException("Player not found in game");
        }

        // Add player to disconnected list if not already there
        if (!disconnectedPlayers.contains(username)) {
            disconnectedPlayers.add(username);
        }

        // Player remains in the game model but is marked as disconnected
    }

    /**
     * Handles reconnection of a previously disconnected player
     *
     * @param username Username of the player attempting to reconnect
     * @return true if reconnection was successful, false otherwise
     * @throws IllegalArgumentException if the player was never part of this game
     */
    public boolean reconnectPlayer(String username) {
        // Check if player was originally in this game
        if (!connectedPlayers.contains(username)) {
            throw new IllegalArgumentException("Player was never part of this game");
        }

        // Check if player is in the disconnected list
        if (!disconnectedPlayers.contains(username)) {
            return false; // Player isn't disconnected, nothing to do
        }

        // Remove player from disconnected list
        disconnectedPlayers.remove(username);

        // Player data is still in the model, so no need to recreate
        return true;
    }

    /**
     * Retrieves player data by username
     *
     * @param username Username of the player
     * @return Player object containing player data
     * @throws IllegalArgumentException if the player is not found
     */
    private Player getPlayerByID(String username){
        return model.getGame().getPlayers().stream().filter(player -> player.getUsername().equals(username)).findFirst().orElseThrow(() -> new IllegalArgumentException("Player not found"));
    }


    /**
     * Retrieves all usernames of players in the game
     *
     * @return List of player usernames
     */
    public List<String> getAllUsernames() {
        List<String> usernames = new ArrayList<>();
        for (Player p : model.getGame().getPlayers()) {
            usernames.add(p.getUsername());
        }
        return usernames;
    }

    /**
     * Retrieves the list of players who have disconnected from the game
     *
     * @return List of disconnected player usernames
     */
    public List<String> getDisconnectedPlayers() {
        return new ArrayList<>(disconnectedPlayers);
    }

    /**
     * Checks if a player is currently disconnected
     *
     * @param username Username to check
     * @return true if player is disconnected, false otherwise
     */
    public boolean isPlayerDisconnected(String username) {
        return disconnectedPlayers.contains(username);
    }

    /**
     * Gets the current state of the game
     *
     * @return Current game state
     */
    public State getState() {
        return state;
    }

    /**
     * Takes a component from the unviewed pile
     *
     * @param username Username of the player taking the component
     * @param component Component to take from the unviewed pile
     * @return The taken component
     * @throws IllegalStateException if game is not in ASSEMBLING state
     * @throws NoSuchElementException if the component is not in the unviewed pile
     */
    public synchronized Component takeComponentFromUnviewed(String username, Component component) {
        if (state != State.ASSEMBLING) {
            throw new IllegalStateException("Cannot take components outside the assembling phase");
        }

        try {
            model.componentFromUnviewed(component);
            // Add component to viewed pile so other players can see it
            model.componentToViewed(component);
            return component;
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Component not found in unviewed pile");
        }
    }

    /**
     * Takes a component from the viewed pile
     *
     * @param username Username of the player taking the component
     * @param component Component to take from the viewed pile
     * @return The taken component
     * @throws IllegalStateException if game is not in ASSEMBLING state
     * @throws NoSuchElementException if the component is not in the viewed pile //TODO check if this is the right exception
     */
    public synchronized Component takeComponentFromViewed(String username, Component component) {
        if (state != State.ASSEMBLING) {
            throw new IllegalStateException("Cannot take components outside the assembling phase");
        }

        try {
            model.componentFromViewed(component);
            return component;
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Component not found in viewed pile");
        }
    }

    /**
     * Takes a component that was previously booked by a player (Level 2 only)
     *
     * @param username Username of the player taking the component
     * @param component Component to take from the booked pile
     * @return The taken component
     * @throws IllegalStateException if game is not in ASSEMBLING state or not level 2
     * @throws IllegalArgumentException if the component is not in the player's booked list
     */
    public Component takeComponentFromBooked(String username, Component component) {
        if (state != State.ASSEMBLING) {
            throw new IllegalStateException("Cannot take components outside the assembling phase");
        }

        if (model.getLevel() != 2) {
            throw new IllegalStateException("Booking components is only available in level 2 games");
        }

        Player player = getPlayerByID(username);

        try {
            model.componentFromBooked(component, player);
            return component;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Component not found in player's booked list");
        }
    }

    /**
     * Adds a component to player's booked list (Level 2 only)
     *
     * @param username Username of the player booking the component
     * @param component Component to book
     * @throws IllegalStateException if game is not in ASSEMBLING state or not level 2
     */
    public void addComponentToBooked(String username, Component component) {
        if (state != State.ASSEMBLING) {
            throw new IllegalStateException("Cannot book components outside the assembling phase");
        }

        if (model.getLevel() != 2) {
            throw new IllegalStateException("Booking components is only available in level 2 games");
        }

        Player player = getPlayerByID(username);
        model.componentToBooked(component, player);
    }

    /**
     * Adds a component to the viewed pile so other players can see it
     *
     * @param component Component to add to viewed pile
     * @throws IllegalStateException if game is not in ASSEMBLING state
     */
    public synchronized void addComponentToViewed(Component component) {
        if (state != State.ASSEMBLING) {
            throw new IllegalStateException("Cannot manipulate components outside the assembling phase");
        }

        model.componentToViewed(component);
    }

    /**
     * Places a component on the player's ship at specified coordinates
     *
     * @param username Username of the player placing the component
     * @param component Component to place
     * @param x X-coordinate on ship grid
     * @param y Y-coordinate on ship grid
     * @throws IllegalStateException if game is not in ASSEMBLING state
     * @throws InvalidParameterException if placement is invalid (handled by ship implementation)
     */
    public void placeComponent(String username, Component component, int x, int y) {
        if (state != State.ASSEMBLING) {
            throw new IllegalStateException("Cannot place components outside the assembling phase");
        }

        Player player = getPlayerByID(username);

        try {
            model.addToShip(component, player, x, y);
        } catch (Exception e) {
            // Ship implementation will throw appropriate exceptions if placement is invalid
            throw new InvalidParameterException("Cannot place component at specified location: " + e.getMessage());
        }
    }

    private Component getComponentByID(int id){
        return null; //TODO TAVE
    }

    /**
     * Rotates a component clockwise
     *
     * @param component Component to rotate
     */
    public Component rotateComponentClockwise(int component) {
        Component c = getComponentByID(component);
        model.RotateClockwise(c);
        return c;
    }

    /**
     * Rotates a component counterclockwise
     *
     * @param component Component to rotate
     */
    public Component rotateComponentCounterclockwise(int component) {
        Component c = getComponentByID(component);
        model.RotateCounterclockwise(c);
        return c;
    }

    /**
     * Removes a component from the player's ship (only during assembling phase if the ship is not valid)
     *
     * @param username Username of the player removing the component
     * @param component Component to remove
     * @throws IllegalStateException if game is not in ASSEMBLING state
     */
    public void removeComponentFromShip(String username, Component component) {
        if (state != State.VALIDATING) {
            throw new IllegalStateException("Cannot remove components outside the assembling phase");
        }
        Player player = getPlayerByID(username);
        model.removeComponent(component, player);
    }

    public boolean validateShip(String username) {
        if (state != State.VALIDATING) {
            throw new IllegalStateException("Cannot validate ship outside the assembling phase");
        }
        return model.shipValidating(getPlayerByID(username));
    }

    public void stopAssembling(String username, int position) {
        if (state != State.ASSEMBLING) {
            throw new IllegalStateException("Cannot validate ship outside the assembling phase");
        }
        assemblingComplete.put(username, true);
        model.stopAssembling(getPlayerByID(username), position);
        if(assemblingComplete.values().stream().allMatch(Boolean::booleanValue)){
            nextState();
            //TODO: notify players of state change
        }
    }
    
    public void turnHourglass(String username) {
        if (state != State.ASSEMBLING) {
            throw new IllegalStateException("Cannot validate ship outside the assembling phase");
        }
        if (!connectedPlayers.contains(username)) {
            throw new IllegalArgumentException("Cannot turn hourglass for not connected player");
        }
        if (model.getLevel() != 2) {
            throw new IllegalStateException("Hourglass is only available in level 2 games");
        }
        //TODO tave: hourglass management in gamemodel
    }
    
    public int getHourglassTime(String username) {
        if (state != State.ASSEMBLING) {
            throw new IllegalStateException("Cannot validate ship outside the assembling phase");
        }
        if (model.getLevel() != 2) {
            throw new IllegalStateException("Hourglass is only available in level 2 games");
        }
        //TODO tave: hourglass management in gamemodel
        return 0; //return remaining time
    }

    public List<AdventureCard> peekDeck(String username, int num){
        if (state != State.ASSEMBLING) {
            throw new IllegalStateException("Cannot view a deck outside the assembling phase");
        }
        if (model.getLevel() != 2) {
            throw new IllegalStateException("Decks are only available in level 2 games");
        }
        if (!connectedPlayers.contains(username)) {
            throw new IllegalArgumentException("Cannot view deck for not connected player");
        }
        if (assemblingComplete.get(username)){
            throw new IllegalArgumentException("Cannot view deck after ship is done assembling");
        }
        return model.viewDeck(num);
    }

    public void addAlien(String username, AlienColor color, int component) {
        if (state != State.VALIDATING) {
            throw new IllegalStateException("Cannot add aliens outside the validating phase");
        }
        if (!model.shipValidating(getPlayerByID(username))) {
            throw new IllegalArgumentException("Cannot add alien to invalid ship");
        }
        if (!(getComponentByID(component) instanceof Cabin) && (getComponentByID(component) instanceof StartingCabin)) {
            throw new IllegalArgumentException("Aliens can only be placed in cabins");
        }
        if(model.getLevel() != 2){
            throw new IllegalArgumentException("Aliens are only available in level 2 games");
        }
        model.setAlien(color, (Cabin)getComponentByID(component), getPlayerByID(username));
    }

    public void initShip(String username) {
        if (state != State.VALIDATING) {
            throw new IllegalStateException("Cannot initiate ship outside the validating phase");
        }
        model.getGame().getPlayers().forEach(p -> {if(p.equals(getPlayerByID(username))) p.getShip().initAstronauts();});
        readyToFly.put(username, true);
        if(readyToFly.values().stream().allMatch(Boolean::booleanValue)) {
            nextState();
            model.createDeck();
            //TODO: notify players of state change
            drawCard();
        }
    }
        
}
