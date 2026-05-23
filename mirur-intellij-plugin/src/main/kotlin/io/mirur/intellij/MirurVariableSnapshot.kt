package io.mirur.intellij

/**
 * IntelliJ-side normalized variable model for bridging into mirur-core VariableObject later.
 */
data class MirurVariableSnapshot(
    val name: String,
    val signature: String,
    val value: Any?,
    val elementType: String = "double",
    val shape: List<Int> = emptyList(),
    val min: Double = Double.NaN,
    val max: Double = Double.NaN,
    val sampleCount: Int = 0,
    val isDownsampled: Boolean = false,
    val isTruncated: Boolean = false,
    val isPinned: Boolean = false,
)
