package id.feinn.utility.crypto.macs

import id.feinn.utility.crypto.BlockCipher
import id.feinn.utility.crypto.CipherParameters
import id.feinn.utility.crypto.Mac
import id.feinn.utility.crypto.paddings.BlockCipherPadding
import id.feinn.utility.crypto.modes.CBCBlockCipher

public class CBCBlockCipherMac : Mac {

    private val mac: ByteArray

    private val buf: ByteArray
    private var bufOff: Int
    private val cipher: BlockCipher
    private val padding: BlockCipherPadding?

    private val macSize: Int

    public constructor(cipher: BlockCipher) : this(cipher, (cipher.getBlockSize() * 8) / 2, null)

    public constructor(cipher: BlockCipher, padding: BlockCipherPadding) : this(cipher, (cipher.getBlockSize() * 8) / 2, padding)

    public constructor(cipher: BlockCipher, macSizeInBits: Int) : this(cipher, macSizeInBits, null)

    public constructor(cipher: BlockCipher, macSizeInBits: Int, padding: BlockCipherPadding?) {
        if ((macSizeInBits % 8) != 0) throw IllegalArgumentException("MAC size must be multiple of 8")

        this.cipher = CBCBlockCipher.newInstance(cipher)
        this.padding = padding
        this.macSize = macSizeInBits / 8

        mac = ByteArray(cipher.getBlockSize())

        buf = ByteArray(cipher.getBlockSize())
        bufOff = 0
    }

    override fun getAlgorithmName(): String = cipher.getAlgorithmName()

    override fun init(params: CipherParameters) {
        reset()

        cipher.init(true, params)
    }

    override fun getMacSize(): Int = macSize

    override fun update(`in`: Byte) {
        if (bufOff == buf.size) {
            cipher.processBlock(buf, 0, mac, 0)
            bufOff = 0
        }

        buf[bufOff++] = `in`
    }

    override fun update(`in`: ByteArray, inOff: Int, len: Int) {
        var lenT = len
        var inOffT = inOff

        if (lenT < 0) throw IllegalArgumentException("Can't have a negative input length!")

        val blockSize = cipher.getBlockSize()
        val gapLen = blockSize - bufOff

        if (lenT > gapLen) {
            `in`.copyInto(destination = buf, destinationOffset = bufOff, startIndex = inOff, endIndex = inOff + gapLen)

            cipher.processBlock(buf, 0, mac, 0)

            bufOff = 0
            lenT -= gapLen
            inOffT += gapLen

            while (lenT > blockSize) {
                cipher.processBlock(`in`, inOffT, mac, 0)

                lenT -= blockSize
                inOffT += blockSize
            }
        }

        `in`.copyInto(destination = buf, destinationOffset = bufOff, startIndex = inOff, endIndex = inOff + len)

        bufOff += lenT
    }

    override fun doFinal(out: ByteArray, outOff: Int): Int {
        val blockSize = cipher.getBlockSize()

        if (padding == null) {
            while (bufOff < blockSize) {
                buf[bufOff] = 0
                bufOff++
            }
        } else {
            if (bufOff == blockSize) {
                cipher.processBlock(buf, 0, mac, 0)
                bufOff = 0
            }

            padding.addPadding(buf, bufOff)
        }

        cipher.processBlock(buf, 0, mac, 0)

        mac.copyInto(destination = out, destinationOffset = outOff, startIndex = 0, endIndex = macSize)

        reset()

        return macSize
    }

    override fun reset() {
        for (i in buf.indices) buf[i] = 0

        bufOff = 0

        cipher.reset()
    }


}

































