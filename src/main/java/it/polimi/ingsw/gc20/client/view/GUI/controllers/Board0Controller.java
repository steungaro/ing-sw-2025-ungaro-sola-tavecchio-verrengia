package it.polimi.ingsw.gc20.client.view.GUI.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import it.polimi.ingsw.gc20.client.view.GUI.TokenPlaceholders.TokenPlaceholder;

import java.net.URL;
import java.util.*;

public class Board0Controller implements Initializable {

    @FXML
    private ImageView boardImageView;

    @FXML
    private AnchorPane tilesContainer;

    /*
### **Percorso Principale (Triangoli Numerati):**
Posizione 1: (650, 90) - triangolo in alto a destra
Posizione 2: (580, 90) - triangolo in alto
Posizione 3: (520, 120) - triangolo in alto a sinistra dell'ovale
Posizione 4: (130, 120) - triangolo in alto a sinistra
Posizione 5: (80, 150) - inizio curva sinistra
Posizione 6: (50, 200) - lato sinistro alto
Posizione 7: (30, 260) - lato sinistro centro
Posizione 8: (50, 320) - lato sinistro basso
Posizione 9: (80, 380) - inizio curva inferiore sinistra
Posizione 10: (130, 420) - parte inferiore sinistra
Posizione 11: (200, 450) - parte inferiore centro-sinistra
Posizione 12: (280, 470) - parte inferiore centro
Posizione 13: (360, 470) - parte inferiore centro-destra
Posizione 14: (440, 450) - parte inferiore destra
Posizione 15: (520, 420) - inizio curva inferiore destra
Posizione 16: (570, 380) - lato destro basso
Posizione 17: (620, 320) - lato destro centro
Posizione 18: (650, 260) - lato destro alto
     */

    private List<TokenPlaceholder> pathPlaceholders;

    private static final double[][] PATH_COORDINATES = {
            {650, 90},   // Posizione 1
            {580, 90},   // Posizione 2
            {520, 120},  // Posizione 3
            {130, 120},  // Posizione 4
            {80, 150},   // Posizione 5
            {50, 200},   // Posizione 6
            {30, 260},   // Posizione 7
            {50, 320},   // Posizione 8
            {80, 380},   // Posizione 9
            {130, 420},  // Posizione 10
            {200, 450},  // Posizione 11
            {280, 470},  // Posizione 12
            {360, 470},  // Posizione 13
            {440, 450},  // Posizione 14
            {520, 420},  // Posizione 15
            {570, 380},  // Posizione 16
            {620, 320},  // Posizione 17
            {650, 260}   // Posizione 18
    };

    private void initializePathPlaceholders() {
        pathPlaceholders = new ArrayList<>();

        for (int i = 0; i < PATH_COORDINATES.length; i++) {
            TokenPlaceholder placeholder = new TokenPlaceholder(15); // 15px

            // Imposta le coordinate
            double x = PATH_COORDINATES[i][0];
            double y = PATH_COORDINATES[i][1];
            placeholder.setLayoutX(x);
            placeholder.setLayoutY(y);

            // Aggiungi evento click per gestire movimento pedine
            final int position = i;
            placeholder.setOnMouseClicked(e -> handlePositionClick(position)); // TODO

            pathPlaceholders.add(placeholder);
        }
    }

    /**
     * Aggiunge tutti i segnaposti al pannello di gioco
     */
    public void addPlaceholdersToPane(Pane gamePane) {
        for (TokenPlaceholder placeholder : pathPlaceholders) {
            gamePane.getChildren().add(placeholder);
        }
    }

    /**
     * Gestisce il click su una posizione del percorso
     */
    private void handlePositionClick(int position) {
        TokenPlaceholder placeholder = pathPlaceholders.get(position);

        if (placeholder.isOccupied()) {
            placeholder.removeToken();
            System.out.println("Rimossa pedina dalla posizione " + (position + 1));
        } else {
            placeholder.setToken("Pedina", "P");
            System.out.println("Aggiunta pedina alla posizione " + (position + 1));
        }
    }

    /**
     * Muove una pedina da una posizione all'altra
     */
    public boolean movePiece(int fromPosition, int toPosition) {
        if (fromPosition < 0 || fromPosition >= pathPlaceholders.size() ||
                toPosition < 0 || toPosition >= pathPlaceholders.size()) {
            return false;
        }

        TokenPlaceholder from = pathPlaceholders.get(fromPosition);
        TokenPlaceholder to = pathPlaceholders.get(toPosition);

        if (!from.isOccupied() || to.isOccupied()) {
            return false; // Non può muovere pedina inesistente o su posizione occupata
        }

        // Trasferisci la pedina
        Object token = from.getToken();
        from.removeToken();
        to.setToken(token, "P");

        System.out.println("Pedina spostata da posizione " + (fromPosition + 1) +
                " a posizione " + (toPosition + 1));
        return true;
    }

    /**
     * Piazza una pedina in una specifica posizione
     */
    public boolean placePiece(int position, Object piece, String displayText) {
        if (position < 0 || position >= pathPlaceholders.size()) {
            return false;
        }

        TokenPlaceholder placeholder = pathPlaceholders.get(position);
        if (placeholder.isOccupied()) {
            return false; // Posizione già occupata
        }

        placeholder.setToken(piece, displayText);
        return true;
    }

    /**
     * Rimuove una pedina da una posizione
     */
    public boolean removePiece(int position) {
        if (position < 0 || position >= pathPlaceholders.size()) {
            return false;
        }

        TokenPlaceholder placeholder = pathPlaceholders.get(position);
        if (!placeholder.isOccupied()) {
            return false; // Nessuna pedina da rimuovere
        }

        placeholder.removeToken();
        return true;
    }

    /**
     * Ottiene un segnaposto specifico per posizione
     */
    public TokenPlaceholder getPlaceholder(int position) {
        if (position >= 0 && position < pathPlaceholders.size()) {
            return pathPlaceholders.get(position);
        }
        return null;
    }

    /**
     * Ottiene tutte le posizioni occupate
     */
    public List<Integer> getOccupiedPositions() {
        List<Integer> occupied = new ArrayList<>();
        for (int i = 0; i < pathPlaceholders.size(); i++) {
            if (pathPlaceholders.get(i).isOccupied()) {
                occupied.add(i);
            }
        }
        return occupied;
    }

    /**
     * Resetta tutto il percorso rimuovendo tutte le pedine
     */
    public void resetPath() {
        for (TokenPlaceholder placeholder : pathPlaceholders) {
            placeholder.removeToken();
        }
        System.out.println("Percorso resettato - tutte le pedine rimosse");
    }

    /**
     * Getter per la lista completa dei segnaposti
     */
    public List<TokenPlaceholder> getPathPlaceholders() {
        return pathPlaceholders;
    }

    /**
     * Metodo di utilità per ottenere le coordinate di una posizione
     */
    public double[] getCoordinates(int position) {
        if (position >= 0 && position < PATH_COORDINATES.length) {
            return PATH_COORDINATES[position].clone();
        }
        return null;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Dimensionamento dell'AnchorPane alla dimensione dell'immagine
        boardImageView.layoutBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
            tilesContainer.setPrefWidth(newBounds.getWidth());
            tilesContainer.setPrefHeight(newBounds.getHeight());
        });

        initializePathPlaceholders();

        for (TokenPlaceholder placeholder : pathPlaceholders) {
            // Modifica opzionale: rendiamo i placeholder più evidenti
            placeholder.setPlaceholderText(Integer.toString(pathPlaceholders.indexOf(placeholder) + 1));
            tilesContainer.getChildren().add(placeholder);
        }
    }
}