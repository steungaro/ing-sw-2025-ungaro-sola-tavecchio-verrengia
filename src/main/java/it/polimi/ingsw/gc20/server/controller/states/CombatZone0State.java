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

/**
 * Represents the CombatZone0State in the game, which is a specific playing state where
 * players engage in combat actions such as activating cannons, engines, and shields,
 * rolling dice, and managing crew losses. This state is dynamically created during the game
 * and integrates with the game model, controller, and adventure card mechanics.
 */
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
     * Constructs a CombatZone0State object. This initializes the state with the provided
     * game model, controller, and adventure card, setting the initial phase to AUTOMATIC_ACTION.
     * It also configures the standby message for the current player and notifies the message manager
     * of the phase change. The method waits for 5 seconds before proceeding to the automatic action.
     *
     * @param model      the game model that provides the data structure and logic for the game
     * @param controller the game controller that manages game flow and interactions
     * @param card       the adventure card associated with this game state
     */
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
        getController().getMessageManager().broadcastPhase(new AutomaticActionMessage("Finding the player with the minimum crew..."));
        this.manager = null;
        try {
            Thread.sleep(5000); // Sleep for 5 seconds (5000 milliseconds)
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        this.phase = StatePhase.AUTOMATIC_ACTION;
        this.automaticAction();
    }

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
        getController().getMessageManager().broadcastUpdate(new PlayerUpdateMessage(p.getUsername(), 0, p.isInGame(), p.getColor(), (p.getPosition() % getModel().getGame().getBoard().getSpaces() + getModel().getGame().getBoard().getSpaces()) % getModel().getGame().getBoard().getSpaces()));
        //set the phase to engine phase
        this.phase = StatePhase.ENGINES_PHASE;
        setStandbyMessage(p.getUsername() + " is activating engines.");
        //notify the first player that he has to activate the engines and the others that they have to wait
        getController().getMessageManager().notifyPhaseChange(phase, this);
    }

    @Override
    public void activateCannons(Player player, List<Pair<Integer, Integer>> cannons, List<Pair<Integer, Integer>> batteries) throws InvalidStateException, InvalidTurnException, EnergyException, InvalidCannonException, ComponentNotFoundException, InvalidShipException, DieNotRolledException {
        //check if the player is in the right phase
        if(phase != StatePhase.CANNONS_PHASE) {
            throw new InvalidStateException("You cannot activate cannons now.");
        }
        //check if the player is in the right turn
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn!");
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
        super.activateCannons(player, cannons, batteries);
        float firepower = getModel().firePower(player, cannonsComponents, batteriesComponents);
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
            setStandbyMessage(p.getUsername() + " is rolling the dice.");
            //notify the current player that he has to roll the dice; others will go to the standby phase
            getController().getMessageManager().notifyPhaseChange(phase, this);
        } else {
            setStandbyMessage(getCurrentPlayer() + " is activating cannons.");
            //notify the next player that he has to activate the cannons and the others that they have to wait
            getController().getMessageManager().notifyPhaseChange(phase, this);
        }

    }

    @Override
    public int rollDice(Player player) throws InvalidStateException, InvalidTurnException{
        //check if the player is in the right turn
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn!");
        }
        //check if the player is in the right phase
        if (phase != StatePhase.ROLL_DICE_PHASE) {
            throw new InvalidStateException("You cannot roll dice now.");
        }
        //roll the dice
        result = getModel().getGame().rollDice();
        //check what type is the first projectile
        switch (manager.getFirstProjectile()) {
            case LIGHT_FIRE, LIGHT_METEOR:
                phase = StatePhase.SELECT_SHIELD;
                setStandbyMessage(getCurrentPlayer() + " is selecting the shields to activate.");
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
                    setStandbyMessage(getCurrentPlayer() + " is validating their ship.");
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

    /**
     * This method is used to finish the fire manager and check if the player finished shooting.
     * If the player finished shooting, we can draw a new card, otherwise we have to roll the dice again.
     */
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
            setStandbyMessage(getCurrentPlayer() + " is rolling the dice.");
            //notify the current player that he has to roll the dice again
            getController().getMessageManager().notifyPhaseChange(phase, this);
        }
    }

    @Override
    public void loseCrew(Player player, List<Pair<Integer, Integer>> cabins) throws InvalidTurnException, EmptyCabinException, InvalidStateException, ComponentNotFoundException {
        //check if we are in the right phase
        if (phase != StatePhase.LOSE_CREW_PHASE) {
            throw new InvalidStateException("You cannot remove crew now.");
        }
        if (player.getUsername().equals(getCurrentPlayer())) {
            //check if the player has enough crew to lose
            if (player.getShip().crew() < lostCrew) {
                //if the player doesn't have enough crew, we set the lostCrew to the crew of the player
                lostCrew = player.getShip().crew();
            }
            //check if the player has selected enough cabins
            if (cabins.size() != lostCrew) {
                throw new InvalidStateException("Invalid number of cabins selected, you need to select exactly " + lostCrew + " cabins.");
            }
            getModel().loseCrew(player, Translator.getComponentAt(player, cabins, Cabin.class));
            getController().getMessageManager().broadcastUpdate(Ship.messageFromShip(player.getUsername(), player.getShip(), "lost crew"));
            lostCrew -= cabins.size();
            //check if the player has lost the necessary crew
            if (lostCrew == 0) {
                setCannonPhase();
            }
        } else {
            throw new InvalidTurnException("It's not your turn!");
        }
    }

    @Override
    public void activateEngines(Player player, List<Pair<Integer, Integer>> engines, List<Pair<Integer, Integer>> batteries) throws InvalidStateException, InvalidTurnException, EnergyException, InvalidEngineException, ComponentNotFoundException{
        //check if the player is in the right phase
        if(phase != StatePhase.ENGINES_PHASE) {
            throw new InvalidStateException("You cannot activate engines now.");
        }
        //check if the player is in the right turn
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn!");
        }
        //translate the coordinates to the components and calculate the engine power
        int enginePower = getModel().enginePower(player, engines.size(), Translator.getComponentAt(player, batteries, Battery.class));
        getController().getMessageManager().broadcastUpdate(Ship.messageFromShip(player.getUsername(), player.getShip(), "activated engines"));
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
            setStandbyMessage(p.getUsername() + " is choosing the cabins to lose crew from.");
            //notify the current player that he has to lose crew; others will go to the standby phase
            getController().getMessageManager().notifyPhaseChange(phase, this);

        } else {
            phase = StatePhase.ENGINES_PHASE;
            setStandbyMessage(getCurrentPlayer() + " is activating engines.");
            //notify the next player that he has to activate the engines and the others that they have to wait
            getController().getMessageManager().notifyPhaseChange(phase, this);
        }
    }

    @Override
    public void activateShield(Player player, Pair<Integer, Integer> shield, Pair<Integer, Integer> battery) throws ComponentNotFoundException, InvalidTurnException, InvalidStateException, EnergyException {
        //check if the player is in the right turn
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn!");
        }
        //check if the player is in the right phase
        if (phase!=StatePhase.SELECT_SHIELD) {
            throw new InvalidStateException("You cannot activate shield now.");
        }
        //use the shield and battery to activate the shield
        try {
            manager.activateShield(Translator.getComponentAt(player, shield, Shield.class), Translator.getComponentAt(player, battery, Battery.class));
            getController().getMessageManager().broadcastUpdate(Ship.messageFromShip(player.getUsername(), player.getShip(), "activated shield"));
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
                    setStandbyMessage(getCurrentPlayer() + " is rolling the dice.");
                    //notify the current player that he has to roll the dice again
                    getController().getMessageManager().notifyPhaseChange(phase, this);
                }
        } catch (InvalidShipException e) {
            //notify all the players of the ship update
            getController().getMessageManager().broadcastUpdate(Ship.messageFromShip(player.getUsername(), player.getShip(), "destroyed a component"));
            //notify the current player that he has to choose the branch
            phase = StatePhase.VALIDATE_SHIP_PHASE;
            setStandbyMessage(getCurrentPlayer() + " is validating their ship.");
            getController().getMessageManager().notifyPhaseChange(phase, this);
        } catch (DieNotRolledException e) {
            //cannot happen
        }
    }

    @Override
    public void chooseBranch(Player player, Pair<Integer, Integer> coordinates) throws InvalidTurnException, InvalidStateException {
        //check if the player is in the right turn
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn!");
        }
        //check if the player is in the right phase
        if (phase != StatePhase.VALIDATE_SHIP_PHASE) {
            throw new InvalidStateException("You cannot choose a branch now.");
        }
        //choose the branch selected by the player
        manager.chooseBranch(player, coordinates);
        getController().getMessageManager().broadcastUpdate(Ship.messageFromShip(player.getUsername(), player.getShip(), "chose branch"));
        //check if the fire manager finished
        finishManager();
    }

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
            if (phase == StatePhase.ENGINES_PHASE){
                try{
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
                         ComponentNotFoundException | InvalidShipException | DieNotRolledException e) {
                    //ignore
                }
            }
        }
    }

    /**
     * Sets the phase to CANNONS_PHASE, updates the standby message, and notifies the message manager
     * that the current player is activating cannons.
     * This method is called when all players have declared their firepower and the game is ready for
     * cannon activation.
     */
    private void setCannonPhase(){
        phase = StatePhase.CANNONS_PHASE;
        setStandbyMessage(getCurrentPlayer() + " is activating cannons.");
        //set the current player to the first online player
        setCurrentPlayer(getController().getFirstOnlinePlayer());
        //notify the first player that he has to activate the cannons and the others that they have to wait
        getController().getMessageManager().notifyPhaseChange(phase, this);
    }

    @Override
    public String createsCannonsMessage() {
        //write the message to notify the next player that he has to activate the cannons and show him the declared firepower of the previous players
        StringBuilder message = new StringBuilder("It's your turn, please select the cannons to activate. The declared firepower of the other players is: \n");
        for (Map.Entry<String, Float> entry : declaredFirepower.entrySet()) {
            message.append(" - ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        return message.toString();
    }

    @Override
    public String createsEnginesMessage() {
        //write the message to notify the next player that he has to activate the engines and show him the declared engine power of the previous players
        StringBuilder message = new StringBuilder("It's your turn, please select the engines to activate. The declared engine power of the other players is: ");
        for (Map.Entry<String, Integer> entry : declaredEnginePower.entrySet()) {
            message.append(" - ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
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
        return "A " + manager.getFirstProjectile().getFireType() + " is coming from the " + manager.getFirstDirection().getDirection() + " side at line " + result + ", select the shield to activate.";
    }

    @Override
    public String createsRollDiceMessage() {
        return "A " + manager.getFirstProjectile().getFireType() + " is coming from the " + manager.getFirstDirection().getDirection() + " side, roll the dice to see where it will hit.";
    }
}