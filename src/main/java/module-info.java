module it.polimi.ingsw.gc20 {
    requires javafx.controls;
    requires javafx.fxml;

    requires javafx.graphics;
    requires com.fasterxml.jackson.databind;
    requires javatuples;
    requires java.rmi;
    requires java.logging;
    opens it.polimi.ingsw.gc20 to javafx.fxml;
    exports it.polimi.ingsw.gc20;
    opens it.polimi.ingsw.gc20.server.model.components to com.fasterxml.jackson.databind;
    opens it.polimi.ingsw.gc20.server.model.ship to com.fasterxml.jackson.databind;
    opens it.polimi.ingsw.gc20.server.model.cards to com.fasterxml.jackson.databind;
    opens it.polimi.ingsw.gc20.server.model.gamesets to com.fasterxml.jackson.databind;
    opens it.polimi.ingsw.gc20.server.model.player to com.fasterxml.jackson.databind;

}