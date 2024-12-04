package id.feinn.utility.context

import android.content.Context

/**
 * A typealias for the platform-specific context used in the Android implementation.
 *
 * On Android, `FeinnLocalContext` is defined as an alias for `Context`, which represents
 * the Android `Context` object. This typealias simplifies shared code by abstracting
 * platform-specific context types.
 */
public actual typealias FeinnLocalContext = Context
