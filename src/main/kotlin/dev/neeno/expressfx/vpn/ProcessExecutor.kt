package dev.neeno.expressfx.vpn

import org.apache.logging.log4j.kotlin.logger
import java.util.*
import java.util.concurrent.TimeUnit

interface ProcessExecutor {
    fun exec(process: String, vararg arguments: String): List<String>

    companion object {
        fun cli() = CliProcessExecutor() as ProcessExecutor
    }
}

private class CliProcessExecutor internal constructor(): ProcessExecutor {
    private val log = logger()
    override fun exec(process: String, vararg arguments: String): List<String> {
        log.info("Executing '$process' with arguments ${Arrays.toString(arguments)}")

        val cmd = LinkedList<String>()
        cmd.addFirst(process)
        cmd.addAll(arguments)

        val cliProcess = ProcessBuilder(cmd).start()
        val output = cliProcess.inputStream.reader(Charsets.UTF_8).let { it.readLines() }
        cliProcess.waitFor(20, TimeUnit.SECONDS)

        log.info("Process exit with status code ${cliProcess.exitValue()}. $output")
        return output
    }
}
