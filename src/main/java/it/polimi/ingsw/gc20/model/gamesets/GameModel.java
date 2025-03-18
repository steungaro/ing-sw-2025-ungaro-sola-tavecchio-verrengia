package it.polimi.ingsw.gc20.model.gamesets;

import it.polimi.ingsw.gc20.model.player.*;
import it.polimi.ingsw.gc20.model.cards.*;
import it.polimi.ingsw.gc20.model.components.*;
import it.polimi.ingsw.gc20.model.bank.*;
import it.polimi.ingsw.gc20.model.ship.*;

import java.security.InvalidParameterException;
import java.util.*;


public class GameModel {
    private Game game;
    private AdventureCard activeCard;
    private int level;

    /**
     * Default Constructor
     */
    public void GameModel() {
        this.game = null;
        this.activeCard = null;
        this.level = 1;
    }

    /**
     * set function for the level
     *
     * @param level
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * get function for the level
     *
     * @return level
     */
    public int getLevel() {
        return this.level;
    }

    /**
     * set function for the game
     *
     * @param game
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * get function for the game
     *
     * @return game
     */
    public Game getGame() {
        return this.game;
    }

    /**
     * set function for the active card
     *
     * @param activeCard
     */
    public void setActiveCard(AdventureCard activeCard) {
        this.activeCard = activeCard;
    }

    /**
     * get function for the active card
     *
     * @return activeCard
     */
    public AdventureCard getActiveCard() {
        return this.activeCard;
    }

    /**
     * function that starts the game
     * it creates the game, the pile, set the level of the game and create the decks (TODO implement with json)
     * and board based on the level
     * it also creates the players set the usernames, status, color and ship based on the level
     * and also add all component to the unviewed list TODO implement with the json
     * @param livello         level of the game
     * @param usernames       list of the players' username
     * @param gameID          id of the game
     */
    public void startGame(int livello, List<String> usernames, int gameID) {
        Game game = new Game();
        game.setID(gameID);
        Pile pile = new Pile();
        Board board;
        setLevel(livello);
        //creating the board based on the level
        if (level == 2) {
            board = new NormalBoard();
            //TODO create and setting the four deck
        } else {
            board = new LearnerBoard();
            //TODO create and setting the deck
        }

        //creating the players and setting the first player
        for (int i = 0; i < usernames.size(); i++) {
            Player player = new Player();
            if (i == 0) {
                //setting username and status of the first player
                player.setUsername(usernames.get(i));
                player.setGameStatus(true);
            }
            player.setColor(PlayerColor.values()[i]);
            //setting the ship based on the level
            if (livello == 2) {
                player.setShip(new NormalShip());
            } else {
                player.setShip(new LearnerShip());
            }
            board.addPlayer(player);
            game.addPlayer(player);
        }
        game.addBoard(board);
        pile.addUnviewed(/*all components*/);
        game.setPile(pile);

        this.setGame(game);
    }

    /**
     * function when a component is taken from the unviewed list
     * it removes the component from the unviewed list
     * TODO capire se devo ritornare il componente al controller oppure no, teoricamente no
     *
     * @param c component to remove
     * @param p player that takes the component
     * @throws NoSuchElementException if the component is not present in the unviewed list
     */
    public void componentFromUnviewed(Component c, Player p) throws NoSuchElementException {
        game.getPile().removeUnviewed(c);
    }

    /**
     * function when a component is taken from the viewed list
     * it removes it from the viewed list
     *
     * @param c component to remove
     * @param p player that takes the component
     * @throws NoSuchElementException if the component is not present in the viewed list
     */
    public void componentFromViewed(Component c, Player p) throws NoSuchElementException {
        game.getPile().removeViewed(c);
    }

    /**
     * function when a component is taken from the booked list
     * it remove the component from the booked list of the player
     *
     * @apiNote this method is called only in level 2 games, and the component in booked list cannot be moved in the viewed list
     * @param c component to remove
     * @param p player that takes the component
     * @throws IllegalArgumentException if the component is not present in the booked list
     */
    public void componentFromBooked(Component c, Player p) throws IllegalArgumentException {
        Ship s = p.getShip();
        ((NormalShip) s).removeBooked(c);
    }

    /**
     * method that allows the player to view the deck
     *
     * @apiNote this method is called only in level 2 games
     * @param numDeck the deck to view
     * @throws IllegalArgumentException if numDeck is not 1, 2 or 3
     */
    public void viewDeck(int numDeck) throws IllegalArgumentException {
        Board b = game.getBoard();
        ((NormalBoard) b).peekDeck(numDeck);
    }

    /**
     * function that allows the player to rotate the component taken clockwise
     *
     * @param c component to rotate
     * @param p player that rotates the component
     */
    public void RotateClockwise(Component c, Player p) {
        c.rotateClockwise();
    }

