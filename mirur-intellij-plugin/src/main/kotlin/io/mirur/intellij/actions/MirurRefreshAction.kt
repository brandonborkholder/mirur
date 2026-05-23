package io.mirur.intellij.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class MirurRefreshAction(
    private val onRefresh: () -> Unit,
) : AnAction("Refresh") {
    override fun actionPerformed(e: AnActionEvent) {
        onRefresh()
    }
}
