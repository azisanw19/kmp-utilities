package id.feinn.utility.context

/**
 * A platform-specific implementation of the abstract class `FeinnLocalContext`.
 *
 * This class represents the actual implementation of the `FeinnLocalContext` abstraction for the
 * current platform. It serves as the base for any context-related functionality needed in the
 * shared or platform-specific code.
 */
public actual abstract class FeinnLocalContext

/**
 * A concrete implementation of `FeinnLocalContext`.
 *
 * `FeinnLocalContextImpl` provides a singleton instance of the context, which can be used
 * throughout the application. The singleton is accessed through the `instance` property
 * in the `companion object`.
 */
internal class FeinnLocalContextImpl : FeinnLocalContext() {
    companion object {
        /**
         * A singleton instance of `FeinnLocalContextImpl`.
         *
         * This ensures that only one instance of the context implementation is created and shared
         * across the application.
         */
        val instance = FeinnLocalContextImpl()
    }
}
