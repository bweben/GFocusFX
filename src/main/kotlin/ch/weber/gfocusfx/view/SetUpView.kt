package ch.weber.gfocusfx.view

import ch.weber.gfocusfx.app.SetUpController
import javafx.geometry.Insets
import javafx.scene.control.TextField
import javafx.scene.layout.VBox
import tornadofx.*

/**
 * Created by U115746 on 05.07.2017.
 */
class SetUpView : View("My View") {
    override val root = VBox()
    val setUpController: SetUpController by inject()

    var port: TextField by singleAssign()

    init {
        title = "Kotlin Stream Test"

        with (root) {

            hbox {
                label("Port") {
                    hboxConstraints { margin = Insets(5.0) }
                }
                port = textfield {
                    useMaxWidth = true
                    hboxConstraints { margin = Insets(5.0) }
                }

                port.text = "49199"
            }
            hbox {
                button("Start") {
                    hboxConstraints { margin = Insets(5.0) }
                    isDefaultButton = true

                    setOnAction {
                        setUpController.connect(port.text)
                    }
                }
            }
        }
    }
}