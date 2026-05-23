package io.mirur.intellij

import java.text.DecimalFormat
import java.util.Locale

object MirurDatasetFormatting {
    private val minMaxFormat = DecimalFormat("0.######")

    fun datasetBadge(dataset: MirurDatasetMetadata): String =
        "${dataset.elementType}${formatShape(dataset.shape)}"

    fun minMax(dataset: MirurDatasetMetadata): String =
        "${formatNumber(dataset.min)} / ${formatNumber(dataset.max)}"

    fun sampling(dataset: MirurDatasetMetadata): String =
        when {
            dataset.isTruncated -> "Truncated"
            dataset.isDownsampled -> "Downsampled"
            else -> "Full"
        }

    private fun formatShape(shape: List<Int>): String =
        shape.joinToString(separator = "", prefix = "", postfix = "") { "[$it]" }

    private fun formatNumber(value: Double): String {
        if (value.isNaN()) return "NaN"
        if (value.isInfinite()) return if (value > 0) "∞" else "-∞"
        synchronized(minMaxFormat) {
            return minMaxFormat.format(value)
        }
    }

    fun inferMetadata(snapshot: MirurVariableSnapshot): MirurDatasetMetadata {
        val shape = snapshot.shape.ifEmpty { listOf(snapshot.sampleCount) }
        return MirurDatasetMetadata(
            name = snapshot.name,
            elementType = snapshot.elementType.lowercase(Locale.ROOT),
            shape = shape,
            min = snapshot.min,
            max = snapshot.max,
            isDownsampled = snapshot.isDownsampled,
            isTruncated = snapshot.isTruncated,
            isPinned = snapshot.isPinned,
        )
    }
}


data class MirurDatasetMetadata(
    val name: String,
    val elementType: String,
    val shape: List<Int>,
    val min: Double,
    val max: Double,
    val isDownsampled: Boolean,
    val isTruncated: Boolean,
    val isPinned: Boolean,
)
