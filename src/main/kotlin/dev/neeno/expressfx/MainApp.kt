package dev.neeno.expressfx

import dev.neeno.expressfx.gui.MainView
import javafx.scene.image.Image
import javafx.stage.Stage
import tornadofx.App
import tornadofx.launch

class MainApp : App(MainView::class) {
    override fun start(stage: Stage) {
        stage.isResizable = false
        stage.icons += Image("icons/spy.png")
        super.start(stage)
    }
}

fun main(args: Array<String>) {
    launch<MainApp>(args)
}
