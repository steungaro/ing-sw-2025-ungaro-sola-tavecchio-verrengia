package it.polimi.ingsw.gc20.server.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import it.polimi.ingsw.gc20.server.model.lobby.Lobby;

import static org.junit.jupiter.api.Assertions.*;

class MatchControllerTest {
    private static MatchController matchController;

    @BeforeAll
    static void setUp() {
        // Initialize the MatchController instance before each test
        matchController = MatchController.getInstance(3, 3);
        matchController.createLobby("lobby1", "player1", 4, 2);
        matchController.setMaxMatches(3);
        matchController.setMaxLobbies(3);
    }

    @Test
    void getGameController() {
        // Non ci sono giochi inizialmente
        assertNull(matchController.getGameController("nonexistentId"));
    }

    @Test
    void getInstance() {
        // Verifica che getInstance senza parametri restituisca un'istanza non nulla
        assertNotNull(MatchController.getInstance());
    }

    @Test
    void getPlayersInLobbies() {
        // Verifica che la lista dei giocatori nelle lobby non sia nulla
        assertNotNull(matchController.getPlayersInLobbies());
        // Dovrebbe contenere "player1" impostato in setUp
        assertTrue(matchController.getPlayersInLobbies().contains("player1"));
    }

    @Test
    void testGetInstance() {
        // Verifica che getInstance con parametri restituisca un'istanza non nulla
        assertNotNull(MatchController.getInstance(3, 3));
    }

    @Test
    void setMaxLobbies() {
        matchController.setMaxLobbies(5);
        // Non possiamo verificare direttamente il campo privato, ma possiamo verificare
        // che sia possibile creare più lobby del limite precedente
        matchController.createLobby("lobby2", "player2", 4, 2);
        matchController.createLobby("lobby3", "player3", 4, 2);
        matchController.createLobby("lobby4", "player4", 4, 2);
        // Se non lancia eccezioni, il test passa
    }

    @Test
    void setMaxMatches() {
        matchController.setMaxMatches(5);
        // Simile a setMaxLobbies, non possiamo verificare direttamente
    }

    @Test
    void getLobbies() {
        List<Lobby> lobbies = matchController.getLobbies();
        assertNotNull(lobbies);
        assertFalse(lobbies.isEmpty());
    }

    @Test
    void getLobby() {
        Lobby lobby = matchController.getLobby("lobby1");
        assertNotNull(lobby);
        assertEquals("lobby1", lobby.getId());
    }

    @Test
    void joinLobby() {
        // Creiamo una nuova lobby per questo test
        matchController.createLobby("joinTestLobby", "owner", 4, 2);
        matchController.joinLobby("joinTestLobby", "newPlayer");

        // Verifichiamo che il giocatore sia nella lobby
        Lobby lobby = matchController.getLobby("joinTestLobby");
        assertTrue(lobby.getUsers().contains("newPlayer"));
    }

    @Test
    void createLobby() {
        matchController.createLobby("createTestLobby", "creator", 4, 2);

        // Verifichiamo che la lobby sia stata creata
        Lobby lobby = matchController.getLobby("createTestLobby");
        assertNotNull(lobby);
        assertEquals("createTestLobby", lobby.getId());
        assertTrue(lobby.getUsers().contains("creator"));
    }

    @Test
    void leaveLobby() {
        // Creiamo una nuova lobby e aggiungiamo un giocatore
        matchController.createLobby("leaveTestLobby", "owner", 4, 2);
        matchController.joinLobby("leaveTestLobby", "leaver");

        // Il giocatore lascia la lobby
        matchController.leaveLobby("leaver");

        // Verifichiamo che il giocatore non sia più nella lobby
        Lobby lobby = matchController.getLobby("leaveTestLobby");
        assertFalse(lobby.getUsers().contains("leaver"));
    }

    @Test
    void endGame() {
        // Test base per endGame, difficile da testare completamente senza un gioco attivo
        // Per un test base, verifichiamo che non lanci eccezioni
        matchController.endGame("nonExistentGameId");
    }

    @Test
    void startLobby() {
        // Creiamo una nuova lobby con abbastanza giocatori
        matchController.createLobby("startTestLobby", "owner", 2, 2);
        matchController.joinLobby("startTestLobby", "player2");

        // Test base: verifichiamo che startLobby non lanci eccezioni
        matchController.startLobby("owner");
    }

    @Test
    void getAllUsers() {
        List<String> allUsers = matchController.getAllUsers();
        assertNotNull(allUsers);
        // Dovrebbe contenere almeno "player1" da setUp
        assertTrue(allUsers.contains("player1"));
    }

    @Test
    void isUsernameAvailable() {
        // "player1" è già usato, quindi non dovrebbe essere disponibile
        assertFalse(matchController.isUsernameAvailable("player1"));

        // Un nome utente casuale dovrebbe essere disponibile
        assertTrue(matchController.isUsernameAvailable("newRandomUsername"));
    }

    @Test
    void getGameControllerForPlayer() {
        // Nessun giocatore dovrebbe essere in un gioco inizialmente
        assertNull(matchController.getGameControllerForPlayer("player1"));
    }
}