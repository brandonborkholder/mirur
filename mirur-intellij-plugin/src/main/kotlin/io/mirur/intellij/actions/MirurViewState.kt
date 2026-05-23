package io.mirur.intellij.actions

/**
 * Shared view state for the Mirur tool window controls.
 */
class MirurViewState {
    var selectedMode: MirurViewMode = MirurViewMode.AUTO
    var pinned: Boolean = false
    var axesResetVersion: Long = 0
        private set

    fun markAxesReset() {
        axesResetVersion += 1
    }
}

enum class MirurViewMode(val displayName: String) {
    AUTO("Auto"),
    LINE("Line"),
    BAR("Bar"),
    HEATMAP("Heatmap"),
    HISTOGRAM("Histogram"),
}
