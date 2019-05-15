package dev.neeno.expressfx.domain.vpn

import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.VBox

data class Status(
    private val connected: Boolean,
    private val server: Server? = null
) {
    fun render(root: VBox) {
        server?.render(root)

        val topLabel = root.lookup("#connectedStatus")
        topLabel.isVisible = !connected

        val button = root.lookup("#connectButton") as Button
        button.text = if (connected) "DISCONNECT" else "CONNECT"

        val icon = root.lookup("#connectedIcon") as ImageView
        icon.changeImageTo(if (connected) "shield" else "unlocked")

        val chooseLocation = root.lookup("#chooseLocationContainer")
        chooseLocation.isVisible = !connected
    }

    private fun ImageView.changeImageTo(resource: String): Image {
        val icon = Image("icons/$resource.png")
        this.image = icon
        this.fitHeight = 128.0
        this.isPreserveRatio = true
        return icon
    }

    companion object {
        val DISCONNECTED = Status(connected = false)
        fun connectedTo(server: Server) = Status(connected = true, server = server)
    }
}


data class Server(
    private val country: String,
    private val city: String
) {
    fun render(root: VBox) {
        val lookup = root.lookup("#connectLocation") as Label
        lookup.text = "$country - $city"
    }
}
