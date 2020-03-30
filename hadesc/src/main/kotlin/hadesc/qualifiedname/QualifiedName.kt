package hadesc.qualifiedname

import hadesc.Name

data class QualifiedName(
    private val names: List<Name> = listOf()
) {
    fun append(name: Name): QualifiedName {
        return QualifiedName(names + name)
    }

    fun mangle(): String = names.joinToString { it.text }

    val size get() = names.size
}
