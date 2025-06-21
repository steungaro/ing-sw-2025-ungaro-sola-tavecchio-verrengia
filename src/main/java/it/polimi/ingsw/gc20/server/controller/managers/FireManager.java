package it.polimi.ingsw.gc20.server.controller.managers;

import it.polimi.ingsw.gc20.server.exceptions.*;
import it.polimi.ingsw.gc20.server.model.cards.FireType;
import it.polimi.ingsw.gc20.server.model.cards.Projectile;
import it.polimi.ingsw.gc20.server.model.components.*;
import it.polimi.ingsw.gc20.server.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.server.model.player.Player;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * The FireManager class is responsible for managing and handling projectile firing
 * actions and defensive behaviors, such as activating shields and cannons,
 * within the game. It also ensures proper validation of the player's ship prior
 * to these actions, while keeping track of ongoing fire sequences.
 * <p>
 * This class maintains the current list of projectiles to fire, tracks whether the next
 * fire action should be skipped, and interacts with the game model to perform
 * operations such as energy deductions, projectile firing, and validating
 * player actions.
 */
public class FireManager {
    private final List<Projectile> fires;
    private boolean skipNextFire;
    private final GameModel gm;
    private final Validator validator;
    final Player player;

    /**
     * Constructs a FireManager instance responsible for handling the player's projectiles and game model.
     *
     * @param model the game model representing the current state of the game
     * @param fires the list of initial projectiles; a copy of this list will be stored internally
     *              to avoid modifying the original list
     * @param p the player associated with this FireManager
     */
    public FireManager(GameModel model, List<Projectile> fires, Player p) {
        //create a copy of the list of fires to avoid modifying the original list
        if (fires == null) {
            this.fires = new ArrayList<>();
        } else {
            this.fires = new ArrayList<>(fires);
        }
        this.skipNextFire = false;
        this.gm = model;
        this.player = p;
        this.validator = new Validator();
    }

    /**
     * Activates the specified cannon using a provided battery. This method ensures that the ship
     * and its resources are valid and updates energy consumption and cannon handling accordingly.
     * If the ship is invalid or resources are missing, the cannon activation is skipped.
     *
     * @param cannon the cannon to be activated, which must be attached to the ship
     * @param battery the battery providing energy for the cannon activation
     * @throws InvalidShipException if the ship is not valid or has not been properly configured
     * @throws EnergyException if there is not enough energy in the specified battery to activate the cannon
     */
    public void activateCannon(Cannon cannon, Battery battery) throws InvalidShipException, EnergyException {
        if (validator.isSplit()) {
            throw new InvalidShipException("Ship is not valid, validate it before firing");
        }
        if (battery == null || cannon == null) {
            skipNextFire = false;
            return;
        }
        List<Battery> batteries = new ArrayList<>();
        batteries.add(battery);
        gm.removeEnergy(player, batteries);
        // Check if the cannon is a heavy-meteor-defense cannon
        if (gm.heavyMeteorCannon(player, gm.getGame().rollDice(), fires.getFirst()).contains(cannon)) {
            skipNextFire = true;
        }
    }

    /**
     * Activates the specified shield using the provided battery. This method ensures that the ship
     * and its related resources are valid before activation. If the ship is invalid or resources
     * are missing, the shield activation is skipped. Additionally, it determines whether to skip
     * the next fire action based on the direction covered by the shield.
     *
     * @param shield the shield to be activated, which must be properly configured and attached
     *               to cover specific directions.
     * @param battery the battery providing energy for the shield activation. Must not be null.
     * @throws InvalidShipException if the ship is invalid or improperly configured.
     * @throws EnergyException if there is insufficient energy in the provided battery.
     */
    public void activateShield(Shield shield, Battery battery) throws InvalidShipException, EnergyException {
        if (validator.isSplit()) {
            throw new InvalidShipException("Ship is not valid, validate it before firing");
        }
        if (battery == null || shield == null) {
            skipNextFire = false;
            return;
        }
        gm.useShield(player, battery);
        if (shield.getCoveredSides()[0] == fires.getFirst().getDirection() || shield.getCoveredSides()[1] == fires.getFirst().getDirection()) {
            skipNextFire = true;
        }
    }

