package hadesc.analysis

import hadesc.Name
import hadesc.assertions.requireUnreachable
import hadesc.ast.*
import hadesc.context.Context
import hadesc.diagnostics.Diagnostic
import hadesc.exhaustive
import hadesc.frontend.PropertyBinding
import hadesc.hir.BinaryOperator
import hadesc.hir.TypeTransformer
import hadesc.hir.TypeVisitor
import hadesc.location.HasLocation
import hadesc.location.SourceLocation
import hadesc.resolver.Binding
import hadesc.resolver.TypeBinding
import hadesc.types.Substitution
import hadesc.types.Type
import hadesc.types.emptySubstitution
import hadesc.types.toSubstitution
import hadesc.unit
import java.util.*
import java.util.Collections.singletonList
import kotlin.math.exp
import kotlin.math.min

class Analyzer(
        private val ctx: Context
) {
    fun annotationToType(annotation: TypeAnnotation): Type = when (annotation) {
        is TypeAnnotation.Application -> TODO()
        is TypeAnnotation.Error -> TODO()
        is TypeAnnotation.Function -> TODO()
        is TypeAnnotation.MutPtr -> Type.Ptr(annotationToType(annotation.to), isMutable = true)
        is TypeAnnotation.Ptr -> Type.Ptr(annotationToType(annotation.to), isMutable = false)
        is TypeAnnotation.Qualified -> TODO()
        is TypeAnnotation.Select -> TODO()
        is TypeAnnotation.Union -> TODO()
        is TypeAnnotation.Var -> {
            when (val binding = ctx.resolver.resolveTypeVariable(annotation.name)) {
                is TypeBinding.AssociatedType -> TODO()
                is TypeBinding.Builtin -> binding.type
                is TypeBinding.SealedType -> TODO()
                is TypeBinding.Struct -> TODO()
                is TypeBinding.Trait -> TODO()
                is TypeBinding.TypeAlias -> TODO()
                is TypeBinding.TypeParam -> TODO()
                null -> {
                    ctx.diagnosticReporter.report(annotation.location, Diagnostic.Kind.UnboundType(annotation.name.name))
                    Type.Error(annotation.location)
                }
            }
        }
    }

    private val typeArgsCache = MutableNodeMap<Expression, List<Type>>()
    fun getTypeArgs(expression: Expression): List<Type>? {
        return typeArgsCache[expression]
    }

    fun getSealedTypeConstructorBinding(expression: Expression): PropertyBinding.SealedTypeCaseConstructor? = null
    fun getClosureCaptures(expression: Expression.Closure): ClosureCaptures {
        TODO()
    }

    fun getSealedTypePayloadType(declaration: Declaration.SealedType): Type {
        TODO()
    }

    fun getParamType(param: Param): Type {
        TODO()
    }

    fun getSealedTypeDeclaration(discriminantType: Type): Declaration.SealedType {
        TODO()
    }

    fun resolvePropertyBinding(expr: Expression.Property): PropertyBinding {
        TODO()
    }

    fun typeOfBinder(binder: Binder): Type {
        TODO()
    }

    fun getCallReceiver(expression: Expression.Call): Expression? {
        return null
    }

    private val typeOfExpressionCache = MutableNodeMap<Expression, Type>()
    fun typeOfExpression(expression: Expression): Type {
        return checkNotNull(typeOfExpressionCache[expression]) {
            "Type not inferred for expression at location ${expression.location}"
        }
    }

    fun assignExpressionTypes(expressionTypes: List<Pair<Expression, Type>>) {
        for ((expression, type) in expressionTypes) {
            check(typeOfExpressionCache[expression] == null)
            typeOfExpressionCache[expression] = type
        }
    }
}

data class ClosureCaptures(
    val values: Map<Binder, Type>,
    val types: Set<Binder>
)

typealias op = BinaryOperator

val BIN_OP_RULES: Map<Pair<op, Type>, Pair<Type, Type>> = mapOf(

        (op.AND to Type.Bool) to (Type.Bool to Type.Bool),
        (op.OR to Type.Bool) to (Type.Bool to Type.Bool),


        (op.EQUALS to Type.Bool) to (Type.Bool to Type.Bool),
        (op.NOT_EQUALS to Type.Bool) to (Type.Bool to Type.Bool),
)

data class Discriminant(
    val index: Int,
    val name: Name,
    val params: List<Pair<Name, Type>>
)
