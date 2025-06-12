package com.vk.id.common

/**
 * This annotation marks APIs that you should not use under any circumstances.
 * @since 0.0.2-alpha
 */
@RequiresOptIn(
    level = RequiresOptIn.Level.ERROR,
    message = "This is internal VK ID api, do not use it in your code"
)
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.FIELD,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.TYPEALIAS
)
public annotation class InternalVKIDApi
