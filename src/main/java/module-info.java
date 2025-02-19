module it.polimi.progetto_ingsoft {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens it.polimi.progetto_ingsoft to javafx.fxml;
    exports it.polimi.progetto_ingsoft;
}