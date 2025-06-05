package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.common.message_protocol.toclient.*;
import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.controller.managers.FireManager;
import it.polimi.ingsw.gc20.server.controller.managers.Translator;
import it.polimi.ingsw.gc20.server.exceptions.*;
import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.server.model.cards.Projectile;
import it.polimi.ingsw.gc20.server.model.components.Battery;
import it.polimi.ingsw.gc20.server.model.components.Cabin;
import it.polimi.ingsw.gc20.server.model.components.Cannon;
import it.polimi.ingsw.gc20.server.model.components.Shield;
import it.polimi.ingsw.gc20.server.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.server.model.player.Player;
import it.polimi.ingsw.gc20.server.model.ship.Ship;
import org.javatuples.Pair;

import java.util.*;

@SuppressWarnings("unused") // dynamically created by Cards
public class CombatZone0State extends PlayingState {
    private final int lostDays;
    private int lostCrew;
    private final List<Projectile> cannonFires;
    private final Map<String, Float> declaredFirepower;
    private final Map<String, Integer> declaredEnginePower;
    private FireManager manager;
    private int result;
    /**
     * Default constructor
     */
    @SuppressWarnings("unused") // dynamically created by Cards
    public CombatZone0State(GameModel model, GameController controller, AdventureCard card) {
        super(model, controller);
        this.lostDays = card.getLostDays();
        this.lostCrew = card.getCrew();
        this.cannonFires = card.getProjectiles();
        this.declaredFirepower = new HashMap<>();
        this.declaredEnginePower = new HashMap<>();
        for (Player player : getModel().getInGamePlayers()) {
            declaredFirepower.put(player.getUsername(), 0f);
            declaredEnginePower.put(player.getUsername(), 0);
        }
        //notify the players that they are in the automatic action phase
        getController().getMessageManager().broadcastPhase(new AutomaticActionMessage("finding the player with the minimum crew"));
        this.manager = null;
        try {
            Thread.sleep(5000); // Sleep for 5 seconds (5000 milliseconds)
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        this.phase = StatePhase.AUTOMATIC_ACTION;
        this.automaticAction();
    }

    /** this method is used to check the player with the minimum crew and make him lose the days
     */
    @Override
    public void automaticAction() {
        //find the player with the minimum crew
        Player p = getController().getInGameConnectedPlayers().stream()
                .map(pl -> getController().getPlayerByID(pl))
                .min(Comparator.comparingInt(pl -> pl.getShip().crew()))
                .orElseThrow(() -> new RuntimeException("Error"));
        //move the player
        getModel().movePlayer(p, -lostDays);
        //notify all players connected with an update player message
        getController().getMessageManager().broadcastUpdate(new PlayerUpdateMessage(p.getUsername(), 0, p.isInGame(), p.getColor(), (p.getPosition()%getModel().getGame().getBoard().getSpaces())));
        //set the phase to engine phase
        this.phase = StatePhase.ENGINES_PHASE;
        setStandbyMessage(p.getUsername() + " is activating engines");
        //notify the first player that he has to activate the engines and the others that they have to wait
        getController().getMessageManager().notifyPhaseChange(phase, this);
    }

    /**
     * This method activates the cannons that the player declared
     * @param player who selected the cannons
     * @param cannons that the player wants to activate
     * @param batteries that the player wants to use to activate the double cannon
     * @throws InvalidTurnException if it's not the player's turn
     * @throws InvalidStateException if the player is not in the correct phase
     * @throws InvalidCannonException if a cannon is not valid
     * @throws EnergyException if the player doesn't have enough energy
     */
    @Override
    public void activateCannons(Player player, List<Pair<Integer, Integer>> cannons, List<Pair<Integer, Integer>> batteries) throws InvalidStateException, InvalidTurnException, EnergyException, InvalidCannonException, ComponentNotFoundException{
        //check if the player is in the right phase
        if(phase != StatePhase.CANNONS_PHASE) {
            throw new InvalidStateException("You cannot activate cannons now");
        }
        //check if the player is in the right turn
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        //translate the coordinates to the components
        Set<Cannon> cannonsComponents = new HashSet<>();
        if(Translator.getComponentAt(player, cannons, Cannon.class) != null) {
            cannonsComponents = new HashSet<>(Translator.getComponentAt(player, cannons, Cannon.class));
        }
        List<Battery> batteriesComponents = new ArrayList<>();
        if (Translator.getComponentAt(player, batteries, Battery.class)!=null)
            batteriesComponents.addAll(Translator.getComponentAt(player, batteries, Battery.class));
        //calculate the firepower that the player declared
        float firepower = getModel().FirePower(player, cannonsComponents, batteriesComponents);
        declaredFirepower.put(player.getUsername(), firepower);
        //pass the turn to the next player
        nextPlayer();
        //check if all the players declared their firepower
        if (getCurrentPlayer() == null) {
            //remove from declaredFirePower the players that are not in getInGameConnectedPlayers
            declaredFirepower.keySet().removeIf(p -> !getController().getInGameConnectedPlayers().contains(p));
            //find the player with the minimum firepower
            Player p = getController().getPlayerByID(declaredFirepower.entrySet()
                    .stream()
                    .min(Map.Entry.comparingByValue())
                    .orElseThrow(() -> new RuntimeException("Error"))
                    .getKey());
            //set the phase to the roll dice phase
            phase = StatePhase.ROLL_DICE_PHASE;
            //set the current player to the player with the minimum firepower
            setCurrentPlayer(p.getUsername());
            manager = new FireManager(getModel(), cannonFires, p);
            setStandbyMessage(p.getUsername() + " is rolling the dice");
            //notify the current player that he has to roll the dice; others will go to the standby phase
            getController().getMessageManager().notifyPhaseChange(phase, this);
        } else {
            setStandbyMessage(getCurrentPlayer() + " is activating cannons");
            //notify the next player that he has to activate the cannons and the others that they have to wait
            getController().getMessageManager().notifyPhaseChange(phase, this);
        }

    }

    /**
     * this method is used to roll the dice
     * @param player who needs to roll the dice
     * @return the result of the roll
     * @throws InvalidStateException if the player is not in the correct phase
     * @throws InvalidTurnException if it's not the player's turn
     */
    @Override
    public int rollDice(Player player) throws InvalidStateException, InvalidTurnException{
        //check if the player is in the right turn
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        //check if the player is in the right phase
        if (phase != StatePhase.ROLL_DICE_PHASE) {
            throw new InvalidStateException("You cannot roll dice now");
        }
        //roll the dice
        result = getModel().getGame().rollDice();
        //check what type is the first projectile
        switch (manager.getFirstProjectile()) {
            case LIGHT_FIRE, LIGHT_METEOR:
                phase = StatePhase.SELECT_SHIELD;
                setStandbyMessage(getCurrentPlayer() + " is selecting shield");
                //notify the current player that he has to select the shield
                getController().getMessageManager().notifyPhaseChange(phase, this);
                break;
            case HEAVY_METEOR:
                phase = StatePhase.CANNONS_PHASE;
                break;
            case HEAVY_FIRE:
                phase = StatePhase.AUTOMATIC_ACTION;
                //if the projectile is heavy fire, the player can't do anything to stop it
                try {
                    //fire the projectile
                    manager.fire();
                    //check if we finished shooting
                    finishManager();
                } catch (InvalidShipException e) {
                    //notify all the players of the ship update
                    getController().getMessageManager().broadcastUpdate(Ship.messageFromShip(player.getUsername(), player.getShip(), "destroyed a component"));
                    phase = StatePhase.VALIDATE_SHIP_PHASE;
                    setStandbyMessage(getCurrentPlayer() + " is validating ship");
                    //notify the current player that he has to choose the branch
                    getController().getMessageManager().notifyPhaseChange(phase, this);
                } catch (DieNotRolledException _){
                    //cannot happen
                }
                break;
            case null:
                //if the projectile is null, we can draw a new card
                getModel().getActiveCard().playCard();
                phase = StatePhase.DRAW_CARD_PHASE;
                //to notify all connected players that the player finished shooting,
                // we will wait for the next card to be drawn
                getController().getMessageManager().broadcastPhase(new DrawCardPhaseMessage());
                getController().setState(new PreDrawState(getController()));
                break;
        }
        return result;
    }

    private void finishManager() {
        if (manager.finished()){
            //if we finished shooting, we can draw a new card
            phase = StatePhase.DRAW_CARD_PHASE;
            //to notify all connected players that the player finished shooting,
            // we will wait for the next card to be drawn
            getController().getMessageManager().broadcastPhase(new DrawCardPhaseMessage());
            getModel().getActiveCard().playCard();
            getController().setState(new PreDrawState(getController()));
        } else {
            //if we didn't finish shooting, we have to roll the dice again
            phase = StatePhase.ROLL_DICE_PHASE;
            setStandbyMessage(getCurrentPlayer() + " is rolling the dice");
            //notify the current player that he has to roll the dice again
            getController().getMessageManager().notifyPhaseChange(phase, this);
        }
    }

    /**
     * this method is used to lose the crew of the player
     * @param player who needs to lose the crew
     * @param cabins that the player wants to lose
     * @throws InvalidTurnException if it's not the player's turn
     * @throws EmptyCabinException if the cabin is empty
     * @throws InvalidStateException if the player is not in the correct phase
     */
    @Override
    public void loseCrew(Player player, List<Pair<Integer, Integer>> cabins) throws InvalidTurnException, EmptyCabinException, InvalidStateException, ComponentNotFoundException {
        //check if we are in the right phase
        if (phase != StatePhase.LOSE_CREW_PHASE) {
            throw new InvalidStateException("You cannot remove crew now");
        }
        if (player.getUsername().equals(getCurrentPlayer())) {
            //check if the player has enough crew to lose
            if (player.getShip().crew() < lostCrew) {
                //if the player doesn't have enough crew, we set the lostCrew to the crew of the player
                lostCrew = player.getShip().crew();
            }
            //check if the player has selected enough cabins
            if (cabins.size() != lostCrew) {
                throw new InvalidStateException("You didn't select enough cabins");
            }
            getModel().loseCrew(player, Translator.getComponentAt(player, cabins, Cabin.class));
            lostCrew -= cabins.size();
            //check if the player has lost the necessary crew
            if (lostCrew == 0) {
                setCannonPhase();
            }
        } else {
            throw new InvalidTurnException("It's not your turn");
        }
    }

    @Override
    public String toString() {
        return "CombatZone0State{" +
                "lostDays=" + lostDays +
                ", lostCrew=" + lostCrew +
                ", cannonFires=" + cannonFires +
                ", declaredFirepower=" + declaredFirepower +
                ", declaredEnginePower=" + declaredEnginePower +
                '}';
    }

    /**
     * this method is used to activate the engines of the player
     * @param player who selected the engines
     * @param engines that the player wants to activate
     * @param batteries that the player wants to use to activate the double engine
     * @throws InvalidTurnException if it's not the player's turn
     * @throws InvalidStateException if the player is not in the correct phase
     * @throws InvalidEngineException if an engine is not valid
     * @throws EnergyException if the player doesn't have enough energy
     */
    @Override
    public void activateEngines(Player player, List<Pair<Integer, Integer>> engines, List<Pair<Integer, Integer>> batteries) throws InvalidStateException, InvalidTurnException, EnergyException, InvalidEngineException, ComponentNotFoundException{
        //check if the player is in the right phase
        if(phase != StatePhase.ENGINES_PHASE) {
            throw new InvalidStateException("You cannot activate engines now");
        }
        //check if the player is in the right turn
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        //translate the coordinates to the components and calculate the engine power
        int enginePower = getModel().EnginePower(player, engines.size(), Translator.getComponentAt(player, batteries, Battery.class));
        //save the engine power in the declaredEnginePower map
        declaredEnginePower.put(player.getUsername(), enginePower);

        //pass the turn to the next player
        nextPlayer();
        //check if all the players declared their engine power
        if (getCurrentPlayer() == null) {
            //remove from declaredEnginePower the players that are not in getInGameConnectedPlayers
            declaredEnginePower.keySet().removeIf(p -> !getController().getInGameConnectedPlayers().contains(p));
            //find the player with the minimum Engine power
            Player p = getController().getPlayerByID(declaredEnginePower.entrySet()
                    .stream()
                    .min(Map.Entry.comparingByValue())
                    .orElseThrow(() -> new RuntimeException("Error"))
                    .getKey());
            //set the phase to the LOSE_CREW_PHASE
            phase = StatePhase.LOSE_CREW_PHASE;
            //set the current player to the player with the minimum Engine power
            setCurrentPlayer(p.getUsername());
            setStandbyMessage(p.getUsername() + " is losing crew");
            //notify the current player that he has to lose crew; others will go to the standby phase
            getController().getMessageManager().notifyPhaseChange(phase, this);

        } else {
            phase = StatePhase.ENGINES_PHASE;
            setStandbyMessage(getCurrentPlayer() + " is activating engines");
            //notify the next player that he has to activate the engines and the others that they have to wait
            getController().getMessageManager().notifyPhaseChange(phase, this);
        }
    }

    /**
     * This method is used to activate the shield of the player
     * @param player who selected the shield
     * @param shield that the player wants to activate
     * @param battery that the player wants to use to activate the shield
     * @throws InvalidTurnException if it's not the player's turn
     * @throws EnergyException if the player doesn't have enough energy
     * @throws InvalidStateException if the player is not in the correct phase
     */
    @Override
    public void activateShield(Player player, Pair<Integer, Integer> shield, Pair<Integer, Integer> battery) throws ComponentNotFoundException, InvalidTurnException, InvalidStateException, EnergyException {
        //check if the player is in the right turn
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        //check if the player is in the right phase
        if (phase!=StatePhase.SELECT_SHIELD) {
            throw new InvalidStateException("You cannot activate shield now");
        }
        //use the shield and battery to activate the shield
        try {
            manager.activateShield(Translator.getComponentAt(player, shield, Shield.class), Translator.getComponentAt(player, battery, Battery.class));
            //fire the projectile
                manager.fire();
                if (manager.finished()) {
                    //notify all connected players that the player finished shooting;
                    // we will wait for the next card to be drawn
                    getController().getMessageManager().broadcastPhase(new DrawCardPhaseMessage());
                    phase = StatePhase.DRAW_CARD_PHASE;
                    getModel().getActiveCard().playCard();
                    getController().setState(new PreDrawState(getController()));
                } else {
                    phase = StatePhase.ROLL_DICE_PHASE;
                    setStandbyMessage(getCurrentPlayer() + " is rolling the dice");
                    //notify the current player that he has to roll the dice again
                    getController().getMessageManager().notifyPhaseChange(phase, this);
                }
        } catch (InvalidShipException e) {
            //notify all the players of the ship update
            getController().getMessageManager().broadcastUpdate(Ship.messageFromShip(player.getUsername(), player.getShip(), "destroyed a component"));
            //notify the current player that he has to choose the branch
            phase = StatePhase.VALIDATE_SHIP_PHASE;
            setStandbyMessage(getCurrentPlayer() + " is validating ship");
            getController().getMessageManager().notifyPhaseChange(phase, this);
        } catch (DieNotRolledException e) {
            //cannot happen
        }
    }

    /**
     * This method is used to choose the branch of the ship
     * @param player who selected the branch
     * @param coordinates of the branch that the player wants to select
     * @throws InvalidTurnException if it's not the player's turn
     * @throws InvalidStateException if the player is not in the correct phase
     */
    @Override
    public void chooseBranch(Player player, Pair<Integer, Integer> coordinates) throws InvalidTurnException, InvalidStateException {
        //check if the player is in the right turn
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        //check if the player is in the right phase
        if (phase != StatePhase.VALIDATE_SHIP_PHASE) {
            throw new InvalidStateException("You cannot choose branch now");
        }
        //choose the branch selected by the player
        manager.chooseBranch(player, coordinates);
        //check if the fire manager finished
        finishManager();
    }

    /**
     * This method is used when a player quits the game
     * @param player who quits
     */
    @Override
    public void currentQuit(Player player){
        //first, we check if we are in a penality phase:
        // LOSE_CREW_PHASE or ROLL_DICE_PHASE, ACTIVATE_SHIELD or VALIDATE SHIP
        if (phase == StatePhase.LOSE_CREW_PHASE){
            setCannonPhase();
        }
        else if (phase == StatePhase.ROLL_DICE_PHASE || phase == StatePhase.SELECT_SHIELD){
            //we end the card
            phase = StatePhase.DRAW_CARD_PHASE;
            //to notify all connected players that the player finished shooting,
            // we will wait for the next card to be drawn
            getController().getMessageManager().broadcastPhase(new DrawCardPhaseMessage());
            getModel().getActiveCard().playCard();
            getController().setState(new PreDrawState(getController()));
        }
        else if (phase == StatePhase.VALIDATE_SHIP_PHASE){
            try {
                //we auto choose the branch
                chooseBranch(player, new Pair<>(-1, -1));
                //notify all the players of the ship update
                getController().getMessageManager().broadcastUpdate(Ship.messageFromShip(player.getUsername(), player.getShip(), "destroyed a component"));
                if (phase != StatePhase.DRAW_CARD_PHASE){
                    //to notify all connected players that the player finished shooting,
                    // we will wait for the next card to be drawn
                    getController().getMessageManager().broadcastPhase(new DrawCardPhaseMessage());
                    phase = StatePhase.DRAW_CARD_PHASE;
                    //if we are not in the standby phase, we can draw a new card
                    getModel().getActiveCard().playCard();
                    getController().setState(new PreDrawState(getController()));
                }
            } catch (InvalidTurnException | InvalidStateException _) {
                //ignore
            }
        }else {
            //if we are not in a penality phase, we can pass the turn to the next player
            nextPlayer();
            if (getCurrentPlayer() == null) {
                //we need to verify based on the phase
                if (phase == StatePhase.ENGINES_PHASE) {
                    try {
                        //we auto-activate the engines,
                        // and the player will be removed from the array in the activateEngines method
                        activateEngines(player, new ArrayList<>(), new ArrayList<>());
                    } catch (InvalidTurnException | InvalidStateException | EnergyException | InvalidEngineException |
                             ComponentNotFoundException e) {
                        //ignore
                    }
                } else if (phase == StatePhase.CANNONS_PHASE) {
                    try {
                        //we auto-activate the cannons,
                        // and the player will be removed from the array in the activateCannons method
                        activateCannons(player, new ArrayList<>(), new ArrayList<>());
                    } catch (InvalidTurnException | InvalidStateException | EnergyException | InvalidCannonException |
                             ComponentNotFoundException e) {
                        //ignore
                    }
                }
            }  else {
                if (phase == StatePhase.ENGINES_PHASE) {
                    setStandbyMessage(getCurrentPlayer() + " is activating engines");
                    getController().getMessageManager().notifyPhaseChange(phase, this);
                } else if (phase == StatePhase.CANNONS_PHASE) {
                    setStandbyMessage(getCurrentPlayer() + " is activating cannons");
                    getController().getMessageManager().notifyPhaseChange(phase, this);
                }
            }
        }
    }

    private void setCannonPhase(){
        phase = StatePhase.CANNONS_PHASE;
        setStandbyMessage(getCurrentPlayer() + " is activating cannons");
        //set the current player to the first online player
        setCurrentPlayer(getController().getFirstOnlinePlayer());
        //notify the first player that he has to activate the cannons and the others that they have to wait
        getController().getMessageManager().notifyPhaseChange(phase, this);
    }

    @Override
    public String createsCannonsMessage() {
        //write the message to notify the next player that he has to activate the cannons and show him the declared firepower of the previous players
        StringBuilder message = new StringBuilder("You are the next player, select the cannons to use. The declared firepower of the previous players are: ");
        for (Map.Entry<String, Float> entry : declaredFirepower.entrySet()) {
            message.append(entry.getKey()).append(": ").append(entry.getValue()).append(", ");
        }
        return message.toString();
    }

    @Override
    public String createsEnginesMessage() {
        //write the message to notify the next player that he has to activate the engines and show him the declared engine power of the previous players
        StringBuilder message = new StringBuilder("You are the next player, select the engines to use. The declared engine power of the previous players are: ");
        for (Map.Entry<String, Integer> entry : declaredEnginePower.entrySet()) {
            message.append(entry.getKey()).append(": ").append(entry.getValue()).append(", ");
        }
        return message.toString();
    }

    @Override
    public int getCrew(){
        //return the crew that the player has to lose
        return lostCrew;
    }

    @Override
    public String createsShieldMessage() {
        return "a " + manager.getFirstProjectile().getFireType() + " is coming, from the " + manager.getFirstDirection().getDirection() + "side at line" + result + ", select the shield to use";
    }

    @Override
    public String createsRollDiceMessage() {
        return "a " + manager.getFirstProjectile().getFireType() + " is coming, from the " + manager.getFirstDirection().getDirection() + "side, roll the dice to see where it hits";
    }
}