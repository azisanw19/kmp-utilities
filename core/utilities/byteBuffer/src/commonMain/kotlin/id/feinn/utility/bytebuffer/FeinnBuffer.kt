package id.feinn.utility.bytebuffer

import kotlin.jvm.JvmStatic

public abstract class FeinnBuffer<T> {

    internal companion object {
        private fun createCapacityException(capacity: Int): IllegalArgumentException {
            require(
                value = capacity < 0,
                lazyMessage = {
                    "capacity expected to be negative"
                }
            )

            return IllegalArgumentException("capacity < 0: ($capacity < 0)")
        }

        private fun createLimitException(newLimit: Int, capacity: Int): IllegalArgumentException {
            val msg: String = if (newLimit > capacity) {
                "newLimit > capacity: ($newLimit > $capacity)"
            } else { // assume negative
                require(newLimit < 0) { "newLimit expected to be negative" }
                "newLimit < 0: ($newLimit < 0)"
            }

            return IllegalArgumentException(msg)
        }

        private fun createPositionException(newPosition: Int, limit: Int): IllegalArgumentException {
            val msg: String = if (newPosition > limit) {
                "newPosition > limit: ($newPosition > $limit)"
            } else { // assume negative
                require(newPosition < 0) { "newPosition expected to be negative" }
                "newPosition < 0: ($newPosition < 0)"
            }

            return IllegalArgumentException(msg)
        }

        @JvmStatic
        protected fun createSameBufferException(): IllegalArgumentException {
            return IllegalArgumentException("The source buffer is this buffer")
        }
    }

    protected var mark: Int = -1
    public var position: Int = 0
        set(newPosition) {
            if ((newPosition > limit) or (newPosition < 0)) throw createPositionException(newPosition, limit)
            if (mark > newPosition) mark = -1
            field = newPosition
        }
    public var limit: Int = 0
        set(newLimit) {
            if ((newLimit > capacity) or (newLimit < 0)) throw createLimitException(newLimit, capacity)
            field = newLimit
            if (position > newLimit) position = newLimit
            if (mark > newLimit) mark = -1
        }
    public var capacity: Int = 0
        protected set

    protected constructor(mark: Int, pos: Int, lim: Int, cap: Int) {
        if (cap < 0)
            throw createCapacityException(cap)
        this.capacity = cap
        limit = lim
        position = pos
        if (mark >= 0) {
            if (mark > pos) throw IllegalArgumentException("mark > position: ($mark > $pos)")

            this.mark = mark
        }

    }

    public abstract fun array(): T

    protected fun nextGetIndex() : Int {
        val p = position
        if (p >= limit) throw FeinnBufferUnderflowException()
        position = p + 1
        return p
    }

    protected fun nextGetIndex(nb: Int): Int {
        val p = position
        if (limit - p < nb)
            throw FeinnBufferUnderflowException()
        position = p + nb
        return p
    }

    protected fun checkIndex(i: Int) : Int {
        if ((i < 0) || (i >= limit)) throw IndexOutOfBoundsException()
        return i
    }

    public open fun rewind(): FeinnBuffer<T> {
        position = 0
        mark = -1
        return this
    }

    protected fun checkIndex(i: Int, nb: Int): Int {               // package-private
        if ((i < 0) || (nb > limit - i)) throw IndexOutOfBoundsException()
        return i
    }

    protected fun nextPutIndex(): Int {
        val p = position
        if (p >= limit) throw FeinnBufferOverflowException()
        position = p + 1
        return p
    }

    protected fun nextPutIndex(nb: Int): Int {
        val p = position
        if (limit - p < nb) throw FeinnBufferOverflowException()
        position = p + nb
        return p
    }

}