import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.project.Project


fun Project?.notify(content: String) = Notifications.Bus.notify(notification(content), this)

private fun notification(content: String)
        = Notification("some group disp id", "some title", content, NotificationType.WARNING)

