package io.mirur.intellij

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPanel
import java.awt.BorderLayout
import java.awt.FlowLayout
import javax.swing.JButton
import javax.swing.JToggleButton

class MirurToolWindowFactory : ToolWindowFactory, DumbAware {
    override fun shouldBeAvailable(project: Project): Boolean = true

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val contentManager = toolWindow.contentManager
        val panel = JBPanel<JBPanel<*>>(BorderLayout())

        val controls = JBPanel<JBPanel<*>>(FlowLayout(FlowLayout.LEFT, 8, 0))
        val refreshButton = JButton("Refresh")
        val pinButton = JToggleButton("Pin")
        controls.add(refreshButton)
        controls.add(pinButton)
        panel.add(controls, BorderLayout.NORTH)

        val footer = JBPanel<JBPanel<*>>(FlowLayout(FlowLayout.LEFT, 12, 0))
        val variableLabel = JBLabel("Variable: --")
        val shapeLabel = JBLabel("Shape: --")
        val dtypeLabel = JBLabel("DType: --")
        val eventLabel = JBLabel("State: idle")
        footer.add(variableLabel)
        footer.add(shapeLabel)
        footer.add(dtypeLabel)
        footer.add(eventLabel)
        panel.add(footer, BorderLayout.SOUTH)

        val controller = MirurToolWindowController(project) { snapshot ->
            if (snapshot == null) {
                variableLabel.text = "Variable: --"
                shapeLabel.text = "Shape: --"
                dtypeLabel.text = "DType: --"
                eventLabel.text = "State: idle"
            } else {
                variableLabel.text = "Variable: ${snapshot.variableName}"
                shapeLabel.text = "Shape: ${snapshot.shape.joinToString(prefix = "[", postfix = "]")}"
                dtypeLabel.text = "DType: ${snapshot.dataType}"
                eventLabel.text = if (pinButton.isSelected) "State: pinned" else "State: live"
            }
        }

        refreshButton.addActionListener { controller.refreshFromActiveDebugSession() }
        pinButton.addActionListener {
            controller.setPinned(pinButton.isSelected)
            eventLabel.text = if (controller.isPinned()) "State: pinned" else "State: live"
        }

        val submissionBus = MirurSubmissionBus.getInstance(project)
        controller.consumePendingSubmissions(submissionBus.drain())

        val connection = project.messageBus.connect(toolWindow.disposable)
        connection.subscribe(MirurSubmissionBus.TOPIC, MirurSubmissionBus.Listener { snapshot ->
            controller.onSubmission(snapshot)
        })

        Disposer.register(toolWindow.disposable) {
            if (!connection.isDisposed) {
                connection.dispose()
            }
        }

        val content = contentManager.factory.createContent(panel, "Views", false)
        contentManager.addContent(content)
    }
}
