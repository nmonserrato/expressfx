package dev.neeno.expressfx.infrastructure.vpn

import dev.neeno.expressfx.domain.vpn.Server
import dev.neeno.expressfx.domain.vpn.Status
import dev.neeno.expressfx.domain.vpn.Status.Companion.DISCONNECTED
import dev.neeno.expressfx.domain.vpn.VpnService
import javafx.scene.layout.VBox
import java.util.concurrent.TimeUnit

class ExpressVpn : VpnService {
    override fun status(): Status {
        println("Fetching status...")
        val process = ProcessBuilder("expressvpn", "status").start()
        val output = process.inputStream.reader(Charsets.UTF_8).let { it.readLines()[0] }
        println(output)
        process.waitFor()
        if (output.contains("Not connected"))
            return DISCONNECTED

        val regex = """.*Connected to (\w+) - (\w+)""".toRegex()
        val (country, city) = regex.find(output)!!.destructured
        return Status.connectedTo(Server(country, city))
    }

    override fun switchStatus(root: VBox) {
        if (status() == DISCONNECTED) connect() else disconnect()
        status().render(root)
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
}
