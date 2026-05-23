package io.mirur.intellij

import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.actionSystem.impl.ActionToolbarImpl
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBPanel
import io.mirur.intellij.actions.MirurModeComboBoxAction
import io.mirur.intellij.actions.MirurPinToggleAction
import io.mirur.intellij.actions.MirurRefreshAction
import io.mirur.intellij.actions.MirurResetAxesAction
import io.mirur.intellij.actions.MirurSettingsAction
import io.mirur.intellij.actions.MirurViewState
import java.awt.BorderLayout

class MirurToolWindowFactory : ToolWindowFactory, DumbAware {
    override fun shouldBeAvailable(project: Project): Boolean = true

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val contentManager = toolWindow.contentManager
        val panel = JBPanel<JBPanel<*>>(BorderLayout())
        val viewState = MirurViewState()

        val actions = DefaultActionGroup(
            MirurModeComboBoxAction(viewState) {
                // TODO: wire into chart rendering controller.
            },
            MirurPinToggleAction(viewState) {
                // TODO: wire pin behavior into controller.
            },
            MirurRefreshAction {
                // TODO: wire refresh into controller.
            },
            MirurResetAxesAction(viewState) {
                // TODO: wire axis reset into controller.
            },
            MirurSettingsAction(),
        )

        val toolbar = ActionToolbarImpl("MirurToolWindowToolbar", actions, true)
        toolbar.targetComponent = panel
        panel.add(toolbar.component, BorderLayout.NORTH)

        val content = contentManager.factory.createContent(panel, "Views", false)
        contentManager.addContent(content)
    }
}
