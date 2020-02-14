import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBTextField
import com.intellij.ui.layout.CCFlags
import com.intellij.ui.layout.GrowPolicy
import com.intellij.ui.layout.panel

class TodoWindowFactory : ToolWindowFactory {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {

        val todoPanel = panel {
            row {
                val newTodoField = JBTextField()
                newTodoField(CCFlags.grow, growPolicy = GrowPolicy.MEDIUM_TEXT)
                right { button("+") { ShareK.todos.add(0, newTodoField.text); newTodoField.text = "" } }
            }
            val jbList = JBList(ShareK.todos)
            val decorator = ToolbarDecorator.createDecorator(jbList)
            val todosPanel = decorator.createPanel()
            row {
                todosPanel(CCFlags.grow, growPolicy = GrowPolicy.MEDIUM_TEXT)
            }
        }

        val otherPanel = panel {
            for (i in 1..10)
                row { label("bla bla bal $i") }
            row("outer row") {
                checkBox("check outer")
                row("inner") { for (i in 1..5) checkBox("$i") }
                row("inner") { for (i in 1..5) checkBox("$i") }
            }
        }

        val ktorPanel = panel {
            row { button("start") { ShareK.restart(project) } }
            row { button("stop") { ShareK.stop(project) } }
            row { button("status") { project.notify("TODO") } }
        }

        val tabContent = toolWindow.contentManager.factory::createContent

        toolWindow.contentManager.run {
            addContent(tabContent(todoPanel, "TODO", true))
            addContent(tabContent(ktorPanel, "KTOR", true))
            addContent(tabContent(otherPanel, "Other content", true))
        }
    }
}


