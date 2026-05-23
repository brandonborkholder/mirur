package io.mirur.intellij

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPanel
import com.intellij.ui.components.JBScrollPane
import java.awt.FlowLayout
import java.awt.BorderLayout

class MirurToolWindowFactory : ToolWindowFactory, DumbAware {
    override fun shouldBeAvailable(project: Project): Boolean = true

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val contentManager = toolWindow.contentManager
        val panel = JBPanel<JBPanel<*>>(BorderLayout())

        val statusLabel = JBLabel("Select a variable and submit it to Mirur.")
        val snapshotLabel = JBLabel("No snapshot received.")

        val submissionBus = MirurSubmissionBus.getInstance(project)
        val controller = MirurToolWindowController(
            submissionBus = submissionBus,
            render = { snapshot ->
                snapshotLabel.text = "${snapshot.name} (${snapshot.signature}) = ${snapshot.value}"
            },
            status = { message -> statusLabel.text = message },
        )

        val toolbar = JBPanel<JBPanel<*>>(FlowLayout(FlowLayout.LEFT, 8, 4))
        val refreshButton = javax.swing.JButton("Refresh")
        refreshButton.addActionListener { controller.refreshFromActiveDebugSession(project) }
        val pinToggle = JBCheckBox("Pin")
        pinToggle.addActionListener { controller.setPinned(pinToggle.isSelected) }
        toolbar.add(refreshButton)
        toolbar.add(pinToggle)

        panel.add(toolbar, BorderLayout.NORTH)
        panel.add(JBScrollPane(snapshotLabel), BorderLayout.CENTER)
        panel.add(statusLabel, BorderLayout.SOUTH)

        controller.consumePendingSubmissions()

        val connection = project.messageBus.connect(toolWindow.disposable)
        connection.subscribe(
            MirurSubmissionBus.TOPIC,
            object : MirurSubmissionBus.Listener {
                override fun onSubmission(snapshot: MirurVariableSnapshot) {
                    controller.onSubmission(snapshot)
                }
            },
        )

        val content = contentManager.factory.createContent(panel, "Views", false)
        contentManager.addContent(content)
    }
}
