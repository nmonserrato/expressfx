package dev.neeno.expressfx.vpn

import dev.neeno.expressfx.gui.changeImageTo
import dev.neeno.expressfx.gui.disable
import dev.neeno.expressfx.gui.enable
import dev.neeno.expressfx.gui.find
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.image.ImageView

interface Status {
    companion object {
        val DISCONNECTED: Status = Disconnected
        val CHANGING: Status = Changing

        fun connectedTo(server: Server): Status =
            Connected(server = server)
    }

    fun render(root: Parent)
}

private object Disconnected : Status {
    override fun render(root: Parent) {
        root.find<Node>("#connectedStatus").isVisible = true

        root.find<Button>("#connectButton").enable("CONNECT")

        root.find<ImageView>("#connectedIcon").changeImageTo("unlocked.png")

        root.find<Node>("#chooseLocationContainer").isVisible = true
    }
}

private object Changing : Status {
    override fun render(root: Parent) {
        root.find<Node>("#connectedStatus").isVisible = true

        root.find<Button>("#connectButton").disable("WAIT...")

        root.find<ImageView>("#connectedIcon").changeImageTo("loading.gif")

        root.find<Node>("#chooseLocationContainer").isVisible = false
    }
}

private data class Connected(
    private val server: Server? = null
) : Status {
    override fun render(root: Parent) {
        server?.render(root)

        root.find<Node>("#connectedStatus").isVisible = false

        root.find<Button>("#connectButton").enable("DISCONNECT")

        root.find<ImageView>("#connectedIcon").changeImageTo("shield.png")

        root.find<Node>("#chooseLocationContainer").isVisible = false
    }
}

data class Server(
    private val id: String? = null,
    private val countryCode: String? = null,
    private val description: String,
    private val recommended: Boolean? = null
) {
    fun render(root: Parent) {
        val lookup = root.lookup("#connectLocation") as Label
        lookup.text = description
    }

    fun isRecommended() = recommended ?: false
}

