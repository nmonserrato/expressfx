package dev.neeno.expressfx.vpn

import dev.neeno.expressfx.vpn.Server.Companion.ALPHABETICAL_ORDER
import dev.neeno.expressfx.vpn.Status.Companion.DISCONNECTED
import javafx.collections.FXCollections
import javafx.scene.Parent
import javafx.scene.control.ListView
import tornadofx.runAsync
import tornadofx.ui
import java.util.concurrent.TimeUnit
import kotlin.streams.toList

class ExpressVpn : VpnService {

    private var cachedServers : List<Server> = fetchAvailableServers()

    override fun switchStatus(container: Parent) {
        Status.CHANGING.render(container)
        runAsync {
            if (status() == DISCONNECTED) connect() else disconnect()
        } ui {
            status().render(container)
        }
    }

    override fun renderStatus(container: Parent) {
        status().render(container)
    }

    override fun renderServerList(container: ListView<Any>, onlyRecommended: Boolean) {
        val servers =
            if (onlyRecommended) recommendedServers() else cachedServers.sortedWith(ALPHABETICAL_ORDER)
        val smart = servers.find { it.isSmart() }!!

        val list = FXCollections.observableArrayList<Any>()
        servers.filter { it.isSmart() || !it.sameAs(smart) }.map { it.renderListItem() }.forEach { list.add(it) }
        container.items = list
    }

    private fun status(): Status {
        println("Fetching status...")
        val process = ProcessBuilder("expressvpn", "status").start()
        val output = process.inputStream.reader(Charsets.UTF_8).let { it.readLines()[0] }
        println(output)
        process.waitFor()
        if (output.contains("Not connected"))
            return DISCONNECTED

        val regex = """.*Connected to (.*)""".toRegex()
        val (serverName) = regex.find(output)!!.destructured
        return Status.connectedTo(Server(description = serverName))
    }

    private fun connect() {
        println("Trying to connect...")
        val process = ProcessBuilder("expressvpn", "connect", "esma").start()
        val output = process.inputStream.reader(Charsets.UTF_8).readText()
        process.waitFor(30, TimeUnit.SECONDS)

        println("Process exit with status code ${process.exitValue()}. $output")
    }

    private fun disconnect() {
        println("Trying to discconnect...")
        val process = ProcessBuilder("expressvpn", "disconnect").start()
        val output = process.inputStream.reader(Charsets.UTF_8).readText()
        process.waitFor(10, TimeUnit.SECONDS)
        println("Process exit with status code ${process.exitValue()}. $output")
    }

    private fun fetchAvailableServers(): List<Server> {
        println("Listing available servers")
        val process = ProcessBuilder("expressvpn", "list", "all").start()

        val reader = process.inputStream.reader(Charsets.UTF_8)
        val servers = reader.readLines().stream()
            .skip(2)
            .map { parseServer(it) }
            .peek { println(it) }
            .toList()

        process.waitFor(10, TimeUnit.SECONDS)

        return servers
    }

    private fun recommendedServers(): List<Server> {
        return cachedServers.filter { it.isRecommended() }
    }

    private fun parseServer(it: String): Server {
        val id = it.substringBefore(" ")
        val description = it.substring(44)
        val recommended = description.endsWith("Y")
        val name = description.removeSuffix("Y").trim()
        return Server(id, id.substring(0, 2), name, recommended)
    }
}
