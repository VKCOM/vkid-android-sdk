package com.vk.id.dokka_since_validator

import org.jetbrains.dokka.base.transformers.documentables.SuppressedByConditionDocumentableFilterTransformer
import org.jetbrains.dokka.model.Annotations
import org.jetbrains.dokka.model.DAnnotation
import org.jetbrains.dokka.model.DClass
import org.jetbrains.dokka.model.DFunction
import org.jetbrains.dokka.model.DProperty
import org.jetbrains.dokka.model.Documentable
import org.jetbrains.dokka.model.KotlinVisibility
import org.jetbrains.dokka.model.WithSources
import org.jetbrains.dokka.model.doc.CustomDocTag
import org.jetbrains.dokka.model.doc.Since
import org.jetbrains.dokka.model.doc.Text
import org.jetbrains.dokka.model.properties.WithExtraProperties
import org.jetbrains.dokka.plugability.DokkaContext

internal class SincePresenceChecker(
    context: DokkaContext
) : SuppressedByConditionDocumentableFilterTransformer(context) {

    override fun shouldBeSuppressed(d: Documentable): Boolean {
        if (!d.hasInternalApiAnnotation()
            && d.isPublicApi()
            && !d.hasSinceAnnotation()
            && d.isDocumented()
            && !d.isGenerated()
        ) {
            val message = "@since is not present on ${d.dri}"
            println(message)
            error(message)
        }

        return false
    }

    private fun Documentable.isGenerated(): Boolean {
        return toString().contains("Returns the enum constant of this type with the specified name") ||
                toString().contains("Returns a representation of an immutable list of all enum entries, in the order they're declared") ||
                toString().contains("Returns an array containing the constants of this enum type")
    }

    private fun Documentable.isDocumented(): Boolean {
        return documentation.isNotEmpty()
    }

    private fun Documentable.hasInternalApiAnnotation(): Boolean {
        return annotations.any(::isInternalAnnotation)
    }

    private fun Documentable.hasSinceAnnotation(): Boolean {
        return documentation.values.any { it.children.any { child -> child is Since } }
    }

    private val Documentable.annotations: List<Annotations.Annotation>
        get() = (this as? WithExtraProperties<*>)
            ?.extra
            ?.allOfType<Annotations>()
            ?.flatMap { it.directAnnotations.values.flatten() }
            ?: emptyList()

    private fun isInternalAnnotation(annotation: Annotations.Annotation): Boolean {
        return annotation.dri.packageName == "com.vk.id.common"
                && annotation.dri.classNames == "InternalVKIDApi"
    }

    private fun Documentable.isPublicApi(): Boolean {
        return (this is DAnnotation || this is DFunction || this is DClass || this is DProperty) &&
                !(this is DFunction && this.isConstructor) &&
                (if (this is DFunction) this.visibility.any { it.value is KotlinVisibility.Public } else true) &&
                (if (this is DProperty) this.visibility.any { it.value is KotlinVisibility.Public } else true) &&
                !isPropertyGetterOrSetter() &&
                (if (this is DAnnotation) this.visibility.any { it.value is KotlinVisibility.Public } else true) &&
                (if (this is DClass) this.visibility.any { it.value is KotlinVisibility.Public } else true)
    }

    private fun Documentable.isPropertyGetterOrSetter() = if (this is DFunction) {
        this.dri.callable?.name?.let { it.contains("get-") || it.contains("set-") } ?: false
    } else {
        false
    }
}