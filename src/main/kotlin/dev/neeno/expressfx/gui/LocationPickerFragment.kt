package dev.neeno.expressfx.gui

import dev.neeno.expressfx.vpn.VpnService
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.control.ListView
import tornadofx.Fragment
import tornadofx.onUserSelect

class LocationPickerFragment : Fragment() {
    override val root: Parent by fxml(location = "/LocationPicker.fxml")
    private val vpn = VpnService.instance()

    init {
        title = "Location Picker"
        loadRecentList()
        loadRecommended()
        loadCompleteList()
    }

    private fun loadCompleteList() {
        val allTabList = root.find<ListView<Any>>("#allTabList")
        allTabList.onUserSelect { selectServer(it) }
        vpn.renderServerList(allTabList, onlyRecommended = false)
    }

    private fun loadRecommended() {
        val recommendedList = root.find<ListView<Any>>("#recommendedTabList")
        recommendedList.onUserSelect { selectServer(it) }
        vpn.renderServerList(recommendedList)
    }

    private fun loadRecentList() {
        val recentList = root.find<ListView<Any>>("#recentTabList")
        recentList.onUserSelect { selectServer(it) }
        vpn.renderRecentServerList(recentList)
    }

    private fun selectServer(it: Any) {
        vpn.selectServer(it as Node)
        this.close()
    }
}
