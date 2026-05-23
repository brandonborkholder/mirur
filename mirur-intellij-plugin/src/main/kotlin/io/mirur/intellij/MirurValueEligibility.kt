package io.mirur.intellij

/**
 * Lightweight, string-based type classifier for v0 debugger support.
 */
object MirurValueEligibility {
    private val primitiveNumericArrayRegex = Regex("""^(?:byte|short|int|long|float|double)\s*\[\s*]$""")
    private val boxedNumericArrayRegex = Regex(
        """^(?:java\.lang\.)?(?:Byte|Short|Integer|Long|Float|Double)\s*\[\s*]$"""
    )

    fun isEligible(typeText: String?): Boolean {
        if (typeText.isNullOrBlank()) return false
        val normalized = typeText.replace("? extends ", "").trim()
        return isPrimitiveNumericArray(normalized)
            || isBoxedNumericArray(normalized)
            || isListOfNumber(normalized)
    }

    fun isPrimitiveNumericArray(typeText: String): Boolean = primitiveNumericArrayRegex.matches(typeText)

    fun isBoxedNumericArray(typeText: String): Boolean = boxedNumericArrayRegex.matches(typeText)

    fun isListOfNumber(typeText: String): Boolean {
        val collapsed = typeText.replace("\\s+".toRegex(), "")
        return collapsed == "java.util.List<java.lang.Number>" || collapsed == "List<Number>"
    }
}
