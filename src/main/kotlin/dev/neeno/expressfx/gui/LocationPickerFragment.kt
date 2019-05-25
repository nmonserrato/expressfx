package dev.neeno.expressfx.gui

import dev.neeno.expressfx.events.DomainEvent
import dev.neeno.expressfx.events.Listener
import dev.neeno.expressfx.events.Publisher.Companion.publisher
import dev.neeno.expressfx.events.RecentServersChanged
import dev.neeno.expressfx.vpn.VpnService
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.control.ListView
import tornadofx.Fragment
import tornadofx.onUserSelect

class LocationPickerFragment : Listener, Fragment() {
    override val root: Parent by fxml(location = "/LocationPicker.fxml")

    private val vpn = VpnService.instance()
    init {
        title = "Location Picker"
        initRecentList()
        initRecommendedList()
        initCompleteServerList()
        publisher().register(this)
    }

    override fun somethingHappened(event: DomainEvent) {
        if (event is RecentServersChanged)
            initRecentList()
    }

    private fun initCompleteServerList() {
        val allTabList = root.find<ListView<Any>>("#allTabList")
        allTabList.onUserSelect { selectServer(it) }
        vpn.renderAllServerList(allTabList)
    }

    private fun initRecommendedList() {
        val recommendedList = root.find<ListView<Any>>("#recommendedTabList")
        recommendedList.onUserSelect { selectServer(it) }
        vpn.renderRecommendedServerList(recommendedList)
    }

    private fun initRecentList() {
        val recentList = root.find<ListView<Any>>("#recentTabList")
        recentList.onUserSelect { selectServer(it) }
        vpn.renderRecentServerList(recentList)
    }

    private fun selectServer(it: Any) {
        vpn.selectServer(it as Node)
        this.close()
    }
}
