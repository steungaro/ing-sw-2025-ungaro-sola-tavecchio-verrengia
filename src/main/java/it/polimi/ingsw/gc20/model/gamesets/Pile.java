package it.polimi.ingsw.gc20.model.gamesets;

import it.polimi.ingsw.gc20.model.components.Component;

import java.util.*;

/**
 * @author GC20
 */
public class Pile {

    private final List<Component> viewed;
    private final List<Component> unviewed;
    /**
     * Default constructor
     */
    public Pile() {
        this.viewed = new ArrayList<>();
        this.unviewed = new ArrayList<>();
    }

    /** get function for viewed components
     * @return List<Component>
     */
    public List<Component> getViewed() {
        return this.viewed;
    }

    /** get function for unviewed components
     * @return List<Component>
     */
    public List<Component> getUnviewed() {
        return this.unviewed;
    }

    /** function that remove a component from the viewed list, if not present throws exception
     * @param c component to remove
     * @throws NoSuchElementException if the component is not present in the viewed list
     */
    public void removeViewed(Component c) throws NoSuchElementException{
        // check if element is present in the viewed list
        if (this.viewed.contains(c)) {
            // if present remove it
            this.viewed.remove(c);
        }else { //if not present throw exception
            throw new NoSuchElementException("Component not found in viewed list");
        }
    }
    /** function that remove a component from the unviewed list, if not present throws exception
     * @param c component to remove
     * @throws NoSuchElementException if the component is not present in the unviewed list
     */
    public void removeUnviewed(Component c) throws NoSuchElementException {
        // check if element is present in the unviewed list
        if (this.unviewed.contains(c)) {
            //if present remove it
            this.unviewed.remove(c);
        }else { //if not present throw exception
            throw new NoSuchElementException("Component not found in unviewed list");
        }
    }

    /** function that add a component to the viewed list, if already present throws exception
     * @param c component to add
     * @throws IllegalArgumentException if the component is already present in the viewed list
     */
    public void addViewed(Component c) throws IllegalArgumentException {
        // check if element is already present in the viewed list
        if (this.viewed.contains(c)) {
            //if present throw exception
            throw new IllegalArgumentException("Component already present in viewed list");
        } else {
            //if not present add it
            this.viewed.add(c);
        }
    }

    /** function that create the unviewed list
     * @param c list of components to add
     */
    public void addUnviewed(List<Component> c) {
        //add all the components to the unviewed list
        this.unviewed.addAll(c);
    }

}