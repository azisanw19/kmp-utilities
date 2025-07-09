package id.feinn.utility.crypto.digests

import id.feinn.utility.crypto.CryptoServiceProperties
import id.feinn.utility.crypto.CryptoServicePurpose
import id.feinn.utility.crypto.util.Memoable
import id.feinn.utility.crypto.util.Pack
import kotlin.math.max
import kotlin.properties.Delegates

public abstract class GeneralDigest: ExtendedDigest, Memoable {

    public companion object {

        private const val BYTE_LENGTH: Int = 64

    }

    protected val purpose: CryptoServicePurpose

    private val xBuf: ByteArray = ByteArray(4)
    private var xBufOff: Int by Delegates.notNull()

    private var byteCount: Long by Delegates.notNull()

    protected constructor(): this(CryptoServicePurpose.ANY)

    protected constructor(purpose: CryptoServicePurpose) {
        this.purpose = purpose

        xBufOff = 0
    }

    protected constructor(t: GeneralDigest) {
        this.purpose = t.purpose

        copyIn(t)
    }

    protected constructor(encodedState: ByteArray) {
        val values = CryptoServicePurpose.entries.toTypedArray()
        this.purpose = values[encodedState[encodedState.size - 1].toInt()]

        encodedState.copyInto(xBuf, destinationOffset = 0, startIndex = 0, endIndex = xBuf.size)
        xBufOff = Pack.bigEndianToInt(encodedState, 4)
        byteCount = Pack.bigEndianToLong(encodedState, 8)
    }

    protected fun copyIn(t: GeneralDigest) {
        t.xBuf.copyInto(xBuf, destinationOffset = 0, startIndex = 0, endIndex = t.xBuf.size)

        xBufOff = t.xBufOff
        byteCount = t.byteCount
    }

    override fun update(`in`: Byte) {
        xBuf[xBufOff++] = `in`

        if (xBufOff == xBuf.size) {
            processWord(xBuf, 0)
            xBufOff = 0
        }

        byteCount++
    }

    override fun update(`in`: ByteArray, inOff: Int, len: Int) {
        var lenT = len
        var inT = `in`
        lenT = max(0, lenT)

        var i = 0
        if (xBufOff != 0) {
            while (i < lenT) {
                xBuf[xBufOff++] = inT[inOff + i++]
                if (xBufOff == 4) {
                    processWord(xBuf, 0)
                    xBufOff = 0
                    break
                }
            }
        }

        val limit = lenT - 3
        while (i < limit) {
            processWord(inT, inOff + i)
            i += 4
        }

        while (i < lenT) xBuf[xBufOff++] = inT[inOff + i++]

        byteCount += lenT
    }

    public fun finish() {
        val bitLength: Long = (byteCount shl 3)

        update(128.toByte())

        while (xBufOff != 0) update(0.toByte())

        processLength(bitLength)

        processBlock()
    }

    override fun reset() {
        byteCount = 0

        xBufOff = 0
        for (i in 0 until xBuf.size) xBuf[i] = 0
    }

    protected fun populateState(state: ByteArray) {
        xBuf.copyInto(state, destinationOffset = 0, startIndex = 0, endIndex = xBufOff)
        Pack.intToBigEndian(xBufOff, state, 4)
        Pack.longToBigEndian(byteCount, state, 8)
    }

    override fun getByteLength(): Int = BYTE_LENGTH

    protected abstract fun processWord(`in`: ByteArray, inOff: Int)

    protected abstract fun processLength(bitLength: Long)

    protected abstract fun processBlock()

    protected abstract fun cryptoServiceProperties(): CryptoServiceProperties


}