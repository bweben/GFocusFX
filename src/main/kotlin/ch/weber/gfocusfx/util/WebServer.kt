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
    private var myIp: InetAddress? = null
    private var socket: DatagramSocket? = null

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

    fun getImageFromStream(): BufferedImage {
        var receivedPacket: DatagramPacket
        var outBuffer: ByteArray
        var inBuffer: ByteArray = ByteArray(30000)
        val soi: ByteArray
        var offset = 132
        var bufferedImage: BufferedImage

        val todo = true
        while (todo) {
            try {
                receivedPacket = DatagramPacket(inBuffer, inBuffer.size, myIp, port.toInt())
                socket!!.receive(receivedPacket)
                outBuffer = receivedPacket.data

                var i = 130
                while (i < 320) {
                    if (outBuffer[i].toInt() == -1 && outBuffer[i + 1].toInt() == -40) {
                        offset = i
                    }
                    i += 1
                }

                val newBuffer = java.util.Arrays.copyOfRange(outBuffer, offset, receivedPacket.getLength())
                bufferedImage = ImageIO.read(ByteArrayInputStream(newBuffer))
                //TODO put image out to display
            } catch (ExceIO: IOException) {
                println("Error with client request : " + ExceIO.message)
            }
        }
        socket!!.close()
    }
}
