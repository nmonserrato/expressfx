package dev.neeno.expressfx.gui

import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.control.Button
import javafx.scene.image.Image
import javafx.scene.image.ImageView

fun ImageView.changeImageTo(resource: String, folder: String = "icons", height: Double = 128.0): Image {
    val icon = Image("$folder/$resource")
    this.image = icon
    this.fitHeight = height
    this.isPreserveRatio = true
    return icon
}

fun Button.enable(text: String) {
    this.text = text
    this.isDisable = false
}

fun Button.disable(text: String) {
    this.text = text
    this.isDisable = true
}

fun <T : Node> Parent.find(selector: String): T {
    return this.lookup(selector) as T
}