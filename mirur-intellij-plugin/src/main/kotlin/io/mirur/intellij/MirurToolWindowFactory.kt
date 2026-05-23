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

class MirurToolWindowFactory : ToolWindowFactory, DumbAware {
    override fun shouldBeAvailable(project: Project): Boolean = true

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val contentManager = toolWindow.contentManager
        val panel = JBPanel<JBPanel<*>>(BorderLayout())
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
