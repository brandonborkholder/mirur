package io.mirur.intellij.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import io.mirur.intellij.MirurSettingsService

class MirurSettingsAction : AnAction("Settings") {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        project.getService(MirurSettingsService::class.java)
    }
}
