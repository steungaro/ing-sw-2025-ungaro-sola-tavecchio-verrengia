package it.polimi.ingsw.gc20.model.Gamesets;

import java.io.*;
import java.nio.channels.FileLockInterruptionException;
import java.util.*;

/**
 * @author GC20
 */
public class Pile {

    private List<Component> viewed;
    private List<Component> unviewed;

    public Pile() {
        this.viewed = new ArrayList<Component>();
        this.unviewed = new ArrayList<Component>();
    }

    public List<Component> getViewed() {
        return this.viewed;
    }

    public List<Component> getUnviewed() {
        return this.unviewed;
    }

    //funzione per rimuovere un componente dalla lista viewed, se non presente lancia eccezione
    public void removeViewed(Component c) throws NoSuchElementException{
        // verifico se l'elemento è presente nella lista viewed
        if (this.viewed.contains(c)) {
            //se presente lo rimuovo
            this.viewed.remove(c);
        }else {
            throw new NoSuchElementException("Component not found in viewed list");
        }
    }
    //funzione per rimuovere un componente dalla lista unviewed, se non presente lancia eccezione
    public void removeUnviewed(Component c) throws NoSuchElementException {
        // verifico se l'elemento è presente nella lista unviewed
        if (this.unviewed.contains(c)) {
            //se presente lo rimuovo
            this.unviewed.remove(c);
        }else {
            throw new NoSuchElementException("Component not found in unviewed list");
        }
    }

    //funzione per aggiungere un componente alla lista viewed, se gia presente lancia eccezione
    public void addViewed(Component c) throws IllegalArgumentException {
        //verifico se l'elemento è già presente nella lista viewed
        if (this.viewed.contains(c)) {
            throw new IllegalArgumentException();
        } else {
            //se non presente lo aggiungo
            this.viewed.add(c);
        }
    }

    //funzione per caricare la lista di tutti i componenti in unviewed a runtime
    public void addUnviewed(List<Component> c) {
        //aggiungo tutti gli elementi della lista c alla lista unviewed
        this.unviewed.addAll(c);
    }

}