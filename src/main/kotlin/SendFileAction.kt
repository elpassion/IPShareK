import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys

class SendFileAction: AnAction("Send file") {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project!!
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: run { project.notify("No current file"); return }
        if (file.isDirectory) {
            project.notify("Current file is a directory")
            return
        }
        val content = file.inputStream.reader().readText()
        project.notify("TODO send current file\nname:\n${file.name}\ncontent:\n$content")
    }
}
