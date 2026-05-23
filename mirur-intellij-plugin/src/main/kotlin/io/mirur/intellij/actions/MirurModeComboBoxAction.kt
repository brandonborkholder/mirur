package io.mirur.intellij.actions

import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.actionSystem.ex.ComboBoxAction

class MirurModeComboBoxAction(
    private val state: MirurViewState,
    private val onModeSelected: (MirurViewMode) -> Unit,
) : ComboBoxAction() {

    override fun update(e: AnActionEvent) {
        super.update(e)
        e.presentation.text = state.selectedMode.displayName
    }

    override fun createPopupActionGroup(button: javax.swing.JComponent, dataContext: com.intellij.openapi.actionSystem.DataContext): ActionGroup {
        val group = DefaultActionGroup()
        MirurViewMode.entries.forEach { mode ->
            group.add(SelectModeAction(mode))
        }
        return group
    }

    private inner class SelectModeAction(private val mode: MirurViewMode) : AnAction(mode.displayName) {
        override fun actionPerformed(e: AnActionEvent) {
            state.selectedMode = mode
            onModeSelected(mode)
        }
    }
}
