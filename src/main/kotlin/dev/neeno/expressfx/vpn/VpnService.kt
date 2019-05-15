package dev.neeno.expressfx.vpn

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
