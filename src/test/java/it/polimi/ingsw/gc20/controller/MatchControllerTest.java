package it.polimi.ingsw.gc20.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import it.polimi.ingsw.gc20.model.lobby.Lobby;

import static org.junit.jupiter.api.Assertions.*;

class MatchControllerTest {
    @BeforeAll
    static void setUp() {
        // Initialize the MatchController instance before each test
        MatchController matchController = MatchController.getInstance(3, 3);
/*
        Lobby lobby = matchController.createLobby("lobby1", "player1", 4, 2);
        matchController.joinLobby(lobby.getId(), "player2");
        matchController.joinLobby(lobby.getId(), "player3");
        matchController.joinLobby(lobby.getId(), "player4");
        matchController.startLobby(lobby.getId());
        matchController.setMaxMatches(3);
        matchController.setMaxLobbies(3);*/
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