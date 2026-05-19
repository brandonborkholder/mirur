package io.mirur.intellij

/**
 * IntelliJ-side normalized variable model for bridging into mirur-core VariableObject later.
 */
data class MirurVariableSnapshot(
    val name: String,
    val signature: String,
    val value: Any?,
)
