package dev.neeno.expressfx

import dev.neeno.expressfx.config.Recent
import dev.neeno.expressfx.gui.MainView
import javafx.stage.Stage
import tornadofx.App
import tornadofx.launch

class MainApp : App(MainView::class) {
    override fun start(stage: Stage) {
        stage.isResizable = false
        super.start(stage)
    }
}

fun main(args: Array<String>) {
    launch<MainApp>(args)
}
