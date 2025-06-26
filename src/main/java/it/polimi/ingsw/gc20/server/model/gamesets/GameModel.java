package it.polimi.ingsw.gc20.server.model.gamesets;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import it.polimi.ingsw.gc20.server.exceptions.*;
import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.server.model.cards.FireType;
import it.polimi.ingsw.gc20.server.model.cards.Projectile;
import it.polimi.ingsw.gc20.server.model.components.*;
import it.polimi.ingsw.gc20.server.model.player.Player;
import it.polimi.ingsw.gc20.server.model.player.PlayerColor;
import it.polimi.ingsw.gc20.server.model.ship.LearnerShip;
import it.polimi.ingsw.gc20.server.model.ship.NormalShip;
import it.polimi.ingsw.gc20.server.model.ship.Ship;


import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The GameModel class provides the structure and logic required to manage the core gameplay system.
 * It contains methods to set up the game, manage game components, players, and perform various actions.
 * This class manages the interactions and functionalities related to the game state, components, and players.
 */
public class GameModel {
    private Game game;
    private AdventureCard activeCard;
    private int level;
    /**
     * Constructs a new instance of the GameModel class.
     * Initializes the game model with default values:
     * - The game state is set to null.
     * - The active card is set to null.
     * - The initial level is set to 1.
     */
    public GameModel() {
        this.game = null;
        this.activeCard = null;
        this.level = 1;
    }

    /**
     * Sets the level to the specified value.
     *
     * @param level the new level to set
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Retrieves the current level.
     *
     * @return the current level as an integer
     */
    public int getLevel() {
        return this.level;
    }

    /**
     * Sets the game object to the specified value.
     *
     * @param game the Game object to be assigned
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * Retrieves the current instance of the game.
     *
     * @return the current Game instance associated with the class.
     */
    public Game getGame() {
        return this.game;
    }

    /**
     * Sets the active card for the adventure game.
     *
     * @param activeCard the AdventureCard to set as the active card
     */
    public void setActiveCard(AdventureCard activeCard) {
        this.activeCard = activeCard;
    }

    /**
     * Retrieves the currently active AdventureCard.
     *
     * @return the active AdventureCard, or null if no card is currently active.
     */
    public AdventureCard getActiveCard() {
        return this.activeCard;
    }


    /**
     * Initializes a Player object with the provided username, index, and other attributes.
     *
     * @param username the name to be assigned to the player
     * @param index the index used to determine the player's color and attributes
     * @return a fully initialized Player object
     */
    private Player initPlayer (String username, int index){
        Player player = new Player();
        player.setUsername(username);
        player.setGameStatus(true);
        player.setColor(PlayerColor.values()[index]);
        player.setPosition(-1);
        if (level == 2){
            player.setShip(new NormalShip(player.getColor()));
        }else{
            player.setShip(new LearnerShip(player.getColor()));
        }
        return player;
    }

    /**
     * Initializes and starts a new game session based on the specified parameters.
     *
     * @param level the difficulty level of the game; determines the type of board to create
     * @param usernames a list of usernames representing the players participating in the game
     * @param gameID the unique identifier for the game session
     */
    public void startGame(int level, List<String> usernames, String gameID) {
        Game game = new Game();
        game.setID(gameID);
        Pile pile = new Pile();
        Board board;
        setLevel(level);
        //creating the board based on the level
        if (level == 2) {
            board = new NormalBoard();
            board.createDeck();
            board.mergeDecks();
        } else {
            board = new LearnerBoard();
            board.createDeck();
        }
        game.addBoard(board);
        //creating the players and initializing the player
        for (int i = 0; i < usernames.size(); i++) {
            Player player = initPlayer(usernames.get(i), i);
            board.addPlayer(player);
            game.addPlayer(player);
        }
        game.addBoard(board);

        List<Component> allComponents = new ArrayList<>();
        ObjectMapper mapper = JsonMapper.builder()
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .build();

        try {
            allComponents = Arrays.asList(mapper.readValue(getClass().getResourceAsStream("/components.json"), Component[].class));
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "error while trying to read Component.json", e);
        }

        pile.addUnviewed(allComponents);
        game.setPile(pile);

