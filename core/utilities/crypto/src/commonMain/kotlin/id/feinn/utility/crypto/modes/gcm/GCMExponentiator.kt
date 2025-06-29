package id.feinn.utility.crypto.modes.gcm

public interface GCMExponentiator {

    public fun init(x: ByteArray)

    public fun exponentiateX(pow: Long, output: ByteArray)

}