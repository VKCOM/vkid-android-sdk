package com.vk.id.dokka_skip

import org.jetbrains.dokka.base.transformers.documentables.SuppressedByConditionDocumentableFilterTransformer
import org.jetbrains.dokka.model.Annotations
import org.jetbrains.dokka.model.Documentable
import org.jetbrains.dokka.model.properties.WithExtraProperties
import org.jetbrains.dokka.plugability.DokkaContext

internal class HideInternalApiTransformer(
    context: DokkaContext
) : SuppressedByConditionDocumentableFilterTransformer(context) {

    override fun shouldBeSuppressed(d: Documentable): Boolean {

        val annotations: List<Annotations.Annotation> =
            (d as? WithExtraProperties<*>)
                ?.extra
                ?.allOfType<Annotations>()
                ?.flatMap { it.directAnnotations.values.flatten() }
                ?: emptyList()

        return annotations.any { isInternalAnnotation(it) }
    }

    private fun isInternalAnnotation(annotation: Annotations.Annotation): Boolean {
        return annotation.dri.packageName == "com.vk.id.commn"
            && annotation.dri.classNames == "InternalVKIDApi"
    }
}