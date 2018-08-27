import org.kohsuke.github.GitHub
import java.awt.Dimension
import java.io.FileOutputStream
import javax.swing.JFrame
import javax.swing.JFrame.EXIT_ON_CLOSE
import javax.swing.JOptionPane

fun main(args: Array<String>) {
    JFrame().apply {
        title = "Export from github"
        size = Dimension(300, 60)
        defaultCloseOperation = EXIT_ON_CLOSE
        contentPane = ConvertUI(::execute).mainPanel // exportation interface
        setLocationRelativeTo(null)
        isVisible = true
    }
}

fun execute(location: String, callBack: Runnable) {
    Thread {
        try {
            pdfExportation(FileOutputStream("./output.pdf")
                    , GitInterface(GitHub.connectAnonymously()).gatherRepo(location))
            JOptionPane.showMessageDialog(null, "Exportation Complete")
        } catch (e: Exception) {
            JOptionPane.showMessageDialog(null, e.message, e.javaClass.simpleName, JOptionPane.ERROR_MESSAGE)
        } finally {
            callBack.run()
        }
    }.start()
}
