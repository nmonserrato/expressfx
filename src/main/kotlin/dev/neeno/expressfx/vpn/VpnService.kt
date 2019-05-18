package dev.neeno.expressfx.vpn

import javafx.scene.Parent

interface VpnService {
    fun status(): Status
    fun connect()
    fun disconnect()
    fun switchStatus(root: Parent)

    companion object {
        fun instance() = ExpressVpn()
    }
}
