package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.server.controller.GameController;
import it.polimi.ingsw.gc20.server.exceptions.*;
import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.server.model.cards.Planet;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import it.polimi.ingsw.gc20.server.model.gamesets.GameModel;
import it.polimi.ingsw.gc20.server.model.player.Player;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused") // dynamically created by Cards
public class PlanetsState extends CargoState {
    private final List<Planet> planets;
    private final int lostDays;
    private String landedPlayer;
    private int landedPlanetIndex;
    private final List<Player> playersToMove;
    /**
     * Default constructor
     */
    @SuppressWarnings("unused") // dynamically created by Cards
    public PlanetsState(GameModel model, GameController controller, AdventureCard card) {
        super(model, controller);
        this.planets = card.getPlanets();
        this.lostDays = card.getLostDays();
        this.landedPlayer = null;
        this.landedPlanetIndex = -1;
        this.playersToMove = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "PlanetsState{" +
                "planets=" + planets +
                ", lostDays=" + lostDays +
                ", landedPlayer='" + landedPlayer + '\'' +
                ", landedPlanetIndex=" + landedPlanetIndex +
                '}';
    }



    /**
     * Accepts a planet card and lands on the planet
     * @param player is the player that wants to land on the planet
     * @param planetIndex is the index of the planet card in the player's hand
     * @throws IllegalStateException if the game is not in the planet phase
     * @throws InvalidTurnException if it is not the player's turn
     */
    @Override
    public void landOnPlanet(Player player, int planetIndex) throws InvalidTurnException {
        if (!getCurrentPlayer().equals(player.getUsername())) {
            throw new InvalidTurnException("It's not your turn");
        }
        if (planets.get(planetIndex).getAvailable()) {
            planets.get(planetIndex).setAvailable(false);
            landedPlayer = player.getUsername();
            landedPlanetIndex = planetIndex;
            playersToMove.add(player);
        } else {
            throw new IllegalStateException("The planet is not available");
        }
    }

    @Override
    public void loadCargo(Player player, CargoColor loaded, Pair<Integer, Integer> chTo) throws InvalidTurnException, CargoException, CargoNotLoadable, CargoFullException {
        if (!player.getUsername().equals(landedPlayer)) {
            throw new IllegalArgumentException("You can't load cargo unless you are on the planet");
        }
        if (planets.get(landedPlanetIndex).getReward().contains(loaded)) {
            planets.get(landedPlanetIndex).getReward().remove(loaded);
            super.loadCargo(player, loaded, chTo);
        } else {
            throw new IllegalStateException("You can't load this cargo, it's not in the reward");
        }
    }

    @Override
    public void unloadCargo(Player player, CargoColor unloaded, Pair<Integer, Integer> ch) throws InvalidTurnException, CargoException, CargoNotLoadable, CargoFullException, InvalidCargoException {
        if (!player.getUsername().equals(landedPlayer)) {
            throw new IllegalArgumentException("You can't unload cargo unless you are on the planet");
        }
        super.unloadCargo(player, unloaded, ch);
    }

    @Override
    public void moveCargo(Player player, CargoColor cargo, Pair<Integer, Integer> from, Pair<Integer, Integer> to) throws InvalidTurnException, CargoException, CargoNotLoadable, CargoFullException, InvalidCargoException {
        if (!player.getUsername().equals(landedPlayer)) {
            throw new IllegalArgumentException("You can't move cargo unless you are on the planet");
        }
        super.moveCargo(player, cargo, from, to);
    }

    @Override
    public void endMove(Player player) throws InvalidTurnException {
        if (!player.getUsername().equals(landedPlayer)) {
            throw new InvalidTurnException("It's not your turn");
        }
        landedPlayer = null;
        landedPlanetIndex = -1;
        nextPlayer();
        if (getCurrentPlayer() == null) {
            playersToMove.reversed().forEach(p -> getModel().movePlayer(p, -lostDays));
            getController().setState(new PreDrawState(getController()));
        }
    }
}
