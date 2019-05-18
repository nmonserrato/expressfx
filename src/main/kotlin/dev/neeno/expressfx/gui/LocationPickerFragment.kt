package dev.neeno.expressfx.gui

import javafx.scene.Parent
import tornadofx.Fragment

class LocationPickerFragment : Fragment() {
    override val root: Parent by fxml(location = "/LocationPicker.fxml")

    init {
        title = "Location Picker"
    }
}
