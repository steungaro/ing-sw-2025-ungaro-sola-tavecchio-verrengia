package it.polimi.ingsw.gc20.controller;

import it.polimi.ingsw.gc20.controller.event.Event;
import it.polimi.ingsw.gc20.controller.event.EventType;
import it.polimi.ingsw.gc20.exceptions.InvalidShipException;
import it.polimi.ingsw.gc20.model.cards.*;
import it.polimi.ingsw.gc20.model.components.*;
import it.polimi.ingsw.gc20.model.gamesets.*;
import it.polimi.ingsw.gc20.model.player.*;
import java.security.InvalidParameterException;
import java.util.*;


/**
 * Controller class for managing the game flow and player interactions
 */
public class GameController {
    private final GameModel model;
    private State state;
    private State pausedState;
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

    private Player nextOnlinePlayer (String username) {
        List<Player> players = model.getGame().getPlayers();
        int index = players.indexOf(getPlayerByID(username));
        for (int i = 1; i < players.size(); i++) {
            Player p = players.get((index + i) % players.size());
            if (connectedPlayers.contains(p.getUsername())) {
                return p;
            }
        }
        return null;
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
            return;
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

    /**
     * Accepts a planet card and lands on the planet
     * @param username is the username of the player that wants to land on the planet
     * @param planetIndex is the index of the planet card in the player's hand
     * @return the list of cargo that the player can take from the planet
     * @throws IllegalStateException if the game is not in the planet phase
     * @throws IllegalArgumentException if it is not the player's turn
     */
    public List<CargoColor> landOnPlanet(String username, int planetIndex) {
        if (state != State.WAITING_PLANET) {
            throw new IllegalStateException("Cannot land on a planet outside the planet phase");
        }
        if (!username.equals(currentPlayer)) {
            throw new IllegalArgumentException("Not your turn");
        }
        Player player = getPlayerByID(username);
        state = State.WAITING_CARGO_GAIN;
        cargo = model.PlanetLand(player, planetIndex);
        return cargo;
    }

    /**
     * Loads cargo onto the player's ship
     * @param username is the username of the player that wants to load the cargo
     * @param loaded is the cargo that the player wants to load
     * @param ch is the cargo hold where the player wants to load the cargo
     * @throws IllegalStateException if the game is not in the cargo loading phase
     * @throws IllegalArgumentException if it is not the player's turn
     * @throws IllegalArgumentException if the array of cargo provided by the current card does not contain the loaded component
     * @apiNote To be used after accepting a planet, accepting a smuggler, accepting an abandoned station
     */
    public void loadCargo(String username, CargoColor loaded, CargoHold ch) {
        if (state != State.WAITING_CARGO_GAIN) {
            throw new IllegalStateException("Cannot load cargo outside the cargo phase");
        }
        if (!username.equals(currentPlayer)) {
            throw new IllegalArgumentException("Not your turn");
        }
        Player player = getPlayerByID(username);
        if (!cargo.contains(loaded)) {
            throw new IllegalArgumentException("Cargo does not contain the loaded component");
        }
        cargo.remove(loaded);
        model.addCargo(getPlayerByID(username), loaded, ch);
    }

    /**
     * Unloads cargo from the player's ship
     * @param username is the username of the player that wants to unload the cargo
     * @param lost is the cargo that the player wants to unload
     * @param ch is the cargo hold where the player wants to unload the cargo
     * @throws IllegalStateException if the game is not in the cargo unloading phase
     * @throws IllegalArgumentException if it is not the player's turn
     * @throws IllegalArgumentException if the player is not unloading the most valuable cargo (only in LOST CARGO PHASE)
     * @apiNote To be used after accepting a planet, accepting a smuggler, accepting an abandoned station (to unload without limits) or after smugglers, combatzone (to remove most valuable one)
     */
    public void unloadCargo(String username, CargoColor lost, CargoHold ch) {
        if (!username.equals(currentPlayer)) {
            throw new IllegalArgumentException("Not your turn");
        }
        if (state != State.WAITING_CARGO_GAIN && state != State.WAITING_CARGO_LOST) {
            throw new IllegalStateException("Cannot unload cargo outside the cargo phase");
        }
        Player player = getPlayerByID(username);

        if (state == State.WAITING_CARGO_LOST) {
            Map<CargoColor, Integer> playerCargo = player.getShip().getCargo();
            for (CargoColor c : playerCargo.keySet()) {
                if (c.ordinal() < lost.ordinal() && playerCargo.get(c) > 0) {
                    throw new IllegalArgumentException("Not unloading the most valuable cargo.");
                }
            }
        }
        model.MoveCargo(getPlayerByID(username), lost, ch, null);
    }

    /**
     * Moves cargo from one cargo hold to another
     * @param username is the username of the player that wants to move the cargo
     * @param cargo is the cargo that the player wants to move
     * @param from is the cargo hold where the player wants to move the cargo from
     * @param to is the cargo hold where the player wants to move the cargo to
     * @throws IllegalStateException if the game is not in the cargo moving phase
     * @throws IllegalArgumentException if it is not the player's turn
     * @apiNote To be used in losing/gaining cargo
     */
    public void moveCargo(String username, CargoColor cargo, CargoHold from, CargoHold to) {
        if (state != State.WAITING_CARGO_GAIN && state != State.WAITING_CARGO_LOST) {
            throw new IllegalStateException("Cannot move cargo outside the cargo phase");
        }
        if (!username.equals(currentPlayer)) {
            throw new IllegalArgumentException("Not your turn");
        }
        Player player = getPlayerByID(username);
        model.MoveCargo(getPlayerByID(username), cargo, from, to);
    }

    /**
     * Unloads crew from the player's ship to activate the card -> player gains credits and loses days
     * @param username is the username of the player that wants to activate abandoned ship card
     * @param cabins is the list of cabins that the player wants to unload crew from (make it double if removing two asstronauts)
     * @throws IllegalStateException if the game is not in the abandoned ship phase
     * @throws IllegalArgumentException if the player does not have enough crew to abandon the ship
     * @throws IllegalArgumentException if it is not the player's turn
     * @apiNote To be used after accepting an abandoned ship
     */
    public void abandonedShip(String username, List<Cabin> cabins) {
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
        model.AbandonedShip(getPlayerByID(username), cabins);
    }

    /**
     * Activates abandoned station card, returns the list of cargo provided by the card and enters the cargo phase
     * @param username is the username of the player that wants to activate abandoned station card
     * @return the list of cargo that the player can take from the abandoned station
     * @throws IllegalStateException if the game is not in the abandoned station phase
     * @throws IllegalArgumentException if it is not the player's turn
     * @throws IllegalArgumentException if the player does not have enough crew to invade the station
     */
    public List<CargoColor> abandonedStation(String username) {
        AbandonedStation card = (AbandonedStation) model.getActiveCard();
        if (!username.equals(currentPlayer)) {
            throw new IllegalArgumentException("Not your turn");
        }
        if(state != State.WAITING_ACCEPTANCE){
            throw new IllegalStateException("Cannot land on station outside the abandoned station phase");
        } else if (getPlayerByID(username).getShip().crew() < card.getCrewNeeded()) {
            throw new IllegalStateException("Cannot invade station with insufficient crew");
        }
        state = State.WAITING_CARGO_GAIN;
        return model.AbandonedStation(getPlayerByID(username));
    }

    /**
     * To be called when an enemy card is active, used to declare firepower and activate cannons. If the player wins the fight, the card is considered defeated and the player can take its rewards
     * @param username is the username of the player that wants to shoot the enemy
     * @param cannons is the list of cannons that the player wants to activate
     * @param batteries is the list of batteries that the player wants to use to activate the cannons
     * @throws IllegalStateException if the game is not in the enemy phase
     * @throws IllegalArgumentException if it is not the player's turn
     * @return 1 if the player wins the fight, 0 if the fight is a draw, -1 if the player loses the fight
     */
    public int shootEnemy(String username, List<Cannon> cannons, List<Battery> batteries){
        if (state != State.WAITING_CANNONS) {
            throw new IllegalStateException("Cannot activate cannons now");
        }
        if (!username.equals(currentPlayer)) {
            throw new IllegalArgumentException("Not your turn");
        }
        Player player = getPlayerByID(username);
        float firePower =  model.FirePower(player, new HashSet<>(cannons), batteries);

        if(firePower > ((Enemy) model.getActiveCard()).getFirePower()){
            model.getActiveCard().playCard();
            state = State.WAITING_ACCEPTANCE;
            //TODO notify player of state change
            return 1;
        } else if (firePower == ((Enemy) model.getActiveCard()).getFirePower()){
            endMove(username); //nextPlayer
            return 0;
        } else {
            if (model.getActiveCard() instanceof Pirates) {
                state = State.FIRING;
                //TODO: notify player of state change
            } else if (model.getActiveCard() instanceof Slavers) {
                state = State.WAITING_CREW;
                //TODO: notify player of state change
            } else if (model.getActiveCard() instanceof Smugglers) {
                state = State.WAITING_CARGO_LOST;
                //TODO: notify player of state change
            }
            return -1;
        }
    }

    /**
     * To be called when a smugglers card has been defeated, used to accept the smugglers' offer and take the rewards
     * @param username is the username of the player that wants to accept the smugglers' offer
     * @return the list of cargo that the player can take from the smugglers
     * @throws IllegalStateException if the game is not in the smugglers phase
     * @throws IllegalArgumentException if it is not the player's turn
     * @throws IllegalArgumentException if the smugglers card has not been defeated
     */
    public List<CargoColor> acceptSmugglers(String username) {
        if (state != State.WAITING_ACCEPTANCE && model.getActiveCard() instanceof Smugglers) {
            throw new IllegalStateException("Cannot accept smugglers now");
        }
        if (!model.getActiveCard().isPlayed()) {
            throw new IllegalStateException("Card not defeated");
        }
        state = State.WAITING_CARGO_GAIN;
        //TODO: notify player of state change
        return model.smugglersSuccess(getPlayerByID(username));
    }

    /**
     * To be called when a pirates card has been defeated, used to accept the pirates' offer and take the rewards
     * @param username is the username of the player that wants to accept the pirates' offer
     * @throws IllegalStateException if the game is not in the pirates phase
     * @throws IllegalArgumentException if it is not the player's turn
     * @throws IllegalArgumentException if the pirates card has not been defeated
     */
    public void acceptPirates(String username) {
        if (state != State.WAITING_ACCEPTANCE && model.getActiveCard() instanceof Pirates) {
            throw new IllegalStateException("Cannot accept pirates now");
        }
        if (!model.getActiveCard().isPlayed()) {
            throw new IllegalStateException("Card not defeated");
        }
        model.piratesSuccess(getPlayerByID(username));
    }

    /**
     * To be called when a slavers card has been defeated, used to accept the reward
     * @param username is the username of the player that wants
     * @throws IllegalStateException if the game is not in the slavers phase
     * @throws IllegalArgumentException if it is not the player's turn
     * @throws IllegalArgumentException if the slavers card has not been defeated
     */
    public void acceptSlavers(String username) {
        if (state != State.WAITING_ACCEPTANCE && model.getActiveCard() instanceof Slavers) {
            throw new IllegalStateException("Cannot accept slavers now");
        }
        if (!model.getActiveCard().isPlayed()) {
            throw new IllegalStateException("Card not defeated");
        }
        model.slaversSuccess(getPlayerByID(username));
    }

    /**
     * To be called when a meteor swarm card or a pirates card or a combatzone card is firing the player
     * @implSpec gets in the correct state by looking at the first projectile in the list
     */
    private void nextFire() {
        if (state != State.FIRING) {
            throw new IllegalStateException("Cannot fire outside the firing phase");
        }
        if (projectiles.isEmpty()) {
            return;
        } else if (projectiles.getFirst().getFireType() == FireType.LIGHT_METEOR) {
            state = State.WAITING_SHIELDS;
            //TODO: notify player of state change
        } else if (projectiles.getFirst().getFireType() == FireType.HEAVY_METEOR) {
            state = State.WAITING_CANNONS;
            //TODO: notify player of state change
        } else if (projectiles.getFirst().getFireType() == FireType.HEAVY_FIRE) {
            model.Fire(getPlayerByID(currentPlayer), model.getGame().rollDice(), projectiles.getFirst());
            //TODO: catch exception
        } else if (projectiles.getFirst().getFireType() == FireType.LIGHT_FIRE) {
            state = State.WAITING_SHIELDS;
            //TODO: notify player of state change
        }
    }

    /**
     * To be called when a light_fire/meteor is firing the player
     * @param username is the username of the player that wants to activate the shield
     * @param shieldComp is the shield component that the player wants to activate
     * @param batteryComp is the energy component that the player wants to use to activate the shield
     * @throws IllegalStateException if the game is not in the meteor swarm phase
     * @throws IllegalArgumentException if it is not the player's turn
     * @apiNote Ship may need to be validated
     */
    public void activateShield(String username, Shield shieldComp, Battery batteryComp) {
        if (state != State.WAITING_SHIELDS && (projectiles.getFirst().getFireType() != FireType.LIGHT_METEOR || projectiles.getFirst().getFireType() != FireType.LIGHT_FIRE)) {
            throw new IllegalStateException("Cannot activate shields now");
        }
        if (shieldComp == null) {
            try {
                model.Fire(getPlayerByID(username), model.getGame().rollDice(), projectiles.getFirst());
            } catch (InvalidShipException e) {
                state = State.VALIDATING;
                //TODO: notify player to validate ship
            }
        }
        Player player = getPlayerByID(username);
        model.UseShield(player, batteryComp);
        if (shieldComp != null && !projectiles.isEmpty() && !Arrays.stream(shieldComp.getCoveredSides()).toList().contains(projectiles.getFirst().getFireType())) {
            model.Fire(getPlayerByID(username), model.getGame().rollDice(), projectiles.getFirst());
        }
        if (projectiles.size() > 1) {
            projectiles.removeFirst();
            nextFire();
        } else {
            if (model.getActiveCard() instanceof Pirates) {
                projectiles = model.piratesFailure();
            } else if (model.getActiveCard() instanceof MeteorSwarm) {
                projectiles = ((MeteorSwarm) model.getActiveCard()).getMeteors();
            }
            endMove(username);
        }
    }

    /**
     * To be called when a heavy_meteor is firing the player
     * @param username is the username of the player that wants to activate the cannon
     * @param cannon is the cannon that the player wants to activate
     * @param battery is the energy component that the player wants to use to activate the cannon
     * @throws IllegalStateException if the game is not in the meteor swarm phase
     * @throws IllegalArgumentException if it is not the player's turn
     * @apiNote Ship may need to be validated
     */
    public void activateCannonForProjectile(String username, Cannon cannon, Battery battery) {
        if (state != State.WAITING_CANNONS && projectiles.getFirst().getFireType() != FireType.HEAVY_METEOR) {
            throw new IllegalStateException("Cannot activate cannons now");
        }
        if (!username.equals(currentPlayer)) {
            throw new IllegalArgumentException("Not your turn");
        }
        List<Cannon> valid = model.heavyMeteorCannon(getPlayerByID(username), model.getGame().rollDice(), projectiles.getFirst());
        List <Battery> batteryList = new ArrayList<>();
        batteryList.add(battery);
        model.removeEnergy(getPlayerByID(username), batteryList);
        if (!valid.contains(cannon)) {
            try {
                model.Fire(getPlayerByID(username), model.getGame().rollDice(), projectiles.getFirst());
            } catch (InvalidShipException e) {
                state = State.VALIDATING;
                // TODO: notify player to validate ship
            }
        }

        projectiles.removeFirst();
        if (projectiles.isEmpty()) {
            projectiles = ((MeteorSwarm)model.getActiveCard()).getMeteors();
            endMove(username);
        } else {
            nextFire();
        }
    }

    /**
     * To be called when a player terminates their turn. Based on the type of card, the game will move to the next player or the next phase
     * @param username is the username of the player that wants to terminate their turn
     * @throws IllegalStateException if the game is not in the card phase
     * @throws IllegalArgumentException if it is not the player's turn
     */
    public void endMove(String username) {
        if (state == State.VALIDATING || state == State.ENDGAME || state == State.ASSEMBLING || state == State.CREATING || state == State.FLIGHT) {
            throw new IllegalStateException("Cannot end move outside the card phase");
        }
        if (!username.equals(currentPlayer)) {
            throw new IllegalArgumentException("Not your turn");
        }
        // TODO select only active players (or any player and call a thread to manage the inactive ones?)
        currentPlayer = model.getGame().getPlayers().get((model.getGame().getPlayers().indexOf(getPlayerByID(currentPlayer)) + 1) % model.getGame().getPlayers().size()).getUsername();
        if (model.getActiveCard() instanceof Planets) {
            state = State.WAITING_PLANET;
        } else if (model.getActiveCard() instanceof AbandonedShip && !model.getActiveCard().isPlayed()) {
            state = State.WAITING_CREW;
        } else if (model.getActiveCard() instanceof AbandonedStation && !model.getActiveCard().isPlayed()) {
            state = State.WAITING_ACCEPTANCE;
        } else if (model.getActiveCard() instanceof CombatZone) {
            //TODO
        } else if (model.getActiveCard() instanceof MeteorSwarm) {
            state = State.FIRING;
        } else if (model.getActiveCard() instanceof Enemy && !model.getActiveCard().isPlayed()) {
            state = State.WAITING_CANNONS;
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
        if(!connectedPlayers.contains(username)) {
            throw new IllegalArgumentException("Player not found in game");
        }
        connectedPlayers.remove(username);
        disconnectedPlayers.add(username);
        if(!isGameValid()){
            pauseGame();
        }
    }

    public boolean isGameValid() {
        return connectedPlayers.size() > 1;
    }

    public void pauseGame() {
        pausedState = state;
        state = State.PAUSED;
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
        if (!disconnectedPlayers.contains(username)) {
            throw new IllegalArgumentException("Player was never part of this game");
        }
        if(connectedPlayers.size() == 1){
            state = pausedState;
        }
        // Remove player from disconnected list
        disconnectedPlayers.remove(username);
        connectedPlayers.add(username);
        return true;
    }

    public void forfeitGame(String username){
        if(!connectedPlayers.contains(username)) {
            throw new IllegalArgumentException("Player not found in game");
        }
        connectedPlayers.remove(username);
        //TODO: calculate player score?
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
