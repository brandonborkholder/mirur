package io.mirur.intellij

import com.intellij.openapi.project.Project

class MirurToolWindowController(
    private val project: Project,
    private val render: (MirurVariableSnapshot?) -> Unit,
) {
    private var currentVariableIdentity: String? = null
    private var currentSnapshot: MirurVariableSnapshot? = null
    private var pinned = false

    fun onSubmission(snapshot: MirurVariableSnapshot) {
        val incomingIdentity = variableIdentity(snapshot)
        if (pinned && currentVariableIdentity != null && currentVariableIdentity != incomingIdentity) {
            return
        }

        currentVariableIdentity = incomingIdentity
        currentSnapshot = snapshot
        render(snapshot)
    }

    fun consumePendingSubmissions(submissions: List<MirurVariableSnapshot>) {
        submissions.forEach(::onSubmission)
    }

    fun refreshFromActiveDebugSession() {
        MirurDebuggerRefresher.refresh(project)?.let(::onSubmission)
    }

    fun setPinned(value: Boolean) {
        pinned = value
    }

    fun isPinned(): Boolean = pinned

    fun getCurrentVariableIdentity(): String? = currentVariableIdentity

    fun getCurrentSnapshot(): MirurVariableSnapshot? = currentSnapshot

    private fun variableIdentity(snapshot: MirurVariableSnapshot): String {
        return "${snapshot.variableName}@${snapshot.stackFrame}"
    }
}
