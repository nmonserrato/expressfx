package dev.neeno.expressfx.infrastructure.vpn

import dev.neeno.expressfx.domain.vpn.Server
import dev.neeno.expressfx.domain.vpn.Status
import java.util.concurrent.TimeUnit

class ExpressVpn {
    fun status(): Status {
        println("Fetching status...")
        val process = ProcessBuilder("expressvpn", "status").start()
        val output = process.inputStream.reader(Charsets.UTF_8).let { it.readLines()[0] }
        println(output)
        process.waitFor()
        if (output.contains("Not connected"))
            return Status.DISCONNECTED

        val regex = """.*Connected to (\w+) - (\w+)""".toRegex()
        val (country, city) = regex.find(output)!!.destructured
        return Status.connectedTo(Server(country, city))
    }

    fun connect() {
        println("Trying to connect...")
        val process = ProcessBuilder("expressvpn", "connect", "esma").start()
        val output = process.inputStream.reader(Charsets.UTF_8).let { it.readText() }
        process.waitFor(30, TimeUnit.SECONDS)

        println("Process exit with status code ${process.exitValue()}. $output")
    }

    fun disconnect() {
        println("Trying to discconnect...")
        val process = ProcessBuilder("expressvpn", "disconnect").start()
        val output = process.inputStream.reader(Charsets.UTF_8).let { it.readText() }
        process.waitFor(10, TimeUnit.SECONDS)
        println("Process exit with status code ${process.exitValue()}. $output")
    }
}
