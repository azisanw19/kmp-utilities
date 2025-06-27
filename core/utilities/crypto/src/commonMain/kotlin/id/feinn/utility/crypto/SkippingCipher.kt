package id.feinn.utility.crypto

public interface SkippingCipher {

    public fun skip(numberOfByte: Long): Long

    public fun seekTo(position: Long): Long

    public fun getPosition(): Long

}