package dev.neeno.expressfx.gui

import dev.neeno.expressfx.vpn.VpnService
import javafx.scene.Parent
import javafx.scene.control.Button
import javafx.scene.control.Hyperlink
import javafx.stage.Modality
import tornadofx.View
import tornadofx.action


class MainView(private val vpn: VpnService) : View() {
    override val root: Parent by fxml(location = "/MainScreen.fxml")
    private val chooseLocationDialog = LocationPickerFragment()

    @Suppress("unused")
    constructor() : this(VpnService.instance())

    init {
        title = "ExpressFX"
        vpn.renderStatus(root)

        val button = root.find<Button>("#connectButton")
        button.action { vpn.switchStatus(root) }

        val chooseLocationLink = root.find<Hyperlink>("#chooseLocationLink")

        chooseLocationLink.action {
            chooseLocationDialog.openModal(
                modality = Modality.APPLICATION_MODAL,
                escapeClosesWindow = true,
                block = true,
                resizable = false
            )

            vpn.renderStatus(root)
        }
    }
}
