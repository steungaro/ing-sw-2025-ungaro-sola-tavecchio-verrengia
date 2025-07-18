@SuppressWarnings("JavaModuleNaming")
module it.polimi.ingsw.gc20 {
    requires javafx.controls;
    requires javafx.fxml;

    requires javafx.graphics;
    requires com.fasterxml.jackson.databind;
    requires javatuples;
    requires java.rmi;
    requires java.logging;
    requires java.desktop;
    exports it.polimi.ingsw.gc20.server.model.gamesets;
    exports it.polimi.ingsw.gc20.server.model.components;
    exports it.polimi.ingsw.gc20.client.view.common.localmodel.components;

    exports it.polimi.ingsw.gc20.server.model.ship;
    exports it.polimi.ingsw.gc20.server.model.cards;
    exports it.polimi.ingsw.gc20.server.model.player;
    exports it.polimi.ingsw.gc20.server.controller;
    exports it.polimi.ingsw.gc20.client.view.common.localmodel;
    exports it.polimi.ingsw.gc20.client.view.common.localmodel.board;
    exports it.polimi.ingsw.gc20.client.view.common.localmodel.ship;
    exports it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards;
    exports it.polimi.ingsw.gc20.client.view.common;
    exports it.polimi.ingsw.gc20.client.network.common;
    exports it.polimi.ingsw.gc20.server.exceptions;
    exports it.polimi.ingsw.gc20.server.controller.states;
    exports it.polimi.ingsw.gc20.server.controller.managers;
    exports it.polimi.ingsw.gc20.common.message_protocol.toclient;
    exports it.polimi.ingsw.gc20.common.message_protocol.toserver;
    exports it.polimi.ingsw.gc20.client.view.TUI;
    exports it.polimi.ingsw.gc20.client.view.GUI.controllers;
    exports it.polimi.ingsw.gc20.server.model.lobby;
    opens it.polimi.ingsw.gc20.server.model.components to com.fasterxml.jackson.databind;
    opens it.polimi.ingsw.gc20.server.model.ship to com.fasterxml.jackson.databind;
    opens it.polimi.ingsw.gc20.server.model.cards to com.fasterxml.jackson.databind;
    opens it.polimi.ingsw.gc20.server.model.gamesets to com.fasterxml.jackson.databind;
    opens it.polimi.ingsw.gc20.server.model.player to com.fasterxml.jackson.databind;
    opens it.polimi.ingsw.gc20.client.view.common.localmodel.components to com.fasterxml.jackson.databind;
    exports it.polimi.ingsw.gc20.common.interfaces to java.rmi;
    opens it.polimi.ingsw.gc20.common.interfaces to java.rmi;
    opens it.polimi.ingsw.gc20.server.network.RMI to java.rmi;

    exports it.polimi.ingsw.gc20.client.view .GUI to javafx.graphics;
    opens it.polimi.ingsw.gc20.client.view.GUI.controllers to javafx.fxml;
    exports it.polimi.ingsw.gc20.common.message_protocol;
}