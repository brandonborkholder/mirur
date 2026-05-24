package io.mirur.intellij

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.util.messages.Topic
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.ConcurrentLinkedQueue

@Service(Service.Level.PROJECT)
class MirurSubmissionBus(private val project: Project) {
    private val queue = ConcurrentLinkedQueue<MirurVariableSnapshot>()
    private val listeners = CopyOnWriteArrayList<(MirurVariableSnapshot) -> Unit>()

    fun submit(snapshot: MirurVariableSnapshot) {
        queue.add(snapshot)
        listeners.forEach { listener -> listener(snapshot) }
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

    fun addListener(listener: (MirurVariableSnapshot) -> Unit): () -> Unit {
        listeners.add(listener)
        return { listeners.remove(listener) }
    }

    fun interface Listener {
        fun onSubmission(snapshot: MirurVariableSnapshot)
    }

    companion object {
        val TOPIC: Topic<Listener> = Topic.create("Mirur submissions", Listener::class.java)

        fun getInstance(project: Project): MirurSubmissionBus = project.getService(MirurSubmissionBus::class.java)
    }
}
