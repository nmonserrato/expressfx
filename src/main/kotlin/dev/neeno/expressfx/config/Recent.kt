package dev.neeno.expressfx.config

import dev.neeno.expressfx.events.DomainEvent
import dev.neeno.expressfx.events.Listener
import dev.neeno.expressfx.events.Publisher.Companion.publisher
import dev.neeno.expressfx.events.VpnConnected
import java.io.File
import java.util.*
import kotlin.streams.toList

class Recent : Listener {
    companion object {
        private const val RECENT_LIST_SIZE = 10
        private val instance = Recent()
        fun file() = instance
    }

    private val file = File(System.getProperty("user.home") + "/.config/expressfx/recents")
    private val recentServers = LinkedList<String>()

    fun lastServerId() = recentServers[0]

    override fun somethingHappened(event: DomainEvent) {
        println("Recent received event of type ${event::class}")
        if (event is VpnConnected)
            saveLast(event.server.cmdLineId())
    }

    private fun saveLast(id: String?) {
        if (recentServers.contains(id)) recentServers.remove(id)
        if (recentServers.size == RECENT_LIST_SIZE) recentServers.removeLast()
        recentServers.addFirst(id)
        val serializedList = recentServers.joinToString(separator = System.getProperty("line.separator"))
        file.writeText(serializedList)
    }

    fun allServers(): List<String> = recentServers.stream().toList()

    init {
        file.parentFile.mkdirs()
        if (!file.exists()) file.writeText("smart")
        file.readLines().forEach { recentServers.addLast(it) }
        publisher().register(this)
    }
}