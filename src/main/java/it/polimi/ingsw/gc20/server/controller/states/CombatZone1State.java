package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.common.message_protocol.toclient.*;
import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.controller.managers.FireManager;
import it.polimi.ingsw.gc20.server.controller.managers.Translator;
import it.polimi.ingsw.gc20.server.exceptions.*;
import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.server.model.cards.Projectile;
import it.polimi.ingsw.gc20.server.model.components.Battery;
import it.polimi.ingsw.gc20.server.model.components.Cannon;
import it.polimi.ingsw.gc20.server.model.components.Shield;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.server.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.server.model.player.Player;
import it.polimi.ingsw.gc20.server.model.ship.Ship;
import org.javatuples.Pair;

import java.util.*;

/**
 * Represents the CombatZone0 in the game, where players engage in combat
 * by activating cannons and engines, rolling dice, and managing cargo. This state is
 * dynamically created during the game and integrates with the game model, controller,
 * and adventure card mechanics.
 */
@SuppressWarnings("unused") // dynamically created by Cards
public class  CombatZone1State extends CargoState {
    private final int lostDays;
    private int lostCargo;
    private final List<Projectile> cannonFires;
    private final Map<String, Float> declaredFirepower;
    private final Map<String, Integer> declaredEnginePower;
    private boolean removingCargo;
    private FireManager manager;
    int result;

    /**
     * Constructs a CombatZone1State object. This initializes the state with the provided
     * game model, controller, and adventure card. It sets the initial phase to CANNONS_PHASE,
     * initializes the lost days and cargo, and prepares the declared firepower and engine power maps.
     * It also configures the standby message for the current player and notifies the
     * message manager of the phase change.
     *
     * @param model      the game model that provides the data structure and logic for the game
     * @param controller the game controller that manages game flow and interactions
     * @param card       the adventure card associated with this game state
     */
    public CombatZone1State(GameModel model, GameController controller, AdventureCard card) {
        super(model, controller);
        this.lostDays = card.getLostDays();
        this.lostCargo = card.getLostCargo();
        this.cannonFires = card.getProjectiles();
        this.declaredFirepower = new HashMap<>();
        this.declaredEnginePower = new HashMap<>();
        for (Player player : getModel().getInGamePlayers()) {
            declaredFirepower.put(player.getUsername(), 0f);
            declaredEnginePower.put (player.getUsername(), 0);
        }
        this.removingCargo = false;
        this.manager = null;
        this.phase = StatePhase.CANNONS_PHASE;
        //notify the first player that he has to select the cannons
        setStandbyMessage(getCurrentPlayer() + " is activating the cannons.");
        getController().getMessageManager().notifyPhaseChange(phase, this);
    }

    @Override
    public void automaticAction() {
        //find the player with the minimum crew
        Player player = getController().getInGameConnectedPlayers().stream()
                .map(p -> getController().getPlayerByID(p))
                .min(Comparator.comparingInt(p -> p.getShip().crew()))
                .orElseThrow(() -> new RuntimeException("Error"));
        //create the fire manager
        manager = new FireManager(getModel(), cannonFires, player);
        //set the current player to the player with the minimum crew
        setCurrentPlayer(player.getUsername());
        //set the phase to the roll dice phase
        phase = StatePhase.ROLL_DICE_PHASE;
        setStandbyMessage(getCurrentPlayer() + " is rolling the dice.");
        //notify the current player that he has to roll the dice; others will go to the standby phase
        getController().getMessageManager().notifyPhaseChange(phase, this);
    }

