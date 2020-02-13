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
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveText
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.put
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine

class TodoWindowFactory : ToolWindowFactory {

    companion object {

        val todosModel = CollectionListModel<String>()

        var server: NettyApplicationEngine? = null
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

        val serverPanel = panel {
            row {
                button("start") {
                    server?.stop(100, 300)
                    server = createServer(project)
                    server?.start(false)
                    notify("server started", project)
                }
            }
            row {
                button("stop") {
                    server?.stop(100, 300)
                    server = null
                    notify("server stopped", project)
                }
            }
            row {
                button("status") {
                    notify("TODO", project)
                }
            }
        }

        val contentFactory = toolWindow.contentManager.factory
        val todoContent = contentFactory.createContent(todoPanel, "TODO", true)
        val serverContent = contentFactory.createContent(serverPanel, "Server", true)
        val otherContent = contentFactory.createContent(otherPanel, "Other content", true)
        toolWindow.contentManager.addContent(todoContent)
        toolWindow.contentManager.addContent(serverContent)
        toolWindow.contentManager.addContent(otherContent)
    }
}

fun createServer(project: Project) = embeddedServer(Netty, 8080) {
    routing {
        get("/status") {
            call.respondText("Hi clients! put some files!", ContentType.Text.Plain)
        }
        put("/files/*") {
            notify(call.receiveText(), project)
            call.respond(HttpStatusCode.OK)
        }
    }
}


