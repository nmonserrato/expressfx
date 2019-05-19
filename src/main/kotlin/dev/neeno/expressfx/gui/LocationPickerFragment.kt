package dev.neeno.expressfx.gui

import dev.neeno.expressfx.vpn.VpnService
import javafx.scene.Parent
import javafx.scene.control.ListView
import tornadofx.Fragment
import tornadofx.Stylesheet.Companion.tab

class LocationPickerFragment : Fragment() {
    override val root: Parent by fxml(location = "/LocationPicker.fxml")
    private val vpn = VpnService.instance()

    init {
        title = "Location Picker"
        val recommendedList = root.find<ListView<Any>>("#recommendedTabList")
        val allTabList = root.find<ListView<Any>>("#allTabList")

        vpn.renderServerList(recommendedList)
        vpn.renderServerList(allTabList, onlyRecommended = false)
    }
}
