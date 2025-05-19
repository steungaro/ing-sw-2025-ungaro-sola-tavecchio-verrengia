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
import it.polimi.ingsw.gc20.server.network.NetworkService;
import org.javatuples.Pair;

import java.util.*;

@SuppressWarnings("unused") // dynamically created by Cards
public class    CombatZone1State extends CargoState {
    private final int lostDays;
    private int lostCargo;
    private final List<Projectile> cannonFires;
    private final Map<String, Float> declaredFirepower;
    private final Map<String, Integer> declaredEnginePower;
    private boolean removingCargo;
    private FireManager manager;
    /**
     * Default constructor
     */
    @SuppressWarnings("unused") // dynamically created by Cards
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
        for (String username : getController().getInGameConnectedPlayers()){
            if (username.equals(getCurrentPlayer())) {
                NetworkService.getInstance().sendToClient(username, new CombatZoneCannonMessage(declaredFirepower));
            } else {
                NetworkService.getInstance().sendToClient(username, new StandbyMessage("Waiting for " + getCurrentPlayer() + " to select the cannons"));
            }
        }
    }

    /**
     * this method is used to find the player with the minimum crew and set it as the current player to play
     */
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
        //notify all players that the current player has to roll the dice
        for (String username : getController().getInGameConnectedPlayers()){
            if (username.equals(getCurrentPlayer())) {
                NetworkService.getInstance().sendToClient(username, new RollDiceMessage(manager.getFirstProjectile(), manager.getFirstDirection().getValue()));
            } else {
                NetworkService.getInstance().sendToClient(username, new StandbyMessage("Waiting for " + getCurrentPlayer() + " to roll the dice"));
            }
        }
    }


    /**
     * this method is used to activate the cannons selected from the player using the batteries
     * @param player the player who is activating the cannons
     * @param cannons the cannons selected by the player
     * @param batteries the batteries selected by the player
     */
    @Override
    public void activateCannons(Player player, List<Pair<Integer, Integer>> cannons, List<Pair<Integer, Integer>> batteries) throws InvalidTurnException, InvalidStateException, EnergyException, InvalidCannonException {
        // check if the player is the current player
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        // check if the player is in the cannon phase
        if(phase != StatePhase.CANNONS_PHASE) {
            throw new InvalidStateException("Not in cannon phase");
        }
        // translate the coordinates to the components
        Set<Cannon> cannonsComponents = new HashSet<>();
        List<Battery> batteriesComponents = new ArrayList<>();
        if(Translator.getComponentAt(player, cannons, Cannon.class)!=null)
            cannonsComponents.addAll(Translator.getComponentAt(player, cannons, Cannon.class));
        if(Translator.getComponentAt(player, batteries, Battery.class)!=null)
            batteriesComponents.addAll(Translator.getComponentAt(player, batteries, Battery.class));
        //save the declared firepower in the map
        float firepower = getModel().FirePower(player, cannonsComponents, batteriesComponents);
        declaredFirepower.put(player.getUsername(), firepower);
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
            for (String username : getController().getInGameConnectedPlayers()){
                NetworkService.getInstance().sendToClient(username, new PlayerUpdateMessage(p.getUsername(), 0, true, p.getColor(), (p.getPosition() % getModel().getGame().getBoard().getSpaces())));
            }
            //set the current player to the first online player
            setCurrentPlayer(getController().getFirstOnlinePlayer());
            //set the phase to the next phase
            phase = StatePhase.ENGINES_PHASE;
            //notify all the connected players that the current player has to select the engines
            for (String username : getController().getInGameConnectedPlayers()){
                if (username.equals(getCurrentPlayer())) {
                    NetworkService.getInstance().sendToClient(username, new CombatZoneEngineMessage(declaredEnginePower));
                } else {
                    NetworkService.getInstance().sendToClient(username, new StandbyMessage("Waiting for " + getCurrentPlayer() + " to select the engines"));
                }
            }
        } else {
            //notify the current player that he has to select the cannons
            for (String username : getController().getInGameConnectedPlayers()){
                if (username.equals(getCurrentPlayer())) {
                    NetworkService.getInstance().sendToClient(username, new CombatZoneCannonMessage(declaredFirepower));
                } else {
                    NetworkService.getInstance().sendToClient(username, new StandbyMessage("Waiting for " + getCurrentPlayer() + " to select the cannons"));
                }
            }
        }
    }

        @Override
    public String toString() {
        return "CombatZone1State{" +
                "lostDays=" + lostDays +
                ", lostCargo=" + lostCargo +
                ", cannonFires=" + cannonFires +
                ", declaredFirepower=" + declaredFirepower +
                ", declaredEnginePower=" + declaredEnginePower +
                ", removingCargo=" + removingCargo +
                '}';
    }

    /**
     * this method is used to activate the engines selected from the player using the batteries
     * @param player the player who is activating the engines
     * @param engines the engines selected by the player
     * @param batteries the batteries selected by the player
     */
    @Override
    public void activateEngines(Player player, List<Pair<Integer, Integer>> engines, List<Pair<Integer, Integer>> batteries) throws InvalidTurnException, InvalidStateException, EnergyException, InvalidEngineException {
        //check if the player is the current player
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        //check if the player is in the engine phase
        if(!phase.equals(StatePhase.ENGINES_PHASE)) {
            throw new InvalidStateException("Not in engine phase");
        }
        //translate the coordinates to the components
        List<Battery> batteriesComponents = new ArrayList<>();
        if(Translator.getComponentAt(player, batteries, Battery.class)!=null)
            batteriesComponents.addAll(Translator.getComponentAt(player, batteries, Battery.class));
        //save the declared engine power in the map
        declaredEnginePower.put(player.getUsername(), getModel().EnginePower(player, engines.size(), batteriesComponents));
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
            Map<CargoColor, Integer> cargo = player.getShip().getCargo();
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
                //if the player has no cargo to lose, set the phase to the remove battery phase
                for (String username : getController().getInGameConnectedPlayers()){
                    if (username.equals(getCurrentPlayer())) {
                        NetworkService.getInstance().sendToClient(username, new RemoveBatteryMessage(lostCargo));
                    } else {
                        NetworkService.getInstance().sendToClient(username, new StandbyMessage("Waiting for " + getCurrentPlayer() + " to remove the batteries"));
                    }
                }
                phase = StatePhase.BATTERY_PHASE;
            } else {
                removingCargo = true;
                //set the phase to the remove cargo phase
                phase = StatePhase.REMOVE_CARGO;
                //notify the current player that he has to remove the cargo
                for (String username : getController().getInGameConnectedPlayers()){
                    if (username.equals(getCurrentPlayer())) {
                        NetworkService.getInstance().sendToClient(username, new RemoveCargoMessage(lostCargo));
                    } else {
                        NetworkService.getInstance().sendToClient(username, new StandbyMessage("Waiting for " + getCurrentPlayer() + " to remove the cargo"));
                    }
                }
            }

        }
    }

    /**
     * this method is used to roll the dice
     * @param player the player who is rolling the dice
     * @return the value of the dice rolled
     */
    @Override
    public int rollDice(Player player) throws InvalidTurnException, InvalidStateException{
        //check if the player is the current player
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        //check if the player is in the roll dice phase
        if (!phase.equals(StatePhase.ROLL_DICE_PHASE)) {
            throw new InvalidStateException("Not in roll dice phase");
        }
        // roll the dice
        int result = getModel().getGame().rollDice();
        //check what type is the first projectile
        switch (manager.getFirstProjectile()) {
            case LIGHT_FIRE, LIGHT_METEOR:
                phase = StatePhase.SELECT_SHIELD;
                //notify all players that the current player has to select the shield
                for (String username : getController().getInGameConnectedPlayers()){
                    if (username.equals(getCurrentPlayer())) {
                        NetworkService.getInstance().sendToClient(username, new ShieldPhaseMessage(manager.getFirstProjectile(), manager.getFirstDirection().getValue(), result));
                    } else {
                        NetworkService.getInstance().sendToClient(username, new StandbyMessage("Waiting for " + getCurrentPlayer() + " to select the shield"));
                    }
                }
                break;
            case HEAVY_METEOR:
                phase = StatePhase.CANNONS_PHASE;
                break;
            case HEAVY_FIRE:
                //if the projectile is a heavy fire, the player can't do anything
                try {
                    //fire the projectile
                    manager.fire();
                    //check if we finished shooting
                    if (manager.finished()) {
                        //if we finished shooting, set the phase to standby phase
                        phase = StatePhase.STANDBY_PHASE;
                        //notify all players that the card has been plated, next will be drawn
                        for (String username : getController().getInGameConnectedPlayers()){
                            NetworkService.getInstance().sendToClient(username, new StandbyMessage("Waiting for next card to be drawn"));
                        }
                        getModel().getActiveCard().playCard();
                        getController().setState(new PreDrawState(getController()));
                    } else {
                        //if we didn't finish shooting, set the phase to roll dice phase
                        phase = StatePhase.ROLL_DICE_PHASE;
                        //notify all players that the current player has to roll the dice
                        for (String username : getController().getInGameConnectedPlayers()){
                            if (username.equals(getCurrentPlayer())) {
                                NetworkService.getInstance().sendToClient(username, new RollDiceMessage(manager.getFirstProjectile(), manager.getFirstDirection().getValue()));
                            } else {
                                NetworkService.getInstance().sendToClient(username, new StandbyMessage("Waiting for " + getCurrentPlayer() + " to roll the dice"));
                            }
                        }
                    }
                } catch (InvalidShipException e) {
                    phase = StatePhase.VALIDATE_SHIP_PHASE;
                    //notify all players that the current player has to choose the branch
                    for (String username : getController().getInGameConnectedPlayers()){
                        if (username.equals(getCurrentPlayer())) {
                            NetworkService.getInstance().sendToClient(username, new ChooseBranchMessage());
                        } else {
                            NetworkService.getInstance().sendToClient(username, new StandbyMessage("Waiting for " + getCurrentPlayer() + " to choose the branch"));
                        }
                    }
                } catch (DieNotRolledException _) {
                    //cannot happen
                }
                break;
            case null:
                //if the projectile is null, we can draw a new card
                getModel().getActiveCard().playCard();
                //notify all players that the card has been plated, next will be drawn
                for (String username : getController().getInGameConnectedPlayers()){
                    NetworkService.getInstance().sendToClient(username, new StandbyMessage("Waiting for next card to be drawn"));
                }
                phase = StatePhase.STANDBY_PHASE;
                getController().setState(new PreDrawState(getController()));
                break;
        }
        return result;
    }



    /**
     * this method is used to activate the shield selected from the player using the batteries
     * @param player the player who is activating the shield
     * @param shield the shield selected by the player
     * @param battery the battery selected by the player
     * @throws InvalidStateException if the player is not in the correct phase
     * @throws InvalidTurnException if it's not the player's turn
     * @throws EnergyException if the player doesn't have enough energy
     */
    @Override
    public void activateShield(Player player, Pair<Integer, Integer> shield, Pair<Integer, Integer> battery) throws InvalidStateException, InvalidTurnException, EnergyException{
        //check if the player is the current player
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        //check if the player is in the shield phase
        if(!phase.equals(StatePhase.SELECT_SHIELD)) {
            throw new InvalidStateException("Not in shield phase");
        }
        //use the shield adn battery to activate the shield
        try {
            manager.activateShield(Translator.getComponentAt(player, shield, Shield.class), Translator.getComponentAt(player, battery, Battery.class));
            //fire the projectile
            try {
                manager.fire();
                //check if we finished shooting
                if (manager.finished()) {
                    //if we finished shooting, set the phase to standby phase
                    phase = StatePhase.STANDBY_PHASE;
                    //notify all players that the card has been plated, next will be drawn
                    for (String username : getController().getInGameConnectedPlayers()){
                        NetworkService.getInstance().sendToClient(username, new StandbyMessage("Waiting for next card to be drawn"));
                    }
                    getModel().getActiveCard().playCard();
                    getController().setState(new PreDrawState(getController()));
                } else {
                    //if we didn't finish shooting, set the phase to roll dice phase
                    phase = StatePhase.ROLL_DICE_PHASE;
                    //notify all connected players that the current player has to roll the dice
                    for (String username : getController().getInGameConnectedPlayers()){
                        if (username.equals(getCurrentPlayer())) {
                            NetworkService.getInstance().sendToClient(username, new RollDiceMessage(manager.getFirstProjectile(), manager.getFirstDirection().getValue()));
                        } else {
                            NetworkService.getInstance().sendToClient(username, new StandbyMessage("Waiting for " + getCurrentPlayer() + " to roll the dice"));
                        }
                    }
                }
            } catch (InvalidShipException e) {
                phase = StatePhase.VALIDATE_SHIP_PHASE;
                //notify all players that the current player has to choose the branch
                for (String username : getController().getInGameConnectedPlayers()){
                    if (username.equals(getCurrentPlayer())) {
                        NetworkService.getInstance().sendToClient(username, new ChooseBranchMessage());
                    } else {
                        NetworkService.getInstance().sendToClient(username, new StandbyMessage("Waiting for " + getCurrentPlayer() + " to choose the branch"));
                    }
                }
            } catch (DieNotRolledException _) {
                //cannot happen
            }
        } catch (InvalidShipException e) {
            //notify all players that the current player has to choose the branch
            for (String username : getController().getInGameConnectedPlayers()){
                if (username.equals(getCurrentPlayer())) {
                    NetworkService.getInstance().sendToClient(username, new ChooseBranchMessage());
                } else {
                    NetworkService.getInstance().sendToClient(username, new StandbyMessage("Waiting for " + getCurrentPlayer() + " to choose the branch"));
                }
            }
            phase = StatePhase.VALIDATE_SHIP_PHASE;
        }
    }

    /**
     * this method is used to unload cargo from the player's ship
     * @param player the player who is unloading the cargo
     * @param unloaded the color of the cargo to be unloaded
     * @param ch the cargo hold from which the cargo is unloaded
     */
    @Override
    public void unloadCargo(Player player, CargoColor unloaded, Pair<Integer, Integer> ch) throws InvalidTurnException, InvalidStateException, InvalidCargoException  {
        //check if the player is the current player
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        //check if the player is in the remove cargo phase
        if (phase != StatePhase.REMOVE_CARGO) {
            throw new InvalidStateException("Not in remove cargo phase");
        }
        //remove the cargo from the ship
        super.unloadCargo(player, unloaded, ch);
        //notify all players about the ship update
        for (String username : getController().getInGameConnectedPlayers()){
            NetworkService.getInstance().sendToClient(username, new UpdateShipMessage(player.getUsername(), player.getShip(), "unloaded cargo"));
        }
        //check if the player has more cargo to lose
        Map<CargoColor, Integer> cargo = player.getShip().getCargo();
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
            //if the player has no cargo to lose, set the phase to the remove battery phase and notify the player
            for (String username : getController().getInGameConnectedPlayers()){
                if (username.equals(getCurrentPlayer())) {
                    NetworkService.getInstance().sendToClient(username, new RemoveBatteryMessage(lostCargo));
                } else {
                    NetworkService.getInstance().sendToClient(username, new StandbyMessage("Waiting for " + getCurrentPlayer() + " to remove the batteries"));
                }
            }
            phase = StatePhase.BATTERY_PHASE;
        }
    }


    /**
     * this method is used to end the move of the player
     * @param player the player who is ending the move
     * @throws InvalidStateException if the player is not in the correct phase, or if he didn't lose the cargo necessary
     * @throws InvalidTurnException if the player it's not the current player
     */
    @Override
    public void endMove(Player player) throws InvalidTurnException, InvalidStateException{
        //check if the player is the current player
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        //check if the player is in the remove cargo phase or if he didn't lose the cargo necessary
        if (phase != StatePhase.REMOVE_CARGO && phase!=StatePhase.BATTERY_PHASE) {
            throw new InvalidStateException("Request not valid");
        }
        if (lostCargo != 0) {
            throw new InvalidStateException("You have to remove cargo");
        }

        //if the player removed all the cargo, set the current player to the first online player
        setCurrentPlayer(getController().getFirstOnlinePlayer());
        //notify all players that an automatic action is being performed
        for (String username : getController().getInGameConnectedPlayers()){
            NetworkService.getInstance().sendToClient(username, new AutomaticActionMessage("finding player with minimum crew"));
        }
        //set the phase to the next phase
        phase = StatePhase.AUTOMATIC_ACTION;
        automaticAction();
    }

    /**
     * this method is used to remove the selected energy from the ship
     * @param player the player who is losing energy
     * @param battery the battery to be lost
     * @throws IllegalStateException if the player is not in the correct phase
     * @throws InvalidTurnException if the player it's not the current player
     * @throws EnergyException if the player doesn't have enough energy
     */
    @Override
    public void loseEnergy(Player player, Pair<Integer, Integer> battery) throws InvalidStateException, InvalidTurnException, EnergyException{
        //check if the player it's the current player
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        //check if the player is in the remove battery phase
        if (phase != StatePhase.BATTERY_PHASE) {
            throw new InvalidStateException("Not in remove cargo phase");
        }
        //remove the energy from the ship
        super.loseEnergy(player, battery);
        lostCargo--;
        //notify all players about the ship update
        for (String username : getController().getInGameConnectedPlayers()){
            NetworkService.getInstance().sendToClient(username, new UpdateShipMessage(player.getUsername(), player.getShip(), "removed energy"));
        }
        if (player.getShip().getTotalEnergy() == 0) {
            lostCargo = 0;
        }
    }

    /**
     * this metho is used to choose the branch of the ship
     * @param player the player who is choosing the branch
     * @param coordinates the coordinates of the branch
     * @throws InvalidTurnException if the player it's not the current player
     * @throws InvalidStateException if the player is not in the correct phase
     */
    @Override
    public void chooseBranch(Player player, Pair<Integer, Integer> coordinates) throws InvalidTurnException, InvalidStateException{
        //check if the player is the current player
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        //check if the player is in the branch phase
        if(!phase.equals(StatePhase.VALIDATE_SHIP_PHASE)) {
            throw new InvalidStateException("Not in branch phase");
        }
        //choose the branch selected by the player
        manager.chooseBranch(player, coordinates);
        //check if the fire manager is finished
        if (manager.finished()) {
            //if the fire manager is finished, we can draw a new card
            phase = StatePhase.STANDBY_PHASE;
            //notify all players that the card has been plated, next will be drawn
            for (String username : getController().getInGameConnectedPlayers()){
                NetworkService.getInstance().sendToClient(username, new StandbyMessage("Waiting for next card to be drawn"));
            }
            getModel().getActiveCard().playCard();
            getController().setState(new PreDrawState(getController()));
        } else {
            //if the fire manager is not finished, we can roll the dice again
            phase = StatePhase.ROLL_DICE_PHASE;
            //notify the current player that he has to roll the dice
            for (String username : getController().getInGameConnectedPlayers()){
                if (username.equals(getCurrentPlayer())) {
                    NetworkService.getInstance().sendToClient(username, new RollDiceMessage(manager.getFirstProjectile(), manager.getFirstDirection().getValue()));
                } else {
                    NetworkService.getInstance().sendToClient(username, new StandbyMessage("Waiting for " + getCurrentPlayer() + " to roll the dice"));
                }
            }
        }
    }

    /**
     * this method is used when a player quits the game
     * @param player who quits
     */
    public void currentQuit(Player player){
        //first, we check if we are in a penality phase:
        // REMOVE_CARGO or ROLL_DICE_PHASE, ACTIVATE_SHIELD or VALIDATE SHIP
        if (phase == StatePhase.REMOVE_CARGO){
            //we go to the next phase
            //notify all players that the current player has to remove the cargo
            for (String username : getController().getInGameConnectedPlayers()){
                NetworkService.getInstance().sendToClient(username, new AutomaticActionMessage("finding player with minimum crew"));
            }
            phase = StatePhase.AUTOMATIC_ACTION;
            automaticAction();
        }
        else if (phase == StatePhase.ROLL_DICE_PHASE || phase == StatePhase.SELECT_SHIELD){
            //we end the card
            phase = StatePhase.STANDBY_PHASE;
            //notify all players that the card has been plated, next will be drawn
            for (String username : getController().getInGameConnectedPlayers()){
                NetworkService.getInstance().sendToClient(username, new StandbyMessage("Waiting for next card to be drawn"));
            }
            getModel().getActiveCard().playCard();
            getController().setState(new PreDrawState(getController()));
        }
        else if (phase == StatePhase.VALIDATE_SHIP_PHASE){
            try {
                //we auto choose the branch
                chooseBranch(player, new Pair<>(-1, -1));
                if (phase != StatePhase.STANDBY_PHASE){
                    phase = StatePhase.STANDBY_PHASE;
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
                    } catch (InvalidTurnException | InvalidStateException | EnergyException | InvalidEngineException e) {
                        //ignore
                    }
                } else if (phase == StatePhase.CANNONS_PHASE) {
                    try {
                        //we auto-activate the cannons,
                        // and the player will be removed from the array in the activateCannons method
                        activateCannons(player, new ArrayList<>(), new ArrayList<>());
                    } catch (InvalidTurnException | InvalidStateException | EnergyException | InvalidCannonException e) {
                        //ignore
                    }
                }
            }
        }
    }
}
