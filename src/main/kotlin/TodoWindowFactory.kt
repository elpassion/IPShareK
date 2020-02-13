import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.CollectionListModel
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBTextField
import com.intellij.ui.layout.CCFlags
import com.intellij.ui.layout.GrowPolicy
import com.intellij.ui.layout.panel
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

class TodoWindowFactory: ToolWindowFactory {

    companion object {
        val todosModel = CollectionListModel<String>()

        val server = embeddedServer(Netty, 8080) {
            routing {
                get("/status") {
                    call.respondText("Hi clients! post me some files!", ContentType.Text.Plain)
                }
            }
        }
    }

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val todoPanel = panel {
            row {
                val newTodoField = JBTextField()
                newTodoField(CCFlags.grow, growPolicy = GrowPolicy.MEDIUM_TEXT)
                right { button("+") { todosModel.add(0, newTodoField.text); newTodoField.text = "" } }
            }
            val jbList = JBList(todosModel)
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
        val contentFactory = toolWindow.contentManager.factory
        val todoContent = contentFactory.createContent(todoPanel, "TODO", true)
        val otherContent = contentFactory.createContent(otherPanel, "Other content", true)
        toolWindow.contentManager.addContent(todoContent)
        toolWindow.contentManager.addContent(otherContent)
    }
}

