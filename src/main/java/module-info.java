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
    opens it.polimi.ingsw.gc20 to javafx.fxml;
    exports it.polimi.ingsw.gc20;
    exports it.polimi.ingsw.gc20.server.model.gamesets;
    exports it.polimi.ingsw.gc20.server.model.components;
    exports it.polimi.ingsw.gc20.client.view.common.localmodel.components;

    opens it.polimi.ingsw.gc20.server.model.components to com.fasterxml.jackson.databind;
    opens it.polimi.ingsw.gc20.server.model.ship to com.fasterxml.jackson.databind;
    opens it.polimi.ingsw.gc20.server.model.cards to com.fasterxml.jackson.databind;
    opens it.polimi.ingsw.gc20.server.model.gamesets to com.fasterxml.jackson.databind;
    opens it.polimi.ingsw.gc20.server.model.player to com.fasterxml.jackson.databind;
    opens it.polimi.ingsw.gc20.client.view.common.localmodel.components to com.fasterxml.jackson.databind;
    exports it.polimi.ingsw.gc20.common.interfaces to java.rmi;
    opens it.polimi.ingsw.gc20.common.interfaces to java.rmi;
    opens it.polimi.ingsw.gc20.server.network.RMI to java.rmi;
    exports it.polimi.ingsw.gc20.common.message_protocol.toserver;

    exports it.polimi.ingsw.gc20.client.view.GUI to javafx.graphics;
    opens it.polimi.ingsw.gc20.client.view.GUI.controllers to javafx.fxml;
}