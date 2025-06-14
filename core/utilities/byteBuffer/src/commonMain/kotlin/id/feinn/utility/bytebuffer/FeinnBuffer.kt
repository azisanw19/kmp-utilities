package id.feinn.utility.bytebuffer

import kotlin.jvm.JvmStatic

/**
 * Abstract base class for buffer implementations that store elements of type T.
 *
 * Provides core buffer management functionality including position, limit, and capacity tracking,
 * as well as bounds checking and mark/reset support.
 *
 * @param T the type of elements stored in this buffer
 */
public abstract class FeinnBuffer<T> {

    internal companion object {
        /**
         * Creates an IllegalArgumentException for invalid capacity values.
         *
         * @param capacity the invalid capacity value
         * @return IllegalArgumentException with descriptive message
         */
        fun createCapacityException(capacity: Int): IllegalArgumentException {
            require(
                value = capacity < 0,
                lazyMessage = {
                    "capacity expected to be negative"
                }
            )

            return IllegalArgumentException("capacity < 0: ($capacity < 0)")
        }

        /**
         * Creates an IllegalArgumentException for invalid limit values.
         *
         * @param newLimit the proposed new limit value
         * @param capacity the buffer's current capacity
         * @return IllegalArgumentException with descriptive message
         */
        private fun createLimitException(newLimit: Int, capacity: Int): IllegalArgumentException {
            val msg: String = if (newLimit > capacity) {
                "newLimit > capacity: ($newLimit > $capacity)"
            } else { // assume negative
                require(newLimit < 0) { "newLimit expected to be negative" }
                "newLimit < 0: ($newLimit < 0)"
            }

            return IllegalArgumentException(msg)
        }

        /**
         * Creates an IllegalArgumentException for invalid position values.
         *
         * @param newPosition the proposed new position value
         * @param limit the buffer's current limit
         * @return IllegalArgumentException with descriptive message
         */
        private fun createPositionException(newPosition: Int, limit: Int): IllegalArgumentException {
            val msg: String = if (newPosition > limit) {
                "newPosition > limit: ($newPosition > $limit)"
            } else { // assume negative
                require(newPosition < 0) { "newPosition expected to be negative" }
                "newPosition < 0: ($newPosition < 0)"
            }

            return IllegalArgumentException(msg)
        }

        /**
         * Creates an exception for operations where the source buffer is the same as the target buffer.
         *
         * @return IllegalArgumentException with standard message
         */
        @JvmStatic
        protected fun createSameBufferException(): IllegalArgumentException {
            return IllegalArgumentException("The source buffer is this buffer")
        }
    }

    /**
     * The current mark position (-1 indicates no mark is set)
     */
    protected var mark: Int = -1

    /**
     * The current position index in the buffer.
     *
     * @throws IllegalArgumentException if position is set beyond limit or negative
     */
    public var position: Int = 0
        set(newPosition) {
            if ((newPosition > limit) or (newPosition < 0)) throw createPositionException(newPosition, limit)
            if (mark > newPosition) mark = -1
            field = newPosition
        }

    /**
     * The current limit index in the buffer.
     *
     * @throws IllegalArgumentException if limit is set beyond capacity or negative
     */
    public var limit: Int = 0
        set(newLimit) {
            if ((newLimit > capacity) or (newLimit < 0)) throw createLimitException(newLimit, capacity)
            field = newLimit
            if (position > newLimit) position = newLimit
            if (mark > newLimit) mark = -1
        }

    /**
     * The buffer's total capacity (maximum number of elements it can hold).
     * Protected setter ensures only subclasses can modify capacity.
     */
    public var capacity: Int = 0
        protected set

    /**
     * Primary constructor for buffer initialization.
     *
     * @param mark initial mark position (-1 for no mark)
     * @param pos initial position
     * @param lim initial limit
     * @param cap buffer capacity
     * @throws IllegalArgumentException if capacity is negative or mark is invalid
     */
    protected constructor(mark: Int, pos: Int, lim: Int, cap: Int) {
        if (cap < 0)
            throw createCapacityException(cap)
        this.capacity = cap
        limit = lim
        position = pos
        if (mark >= 0) {
            require(
                value = mark > pos,
                lazyMessage = {
                    "mark > position: ($mark > $pos)"
                }
            )

            this.mark = mark
        }

    }

    /**
     * Returns the backing array for this buffer.
     *
     * @return the array backing this buffer
     */
    public abstract fun array(): T

    /**
     * Increments and returns the current position for a get operation.
     *
     * @return the position before increment
     * @throws FeinnBufferUnderflowException if position would exceed limit
     */
    protected fun nextGetIndex() : Int {
        val p = position
        if (p >= limit) throw FeinnBufferUnderflowException()
        position = p + 1
        return p
    }

    /**
     * Increments position by nb and returns the original position for bulk get operations.
     *
     * @param nb number of elements to get
     * @return the original position
     * @throws FeinnBufferUnderflowException if remaining elements < nb
     */
    protected fun nextGetIndex(nb: Int): Int {
        val p = position
        if (limit - p < nb)
            throw FeinnBufferUnderflowException()
        position = p + nb
        return p
    }

    /**
     * Checks if index is within bounds (0 <= i < limit).
     *
     * @param i index to check
     * @return the valid index
     * @throws IndexOutOfBoundsException if index is invalid
     */
    protected fun checkIndex(i: Int) : Int {
        if ((i < 0) || (i >= limit)) throw IndexOutOfBoundsException()
        return i
    }

    /**
     * Resets this buffer's position to 0 and discards the mark.
     *
     * @return this buffer
     */
    public open fun rewind(): FeinnBuffer<T> {
        position = 0
        mark = -1
        return this
    }

    /**
     * Checks if index and following nb-1 elements are within bounds.
     *
     * @param i starting index
     * @param nb number of elements
     * @return the valid starting index
     * @throws IndexOutOfBoundsException if range would exceed bounds
     */
    protected fun checkIndex(i: Int, nb: Int): Int {               // package-private
        if ((i < 0) || (nb > limit - i)) throw IndexOutOfBoundsException()
        return i
    }

    /**
     * Increments and returns the current position for a put operation.
     *
     * @return the position before increment
     * @throws FeinnBufferOverflowException if position would exceed limit
     */
    protected fun nextPutIndex(): Int {
        val p = position
        if (p >= limit) throw FeinnBufferOverflowException()
        position = p + 1
        return p
    }

    /**
     * Increments position by nb and returns the original position for bulk put operations.
     *
     * @param nb number of elements to put
     * @return the original position
     * @throws FeinnBufferOverflowException if remaining space < nb
     */
    protected fun nextPutIndex(nb: Int): Int {
        val p = position
        if (limit - p < nb) throw FeinnBufferOverflowException()
        position = p + nb
        return p
    }

}