        this.setGame(game);
    }

    /**
     * Processes the given component by removing it from the unviewed pile.
     *
     * @param c the component to be removed from the unviewed pile
     * @throws ComponentNotFoundException if the specified component does not exist in the unviewed pile
     */
    public void componentFromUnviewed(Component c) throws ComponentNotFoundException {
        game.getPile().removeUnviewed(c);
    }

    /**
     * Removes the specified component from the viewed pile in the game.
     *
     * @param c the component to be removed from the viewed pile
     * @throws ComponentNotFoundException if the specified component is not found in the viewed pile
     */
    public void componentFromViewed(Component c) throws ComponentNotFoundException {
        game.getPile().removeViewed(c);
    }

    /**
     * Removes a booked component from the player's booked list.
     *
     * @param c The component to be removed from the booked list of the ship.
     * @param p The player owning the ship from which the component will be removed.
     * @throws ComponentNotFoundException If the specified component is not found in the booked list.
     */
    public void componentFromBooked(Component c, Player p) throws ComponentNotFoundException {
        Ship s = p.getShip();
        ((NormalShip) s).removeBooked(c);
    }

    /**
     * Retrieves a view of the specified adventure card deck without modifying it.
     *
     * @param numDeck the index of the deck to view, where each deck is identified by a specific number
     * @return a list of AdventureCard objects representing the cards in the specified deck
     * @throws InvalidIndexException if the specified deck index is invalid
     */
    public List<AdventureCard> viewDeck(int numDeck) throws InvalidIndexException {
        Board b = game.getBoard();
        return ((NormalBoard) b).peekDeck(numDeck);
    }

    /**
     * Rotates the given component 90 degrees in the clockwise direction.
     *
     * @param c the component to be rotated
     */
    public void RotateClockwise(Component c) {
        c.rotateClockwise();
    }

    /**
     * Rotates the given component counterclockwise.
     *
     * @param c the component to be rotated counterclockwise
     */
    public void RotateCounterclockwise(Component c) {
        c.rotateCounterclockwise();
    }

    /**
     * Books a given component for a player's ship by adding it to the list of booked components.
     *
     * @param c the component to be booked
     * @param p the player whose ship the component is being booked for
     * @throws NoSpaceException if there is not enough space on the ship to book the component
     */
    public void componentToBooked(Component c, Player p) throws NoSpaceException {
        Ship s = p.getShip();
        ((NormalShip) s).addBooked(c);
    }

    /**
     * Adds the specified component to the "viewed" state of the pile in the game.
     *
     * @param c the component to be added to the viewed state
     * @throws DuplicateComponentException if the specified component already exists in the viewed state
     */
    public void componentToViewed(Component c) throws DuplicateComponentException {
        game.getPile().addViewed(c);
    }

    /**
     * Adds a component to the specified ship at the given coordinates.
     *
     * @param c the component to be added to the ship
     * @param p the player whose ship will receive the component
     * @param x the x-coordinate where the component will be placed
     * @param y the y-coordinate where the component will be placed
     * @throws InvalidTileException if the specified tile is invalid for placement
     */
    public void addToShip(Component c, Player p, int x, int y) throws InvalidTileException {
        Ship s = p.getShip();
        s.addComponent(c, x, y);
    }

    /**
     * Stops the assembling process for a player and updates their position on the board.
     * If the specified position is already occupied, an exception is thrown.
     * Handles different board configurations based on the game level.
     *
     * @param p the player whose assembling process is being stopped
     * @param position the position on the board to place the player, mapped according to game level
     * @throws InvalidIndexException if the specified position is already occupied
     */
    public void stopAssembling(Player p, int position) throws InvalidIndexException {
        game.getBoard().removePlayer(p);
        int realPosition = getRealPosition(position);
        if (game.isOccupied(realPosition)) {
            throw new InvalidIndexException("Position already occupied");
        } else {
            for (Player player : game.getPlayers()) {
                if (player == p) {
                    if (position == 1) {
                        player.setLeader();
                    }
                    player.setPosition(realPosition);
                }
            }
        }

    }

    /**
     * Calculates the real position based on the specified position and the current level.
     *
     * @param position input position, expected values are 1, 2, 3, or 4
     * @return the real position as an integer, or -1 if the input position is invalid
     */
    private int getRealPosition(int position) {
        int realPosition = -1;
        if (level==2){
            switch (position){
                case 1 -> realPosition = 6;
                case 2 -> realPosition = 3;
                case 3 -> realPosition = 1;
                case 4 -> realPosition = 0;
            }
        }else{
            switch (position){
                case 1 -> realPosition = 4;
                case 2 -> realPosition = 2;
                case 3 -> realPosition = 1;
                case 4 -> realPosition = 0;
            }
        }
        return realPosition;
    }

    /**
     * Validates whether the ship associated with the given player is valid.
     *
     * @param p the player whose ship is to be validated
     * @return true if the player's ship is valid, false otherwise
     */
    public boolean shipValidating(Player p) {
        Ship s = p.getShip();
        return s.isValid();
    }

    /**
     * Removes a component from the player's ship at the specified coordinates.
     *
     * @param x the x-coordinate of the component to be removed
     * @param y the y-coordinate of the component to be removed
     * @param p the player whose ship contains the component
     * @throws ComponentNotFoundException if no component is found at the specified coordinates
     */
    public void removeComponent(Integer x, Integer y, Player p) throws ComponentNotFoundException {
        Ship s = p.getShip();
        Component c = s.getComponentAt(x, y);
        s.killComponent(c);
    }

    /**
     * Assigns an alien to a specified cabin in the player's ship.
     *
     * @param a The alien to be placed in the cabin.
     * @param c The cabin where the alien will be placed.
     * @param p The player whose ship the alien will be placed in.
     * @throws InvalidAlienPlacement If the alien cannot be placed in the given cabin.
     */
    public void setAlien(AlienColor a, Cabin c, Player p) throws InvalidAlienPlacement {
        Ship s = p.getShip();
        ((NormalShip) s).addAlien(a, c);
    }

    /**
     * Adds pieces to the player's ship by initializing astronauts and
     * transferring booked items to waste.
     *
     * @param p the player whose ship will have pieces added
     */
    public void addPieces(Player p) {
        Ship s = p.getShip();
        p.getShip().initAstronauts();
        if (level==2) {
            s.addBookedToWaste();
        }
    }

    /**
     * Creates a deck by merging existing decks on the game board.
     * This method retrieves the board from the current game instance
     * and merges its decks into a single deck.
     */
    public void createDeck() {
        Board b = game.getBoard();
        b.mergeDecks();
    }

    /**
     * Draws a card from the game's board deck. If the drawn card is named
     * "CombatZone" and there is only one player in the game, the method
     * continues drawing until a valid card is retrieved. The drawn card
     * is then set as the active card.
     *
     * @return the drawn AdventureCard object
     * @throws EmptyDeckException if there are no cards left in the deck
     */
    public AdventureCard drawCard() throws EmptyDeckException{
        AdventureCard card = game.getBoard().drawCard();
        while (card.getName().equals("CombatZone") && getInGamePlayers().size() == 1){
            card = game.getBoard().drawCard();
        }
        this.setActiveCard(card);
        return card;
    }


    /**
     * Removes crew members from the specified cabins of the player's ship.
     *
     * @param p the player whose ship's crew is to be modified
     * @param l the list of cabins from which crew members will be removed
     * @throws EmptyCabinException if a cabin in the list is already empty
     */
    public void loseCrew (Player p, List<Cabin> l) throws EmptyCabinException {
        for (Cabin cabin : l) {
            p.getShip().unloadCrew(cabin);
        }
    }

    /**
     * Moves the specified player by a given number of positions in the game.
     *
     * @param p the player to be moved
     * @param num the number of positions to move the player
     */
    public void movePlayer (Player p, int num){
        game.move(p, num);
    }

    /**
     * Adds the specified number of credits to the given player's account.
     *
     * @param p the player object to which the credits will be added
     * @param credits number of credits to be added to the player's account
     */
    public void addCredits (Player p, int credits){
        p.addCredits(credits);
    }

    /**
     * Retrieves the number of crew members on the player's ship.
     *
     * @param p the player whose ship's crew count is to be retrieved
     * @return the number of crew members on the player's ship
     */
    public int getCrew (Player p) {
        return p.getShip().crew();
    }

    /**
     * Calculates and applies the firepower of a player's ship using the specified set of cannons
     * and available energy from the list of batteries. The method ensures there is enough energy before
     * firing and deducts the energy after the operation.
     *
     * @param p the player whose ship will fire the cannons
     * @param cannons the set of cannons to be fired
     * @param energy the list of batteries providing the necessary energy
     * @return the calculated firepower of the cannons being fired
     * @throws EnergyException if there is insufficient energy in any battery
     * @throws InvalidCannonException if an invalid cannon is encountered during the operation
     */
    public float firePower(Player p, Set<Cannon> cannons, List<Battery> energy) throws EnergyException, InvalidCannonException {
        float power;
        for (Battery e : energy) {
            if (e.getAvailableEnergy() == 0) {
                throw new EnergyException("Not enough energy");
            }
        }
        power = p.getShip().firePower(cannons, energy.size());

        for (Battery e : energy) {
            p.getShip().useEnergy(e);
        }
        return power;
    }

    /**
     * Calculates the engine power based on the player's ship and the available energy from batteries.
     * This method also accounts for the use of double engines and checks for sufficient energy supply.
     *
     * @param p the player whose ship's engine power is being calculated
     * @param doubleEngines the number of double engines to be activated
     * @param energy the list of batteries supplying energy to the ship
     * @return the calculated engine power based on the specified parameters
     * @throws EnergyException if there is not enough energy available in the batteries
     * @throws InvalidEngineException if an invalid engine configuration is encountered
     * @throws IllegalArgumentException if the provided energy supply is not enough to activate the engines
     */
    public int enginePower(Player p, int doubleEngines, List<Battery> energy) throws EnergyException, InvalidEngineException {
        int power;
        if (energy == null && doubleEngines > 0) {
            throw new EnergyException("Not enough energy");
        }
        if (energy == null){
            return p.getShip().enginePower(0);
        }
        for (Battery e : energy) {
            if (e.getAvailableEnergy() == 0) {
                throw new EnergyException("Not enough energy");
            }
        }
        if (doubleEngines <= energy.size() && energy.size() <= p.getShip().getTotalEnergy()) {
            power = p.getShip().enginePower(doubleEngines);
            for (Battery e : energy) {
                p.getShip().useEnergy(e);
            }
        } else {
            throw new IllegalArgumentException("Not enough energy");
        }
        return power;
    }

    /**
     * Moves cargo of a specified color from one cargo hold to another.
     * If the destination cargo hold is null, the cargo is unloaded from the source cargo hold.
     * Ensures that the destination cargo hold has available slots before loading the cargo.
     *
     * @param p The player whose ship is performing the cargo operations.
     * @param c The color of the cargo to be moved.
     * @param from The source cargo hold from which the cargo is removed.
     * @param to The destination cargo hold to which the cargo is added. If null, the cargo will simply be unloaded.
     * @throws InvalidCargoException If the cargo cannot be unloaded or loaded due to invalid settings or conditions.
     * @throws CargoNotLoadable If the cargo cannot be loaded into the destination cargo hold.
     * @throws CargoFullException If the destination cargo hold does not have any available slots.
     */
    public void moveCargo(Player p, CargoColor c, CargoHold from, CargoHold to) throws InvalidCargoException, CargoNotLoadable, CargoFullException {
        if (to == null){
            p.getShip().unloadCargo(c, from);
            return;
        }
        if (to.getAvailableSlots()==0){
            throw new CargoFullException("CargoHold has not available slots");
        }
        p.getShip().unloadCargo(c, from);
        p.getShip().loadCargo(c, to);
    }

    /**
     * Adds cargo to a player's ship.
     *
     * @param p the player whose ship will load the cargo
     * @param c the color of the cargo to be loaded
     * @param ch the cargo hold where the cargo will be placed
     * @throws CargoNotLoadable if the cargo cannot be loaded
     * @throws CargoFullException if the cargo hold is full
     */
    public void addCargo(Player p, CargoColor c, CargoHold ch)  throws CargoNotLoadable, CargoFullException {
        p.getShip().loadCargo(c, ch);
    }

    /**
     * Activates the shield of the player's ship by using energy from the provided battery.
     *
     * @param p the Player whose ship's shield will be activated
     * @param e the Battery providing the energy required to activate the shield
     * @throws EnergyException if there is an issue with energy usage, or if there
     *                         is not enough energy to activate the shield
     */
    public void useShield(Player p, Battery e) throws EnergyException {
        p.getShip().useEnergy(e);
    }

    /**
     * Calculates and returns the score of players based on their position, cargo, waste, and other game factors.
     * The method assigns points depending on the level of the game, the player's cargo value, and penalties for waste.
     * Additional bonus points are awarded to players with the least exposed parts of the ship.
     *
     * @return a map where each key is a Player and the value is the calculated score for that player.
     */
    public Map<Player, Integer> calculateScore() {
        Map<Player, Integer> score = new HashMap<>();
        for (Player p : game.getPlayers()) {
            score.put(p, 0);
        }
        game.sortPlayerByPosition();
        int min = 0;
        int waste;
        int points=4;
        for (Player p : game.getPlayers()) {
            if (p.isInGame()) {
                if (min > p.getShip().getAllExposed() || min == 0) {
                    min = p.getShip().getAllExposed();
                }
                if (level == 2) {
                    score.put(p, points * 2);
                } else {
                    score.put(p, points);
                }
                points --;
                for (Map.Entry<CargoColor, Integer> e : p.getShip().getCargo().entrySet()) {
                    CargoColor color = e.getKey();
                    int quantity = e.getValue();
                    score.put (p, score.get(p) + color.value()*quantity);
                }
            } else {
                for (Map.Entry<CargoColor, Integer> e : p.getShip().getCargo().entrySet()) {
                    CargoColor color = e.getKey();
                    int quantity = e.getValue();
                    score.put (p, score.get(p) + color.value()*quantity);
                }
                score.put(p, (score.get(p)+1)/2);
            }
            waste = p.getShip().getWaste().size();

            score.put(p, score.get(p) - waste);
        }
        for (Player g : game.getPlayers()) {
            if (g.getShip().getAllExposed() == min && g.isInGame()) {
                if (level == 2) {
                    score.put(g, score.get(g) + 4);
                } else {
                    score.put(g, score.get(g) + 2);
                }
            }
        }
        return score;
    }

    /**
     * Retrieves the number of astronauts currently on the player's ship.
     *
     * @param p the player whose ship's astronaut count is to be retrieved
     * @return the number of astronauts on the player's ship
     */
    public int getAstronauts(Player p) {
        return p.getShip().getAstronauts();
    }

    /**
     * Fires a projectile at a specific component of the player's ship based on the direction and type of the projectile.
     * Adjusts behavior based on the type of fire, direction, and the level of the game.
     *
     * @param p the player whose ship is being targeted
     * @param diceResult the result of the dice roll determining the position on the ship
     * @param fire the projectile that is being fired, including its type and direction
     * @throws InvalidShipException if the ship state is invalid for the operation
     */
    public void fire(Player p, int diceResult, Projectile fire) throws InvalidShipException {
        Component c;
        if (fire.getFireType() == FireType.LIGHT_METEOR) {
            if (fire.getDirection() == Direction.UP || fire.getDirection() == Direction.DOWN) {
                if (level == 2) {
                    c = p.getShip().getFirstComponent(fire.getDirection(), diceResult-4);
                } else {
                    c = p.getShip().getFirstComponent(fire.getDirection(), diceResult-5);
                }
            } else {
                c = p.getShip().getFirstComponent(fire.getDirection(), diceResult-5);
            }


            if (c==null) {
                return;
            }
            if (c.getConnectors().get(fire.getDirection()) == ConnectorEnum.ZERO) {
                return;
            }
        }
        fire.Fire(p.getShip(), diceResult);
    }

    /**
     * Determines the list of heavy-meteor-defeating cannons to be fired based on the player's current ship,
     * the dice result, and the projectile's direction.
     *
     * @param p the player whose ship's cannons will be used
     * @param diceResult the result of the dice roll to influence cannon selection
     * @param fire the projectile determining the cannon firing direction
     * @return a list of cannons selected based on the provided parameters
     */
    public List<Cannon> heavyMeteorCannon (Player p, int diceResult, Projectile fire) {
        List<Cannon> cannons;
        Ship s = p.getShip();
        Direction direction = fire.getDirection();
        if (direction == Direction.UP) {
            if (level == 2) {
                return s.getCannons(direction, diceResult - 4);
            } else {
                return s.getCannons(direction, diceResult - 5);
            }
            } else if (direction == Direction.DOWN) {
                if (level == 2) {
                    cannons = s.getCannons(direction, diceResult - 4);
                    cannons.addAll(s.getCannons(direction, diceResult - 5));
                    cannons.addAll(s.getCannons(direction, diceResult - 3));
                } else {
                    cannons = s.getCannons(direction, diceResult - 5);
                    cannons.addAll(s.getCannons(direction, diceResult - 6));
                    cannons.addAll(s.getCannons(direction, diceResult - 4));
                }
                return cannons;
            } else {
                cannons = s.getCannons(direction, diceResult - 5);
                cannons.addAll(s.getCannons(direction, diceResult - 6));
                cannons.addAll(s.getCannons(direction, diceResult - 4));
                return cannons;
            }
    }

    /**
     * Removes energy from the provided list of batteries and applies it to the player's ship.
     * If any battery in the list has zero available energy, an EnergyException is thrown.
     *
     * @param p       the player whose ship the energy will be applied to
     * @param energy  the list of batteries providing the energy to be removed
     * @throws EnergyException if any battery in the list has zero available energy
     */
    public void removeEnergy (Player p, List<Battery> energy) throws EnergyException{
        for (Battery e: energy){
            if (e.getAvailableEnergy() == 0) {
                throw new EnergyException("Not enough energy");
            }
        }
        for (Battery e : energy){
            p.getShip().useEnergy(e);
        }
    }


    /**
     * Initializes the countdown for the game based on the current level.
     * When the game is at level 2, the countdown will be initialized
     * specifically on the board associated with the game. This method
     * checks and casts the board to a NormalBoard to call its
     * initCountdown method.
     */
    public void initCountdown (){
        if (level == 2) {
            Board board = this.game.getBoard();
            ((NormalBoard) board).initCountdown();
        }
    }

    /**
     * Retrieves the hourglass timestamp associated with the hourglass rotation in the game.
     *
     * @return the hourglass timestamp as a long value
     */
    public long getHourglassTimestamp() {
        Board board = this.game.getBoard();
        return ((NormalBoard) board).getHourglassTimestamp();
    }

    /**
     * Retrieves the turned hourglass value from the game board.
     *
     * @return an integer representing the turned hourglass value of the board
     */
    public int getTurnedHourglass() {
        Board board = this.game.getBoard();
        return ((NormalBoard) board).getTurnedHourglass();
    }

    /**
     * Triggers the hourglass turn action for the current game board.
     * This method interacts with the board associated with the current game
     * and delegates the hourglass turning functionality specifically to a
     * NormalBoard instance.
     *
     * @throws HourglassException if there is an issue during the hourglass turn action
     */
    public void turnHourglass() throws HourglassException {
        Board board = this.game.getBoard();
        ((NormalBoard) board).turnHourglass();
    }

    /**
     * Calculates and retrieves the remaining time from the game board.
     *
     * @return the remaining time in the game as an integer
     */
    public int getRemainingTime() {
        Board board = this.game.getBoard();
        return ((NormalBoard) board).getRemainingTime();
    }

    /**
     * Calculates and returns the total remaining time in the game.
     *
     * @return the total remaining time calculated from the game's board
     */
    public int getTotalRemainingTime() {
        Board board = this.game.getBoard();
        return ((NormalBoard) board).getTotalRemainingTime();
    }

    /**
     * Retrieves a list of players who are currently in the game.
     *
     * @return a list of Player objects representing the players who are still active in the game.
     */
    public List<Player> getInGamePlayers () {
        List<Player> inGamePlayers = new ArrayList<>();
        game.sortPlayerByPosition();
        for (Player p : game.getPlayers()) {
            if (p.isInGame()) {
                inGamePlayers.add(p);
            }
        }
        return inGamePlayers;
    }

    /**
     * Automatically determines and validates the position of a ship for the given player
     * based on the current level of the game.
     *
     * @param p the player whose ship's position is to be validated
     */
    public void  autoValidation (Player p){
        int row;
        int column;
        if(level==2){
            row = 2;
            column = 3;
        }
        else{
            row = 2;
            column = 2;
        }

        p.getShip().findValid(row, column);
    }

    public void createDemoShips(){
        //load components.json into the hashmap of all components where the key is the id component of the component
        Map<Integer, Component> components = new HashMap<>();
        ObjectMapper mapper = JsonMapper.builder()
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .build();
        try {
            Component[] allComponents = mapper.readValue(getClass().getResourceAsStream("/components.json"), Component[].class);
            for (Component c : allComponents) {
                components.put(c.getIDComponent(), c);
            }
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "error while trying to read Component.json", e);
        }
        if(getLevel() == 2){
            try {
                buildDemoNormalShipInvalid1(getInGamePlayers().getFirst(), components);
                buildDemoNormalShipValid1(getInGamePlayers().get(1), components);
                if (getInGamePlayers().size() > 2) {
                    buildDemoNormalShipInvalid2(getInGamePlayers().get(2), components);
                }
                if (getInGamePlayers().size() > 3) {
                    buildDemoNormalShipValid2(getInGamePlayers().get(3), components);
                }
            }catch (Exception e){
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, "error while trying to build demo ships", e);
            }

        }
    }

    private void buildDemoNormalShipInvalid2(Player p, Map<Integer, Component> components) throws InvalidTileException {
        Component c;
        Ship s = p.getShip();
        c = components.get(97);
        s.addComponent(c, 0, 2);

        c = components.get(131);
        s.addComponent(c,0, 4);

        c = components.get(123);
        s.addComponent(c, 1, 1);

        c = components.get(45);
        c.rotateClockwise();
        s.addComponent(c, 1, 2);

        c = components.get(110);
        s.addComponent( c, 1, 3);

        c = components.get(145);
        c.rotateClockwise();
        s.addComponent( c, 1, 4);

        c = components.get(107);
        s.addComponent(c, 1, 5);

        c = components.get(49);
        s.addComponent(c, 2, 0);

        c = components.get(31);
        s.addComponent( c, 2, 1);

        c = components.get(135);
        c.rotateCounterclockwise();
        s.addComponent( c, 2, 2);

        c = components.get(11);
        c.rotateCounterclockwise();
        s.addComponent( c, 2, 4);

        c = components.get(19);
        c.rotateClockwise();
        c.rotateClockwise();
        s.addComponent( c, 2, 5);

        c = components.get(26);
        s.addComponent(c, 2, 6);

        c = components.get(98);
        s.addComponent(c, 3, 0);

        c = components.get(35);
        c.rotateClockwise();
        s.addComponent(c, 3, 1);

        c = components.get(40);
        c.rotateCounterclockwise();
        s.addComponent( c, 3, 2);

        c = components.get(148);
        s.addComponent( c, 3, 3);

        c = components.get(21);
        s.addComponent( c, 3, 4);

        c = components.get(32);
        s.addComponent( c, 3, 5);

        c = components.get(62);
        c.rotateCounterclockwise();
        s.addComponent( c, 3, 6);

        c = components.get(71);
        s.addComponent( c, 4, 0);

        c = components.get(66);
        s.addComponent( c, 4, 1);

        c = components.get(81);
        s.addComponent( c, 4, 2);

        c = components.get(70);
        c.rotateClockwise();
        s.addComponent( c, 4, 4);

        c = components.get(94);
        s.addComponent( c, 4, 5);

        c = components.get(86);
        s.addComponent( c, 4, 6);
    }

    private void buildDemoNormalShipInvalid1(Player p, Map<Integer, Component> components) throws InvalidTileException {
        Component c;
        Ship s = p.getShip();
        c = components.get(20);
        c.rotateCounterclockwise();
        s.addComponent(c, 0, 2);

        c = components.get(122);
        s.addComponent(c,0, 4);

        c = components.get(102);
        s.addComponent(c, 1, 1);

        c = components.get(5);
        c.rotateClockwise();
        s.addComponent(c, 1, 2);

        c = components.get(118);
        s.addComponent( c, 1, 3);

        c = components.get(56);
        c.rotateClockwise();
        s.addComponent( c, 1, 4);

        c = components.get(109);
        s.addComponent(c, 1, 5);

        c = components.get(115);
        c.rotateCounterclockwise();
        s.addComponent(c, 2, 0);

        c = components.get(48);
        c.rotateCounterclockwise();
        s.addComponent( c, 2, 1);

        c = components.get(12);
        s.addComponent( c, 2, 2);

        c = components.get(133);
        s.addComponent( c, 2, 4);

        c = components.get(38);
        c.rotateCounterclockwise();
        c.rotateCounterclockwise();
        s.addComponent( c, 2, 5);

        c = components.get(147);
        s.addComponent(c, 2, 6);

        c = components.get(15);
        s.addComponent(c, 3, 0);

        c = components.get(151);
        c.rotateCounterclockwise();
        c.rotateCounterclockwise();
        s.addComponent(c, 3, 1);

        c = components.get(29);
        c.rotateCounterclockwise();
        s.addComponent( c, 3, 2);

        c = components.get(10);
        s.addComponent( c, 3, 3);

        c = components.get(41);
        c.rotateCounterclockwise();
        c.rotateCounterclockwise();
        s.addComponent( c, 3, 4);

        c = components.get(58);
        s.addComponent( c, 3, 5);

        c = components.get(126);
        c.rotateClockwise();
        s.addComponent( c, 3, 6);

        c = components.get(88);
        s.addComponent( c, 4, 0);

        c = components.get(64);
        s.addComponent( c, 4, 1);

        c = components.get(84);
        s.addComponent( c, 4, 2);

        c = components.get(92);
        s.addComponent( c, 4, 4);

        c = components.get(83);
        s.addComponent( c, 4, 5);

        c = components.get(16);
        c.rotateClockwise();
        s.addComponent( c, 4, 6);
    }

    public void buildDemoNormalShipValid1(Player p, Map<Integer, Component> components) throws InvalidTileException {
        Component c;
        Ship s = p.getShip();
        c = components.get(96);
        s.addComponent(c, 0, 2);

        c = components.get(121);
        s.addComponent(c,0, 4);

        c = components.get(125);
        s.addComponent(c, 1, 1);

        c = components.get(0);
        c.rotateCounterclockwise();
        s.addComponent(c, 1, 2);

        c = components.get(117);
        s.addComponent( c, 1, 3);

        c = components.get(18);
        c.rotateCounterclockwise();
        c.rotateCounterclockwise();
        s.addComponent( c, 1, 4);

        c = components.get(104);
        c.rotateClockwise();
        s.addComponent(c, 1, 5);

        c = components.get(114);
        c.rotateCounterclockwise();
        s.addComponent(c, 2, 0);

        c = components.get(34);
        c.rotateCounterclockwise();
        s.addComponent( c, 2, 1);

        c = components.get(138);
        c.rotateCounterclockwise();
        c.rotateCounterclockwise();
        s.addComponent( c, 2, 2);

        c = components.get(60);
        s.addComponent( c, 2, 4);

        c = components.get(146);
        s.addComponent( c, 2, 5);

        c = components.get(14);
        c.rotateCounterclockwise();
        c.rotateCounterclockwise();
        s.addComponent(c, 2, 6);

        c = components.get(2);
        s.addComponent(c, 3, 0);

        c = components.get(3);
        c.rotateCounterclockwise();
        s.addComponent(c, 3, 1);

        c = components.get(149);
        c.rotateCounterclockwise();
        c.rotateCounterclockwise();
        s.addComponent( c, 3, 2);

        c = components.get(129);
        c.rotateCounterclockwise();
        c.rotateCounterclockwise();
        s.addComponent( c, 3, 3);

        c = components.get(43);
        s.addComponent( c, 3, 4);

        c = components.get(37);
        s.addComponent( c, 3, 5);

        c = components.get(57);
        c.rotateClockwise();
        s.addComponent( c, 3, 6);

        c = components.get(90);
        s.addComponent( c, 4, 0);

        c = components.get(91);
        s.addComponent( c, 4, 1);

        c = components.get(77);
        s.addComponent( c, 4, 2);

        c = components.get(4);
        c.rotateClockwise();
        s.addComponent( c, 4, 4);

        c = components.get(82);
        s.addComponent( c, 4, 5);

        c = components.get(95);
        s.addComponent( c, 4, 6);

    }

    public void buildDemoNormalShipValid2(Player p, Map<Integer, Component> components) throws InvalidTileException {
        Component c;
        Ship s = p.getShip();
        c = components.get(105);
        s.addComponent(c, 0, 2);

        c = components.get(128);
        s.addComponent(c, 0, 4);

        c = components.get(101);
        s.addComponent(c, 1, 1);

        c = components.get(59);
        s.addComponent(c, 1, 2);

        c = components.get(36);
        c.rotateClockwise();
        c.rotateClockwise();
        s.addComponent(c, 1, 3);

        c = components.get(143);
        s.addComponent(c, 1, 4);

        c = components.get(13);
        c.rotateClockwise();
        c.rotateClockwise();
        s.addComponent(c, 1, 5);

        c = components.get(99);
        s.addComponent(c, 2, 0);

        c = components.get(47);
        c.rotateCounterclockwise();
        c.rotateCounterclockwise();
        s.addComponent(c, 2, 1);

        c = components.get(33);
        c.rotateClockwise();
        s.addComponent(c, 2, 2);

        c = components.get(53);
        c.rotateClockwise();
        c.rotateClockwise();
        s.addComponent(c, 2, 4);

        c = components.get(144);
        c.rotateClockwise();
        c.rotateClockwise();
        s.addComponent(c, 2, 5);

        c = components.get(46);
        s.addComponent(c, 2, 6);

        c = components.get(39);
        c.rotateClockwise();
        s.addComponent(c, 3, 0);

        c = components.get(61);
        s.addComponent(c, 3, 1);

        c = components.get(139);
        s.addComponent(c, 3, 2);

        c = components.get(1);
        s.addComponent(c, 3, 3);

        c = components.get(6);
        s.addComponent(c, 3, 4);

        c = components.get(150);
        s.addComponent(c, 3, 5);

        c = components.get(134);
        c.rotateClockwise();
        s.addComponent(c, 3, 6);

        c = components.get(73);
        s.addComponent(c, 4, 0);

        c = components.get(78);
        s.addComponent(c, 4, 1);

        c = components.get(93);
        s.addComponent(c, 4, 2);

        c = components.get(74);
        c.rotateClockwise();
        s.addComponent(c, 4, 4);

        c = components.get(30);
        c.rotateClockwise();
        s.addComponent(c, 4, 5);

        c = components.get(63);
        s.addComponent(c, 4, 6);
    }
}


