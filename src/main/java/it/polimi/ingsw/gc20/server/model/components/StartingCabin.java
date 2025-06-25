package it.polimi.ingsw.gc20.server.model.components;

import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewStartingCabin;
import it.polimi.ingsw.gc20.server.exceptions.InvalidAlienPlacement;
import it.polimi.ingsw.gc20.server.model.player.PlayerColor;

/**
 * The StartingCabin class represents a special type of cabin that is used at the
 * starting point of the game. This cabin has specific rules:
 * - No alien can be placed, unloaded, or exist within this cabin.
 * - It has default initialization behavior that distinguishes it from other cabin types.
 * <p>
 * The class is responsible for enforcing these rules through its overridden methods.
 */
public class StartingCabin extends Cabin {

    /**
     * Constructor for the StartingCabin class.
     * Initializes the cabin with an ID based on the specified PlayerColor.
     * The ID is calculated as 1000 plus the ordinal value of the PlayerColor.
     *
     * @param playerColor the color of the player associated with this starting cabin
     */
    public StartingCabin(PlayerColor playerColor) {
        this.ID = 1000 + playerColor.ordinal();
    }

    /**
     * Default constructor for the StartingCabin class.
     * Initializes the cabin with a default ID of 1000, representing the blue starting cabin.
     * This constructor is used to create an instance of the StartingCabin with
     * standard initial settings.
     */
    public StartingCabin() {
        this.ID = 1000; // Default ID for the starting cabin
    }

    /**
     * Attempts to set an alien in the starting cabin. This operation is not allowed
     * as the starting cabin cannot contain aliens. The method will always throw
     * an exception.
     *
     * @param color the color of the alien that is attempted to be set in the cabin
     * @throws InvalidAlienPlacement if trying to place an alien in the starting cabin
     */
    @Override
    public void setAlien(AlienColor color) throws InvalidAlienPlacement {
        throw new InvalidAlienPlacement("Cannot set an alien in the starting cabin");
    }

    /**
     * Checks whether the cabin contains an alien.
     * This implementation always returns false because the starting cabin cannot contain aliens.
     *
     * @return false, as the starting cabin cannot have an alien
     */
    @Override
    public boolean getAlien() {
        return false;
    }

    /**
     * Retrieves the color of the alien associated with the starting cabin.
     * In the context of the starting cabin, this method always returns AlienColor.NONE
     * because starting cabins cannot contain aliens.
     *
     * @return AlienColor.NONE, indicating that the starting cabin does not have a color associated with an alien.
     */
    @Override
    public AlienColor getAlienColor() {
        return AlienColor.NONE;
    }

    /**
     * Unloads an alien from the starting cabin.
     * <p>
     * This operation is not allowed as the starting cabin cannot contain or unload aliens.
     * The method will always throw an InvalidAlienPlacement exception to enforce this restriction.
     *
     * @throws InvalidAlienPlacement if attempting to unload an alien from the starting cabin
     */
    @Override
    public void unloadAlien() throws InvalidAlienPlacement {
        throw new InvalidAlienPlacement("Cannot unload an alien in the starting cabin");
    }

    /**
     * Adds a LifeSupport component to the cabin. This method sets the cabin's color to {@code AlienColor.NONE}
     * when a LifeSupport component is added.
     *
     * @param ls the LifeSupport component to be added
     */
    @Override
    public void addSupport(LifeSupport ls) {
        super.setColor(AlienColor.NONE);
    }

    /**
     * Removes a LifeSupport component from the cabin. When a LifeSupport component
     * is removed, the cabin's color is set to AlienColor.NONE.
     *
     * @param ls the LifeSupport component to be removed
     */
    @Override
    public void removeSupport(LifeSupport ls) {
        super.setColor(AlienColor.NONE);
    }

    public ViewComponent createViewComponent (){
        ViewStartingCabin viewStartingCabin = new ViewStartingCabin();
        viewStartingCabin.astronauts = getAstronauts();
        viewStartingCabin.alien = getAlien();
        viewStartingCabin.alienColor = getAlienColor();
        viewStartingCabin.cabinColor = getCabinColor();
        initializeViewComponent(viewStartingCabin);
        return viewStartingCabin;
    }

}