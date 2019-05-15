package dev.neeno.expressfx

import dev.neeno.expressfx.gui.MainView
import tornadofx.App
import tornadofx.launch

class MainApp : App(MainView::class)

fun main(args: Array<String>) {
    launch<MainApp>(args)
}