import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.ui.CollectionListModel
import io.ktor.application.call
import io.ktor.client.HttpClient
import io.ktor.client.request.put
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
import kotlinx.coroutines.runBlocking
import java.io.File

object ShareK {

    val todos = CollectionListModel<String>()

    fun stop(project: Project) {
        server?.stop(100, 300)
        server = null
        project.notify("server stopped")
    }

    fun restart(project: Project) {
        stop(project)
        server = createServer(project)
        server?.start(false)
        project.notify("server started")
    }

    fun send(name: String, content: String, project: Project) {
        val addr = try { todos.getElementAt(0) } catch (e: IndexOutOfBoundsException) {
            project.notify("Please put your friend ip addr as first todo note")
            return
        }
        runBlocking {
            client.put<Unit>("http://$addr:8080/files/$name") {
                body = content
            }
        }
    }

    private var server: NettyApplicationEngine? = null

    private val client = HttpClient()
}


private fun createServer(project: Project) = embeddedServer(Netty, 8080) {
    routing {
        get("/status") {
            call.respondText("Hi clients! put some files!", ContentType.Text.Plain)
        }
        put("/files/{name}") {
            val name = call.parameters["name"] ?: "unknown.txt"
            val content = call.receiveText()
            project.saveShareKFile(name, content)
            call.respond(HttpStatusCode.OK)
        }
    }
}

private fun Project.saveShareKFile(name: String, content: String) {
    val base = basePath ?: run { notify("No project base path"); return }
    val dir = "$base/sharek/"
    File(dir).mkdirs()
    val path = "$dir$name"
    val file = File(path)
    file.writeText(content)
    LocalFileSystem.getInstance().refreshAndFindFileByIoFile(file)
    notify("Saved:\n$path")
    // TODO: log clickable file name
}
