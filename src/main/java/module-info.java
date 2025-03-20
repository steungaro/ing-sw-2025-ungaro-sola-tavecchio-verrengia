module it.polimi.ingsw.gc20 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires jdk.jfr;
    requires javatuples;
    requires java.smartcardio;
    opens it.polimi.ingsw.gc20 to javafx.fxml;
    exports it.polimi.ingsw.gc20;
}