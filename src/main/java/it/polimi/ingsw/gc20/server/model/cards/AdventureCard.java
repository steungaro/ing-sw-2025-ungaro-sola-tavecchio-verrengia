package it.polimi.ingsw.gc20.server.model.cards;

import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.controller.states.State;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.server.model.gamesets.GameModel;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * @author GC20
 */

public class AdventureCard implements Serializable {
    private String name;
    private int level;
    private int IDCard;
    private boolean played;
    private int crew;
    private int credits;
    private int lostDays;
    private List<CargoColor> reward;
    private int lostCargo;
    private List<Projectile> projectiles;
    private int firePower;
    private List<Planet> planets;
    private final Logger logger = Logger.getLogger(AdventureCard.class.getName());





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

    public int getCrew() {
        return crew;
    }

    public void setCrew(int crew) {
        this.crew = crew;
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

    public List<Projectile> getProjectiles() {
        return projectiles;
    }

    public void setProjectiles(List<Projectile> projectiles) {
        this.projectiles = projectiles;
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

    public Boolean isPlayed(){
        return this.played;
    }


    public void setState(GameController controller, GameModel model){
        try {
            if(Objects.equals(name, "CombatZone")){
                name = name + combatType();
            }
            // Construct the full class name with package
            String stateClassName = "it.polimi.ingsw.gc20.server.controller.states." + name + "State";
            // Get the class object for the state
            Class<?> stateClass = Class.forName(stateClassName);

            // Get the constructor that takes GameModel, GameController, and AdventureCard
            Constructor<?> constructor = stateClass.getConstructor(
                    GameModel.class, GameController.class, AdventureCard.class);

            // Instantiate the state using the constructor
            Object stateInstance = constructor.newInstance(model, controller, this);

            // Set the state in the controller
            controller.setState((State) stateInstance);
        } catch (ClassNotFoundException e) {
            logger.severe("Error: State class not found for card: " + name);
        } catch (NoSuchMethodException | IllegalAccessException |
                 InstantiationException | InvocationTargetException e) {
            logger.severe("Error: Unable to instantiate state for card: " + name);
        }
    }

    /**
     * Play the card
     */
    public void playCard() {
        this.played = true;
    }

    /**
     * @return the type of combat
     * @implNote if the card has lostCrew > 0, the method returns 0, else 1
     */
    public int combatType() {
        if (crew > 0) {
            return 0;
        } else {
            return 1;
        }
    }

}