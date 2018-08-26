import org.junit.Test
import org.kohsuke.github.GitHub
import java.io.File
import java.io.FileOutputStream

class EBookInterfaceKtTest {

    @Test
    fun pdfExportation() {
        GitHub.connectAnonymously().apply {
            GitInterface(this).gatherRepo("Phosphorus15", "ExpectedVirus").apply {
                pdfExportation(FileOutputStream(File("./test.pdf")), this)
            }
        }
    }
}