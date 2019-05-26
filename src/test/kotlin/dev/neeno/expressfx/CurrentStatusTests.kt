package dev.neeno.expressfx

import dev.neeno.expressfx.gui.MainView
import dev.neeno.expressfx.gui.find
import dev.neeno.expressfx.vpn.ExpressVpn
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.image.ImageView
import jdk.nashorn.internal.ir.annotations.Ignore
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import javafx.embed.swing.JFXPanel
import javax.swing.SwingUtilities
import java.util.concurrent.CountDownLatch

@Ignore
class CurrentStatusTests {
    private val view = MainView(ExpressVpn(InMemoryProcessExecutor()))

    companion object {
        @BeforeAll
        @JvmStatic
        fun setupEnv() {
            System.setProperty("env.test", "true")
            setupJavaFX()
        }

        @AfterAll
        @JvmStatic
        fun clearEnv() {
            System.clearProperty("env.test")
        }

        private fun setupJavaFX() {
            val latch = CountDownLatch(1)
            SwingUtilities.invokeLater {
                JFXPanel()
                latch.countDown()
            }

            try {
                latch.await()
            } catch (e: InterruptedException) {
                throw RuntimeException(e)
            }

        }
    }

    @Test
    internal fun `current disconnected status is displayed`() {

        assertTrue(view.root.find<Node>("#connectedStatus").isVisible)

        assertTrue(view.root.find<Node>("#chooseLocationContainer").isVisible)

        assertFalse(view.root.find<Button>("#connectButton").isDisabled)
        assertEquals("CONNECT", view.root.find<Button>("#connectButton").text)


        assertTrue(view.root.find<ImageView>("#connectedIcon").image.impl_getUrl().endsWith("unlocked.png"))
    }
}
