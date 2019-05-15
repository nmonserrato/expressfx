package dev.neeno.expressfx.domain.vpn

import dev.neeno.expressfx.infrastructure.vpn.ExpressVpn
import javafx.scene.layout.VBox

interface VpnService {
    fun status(): Status
    fun connect()
    fun disconnect()
    fun switchStatus(root: VBox)

    companion object {
        fun instance() = ExpressVpn()
    }
}
