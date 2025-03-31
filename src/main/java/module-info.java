module it.polimi.ingsw.gc20 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires jdk.jfr;
    requires com.fasterxml.jackson.databind;
    requires javatuples;
    requires java.smartcardio;
    requires java.rmi;
    opens it.polimi.ingsw.gc20 to javafx.fxml;
    exports it.polimi.ingsw.gc20;
    opens it.polimi.ingsw.gc20.model.components to com.fasterxml.jackson.databind;
    opens it.polimi.ingsw.gc20.model.ship to com.fasterxml.jackson.databind;
    opens it.polimi.ingsw.gc20.model.cards to com.fasterxml.jackson.databind;
    opens it.polimi.ingsw.gc20.model.gamesets to com.fasterxml.jackson.databind;
    opens it.polimi.ingsw.gc20.model.player to com.fasterxml.jackson.databind;

}