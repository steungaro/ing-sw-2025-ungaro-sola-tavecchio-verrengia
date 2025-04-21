package it.polimi.ingsw.gc20.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import it.polimi.ingsw.gc20.model.lobby.Lobby;

import static org.junit.jupiter.api.Assertions.*;

class MatchControllerTest {
    @BeforeEach
    void setUp() {
        // Initialize the MatchController instance before each test
        List<String> players = new ArrayList<>();
        players.add("player1");
        players.add("player2");
        players.add("player3");
        players.add("player4");
        GameController gameController = new GameController("0", players, 2);
        MatchController matchController = MatchController.getInstance(3, 3);

        Lobby lobby = matchController.createLobby("lobby1", "player1", 4, 2);
        matchController.startLobby("1");
    }

    @Test
    void getGameController() {
    }

    @Test
    void getInstance() {
    }

    @Test
    void getPlayersInLobbies() {
    }

    @Test
    void testGetInstance() {
    }

    @Test
    void setMaxLobbies() {
    }

    @Test
    void setMaxMatches() {
    }

    @Test
    void getLobbies() {
    }

    @Test
    void getLobby() {
    }

    @Test
    void joinLobby() {
    }

    @Test
    void createLobby() {
    }

    @Test
    void leaveLobby() {
    }

    @Test
    void endGame() {
    }

    @Test
    void startLobby() {
    }
}