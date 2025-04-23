package it.polimi.ingsw.gc20.model.gamesets;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.gc20.exceptions.*;
import it.polimi.ingsw.gc20.model.player.*;
import it.polimi.ingsw.gc20.model.cards.*;
import it.polimi.ingsw.gc20.model.components.*;
import it.polimi.ingsw.gc20.model.ship.*;


import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameModel {
    private Game game;
    private AdventureCard activeCard;
    private int level;
    /**
     * Default Constructor
     */
    public GameModel() {
        this.game = null;
        this.activeCard = null;
        this.level = 1;
    }

    /**
     * setter function for the level
     * @param level level of the game to set
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * getter function for the level
     * @return level
     */
    public int getLevel() {
        return this.level;
    }

    /**
     * setter function for the game
     * @param game game to set
     *
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * getter function for the game
     * @return game
     */
    public Game getGame() {
        return this.game;
    }

    /**
     * setter function for the active card
     * @param activeCard card to set as active
     */
    public void setActiveCard(AdventureCard activeCard) {
        this.activeCard = activeCard;
    }

    /**
     * getter function for the active card
     * @return activeCard
     */
    public AdventureCard getActiveCard() {
        return this.activeCard;
    }


    /** private function for the init of one player used only in start game
     *
     * @param username of the player
     * @param index of the player used to select the color
     * @return player created
     */
    private Player initPlayer (String username, int index){
        Player player = new Player();
        player.setUsername(username);
        player.setGameStatus(true);
        player.setColor(PlayerColor.values()[index]);
        if (level == 2){
            player.setShip(new NormalShip());
        }else{
            player.setShip(new LearnerShip());
        }
        return player;
    }

    /**
     * function that starts the game
     * it creates the game, the pile, set the level of the game and create the decks
     * and board based on the level
     * it also creates the players set the usernames, status, color and ship based on the level
     * and also add all component to the unViewed list
     * @param level           level of the game
     * @param usernames       list of the players' username
     * @param gameID          id of the game
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
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
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
     * function when a component is taken from the unViewed list,
     * it removes the component from the unViewed list
     *
     *
     * @param c component to remove
     * @throws ComponentNotFoundException if the component is not present in the unViewed list
     */
    public void componentFromUnviewed(Component c) throws ComponentNotFoundException {
        game.getPile().removeUnviewed(c);
    }

    /**
     * function when a component is taken from the viewed list,
     * it removes it from the viewed list
     * @param c component to remove
     * @throws ComponentNotFoundException if the component is not present in the viewed list
     */
    public void componentFromViewed(Component c) throws ComponentNotFoundException {
        game.getPile().removeViewed(c);
    }

    /**
     * function when a component is taken from the booked list
     * remove the component from the booked list of the player
     *
     * @apiNote this method is called only in level 2 games, and the component in the booked list cannot be moved in the viewed list
     * @param c component to remove
     * @param p player that takes the component
     * @throws ComponentNotFoundException the component is not present in the booked list
     */
    public void componentFromBooked(Component c, Player p) throws ComponentNotFoundException {
        Ship s = p.getShip();
        ((NormalShip) s).removeBooked(c);
    }

    /**
     * method that allows the player to view the deck
     *
     * @apiNote this method is called only in level 2 games
     * @param numDeck the deck to view
     * @throws InvalidIndexException if numDeck is not 1, 2 or 3
     */
    public List<AdventureCard> viewDeck(int numDeck) throws InvalidIndexException {
        Board b = game.getBoard();
        return ((NormalBoard) b).peekDeck(numDeck);
    }

    /**
     * function that allows the player to rotate the component taken clockwise
     *
     * @param c component to rotate
     */
    public void RotateClockwise(Component c) {
        c.rotateClockwise();
    }

    /**
     * function that allows the player to rotate the component taken counterclockwise
     *
     * @param c component to rotate
     */
    public void RotateCounterclockwise(Component c) {
        c.rotateCounterclockwise();
    }

    /**
     * function to add a component to the booked list only called in level 2 games
     *
     * @apiNote this method is called only in level 2 games
     * @param c component to add
     * @param p player that adds the component
     * @throws NoSpaceException if the booked list is full
     */
    public void componentToBooked(Component c, Player p) throws NoSpaceException {
        Ship s = p.getShip();
        ((NormalShip) s).addBooked(c);
    }

    /**
     * function to add a component to the viewed list
     *
     * @param c component to add
     * @throws DuplicateComponentException if the component is already present in the viewed list
     */
    public void componentToViewed(Component c) throws DuplicateComponentException {
        game.getPile().addViewed(c);
    }

    /**
     * function to add a component to the ship at the x, y coordinates
     *
     * @param c component to add
     * @param p player that adds the component
     * @param x x coordinate of the component
     * @param y y coordinate of the component
     * @throws InvalidTileException if the tile is not valid
     */
    public void addToShip(Component c, Player p, int x, int y) throws InvalidTileException {
        Ship s = p.getShip();
        s.addComponent(c, x, y);
    }

    /** function to stop assembling the ship by a player
     * when is it called the player chose a position where they want to start
     * if that position is already occupied the method throws an exception
     *
     * @apiNote catch the exception if the position isn't valid
     * @param p        player that stops assembling
     * @param position position chosen by the player (1-4)
     * @throws IllegalArgumentException if the position is already occupied
     */
    public void stopAssembling(Player p, int position) throws InvalidIndexException {
        game.getBoard().removePlayer(p);
        if (game.isOccupied(position)) {
            throw new InvalidIndexException("Position already occupied");
        } else {
            for (Player player : game.getPlayers()) {
                if (player == p) {
                    if (position == 1) {
                        player.setLeader();
                    }
                    player.setPosition(position);
                }
            }
        }

    }

    /**
     * function that validates the ship
     *
     * @param p player that validates the ship
     * @return boolean true if the ship is valid, false otherwise
     */
    public boolean shipValidating(Player p) {
        Ship s = p.getShip();
        return s.isValid();
    }

    /**
     * function to remove a component from the ship
     *
     * @param c component to remove
     * @param p player that removes the component
     * @throws ComponentNotFoundException if the component is not present in the ship
     */
    public void removeComponent(Component c, Player p) throws ComponentNotFoundException {
        Ship s = p.getShip();
        s.killComponent(c);
    }

    /**
     * function that sets the alien on the ship
     * it creates a new alien and call the method of the ship to add an alien to the ship
     *
     * @apiNote controller has to find a condition till the player can add alien to a ship (based on what it can host)
     * @apiNote function to call only in level 2 games
     *
     * @param a color of the alien
     * @param c cabin for the alien
     * @param p player that adds the alien
     * @throws InvalidAlienPlacement  if the component is not a cabin
     * @throws InvalidAlienPlacement if the alien has already been added
     */
    public void setAlien(AlienColor a, Cabin c, Player p) throws InvalidAlienPlacement {
        Ship s = p.getShip();
        ((NormalShip) s).addAlien(a, c);
    }

    /**
     * function that sets the astronaut on the ship and move the component from the booked list to the waste
     *
     * @param p player that adds the astronaut
     */
    public void addPieces(Player p) {
        Ship s = p.getShip();
        p.getShip().initAstronauts();
        s.addBookedToWaste();
    }

    /**
     * merge the four decks and create the deck to use in the game
     * @apiNote only called in level 2 games
     */
    public void createDeck() {
        Board b = game.getBoard();
        b.mergeDecks();
    }

    /**
     * function that automatically draw the first card of the deck and set it as active
     * it sorts the player by position in the board and verifies if the player is a lap behind or if the player doesn't have any astronaut
     *  if so the player is set as not in the game
     *
     * @return the card drawn
     * @throws EmptyDeckException if the deck is empty
     */
    public AdventureCard drawCard() throws EmptyDeckException{
        game.sortPlayerByPosition();
        for (int i = 1; i< game.getPlayers().size(); i++){
            if (game.getPlayers().getFirst().getPosition()-game.getPlayers().get(i).getPosition() >= game.getBoard().getSpaces()){
                game.getPlayers().get(i).setGameStatus(false);
            }
            if (game.getPlayers().get(i).getShip().getAstronauts()==0){
                game.getPlayers().get(i).setGameStatus(false);
            }
        }
        AdventureCard card = game.getBoard().drawCard();
        while (Objects.equals(card.getName(), "CombatZone") && getInGamePlayers().size() == 1){
            card = game.getBoard().drawCard();
        }
        this.setActiveCard(card);
        return card;
    }


    /** Function to call when the player loses some member of the crew
     * @param p player that loses the crew
     * @param l list of crew member to remove
     * @throws EmptyCabinException if the cabin is empty
     */
    public void loseCrew (Player p, List<Cabin> l) throws EmptyCabinException {
        for (Cabin cabin : l) {
            p.getShip().unloadCrew(cabin);
        }
    }

    /** Function to move a player
     * @param p player to move
     * @param num number of spaces to move
     */
    public void movePlayer (Player p, int num){
        game.move(p, num);
    }

    /** Function to add credits to a player
     * @param p player to add the credits
     * @param credits number of credits to add
     */
    public void addCredits (Player p, int credits){
        p.addCredits(credits);
    }

    /** function to get the number of astronauts and alien for certain condition of some cards
     * @param p player who chose to activate the effect of the card
     * @return the number of astronauts and alien
     */
    public int getCrew (Player p) {
        return p.getShip().crew();
    }

    /**
     * function to call when is needed to calculate the firepower of the ship
     * based on the base firepower of a ship and the double cannons that the player activates
     *
     * @param p       player who chose to activate the effect of the card
     * @param cannons double cannons to activate
     * @param energy  energy to use
     * @throws EnergyException if the energy is not enough
     * @throws InvalidCannonException if the cannon is not valid
     */
    public float FirePower(Player p, Set<Cannon> cannons, List<Battery> energy) throws EnergyException, InvalidCannonException {
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
     * function to call when is needed to calculate the firepower of the ship
     *
     * @param p             player who chose to activate the effect of the card
     * @param doubleEngines number of double engines to activate
     * @param energy        list of energy to use
     * @return the engine power of the ship
     * @throws EnergyException if the energy passed is not enough or the ship has not sufficient energy
     * @throws InvalidEngineException if the engines activated are not valid
     */
    public int EnginePower(Player p, int doubleEngines, List<Battery> energy) throws EnergyException, InvalidEngineException {
        int power;
        for (Battery e : energy) {
            if (e.getAvailableEnergy() == 0) {
                throw new EnergyException("Not enough energy");
            }
        }
        if (doubleEngines <= energy.size() && energy.size() < p.getShip().getTotalEnergy()) {
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
     * method to move or remove a cargo from the ship if the cargoHold to is null
     * @param c    cargo to move
     * @param from cargoHold from
     * @param to   cargoHold to
     * @throws InvalidCargoException if there is no cargo of that type to unload
     * @throws CargoNotLoadable if the cargo is not loadable
     * @throws CargoFullException if the cargoHold is full
     */
    public void MoveCargo(Player p, CargoColor c, CargoHold from, CargoHold to) throws InvalidCargoException, CargoNotLoadable, CargoFullException {
        if (to == null){
            p.getShip().unloadCargo(c, from);
            return;
        }
        if (to.getAvailableSlots()==0){
            throw new CargoFullException("CargoHold has not available slots");
        }
        p.getShip().loadCargo(c, to);
    }

    /**
     * method to add a cargo to the cargoHold
     *
     * @param p player that is adding the cargo
     * @param c  cargo to add
     * @param ch cargoHold to add the cargo it needs to be not full
     * @throws CargoNotLoadable if the cargo is not loadable
     * @throws CargoFullException if the cargoHold is full
     */
    public void addCargo(Player p, CargoColor c, CargoHold ch)  throws CargoNotLoadable, CargoFullException {
        p.getShip().loadCargo(c, ch);
    }

    /**
     * method to remove a battery when a shield is used
     *
     * @param p player who chose to activate the effect of the card
     * @param e energy to use
     * @throws EnergyException if the energy is not enough
     */
    public void useShield(Player p, Battery e) throws EnergyException {
        p.getShip().useEnergy(e);
    }

    /**
     * method to calculate the score of the players
     *
     * @return map of the player and the score
     */
    public Map<Player, Integer> calculateScore() {
        Map<Player, Integer> score = new HashMap<>();
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

    /** function that returns the number of astronauts of one ship
     *
     * @param p player which number of astronauts needs to be returned
     */
    public int getAstronauts(Player p) {
        return p.getShip().getAstronauts();
    }

    /** function to call when a projectile is fired and hit the ship
     * @param p player who gets hit
     * @param diceResult result of the dice throw that indicates the row or column hit
     * @throws InvalidShipException if the ship is invalid
     * @apiNote controller uses this method only if the projectile hit the ship
     */
    public void Fire (Player p, int diceResult, Projectile fire) throws InvalidShipException {
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

    /** function to find cannon that could be activated to protect the ship from a heavy meteor
     *
     * @param p player
     * @param diceResult result of the dice throw
     * @param fire projectile
     * @return list of cannon which point in the same direction of the meteor
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

    /** function to removeEnergy to use it in case of not having enough cargo
     * @apiNote controller needs to verify how much energy the player has to remove when cargo is not enough
     * @param p player that needs to remove the energy
     * @param energy list of energy to remove from the ship
     * @throws EnergyException if the energy is not enough
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
     * Function that starts the hourglass. This function is meant to be called only once per match, at the beginning of the game.
     */
    public void initCountdown (){
        if (level == 2) {
            Board board = this.game.getBoard();
            ((NormalBoard) board).initCountdown();
        }
    }

    /** function that return the amount of time the hourglass has been turned
     *
     * @return the amount of time the hourglass has been turned
     */
    public int getTurnedHourglass() {
        Board board = this.game.getBoard();
        return ((NormalBoard) board).getTurnedHourglass();
    }

    /** Function that turns the hourglass, to be used every time a player turns the hourglass except for the first time (which is done at the beginning of the game)
     * @throws HourglassException if the hourglass is already turned 3 times, or if the remaining time is not 0
     */
    public void turnHourglass() throws HourglassException {
        Board board = this.game.getBoard();
        ((NormalBoard) board).turnHourglass();
    }

    /** Function that returns the remaining time
     * @return The number of seconds left of the current turn
     */
    public int getRemainingTime() {
        Board board = this.game.getBoard();
        return ((NormalBoard) board).getRemainingTime();
    }

    /** Function that returns the total remaining time
     * @return int is the number of seconds left
     */
    public int getTotalRemainingTime() {
        Board board = this.game.getBoard();
        return ((NormalBoard) board).getTotalRemainingTime();
    }

    /** Function that returns a list of the player that is actually in the game
     * @return List of the player that are in the game
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

    /** Function that set the player status to false if the choice to give up
     * @param p player that chooses to give up
     */
    public void giveUp (Player p) {
        p.setGameStatus(false);
    }

    /** Function that auto validate the ship if the player is disconnected
     * @param p player that is disconnected
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


}


