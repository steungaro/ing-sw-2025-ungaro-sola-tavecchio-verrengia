package it.polimi.ingsw.gc20.server.model.components;

import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewCabin;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.server.exceptions.EmptyCabinException;
import it.polimi.ingsw.gc20.server.exceptions.InvalidAlienPlacement;
import it.polimi.ingsw.gc20.server.model.ship.NormalShip;
import it.polimi.ingsw.gc20.server.model.ship.Ship;
import it.polimi.ingsw.gc20.server.model.ship.Tile;

import java.util.Map;

/**
 * Represents a cabin in the ship, capable of housing astronauts and aliens.
 * The cabin can have a specific color and interact with life support systems.
 * It manages its occupants, specifically astronauts and aliens,
 * while ensuring compatibility with its constraints.
 */
public class Cabin extends Component {
    private int astronauts;
    private boolean alien = false;
    private AlienColor alienColor = AlienColor.NONE;
    private AlienColor cabinColor = AlienColor.NONE;

    public Cabin() {}

    /**
     * Retrieves the number of astronauts currently present in the cabin.
     *
     * @return the number of astronauts in the cabin
     */
    public int getAstronauts() {
        return astronauts;
    }

    /**
     * Updates the number of astronauts in the cabin by adding the specified value.
     *
     * @param astronauts the number of astronauts to add to the cabin
     */
    public void setAstronauts(int astronauts) {
        this.astronauts += astronauts;
    }

    /**
     * Returns whether an alien is present in the cabin.
     *
     * @return true if an alien is present in the cabin, false otherwise
     */
    public boolean getAlien() {
        return alien;
    }

    /**
     * Sets the alien color for the cabin. Ensures that the alien can only be placed
     * in a cabin of a compatible color. Throws an exception if the color of the alien
     * is incompatible with the cabin's color.
     *
     * @param color the color of the alien to be set in the cabin
     * @throws InvalidAlienPlacement if the alien's color is incompatible with the cabin's color
     */
    public void setAlien(AlienColor color) throws InvalidAlienPlacement {
        if (cabinColor != color && cabinColor != AlienColor.BOTH) {
            throw new InvalidAlienPlacement("Cannot have " + color + " alien in " + cabinColor + "cabin.");
        }
        alien = true;
        alienColor = color;
    }

    /**
     * Retrieves the color of the alien associated with the cabin.
     *
     * @return the color of the alien, represented as an instance of AlienColor
     */
    public AlienColor getAlienColor() {
        return alienColor;
    }

    /**
     * Retrieves the current color of the cabin.
     *
     * @return the color of the cabin as an instance of AlienColor
     */
    public AlienColor getCabinColor() {
        return cabinColor;
    }

    /**
     * Sets the color of the cabin. If the cabin color is already set to a value other than NONE
     * and the new color differs from the existing one, the cabin color will be updated to BOTH.
     * Otherwise, the cabin color will be updated to the specified color.
     *
     * @param color the color to set for the cabin
     */
    public void setColor(AlienColor color) {
        if (cabinColor != AlienColor.NONE && cabinColor != color) {
            cabinColor = AlienColor.BOTH;
        } else {
            cabinColor = color;
        }
    }

    /**
     * Function that unloads one astronaut from the cabin.
     */
    public void unloadAstronaut() {
        astronauts--;
    }

    /**
     * Unloads the alien from the cabin by resetting the associated properties.
     * Specifically, this method sets the alien presence to false and the alien's color to NONE.
     *
     * @throws InvalidAlienPlacement if the current operation violates any placement constraints.
     */
    public void unloadAlien() throws InvalidAlienPlacement {
        alien = false;
        alienColor = AlienColor.NONE;
    }

    /**
     * Adds the given life support to the cabin. Updates the cabin's color based on the color of the provided life support.
     * If the cabin's color is not NONE and differs from the life support's color, the cabin's color is set to BOTH.
     * Otherwise, the cabin's color is updated to match the life support's color.
     *
     * @param ls the LifeSupport instance to add to the cabin
     */
    public void addSupport(LifeSupport ls) {
        if (cabinColor != AlienColor.NONE && cabinColor != ls.getColor()) { 
            cabinColor = AlienColor.BOTH;
        } else {
            cabinColor = ls.getColor();
        }
    }

