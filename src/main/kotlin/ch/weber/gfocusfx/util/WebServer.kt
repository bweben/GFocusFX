package ch.weber.gfocusfx.util

import ch.weber.gfocusfx.model.CameraMode
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.github.kittinunf.result.getAs

/**
 * Created by natha on 05.07.2017.
 */

class WebServer(val port: String, val ip: String) {
    init {
        FuelManager.instance.basePath = "http://$ip";
    }

    fun setMode(mode: CameraMode) {
        if (mode === CameraMode.CTRL) "/cam.cgi?mode=accctrl&type=req_acc&value=4D454930-0100-1000-8001-02070002D200&value2=iPhone".httpGet().responseString { request, response, result ->
            result.fold({ d ->
                println(d)
            }, { err ->
                println(err)
            })
        }
    }

    fun getState() {
        "cam.cgi?mode=getstate".httpGet().responseString{ request, response, result ->
            result.fold({ d ->
                println(d)
            }, { err ->
                println(err)
            })
        }
    }

    fun startStream() {
        "cam.cgi?mode=startstream&value=$port".httpGet().responseString{ request, response, result ->
            result.fold({ d ->
                println(d)
            }, { err ->
                println(err)
            })
        }
    }
}
