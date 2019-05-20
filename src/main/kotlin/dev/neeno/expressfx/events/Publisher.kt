package dev.neeno.expressfx.events

interface DomainEvent

interface Listener {
    fun somethingHappened(event: DomainEvent)
}

class Publisher private constructor() {

    private val listeners: MutableCollection<Listener> = LinkedHashSet()

    companion object {
        private val instance = Publisher()
        fun publisher() = instance
    }

    fun register(listener: Listener) {
        println("Registered new listener of type ${listener::class}")
        listeners.add(listener)
    }

    fun notifyEvent(event: DomainEvent) {
        println("Notifing event of type ${event::class}")
        listeners.forEach { it.somethingHappened(event) }
    }
}