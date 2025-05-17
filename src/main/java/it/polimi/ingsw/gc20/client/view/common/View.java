package it.polimi.ingsw.gc20.client.view.common;

import it.polimi.ingsw.gc20.client.network.common.Client;
import it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards.ViewAdvetnureCard;
import it.polimi.ingsw.gc20.client.view.common.localmodel.board.ViewBoard;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ship.ViewShip;
import it.polimi.ingsw.gc20.common.interfaces.ViewInterface;
import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;
import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public abstract class View implements ViewInterface {
    private static View instance;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(View.class.getName());
    protected boolean loggedIn;
    protected String username;
    protected Client client;
    private ViewBoard board;
    private Map<String, ViewShip> ships;
    protected AdventureCard currentCard;

    private ViewComponent componentInHand;

    public ViewComponent getComponentInHand() {
        return componentInHand;
    }

    public void setComponentInHand(ViewComponent componentInHand) {
        this.componentInHand = componentInHand;
    }
    public ViewBoard getBoard() {
        return board;
    }

    public void setBoard(ViewBoard board) {
        this.board = board;
    }

    public Map<String, ViewShip> getShips() {
        return ships;
    }

    public void setShips(Map<String, ViewShip> ships) {
        this.ships = ships;
    }

    public ViewShip getShip (String username) {
        return ships.get(username);
    }

    public void setShip (String username, ViewShip ship) {
        ships.put(username, ship);
    }

    protected View() {
        this.loggedIn = false;
        this.username = null;
        this.client = null;
    }

    public void ping() {
        LOGGER.info("Ping received from server, ponging back.");
        client.pong(username);
    }

    public static View getInstance() {
        return instance;
    }

    public static void setInstance(View instance) {
        View.instance = instance;
    }

    public void printShip(String username) {
        ViewShip ship = ships.get(username);
        if (ship != null) {
            ship.toString();
        } else {
            LOGGER.warning("No ship found for " + username);
        }
    }

    public void printBoard() {
        if (board != null) {
            board.toString();
        } else {
            LOGGER.warning("No board found.");
        }
    }

    public void printDeck(int index) {
        if (board != null) {
            List<ViewAdvetnureCard> cards = board.decks.get(index);
            if (cards != null) {
                String out = printCardsInLine(cards);
                System.out.println(out);
                LOGGER.info("Deck " + index + ":\n");
            } else {
                LOGGER.warning("No card found at index " + index);
            }
        } else {
            LOGGER.warning("No deck found at index " + index);
        }
    }

    /**
     * Metodo che consente di visualizzare multiple carte sulla stessa riga orizzontale,
     * andando a capo ogni 10 carte
     * @param cards Lista di ViewAdventureCard da visualizzare
     * @return Stringa con la rappresentazione delle carte affiancate
     */
    public String printCardsInLine(List<ViewAdvetnureCard> cards) {
        if (cards == null || cards.isEmpty()) {
            return "";
        }
        
        final int cardsPerRow = 10;
        
        int numCardRows = (int) Math.ceil((double) cards.size() / cardsPerRow);
        
        StringBuilder finalResult = new StringBuilder();

        for (int cardRow = 0; cardRow < numCardRows; cardRow++) {
            int startIdx = cardRow * cardsPerRow;
            int endIdx = Math.min(startIdx + cardsPerRow, cards.size());
            List<ViewAdvetnureCard> rowCards = cards.subList(startIdx, endIdx);
            
            List<String> cardStrings = rowCards.stream()
                    .map(Object::toString)
                    .collect(Collectors.toList());
            
            List<String[]> cardLines = cardStrings.stream()
                    .map(card -> card.split("\n"))
                    .collect(Collectors.toList());
            
            int numRows = cardLines.get(0).length;
            
            for (int i = 0; i < numRows; i++) {
                for (String[] cardLine : cardLines) {
                    finalResult.append(cardLine[i]);
                    finalResult.append("  ");
                }
                finalResult.append("\n");
            }
            
            if (cardRow < numCardRows - 1) {
                finalResult.append("\n");
            }
        }
        return finalResult.toString();
    }

    public void printDeck(){
        printDeck(0);
    }

    public void printCurrentCard() {
        if (currentCard != null) {
            currentCard.toString();
        } else {
            LOGGER.warning("No current card found.");
        }
    }

    public void printViewPile(){
        if (board != null) {
            List<ViewComponent> cards = board.viewedPile;
            if (cards != null) {
                String out = printComponentsInLine(cards);
                System.out.println(out);
                LOGGER.info("View Pile:\n");
            } else {
                LOGGER.warning("No card found in view pile.");
            }
        } else {
            LOGGER.warning("No view pile found.");
        }
    }

    /**
     * Metodo che consente di visualizzare multipli componenti sulla stessa riga orizzontale,
     * andando a capo ogni 10 componenti
     * @param components Lista di ViewComponent da visualizzare
     * @return Stringa con la rappresentazione dei componenti affiancati
     */
    public String printComponentsInLine(List<ViewComponent> components) {
        if (components == null || components.isEmpty()) {
            return "";
        }

        final int componentsPerRow = 10;

        int numComponentRows = (int) Math.ceil((double) components.size() / componentsPerRow);

        StringBuilder finalResult = new StringBuilder();

        for (int componentRow = 0; componentRow < numComponentRows; componentRow++) {
            int startIdx = componentRow * componentsPerRow;
            int endIdx = Math.min(startIdx + componentsPerRow, components.size());
            List<ViewComponent> rowComponents = components.subList(startIdx, endIdx);

            List<String> componentStrings = rowComponents.stream()
                    .map(Object::toString)
                    .collect(Collectors.toList());

            List<String[]> componentLines = componentStrings.stream()
                    .map(component -> component.split("\n"))
                    .collect(Collectors.toList());

            int numRows = componentLines.get(0).length;

            for (int i = 0; i < numRows; i++) {
                for (String[] componentLine : componentLines) {
                    finalResult.append(componentLine[i]);
                    finalResult.append("  ");
                }
                finalResult.append("\n");
            }

            if (componentRow < numComponentRows - 1) {
                finalResult.append("\n");
            }
        }
        return finalResult.toString();
    }

    @Override
    public void updateView(Message message) throws RemoteException {
        // Submit message handling to the executor for asynchronous processing
        executor.submit(() -> {
            try {
                message.handleMessage();
            } catch (Exception e) {
                LOGGER.warning("Error while handling message: " + e.getMessage());
            }
        });
    }
}