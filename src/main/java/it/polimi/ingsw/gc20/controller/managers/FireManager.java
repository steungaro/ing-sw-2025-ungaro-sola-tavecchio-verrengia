package it.polimi.ingsw.gc20.controller.managers;

import it.polimi.ingsw.gc20.exceptions.InvalidShipException;
import it.polimi.ingsw.gc20.exceptions.InvalidTurnException;
import it.polimi.ingsw.gc20.model.cards.FireType;
import it.polimi.ingsw.gc20.model.cards.Projectile;
import it.polimi.ingsw.gc20.model.components.*;
import it.polimi.ingsw.gc20.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.model.player.Player;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.List;

public class FireManager {
    private final List<Projectile> fires;
    private boolean skipNextFire;
    private final GameModel gm;
    private final Validator validator;
    Player player;

    public FireManager(GameModel model, List<Projectile> fires, Player p) {
        this.fires = fires;
        this.skipNextFire = false;
        this.gm = model;
        this.player = p;
        this.validator = new Validator();
    }

    public void activateCannon(Cannon cannon, Battery battery) throws InvalidShipException, IllegalStateException {
        if (fires.getFirst().getFireType() != FireType.HEAVY_METEOR) {
            throw new IllegalStateException("Cannot activate cannon in this state");
        }
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
        if (gm.heavyMeteorCannon(player, gm.getGame().rollDice(), fires.getFirst()).contains(cannon)) {
            skipNextFire = true;
        }
    }

    public void activateShield(Shield shield, Battery battery) throws InvalidShipException, IllegalStateException {
        if (fires.getFirst().getFireType() != FireType.LIGHT_METEOR && fires.getFirst().getFireType() != FireType.LIGHT_FIRE) {
            throw new IllegalStateException("Cannot activate shield in this state");
        }
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

    public void fire() throws InvalidShipException {
        if (validator.isSplit()) {
            throw new InvalidShipException("Ship is not valid, validate it before firing");
        }
        if (skipNextFire) {
            skipNextFire = false;
        }
        if (fires.isEmpty()) {
            return;
        }
        Projectile fire = fires.getFirst();
        int dice = gm.getGame().lastRolled();
        if (fire.getFireType() != FireType.LIGHT_METEOR && player.getShip().getFirstComponent(fire.getDirection(), dice).getConnectors().get(fire.getDirection()) !=
        ConnectorEnum.ZERO) {
            return;
        }
        try {
            gm.Fire(player, dice, fire);
        } catch (InvalidShipException e) {
            validator.setSplit();
            throw e;
        }
    }

    public boolean finished() {
        return fires.isEmpty();
    }

    public boolean isSplit() {
        return validator.isSplit();
    }

    public boolean isFirstHeavyFire() {
        if (fires.isEmpty()) {
            return false;
        }
        return fires.getFirst().getFireType() == FireType.HEAVY_FIRE;
    }

    public void chooseBranch(Player p, Pair<Integer, Integer> coordinates) throws InvalidTurnException {
        if (!p.equals(player)) {
            throw new InvalidTurnException("It's not your turn");
        }
        if (!validator.isSplit()) {
            throw new IllegalStateException("Ship is valid.");
        }
        validator.chooseBranch(p, coordinates.getValue0(), coordinates.getValue1());
    }
}
