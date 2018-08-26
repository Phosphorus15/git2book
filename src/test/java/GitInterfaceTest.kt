import org.junit.Test

import org.junit.Assert.*
import org.kohsuke.github.GitHub

class GitInterfaceTest {

    @Test
    fun gatherRepo() {
        GitHub.connectAnonymously().apply {
            GitInterface(this).gatherRepo("Phosphorus15","ExpectedVirus").apply {
                println(this)
            }
        }
    }

    @Test
    fun gatherRepo1() {
    }
}