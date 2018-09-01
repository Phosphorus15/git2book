import net.steepout.git2book.GitInterface
import net.steepout.git2book.walk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.kohsuke.github.GitHub

class GitInterfaceTest {

    @Test
    fun gatherRepo() {
        GitHub.connectAnonymously().apply {
            GitInterface(this).gatherRepo("Phosphorus15", "ExpectedVirus").apply {
                assertEquals("ExpectedVirus / [Java, C++]", this.toString())
            }
        }
    }

    @Test
    fun listContents() {
        GitHub.connectAnonymously().apply {
            GitInterface(this).gatherRepo("Phosphorus15", "ExpectedVirus").apply {
                assertTrue(rootEntry.listEntries().any { it.name == "main" })
            }
        }
    }

    @Test
    fun fetchContent() {
        GitHub.connectAnonymously().apply {
            GitInterface(this).gatherRepo("Phosphorus15", "ExpectedVirus").apply {
                rootEntry.walk { entry, s ->
                    if (s.endsWith("cpp")) {
                        entry.getPossibleContent()?.readBytes().apply {
                            println(String(this!!))
                        }
                    }
                }
            }
        }
    }
}