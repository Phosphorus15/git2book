import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.xmp.impl.Base64
import java.io.OutputStream

private val desc: String = "This pdf was exported from git sources, powered by"

private fun String.emptyBut(default: String): String = if (isEmpty()) default else this

class ResourceAnchor

fun pdfExportation(stream: OutputStream, repo: Repository) {
    val document = Document()
    val writer = PdfWriter.getInstance(document, stream)
    document.open()
    document.addTitle("Github Repository")
    document.addAuthor("git2book")
    document.add(Image.getInstance(Base64.decode(imageBase64.toByteArray())).apply { alignment = Element.ALIGN_CENTER })
    Paragraph(repo.name).apply {
        alignment = Element.ALIGN_CENTER
        font = font.apply { size = 36f }
        document.add(this)
    }
    Paragraph("\n\n" + repo.description.emptyBut("Exported from Git repository")).apply {
        alignment = Element.ALIGN_CENTER
        font = font.apply { size = 12f; color = BaseColor.GRAY }
        document.add(this)
    }
    Paragraph("\n\n" + repo.url).apply {
        alignment = Element.ALIGN_CENTER
        font = font.apply { size = 8f; color = BaseColor.GRAY }
        document.add(this)
    }
    document.newPage()
    writer.isPageEmpty = false
    Paragraph(desc).apply {
        alignment = Element.ALIGN_LEFT
        font = font.apply { size = 12f; color = BaseColor.DARK_GRAY }
        add(Chunk("git2book").apply {
            setAnchor("https://github.com/Phosphorus15/git2book")
            font = font.apply { size = 12f; color = BaseColor.BLUE }
            setUnderline(1f, 0f)
        })
        document.add(this)
    }
    document.close()
    writer.close()
}

fun main(args: Array<String>) {
    //pdfExportation(FileOutputStream(File("./test.pdf")))
}