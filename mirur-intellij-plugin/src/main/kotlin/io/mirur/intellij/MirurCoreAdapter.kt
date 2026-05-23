package io.mirur.intellij

/**
 * Phase 4 adapter boundary for mapping IntelliJ debug values into shared Mirur model objects.
 *
 * In a follow-up, this adapter should return `mirur.core.VariableObject` once the Gradle IntelliJ
 * module consumes `mirur-core` artifacts directly.
 */
object MirurCoreAdapter {
    fun toSnapshot(name: String, value: Any?): MirurVariableSnapshot {
        val signature = value?.javaClass?.simpleName ?: "null"
        val shape = inferShape(value)
        val sampleCount = shape.fold(1) { acc, dim -> acc * dim }.coerceAtLeast(0)
        return MirurVariableSnapshot(
            name = name,
            signature = signature,
            value = value,
            elementType = inferElementType(signature),
            shape = shape,
            sampleCount = sampleCount,
        )
    }

    private fun inferElementType(signature: String): String {
        val normalized = signature.lowercase()
        return when {
            normalized.contains("float") -> "float"
            normalized.contains("int") -> "int"
            normalized.contains("long") -> "long"
            else -> "double"
        }
    }

    private fun inferShape(value: Any?): List<Int> {
        val lengths = mutableListOf<Int>()
        var current: Any? = value
        while (current != null && current.javaClass.isArray) {
            val length = java.lang.reflect.Array.getLength(current)
            lengths.add(length)
            current = if (length > 0) java.lang.reflect.Array.get(current, 0) else null
        }
        return lengths
    }
}
