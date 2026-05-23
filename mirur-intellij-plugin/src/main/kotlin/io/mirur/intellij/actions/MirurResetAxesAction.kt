package io.mirur.intellij.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class MirurResetAxesAction(
    private val state: MirurViewState,
    private val onResetAxes: (Long) -> Unit,
) : AnAction("Reset Axes") {
    override fun actionPerformed(e: AnActionEvent) {
        state.markAxesReset()
        onResetAxes(state.axesResetVersion)
    }
}
