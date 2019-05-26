package dev.neeno.expressfx.config

import dev.neeno.expressfx.events.DomainEvent
import dev.neeno.expressfx.events.Listener
import dev.neeno.expressfx.events.Publisher.Companion.publisher
import dev.neeno.expressfx.events.RecentServersChanged
import dev.neeno.expressfx.events.VpnConnected
import java.util.*
import kotlin.streams.toList

interface Recent {
    companion object {
        internal const val RECENT_LIST_SIZE = 10
        private val instance = RecentFile()
        fun file() = instance
    }

    fun lastServerId(): String
    fun allServers(): List<String>
}

class RecentFile(private val file: File) : Recent, Listener {

    constructor() : this(File.withName("recents"))

    private val recentServers = LinkedList<String>()

    init {
        file.initializeIfNotExists("smart")
        file.readLines().forEach { recentServers.addLast(it) }
        publisher().register(this)
    }

    override fun lastServerId() = recentServers[0]

    override fun allServers(): List<String> = recentServers.stream().toList()

    override fun somethingHappened(event: DomainEvent) {
        println("Recent received event of type ${event::class}")
        if (event is VpnConnected)
            saveLast(event.server.cmdLineId())
    }

    private fun saveLast(id: String?) {
        if (recentServers.contains(id)) recentServers.remove(id)
        if (recentServers.size == Recent.RECENT_LIST_SIZE) recentServers.removeLast()
        recentServers.addFirst(id)
        val serializedList = recentServers.joinToString(separator = System.getProperty("line.separator"))
        file.overwriteContent(serializedList)
        publisher().notifyEvent(RecentServersChanged())
    }
}