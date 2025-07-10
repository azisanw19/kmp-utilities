package id.feinn.utility.crypto.macs

import id.feinn.utility.crypto.CipherParameters
import id.feinn.utility.crypto.Digest
import id.feinn.utility.crypto.Mac
import id.feinn.utility.crypto.digests.ExtendedDigest
import id.feinn.utility.crypto.params.KeyParameter
import id.feinn.utility.crypto.util.Memoable

public class HMac: Mac {

    private companion object {

        private const val IPAD: Byte = 0x36.toByte()
        private const val OPAD: Byte = 0x5c.toByte()

        private val blockLength: MutableMap<String, Int> = mutableMapOf(
            "GOST3411" to 32,

            "MD2" to 16,
            "MD4" to 64,
            "MD5" to 64,

            "RIPEMD128" to 64,
            "RIPEMD160" to 64,

            "SHA-1" to 64,
            "SHA-224" to 64,
            "SHA-256" to 64,
            "SHA-384" to 128,
            "SHA-512" to 128,

            "Tiger" to 64,
            "Whirlpool" to 64
        )

        private fun getByteLength(digest: Digest): Int {
            if (digest is ExtendedDigest) return digest.getByteLength()

            val b = blockLength[digest.getAlgorithmName()]

            if (b == null) throw IllegalArgumentException("unknown digest passed: ${digest.getAlgorithmName()}")

            return b
        }

        private fun xorPad(pad: ByteArray, len: Int, n: Byte) {
            for (i in 0 until len) pad[i] = (pad[i].toInt() xor n.toInt()).toByte()
        }

    }

    private val digest: Digest
    private val digestSize: Int
    private val blockLength: Int
    private var ipadState: Memoable? = null
    private var opadState: Memoable? = null

    private val inputPad: ByteArray
    private val outputBuf: ByteArray

    public constructor(digest: Digest) : this(digest, getByteLength(digest))

    public constructor(digest: Digest, byteLength: Int) {
        this.digest = digest
        this.digestSize = digest.getDigestSize()
        this.blockLength = byteLength
        this.inputPad = ByteArray(blockLength)
        this.outputBuf = ByteArray(blockLength + digestSize)
    }

    override fun getAlgorithmName(): String = "${digest.getAlgorithmName()}/HMAC"

    public fun getUnderlyingDigest(): Digest = digest

    override fun init(params: CipherParameters) {
        digest.reset()

        val key = (params as KeyParameter).getKey()
        var keyLength = key.size

        if (keyLength > blockLength) {
            digest.update(key, 0, keyLength)
            digest.doFinal(inputPad, 0)

            keyLength = digestSize
        } else {
            key.copyInto(inputPad, destinationOffset = 0, startIndex = 0, endIndex = keyLength)
        }

        for (i in keyLength until inputPad.size) inputPad[i] = 0

        inputPad.copyInto(outputBuf, destinationOffset = 0, startIndex = 0, endIndex = blockLength)

        xorPad(inputPad, blockLength, IPAD)
        xorPad(outputBuf, blockLength, OPAD)

        if (digest is Memoable) {
            opadState = digest.copy()

            (opadState as Digest).update(outputBuf, 0, blockLength)
        }

        digest.update(inputPad, 0, inputPad.size)

        if (digest is Memoable) ipadState = digest.copy()
    }

    override fun getMacSize(): Int = digestSize

    override fun update(`in`: Byte) {
        digest.update(`in`)
    }

    override fun update(`in`: ByteArray, inOff: Int, len: Int) {
        digest.update(`in`, inOff, len)
    }

    override fun doFinal(out: ByteArray, outOff: Int): Int {
        digest.doFinal(outputBuf, blockLength)

        if (opadState != null) {
            (digest as Memoable).reset(opadState!!)
            digest.update(outputBuf, blockLength, digest.getDigestSize())
        } else {
            digest.update(outputBuf, 0, outputBuf.size)
        }

        val len = digest.doFinal(out, outOff)

        for (i in blockLength until outputBuf.size) outputBuf[i] = 0

        if (ipadState != null) (digest as Memoable).reset(ipadState!!)
        else digest.update(inputPad, 0, inputPad.size)

        return len
    }

    override fun reset() {
        if (ipadState != null) (digest as Memoable).reset(ipadState!!)
        else {
            digest.reset()
            digest.update(inputPad, 0, inputPad.size)
        }
    }

}