package dev.neeno.expressfx.vpn

import dev.neeno.expressfx.gui.changeImageTo
import dev.neeno.expressfx.gui.disable
import dev.neeno.expressfx.gui.enable
import dev.neeno.expressfx.gui.find
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.control.Button
import javafx.scene.image.ImageView

interface Status {
    companion object {
        val DISCONNECTED: Status = Disconnected
        val CHANGING: Status = Changing

        fun connectedTo(server: Server): Status = Connected(server = server)
    }

    fun render(container: Parent)
}

private object Disconnected : Status {
    override fun render(container: Parent) {
        container.find<Node>("#connectedStatus").isVisible = true

        container.find<Button>("#connectButton").enable("CONNECT")

        container.find<ImageView>("#connectedIcon").changeImageTo("unlocked.png")

        container.find<Node>("#chooseLocationContainer").isVisible = true
    }
}

private object Changing : Status {
    override fun render(container: Parent) {
        container.find<Node>("#connectedStatus").isVisible = true

        container.find<Button>("#connectButton").disable("WAIT...")

        container.find<ImageView>("#connectedIcon").changeImageTo("loading.gif")

        container.find<Node>("#chooseLocationContainer").isVisible = false
    }
}

private data class Connected(private val server: Server) : Status {
    override fun render(container: Parent) {
        server.renderDescription(container)

        container.find<Node>("#connectedStatus").isVisible = false

        container.find<Button>("#connectButton").enable("DISCONNECT")

        container.find<ImageView>("#connectedIcon").changeImageTo("shield.png")

        container.find<Node>("#chooseLocationContainer").isVisible = false
    }
}

