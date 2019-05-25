package dev.neeno.expressfx.vpn

import dev.neeno.expressfx.config.Recent
import dev.neeno.expressfx.gui.changeImageTo
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.control.Label
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import tornadofx.getChildList
import java.lang.IllegalStateException

data class Server internal constructor(
    private val id: String? = null,
    private val countryCode: String? = null,
    private val description: String,
    private val recommended: Boolean? = null
) {
    companion object {
        val ALPHABETICAL_ORDER: Comparator<Server> = compareBy { it.description }

        fun fromSelectedListItem(servers: List<Server>, guiItem: Node): Server {
            val guiDescription = (guiItem.getChildList()!!.find { it is Label } as Label).text
            return servers.find { it.description == guiDescription }
                ?: throw IllegalStateException("Selected server not found in server list")
        }

        fun fromCliOutput(str: String): Server {
            val id = str.substringBefore(" ")
            val description = str.substring(44)
            val recommended = description.endsWith("Y")
            val name = description.removeSuffix("Y").trim()
            return Server(id, id.substring(0, 2), name, recommended)
        }

        fun lastConnected(allServers: List<Server>): Server {
            val lastServerId = Recent.file().lastServerId()
            return allServers.find { it.id == lastServerId }!!
        }

        fun loadRecent(allServers: List<Server>): List<Server> {
            val ids = Recent.file().allServers()
            return ids.map { recentId -> allServers.find { it.id == recentId }!! }
        }

        fun withName(name: String): Server {
            return Server(description = name)
        }
    }

    fun isRecommended() = recommended ?: false
    fun isSmart() = id == "smart"
    fun sameAs(other: Server) = description == other.description


    fun renderDescription(container: Parent) {
        val lookup = container.lookup("#connectLocation") as Label
        lookup.text = description
    }

    fun renderListItem(): Node {
        return HBox(flag(), Label(description))
    }

    fun cmdLineId(): String {
        return id ?: throw IllegalStateException("Invalid server selected with no id")
    }

    private fun flag(): Pane {
        val flag = ImageView()
        if ("smart" == id) {
            flag.changeImageTo("star.png", "icons", 12.0)
        } else {
            flag.changeImageTo("${countryCode ?: "eu"}.png", "flags", 12.0)
        }
        val flagContainer = Pane(flag)
        flagContainer.prefWidth = 30.0
        return flagContainer
    }
}