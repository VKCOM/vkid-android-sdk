package com.vk.id.tracking.tracer

/**
 * An exception that is thrown when there was an error reporting [cause] to Tracer.
 * Originally the [cause] happened, than we tried to report it to tracer which failed, which means that two exception happened overall.
 */
internal class FailedTracerReportingException(cause: Throwable) : Exception("Failed to report cause to Tracer", cause)
