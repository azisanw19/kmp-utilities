package id.feinn.utility.crypto.modes.gcm

public interface GCMMultiplier {

    public fun init(H: ByteArray)

    public fun multiplyH(x: ByteArray)

}