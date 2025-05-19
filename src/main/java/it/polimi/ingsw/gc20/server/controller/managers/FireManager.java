package it.polimi.ingsw.gc20.server.controller.managers;

import it.polimi.ingsw.gc20.server.exceptions.*;
import it.polimi.ingsw.gc20.server.model.cards.FireType;
import it.polimi.ingsw.gc20.server.model.cards.Projectile;
import it.polimi.ingsw.gc20.server.model.components.Battery;
import it.polimi.ingsw.gc20.server.model.components.Cannon;
import it.polimi.ingsw.gc20.server.model.components.ConnectorEnum;
import it.polimi.ingsw.gc20.server.model.components.Shield;
import it.polimi.ingsw.gc20.server.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.server.model.player.Player;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.List;

public class FireManager {
    private final List<Projectile> fires;
    private boolean skipNextFire;
    private final GameModel gm;
    private final Validator validator;
    final Player player;
    public FireManager(GameModel model, List<Projectile> fires, Player p) {
        this.fires = fires;
        this.skipNextFire = false;
        this.gm = model;
        this.player = p;
        this.validator = new Validator();
    }

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
        if (gm.heavyMeteorCannon(player, gm.getGame().rollDice(), fires.getFirst()).contains(cannon)) {
            skipNextFire = true;
        }
    }

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
            gm.Fire(player, dice, fire);
        } catch (InvalidShipException e) {
            validator.setSplit();
            throw e;
        }
    }

    public boolean finished() {
        if(fires == null)
            return true;
        return fires.isEmpty();
    }

    public boolean isSplit() {
        return validator.isSplit();
    }

    /** posso modificare questa funzione in modo che ritorni il tipo di proeittile e null se è finito
     * @return il tipo di proiettile
     */
    public FireType getFirstProjectile() {
        if (fires.isEmpty()) {
            return null;
        }
        return fires.getFirst().getFireType();
    }

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
}
