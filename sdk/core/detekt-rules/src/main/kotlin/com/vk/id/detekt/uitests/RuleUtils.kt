package com.vk.id.detekt.uitests

import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtElement
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.psiUtil.getNonStrictParentOfType
import org.jetbrains.kotlin.psi.psiUtil.getSuperNames

private const val TEST_ANNOTATION = "Test"
private const val SCENARIO_BASE_CLASS = "BaseScenario"
private val screenBaseClasses = listOf("KScreen", "UiScreen")

fun KtElement.inTestMethod(): Boolean = getNonStrictParentOfType<KtNamedFunction>()?.isTestMethod() ?: false

fun KtNamedFunction.isTestMethod(): Boolean = annotationEntries.any { it.shortName?.asString() == TEST_ANNOTATION }

fun KtClass.isTestClass(): Boolean = (body?.functions.orEmpty()).any(KtNamedFunction::isTestMethod)

fun KtElement.isOrInScreenObject(): Boolean {
    return getNonStrictParentOfType<KtClassOrObject>()
        ?.getSuperNames()
        .orEmpty().any { it in screenBaseClasses }
}

fun KtElement.inScenario(): Boolean {
    return getNonStrictParentOfType<KtClassOrObject>()
        ?.getSuperNames()
        .orEmpty().any { it == SCENARIO_BASE_CLASS }
}