package io.mirur.intellij

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPanel
import com.intellij.ui.components.JBScrollPane
import java.awt.FlowLayout
import java.awt.BorderLayout
import java.awt.FlowLayout

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

        val connection = project.messageBus.connect(toolWindow.disposable)
        connection.subscribe(
            MirurSubmissionBus.TOPIC,
            object : MirurSubmissionBus.Listener {
                override fun onSubmission(snapshot: MirurVariableSnapshot) {
                    controller.onSubmission(snapshot)
                }
            },
        )

        controller.consumePendingSubmissions()

        panel.add(JBLabel("Mirur is ready. Debugger integration will be added in Phase 4."), BorderLayout.NORTH)

        val footer = JBPanel<JBPanel<*>>(FlowLayout(FlowLayout.LEFT, 12, 0))
        val badgeLabel = JBLabel("Dataset: --")
        val minMaxLabel = JBLabel("Min/Max: --")
        val samplingLabel = JBLabel("Sampling: --")
        val eventLabel = JBLabel("State: idle")
        footer.add(badgeLabel)
        footer.add(minMaxLabel)
        footer.add(samplingLabel)
        footer.add(eventLabel)
        panel.add(footer, BorderLayout.SOUTH)

        val controller = MirurDatasetFooterController(project) { state ->
            val dataset = state.dataset
            if (dataset == null) {
                badgeLabel.text = "Dataset: --"
                minMaxLabel.text = "Min/Max: --"
                samplingLabel.text = "Sampling: --"
                eventLabel.text = "State: idle"
            } else {
                badgeLabel.text = "Dataset: ${MirurDatasetFormatting.datasetBadge(dataset)}"
                minMaxLabel.text = "Min/Max: ${MirurDatasetFormatting.minMax(dataset)}"
                samplingLabel.text = "Sampling: ${MirurDatasetFormatting.sampling(dataset)}"
                eventLabel.text = "State: ${state.event.name.lowercase()}"
            }
        }
        controller.start()
        Disposer.register(toolWindow.disposable, controller)

        val content = contentManager.factory.createContent(panel, "Views", false)
        contentManager.addContent(content)
    }
}
