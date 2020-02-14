import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys

class SendFileAction: AnAction("Send file") {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project!!
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: run { notify("No current file", project); return }
        if (file.isDirectory) { notify("Current file is a directory", project); return }
        val content = file.inputStream.reader().readText()
        notify("TODO send current file\nname:\n${file.name}\ncontent:\n$content", project)
    }
}
