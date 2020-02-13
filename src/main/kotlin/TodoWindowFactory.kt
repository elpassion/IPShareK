import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.layout.panel

class TodoWindowFactory: ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val panel = panel {
            row {
                textField({ "" }, { println(it) })
            }
        }
        toolWindow.component.add(panel)
    }
}