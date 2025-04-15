package it.polimi.ingsw.gc20.controller.managers;

import it.polimi.ingsw.gc20.exceptions.InvalidShipException;
import it.polimi.ingsw.gc20.model.cards.FireType;
import it.polimi.ingsw.gc20.model.cards.Projectile;
import it.polimi.ingsw.gc20.model.components.*;
import it.polimi.ingsw.gc20.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.model.player.Player;

import java.util.ArrayList;
import java.util.List;

public class FireManager {
    private List<Projectile> fires;
    private boolean skipNextFire;
    private GameModel gm;
    Player player;

    public FireManager(GameModel model, List<Projectile> fires, Player p) {
        this.fires = fires;
        this.skipNextFire = false;
        this.gm = model;
        this.player = p;
    }

    public void activateCannon(Cannon cannon, Battery battery) {
        if (fires.getFirst().getFireType() != FireType.HEAVY_METEOR) {
            throw new IllegalStateException("Cannot activate cannon in this state");
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

    public void activateShield(Shield shield, Battery battery) {
        if (fires.getFirst().getFireType() != FireType.LIGHT_METEOR && fires.getFirst().getFireType() != FireType.LIGHT_FIRE) {
            throw new IllegalStateException("Cannot activate shield in this state");
        }
        List<Battery> batteries = new ArrayList<>();
        if (battery == null || shield == null) {
            skipNextFire = false;
            return;
        }
        batteries.add(battery);
        gm.removeEnergy(player, batteries);
        if (shield.getCoveredSides()[0] == fires.getFirst().getDirection() || shield.getCoveredSides()[1] == fires.getFirst().getDirection()) {
            skipNextFire = true;
        }
    }

    public boolean fire() {
        if (skipNextFire) {
            skipNextFire = false;
            return false;
        }
        if (fires.isEmpty()) {
            return false;
        }
        Projectile fire = fires.getFirst();
        int dice = gm.getGame().lastRolled();
        if (fire.getFireType() != FireType.LIGHT_METEOR && player.getShip().getFirstComponent(fire.getDirection(), dice).getConnectors().get(fire.getDirection()) !=
        ConnectorEnum.ZERO) {
            return false;
        }
        try {
            gm.Fire(player, dice, fire);
        } catch (InvalidShipException e) {
            return true;
        }
        return false;
    }

    public boolean finished() {
        return fires.isEmpty();
    }

    public boolean isFirstHeavyFire() {
        if (fires.isEmpty()) {
            return false;
        }
        return fires.getFirst().getFireType() == FireType.HEAVY_FIRE;
    }
}
