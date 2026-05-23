package io.mirur.intellij

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.util.messages.Topic
import java.util.concurrent.atomic.AtomicReference

@Service(Service.Level.PROJECT)
class MirurSubmissionBus(private val project: Project) {
    interface Listener {
        fun onSubmission(snapshot: MirurVariableSnapshot)
    }

    private val latestSnapshot = AtomicReference<MirurVariableSnapshot?>()

    fun submit(snapshot: MirurVariableSnapshot) {
        latestSnapshot.set(snapshot)
        project.messageBus.syncPublisher(TOPIC).onSubmission(snapshot)
    }

    fun drain(): List<MirurVariableSnapshot> {
        return latestSnapshot.getAndSet(null)?.let(::listOf) ?: emptyList()
    }

    companion object {
        @JvmField
        val TOPIC: Topic<Listener> = Topic.create("Mirur submission", Listener::class.java)

        fun getInstance(project: Project): MirurSubmissionBus = project.getService(MirurSubmissionBus::class.java)
    }
}
