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
        val sampleCount = if (shape.isEmpty()) 0 else shape.fold(1) { acc, dim -> acc * dim }.coerceAtLeast(0)
        return MirurVariableSnapshot(
            name = name,
            signature = signature,
            value = value,
            elementType = inferElementType(value),
            shape = shape,
            sampleCount = sampleCount,
        )
    }

    private fun inferElementType(value: Any?): String {
        val arrayClass = value?.javaClass ?: return "unknown"
        val component = deepestComponentType(arrayClass)
        val normalized = component.name.lowercase()
        return when {
            normalized == "double" || normalized == "java.lang.double" -> "double"
            normalized == "float" || normalized == "java.lang.float" -> "float"
            normalized == "long" || normalized == "java.lang.long" -> "long"
            normalized == "int" || normalized == "java.lang.integer" -> "int"
            normalized == "short" || normalized == "java.lang.short" -> "short"
            normalized == "byte" || normalized == "java.lang.byte" -> "byte"
            normalized == "boolean" || normalized == "java.lang.boolean" -> "boolean"
            normalized == "char" || normalized == "java.lang.character" -> "char"
            else -> component.simpleName.ifBlank { "unknown" }
        }
    }

    private fun deepestComponentType(clazz: Class<*>): Class<*> {
        var current = clazz
        while (current.isArray) {
            current = current.componentType
        }
        return current
    }

    private fun inferShape(value: Any?): List<Int> {
        val maxLengths = mutableListOf<Int>()

        fun walk(node: Any?, depth: Int) {
            if (node == null || !node.javaClass.isArray) return
            val length = java.lang.reflect.Array.getLength(node)
            while (maxLengths.size <= depth) {
                maxLengths.add(0)
            }
            if (length > maxLengths[depth]) {
                maxLengths[depth] = length
            }

            for (i in 0 until length) {
                walk(java.lang.reflect.Array.get(node, i), depth + 1)
            }
        }

        walk(value, 0)
        return maxLengths
    }
}
