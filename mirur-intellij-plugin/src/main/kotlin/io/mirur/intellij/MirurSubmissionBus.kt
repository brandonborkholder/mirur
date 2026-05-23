package io.mirur.intellij

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.ConcurrentLinkedQueue

@Service(Service.Level.PROJECT)
class MirurSubmissionBus(private val project: Project) {
    private val queue = ConcurrentLinkedQueue<MirurVariableSnapshot>()
    private val listeners = CopyOnWriteArrayList<(MirurVariableSnapshot) -> Unit>()

    fun submit(snapshot: MirurVariableSnapshot) {
        queue.add(snapshot)
        listeners.forEach { listener -> listener(snapshot) }
    }

    fun drain(): List<MirurVariableSnapshot> {
        return latestSnapshot.getAndSet(null)?.let(::listOf) ?: emptyList()
    }

    fun addListener(listener: (MirurVariableSnapshot) -> Unit): () -> Unit {
        listeners.add(listener)
        return { listeners.remove(listener) }
    }

    companion object {
        @JvmField
        val TOPIC: Topic<Listener> = Topic.create("Mirur submission", Listener::class.java)

        fun getInstance(project: Project): MirurSubmissionBus = project.getService(MirurSubmissionBus::class.java)
    }
}
