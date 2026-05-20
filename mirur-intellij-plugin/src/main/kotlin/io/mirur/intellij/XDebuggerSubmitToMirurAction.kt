package io.mirur.intellij

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAware
import com.intellij.xdebugger.XDebuggerManager

/**
 * Phase 4 debugger-aware action.
 *
 * Attempts to submit current editor selection/expression in the active debug session.
 */
class XDebuggerSubmitToMirurAction : SubmitToMirurAction(), DumbAware {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val session = XDebuggerManager.getInstance(project).currentSession
        if (session == null) {
            notify(project, "No active debug session.")
            return
        }

        val sourcePosition = session.currentPosition
        val expression = e.getData(com.intellij.openapi.actionSystem.CommonDataKeys.EDITOR)
            ?.selectionModel
            ?.selectedText
            ?.takeIf { it.isNotBlank() }
            ?: sourcePosition?.file?.name

        if (expression.isNullOrBlank()) {
            notify(project, "No selected expression to submit.")
            return
        }

        MirurSubmissionBus.getInstance(project).submit(
            MirurVariableSnapshot(
                name = expression,
                signature = "debug-expression",
                value = expression,
            )
        )

        notify(project, "Queued debug expression for Mirur: $expression")
    }

    private fun notify(project: com.intellij.openapi.project.Project, message: String) {
        NotificationGroupManager.getInstance()
            .getNotificationGroup("Mirur")
            .createNotification("Mirur", message, NotificationType.INFORMATION)
            .notify(project)
    }
}
