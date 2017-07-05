package ch.weber.gfocusfx

import java.net.*
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.IOException
import javax.imageio.ImageIO
import java.awt.Dimension
import java.awt.Graphics
import java.awt.event.*
import javax.swing.*
import java.util.Arrays

class UDPServer : JPanel() {

    internal var theSocket: DatagramSocket? = null
    internal var serverPort = 49199
    private var myip: InetAddress? = null

    internal var image: BufferedImage? = null
    internal var bufferedImage: BufferedImage? = null

    override fun paint(g: Graphics) {
        if (bufferedImage != null)
            g.drawImage(bufferedImage, 0, 0, null)
    }

    init {
        this.preferredSize = Dimension(640, 480);
        try {
            myip = InetAddress.getLocalHost()
            theSocket = DatagramSocket(serverPort)
            System.out.println("UDP Socket on IP address " + myip!!.hostAddress + " on port " + serverPort + " created")
        } catch (ExceSocket: SocketException) {
            System.out.println("Socket creation error : " + ExceSocket.message)
        } catch (e: UnknownHostException) {
            System.out.println("Cannot find myip")
        }

    }

    fun clientRequest() {
        var theRecievedPacket: DatagramPacket
        var outBuffer: ByteArray
        val inBuffer: ByteArray = ByteArray(30000)
        val soi: ByteArray
        var offset = 132

        val toto = true
        while (toto) {
            try {
                theRecievedPacket = DatagramPacket(inBuffer, inBuffer.size, myip, serverPort)
                theSocket!!.receive(theRecievedPacket)
                outBuffer = theRecievedPacket.data

                var i = 130
                while (i < 320) {
                    if (outBuffer[i].toInt() == -1 && outBuffer[i + 1].toInt() == -40) {
                        offset = i
                    }
                    i += 1
                }

                val newBuffer = Arrays.copyOfRange(outBuffer, offset, theRecievedPacket.getLength())

                bufferedImage = ImageIO.read(ByteArrayInputStream(newBuffer))

                repaint()

            } catch (ExceIO: IOException) {
                System.out.println("Error with client request : " + ExceIO.message)
            }

        }
        theSocket!!.close()
    }

}

fun main(args: Array<String>) {
    var imagePanel = JPanel()
    val theServer = UDPServer()

    val frame = JFrame("GH3 IP ADDRESS :")
    //Release the resource window handle as we close the frame
    frame.addWindowListener(object : WindowAdapter() {
        override fun windowClosing(e: WindowEvent) {
            System.exit(0)
        }
    })
    imagePanel.add(theServer)
    frame.add(imagePanel)
    frame.pack()
    frame.isVisible = true

    theServer.clientRequest()
}