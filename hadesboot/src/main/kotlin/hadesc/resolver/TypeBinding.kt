package hadesc.resolver

import hadesc.ast.Binder
import hadesc.ast.Declaration
import hadesc.ast.InterfaceRef

sealed class TypeBinding {
    data class Struct(
            val declaration: Declaration.Struct
    ) : TypeBinding()

    data class TypeParam(val binder: Binder, val bound: InterfaceRef?) : TypeBinding()

    data class Enum(
            val declaration: Declaration.Enum
    ) : TypeBinding()
}