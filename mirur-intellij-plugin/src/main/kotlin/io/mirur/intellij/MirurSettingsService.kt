package io.mirur.intellij

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@Service(Service.Level.APP)
@State(name = "MirurSettings", storages = [Storage("mirur.xml")])
class MirurSettingsService : PersistentStateComponent<MirurSettingsService.State> {
    data class State(
        var autoOpenToolWindow: Boolean = true,
        var maxPreviewElements: Int = 1024,
    )

    private var state = State()

    override fun getState(): State = state

    override fun loadState(state: State) {
        this.state = state
    }
}
