package io.mirur.intellij

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAware

class SubmitToMirurAction : AnAction(), DumbAware {
    override fun actionPerformed(e: AnActionEvent) {
        NotificationGroupManager.getInstance()
            .getNotificationGroup("Mirur")
            .createNotification(
                "Mirur",
                "Submit to Mirur action triggered. Phase 4 will connect debugger values.",
                NotificationType.INFORMATION,
            )
            .notify(e.project)
    }
}
