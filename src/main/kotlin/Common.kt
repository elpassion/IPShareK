import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.project.Project


fun notify(content: String, project: Project?) = Notifications.Bus.notify(notification(content), project)

private fun notification(content: String)
        = Notification("some group disp id", "some title", content, NotificationType.WARNING)