    /**
     * function that allows the player to rotate the component taken counterclockwise
     *
     * @param c component to rotate
     * @param p player that rotates the component
     */
    public void RotateCounterclockwise(Component c, Player p) {
        c.rotateCounterclockwise();
    }

    /**
     * function to add a component to the booked list only called in level 2 games
     *
     * @apiNote this method is called only in level 2 games
     * @param c component to add
     * @param p player that adds the component
     */
    public void componentToBooked(Component c, Player p) {
        Ship s = p.getShip();
        ((NormalShip) s).addBooked(c);
    }

    /**
     * function to add a component to the viewed list
     *
     * @param c component to add
     * @param p player that adds the component
     */
    public void componentToViewed(Component c, Player p) {
        game.getPile().addViewed(c);
    }

    /**
     * function to add a component to the ship at the x, y coordinates
     *
     * @param c component to add
     * @param p player that adds the component
     * @param x x coordinate of the component
     * @param y y coordinate of the component
     */
    public void addToShip(Component c, Player p, int x, int y) {
        Ship s = p.getShip();
        s.addComponent(c, x, y);
    }

    /** function to stop assembling the ship by a player
     * when is it called the player chose a position where they want to start
     * if that posintion is already occupied the method throws an exception
     *
     * function when a player stop assembling
     * @apiNote catch the exception if the position isn't valid
     * @param p        player that stops assembling
     * @param position position chosen by the player (1-4)
     * @throws IllegalArgumentException if the position is already occupied
     */
    public void stopAssembling(Player p, int position) throws IllegalArgumentException {
        game.getBoard().removePlayer(p);
        if (game.isOccupied(position)) {
            throw new IllegalArgumentException("Position already occupied");
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
     */
    public void removeComponent(Component c, Player p) {
        Ship s = p.getShip();
        s.killComponent(c);
    }

    /**
     * function that sets the alien in the ship
     * it creates a new alien and call the method of the ship to add an alien to the ship
     *
     * @apiNote capire condizione per smettere di aggiungere alieni
     * @apiNote funzione da chiamare solo in game di livello 2
     *
     * @param a color of the alien
     * @param c cabin for the alien
     * @param p player that adds the alien
     * @throws IllegalArgumentException  if the component is not a cabin
     * @throws InvalidParameterException if the cabin cannot host this type of alien
     */
    public void setAlien(AlienColor a, Component c, Player p) throws IllegalArgumentException, InvalidParameterException {
        Ship s = p.getShip();
        Alien alien = new Alien();
        alien.setColor(a);
        alien.setCabin(((Cabin)c));
        ((NormalShip) s).addAlien(alien, c);
    }

    /**
     * function that sets the astronaut in the ship and move the component frome the booked list to the waste
     *
     * @param p player that adds the astronaut
     * @throws IllegalArgumentException  if the component is not a cabin
     * @throws InvalidParameterException if the cabin cannot host this type of astronaut
     */
    public void addPieces(Player p) {
        Ship s = p.getShip();
        p.getShip().initAstronauts();
        if (level == 2) {
            ((NormalShip) s).addBookedToWaste();
        }
    }

    /**
     * merge the four deck and create the deck to use in the game
     * @apiNote only called in level 2 games
     */
    public void createDeck() {
        Board b = game.getBoard();
        ((NormalBoard) b).mergeDecks();
    }

    /**
     * function that automatically draw the first card of the deck and set it as active
     * it sort the player by position in the board and verify if the player is a lap behind or if the player don't have any astronaut
     *  if so the player is set as not in game
     *
     * @return the card drawn
     */
    public AdventureCard drawCard() {
        game.sortPlayerByPosition();
        for (int i = 1; i<= game.getPlayers().size(); i++){
            if (game.getPlayers().get(0).getPosition()-game.getPlayers().get(i).getPosition() >= game.getBoard().getSpaces()){
                game.getPlayers().get(i).setGameStatus(false);
            }
            if (game.getPlayers().get(i).getShip().getAstronauts()==0){
                game.getPlayers().get(i).setGameStatus(false);
            }
        }

        this.setActiveCard(game.getBoard().drawCard());
        return this.getActiveCard();
    }

    /**
     * function to call when the Planet card is active and the player select a planet to land
     * it return the list of cargo reward for the player that land in a planet
     *
     * @param p     player that lands
     * @param index index of the planet
     * @return the list of cargo colors
     */
    public List<Cargo> PlanetLand(Player p, int index) {
        AdventureCard c = getActiveCard();
        Planet planet = ((Planets) c).getPlanet(index);
        return ((Planets) c).land(p, planet);
    }

    /**
     * function to apply the effect of the abandoned ship card
     *
     * @apiNote the player as already chose the crew members to remove, controller need to pass the list of crew members to remove
     *
     * @param p player whose chose to activate the effect of the card
     * @param a list of crew members to remove
     * @return the list of cargo colors
     */
    public void AbbandonedShip(Player p, List<Crew> a) {
        AdventureCard c = getActiveCard();
        setActiveCard(null);
        ((AbandonedShip) c).Effect(p, game, a);
    }

    /** function to get the number of astronauts and alien for certain condition of some cards
     * @param p player whose chose to activate the effect of the card
     * @return the number of astronauts and alien
     */
    public int getCrew (Player p) {
        return p.getShip().crew();
    }
    /**
     * function to apply the effect of the abandoned station card
     *
     * @apiNote the controller needs to verify the number of crew member of the ship before calling this method, calling this only if the player activate the effect
     * @param p player whose chose to activate the effect of the card
     * @return the list of cargo colors
     */
    public List<Cargo> AbbandonedStation(Player p) {
        AdventureCard c = getActiveCard();
        Ship s = p.getShip();
        return ((AbandonedStation) c).Effect(p, game);
    }

    /**
     * function to call when is needed to calculate the firepower of the ship
     * based on the base firepower of a ship and the double cannons that the player activate
     *
     * @param p       player whose chose to activate the effect of the card
     * @param cannons double cannons to activate
     * @param energy  energy to use
     */
    public float FirePower(Player p, Set<Cannon> cannons, Set<Energy> energy) throws IllegalArgumentException {
        float power = 0;
        try {
            power = p.getShip().firePower(cannons, energy.size());
            for (Energy e : energy) {
                p.getShip().useEnergy(e);
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Not enough energy");
        }
        return power;
    }

    /**
     * function to call when is needed to calculate the firepower of the ship
     *
     * @param p             player whose chose to activate the effect of the card
     * @param doubleEngines number of double engines to activate
     * @param energy        list of energy to use
     * @return the engine power of the ship
     * @throws IllegalArgumentException
     */
    public int EnginePower(Player p, int doubleEngines, Set<Energy> energy) throws IllegalArgumentException {
        int power = 0;
        if (doubleEngines < energy.size() && energy.size() < p.getShip().getTotalEnergy()) {
            power = p.getShip().enginePower(doubleEngines);
            for (Energy e : energy) {
                p.getShip().useEnergy(e);
            }
        } else {
            throw new IllegalArgumentException("Not enough energy");
        }
        return power;
    }

    /**
     * method to move or remove a cargo from the ship if the to cargoHold is null
     *
     * @param p    player whose chose to activate the effect of the card
     * @param c    cargo to move
     * @param from cargoHold from
     * @param to   cargoHold to
     */
    public void MoveCargo(Player p, Cargo c, CargoHold from, CargoHold to) {
        c.setCargoHold(to);
        from.unloadCargo(c);
        if (to != null) {
            to.loadCargo(c);
        }
    }

    /**
     * method to add a cargo to the cargoHold
     *
     * @param p  player whose is adding the cargo
     * @param c  cargo to add
     * @param ch cargoHold to add the cargo it needs to be not full
     */
    public void addCargo(Player p, Cargo c, CargoHold ch) {
        ch.loadCargo(c);
        c.setCargoHold(ch);
    }

    /**
     * method for the openSpace card
     * it verifies if the engine power is zero if so it set the player state to false
     *
     * @param p             player whose chose to activate the effect of the card
     * @param doubleEngines number of double engines to activate
     * @param energy        list of energy to use
     * @throws IllegalArgumentException if there is not enough energy
     */
    public void OpenSpace(Player p, int doubleEngines, Set<Energy> energy) throws IllegalArgumentException {
        int power = 0;
        AdventureCard c = getActiveCard();
        try {
            power = EnginePower(p, doubleEngines, energy);
            if (power == 0) {
                p.setGameStatus(false);
            }
            ((OpenSpace) c).Effect(p, game, power);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Not enough energy");
        }
    }

    /**
     * method to remove a battery when  a shield is utilized
     *
     * @param p player whose chose to activate the effect of the card
     * @param e energy to use
     */
    public void UseShield(Player p, Energy e) {
        e.getBattery().useEnergy(e);
        p.getShip().useEnergy(e);
    }

    /**
     * method for the stardust card
     *
     * @param p player victim of the effect of the card
     */
    public void Stardust(Player p) {
        AdventureCard c = getActiveCard();
        ((Stardust) c).Effect(p, game);
    }

    /**
     * method for the epidemic card
     *
     * @param p player victim of the effect of the card
     */
    public void Epidemic(Player p) {
        AdventureCard c = getActiveCard();
        ((Epidemic) c).Effect(p);
    }

    /**
     * method to calculate the score of the players
     * TODO verify that the function is correct
     *
     */
    public Map<Player, Integer> calculateScore() {
        Map<Player, Integer> score = new HashMap();
        game.sortPlayerByPosition();
        int min = 0;
        int waste = 0;
        Player g = null;
        int points=4;
        for (Player p : game.getPlayers()) {
            if (p.isInGame()) {
                if (min > p.getShip().getAllExposed() || min == 0) {
                    min = p.getShip().getAllExposed();
                    g = p;
                }
                if (level == 2) {
                    score.put(p, points * 2);
                } else {
                    score.put(p, points);
                }
                points --;
            }
            waste = p.getShip().getWaste().size();

            score.put(p, score.get(p) - waste);
            for (Cargo c : p.getShip().getCargo()) {
                score.put(p, score.get(p) + c.getColor().value());
            }
        }
        if (g.isInGame()) {
            if (level == 2) {
                score.put(g, score.get(g) + 4);
            } else {
                score.put(g, score.get(g) + 2);
            }
        }
        return score;
    }

    /** function that return the number of astronuts of one ship
     *
     * @param p
     */
    public int getAstronauts(Player p) {
        return p.getShip().getAstronauts();
    }

    /** function for the lost days effect of the combat zone card
     *
     * @param p player
     */
    public void CombatZoneLostDays (Player p){
        AdventureCard c = getActiveCard();
        ((CombatZone) c).EffectLostDays(p, game);
    }

    public void CombatZoneLostCrew (Player p, List<Crew> c){
        AdventureCard card = getActiveCard();
        ((CombatZone) card).EffectLostCrew(p, c);
    }

    public void combatZoneLostCargo (Player p, List <Cargo> c){
        AdventureCard card = getActiveCard();
        ((CombatZone) card).EffectLostCargo(p, c);
    }

    public List<Projectile> combatZoneGetFire(Player p){
        AdventureCard card = getActiveCard();
        return ((CombatZone) card).EffectCannonFire();
    }

    /** function to call when a projectile is fired and hit the ship
     * @param p player who get hit
     * @param diceResult result of the dice throw that indicates the row or column hit
     * @throws Exception if the ship is invalid
     * @APINote il controller mi passa il proeittile solamente se colpisce la nave
     */
    public void Fire (Player p, int diceResult, Projectile fire) throws Exception {
        Component c = null;
        if (fire.getFireType() == FireType.LIGHTMETEOR) {
            c = p.getShip().getFirstComponent(fire.getDirection(), diceResult);
            if (c.getConnectors().get(fire.getDirection()) == ConnectorEnum.ZERO) {
                return;
            }
        }
        fire.Fire(p.getShip(), diceResult);
    }

    public List<Cannon> heavyMeteorCannon (Player p, int diceResult, Projectile fire) throws Exception {
        List<Cannon> cannons;
        Ship s = p.getShip();
        Direction direction = fire.getDirection();
        if (direction == Direction.UP) {
            if (s instanceof NormalShip) {
                return s.getCannons(direction, diceResult - 4);
            } else {
                return s.getCannons(direction, diceResult - 5);
            }
            } else if (direction == Direction.DOWN) {
                if (s instanceof NormalShip) {
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


    public void SlaversSuccess (Player p, Integer credits){
        AdventureCard card = getActiveCard();
        ((Slavers) card).EffectSuccess(p, game);
    }

    public void slaversFailure (Player p, List<Crew> l){
        AdventureCard card = getActiveCard();
        ((Slavers) card).EffectFailure(p, l);
    }


    public List<Cargo> smugglersSuccess (Player p, Game game){
        AdventureCard card = getActiveCard();
        return ((Smugglers) card).EffectSuccess(p, game);
    }

    public void smugglersFailure (Player p, List<Cargo> l){
        AdventureCard card = getActiveCard();
        ((Smugglers) card).EffectFailure(p, l);
    }

    public void piratesSuccess (Player p, Game game){
        AdventureCard card = getActiveCard();
        ((Pirates) card).EffectSuccess(p, game);
    }

    public List<Projectile> piratesFailure (){
        AdventureCard card = getActiveCard();
        return ((Pirates) card).EffectFailure();
    }


    public void removeEnergy (Player p, List<Energy> energy){
        for (Energy e : energy){
            p.getShip().useEnergy(e);
        }
    }

    //TODO gestione rimozione cargo insufficienti (il controller verica se mancano e chiama il metodo per rimuovere l'energia)
    //TODO metodi per gestione sceglie di ritirarsi (nel controller)
    //TODO gestione creazione dei deck (da vedere con json)
    //TODO capire la condizione per aggiungere gli alieni alla ship servirebbe una condizione tipo se puo hostare ancora un alieno ma in ship non ho nulla
    //TODO controller deve mandare cargo da scaricare in ordine di valore prendendo sempre quelli che valgono di più
}


