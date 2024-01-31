@file:Suppress("MatchingDeclarationName")

package com.vk.id.sample.app.util.carrying

import kotlin.reflect.KCallable
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.KTypeParameter
import kotlin.reflect.KVisibility

internal abstract class CarryingCallable<T>(source: KCallable<T>) : KCallable<T> {
    override val annotations: List<Annotation> = source.annotations
    override val isAbstract: Boolean = source.isAbstract
    override val isFinal: Boolean = source.isAbstract
    override val isOpen: Boolean = source.isOpen
    override val isSuspend: Boolean = source.isSuspend
    override val name: String = source.name
    override val returnType: KType = source.returnType
    override val typeParameters: List<KTypeParameter> = source.typeParameters
    override val visibility: KVisibility? = source.visibility
}

internal fun <T, U> KCallable<T>.carry(arg1: U): KCallable<T> = object : CarryingCallable<T>(this) {
    private fun tmp(@Suppress("UNUSED_PARAMETER") arg1: U) = Unit
    private val tmpReference: KFunction<Unit> = ::tmp
    override val parameters: List<KParameter> = tmpReference.parameters + this@carry.parameters
    override fun call(vararg args: Any?) = this@carry.call(arg1, *args)
    override fun callBy(args: Map<KParameter, Any?>): T = error("Unsupported")
}

internal fun <T, U, V> KCallable<T>.carry(arg1: U, arg2: V): KCallable<T> = object : CarryingCallable<T>(this) {
    private fun tmp(@Suppress("UNUSED_PARAMETER") arg1: U, @Suppress("UNUSED_PARAMETER") arg2: V) = Unit
    private val tmpReference: KFunction<Unit> = ::tmp
    override val parameters: List<KParameter> = tmpReference.parameters + this@carry.parameters
    override fun call(vararg args: Any?) = this@carry.call(arg1, arg2, *args)
    override fun callBy(args: Map<KParameter, Any?>): T = error("Unsupported")
}
