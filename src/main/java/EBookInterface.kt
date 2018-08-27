import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfDestination
import com.itextpdf.text.pdf.PdfOutline
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.xmp.impl.Base64
import java.io.OutputStream
import java.nio.charset.Charset


private val desc: String = "This pdf was exported from git sources, powered by"

private fun String.emptyBut(default: String): String = if (isEmpty()) default else this

private fun String.tryReplaceIndent(): String = replace("\t", "    ")

class ResourceAnchor

class CentralText(text: String, fontSize: Float, fontColor: BaseColor = BaseColor.BLACK) : Paragraph(text) {
    init {
        alignment = Element.ALIGN_CENTER
        font = font.apply { size = fontSize; color = fontColor }
    }

    inline fun applyTo(document: Document, lbd: CentralText.() -> Unit = {}) {
        lbd()
        document.add(this)
    }
}

fun pdfExportation(stream: OutputStream, repo: Repository) { // FIXME this is not qingzhen
    val document = Document()
    val writer = PdfWriter.getInstance(document, stream)
    document.open()
    val root = writer.rootOutline
    println("started")
    PdfOutline(root, PdfDestination(PdfDestination.FITH, writer.getVerticalPosition(true)), "Cover")
    documentCover(document, writer, repo)
    val fileMark = PdfOutline(root,
            PdfDestination(
                    PdfDestination.FITH, writer.getVerticalPosition(true)),
            "files", true)
    documentContent(document, writer, repo, fileMark)
    document.close()
    writer.close()
}

private fun documentContent(document: Document, writer: PdfWriter, repo: Repository, outline: PdfOutline) =
        iterateFiles(document, writer, repo.rootEntry, outline, "")


private fun iterateFiles(document: Document, writer: PdfWriter, entry: Entry, outline: PdfOutline, path: String) {
    println(entry.name)
    when {
        entry.name == "" -> entry.listEntries().forEach {
            iterateFiles(document, writer, it, outline, "/")
        }
        entry.isDirectory -> {
            val dir = PdfOutline(outline,
                    PdfDestination(
                            PdfDestination.FITH, writer.getVerticalPosition(true)),
                    entry.name, true)
            entry.listEntries().forEach {
                iterateFiles(document, writer, it, dir, "$path${entry.name}/")
            }
        }
        else -> {
            val content = entry.getPossibleContent()
            if (content != null) {
                PdfOutline(outline,
                        PdfDestination(
                                PdfDestination.FITH, writer.getVerticalPosition(true)),
                        entry.name)
                CentralText(path + entry.name, 12f, BaseColor.GRAY).applyTo(document)
                val encoding = if (entry.getPossibleEncoding() == null) "UTF-8" else entry.getPossibleEncoding()
                Paragraph(String(content.readBytes(), Charset.forName(encoding)).tryReplaceIndent()).apply {
                    document.add(this)
                }
                document.newPage()
                writer.isPageEmpty = false
            }
        }
    }
}

private fun documentCover(document: Document, writer: PdfWriter, repo: Repository) {
    document.addTitle("Github Repository")
    document.addAuthor("git2book")
    document.add(Image.getInstance(Base64.decode(imageBase64.toByteArray())).apply { alignment = Element.ALIGN_CENTER })
    CentralText(repo.name, 36f).applyTo(document)
    CentralText("\n\n" + repo.description
            .emptyBut("Exported from Git repository"),
            12f, BaseColor.GRAY).applyTo(document)
    CentralText("\n\n" + repo.url, 8f, BaseColor.GRAY).applyTo(document)
    document.newPage()
    writer.isPageEmpty = false
    Paragraph(desc).apply {
        alignment = Element.ALIGN_LEFT
        font = font.apply { size = 12f; color = BaseColor.DARK_GRAY }
        add(Chunk(" git2book").apply {
            setAnchor("https://github.com/Phosphorus15/git2book")
            font = font.apply { size = 12f; color = BaseColor.BLUE }
            setUnderline(1f, 0f)
        })
        document.add(this)
    }
    document.newPage()
    writer.isPageEmpty = false
}

fun main(args: Array<String>) {

}