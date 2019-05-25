package dev.neeno.expressfx.vpn

import dev.neeno.expressfx.events.Publisher.Companion.publisher
import dev.neeno.expressfx.events.VpnConnected
import dev.neeno.expressfx.vpn.ProcessExecutor.Companion.cli
import dev.neeno.expressfx.vpn.Server.Companion.ALPHABETICAL_ORDER
import dev.neeno.expressfx.vpn.Server.Companion.fromCliOutput
import dev.neeno.expressfx.vpn.Status.Companion.DISCONNECTED
import javafx.collections.FXCollections
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.control.ListView
import tornadofx.runAsync
import tornadofx.ui
import kotlin.streams.toList

class ExpressVpn : VpnService {

    companion object {
        private const val EXECUTABLE = "expressvpn"
    }

    private val allServers: List<Server> = fetchAvailableServers()
    private var selectedServer: Server = Server.lastConnected(allServers)

    override fun switchStatus(container: Parent) {
        Status.CHANGING.render(container)
        runAsync {
            if (status() == DISCONNECTED) connect() else disconnect()
        } ui {
            status().render(container)
        }
    }

    override fun selectServer(guiItem: Node) {
        selectedServer = Server.fromSelectedListItem(allServers, guiItem)
    }

    override fun renderStatus(container: Parent) {
        selectedServer.renderDescription(container)
        status().render(container)
    }

    override fun renderAllServerList(container: ListView<Any>) {
        renderServerList(container, allServers.sortedWith(ALPHABETICAL_ORDER))
    }

    override fun renderRecommendedServerList(container: ListView<Any>) {
        renderServerList(container, recommendedServers())
    }

    override fun renderRecentServerList(container: ListView<Any>) {
        renderServerList(container, Server.loadRecent(allServers))
    }

    private fun renderServerList(container: ListView<Any>, filteredList: List<Server>) {
        val list = FXCollections.observableArrayList<Any>()
        filteredList.map { it.renderListItem() }.forEach { list.add(it) }
        container.items = list
    }

    private fun status(): Status {
        val output = cli().exec(EXECUTABLE, "status")[0]
        if (output.contains("Not connected"))
            return DISCONNECTED

        val regex = """.*Connected to (.*)""".toRegex()
        val (serverName) = regex.find(output)!!.destructured
        return Status.connectedTo(Server(description = serverName))
    }

    private fun connect() {
        cli().exec(EXECUTABLE, "connect", selectedServer.cmdLineId())
        publisher().notifyEvent(VpnConnected(selectedServer))

    }

    private fun disconnect() {
        cli().exec(EXECUTABLE, "disconnect")
    }

    private fun fetchAvailableServers(): List<Server> {
        val output = cli().exec(EXECUTABLE, "list", "all")
        val completeList = output.stream().skip(2).map { fromCliOutput(it) }.toList()
        val smart = completeList.find { it.isSmart() }!!
        return completeList.filter { it.isSmart() || !it.sameAs(smart) }
    }

    private fun recommendedServers(): List<Server> {
        return allServers.filter { it.isRecommended() }
    }
}
