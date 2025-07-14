package id.feinn.utility.crypto.digests

import id.feinn.utility.crypto.CryptoServiceProperties
import id.feinn.utility.crypto.CryptoServicePurpose
import id.feinn.utility.crypto.util.Memoable
import id.feinn.utility.crypto.util.Pack
import id.feinn.utility.crypto.util.Utils
import kotlin.properties.Delegates

public class MD5Digest : GeneralDigest, EncodableDigest {

    public companion object {
        private const val DIGEST_LENGTH: Int = 16

        //
        // round 1 left rotates
        //
        private const val S11: Int = 7
        private const val S12: Int = 12
        private const val S13: Int = 17
        private const val S14: Int = 22

        //
        // round 2 left rotates
        //
        private const val S21: Int = 5
        private const val S22: Int = 9
        private const val S23: Int = 14
        private const val S24: Int = 20

        //
        // round 3 left rotates
        //
        private const val S31: Int = 4
        private const val S32: Int = 11
        private const val S33: Int = 16
        private const val S34: Int = 23

        //
        // round 4 left rotates
        //
        private const val S41: Int = 6
        private const val S42: Int = 10
        private const val S43: Int = 15
        private const val S44: Int = 21
    }

    private var H1: Int by Delegates.notNull()
    private var H2: Int by Delegates.notNull()
    private var H3: Int by Delegates.notNull()
    private var H4: Int by Delegates.notNull()

    private val X: IntArray = IntArray(16)
    private var xOff: Int by Delegates.notNull()

    public constructor() : this(CryptoServicePurpose.ANY)

    public constructor(purpose: CryptoServicePurpose) : super(purpose) {
        reset()
    }

    public constructor(encodedState: ByteArray) : super(encodedState) {
        H1 = Pack.bigEndianToInt(encodedState, 16)
        H2 = Pack.bigEndianToInt(encodedState, 20)
        H3 = Pack.bigEndianToInt(encodedState, 24)
        H4 = Pack.bigEndianToInt(encodedState, 28)

        xOff = Pack.bigEndianToInt(encodedState, 32)
        for (i in 0 until xOff) X[i] = Pack.bigEndianToInt(encodedState, 36 + (i * 4))
    }

    public constructor(t: MD5Digest) : super(t) {
        copyIn(t)
    }

    private fun copyIn(t: MD5Digest) {
        super.copyIn(t)

        H1 = t.H1
        H2 = t.H2
        H3 = t.H3
        H4 = t.H4

        t.X.copyInto(destination = X, destinationOffset = 0, startIndex = 0, endIndex = t.X.size)
        xOff = t.xOff
    }

    override fun getAlgorithmName(): String = "MD5"

    override fun getDigestSize(): Int = DIGEST_LENGTH

    override fun processWord(`in`: ByteArray, inOff: Int) {
        X[xOff++] = Pack.littleEndianToInt(`in`, inOff)

        if (xOff == 16) processBlock()
    }

    override fun processLength(bitLength: Long) {
        if (xOff > 14) processBlock()

        X[14] = (bitLength and 0xffffffffL).toInt()
        X[15] = (bitLength ushr 32).toInt()
    }

    override fun doFinal(out: ByteArray, outOff: Int): Int {
        finish()

        Pack.intToLittleEndian(H1, out, outOff)
        Pack.intToLittleEndian(H2, out, outOff + 4)
        Pack.intToLittleEndian(H3, out, outOff + 8)
        Pack.intToLittleEndian(H4, out, outOff + 12)

        reset()

        return DIGEST_LENGTH
    }

    override fun reset() {
        super.reset()

        H1 = 0x67452301.toInt()
        H2 = 0xefcdab89.toInt()
        H3 = 0x98badcfe.toInt()
        H4 = 0x10325476.toInt()

        xOff = 0

        for (i in X.indices) X[i] = 0
    }

    private fun rotateLeft(x: Int, n: Int): Int = (x shl n) or (x ushr (32 - n))

    private fun F(u: Int, v: Int, w: Int): Int = (u and v) or (u.inv() and w)

    private fun G(u: Int, v: Int, w: Int): Int = (u and w) or (v and w.inv())

    private fun H(u: Int, v: Int, w: Int): Int = u xor v xor w

    private fun K(u: Int, v: Int, w: Int): Int = v xor (u or w.inv())

