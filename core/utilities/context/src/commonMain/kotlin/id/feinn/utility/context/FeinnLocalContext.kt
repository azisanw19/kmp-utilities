package id.feinn.utility.context

/**
 * An expected abstract class representing a platform-specific context.
 *
 * `FeinnLocalContext` serves as a platform-agnostic abstraction for context handling in
 * shared code. Each platform must provide an actual implementation of this class to
 * define how context is represented and used in that environment (e.g., `Context` on Android).
 *
 * This abstraction enables shared code to work with context-related functionality
 * without depending on platform-specific details.
 */
public expect abstract class FeinnLocalContext
