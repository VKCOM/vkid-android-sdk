package com.vk.id.detekt.uitests

import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.psi.KtNamedFunction

class TestMethodNamingRule(config: Config) : Rule(config) {

    override val issue = Issue(
        id = javaClass.simpleName,
        severity = Severity.Defect,
        description = "Test method name should not be long and contain unnecessary words",
        debt = Debt.FIVE_MINS
    )

    private val maxFullQualifierLength: Int by config(defaultValue = 100)
    private val unexpectedWords: List<String> by config(emptyList())

    override fun visitNamedFunction(function: KtNamedFunction) {
        super.visitNamedFunction(function)

        if (!function.isTestMethod()) return

        val actualShortName = function.name.toString()
        for (unexpectedWord in unexpectedWords) {
            if (actualShortName.lowercase().contains(unexpectedWord)) {
                report(
                    CodeSmell(
                        issue,
                        Entity.from(function),
                        "Test method name must not contain '$unexpectedWord'"
                    )
                )
            }
        }

        val isSingleWord = function.name?.none(Char::isUpperCase) ?: false
        if (isSingleWord) {
            report(
                CodeSmell(
                    issue,
                    Entity.from(function),
                    "Test method name should not consist of a single word"
                )
            )
            return
        }

        val actualFqName = function.fqName?.asString() ?: ""
        if (actualFqName.length > maxFullQualifierLength) {
            report(
                ThresholdedCodeSmell(
                    issue,
                    Entity.from(function),
                    Metric("SIZE", actualFqName.length, maxFullQualifierLength),
                    "Test method name length should not be long (full qualifier)"
                )
            )
        }
    }
}