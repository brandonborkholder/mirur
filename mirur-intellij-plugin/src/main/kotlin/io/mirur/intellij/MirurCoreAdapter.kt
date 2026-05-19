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
        return MirurVariableSnapshot(name = name, signature = signature, value = value)
    }
}