    /**
     * Removes the specified life support from the cabin. Updates the cabin's color
     * and alien-related properties based on the color of the given life support
     * and the current state of the cabin.
     *
     * @param ls the LifeSupport instance to be removed from the cabin
     */
    public void removeSupport(LifeSupport ls){
        if (cabinColor == AlienColor.BOTH) {
            cabinColor = ls.getColor() == AlienColor.BROWN ? AlienColor.PURPLE : AlienColor.BROWN;
        } else {
            cabinColor = AlienColor.NONE;
        }
        if (alien && cabinColor != alienColor ) {
            alien = false;
        }
    }

    /**
     * Calculates and retrieves the total number of occupants in the cabin.
     * The occupants include the astronauts and, if present, the alien.
     *
     * @return the total number of occupants in the cabin as an integer
     */
    public int getOccupants(){
        return astronauts + (alien ? 1 : 0);
    }

    /**
     * Initializes the number of astronauts in the cabin based on the alien presence.
     * If no alien is present, sets the number of astronauts to 2 in the cabin.
     * If an alien is present, no astronauts are added.
     *
     * @return the number of astronauts initialized in the cabin. If no alien is present, returns 2; otherwise, returns 0.
     */
    @Override
    public int initializeAstronauts (){
        if (!alien) {
            setAstronauts(2);
            return 2;
        }
        return 0;
    }

    /**
     * Updates the parameters of the cabin based on the provided ship and sign value.
     *
     * @param s the ship where the cabin is currently located
     * @param sign the direction of the update; use a positive value to add support,
     *             and a negative value to unload crew until the cabin is empty
     */
    @Override
    public void updateParameter (Ship s, int sign){
        if (sign<0){
            while (astronauts>0){
                try {
                    s.unloadCrew(this);
                } catch (EmptyCabinException _) {}
            }
        } else if (sign>0){
            if (!s.isNormal()) return;

            //find the coordinates of the cabin
            Tile[][] table = ((NormalShip) s).getTable();
            int [] position = s.findComponent(this);

            //found the cabin
            if (position!=null){
                Map<Direction, ConnectorEnum> connectors = this.getConnectors();
                for (Map.Entry<Direction, ConnectorEnum> entry : connectors.entrySet()) {
                    int row = position[0];
                    int col = position[1];

                    switch (entry.getKey()) {
                        case UP: row--; break;
                        case DOWN: row++; break;
                        case LEFT: col--; break;
                        case RIGHT: col++; break;
                    }

                    if (row < 0 || row >= table.length || col < 0 || col >= table[0].length) {
                        continue; // Skip out-of-bounds indices
                    }

                    Component comp = table[row][col].getComponent();

                    if (comp == null) {
                        continue;
                    }
                    if (entry.getValue() == ConnectorEnum.ZERO || !(comp.isLifeSupport())) {
                        continue;
                    }

                    if (comp.isLifeSupport()) {
                        if (this.isValid(comp, entry.getKey())) {
                            this.addSupport((LifeSupport)(comp));
                        }
                    }
                }
            }
        }
    }

    /**
     * Determines if this component is a cabin.
     *
     * @return true if this component is a cabin, false otherwise
     */
    @Override
    public boolean isCabin() {
        return true;
    }

    /**
     * Creates and initializes a view component for the cabin.
     * The component is initialized with the current astronauts, alien, alien color, and cabin color values.
     *
     * @return the created and initialized view component for the cabin
     */
    @Override
    public ViewComponent createViewComponent (){
        ViewCabin viewCabin = new ViewCabin();
        viewCabin.astronauts = astronauts;
        viewCabin.alien = alien;
        viewCabin.alienColor = alienColor;
        viewCabin.cabinColor = cabinColor;
        initializeViewComponent( viewCabin);
        return viewCabin;
    }
}