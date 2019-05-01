package dev.neeno.expressfx.infrastructure.vpn

import org.junit.jupiter.api.Test

class ExpressVpnTest {
    private val vpn = ExpressVpn()

    @Test
    internal fun disconnection() {
        vpn.disconnect()
    }

    @Test
    internal fun connection() {
        vpn.connect()
    }

    @Test
    internal fun status() {
        println(vpn.status())
    }
}