    override fun processBlock() {
        var a = H1
        var b = H2
        var c = H3
        var d = H4

        //
        // Round 1 - F cycle, 16 times.
        //
        a = rotateLeft(a + F(b, c, d) + X[ 0] + 0xd76aa478.toInt(), S11) + b
        d = rotateLeft(d + F(a, b, c) + X[ 1] + 0xe8c7b756.toInt(), S12) + a
        c = rotateLeft(c + F(d, a, b) + X[ 2] + 0x242070db.toInt(), S13) + d
        b = rotateLeft(b + F(c, d, a) + X[ 3] + 0xc1bdceee.toInt(), S14) + c
        a = rotateLeft(a + F(b, c, d) + X[ 4] + 0xf57c0faf.toInt(), S11) + b
        d = rotateLeft(d + F(a, b, c) + X[ 5] + 0x4787c62a.toInt(), S12) + a
        c = rotateLeft(c + F(d, a, b) + X[ 6] + 0xa8304613.toInt(), S13) + d
        b = rotateLeft(b + F(c, d, a) + X[ 7] + 0xfd469501.toInt(), S14) + c
        a = rotateLeft(a + F(b, c, d) + X[ 8] + 0x698098d8.toInt(), S11) + b
        d = rotateLeft(d + F(a, b, c) + X[ 9] + 0x8b44f7af.toInt(), S12) + a
        c = rotateLeft(c + F(d, a, b) + X[10] + 0xffff5bb1.toInt(), S13) + d
        b = rotateLeft(b + F(c, d, a) + X[11] + 0x895cd7be.toInt(), S14) + c
        a = rotateLeft(a + F(b, c, d) + X[12] + 0x6b901122.toInt(), S11) + b
        d = rotateLeft(d + F(a, b, c) + X[13] + 0xfd987193.toInt(), S12) + a
        c = rotateLeft(c + F(d, a, b) + X[14] + 0xa679438e.toInt(), S13) + d
        b = rotateLeft(b + F(c, d, a) + X[15] + 0x49b40821.toInt(), S14) + c

        //
        // Round 2 - G cycle, 16 times.
        //
        a = rotateLeft(a + G(b, c, d) + X[ 1] + 0xf61e2562.toInt(), S21) + b
        d = rotateLeft(d + G(a, b, c) + X[ 6] + 0xc040b340.toInt(), S22) + a
        c = rotateLeft(c + G(d, a, b) + X[11] + 0x265e5a51.toInt(), S23) + d
        b = rotateLeft(b + G(c, d, a) + X[ 0] + 0xe9b6c7aa.toInt(), S24) + c
        a = rotateLeft(a + G(b, c, d) + X[ 5] + 0xd62f105d.toInt(), S21) + b
        d = rotateLeft(d + G(a, b, c) + X[10] + 0x02441453.toInt(), S22) + a
        c = rotateLeft(c + G(d, a, b) + X[15] + 0xd8a1e681.toInt(), S23) + d
        b = rotateLeft(b + G(c, d, a) + X[ 4] + 0xe7d3fbc8.toInt(), S24) + c
        a = rotateLeft(a + G(b, c, d) + X[ 9] + 0x21e1cde6.toInt(), S21) + b
        d = rotateLeft(d + G(a, b, c) + X[14] + 0xc33707d6.toInt(), S22) + a
        c = rotateLeft(c + G(d, a, b) + X[ 3] + 0xf4d50d87.toInt(), S23) + d
        b = rotateLeft(b + G(c, d, a) + X[ 8] + 0x455a14ed.toInt(), S24) + c
        a = rotateLeft(a + G(b, c, d) + X[13] + 0xa9e3e905.toInt(), S21) + b
        d = rotateLeft(d + G(a, b, c) + X[ 2] + 0xfcefa3f8.toInt(), S22) + a
        c = rotateLeft(c + G(d, a, b) + X[ 7] + 0x676f02d9.toInt(), S23) + d
        b = rotateLeft(b + G(c, d, a) + X[12] + 0x8d2a4c8a.toInt(), S24) + c

        //
        // Round 3 - H cycle, 16 times.
        //
        a = rotateLeft(a + H(b, c, d) + X[ 5] + 0xfffa3942.toInt(), S31) + b
        d = rotateLeft(d + H(a, b, c) + X[ 8] + 0x8771f681.toInt(), S32) + a
        c = rotateLeft(c + H(d, a, b) + X[11] + 0x6d9d6122.toInt(), S33) + d
        b = rotateLeft(b + H(c, d, a) + X[14] + 0xfde5380c.toInt(), S34) + c
        a = rotateLeft(a + H(b, c, d) + X[ 1] + 0xa4beea44.toInt(), S31) + b
        d = rotateLeft(d + H(a, b, c) + X[ 4] + 0x4bdecfa9.toInt(), S32) + a
        c = rotateLeft(c + H(d, a, b) + X[ 7] + 0xf6bb4b60.toInt(), S33) + d
        b = rotateLeft(b + H(c, d, a) + X[10] + 0xbebfbc70.toInt(), S34) + c
        a = rotateLeft(a + H(b, c, d) + X[13] + 0x289b7ec6.toInt(), S31) + b
        d = rotateLeft(d + H(a, b, c) + X[ 0] + 0xeaa127fa.toInt(), S32) + a
        c = rotateLeft(c + H(d, a, b) + X[ 3] + 0xd4ef3085.toInt(), S33) + d
        b = rotateLeft(b + H(c, d, a) + X[ 6] + 0x04881d05.toInt(), S34) + c
        a = rotateLeft(a + H(b, c, d) + X[ 9] + 0xd9d4d039.toInt(), S31) + b
        d = rotateLeft(d + H(a, b, c) + X[12] + 0xe6db99e5.toInt(), S32) + a
        c = rotateLeft(c + H(d, a, b) + X[15] + 0x1fa27cf8.toInt(), S33) + d
        b = rotateLeft(b + H(c, d, a) + X[ 2] + 0xc4ac5665.toInt(), S34) + c

        //
        // Round 4 - K cycle, 16 times.
        //
        a = rotateLeft(a + K(b, c, d) + X[ 0] + 0xf4292244.toInt(), S41) + b
        d = rotateLeft(d + K(a, b, c) + X[ 7] + 0x432aff97.toInt(), S42) + a
        c = rotateLeft(c + K(d, a, b) + X[14] + 0xab9423a7.toInt(), S43) + d
        b = rotateLeft(b + K(c, d, a) + X[ 5] + 0xfc93a039.toInt(), S44) + c
        a = rotateLeft(a + K(b, c, d) + X[12] + 0x655b59c3.toInt(), S41) + b
        d = rotateLeft(d + K(a, b, c) + X[ 3] + 0x8f0ccc92.toInt(), S42) + a
        c = rotateLeft(c + K(d, a, b) + X[10] + 0xffeff47d.toInt(), S43) + d
        b = rotateLeft(b + K(c, d, a) + X[ 1] + 0x85845dd1.toInt(), S44) + c
        a = rotateLeft(a + K(b, c, d) + X[ 8] + 0x6fa87e4f.toInt(), S41) + b
        d = rotateLeft(d + K(a, b, c) + X[15] + 0xfe2ce6e0.toInt(), S42) + a
        c = rotateLeft(c + K(d, a, b) + X[ 6] + 0xa3014314.toInt(), S43) + d
        b = rotateLeft(b + K(c, d, a) + X[13] + 0x4e0811a1.toInt(), S44) + c
        a = rotateLeft(a + K(b, c, d) + X[ 4] + 0xf7537e82.toInt(), S41) + b
        d = rotateLeft(d + K(a, b, c) + X[11] + 0xbd3af235.toInt(), S42) + a
        c = rotateLeft(c + K(d, a, b) + X[ 2] + 0x2ad7d2bb.toInt(), S43) + d
        b = rotateLeft(b + K(c, d, a) + X[ 9] + 0xeb86d391.toInt(), S44) + c

        H1 += a
        H2 += b
        H3 += c
        H4 += d

        //
        // reset the offset and clean out the word buffer.
        //
        xOff = 0
        for (i in X.indices) X[i] = 0
    }

    override fun copy(): Memoable = MD5Digest(this)

    override fun reset(other: Memoable) {
        val d = other as MD5Digest

        copyIn(d)
    }

    override fun getEncodedState(): ByteArray {
        val state = ByteArray(36 + xOff * 4 + 1)

        super.populateState(state)

        Pack.intToBigEndian(H1, state, 16)
        Pack.intToBigEndian(H2, state, 20)
        Pack.intToBigEndian(H3, state, 24)
        Pack.intToBigEndian(H4, state, 28)
        Pack.intToBigEndian(xOff, state, 32)

        for (i in 0 until xOff) Pack.intToBigEndian(X[i], state, 36 + (i * 4))

        state[state.size - 1] = purpose.ordinal.toByte()

        return state
    }

    override fun cryptoServiceProperties(): CryptoServiceProperties = Utils.getDefaultProperties(this, purpose)

}