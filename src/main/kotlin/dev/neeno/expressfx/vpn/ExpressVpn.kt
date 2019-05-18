package dev.neeno.expressfx.vpn

import dev.neeno.expressfx.vpn.Status.Companion.DISCONNECTED
import javafx.scene.Parent
import tornadofx.runAsync
import tornadofx.ui
import java.util.concurrent.TimeUnit
import kotlin.streams.toList

class ExpressVpn : VpnService {
    override fun status(): Status {
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

    override fun switchStatus(root: Parent) {
        Status.CHANGING.render(root)
        runAsync {
            if (status() == DISCONNECTED) connect() else disconnect()
        } ui {
            status().render(root)
        }
    }

    override fun connect() {
        println("Trying to connect...")
        val process = ProcessBuilder("expressvpn", "connect", "esma").start()
        val output = process.inputStream.reader(Charsets.UTF_8).readText()
        process.waitFor(30, TimeUnit.SECONDS)

        println("Process exit with status code ${process.exitValue()}. $output")
    }

    override fun disconnect() {
        println("Trying to discconnect...")
        val process = ProcessBuilder("expressvpn", "disconnect").start()
        val output = process.inputStream.reader(Charsets.UTF_8).readText()
        process.waitFor(10, TimeUnit.SECONDS)
        println("Process exit with status code ${process.exitValue()}. $output")
    }

    fun availableServers(): List<Server> {
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

    fun recommendedServers(): List<Server> {
        return availableServers().filter { it.isRecommended() }
    }

    private fun parseServer(it: String): Server {
        val id = it.substringBefore(" ")
        val description = it.substring(44)
        val recommended = description.endsWith("Y")
        val name = description.removeSuffix("Y").trim()
        return Server(id, id.substring(0, 2), name, recommended)
    }
}
