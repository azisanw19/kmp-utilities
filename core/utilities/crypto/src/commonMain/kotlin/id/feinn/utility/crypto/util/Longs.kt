package id.feinn.utility.crypto.util

public object Longs {

    public const val BYTES: Int = 8
    public const val SIZE: Int = Long.SIZE_BITS

    public fun highestOneBit(i: Long): Long = i.takeHighestOneBit()

    public fun lowestOneBit(i: Long): Long = i.takeLowestOneBit()

    public fun numberOfLoadingZeros(i: Long): Int = i.countLeadingZeroBits()

    public fun numberOfTrailingZeros(i: Long): Int = i.countTrailingZeroBits()

    public fun reverse(i: Long): Long = i.reverse()

    public fun reverseBytes(i: Long): Long = i.reverseBytes()

    public fun rotateLeft(i: Long, distance: Int): Long = i.rotateLeft(distance)

    public fun rotateRight(i: Long, distance: Int): Long = i.rotateRight(distance)

    public fun valueOf(value: Long): Long = value
}