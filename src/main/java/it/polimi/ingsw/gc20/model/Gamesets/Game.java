package it.polimi.ingsw.gc20.model.Gamesets;

import it.polimi.ingsw.gc20.model.Playerclasses.Player;

import java.io.*;
import java.util.*;

/**
 * @author GC20
 */
public class Game {

    private List<Player> players;
    private Board board;
    private Integer gameID;
    private Pile pile;
    private Map<Cargo, Integer> cargoAvailable;

    /**
     * Default constructor
     */
    public Game() {
        this.players = new ArrayList<Player>();
        this.board = null;
        this.gameID = null;
        this.pile = null;
        this.cargoAvailable = new HashMap<Cargo, Integer>();
    }
    //funzione per aggiungere un giocatore alla partita
    public void addPlayer(Player p){
        players.add(p);
    }

    //funzione per avere la lista di player
    public List<Player> getPlayers() {
        return this.players;
    }

    //meotodo che aggiunge una boarda alla partita
    public void addBoard(Board board){
        this.board = board;
    }

    //metodo che restituisce la board
    public Board getBoard() {
        return this.board;
    }

    //metodo che restituisce l'id della partita
    public Integer getID() {
        return this.gameID;
    }

    //metodo che setta l'id della partita
    public void setID(Integer id) {
        this.gameID = id;
    }

    //metodo che restituisce la quantità di cargo disponibili di un determinato colore
    public Integer getCargoAvailable(Cargo c) {
        // TODO implement here
        return null;
    }

    //funzione che setta la quantità di cargo disponibili di un determinato colore
    public void setCargoAvailable(Cargo c, Integer i) {
        // TODO implement here
        return null;
    }

    //metodo che restituisce la pila di componenti
    public Pile getPile() {
        return this.pile;
    }

    //metodo che setta la pila di componenti
    public void setPile(Pile pile) {
        this.pile = pile;
    }

}