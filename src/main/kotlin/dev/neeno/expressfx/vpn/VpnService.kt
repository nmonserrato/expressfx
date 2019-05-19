package dev.neeno.expressfx.vpn

import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.control.ListView

interface VpnService {
    fun switchStatus(container: Parent)
    fun renderStatus(container: Parent)
    fun renderServerList(container: ListView<Any>, onlyRecommended: Boolean = true)
    fun renderRecentServerList(container: ListView<Any>)
    fun selectServer(guiItem: Node)

    companion object {
        private val instance = ExpressVpn()
        fun instance() = instance
    }
}
