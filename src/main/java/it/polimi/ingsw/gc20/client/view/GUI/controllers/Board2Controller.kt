package it.polimi.ingsw.gc20.client.view.GUI.controllers

import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.fxml.FXML
import javafx.scene.Parent
import javafx.scene.control.Label
import javafx.scene.layout.Pane
import javafx.scene.shape.Circle
import javafx.scene.text.Font
import java.util.ArrayList
class Board2Controller {
    @FXML
    private val circle0: Circle? = null

    @FXML
    private val circle1: Circle? = null

    @FXML
    private val circle2: Circle? = null

    @FXML
    private val circle3: Circle? = null

    @FXML
    private val circle4: Circle? = null

    @FXML
    private val circle5: Circle? = null

    @FXML
    private val circle6: Circle? = null

    @FXML
    private val circle7: Circle? = null

    @FXML
    private val circle8: Circle? = null

    @FXML
    private val circle9: Circle? = null

    @FXML
    private val circle10: Circle? = null

    @FXML
    private val circle11: Circle? = null

    @FXML
    private val circle12: Circle? = null

    @FXML
    private val circle13: Circle? = null

    @FXML
    private val circle14: Circle? = null

    @FXML
    private val circle15: Circle? = null

    @FXML
    private val circle16: Circle? = null

    @FXML
    private val circle17: Circle? = null

    private val circles: MutableList<Circle> = ArrayList<Circle>()
    private val circleLabels: MutableList<Label?> = ArrayList<Label?>()

    @FXML
    private fun initialize() {
        circles.add(circle0!!)
        circles.add(circle1!!)
        circles.add(circle2!!)
        circles.add(circle3!!)
        circles.add(circle4!!)
        circles.add(circle5!!)
        circles.add(circle6!!)
        circles.add(circle7!!)
        circles.add(circle8!!)
        circles.add(circle9!!)
        circles.add(circle10!!)
        circles.add(circle11!!)
        circles.add(circle12!!)
        circles.add(circle13!!)
        circles.add(circle14!!)
        circles.add(circle15!!)
        circles.add(circle16!!)
        circles.add(circle17!!)

        for (circle in circles) {
            val label = Label()
            label.setFont(Font(16.0))
            label.setStyle("-fx-text-fill: black; -fx-font-weight: bold;")

            label.layoutXProperty().bind(circle.layoutXProperty().subtract(8))
            label.layoutYProperty().bind(circle.layoutYProperty().subtract(12))

            circleLabels.add(label)

            circle.parentProperty()
                .addListener(ChangeListener { obs: ObservableValue<out Parent?>?, oldParent: Parent?, newParent: Parent? ->
                    if (newParent != null) {
                        (newParent as Pane).getChildren().add(label)
                    }
                })
        }
    }


    fun setNumberInCircle(circleIndex: Int, number: Int) {
        if (circleIndex >= 0 && circleIndex < circleLabels.size) {
            circleLabels.get(circleIndex)!!.setText(number.toString())
        }
    }
}