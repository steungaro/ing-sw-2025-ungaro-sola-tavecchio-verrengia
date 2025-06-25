package it.polimi.ingsw.gc20.server.model.gamesets;

import it.polimi.ingsw.gc20.server.exceptions.ComponentNotFoundException;
import it.polimi.ingsw.gc20.server.exceptions.DuplicateComponentException;
import it.polimi.ingsw.gc20.server.model.components.Component;

import java.util.*;

/**
 * The Pile class represents a collection of components, separated into two categories:
 * viewed and unviewed. It provides functionality to manage, add, and remove components
 * in each category, ensuring certain constraints such as uniqueness and presence when
 * performing operations.
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

    /**
     * Retrieves the list of components that have been marked as viewed.
     *
     * @return a list of components that have been viewed.
     */
    public List<Component> getViewed() {
        return this.viewed;
    }

    /**
     * Retrieves the list of components that have not been viewed.
     *
     * @return a list of components that are classified as unviewed.
     */
    public List<Component> getUnviewed() {
        return this.unviewed;
    }

    /**
     * Removes a specified component from the viewed list.
     * If the component is not present in the viewed list, a {@code ComponentNotFoundException} is thrown.
     *
     * @param c the component to be removed from the viewed list
     * @throws ComponentNotFoundException if the component is not found in the viewed list
     */
    public void removeViewed(Component c) throws ComponentNotFoundException {
        // check if an element is present in the viewed list
        if (this.viewed.contains(c)) {
            // if present, remove it
            this.viewed.remove(c);
        }else { //if not present, throw exception
            throw new ComponentNotFoundException("Component not found in viewed list");
        }
    }
    /**
     * Removes a specified component from the unviewed list.
     * If the component is not present in the unviewed list, a {@code ComponentNotFoundException} is thrown.
     *
     * @param c the component to be removed from the unviewed list
     * @throws ComponentNotFoundException if the component is not found in the unviewed list
     */
    public void removeUnviewed(Component c) throws ComponentNotFoundException {
        // check if an element is present in the unviewed list
        if (this.unviewed.contains(c)) {
            //if present, remove it
            this.unviewed.remove(c);
        }else { //if not present, throw exception
            throw new ComponentNotFoundException("Component not found in unviewed list");
        }
    }

    /**
     * Adds the specified component to the viewed list. If the component is already present in the viewed list,
     * a {@code DuplicateComponentException} is thrown.
     *
     * @param c the component to be added to the viewed list
     * @throws DuplicateComponentException if the component is already present in the viewed list
     */
    public void addViewed(Component c) throws DuplicateComponentException {
        // check if an element is already present in the viewed list
        if (this.viewed.contains(c)) {
            //if present, throw exception
            throw new DuplicateComponentException("Component already present in viewed list");
        } else {
            //if not present, add it
            this.viewed.add(c);
        }
    }

    /**
     * Adds a list of components to the unviewed list. The components in the given list
     * are randomized before being added.
     *
     * @param c the list of components to be added to the unviewed list
     */
    public void addUnviewed(List<Component> c) {
        //add all the components to the unviewed list
        Collections.shuffle (c);
        this.unviewed.addAll(c);
    }

}