package it.polimi.ingsw.gc20.controller;

import it.polimi.ingsw.gc20.controller.event.*;
import it.polimi.ingsw.gc20.controller.states.*;
import it.polimi.ingsw.gc20.exceptions.*;
import it.polimi.ingsw.gc20.model.cards.*;
import it.polimi.ingsw.gc20.model.components.*;
import it.polimi.ingsw.gc20.model.gamesets.*;
import it.polimi.ingsw.gc20.model.player.*;
import it.polimi.ingsw.gc20.interfaces.*;
import java.security.InvalidParameterException;
import java.util.*;


/**
 * Controller class for managing the game flow and player interactions
 */
public class GameController implements GameControllerInterface {
    private final GameModel model;
    private State state;
    private final String gameID;

    private final List<String> connectedPlayers = new ArrayList<>();
    private final List<String> disconnectedPlayers = new ArrayList<>();
//    private final Map<String, Boolean> assemblingComplete = new HashMap<>();
//    private final Map<String, Boolean> readyToFly = new HashMap<>();
    private String currentPlayer;

    private final Map<EventType<? extends Event>, List<EventHandler<? extends Event>>> eventHandlers = new HashMap<>();

//    private List<CargoColor> cargoGained;
//    private int cargoLost;
//    private Map<String, Float> declaredForFight;
//    private List<Projectile> projectiles;

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
        state = new AssemblingState(model);
        currentPlayer = "";
        connectedPlayers.addAll(usernames);
        //TODO: notify players of game start
    }

    public void setState(State state) {
        this.state = state;
    }

    /**
     * @return the first player in the list of players that is online
     */
    private String getFirstOnlinePlayer() {
        return model.getInGamePlayers().stream()
                .map(Player::getUsername)
                .filter(connectedPlayers::contains)
                .findFirst()
                .orElse(null);
    }

    /**
     * @return the first player in the list of players without regarding their online status
     */
    private String getFirstPlayer() {
        return model.getInGamePlayers().getFirst().getUsername();
    }

    /**
     * @param username is the username of the player
     * @return the next player in the list of players that is online
     * @implNote do not call this method if the player is the last one in the list
     */
    private String getNextOnlinePlayer(String username) {
        if (model.getInGamePlayers().getLast().getUsername().equals(username)) {
            return null;
        }
        return model.getInGamePlayers()
                .stream()
                .map(Player::getUsername)
                .filter(connectedPlayers::contains)
                .toList()
                .get(model.getInGamePlayers().indexOf(getPlayerByID(username)) + 1);
    }

    /**
     * Handles the card drawing phase of the game
     * Processes the card based on its type
     */
    private void drawCard(){
        AdventureCard card = model.drawCard();
        if (card == null) {
            state = new EndgameState();
        } else {
            card.setState(this);
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
     * @throws IllegalStateException if the game is not in the planet phase
     * @throws InvalidTurnException if it is not the player's turn
     */
    @Override
    public void landOnPlanet(String username, int planetIndex) throws InvalidTurnException {
        if (!currentPlayer.equals(username)) {
            throw new InvalidTurnException("Not your turn");
        }
        state.landOnPlanet(username, planetIndex);
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
    @Override
    public void loadCargo(String username, CargoColor loaded, CargoHold ch) {
        if (stateEnum != StateEnum.WAITING_CARGO_GAIN) {
            throw new IllegalStateException("Cannot load cargo outside the cargo phase");
        }
        if (!username.equals(currentPlayer)) {
            throw new IllegalArgumentException("Not your turn");
        }
        Player player = getPlayerByID(username);
        if (!cargoGained.contains(loaded)) {
            throw new IllegalArgumentException("Cargo gained does not contain the loaded cargo");
        }
        cargoGained.remove(loaded);
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
    @Override
    public void unloadCargo(String username, CargoColor lost, CargoHold ch) {
        if (!username.equals(currentPlayer)) {
            throw new IllegalArgumentException("Not your turn");
        }
        if (stateEnum != StateEnum.WAITING_CARGO_GAIN && stateEnum != StateEnum.WAITING_CARGO_LOST) {
            throw new IllegalStateException("Cannot unload cargo outside the cargo phase");
        }
        Player player = getPlayerByID(username);

        if (stateEnum == StateEnum.WAITING_CARGO_LOST) {
            Map<CargoColor, Integer> playerCargo = player.getShip().getCargo();
            for (CargoColor c : playerCargo.keySet()) {
                if (c.ordinal() < lost.ordinal() && playerCargo.get(c) > 0) {
                    throw new IllegalArgumentException("Not unloading the most valuable cargo.");
                }
            }
        cargoLost--;
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
    @Override
    public void moveCargo(String username, CargoColor cargo, CargoHold from, CargoHold to) {
        if (stateEnum != StateEnum.WAITING_CARGO_GAIN && stateEnum != StateEnum.WAITING_CARGO_LOST) {
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
    private void abandonedShip(String username, List<Cabin> cabins) {
        if (stateEnum != StateEnum.WAITING_CREW && getActiveCard() instanceof AbandonedShip) {
            throw new IllegalStateException("Cannot abandon ship outside the abandoned ship card");
        }
        if (!username.equals(currentPlayer)) {
            throw new IllegalArgumentException("Not your turn");
        }
        if (model.getCrew(getPlayerByID(username)) < ((AbandonedShip)getActiveCard()).getLostCrew()) {
            throw new IllegalStateException("Cannot abandon ship with insufficient crew");
        }
        Player player = getPlayerByID(username);
        model.AbandonedShip(getPlayerByID(username), cabins);
    }

    /**
     * Activates abandoned station card, returns the list of cargo provided by the card and enters the cargo phase
     * @param username is the username of the player that wants to activate abandoned station card
     * @throws IllegalStateException if the game is not in the abandoned station phase
     * @throws IllegalArgumentException if it is not the player's turn
     * @throws IllegalArgumentException if the player does not have enough crew to invade the station
     */
    private void abandonedStation(String username) {
        AbandonedStation card = (AbandonedStation) getActiveCard();
        if (!username.equals(currentPlayer)) {
            throw new IllegalArgumentException("Not your turn");
        }
        if(stateEnum != StateEnum.WAITING_ACCEPTANCE){
            throw new IllegalStateException("Cannot land on station outside the abandoned station phase");
        } else if (getPlayerByID(username).getShip().crew() < card.getCrewNeeded()) {
            throw new IllegalStateException("Cannot invade station with insufficient crew");
        }
        stateEnum = StateEnum.WAITING_CARGO_GAIN;
        getActiveCard().playCard();
        cargoGained = model.AbandonedStation(getPlayerByID(username));
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
        if (stateEnum != StateEnum.WAITING_CANNONS) {
            throw new IllegalStateException("Cannot activate cannons now");
        }
        if (!username.equals(currentPlayer)) {
            throw new IllegalArgumentException("Not your turn");
        }
        Player player = getPlayerByID(username);
        float firePower =  model.FirePower(player, new HashSet<>(cannons), batteries);

        if(firePower > ((Enemy) getActiveCard()).getFirePower()){
            getActiveCard().playCard();
            stateEnum = StateEnum.WAITING_ACCEPTANCE;
            //TODO notify player of state change
            return 1;
        } else if (firePower == ((Enemy) getActiveCard()).getFirePower()){
            endMove(username); //nextPlayer
            return 0;
        } else {
            if (getActiveCard() instanceof Pirates) {
                stateEnum = StateEnum.FIRING;
                //TODO: notify player of state change
            } else if (getActiveCard() instanceof Slavers) {
                stateEnum = StateEnum.WAITING_CREW;
                //TODO: notify player of state change
            } else if (getActiveCard() instanceof Smugglers) {
                stateEnum = StateEnum.WAITING_CARGO_LOST;
                //TODO: notify player of state change
            }
            return -1;
        }
    }

    /**
     * @param username
     * @param cannons
     * @param batteries
     */
    @Override
    public void activateCannonsCombatZone(String username, List<Cannon> cannons, List<Battery> batteries) {
        if (stateEnum != StateEnum.WAITING_CANNONS) {
            throw new IllegalStateException("Cannot activate cannons now");
        }
        if (!username.equals(currentPlayer)) {
            throw new IllegalArgumentException("Not your turn");
        }
        declaredForFight.putIfAbsent(username, model.FirePower(getPlayerByID(username), new HashSet<>(cannons), batteries));
        if (declaredForFight.size() >= model.getInGamePlayers().stream().filter(p -> !isPlayerDisconnected(p.getUsername())).count()) {
            if (((CombatZone)getActiveCard()).combatType() == 0) {
                stateEnum = StateEnum.FIRING;
                currentPlayer = declaredForFight.entrySet().stream()
                        .min(Map.Entry.comparingByValue())
                        .map(Map.Entry::getKey)
                        .orElse(null);
                nextFire();
            } else {

            }
        }
    }

    /**
     * To be called when a smugglers card has been defeated, used to accept the smugglers' offer and take the rewards
     * @param username is the username of the player that wants to accept the smugglers' offer
     * @throws IllegalStateException if the game is not in the smugglers phase
     * @throws IllegalArgumentException if it is not the player's turn
     * @throws IllegalArgumentException if the smugglers card has not been defeated
     */
    private void acceptSmugglers(String username) {
        if (stateEnum != StateEnum.WAITING_ACCEPTANCE && getActiveCard() instanceof Smugglers) {
            throw new IllegalStateException("Cannot accept smugglers now");
        }
        if (!getActiveCard().isPlayed()) {
            throw new IllegalStateException("Card not defeated");
        }
        stateEnum = StateEnum.WAITING_CARGO_GAIN;
        //TODO: notify player of state change
        cargoGained = model.smugglersSuccess(getPlayerByID(username));
    }

    /**
     * To be called when a pirates card has been defeated, used to accept the pirates' offer and take the rewards
     * @param username is the username of the player that wants to accept the pirates' offer
     * @throws IllegalStateException if the game is not in the pirates phase
     * @throws IllegalArgumentException if it is not the player's turn
     * @throws IllegalArgumentException if the pirates card has not been defeated
     */
    private void acceptPirates(String username) {
        if (stateEnum != StateEnum.WAITING_ACCEPTANCE && getActiveCard() instanceof Pirates) {
            throw new IllegalStateException("Cannot accept pirates now");
        }
        if (!getActiveCard().isPlayed()) {
            throw new IllegalStateException("Card not defeated");
        }
        model.piratesSuccess(getPlayerByID(username));
        stateEnum = StateEnum.FLIGHT;
        drawCard();
    }

    /**
     * To be called when a slavers card has been defeated, used to accept the reward
     * @param username is the username of the player that wants
     * @throws IllegalStateException if the game is not in the slavers phase
     * @throws IllegalArgumentException if it is not the player's turn
     * @throws IllegalArgumentException if the slavers card has not been defeated
     */
    private void acceptSlavers(String username) {
        if (stateEnum != StateEnum.WAITING_ACCEPTANCE && getActiveCard() instanceof Slavers) {
            throw new IllegalStateException("Cannot accept slavers now");
        }
        if (!getActiveCard().isPlayed()) {
            throw new IllegalStateException("Card not defeated");
        }
        model.slaversSuccess(getPlayerByID(username));
        stateEnum = StateEnum.FLIGHT;
        drawCard();
    }

    public void acceptCard(String username) {
        if (stateEnum != StateEnum.WAITING_ACCEPTANCE) {
            throw new IllegalStateException("Cannot accept card now");
        }
        if (!username.equals(currentPlayer)) {
            throw new IllegalArgumentException("Not your turn");
        }
        if (getActiveCard() instanceof AbandonedStation) {
            abandonedStation(username);
        } else if (getActiveCard() instanceof Smugglers) {
            acceptSmugglers(username);
        } else if (getActiveCard() instanceof Pirates) {
            acceptPirates(username);
        } else if (getActiveCard() instanceof Slavers) {
            acceptSlavers(username);
        }
    }

    /**
     * @param username
     * @param cabins
     */
    @Override
    public void loseCrew(String username, List<Cabin> cabins) {
        if (stateEnum != StateEnum.WAITING_CREW) {
            throw new IllegalStateException("Cannot lose crew now");
        }
        if (!username.equals(currentPlayer)) {
            throw new IllegalArgumentException("Not your turn");
        }
        if (getActiveCard() instanceof AbandonedShip) {
            abandonedShip(username, cabins);
            stateEnum = StateEnum.FLIGHT;
            drawCard();
        } else if (getActiveCard() instanceof Slavers) {
            model.slaversFailure(getPlayerByID(username), cabins);
            stateEnum = StateEnum.FLIGHT;
            drawCard();
        } else if (getActiveCard() instanceof CombatZone) {
            model.CombatZoneLostCrew(getPlayerByID(username), cabins);
            stateEnum = StateEnum.WAITING_CANNONS;
            currentPlayer = getFirstOnlinePlayer();
            declaredForFight = new HashMap<>();
            // TODO: notify player of state change
        }
    }

    /**
     * To be called when a meteor swarm card or a pirates card or a combatzone card is firing the player
     * @implSpec gets in the correct state by looking at the first projectile in the list
     */
    private void nextFire() {
        if (stateEnum != StateEnum.FIRING) {
            throw new IllegalStateException("Cannot fire outside the firing phase");
        }
        if (projectiles.isEmpty()) {
            return;
        } else if (projectiles.getFirst().getFireType() == FireType.LIGHT_METEOR) {
            stateEnum = StateEnum.WAITING_SHIELDS;
            //TODO: notify player of state change
        } else if (projectiles.getFirst().getFireType() == FireType.HEAVY_METEOR) {
            stateEnum = StateEnum.WAITING_CANNONS;
            //TODO: notify player of state change
        } else if (projectiles.getFirst().getFireType() == FireType.HEAVY_FIRE) {
            try {
                model.Fire(getPlayerByID(currentPlayer), model.getGame().rollDice(), projectiles.getFirst());
            } catch (InvalidShipException e) {
                stateEnum = StateEnum.VALIDATING;
                //TODO: notify player to validate ship
            }
        } else if (projectiles.getFirst().getFireType() == FireType.LIGHT_FIRE) {
            stateEnum = StateEnum.WAITING_SHIELDS;
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
        if (stateEnum != StateEnum.WAITING_SHIELDS && (projectiles.getFirst().getFireType() != FireType.LIGHT_METEOR || projectiles.getFirst().getFireType() != FireType.LIGHT_FIRE)) {
            throw new IllegalStateException("Cannot activate shields now");
        }
        if (shieldComp == null) {
            try {
                model.Fire(getPlayerByID(username), model.getGame().rollDice(), projectiles.getFirst());
            } catch (InvalidShipException e) {
                stateEnum = StateEnum.VALIDATING;
                //TODO: notify player to validate ship
            }
        }
        Player player = getPlayerByID(username);
        model.UseShield(player, batteryComp);
        if (shieldComp != null && !projectiles.isEmpty() && !Arrays.stream(shieldComp.getCoveredSides()).toList().contains(projectiles.getFirst().getFireType())) {
            try {
                model.Fire(getPlayerByID(username), model.getGame().rollDice(), projectiles.getFirst());
            } catch (InvalidShipException e) {
                stateEnum = StateEnum.VALIDATING;
                //TODO: notify player to validate ship
            }
        }
        if (projectiles.size() > 1) {
            projectiles.removeFirst();
            stateEnum = StateEnum.FIRING;
            nextFire();
        } else {
            if (getActiveCard() instanceof Pirates) {
                projectiles = model.piratesFailure();
            } else if (getActiveCard() instanceof MeteorSwarm) {
                projectiles = ((MeteorSwarm) getActiveCard()).getMeteors();
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
    @Override
    public void activateCannonForProjectile(String username, Cannon cannon, Battery battery) {
        if (stateEnum != StateEnum.WAITING_CANNONS && projectiles.getFirst().getFireType() != FireType.HEAVY_METEOR) {
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
                stateEnum = StateEnum.VALIDATING;
                // TODO: notify player to validate ship
            }
        }

        projectiles.removeFirst();
        if (projectiles.isEmpty()) {
            projectiles = ((MeteorSwarm)getActiveCard()).getMeteors();
            endMove(username);
        } else {
            stateEnum = StateEnum.FIRING;
            nextFire();
        }
    }

    /**
     * Returns the active adventure card
     * @return the active adventure card
     */
    @Override
    public AdventureCard getActiveCard() {
        return model.getActiveCard();
    }

    /**
     * To be called when a player terminates their turn. Based on the type of card, the game will move to the next player or the next phase
     * @param username is the username of the player that wants to terminate their turn
     * @throws IllegalStateException if the game is not in the card phase
     * @throws IllegalArgumentException if it is not the player's turn
     */
    public void endMove(String username) {
        if (stateEnum == StateEnum.VALIDATING || stateEnum == StateEnum.ENDGAME || stateEnum == StateEnum.ASSEMBLING || stateEnum == StateEnum.CREATING || stateEnum == StateEnum.FLIGHT) {
            throw new IllegalStateException("Cannot end move outside the card phase");
        }
        if (!username.equals(currentPlayer)) {
            throw new IllegalArgumentException("Not your turn");
        }
        if (getActiveCard().isPlayed()) {
            drawCard();
            return;
        }
        // TODO select only active players (or any player and call a thread to manage the inactive ones?)
        currentPlayer = model.getGame().getPlayers().get((model.getGame().getPlayers().indexOf(getPlayerByID(currentPlayer)) + 1) % model.getGame().getPlayers().size()).getUsername();
        if (getActiveCard() instanceof Planets) {
            stateEnum = StateEnum.WAITING_PLANET;
        } else if (getActiveCard() instanceof AbandonedShip) {
            stateEnum = StateEnum.WAITING_CREW;
            currentPlayer = getNextOnlinePlayer(currentPlayer);
            if (currentPlayer == null) {
                drawCard();
                return;
            }
        } else if (getActiveCard() instanceof AbandonedStation) {
            stateEnum = StateEnum.WAITING_ACCEPTANCE;
            currentPlayer = getNextOnlinePlayer(currentPlayer);
            if (currentPlayer == null) {
                drawCard();
                return;
            }
        } else if (getActiveCard() instanceof CombatZone) {
            //TODO
        } else if (getActiveCard() instanceof MeteorSwarm) {
            stateEnum = StateEnum.FIRING;
        } else if (getActiveCard() instanceof Enemy && !getActiveCard().isPlayed()) {
            stateEnum = StateEnum.WAITING_CANNONS;
        }




        if (Objects.equals(currentPlayer, model.getGame().getPlayers().getFirst().getUsername())) { //last player played their turn
            if (getActiveCard() instanceof Planets) {
                model.movePlayerReverse();
            }
            stateEnum = StateEnum.FLIGHT;
            drawCard();
        }
    }

    /**
     * @param username
     * @param engines
     * @param batteries
     */
    @Override
    public void activateEngines(String username, List<Engine> engines, List<Battery> batteries) {
        //TODO
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

    /**
     * @param asker is the username of the player asking for data
     * @param asked is the username of the player being asked for data
     * @return player data, if the asker is the same as the asked, return the full data, otherwise return only public data
     */
    @Override
    public Player getPlayerData(String asker, String asked) {
        if (asker.equals(asked)) {
            return getPlayerByID(asker);
        } else {
            return getPlayerByID(asked).getPublicData();
        }
    }

    //TODO: if player color is chosen at connection time, this method is fine, if not, it should be called within validating state, maybe editing #readyToFly function, providing the color chosen
    /**
     * Sets the color for a specific player
     *
     * @param username Username of the player to update
     * @param color Color to assign to the player
     * @throws IllegalArgumentException if player is not found or color is already taken
     */
    @Override
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

    /**
     * Calculates and returns the final scores for all players
     *
     * @return Map associating each player with their score
     */
    public Map<Player, Integer> getPlayerScores(){
        return model.calculateScore();
    }
    //TODO: maybe this method should return a Map <String, Integer> instead of a Map<Player, Integer> to avoid exposing the Player object
    //TODO: maybe make this function only available in the endgame state?

    /**
     * Handles the event of a player giving up
     * @param username is the username of the player that wants to give up
     */
    @Override
    public void giveUp(String username) {
        model.giveUp(getPlayerByID(username));
    }

    /**
     * Handles a player leaving the game mid-session
     *
     * @param username Username of the exiting player
     * @throws IllegalArgumentException if player was not in game or not connected
     */
    public void disconnectPlayer(String username) {
        // Find player with matching username
        if (connectedPlayers.contains(username)) {
            connectedPlayers.remove(username);
            disconnectedPlayers.add(username);
        } else {
            throw new IllegalArgumentException("Player not found in game, not connected or never joined");
        }
        // Player remains in the game model
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
        if (getPlayerByID(username) != null) {
            throw new IllegalArgumentException("Player was never part of this game");
        }

        // Check if player is in the disconnected list
        if (!isPlayerDisconnected(username)) {
            return false; // Player isn't disconnected, nothing to do
        }

        // Remove player from disconnected list
        disconnectedPlayers.remove(username);
        connectedPlayers.add(username);

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
        return model.getGame().getPlayers()
                .stream()
                .filter(player -> player.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));
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
     * Takes a component from the unviewed pile
     *
     * @param username Username of the player taking the component
     * @param component Component to take from the unviewed pile
     * @return The taken component
     * @throws IllegalStateException if game is not in ASSEMBLING state
     * @throws NoSuchElementException if the component is not in the unviewed pile
     */
    public synchronized Component takeComponentFromUnviewed(String username, Component component) {
        return state.takeComponentFromUnviewed(getPlayerByID(username), component);
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
        return state.takeComponentFromViewed(getPlayerByID(username), component);
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
        if (model.getLevel() != 2) {
            throw new IllegalStateException("Booking components is only available in level 2 games");
        }
        return state.takeComponentFromBooked(getPlayerByID(username), component);
    }

    /**
     * Adds a component to player's booked list (Level 2 only)
     *
     * @param username Username of the player booking the component
     * @param component Component to book
     * @throws IllegalStateException if game is not in ASSEMBLING state or not level 2
     * @throws IllegalArgumentException if there aren't enough available spaces in the player's booked list
     */
    public void addComponentToBooked(String username, Component component) {
        if (model.getLevel() != 2) {
            throw new IllegalStateException("Booking components is only available in level 2 games");
        }
        state.addComponentToBooked(getPlayerByID(username), component);
    }

    /**
     * Adds a component to the viewed pile so other players can see it
     *
     * @param component Component to add to viewed pile
     * @throws IllegalStateException if game is not in ASSEMBLING state
     */
    public synchronized void addComponentToViewed(Component component) {
        state.addComponentToViewed(component);
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
        state.placeComponent(getPlayerByID(username), component, x, y);
    }

    /**
     * Rotates a component clockwise
     *
     * @param component Component to rotate
     * @throws IllegalStateException if game is not in ASSEMBLING state
     */
    public void rotateComponentClockwise(Component component) {
        state.rotateComponentClockwise(component);
    }

    /**
     * Rotates a component counterclockwise
     *
     * @param component Component to rotate
     * @throws IllegalStateException if game is not in ASSEMBLING state
     */
    public void rotateComponentCounterclockwise(Component component) {
        state.rotateComponentCounterclockwise(component);
    }

    /**
     * Removes a component from the player's ship (only during validating phase and if the ship is not valid)
     *
     * @param username Username of the player removing the component
     * @param component Component to remove
     * @throws IllegalStateException if game is not in VALIDATING state
     * @apiNote view must call this function until the ship is valid
     * @see #validateShip(String username)
     */
    public void removeComponentFromShip(String username, Component component) {
        state.removeComp(getPlayerByID(username), component);
    }

    /**
     * Validates the player's ship
     *
     * @param username Username of the player that wants to validate the ship
     * @return true if ship is valid, false otherwise
     * @throws IllegalStateException if game is not in VALIDATING state
     * @apiNote this function must be called in a loop from the view until the ship is valid
     * @see #removeComponentFromShip(String username, Component component)
     * @implNote this function will also draw a card if all players have valid ships (and will change the state)
     */
    public boolean validateShip(String username) {
        if (!state.isShipValid(getPlayerByID(username))) {
            return false;
        } else {
            if (state.allShipsReady()) {
                drawCard();
                //TODO: notify players of state change
            }
            return true;
        }
    }

    /**
     * Stops the assembling phase for a player
     * @param username is the username of the player that wants to stop assembling
     * @param position is the relative position on board where the player wants to put their rocket
     * @throws IllegalStateException if game is not in ASSEMBLING state
     * @implNote performs state change to VALIDATING when all players have completed assembling
     */
    public void stopAssembling(String username, int position) {
        state.stopAssembling(getPlayerByID(username), position);
        if (state.allAssembled()) {
            state = new ValidatingShipState(model);
            //TODO: notify players of state change
        }
    }

    /**
     * Turns the hourglass for a player
     *
     * @param username Username of the player turning the hourglass
     * @throws IllegalStateException if game is not in ASSEMBLING state or not level 2
     * @throws IllegalArgumentException if the player is not connected
     * @throws HourglassException if the hourglass time is not 0
     * @throws HourglassException if the player has not completed assembling yet when turning last time
     */
    public void turnHourglass(String username) throws HourglassException {
        if (isPlayerDisconnected(username)) {
            throw new IllegalArgumentException("Cannot turn hourglass for not connected player");
        }
        if (model.getLevel() != 2) {
            throw new IllegalStateException("Hourglass is only available in level 2 games");
        }
        state.turnHourglass(getPlayerByID(username));
    }

    /**
     * Gets the remaining time for the hourglass
     *
     * @param username Username of the player checking the hourglass
     * @return Remaining time in seconds
     * @throws IllegalStateException if game is not in ASSEMBLING state
     * @throws IllegalArgumentException if the game is not in level 2 or the player is disconnected
     */
    public int getHourglassTime(String username) {
        if (model.getLevel() != 2) {
            throw new IllegalStateException("Hourglass is only available in level 2 games");
        }
        if (isPlayerDisconnected(username)) {
            throw new IllegalArgumentException("Player disconnected");
        }
        return state.getHourglassTime(getPlayerByID(username));
    }

    /**
     * Peeks at the selected deck
     *
     * @param username Username of the player peeking at the deck
     * @param num number of the deck to peek at
     * @return List of AdventureCard from the selected deck
     * @throws IllegalStateException if game is not in ASSEMBLING state
     * @throws IllegalArgumentException if player is not connected or has already completed assembling
     * @throws IllegalArgumentException if the game is not in level 2
     */
    public List<AdventureCard> peekDeck(String username, int num){
        if (model.getLevel() != 2) {
            throw new IllegalStateException("Decks are only available in level 2 games");
        }
        if (isPlayerDisconnected(username)) {
            throw new IllegalArgumentException("Cannot view deck for not connected player");
        }
        // TODO: synchronize this method on the selected deck?
        return state.peekDeck(getPlayerByID(username), num);
    }

    /**
     * Adds an alien to the player's ship
     *
     * @param username Username of the player adding the alien
     * @param color Color of the alien
     * @param cabin Cabin where the alien will be placed
     * @throws IllegalStateException if game is not in VALIDATING state
     * @throws IllegalArgumentException if ship is invalid or component is not a cabin
     * @throws IllegalArgumentException if the game is not in level 2
     * @throws IllegalArgumentException if the cabin provided is a StartingCabin
     */
    public void addAlien(String username, AlienColor color, Cabin cabin) {
                if(model.getLevel() != 2){
            throw new IllegalArgumentException("Aliens are only available in level 2 games");
        }
        state.addAlien(getPlayerByID(username), color, cabin);
    }

    /**
     * Sets the player to be ready to fly
     * @param username is the username of the player that wants to fly
     * @throws IllegalStateException if game is not in VALIDATING state
     * @throws IllegalArgumentException if the player's ship is not valid
     * @apiNote be careful to add aliens before calling this function
     */
    public void readyToFly(String username) {
        state.readyToFly(getPlayerByID(username));
    }
}