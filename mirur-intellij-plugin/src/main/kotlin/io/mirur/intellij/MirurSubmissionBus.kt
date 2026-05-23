package io.mirur.intellij

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.util.messages.Topic
import java.util.concurrent.ConcurrentLinkedQueue

@Service(Service.Level.PROJECT)
class MirurSubmissionBus(private val project: Project) {
    interface Listener {
        fun onSubmission(snapshot: MirurVariableSnapshot)
    }

    private val queue = ConcurrentLinkedQueue<MirurVariableSnapshot>()

    fun submit(snapshot: MirurVariableSnapshot) {
        queue.add(snapshot)
        project.messageBus.syncPublisher(TOPIC).onSubmission(snapshot)
    }

    fun drain(): List<MirurVariableSnapshot> {
        val out = mutableListOf<MirurVariableSnapshot>()
        while (true) {
            val item = queue.poll() ?: break
            out.add(item)
        }
        return out
    }

    companion object {
        @JvmField
        val TOPIC: Topic<Listener> = Topic.create("Mirur submission", Listener::class.java)

        fun getInstance(project: Project): MirurSubmissionBus = project.getService(MirurSubmissionBus::class.java)
    }
}
