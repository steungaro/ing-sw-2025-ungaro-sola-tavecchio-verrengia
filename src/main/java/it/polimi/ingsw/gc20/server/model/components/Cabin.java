package it.polimi.ingsw.gc20.server.model.components;

import it.polimi.ingsw.gc20.server.exceptions.EmptyCabinException;
import it.polimi.ingsw.gc20.server.exceptions.InvalidAlienPlacement;
import it.polimi.ingsw.gc20.server.model.ship.NormalShip;
import it.polimi.ingsw.gc20.server.model.ship.Ship;
import it.polimi.ingsw.gc20.server.model.ship.Tile;

import java.util.Map;

public class Cabin extends Component {
    private int astronauts;
    private boolean alien = false;
    private AlienColor alienColor = AlienColor.NONE;
    private AlienColor cabinColor = AlienColor.NONE;

    public Cabin() {}

    /**
     * Function that returns the astronauts in the cabin.
     * @return the astronauts in the cabin
     */
    public int getAstronauts() {
        return astronauts;
    }

    /**
    * Function that sets the astronauts in the cabin.
    * @param astronauts the astronauts to set
     */
    public void setAstronauts(int astronauts) {
        this.astronauts += astronauts;
    }

    /**
    * Getter method for alien parameter.
    * @return the alien color in the cabin
     */
    public boolean getAlien() {
        return alien;
    }

    /**
    * Function that sets the alien in the cabin.
    * @param color is the color of the alien
     * @throws InvalidAlienPlacement if the alien is not the same color as the cabin
     */
    public void setAlien(AlienColor color) throws InvalidAlienPlacement {
        if (cabinColor != color && cabinColor != AlienColor.BOTH) {
            throw new InvalidAlienPlacement("Cannot have " + color + " alien in " + cabinColor + "cabin.");
        }
        alien = true;
        alienColor = color;
    }

    /**
     * Function that returns the color of the alien in the cabin.
     * @return the color of the alien
     */
    public AlienColor getAlienColor() {
        return alienColor;
    }

    /**
     * Function that returns the color of the alien in the cabin.
     * @return the color of the cabin
     */
    public AlienColor getCabinColor() {
        return cabinColor;
    }

    /**
     * Function that sets the color of the cabin.
     * @param color is the color to set
     * @implNote if the cabin already has a different color, it will be set to BOTH
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
    * Function that unloads one alien from the cabin.
     */
    public void unloadAlien() throws InvalidAlienPlacement {
        alien = false;
        alienColor = AlienColor.NONE;
    }

    /**
     * Function that adds support to the cabin.
     * @param ls the lifeSupport that's added
     */
    public void addSupport(LifeSupport ls) {
        if (cabinColor != AlienColor.NONE && cabinColor != ls.getColor()) { 
            cabinColor = AlienColor.BOTH;
        } else {
            cabinColor = ls.getColor();
        }
    }

    /**
     * Function that removes support to the cabin.
     * @param ls the lifeSupport that's removed
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
     * Function that returns the number of astronauts and aliens in the cabin.
     * @return the number of astronauts and aliens in the cabin
     */
    public int getOccupants(){
        return astronauts + (alien ? 1 : 0);
    }

    /**
     * Function that initialize the astronauts in the cabin.
     * @return the number of astronauts in the cabin
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
     * Function that update the parameter of the ship.
     * @param s ship that is updating his parameter
     * @param sign integer that indicate if the parameter is increasing or decreasing
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
                    LifeSupport comp = (LifeSupport) table[row][col].getComponent();

                    if (comp == null) {
                        continue;
                    }
                    if (entry.getValue() == ConnectorEnum.ZERO || !(comp.isLifeSupport())) {
                        continue;
                    }

                    if (this.isValid(comp, entry.getKey())) {
                        this.addSupport(comp);
                    }
                }
            }
        }
    }

    /**
     * Function that returns true if the component is a cabin.
     * @return true if the component is a cabin, false otherwise
     */
    @Override
    public boolean isCabin() {
        return true;
    }
}