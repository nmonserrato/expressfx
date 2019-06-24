package dev.neeno.expressfx.events

import org.apache.logging.log4j.kotlin.logger

interface DomainEvent

interface Listener {
    fun somethingHappened(event: DomainEvent)
}

class Publisher private constructor() {

    private val listeners: MutableCollection<Listener> = LinkedHashSet()
    private val log = logger()

    companion object {
        private val instance = Publisher()
        fun publisher() = instance
    }

    fun register(listener: Listener) {
        log.info("Registered new listener of type ${listener::class}")
        listeners.add(listener)
    }

    fun notifyEvent(event: DomainEvent) {
        log.info("Notifing event of type ${event::class}")
        listeners.forEach { it.somethingHappened(event) }
    }
}