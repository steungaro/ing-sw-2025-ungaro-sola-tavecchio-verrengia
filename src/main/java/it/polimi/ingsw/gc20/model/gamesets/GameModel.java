package it.polimi.ingsw.gc20.model.gamesets;

import it.polimi.ingsw.gc20.model.player.*;
import it.polimi.ingsw.gc20.model.cards.*;
import it.polimi.ingsw.gc20.model.components.*;
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

    /** private function to init the player used to simplify the start game function
     *
     * @param username of the player
     * @param index of the player to set the color
     * @return the player initialized
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
     * it creates the game, the pile, set the level of the game and create the decks (TODO implement with json)
     * and board based on the level
     * it also creates the players set the usernames, status, color and ship based on the level
     * and also add all component to the unviewed list TODO implement with the json
     * @param level           level of the game
     * @param usernames       list of the players' username
     * @param gameID          id of the game
     */
    public void startGame(int level, List<String> usernames, int gameID) {
        Game game = new Game();
        game.setID(gameID);
        Pile pile = new Pile();
        Board board;
        setLevel(level);
        //creating the board based on the level
        if (level == 2) {
            board = new NormalBoard();
            //TODO create and setting the four deck
        } else {
            board = new LearnerBoard();
            //TODO create and setting the deck
        }

        //creating the players and initializing the player
        for (int i = 0; i < usernames.size(); i++) {
            Player player = initPlayer(usernames.get(i), i);
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
     * TODO understand whether the chosen component should be returned to the controller
     *
     * @param c component to remove
     * @throws NoSuchElementException if the component is not present in the unviewed list
     */
    public void componentFromUnviewed(Component c) throws NoSuchElementException {
        game.getPile().removeUnviewed(c);
    }

    /**
     * function when a component is taken from the viewed list
     * it removes it from the viewed list
     * @param c component to remove
     * @throws NoSuchElementException if the component is not present in the viewed list
     */
    public void componentFromViewed(Component c) throws NoSuchElementException {
        game.getPile().removeViewed(c);
    }

    /**
     * function when a component is taken from the booked list
     * remove the component from the booked list of the player
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
    public List<AdventureCard> viewDeck(int numDeck) throws IllegalArgumentException {
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
     */
    public void componentToBooked(Component c, Player p) {
        Ship s = p.getShip();
        ((NormalShip) s).addBooked(c);
    }

    /**
     * function to add a component to the viewed list
     *
     * @param c component to add
     */
    public void componentToViewed(Component c) {
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
     * if that position is already occupied the method throws an exception
     *
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
     * @apiNote controller has to find a condition till the player can add alien to a ship (based on what it can host)
     * @apiNote function to call only in level 2 games
     *
     * @param a color of the alien
     * @param c cabin for the alien
     * @param p player that adds the alien
     * @throws IllegalArgumentException  if the component is not a cabin
     * @throws InvalidParameterException if the cabin cannot host this type of alien
     */
    public void setAlien(AlienColor a, Cabin c, Player p) throws IllegalArgumentException, InvalidParameterException {
        Ship s = p.getShip();
        ((NormalShip) s).addAlien(a, c);
    }

    /**
     * function that sets the astronaut in the ship and move the component from the booked list to the waste
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
            if (game.getPlayers().getFirst().getPosition()-game.getPlayers().get(i).getPosition() >= game.getBoard().getSpaces()){
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
    public List<CargoColor> PlanetLand(Player p, int index) {
        AdventureCard c = getActiveCard();
        Planet planet = ((Planets) c).getPlanet(index);
        return ((Planets) c).land(p, planet);
    }

    /**
     * function to apply the effect of the abandoned ship card
     *
     * @apiNote the player has already chosen the crew members to remove, controller need to pass the list of cabins
     *
     * @param p player whose chose to activate the effect of the card
     * @param a list of cabins to remove crew members from
     */
    public void AbandonedShip(Player p, List<Cabin> a) {
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
    public List<CargoColor> AbandonedStation(Player p) {
        AdventureCard c = getActiveCard();
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
    public float FirePower(Player p, Set<Cannon> cannons, List<Battery> energy) throws IllegalArgumentException {
        float power;
        try {
            power = p.getShip().firePower(cannons, energy.size());
            for (Battery e : energy) {
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
     * @throws IllegalArgumentException if the energy passed is not enough or the ship has not sufficient energy
     */
    public int EnginePower(Player p, int doubleEngines, List<Battery> energy) throws IllegalArgumentException {
        int power;
        if (doubleEngines < energy.size() && energy.size() < p.getShip().getTotalEnergy()) {
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
     * method to move or remove a cargo from the ship if the to cargoHold is null
     * @param c    cargo to move
     * @param from cargoHold from
     * @param to   cargoHold to
     */
    public void MoveCargo(CargoColor c, CargoHold from, CargoHold to) {
        from.unloadCargo(c);
        if (to != null) {
            to.loadCargo(c);
        }
    }

    /**
     * method to add a cargo to the cargoHold
     *
     * @param c  cargo to add
     * @param ch cargoHold to add the cargo it needs to be not full
     */
    public void addCargo(CargoColor c, CargoHold ch) {
        ch.loadCargo(c);
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
    public void OpenSpace(Player p, int doubleEngines, List<Battery> energy) throws IllegalArgumentException {
        int power;
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
    public void UseShield(Player p, Battery e) {
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
        Map<Player, Integer> score = new HashMap<>();
        game.sortPlayerByPosition();
        int min = 0;
        int waste;
        Player g=game.getPlayers().getFirst();
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
            for (Map.Entry<CargoColor, Integer> e : p.getShip().getCargo().entrySet()) {
                CargoColor color = e.getKey();
                int quantity = e.getValue();
                score.put (p, score.get(p) + color.value()*quantity);
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

    /** function that return the number of astronauts of one ship
     *
     * @param p player which number of astronauts need to be returned
     */
    public int getAstronauts(Player p) {
        return p.getShip().getAstronauts();
    }

    /** function for the lost days effect of the combat zone card
     *
     * @apiNote for the combat zone controller needs to calculate which player has which effect similar in pirates, smuggler and slavers
     * @param p player
     */
    public void CombatZoneLostDays (Player p){
        AdventureCard c = getActiveCard();
        ((CombatZone) c).EffectLostDays(p, game);
    }

    /** function for the lost crew effect of the combat zone card
     *
     * @param p player
     * @param c list of crew to remove
     */
    public void CombatZoneLostCrew (Player p, List<Cabin> c){
        AdventureCard card = getActiveCard();
        ((CombatZone) card).EffectLostCrew(p, c);
    }

    /** function for the lost cargo effect of the combat zone card
     *
     * @param p player
     * @param c list of cargo to remove
     */
    public void combatZoneLostCargo (Player p, List <CargoHold> c){
        AdventureCard card = getActiveCard();
        ((CombatZone) card).EffectLostCargo(p, c);
    }

    /** function for the Fire effect of the combat zone card
     *
     * @return list of projectile to be shot at the player
     * @apiNote the controller needs to verify if the projectile hit the ship, in case of hit the Fire method is called in gameModel
     */
    public List<Projectile> combatZoneGetFire(){
        AdventureCard card = getActiveCard();
        return ((CombatZone) card).EffectCannonFire();
    }

    /** function to call when a projectile is fired and hit the ship
     * @param p player who get hit
     * @param diceResult result of the dice throw that indicates the row or column hit
     * @throws Exception if the ship is invalid
     * @apiNote controller utilize this methon only if the projectile hit the ship
     */
    public void Fire (Player p, int diceResult, Projectile fire) throws Exception {
        Component c;
        if (fire.getFireType() == FireType.LIGHT_METEOR) {
            c = p.getShip().getFirstComponent(fire.getDirection(), diceResult);
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
     * @return list of cannon which point the same direction of the meteor
     */
    public List<Cannon> heavyMeteorCannon (Player p, int diceResult, Projectile fire) {
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


    /**function to call in case of player winning against slavers and choosing to take the reward
     * @apiNote controller need to verify which method in the model call (success or failure)
     * @param p player that activate the effect
     */
    public void SlaversSuccess (Player p){
        AdventureCard card = getActiveCard();
        ((Slavers) card).EffectSuccess(p, game);
    }

    /** function to call in case of player losing against slavers
     *
     * @param p player that activate the effect
     * @param l list of crew member to remove
     */
    public void slaversFailure (Player p, List<Cabin> l){
        AdventureCard card = getActiveCard();
        ((Slavers) card).EffectFailure(p, l);
    }


    /** function to call in case of player winning against smugglers, and it chose to take the reward
     *
     * @param p player that activate the effect
     * @return list of cargo reward
     */
    public List<CargoColor> smugglersSuccess (Player p){
        AdventureCard card = getActiveCard();
        return ((Smugglers) card).EffectSuccess(p, game);
    }

    /** function to call in case of player losing against smugglers
     *
     * @param p player that activate the effect
     * @param l list of cargo to remove
     */
    public void smugglersFailure (Player p, List<CargoHold> l){
        AdventureCard card = getActiveCard();
        ((Smugglers) card).EffectFailure(p, l);
    }

    /** function to call in case of player winning against pirates and chose to activate the effect
     *
     * @param p player that activate the effect
     */
    public void piratesSuccess (Player p){
        AdventureCard card = getActiveCard();
        ((Pirates) card).EffectSuccess(p, game);
    }

    /** function to call in case of player losing against pirates
     *
     * @return list of projectile to fire at the ship of the player
     * @apiNote controller needs to roll the dice and call the method fire when a projectile hits the ship
     */
    public List<Projectile> piratesFailure (){
        AdventureCard card = getActiveCard();
        return ((Pirates) card).EffectFailure();
    }

    /** function to removeEnergy to utilize in case of not having enough cargo
     * @apiNote controller needs to verify how much energy the player has to remove, when cargo are insufficient
     * @param p player that needs to remove the energy
     * @param energy list of energy to remove from the ship
     */
    public void removeEnergy (Player p, List<Battery> energy){
        for (Battery e : energy){
            p.getShip().useEnergy(e);
        }
    }

    /** function that verify if the cargo that the player want to remove are the most valued one
     *
     * @param p player that want to remove cargo
     * @param l list of cargo to remove
     * @return true if the list is valid, false if is not valid
     */
    public Boolean verifyCargo (Player p, List<CargoHold> l){
        int redCounter = 0;
        int blueCounter = 0;
        int yellowCounter = 0;
        int greenCounter = 0;

        for (CargoHold cargohold : l){
            int red = cargohold.getCargoHeld(CargoColor.RED);
            int blue = cargohold.getCargoHeld(CargoColor.BLUE);
            int yellow = cargohold.getCargoHeld(CargoColor.YELLOW);
            int green = cargohold.getCargoHeld(CargoColor.GREEN);

            if (red > 0) {
                redCounter++;
            } else if (blue > 0) {
                blueCounter++;
            } else if (yellow > 0) {
                yellowCounter++;
            } else if (green > 0) {
                greenCounter++;
            }
        }
        Map<CargoColor, Integer> totalCargo = p.getShip().getCargo();
        int totalRed = totalCargo.getOrDefault(CargoColor.RED, 0);
        int totalBlue = totalCargo.getOrDefault(CargoColor.BLUE, 0);
        int totalYellow = totalCargo.getOrDefault(CargoColor.YELLOW, 0);
        int totalGreen = totalCargo.getOrDefault(CargoColor.GREEN, 0);
        for (int i=0; i<l.size(); i++){
            if (totalRed>0){
                totalRed--;
                redCounter--;
                if (redCounter<0) {return false;}
            } else if (totalBlue>0) {
                totalBlue--;
                blueCounter--;
                if (blueCounter<0) {return false;}
            }else if (totalYellow>0){
                totalYellow--;
                yellowCounter--;
                if (yellowCounter<0) {return false;}
            } else if (totalGreen>0){
                totalGreen--;
                greenCounter--;
                if (greenCounter<0) {return false;}
            }
        }
        return true;
    }

    //TODO gestione rimozione cargo insufficienti (il controller verica se mancano e chiama il metodo per rimuovere l'energia)
    //TODO metodi per gestione sceglie di ritirarsi (nel controller)
    //TODO gestione creazione dei deck (da vedere con json)
    //TODO capire la condizione per aggiungere gli alieni alla ship servirebbe una condizione tipo se puo hostare ancora un alieno ma in ship non ho nulla
    //TODO controller deve mandare cargo da scaricare in ordine di valore prendendo sempre quelli che valgono di più
}


