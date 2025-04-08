package it.polimi.ingsw.gc20.model.cards;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import it.polimi.ingsw.gc20.controller.GameController;
import it.polimi.ingsw.gc20.controller.states.*;
import it.polimi.ingsw.gc20.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.model.gamesets.GameModel;

import java.util.List;

/**
 * @author GC20
 */

public abstract class AdventureCard {
    private String name;
    private int level;
    private int IDCard;
    private boolean played;
    private int lostCrew;
    private int credits;
    private int lostDays;
    private int crewNeeded;
    private List<CargoColor> reward;
    private int lostCargo;
    private List<Projectile> cannonFire;
    private List<Projectile> meteors;
    private int firePower;
    private List<Planet> planets;
    private int lostMembers;






    /**
     * Default constructor
     */
    public AdventureCard() {
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public void setIDCard(int IDCard) {
        this.IDCard = IDCard;
    }

    public int getIDCard() {
        return IDCard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlayed(boolean played) {
        this.played = played;
    }

    public int getLostCrew() {
        return lostCrew;
    }

    public void setLostCrew(int lostCrew) {
        this.lostCrew = lostCrew;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public int getLostDays() {
        return lostDays;
    }

    public void setLostDays(int lostDays) {
        this.lostDays = lostDays;
    }

    public int getCrewNeeded() {
        return crewNeeded;
    }

    public void setCrewNeeded(int crewNeeded) {
        this.crewNeeded = crewNeeded;
    }

    public List<CargoColor> getReward() {
        return reward;
    }

    public void setReward(List<CargoColor> reward) {
        this.reward = reward;
    }

    public int getLostCargo() {
        return lostCargo;
    }

    public void setLostCargo(int lostCargo) {
        this.lostCargo = lostCargo;
    }

    public List<Projectile> getCannonFire() {
        return cannonFire;
    }

    public void setCannonFire(List<Projectile> cannonFire) {
        this.cannonFire = cannonFire;
    }

    public List<Projectile> getMeteors() {
        return meteors;
    }

    public void setMeteors(List<Projectile> meteors) {
        this.meteors = meteors;
    }

    public int getFirePower() {
        return firePower;
    }

    public void setFirePower(int firePower) {
        this.firePower = firePower;
    }

    public List<Planet> getPlanets() {
        return planets;
    }

    public void setPlanets(List<Planet> planets) {
        this.planets = planets;
    }

    public int getLostMembers() {
        return lostMembers;
    }

    public void setLostMembers(int lostMembers) {
        this.lostMembers = lostMembers;
    }

    public Boolean isPlayed(){
        return this.played;
    }


    public void setState(GameController controller, GameModel model){
        //TODO implement this method
    }

    /**
     * Play the card
     */
    public void playCard() {
        this.played = true;
    }

    /**
     * @return the type of combat
     * @implNote if the card has lostCrew > 0, the method returns 1, else 0
     */
    public int combatType() {
        if (lostCrew > 0) {
            return 1;
        } else {
            return 0;
        }
    }

}