import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages

class ExampleAction: AnAction("Example action") {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project!!
        Messages.showMessageDialog(project, "Hello from ELPassion", "Example title", Messages.getInformationIcon())
        project.notify("jooo")
    }
}
