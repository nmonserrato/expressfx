package dev.neeno.expressfx.vpn

import javafx.scene.Parent
import javafx.scene.control.ListView

interface VpnService {
    fun switchStatus(container: Parent)
    fun renderStatus(container: Parent)
    fun renderServerList(container: ListView<Any>, onlyRecommended: Boolean = true)

    companion object {
        fun instance() = ExpressVpn()
    }
}
