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

/**
 * Represents an AdventureCard which is part of a game model.
 * The AdventureCard has various properties and methods to define its behavior
 * during the course of a game. It includes attributes such as level, crew,
 * credits, reward system, and operational states among others.
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





    /**
     * Default constructor
     */
    public AdventureCard() {
    }

    /**
     * Updates the level of the AdventureCard with the provided value.
     *
     * @param level the new level to set for the card
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Retrieves the level of the AdventureCard.
     *
     * @return the current level of the card
     */
    public int getLevel() {
        return level;
    }

    /**
     * Sets the ID card value for the AdventureCard.
     *
     * @param IDCard the unique integer identifier of the AdventureCard
     */
    public void setIDCard(int IDCard) {
        this.IDCard = IDCard;
    }

    /**
     * Retrieves the unique identifier for the AdventureCard.
     *
     * @return the ID of the AdventureCard as an integer
     */
    public int getIDCard() {
        return IDCard;
    }

    /**
     * Retrieves the name of the AdventureCard.
     *
     * @return the name of the AdventureCard as a string
     */
    public String getName() {
        return name;
    }

    /**
     * Updates the name of the AdventureCard with the provided value.
     *
     * @param name the new name to set for the AdventureCard
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the played status of the AdventureCard.
     *
     * @param played the new played status to set for the card; true if the card has been played, false otherwise
     */
    public void setPlayed(boolean played) {
        this.played = played;
    }

    /**
     * Retrieves the crew value associated with this AdventureCard.
     *
     * @return the number of crew members required or associated with the AdventureCard
     */
    public int getCrew() {
        return crew;
    }

    /**
     * Sets the crew value for the AdventureCard.
     *
     * @param crew the number of crew members to associate with the AdventureCard
     */
    public void setCrew(int crew) {
        this.crew = crew;
    }

    /**
     * Retrieves the number of credits associated with this AdventureCard.
     *
     * @return the credits value as an integer
     */
    public int getCredits() {
        return credits;
    }

    /**
     * Sets the credits' value for the AdventureCard.
     *
     * @param credits the number of credits to associate with the AdventureCard
     */
    public void setCredits(int credits) {
        this.credits = credits;
    }

    /**
     * Retrieves the number of lost days associated with this AdventureCard.
     *
     * @return the number of lost days as an integer
     */
    public int getLostDays() {
        return lostDays;
    }

    /**
     * Sets the number of days lost for the AdventureCard.
     *
     * @param lostDays the number of days to set as lost, represented as an integer
     */
    public void setLostDays(int lostDays) {
        this.lostDays = lostDays;
    }

    /**
     * Retrieves the reward list associated with this AdventureCard.
     *
     * @return a list of CargoColor enumerations representing the rewards of the AdventureCard
     */
    public List<CargoColor> getReward() {
        return reward;
    }

    /**
     * Sets the reward list for the AdventureCard.
     *
     * @param reward a list of CargoColor enumerations representing the rewards to be associated with the AdventureCard
     */
    public void setReward(List<CargoColor> reward) {
        this.reward = reward;
    }

    /**
     * Retrieves the amount of cargo lost associated with this AdventureCard.
     *
     * @return the lost cargo value as an integer
     */
    public int getLostCargo() {
        return lostCargo;
    }

    /**
     * Sets the lost cargo value for the AdventureCard.
     *
     * @param lostCargo the amount of cargo lost, represented as an integer
     */
    public void setLostCargo(int lostCargo) {
        this.lostCargo = lostCargo;
    }

    /**
     * Retrieves the list of projectiles associated with the AdventureCard.
     *
     * @return a list of Projectile objects representing the projectiles associated with the AdventureCard
     */
    public List<Projectile> getProjectiles() {
        return projectiles;
    }

    /**
     * Sets the list of projectiles associated with the AdventureCard.
     *
     * @param projectiles the list of Projectile objects to associate with the AdventureCard
     */
    public void setProjectiles(List<Projectile> projectiles) {
        this.projectiles = projectiles;
    }

    /**
     * Retrieves the firepower value associated with this AdventureCard.
     *
     * @return the firepower value as an integer
     */
    public int getFirePower() {
        return firePower;
    }

    /**
     * Sets the firepower value for the AdventureCard.
     *
     * @param firePower the firepower value to be set for the AdventureCard
     */
    public void setFirePower(int firePower) {
        this.firePower = firePower;
    }

    /**
     * Retrieves the list of planets associated with the AdventureCard.
     *
     * @return a list of Planet objects representing the planets associated with the AdventureCard
     */
    public List<Planet> getPlanets() {
        return planets;
    }

    /**
     * Sets the list of planets associated with the AdventureCard.
     *
     * @param planets the list of Planet objects to associate with the AdventureCard
     */
    public void setPlanets(List<Planet> planets) {
        this.planets = planets;
    }

    /**
     * Checks whether the AdventureCard has been played.
     *
     * @return true if the card has been played, false otherwise
     */
    public Boolean isPlayed(){
        return this.played;
    }


    /**
     * Sets the state of the GameController based on the current AdventureCard.
     * Dynamically constructs the appropriate state class based on the AdventureCard's
     * name and initializes the controller's state with an instance of this class.
     * Handles potential exceptions during class loading and instantiation.
     *
     * @param controller the GameController instance whose state is to be modified
     * @param model the GameModel instance used as context for initializing the new state
     */
    public void setState(GameController controller, GameModel model){
        try {
            if(Objects.equals(name, "CombatZone")){
                name = name + combatType();
            }
            // Construct the full class name with the package
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
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException |
                 InvocationTargetException _) {
        }
    }

    /**
     * Marks the AdventureCard as played by updating its internal state.
     * This method sets the "played" attribute of the AdventureCard to true,
     * indicating that the card has been used in the game.
     */
    public void playCard() {
        this.played = true;
    }

    /**
     * Determines the combat type of the AdventureCard based on the crew value.
     *
     * @return 0 if the card has a crew value greater than 0,
     *         1 if the card has no crew (crew is 0 or less).
     */
    public int combatType() {
        if (crew > 0) {
            return 0;
        } else {
            return 1;
        }
    }

}