    /**
     * Executes the firing process for the player's ship using the available projectiles. This method
     * verifies the ship's validity, handles cases where firing is skipped, and processes the firing
     * logic based on the current projectile and dice roll. If the ship configuration or projectile
     * properties are not valid, the firing is aborted and an exception may be thrown.
     *
     * @throws InvalidShipException if the player's ship is not valid prior to firing
     * @throws DieNotRolledException if the dice roll necessary for targeting has not been completed
     */
    public void fire() throws InvalidShipException, DieNotRolledException {
        if (validator.isSplit()) {
            throw new InvalidShipException("Ship is not valid, validate it before firing");
        }
        if (skipNextFire) {
            skipNextFire = false;
            fires.removeFirst();
            return;
        }
        if (fires.isEmpty()) {
            return;
        }
        Projectile fire = fires.removeFirst();
        int dice = gm.getGame().lastRolled();
        if(player.getShip().getFirstComponent(fire.getDirection(), dice) == null)
            return;
        if(player.getShip().getFirstComponent(fire.getDirection(), dice).getConnectors().get(fire.getDirection())==null)
            return;
        if (fire.getFireType() != FireType.LIGHT_METEOR && player.getShip().getFirstComponent(fire.getDirection(), dice).getConnectors().get(fire.getDirection()) !=
        ConnectorEnum.ZERO) {
            return;
        }
        try {
            gm.fire(player, dice, fire);
        } catch (InvalidShipException e) {
            validator.setSplit();
            throw e;
        }
    }

    /**
     * Checks if there are no active projectiles being managed.
     *
     * @return true if the list of projectiles (fires) is empty, indicating no projectiles are active; false otherwise.
     */
    public boolean finished() {
        return fires.isEmpty();
    }

    /**
     * Checks if the current game state is in a "split" condition, as determined by the underlying `Validator`.
     * This condition might indicate a specific branch in gameplay where certain actions or decisions are required.
     *
     * @return true if the game state is in a split condition; false otherwise.
     */
    public boolean isSplit() {
        return validator.isSplit();
    }

    /**
     * Retrieves the fire type of the first projectile in the list of fires.
     * If the list is empty, it returns null.
     *
     * @return the FireType of the first projectile if present; null otherwise
     */
    public FireType getFirstProjectile() {
        if (fires.isEmpty()) {
            return null;
        }
        return fires.getFirst().getFireType();
    }

    /**
     * Allows a player to choose a branch of the game at specified coordinates. This method ensures
     * that the action is taken during the player's turn and validates the chosen branch. If the
     * action is performed outside the player's turn, an exception is thrown.
     *
     * @param p the player attempting to choose the branch
     * @param coordinates the coordinates representing the branch to be chosen, where the first value
     *                    represents the x-coordinate and the second value represents the y-coordinate
     * @throws InvalidTurnException if the action is attempted outside the player's turn
     */
    public void chooseBranch(Player p, Pair<Integer, Integer> coordinates) throws InvalidTurnException {
        if (!p.equals(player)) {
            throw new InvalidTurnException("It's not your turn");
        }
        try {
            validator.chooseBranch(p, coordinates.getValue0(), coordinates.getValue1());
        } catch (InvalidStateException e){
            // ignore
        }
    }

    /**
     * Retrieves the direction of the first projectile in the list of fires.
     * If the list is empty, it returns null.
     *
     * @return the Direction of the first projectile if the list is not empty; null otherwise
     */
    public Direction getFirstDirection() {
        if (fires.isEmpty()) {
            return null;
        }
        return fires.getFirst().getDirection();
    }
}
