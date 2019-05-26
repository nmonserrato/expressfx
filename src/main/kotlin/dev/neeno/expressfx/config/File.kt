package dev.neeno.expressfx.config

import java.lang.Boolean

interface File {
    companion object {
        fun withName(name: String): File =
            if (Boolean.getBoolean("env.test")) InMemoryFile()
            else FileSystemFile(name)
    }

    fun initializeIfNotExists(content: String)
    fun readLines(): List<String>
    fun overwriteContent(content: String)
}

internal class FileSystemFile(fileName: String) : File {
    private val realFile: java.io.File =
        java.io.File(System.getProperty("user.home") + "/.config/expressfx/$fileName")

    init {
        realFile.parentFile.mkdirs()
    }

    override fun initializeIfNotExists(content: String) {
        if (!realFile.exists()) realFile.writeText(content)
    }

    override fun readLines() = realFile.readLines()

    override fun overwriteContent(content: String) = realFile.writeText(content)
}

internal class InMemoryFile : File {

    private var content: String = ""

    override fun initializeIfNotExists(content: String) {
        this.content = content
    }

    override fun readLines(): List<String> {
        return content.split(System.getProperty("line.separator"))
    }

    override fun overwriteContent(content: String) {
        this.content = content
    }

}
