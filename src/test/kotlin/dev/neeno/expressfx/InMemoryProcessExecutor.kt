package dev.neeno.expressfx

import dev.neeno.expressfx.vpn.ProcessExecutor

class InMemoryProcessExecutor:ProcessExecutor {
    private var connected = false
    override fun exec(process: String, vararg arguments: String): List<String> {
        if (process != "expressvpn") {
            throw AssertionError("wrong executable invoked")
        }

        if (arguments[0] == "status") {
            return if (connected) listOf("Connected to Spain - Barcelona") else listOf("Not connected")
        }

        if (arguments[0] == "list") {
            return listOf("ALIAS COUNTRY                               LOCATION                       RECOMMENDED",
                    "----- ---------------                       ------------------------------ -----------",
                    "smart Smart Location                        Spain - Barcelona              Y",
                    "inmu1 India (IN)                            India - Mumbai - 1             ",
                    "in                                          India (via UK)                 ",
                    "inch                                        India - Chennai                ")
        }

        return emptyList()
    }
}