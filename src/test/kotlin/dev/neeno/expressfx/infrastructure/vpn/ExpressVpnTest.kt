package dev.neeno.expressfx.infrastructure.vpn

import dev.neeno.expressfx.vpn.ExpressVpn
import jdk.nashorn.internal.ir.annotations.Ignore
import org.junit.jupiter.api.Test

@Ignore
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
    internal fun allLocations() {
        vpn.availableServers()
    }

    @Test
    internal fun recommendedLocations() {
        vpn.recommendedServers()
    }

    @Test
    internal fun status() {
        println(vpn.status())
    }
}
