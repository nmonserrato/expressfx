package dev.neeno.expressfx.infrastructure.vpn

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
    internal fun status() {
        println(vpn.status())
    }
}
