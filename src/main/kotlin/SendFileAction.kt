import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile

class SendFileAction: AnAction("Send file") {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project!!
        val vFile: VirtualFile? = e.getData(CommonDataKeys.VIRTUAL_FILE)
        notify("TODO send current file", project)
    }
}
