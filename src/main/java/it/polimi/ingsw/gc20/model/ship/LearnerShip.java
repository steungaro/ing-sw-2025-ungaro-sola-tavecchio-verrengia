package it.polimi.ingsw.gc20.model.ship;

import it.polimi.ingsw.gc20.model.components.*;
/**
 * @author GC20
 */
public class LearnerShip extends Ship {

    private Tile[][] table = new Tile[5][5];


    public LearnerShip() {
        super();

        // Init table
        for (int i=0; i<5; i++) {
            for (int j=0; j<5; j++) {
                table[i][j] = new Tile();
            }
        }

        table[0][0].setAvailability(false);
        table[0][1].setAvailability(false);
        table[0][3].setAvailability(false);
        table[0][4].setAvailability(false);
        table[1][0].setAvailability(false);
        table[1][4].setAvailability(false);
        table[4][2].setAvailability(false);

        Component sc = new StartingCabin();
        table[2][2].addComponent(sc);
        table[2][2].setAvailability(false);
    }

    /**
     * Getter for the number of rows
     * @return rows
     */
    public Integer getRows(){
        return 5;
    }

    /**
     * Getter for the number of columns
     * @return cols
     */
    public Integer getCols(){
        return 5;
    }

    /**
     * @param row: position of the component
     * @param col: position of the component
     * @return the component at the given position
     */
    @Override
    protected  Component getComponentAt(int row, int col) {
        if (row >= 0 && row < getRows() && col >= 0 && col < getCols()) {
            return table[row][col].getComponent();
        }
        return null;
    }

    /**
     * Add a component to the ship
     * @param c: the component to be added
     * @param row: position of the component
     * @param col: position of the component
     */
    @Override
    protected void setComponentAt(Component c, int row, int col) {
        if (row >= 0 && row < getRows() && col >= 0 && col < getCols()) {
            table[row][col].addComponent(c);
        }
    }

    /**
     * Adds a component to the ship at the specified position and updates ship parameters
     * @param c Component to add
     * @param row Row position
     * @param col Column position
     */
    public void addComponent(Component c, int row, int col){
        if (row >= 0 && row < getRows() && col >= 0 && col < getCols()) {
            setComponentAt( c, row, col);
            updateParametersSet(c);
            c.setTile(table[row][col]);
        }
    }

    @Override
    public void unloadCrew(Cabin c) {
        if (c.getAstronauts() < 1) {
            throw new IllegalArgumentException("Empty cabin");
        }
        c.unloadAstronaut();
        astronauts--;
    }

    /**
     * Updates ship parameters when components are removed
     * @param c Component being removed
     */
    @Override
    protected void updateParametersRemove(Component c){
        if(c instanceof Cannon){
            if(((Cannon) c).getOrientation()==Direction.UP){
                if(((Cannon) c).getPower() == 1){
                    singleCannonsPower--;
                }else{
                    doubleCannonsPower -= 2;
                }
            }else{
                if(((Cannon) c).getPower() == 2) {
                    doubleCannons--;
                    doubleCannonsPower--;
                }else if(((Cannon) c).getPower() == 1){
                    singleCannonsPower -= 0.5f;
                }
            }
        }else if(c instanceof Engine){
            if(((Engine) c).getDoublePower()){
                doubleEngines--;
            }else{
                singleEngines--;
            }
        }else if(c instanceof Battery){
            totalEnergy -= ((Battery) c).getAvailableEnergy();
        } else if (c instanceof Cabin) {
            astronauts -= ((Cabin) c).getOccupants();
        } else if (c instanceof CargoHold) {
            ((CargoHold) c).getCargoHeld().forEach((k, v) -> {
                Integer current = cargos.getOrDefault(k, 0);
                cargos.put(k, current - v);
            });
        }
    }
}