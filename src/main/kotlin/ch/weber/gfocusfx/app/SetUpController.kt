package ch.weber.gfocusfx.app

import ch.weber.gfocusfx.model.CameraMode
import ch.weber.gfocusfx.util.WebServer
import tornadofx.*

/**
 * Created by U115746 on 05.07.2017.
 */

class SetUpController: Controller() {
    fun connect(port: String) {
        val webServer: WebServer = WebServer(port, "192.168.54.1")
        webServer.setMode(CameraMode.CTRL)
        webServer.getState()
        webServer.startStream()
    }

}