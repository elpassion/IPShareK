import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.layout.panel

private var currentTodo: String = ""

class TodoWindowFactory: ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val todoPanel = panel {
            row {
                textField({ currentTodo }, {
                    currentTodo = it
                    notify(it, project)
                })
            }
        }
        val otherPanel = panel {
            for (i in 1..20)
            row { label("bla bla bal $i") }
        }
        val contentFactory = toolWindow.contentManager.factory
        val todoContent = contentFactory.createContent(todoPanel, "TODO", true)
        val otherContent = contentFactory.createContent(otherPanel, "Other content", true)
        toolWindow.contentManager.addContent(todoContent)
        toolWindow.contentManager.addContent(otherContent)
    }
}

