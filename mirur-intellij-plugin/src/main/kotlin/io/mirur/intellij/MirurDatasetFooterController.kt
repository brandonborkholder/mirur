package io.mirur.intellij

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.util.Alarm
import com.intellij.util.ui.UIUtil
import java.util.concurrent.atomic.AtomicReference

class MirurDatasetFooterController(
    private val project: Project,
    private val onStateChanged: (MirurDatasetUiState) -> Unit,
) : Disposable {
    private val bus = MirurSubmissionBus.getInstance(project)
    private val alarm = Alarm(Alarm.ThreadToUse.POOLED_THREAD, this)
    private val stateRef = AtomicReference(MirurDatasetUiState.empty())

    fun start() {
        pollBus()
    }

    private fun pollBus() {
        val drained = bus.drain()
        if (drained.isNotEmpty()) {
            val latest = drained.last()
            val dataset = MirurDatasetFormatting.inferMetadata(latest)
            val previous = stateRef.get().dataset
            val event = when {
                dataset.isPinned -> MirurDatasetEvent.Pinned
                previous != null && previous.name == dataset.name -> MirurDatasetEvent.Refreshed
                else -> MirurDatasetEvent.Loaded
            }
            val next = MirurDatasetUiState(dataset = dataset, event = event)
            stateRef.set(next)
            UIUtil.invokeLaterIfNeeded { onStateChanged(next) }
        }
        if (!alarm.isDisposed) {
            alarm.addRequest({ pollBus() }, 250)
        }
    }

    override fun dispose() {
        alarm.cancelAllRequests()
    }
}

data class MirurDatasetUiState(
    val dataset: MirurDatasetMetadata?,
    val event: MirurDatasetEvent,
) {
    companion object {
        fun empty(): MirurDatasetUiState = MirurDatasetUiState(dataset = null, event = MirurDatasetEvent.None)
    }
}

enum class MirurDatasetEvent {
    None,
    Loaded,
    Refreshed,
    Pinned,
}
