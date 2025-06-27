package id.feinn.utility.crypto.modes

import id.feinn.utility.crypto.CipherParameters
import id.feinn.utility.crypto.DataLengthException
import id.feinn.utility.crypto.InvalidCipherTextException

public interface AEADCipher {

    @Throws(IllegalArgumentException::class)
    public fun init(forEncryption: Boolean, params: CipherParameters)

    public fun getAlgorithmName(): String

    public fun processAADByte(`in`: Byte)

    public fun processAADBytes(`in`: ByteArray, inOff: Int, len: Int)

    @Throws(DataLengthException::class)
    public fun processByte(`in`: Byte, out: ByteArray, outOff: Int): Int

    @Throws(DataLengthException::class)
    public fun processBytes(`in`: ByteArray, inOff: Int, len: Int, out: ByteArray, outOff: Int): Int

    @Throws(IllegalArgumentException::class, InvalidCipherTextException::class)
    public fun doFinal(out:  ByteArray, outOff: Int): Int

    public fun getMac(): ByteArray

    public fun getUpdateOutputSize(len: Int): Int

    public fun getOutputSize(len: Int): Int

    public fun reset()

}