package net.steepout.git2book

import org.kohsuke.github.GHContent
import org.kohsuke.github.GHRepository
import org.kohsuke.github.GitHub
import java.io.InputStream

class GitInterface(private val github: GitHub) {

    fun gatherRepo(repo: String): Repository =
            github.getRepository(repo).run {
                GHRepositoryImpl(this).apply {

                }
            }

    fun gatherRepo(user: String, id: String) = gatherRepo("$user/$id")

}

abstract class Repository {

    abstract val name: String

    abstract val languages: List<String>

    abstract val rootEntry: Entry

    abstract val description: String

    abstract val url: String

    override fun toString(): String {
        return "$name / $languages"
    }

}

abstract class Entry {

    abstract val name: String

    abstract val isDirectory: Boolean

    abstract fun listEntries(): List<Entry>

    abstract fun getPossibleEncoding(): String?

    abstract fun getPossibleContent(): InputStream? // open connection

}

class GHEntryImpl(private val content: GHContent) : Entry() {
    override fun getPossibleEncoding(): String? = content.encoding

    override fun getPossibleContent(): InputStream? = content.read()

    override val name: String = content.name

    override val isDirectory: Boolean = content.isDirectory

    override fun listEntries(): List<Entry> =
            if (content.isDirectory) content.listDirectoryContent().map { GHEntryImpl(it) }
            else throw RuntimeException("Illegal access")

}

class VirtualGHRoot(private val repo: GHRepository) : Entry() {
    override fun getPossibleEncoding(): String? = null

    override fun getPossibleContent(): InputStream? = null

    override val name: String = ""
    override val isDirectory: Boolean = true

    override fun listEntries(): List<Entry> = repo.getDirectoryContent("/").map { GHEntryImpl(it) }

}

private class GHRepositoryImpl(repo: GHRepository) : Repository() {
    override val url: String = repo.htmlUrl.toString()
    override val description: String = repo.description
    override val name: String = repo.name
    override val languages: List<String> = repo.listLanguages().keys.toList()
    override val rootEntry: Entry

    init {
        rootEntry = VirtualGHRoot(repo)
    }

}

fun Entry.walk(lbd: (Entry, String) -> Unit) {
    if (isDirectory && name == "") walk("", lbd)
    else lbd(this, name)
}

private fun Entry.walk(path: String, lbd: (Entry, String) -> Unit) {
    if (isDirectory) listEntries().forEach {
        it.walk("$path$name/", lbd)
    } else lbd(this, path + name)
}

fun main(args: Array<String>) {

}
