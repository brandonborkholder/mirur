package io.mirur.intellij

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformCoreDataKeys
import com.intellij.openapi.project.DumbAware
import com.intellij.xdebugger.XDebuggerManager

/**
 * Phase 4 debugger-aware action.
 *
 * Attempts to submit current editor selection/expression in the active debug session.
 */
class XDebuggerSubmitToMirurAction : SubmitToMirurAction(), DumbAware {
    override fun update(e: AnActionEvent) {
        val project = e.project
        val session = project?.let { XDebuggerManager.getInstance(it).currentSession }
        val selectedValue = selectedDebuggerValue(e)
        e.presentation.isEnabledAndVisible = session != null
            && MirurValueEligibility.isEligible(extractTypeText(selectedValue))
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val session = XDebuggerManager.getInstance(project).currentSession
        if (session == null) {
            notify(project, "No active debug session.")
            return
        }

        val selectedValue = selectedDebuggerValue(e)
        val selectedType = extractTypeText(selectedValue)
        if (!MirurValueEligibility.isEligible(selectedType)) {
            notify(project, "Selected debugger value is not yet supported in v0.")
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
                elementType = "double",
                shape = listOf(expression.length),
                min = 0.0,
                max = expression.length.toDouble(),
                sampleCount = expression.length,
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

    private fun selectedDebuggerValue(e: AnActionEvent): Any? = e.getData(PlatformCoreDataKeys.SELECTED_ITEM)

    private fun extractTypeText(selectedValue: Any?): String? {
        if (selectedValue == null) return null
        return readStringMember(selectedValue, "type")
            ?: readStringMember(selectedValue, "typeName")
            ?: readStringMember(selectedValue, "declaredType")
    }

    private fun readStringMember(target: Any, memberName: String): String? {
        val kClass = target::class.java
        val getterName = "get" + memberName.replaceFirstChar { it.uppercaseChar() }
        runCatching {
            val value = kClass.methods
                .firstOrNull { it.parameterCount == 0 && it.name == getterName }
                ?.invoke(target)
            value as? String
        }.getOrNull()?.let { return it }

        return runCatching {
            val field = kClass.declaredFields.firstOrNull { it.name == memberName } ?: return null
            field.isAccessible = true
            field.get(target) as? String
        }.getOrNull()
    }
}
