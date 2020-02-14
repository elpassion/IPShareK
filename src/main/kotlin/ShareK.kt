import com.intellij.openapi.project.Project
import com.intellij.ui.CollectionListModel
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

object ShareK {

    val todos = CollectionListModel<String>()

    fun stop(project: Project) {
        server?.stop(100, 300)
        server = null
        notify("server stopped", project)
    }

    fun restart(project: Project) {
        stop(project)
        server = createServer(project)
        server?.start(false)
        notify("server started", project)
    }

    private var server: NettyApplicationEngine? = null
}


private fun createServer(project: Project) = embeddedServer(Netty, 8080) {
    routing {
        get("/status") {
            call.respondText("Hi clients! put some files!", ContentType.Text.Plain)
        }
        put("/files/{name}") {
            val n = call.parameters["name"] + ":\n" + call.receiveText()
            notify(n, project)
            call.respond(HttpStatusCode.OK)
        }
    }
}

