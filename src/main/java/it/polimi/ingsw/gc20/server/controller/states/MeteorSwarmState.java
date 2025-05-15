package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.controller.managers.FireManager;
import it.polimi.ingsw.gc20.server.controller.managers.Translator;
import it.polimi.ingsw.gc20.server.exceptions.*;
import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.server.model.cards.Projectile;
import it.polimi.ingsw.gc20.server.model.components.Battery;
import it.polimi.ingsw.gc20.server.model.components.Cannon;
import it.polimi.ingsw.gc20.server.model.components.Shield;
import it.polimi.ingsw.gc20.server.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.server.model.player.Player;
import org.javatuples.Pair;

import java.util.List;
import java.util.Map;

@SuppressWarnings("unused") // dynamically created by Cards
public class MeteorSwarmState extends PlayingState {
    private final List<Projectile> meteors;
    private Map<Player, FireManager> fireManagerMap;
    private Map<Player, StatePhase> phaseMap;
    /**
     * Default constructor
     */
    public MeteorSwarmState(GameModel model, GameController controller, AdventureCard card) {
        super(model, controller);
        this.meteors = card.getProjectiles();
        //init the map
        for (String username : getController().getInGameConnectedPlayers()) {
            fireManagerMap.put(getController().getPlayerByID(username), new FireManager(model, meteors, getController().getPlayerByID(username)));
            if (username.equals(getCurrentPlayer())) {
                phaseMap.put(getController().getPlayerByID(username), StatePhase.ROLL_DICE_PHASE);
            } else {
                phaseMap.put(getController().getPlayerByID(username), StatePhase.STANDBY_PHASE);
            }
        }
    }

    @Override
    public String toString() {
        return "MeteorSwarmState{" +
                "meteors=" + meteors +
                '}';
    }

    @Override
    public void activateCannons(Player player, List<Pair<Integer, Integer>> cannons, List<Pair<Integer, Integer>> batteries) throws DieNotRolledException, InvalidTurnException, InvalidShipException, EnergyException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        if (phaseMap.get(player) != StatePhase.CANNONS_PHASE) {
            throw new InvalidTurnException("cannot activate cannons in this phase");
        }