    @Override
    public void activateCannons(Player player, List<Pair<Integer, Integer>> cannons, List<Pair<Integer, Integer>> batteries) throws InvalidTurnException, InvalidStateException, EnergyException, InvalidCannonException, ComponentNotFoundException, InvalidShipException, DieNotRolledException {
        // check if the player is the current player
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn!");
        }
        // check if the player is in the cannon phase
        if(phase != StatePhase.CANNONS_PHASE) {
            throw new InvalidStateException("Not in cannon phase.");
        }
        // translate the coordinates to the components
        Set<Cannon> cannonsComponents = new HashSet<>();
        List<Battery> batteriesComponents = new ArrayList<>();
        if(Translator.getComponentAt(player, cannons, Cannon.class)!=null)
            cannonsComponents.addAll(Translator.getComponentAt(player, cannons, Cannon.class));
        if(Translator.getComponentAt(player, batteries, Battery.class)!=null)
            batteriesComponents.addAll(Translator.getComponentAt(player, batteries, Battery.class));
        //save the declared firepower in the map
        float firepower = getModel().firePower(player, cannonsComponents, batteriesComponents);
        declaredFirepower.put(player.getUsername(), firepower);
        super.activateCannons(player, cannons, batteries);
        //pass the turn to the next player
        nextPlayer();
        //check if the current player is null, if so, it means that all players have played
        if (getCurrentPlayer() == null) {
            //if all played, remove from declaredFirePower the players that are not in getInGameConnectedPlayers
            declaredFirepower.keySet().removeIf(p -> !getController().getInGameConnectedPlayers().contains(p));
            Player p = getController().getPlayerByID(declaredFirepower.entrySet()
                    .stream()
                    .min(Map.Entry.comparingByValue())
                    .orElseThrow(() -> new RuntimeException("Error"))
                    .getKey());
            //get the player with the minimum declaredFirePower and make it lose flight days
            getModel().movePlayer(p, - lostDays);
            //notify all connected players of the player who lost flight days
            getController().getMessageManager().broadcastPhase(new PlayerUpdateMessage(p.getUsername(), 0, true, p.getColor(), (p.getPosition() % getModel().getGame().getBoard().getSpaces() + getModel().getGame().getBoard().getSpaces()) % getModel().getGame().getBoard().getSpaces()));
            //set the current player to the first online player
            setCurrentPlayer(getController().getFirstOnlinePlayer());
            //set the phase to the next phase
            phase = StatePhase.ENGINES_PHASE;
            setStandbyMessage(getCurrentPlayer() + " is activating the engines.");
            //notify all the connected players that the current player has to select the engines
            getController().getMessageManager().notifyPhaseChange(phase, this);
        } else {
            setStandbyMessage(getCurrentPlayer() + " is activating the cannons.");
            getController().getMessageManager().notifyPhaseChange(phase, this);
        }
    }

    @Override
    public void activateEngines(Player player, List<Pair<Integer, Integer>> engines, List<Pair<Integer, Integer>> batteries) throws InvalidTurnException, InvalidStateException, EnergyException, InvalidEngineException, ComponentNotFoundException {
        //check if the player is the current player
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn!");
        }
        //check if the player is in the engine phase
        if(!phase.equals(StatePhase.ENGINES_PHASE)) {
            throw new InvalidStateException("Not in engine phase.");
        }
        //translate the coordinates to the components
        List<Battery> batteriesComponents = new ArrayList<>();
        if(Translator.getComponentAt(player, batteries, Battery.class)!=null)
            batteriesComponents.addAll(Translator.getComponentAt(player, batteries, Battery.class));
        //save the declared engine power in the map
        declaredEnginePower.put(player.getUsername(), getModel().enginePower(player, engines.size(), batteriesComponents));
        getController().getMessageManager().broadcastUpdate(Ship.messageFromShip(player.getUsername(), player.getShip(), "activated engines"));
        //pass the turn to the next player
        nextPlayer();
        //check if the current player is null, if so, it means that all players have played
        if (getCurrentPlayer() == null) {
            //if all played, remove from declaredEnginePower the players that are not in getInGameConnectedPlayers
            declaredEnginePower.keySet().removeIf(p -> !getController().getInGameConnectedPlayers().contains(p));

            //get the player with the minimum declaredEnginePower and make it lose cargo
            setCurrentPlayer(declaredEnginePower.entrySet()
                    .stream()
                    .min(Map.Entry.comparingByValue())
                    .orElseThrow(() -> new RuntimeException("Error"))
                    .getKey());

            //we need to check if the player has cargo to lose
            Map<CargoColor, Integer> cargo = getController().getPlayerByID(getCurrentPlayer()).getShip().getCargo();
            boolean allZero = true;
            for (Integer count: cargo.values()){
                if (count > 0) {
                    allZero = false;
                    break;
                }
            }

            if (allZero && lostCargo > 0) {
                phase = StatePhase.BATTERY_PHASE;
                setStandbyMessage(getCurrentPlayer() + " is selecting the batteries to lose energy from.");
                getController().getMessageManager().notifyPhaseChange(phase, this);
            } else {
                removingCargo = true;
                //set the phase to the remove cargo phase
                phase = StatePhase.REMOVE_CARGO;
                setStandbyMessage(getCurrentPlayer() + " is removing cargo.");
                getController().getMessageManager().notifyPhaseChange(phase, this);
            }
        }else {
            phase = StatePhase.ENGINES_PHASE;
            setStandbyMessage(getCurrentPlayer() + " is activating the engines.");
            getController().getMessageManager().notifyPhaseChange(phase, this);
        }
    }

    @Override
    public int rollDice(Player player) throws InvalidTurnException, InvalidStateException{
        //check if the player is the current player
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn!");
        }
        //check if the player is in the roll dice phase
        if (!phase.equals(StatePhase.ROLL_DICE_PHASE)) {
            throw new InvalidStateException("Not in roll dice phase.");
        }
        // roll the dice
        result = getModel().getGame().rollDice();
        //check what type is the first projectile
        switch (manager.getFirstProjectile()) {
            case LIGHT_FIRE, LIGHT_METEOR:
                phase = StatePhase.SELECT_SHIELD;
                setStandbyMessage(getCurrentPlayer() + " is selecting the shields to activate.");
                getController().getMessageManager().notifyPhaseChange(phase, this);
                break;
            case HEAVY_METEOR:
                break;
            case HEAVY_FIRE:
                //if the projectile is a heavy fire, the player can't do anything
                fireProjectile(player);
                break;
            case null:
                //if the projectile is null, we can draw a new card
                getModel().getActiveCard().playCard();
                //notify all players that the card has been plated, next will be drawn
                getController().getMessageManager().broadcastPhase(new DrawCardPhaseMessage());
                phase = StatePhase.DRAW_CARD_PHASE;
                getController().setState(new PreDrawState(getController()));
                break;
        }
        return result;
    }

    @Override
    public void activateShield(Player player, Pair<Integer, Integer> shield, Pair<Integer, Integer> battery) throws InvalidStateException, InvalidTurnException, EnergyException, ComponentNotFoundException{
        //check if the player is the current player
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn!");
        }
        //check if the player is in the shield phase
        if(!phase.equals(StatePhase.SELECT_SHIELD)) {
            throw new InvalidStateException("Not in shield phase.");
        }
        //use the shield adn battery to activate the shield
        try {
            manager.activateShield(Translator.getComponentAt(player, shield, Shield.class), Translator.getComponentAt(player, battery, Battery.class));
            getController().getMessageManager().broadcastUpdate(Ship.messageFromShip(player.getUsername(), player.getShip(), "activated shield"));
            //fire the projectile
            fireProjectile(player);
        } catch (InvalidShipException e) {
            //notify all the players of the ship update
            getController().getMessageManager().broadcastUpdate(Ship.messageFromShip(player.getUsername(), player.getShip(), "destroyed a component"));
            //notify all players that the current player has to choose the branch
            phase = StatePhase.VALIDATE_SHIP_PHASE;
            setStandbyMessage(getCurrentPlayer() + " is choosing the branch to keep.");
            getController().getMessageManager().notifyPhaseChange(phase, this);
        }
    }

    /**
     * This method is used to fire the projectile and check if the manager has finished shooting.
     * If the manager has finished shooting, it will set the phase to standby phase.
     * If the manager has not finished shooting, it will set the phase to roll dice phase.
     *
     * @param player the player who is firing the projectile
     */
    private void fireProjectile(Player player) {
        try {
            manager.fire();
            getController().getMessageManager().broadcastUpdate(Ship.messageFromShip(player.getUsername(), player.getShip(), "destroyed a component"));
            //check if we finished shooting
            finishManager();
        } catch (InvalidShipException e) {
            //notify all the players of the ship update
            getController().getMessageManager().broadcastUpdate(Ship.messageFromShip(player.getUsername(), player.getShip(), "destroyed a component"));
            phase = StatePhase.VALIDATE_SHIP_PHASE;
            setStandbyMessage(getCurrentPlayer() + " is choosing the branch to keep.");
            getController().getMessageManager().notifyPhaseChange(phase, this);
        } catch (DieNotRolledException _) {
            //cannot happen
        }
    }

    /**
     * This method is used to finish the fire manager and check if it has finished shooting.
     * If it has finished shooting, it will set the phase to standby phase and draw a new card.
     * If it has not finished shooting, it will set the phase to roll dice phase.
     */
    private void finishManager() {
        if (manager.finished()) {
            //if we finished shooting, set the phase to standby phase
            phase = StatePhase.DRAW_CARD_PHASE;
            getController().getMessageManager().broadcastPhase(new DrawCardPhaseMessage());
            getModel().getActiveCard().playCard();
            getController().setState(new PreDrawState(getController()));
        } else {
            phase = StatePhase.ROLL_DICE_PHASE;
            setStandbyMessage(getCurrentPlayer() + " is rolling the dice.");
            getController().getMessageManager().notifyPhaseChange(phase, this);
        }
    }

    @Override
    public void unloadCargo(Player player, CargoColor unloaded, Pair<Integer, Integer> ch) throws InvalidTurnException, InvalidStateException, InvalidCargoException, ComponentNotFoundException  {
        //check if the player is the current player
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn!");
        }
        //check if the player is in the remove cargo phase
        if (phase != StatePhase.REMOVE_CARGO) {
            throw new InvalidStateException("Not in remove cargo phase.");
        }
        //remove the cargo from the ship
        super.unloadCargo(player, unloaded, ch);
        //check if the player has more cargo to lose
        Map<CargoColor, Integer> cargo = getController().getPlayerByID(getCurrentPlayer()).getShip().getCargo();
        boolean allZero = true;
        for (Integer count: cargo.values()){
            if (count > 0) {
                allZero = false;
                break;
            }
        }
        //if all cargos are zero and there is still cargo to remove, go to remove battery phase
        lostCargo--;
        if (allZero && lostCargo > 0) {
            phase = StatePhase.BATTERY_PHASE;
            setStandbyMessage(getCurrentPlayer() + " is selecting the batteries to lose energy from.");
            getController().getMessageManager().notifyPhaseChange(phase, this);
        } else {
            removingCargo = true;
            phase = StatePhase.REMOVE_CARGO;
            setStandbyMessage(getCurrentPlayer() + " is removing cargo.");
            getController().getMessageManager().notifyPhaseChange(phase, this);
        }
    }

    @Override
    public void endMove(Player player) throws InvalidTurnException, InvalidStateException{
        //check if the player is the current player
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn!");
        }
        //check if the player is in the remove cargo phase or if he didn't lose the cargo necessary
        if (phase != StatePhase.REMOVE_CARGO && phase!=StatePhase.BATTERY_PHASE) {
            throw new InvalidStateException("Request not valid.");
        }
        if (lostCargo != 0) {
            throw new InvalidStateException("You have to remove cargo before ending your turn.");
        }

        //if the player removed all the cargo, set the current player to the first online player
        setCurrentPlayer(getController().getFirstOnlinePlayer());
        //notify all players that an automatic action is being performed
        getController().getMessageManager().broadcastPhase(new AutomaticActionMessage("Finding the player with fewer crew members..."));
        //set the phase to the next phase
        phase = StatePhase.AUTOMATIC_ACTION;
        automaticAction();
    }

    @Override
    public void loseEnergy(Player player, Pair<Integer, Integer> battery) throws InvalidStateException, InvalidTurnException, EnergyException, ComponentNotFoundException{
        //check if the player it's the current player
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn!");
        }
        //check if the player is in the remove battery phase
        if (phase != StatePhase.BATTERY_PHASE) {
            throw new InvalidStateException("Not in remove battery phase.");
        }
        //remove the energy from the ship
        super.loseEnergy(player, battery);
        lostCargo--;
        if (player.getShip().getTotalEnergy() == 0) {
            lostCargo = 0;
        }
        getController().getMessageManager().notifyPhaseChange(phase, this);
    }

    @Override
    public void chooseBranch(Player player, Pair<Integer, Integer> coordinates) throws InvalidTurnException, InvalidStateException{
        //check if the player is the current player
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn!");
        }
        //check if the player is in the branch phase
        if(!phase.equals(StatePhase.VALIDATE_SHIP_PHASE)) {
            throw new InvalidStateException("Not in branch phase.");
        }
        //choose the branch selected by the player
        manager.chooseBranch(player, coordinates);
        getController().getMessageManager().broadcastUpdate(Ship.messageFromShip(player.getUsername(), player.getShip(), "chose branch"));
        //check if the fire manager is finished
        finishManager();
    }

    @Override
    public void currentQuit(Player player){
        //first, we check if we are in a penality phase:
        // REMOVE_CARGO or ROLL_DICE_PHASE, ACTIVATE_SHIELD or VALIDATE SHIP
        if (phase == StatePhase.REMOVE_CARGO){
            phase = StatePhase.AUTOMATIC_ACTION;
            getController().getMessageManager().broadcastPhase(new AutomaticActionMessage("Finding the player with fewer crew members..."));
            automaticAction();
        }
        else if (phase == StatePhase.ROLL_DICE_PHASE || phase == StatePhase.SELECT_SHIELD){
            //we end the card
            phase = StatePhase.DRAW_CARD_PHASE;
            getController().getMessageManager().broadcastPhase(new DrawCardPhaseMessage());
            getModel().getActiveCard().playCard();
            getController().setState(new PreDrawState(getController()));
        }
        else if (phase == StatePhase.VALIDATE_SHIP_PHASE){
            try {
                //we auto choose the branch
                chooseBranch(player, new Pair<>(-1, -1));
                //notify the ship update
                getController().getMessageManager().broadcastUpdate(Ship.messageFromShip(player.getUsername(), player.getShip(), "chose a branch"));
                if (phase != StatePhase.DRAW_CARD_PHASE){
                    phase = StatePhase.DRAW_CARD_PHASE;
                    getController().getMessageManager().broadcastPhase(new DrawCardPhaseMessage());
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
    public int cargoToRemove(){
        //return the crew that the player has to lose
        return lostCargo;
    }

    @Override
    public String createsShieldMessage() {
        return "A " + manager.getFirstProjectile().getFireType() + " is coming from the " + manager.getFirstDirection().getDirection() + "side at line" + result + ", select the shields to use.";
    }

    @Override
    public String createsRollDiceMessage() {
        return "A " + manager.getFirstProjectile().getFireType() + " is coming from the " + manager.getFirstDirection().getDirection() + "side, roll the dice to see where it will hit.";
    }
}
