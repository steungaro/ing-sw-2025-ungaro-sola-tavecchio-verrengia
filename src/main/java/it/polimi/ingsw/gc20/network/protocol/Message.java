package it.polimi.ingsw.gc20.network.protocol;

import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    private MessageType type;
    private Object payload;
    private String sender;

    public Message(MessageType type, Object payload, String sender) {
        this.type = type;
        this.payload = payload;
        this.sender = sender;
    }

    // Getters e setters

    public enum MessageType {
        LOGIN, LOGOUT, GAME_ACTION, CHAT, ERROR, NOTIFICATION // Poi decidiamo tutti i messaggi possibili
    }
}