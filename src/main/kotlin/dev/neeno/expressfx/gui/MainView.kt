package dev.neeno.expressfx.gui

import dev.neeno.expressfx.vpn.VpnService
import javafx.scene.control.Button
import javafx.scene.layout.VBox
import tornadofx.View
import tornadofx.action


class MainView : View() {
    override val root: VBox by fxml(location = "/MainScreen.fxml")

    private val vpn = VpnService.instance()

    init {
        title = "ExpressFX"
        vpn.status().render(root)

        val button = root.lookup("#connectButton") as Button
        button.action { vpn.switchStatus(root) }
    }
}
