package io.mirur.intellij.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ToggleAction

class MirurPinToggleAction(
    private val state: MirurViewState,
    private val onPinChanged: (Boolean) -> Unit,
) : ToggleAction("Pin") {
    override fun isSelected(e: AnActionEvent): Boolean = state.pinned

    override fun setSelected(e: AnActionEvent, value: Boolean) {
        state.pinned = value
        onPinChanged(value)
    }
}
