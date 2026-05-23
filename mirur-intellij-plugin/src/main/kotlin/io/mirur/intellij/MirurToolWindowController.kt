package io.mirur.intellij

import com.intellij.xdebugger.XDebuggerManager
import com.intellij.xdebugger.evaluation.XDebuggerEvaluator
import com.intellij.xdebugger.frame.XValue

class MirurToolWindowController(
    private val submissionBus: MirurSubmissionBus,
    private val render: (MirurVariableSnapshot) -> Unit,
    private val status: (String) -> Unit,
) {
    private var currentSnapshot: MirurVariableSnapshot? = null
    private var currentVariableIdentity: String? = null
    private var pinned: Boolean = false

    fun onSubmission(snapshot: MirurVariableSnapshot) {
        if (pinned) {
            status("Pinned: ignoring incoming selection changes.")
            return
        }
        applySnapshot(snapshot)
    }

    fun consumePendingSubmissions() {
        val latest = submissionBus.drain().lastOrNull() ?: return
        onSubmission(latest)
    }

    fun refreshFromActiveDebugSession(project: com.intellij.openapi.project.Project) {
        val variableName = currentSnapshot?.name
        if (variableName.isNullOrBlank()) {
            status("Refresh unavailable: no variable selected yet.")
            return
        }

        val session = XDebuggerManager.getInstance(project).currentSession
        if (session == null) {
            status("Refresh unavailable: debug session is not active.")
            return
        }

        val refreshed = MirurVariableSnapshot(
            name = variableName,
            signature = currentSnapshot?.signature ?: "debug-expression",
            value = currentSnapshot?.value,
        )
        val evaluator = session.currentStackFrame?.evaluator
        if (evaluator == null) {
            applySnapshot(refreshed)
            status("Refreshed '$variableName' using latest known value (no evaluator available).")
            return
        }

        evaluator.evaluate(variableName, object : XDebuggerEvaluator.XEvaluationCallback {
            override fun evaluated(result: XValue) {
                val valuePresentation = result.javaClass.simpleName
                applySnapshot(
                    MirurVariableSnapshot(
                        name = variableName,
                        signature = result.javaClass.simpleName,
                        value = valuePresentation,
                    ),
                )
                status("Refreshed '$variableName' from active debug session.")
            }

            override fun errorOccurred(errorMessage: String) {
                applySnapshot(refreshed)
                status("Refresh failed for '$variableName': $errorMessage")
            }
        }, null)
    }

    fun setPinned(enabled: Boolean) {
        pinned = enabled
        status(if (enabled) "Mirur view pinned." else "Mirur view unpinned.")
    }

    fun isPinned(): Boolean = pinned

    fun getCurrentSnapshot(): MirurVariableSnapshot? = currentSnapshot

    fun getCurrentVariableIdentity(): String? = currentVariableIdentity

    private fun applySnapshot(snapshot: MirurVariableSnapshot) {
        currentSnapshot = snapshot
        currentVariableIdentity = snapshot.identity()
        render(snapshot)
    }

    private fun MirurVariableSnapshot.identity(): String = "$name::$signature"
}