        fireManagerMap.get(player).activateCannon(Translator.getComponentAt(player, cannons.getFirst(), Cannon.class), Translator.getComponentAt(player, batteries.getFirst(), Battery.class));
        try {
            fireManagerMap.get(player).fire();
            nextPlayer();
            //if all the player has received this projectile
            if (getCurrentPlayer() == null){
                //verify if the first player has received all the meteors
                setCurrentPlayer(getController().getFirstOnlinePlayer());
                if (fireManagerMap.get(getController().getPlayerByID(getCurrentPlayer())).finished()) {
                    for (String p : getController().getInGameConnectedPlayers()) {
                        phaseMap.put(getController().getPlayerByID(p), StatePhase.STANDBY_PHASE);
                    }
                    getModel().getActiveCard().playCard();
                    getController().setState(new PreDrawState(getController()));
                } else {
                    for (String p : getController().getInGameConnectedPlayers()) {
                        if (p.equals(getCurrentPlayer())){
                            phaseMap.put(getController().getPlayerByID(getCurrentPlayer()), StatePhase.ROLL_DICE_PHASE);
                        } else {
                            phaseMap.put(getController().getPlayerByID(p), StatePhase.STANDBY_PHASE);
                        }
                    }

                }
            } else {
                phaseMap.put(player, StatePhase.STANDBY_PHASE);
                phaseMap.put(getController().getPlayerByID(getCurrentPlayer()), StatePhase.CANNONS_PHASE);
            }
        } catch (InvalidShipException e) {
            phaseMap.put(player, StatePhase.VALIDATE_SHIP_PHASE);
            phase = StatePhase.CANNONS_PHASE;
        }
    }

    @Override
    public int rollDice(Player player) throws IllegalStateException, InvalidTurnException, DieNotRolledException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        if (fireManagerMap.get(player).finished()) {
            throw new IllegalStateException("Cannot roll dice when not firing");
        }
        if (phaseMap.get(player) != StatePhase.ROLL_DICE_PHASE){
            throw new InvalidTurnException("Cannot roll dice in this phase");
        }
        getModel().getGame().rollDice();
        switch (fireManagerMap.get(player).getFirstProjectile()) {
            case LIGHT_METEOR:
                for (String p : getController().getInGameConnectedPlayers()) {
                    if (p.equals(getCurrentPlayer())) {
                        phaseMap.put(getController().getPlayerByID(p), StatePhase.SELECT_SHIELD);
                    } else {
                        phaseMap.put(getController().getPlayerByID(p), StatePhase.STANDBY_PHASE);
                    }
                }
                break;
            case HEAVY_METEOR:
                for (String p : getController().getInGameConnectedPlayers()) {
                    if (p.equals(getCurrentPlayer())) {
                        phaseMap.put(getController().getPlayerByID(p), StatePhase.CANNONS_PHASE);
                    } else {
                        phaseMap.put(getController().getPlayerByID(p), StatePhase.STANDBY_PHASE);
                    }
                }
                break;
        }
        return getModel().getGame().lastRolled();
    }

    @Override
    public void activateShield(Player player, Pair<Integer, Integer> shield, Pair<Integer, Integer> battery) throws IllegalStateException, InvalidTurnException, InvalidShipException, DieNotRolledException, EnergyException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        if (!phaseMap.get(player).equals(StatePhase.SELECT_SHIELD)) {
            throw new InvalidTurnException("cannot activate shield in this phase");
        }

        fireManagerMap.get(player).activateShield(Translator.getComponentAt(player, shield, Shield.class), Translator.getComponentAt(player, battery, Battery.class));
        try {
            fireManagerMap.get(player).fire();
            nextPlayer();
            //if all the player has received this projectile
            if (getCurrentPlayer() == null){
                //verify if the first player has received all the meteors
                setCurrentPlayer(getController().getFirstOnlinePlayer());
                if (fireManagerMap.get(getController().getPlayerByID(getCurrentPlayer())).finished()) {
                    for (String p : getController().getInGameConnectedPlayers()) {
                        phaseMap.put(getController().getPlayerByID(p), StatePhase.STANDBY_PHASE);
                    }
                    getModel().getActiveCard().playCard();
                    getController().setState(new PreDrawState(getController()));
                } else {
                    for (String p : getController().getInGameConnectedPlayers()) {
                        if (p.equals(getCurrentPlayer())){
                            phaseMap.put(getController().getPlayerByID(getCurrentPlayer()), StatePhase.ROLL_DICE_PHASE);
                        } else {
                            phaseMap.put(getController().getPlayerByID(p), StatePhase.STANDBY_PHASE);
                        }
                    }

                }
            } else {
                phaseMap.put(player, StatePhase.STANDBY_PHASE);
                phaseMap.put(getController().getPlayerByID(getCurrentPlayer()), StatePhase.SELECT_SHIELD);
            }
        } catch (InvalidShipException e) {
            phaseMap.put(player, StatePhase.VALIDATE_SHIP_PHASE);
            phase = StatePhase.SELECT_SHIELD;
        }
    }

    @Override
    public void chooseBranch(Player player, Pair<Integer, Integer> coordinates) throws InvalidTurnException {
        if (!player.getUsername().equals(getCurrentPlayer())) {
            throw new InvalidTurnException("It's not your turn");
        }
        if (phaseMap.get(player) != StatePhase.VALIDATE_SHIP_PHASE){
            throw new InvalidTurnException("cannot choose branch in this phase");
        }
        fireManagerMap.get(player).chooseBranch(player, coordinates);
        //after the player chose the branch we check if there is a next player
        nextPlayer();
        if (getCurrentPlayer() == null) {
            //if there is no next player we set the current player to the fist player
            setCurrentPlayer(getController().getFirstOnlinePlayer());
            //we verify if we shot all the projectiles
            if (fireManagerMap.get(getController().getPlayerByID(getCurrentPlayer())).finished()){
                //we set all the player phase to standby and we can go to the next card
                for (String p : getController().getInGameConnectedPlayers()) {
                    phaseMap.put(getController().getPlayerByID(p), StatePhase.STANDBY_PHASE);
                }
                getModel().getActiveCard().playCard();
                getController().setState(new PreDrawState(getController()));
            } else {
                //if is not finisched we set the first player to the roll dice phase and the others to the standby phase
                for (String p : getController().getInGameConnectedPlayers()) {
                    if (p.equals(getCurrentPlayer())){
                        phaseMap.put(getController().getPlayerByID(getCurrentPlayer()), StatePhase.ROLL_DICE_PHASE);
                    } else {
                        phaseMap.put(getController().getPlayerByID(p), StatePhase.STANDBY_PHASE);
                    }
                }
            }
        } else {
            //if there is a next player we modify is state to the correct phase, memorized in the phase attribute
            phaseMap.put(player, StatePhase.STANDBY_PHASE);
            phaseMap.put(getController().getPlayerByID(getCurrentPlayer()), phase);
        }
    }

    @Override
    public void currentQuit(Player player) {
        if (phaseMap.get(player) == StatePhase.VALIDATE_SHIP_PHASE) {
            try {
                chooseBranch(player, new Pair<>(-1, -1));
                phase = StatePhase.STANDBY_PHASE;
                getModel().getActiveCard().playCard();
                getController().setState(new PreDrawState(getController()));
            } catch (InvalidTurnException e) {
                //ignore
            }
        } else {
            nextPlayer();
            if (getCurrentPlayer() == null) {
                //if there is no next player we set the current player to the first player
                setCurrentPlayer(getController().getFirstOnlinePlayer());
                //we verify if we shot all the projectiles
                if (fireManagerMap.get(getController().getPlayerByID(getCurrentPlayer())).finished()) {
                    //we set all the player phase to standby and we can go to the next card
                    for (String p : getController().getInGameConnectedPlayers()) {
                        phaseMap.put(getController().getPlayerByID(p), StatePhase.STANDBY_PHASE);
                    }
                    getModel().getActiveCard().playCard();
                    getController().setState(new PreDrawState(getController()));
                } else {
                    //if is not finished we set the first player to the roll dice phase and the others to the standby phase
                    for (String p : getController().getInGameConnectedPlayers()) {
                        if (p.equals(getCurrentPlayer())) {
                            phaseMap.put(getController().getPlayerByID(getCurrentPlayer()), StatePhase.ROLL_DICE_PHASE);
                        } else {
                            phaseMap.put(getController().getPlayerByID(p), StatePhase.STANDBY_PHASE);
                        }
                    }
                }
            }
        }
    }
}
