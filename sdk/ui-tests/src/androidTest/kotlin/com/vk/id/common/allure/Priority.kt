package com.vk.id.common.allure

import io.qameta.allure.kotlin.LabelAnnotation
import java.lang.annotation.Inherited

@MustBeDocumented
@Inherited
@Repeatable
@Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.CLASS
)
@LabelAnnotation(name = "Priority")
internal annotation class Priority(val value: String) {
    companion object {
        const val CRITICAL = "Critical"
    }
}
