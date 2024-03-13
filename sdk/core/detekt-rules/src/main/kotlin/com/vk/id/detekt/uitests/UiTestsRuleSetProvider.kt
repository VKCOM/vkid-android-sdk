package com.vk.id.detekt.uitests

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider

class UiTestsRuleSetProvider : RuleSetProvider {
    override val ruleSetId = "ui-tests"

    override fun instance(config: Config): RuleSet {
        return RuleSet(
            ruleSetId,
            listOf(
                TestClassNamingRule(config),
                TestMethodNamingRule(config),
                RestrictedKeywordRule(config),
            )
        )
    }
}