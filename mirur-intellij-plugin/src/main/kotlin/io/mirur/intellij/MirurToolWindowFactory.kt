package io.mirur.intellij

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.Disposable
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.ActionLink
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPanel
import com.intellij.ui.components.JBTextArea
import com.intellij.util.Alarm
import com.intellij.xdebugger.XDebugSessionListener
import com.intellij.xdebugger.XDebuggerManager
import java.awt.BorderLayout
import java.awt.CardLayout
import javax.swing.JButton
import javax.swing.JComponent

sealed interface MirurUiState {
    data object Empty : MirurUiState

    data class Unsupported(val reason: String, val link: String) : MirurUiState

    data object Disconnected : MirurUiState

    data class Ready(val data: MirurVariableSnapshot) : MirurUiState
}

class MirurToolWindowFactory : ToolWindowFactory, DumbAware {
    override fun shouldBeAvailable(project: Project): Boolean = true

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val contentManager = toolWindow.contentManager
        val panel = MirurToolWindowPanel(project)
        val content = contentManager.factory.createContent(panel, "Views", false)
        content.setDisposer(panel)
        contentManager.addContent(content)
    }
}

private class MirurToolWindowPanel(private val project: Project) : JBPanel<JBPanel<*>>(BorderLayout()), Disposable {
    private val cardLayout = CardLayout()
    private val cards = JBPanel<JBPanel<*>>(cardLayout)
    private val refreshButton = JButton("Refresh")
    private val emptyCard = createMessageCard("Select a numeric array in Debug Variables, then choose ‘Visualize with Mirur’.")
    private val disconnectedCard = createMessageCard("Debug session ended. Refresh unavailable.")
    private val unsupportedReason = JBLabel()
    private val supportedTypesLink = ActionLink("Supported types") {
        NotificationGroupManager.getInstance().getNotificationGroup("Mirur")
            .createNotification("Mirur", "Supported types docs: $supportedTypesUrl", NotificationType.INFORMATION)
            .notify(project)
    }
    private val unsupportedCard = createUnsupportedCard()
    private val readyData = JBTextArea()
    private val readyCard = createReadyCard()
    private val alarm = Alarm(Alarm.ThreadToUse.SWING_THREAD, this)
    private var supportedTypesUrl: String = "https://mirur.io/docs/intellij/supported-types"
    private var currentState: MirurUiState = MirurUiState.Empty

    init {
        add(buildToolbar(), BorderLayout.NORTH)
        add(cards, BorderLayout.CENTER)
        cards.add(emptyCard, MirurUiState.Empty::class.java.simpleName)
        cards.add(disconnectedCard, MirurUiState.Disconnected::class.java.simpleName)
        cards.add(unsupportedCard, MirurUiState.Unsupported::class.java.simpleName)
        cards.add(readyCard, MirurUiState.Ready::class.java.simpleName)

        wireDebugSessionEvents()
        scheduleQueuePoll()
        render(MirurUiState.Empty)
    }

    private fun buildToolbar(): JComponent {
        val toolbar = JBPanel<JBPanel<*>>(BorderLayout())
        refreshButton.addActionListener {
            scheduleQueuePoll()
        }
        toolbar.add(refreshButton, BorderLayout.WEST)
        return toolbar
    }

    private fun wireDebugSessionEvents() {
        XDebuggerManager.getInstance(project).currentSession?.addSessionListener(object : XDebugSessionListener {
            override fun sessionStopped() {
                render(MirurUiState.Disconnected)
            }
        })
    }

    private fun scheduleQueuePoll() {
        alarm.cancelAllRequests()
        alarm.addRequest({
            val latest = MirurSubmissionBus.getInstance(project).drain().lastOrNull()
            if (latest == null) {
                if (currentState !is MirurUiState.Disconnected) {
                    render(MirurUiState.Empty)
                }
                return@addRequest
            }
            if (latest.signature == "debug-expression") {
                render(MirurUiState.Ready(latest))
            } else {
                render(
                    MirurUiState.Unsupported(
                        reason = "Type '${latest.signature}' is not currently visualizable.",
                        link = supportedTypesUrl,
                    )
                )
            }
        }, 50)
    }

    private fun render(state: MirurUiState) {
        currentState = state
        when (state) {
            MirurUiState.Empty -> {
                cardLayout.show(cards, MirurUiState.Empty::class.java.simpleName)
                refreshButton.isEnabled = true
            }

            MirurUiState.Disconnected -> {
                cardLayout.show(cards, MirurUiState.Disconnected::class.java.simpleName)
                refreshButton.isEnabled = false
            }

            is MirurUiState.Unsupported -> {
                unsupportedReason.text = state.reason
                supportedTypesUrl = state.link
                cardLayout.show(cards, MirurUiState.Unsupported::class.java.simpleName)
                refreshButton.isEnabled = true
            }

            is MirurUiState.Ready -> {
                readyData.text = "Name: ${state.data.name}\nSignature: ${state.data.signature}\nValue: ${state.data.value}"
                cardLayout.show(cards, MirurUiState.Ready::class.java.simpleName)
                refreshButton.isEnabled = true
            }
        }
    }

    private fun createMessageCard(text: String): JComponent {
        val panel = JBPanel<JBPanel<*>>(BorderLayout())
        val label = JBLabel(text)
        panel.add(label, BorderLayout.NORTH)
        return panel
    }

    private fun createUnsupportedCard(): JComponent {
        val panel = JBPanel<JBPanel<*>>(BorderLayout())
        val content = JBPanel<JBPanel<*>>()
        content.layout = javax.swing.BoxLayout(content, javax.swing.BoxLayout.Y_AXIS)
        content.add(unsupportedReason)
        content.add(supportedTypesLink)
        panel.add(content, BorderLayout.NORTH)
        return panel
    }

    private fun createReadyCard(): JComponent {
        val panel = JBPanel<JBPanel<*>>(BorderLayout())
        readyData.isEditable = false
        readyData.lineWrap = true
        readyData.wrapStyleWord = true
        panel.add(readyData, BorderLayout.CENTER)
        return panel
    }

    override fun dispose() {
        Disposer.dispose(alarm)
    }
}
