package dev.neeno.expressfx.gui

import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.control.Button
import javafx.scene.image.Image
import javafx.scene.image.ImageView

fun ImageView.changeImageTo(resource: String): Image
{
    val icon = Image("icons/$resource")
    this.image = icon
    this.fitHeight = 128.0
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