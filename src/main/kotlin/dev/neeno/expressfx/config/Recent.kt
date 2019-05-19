package dev.neeno.expressfx.config

import java.io.File
import java.util.*
import kotlin.streams.toList

class Recent {
    companion object {
        private const val RECENT_LIST_SIZE = 10
        private val instance = Recent()
        fun file() = instance
    }

    private val file = File(System.getProperty("user.home") + "/.config/expressfx/recents")
    private val recentServers = LinkedList<String>()

    fun lastServerId() = recentServers[0]

    fun saveLast(id: String?) {
        if(recentServers.contains(id)) recentServers.remove(id)
        if(recentServers.size == RECENT_LIST_SIZE) recentServers.removeLast()
        recentServers.addFirst(id)
        val serializedList = recentServers.joinToString(separator = System.getProperty("line.separator"))
        file.writeText(serializedList)
    }

    fun allServers(): List<String> = recentServers.stream().toList()

    init {
        file.parentFile.mkdirs()
        if (!file.exists()) file.writeText("smart")
        file.readLines().forEach { recentServers.addLast(it) }
    }
}