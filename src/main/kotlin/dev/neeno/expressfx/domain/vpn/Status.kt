package dev.neeno.expressfx.domain.vpn

data class Status private constructor(
    private val connected: Boolean,
    private val server: Server? = null
) {
    companion object {
        val DISCONNECTED = Status(connected = false)
        fun connectedTo(server: Server) = Status(connected = true, server = server)
    }
}


data class Server(private val country: String,
                  private val city: String)
