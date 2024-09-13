package com.vk.id.util

import android.net.Uri
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should

/**
 * Analog of io.kotest.matchers.uri for Android Uri
 */

internal infix fun Uri.shouldHaveHost(host: String) = this should haveHost(host)
internal fun haveHost(host: String) = object : Matcher<Uri> {
    override fun test(value: Uri) = MatcherResult(
        value.host == host,
        { "Uri $value should have host $host but was ${value.host}" },
        { "Uri $value should not have host $host" }
    )
}

internal infix fun Uri.shouldHaveScheme(scheme: String) = this should haveScheme(scheme)
internal fun haveScheme(scheme: String) = object : Matcher<Uri> {
    override fun test(value: Uri) = MatcherResult(
        value.scheme == scheme,
        { "Uri $value should have scheme $scheme but was ${value.scheme}" },
        { "Uri $value should not have scheme $scheme" }
    )
}

internal fun Uri.shouldHaveParameter(key: String, value: String) = this should haveParameter(key, value)
internal fun haveParameter(key: String, keyValue: String) = object : Matcher<Uri> {
    override fun test(value: Uri) = MatcherResult(
        value.getQueryParameter(key) == keyValue,
        { "Uri $value should have query parameter $key" },
        { "Uri $value should not have query parameter $key" }
    )
}

internal infix fun Uri.shouldHaveParameter(key: String) = this should haveParameter(key)
internal fun haveParameter(key: String) = object : Matcher<Uri> {
    override fun test(value: Uri) = MatcherResult(
        value.getQueryParameter(key) != null,
        { "Uri $value should have query parameter $key" },
        { "Uri $value should not have query parameter $key" }
    )
}

internal infix fun Uri.shouldHavePath(path: String) = this should havePath(path)
internal fun havePath(path: String) = object : Matcher<Uri> {
    override fun test(value: Uri) = MatcherResult(
        value.path == path,
        { "Uri $value should have path $path but was ${value.path}" },
        { "Uri $value should not have path $path" }
    )
}

internal infix fun Uri.shouldHaveExactSetOfParameters(params: Set<String>) = this should haveParameters(params)
internal fun haveParameters(params: Set<String>) = object : Matcher<Uri> {
    override fun test(value: Uri) = MatcherResult(
        value.queryParameterNames == params,
        { "Uri $value should have $params but was ${value.queryParameterNames}" },
        { "Uri $value should not have other params than $params" }
    )
}
