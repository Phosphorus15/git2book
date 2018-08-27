import java.awt.Dimension
import javax.swing.JFrame
import javax.swing.JFrame.EXIT_ON_CLOSE

fun main(args: Array<String>) {
    JFrame().apply {
        title = "Export from github"
        size = Dimension(300, 60)
        defaultCloseOperation = EXIT_ON_CLOSE
        contentPane = ConvertUI { _, r -> r.run() }.mainPanel // TODO exportation interface
        isVisible = true
    }
}
