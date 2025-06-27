package id.feinn.utility.crypto.modes

import id.feinn.utility.crypto.BlockCipher
import id.feinn.utility.crypto.CipherParameters
import id.feinn.utility.crypto.DataLengthException
import id.feinn.utility.crypto.DefaultMultiBlockCipher
import id.feinn.utility.crypto.params.ParametersWithIV

public class CBCBlockCipher(private var cipher: BlockCipher) : DefaultMultiBlockCipher(), CBCModeCipher {
    private val IV: ByteArray
    private var cbcV: ByteArray
    private var cbcNextV: ByteArray

    private val blockSize: Int
    private var encrypting: Boolean? = null

    public companion object {

        public fun newInstance(cipher: BlockCipher): CBCModeCipher = CBCBlockCipher(cipher)

    }

    init {
        this.blockSize = cipher.getBlockSize()
        this.IV = ByteArray(blockSize)
        this.cbcV = ByteArray(blockSize)
        this.cbcNextV = ByteArray(blockSize)
    }

    override fun getUnderlyingCipher(): BlockCipher = cipher

    @Throws(IllegalArgumentException::class)
    override fun init(forEncryption: Boolean, params: CipherParameters) {
        var paramsT = params

        this.encrypting = forEncryption

        if (paramsT is ParametersWithIV) {
            val iv = paramsT.getIV()

            if (iv.size != blockSize) throw IllegalArgumentException("initialisation vector must be the same length as block size")

            iv.copyInto(destination = IV, destinationOffset = 0, startIndex = 0, endIndex = iv.size)

            paramsT = paramsT.getParameters()
        } else {
            IV.fill(0)
        }

        reset()

        cipher.init(forEncryption, paramsT)
    }

    override fun getAlgorithmName(): String = cipher.getAlgorithmName() + "/CBC"

    override fun getBlockSize(): Int = cipher.getBlockSize()

    override fun processBlock(input: ByteArray, inOff: Int, output: ByteArray, outOff: Int): Int = if (encrypting!!) encryptBlock(input, inOff, output, outOff) else decryptBlock(input, inOff, output, outOff)

    override fun reset() {
        IV.copyInto(destination = cbcV, destinationOffset = 0, startIndex = 0, endIndex = IV.size)
        cbcNextV.fill(0)

        cipher.reset()
    }

    @Throws(DataLengthException::class, IllegalStateException::class)
    private fun encryptBlock(`in`: ByteArray, inOff: Int, out: ByteArray, outOff: Int): Int {
        if ((inOff + blockSize) > `in`.size) throw DataLengthException("input buffer too short")

        for (i in 0 until blockSize) cbcV[i] = (cbcV[i].toInt() xor `in`[inOff + i].toInt()).toByte()

        val length = cipher.processBlock(cbcV, 0, out, outOff)

        out.copyInto(destination = cbcV, destinationOffset = 0, startIndex = outOff, endIndex = outOff + cbcV.size)

        return length
    }

    @Throws(DataLengthException::class, IllegalStateException::class)
    public fun decryptBlock(`in`: ByteArray, inOff: Int, out: ByteArray, outOff: Int): Int {
        if ((inOff + blockSize) > `in`.size) throw DataLengthException("input buffer too short")

        `in`.copyInto(destination = cbcNextV, destinationOffset = 0, startIndex = inOff, endIndex = inOff + blockSize)

        val length = cipher.processBlock(`in`, inOff, out, outOff)

        for (i in 0 until blockSize) out[outOff + i] = (out[outOff + i].toInt() xor cbcV[i].toInt()).toByte()

        val tmp: ByteArray = cbcV
        cbcV = cbcNextV
        cbcNextV = tmp

        return length
    }
}



































