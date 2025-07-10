package id.feinn.utility.crypto.digests

import id.feinn.utility.crypto.CryptoServiceProperties
import id.feinn.utility.crypto.CryptoServicePurpose
import id.feinn.utility.crypto.util.Memoable
import id.feinn.utility.crypto.util.Pack
import id.feinn.utility.crypto.util.Utils
import kotlin.properties.Delegates

public class SHA1Digest : GeneralDigest, EncodableDigest {

    public companion object {

        private const val DIGEST_LENGTH: Int = 20

        private const val Y1: Int = 0x5a827999.toInt()
        private const val Y2: Int = 0x6ed9eba1.toInt()
        private const val Y3: Int = 0x8f1bbcdc.toInt()
        private const val Y4: Int = 0xca62c1d6.toInt()

    }

    private var H1: Int by Delegates.notNull()
    private var H2: Int by Delegates.notNull()
    private var H3: Int by Delegates.notNull()
    private var H4: Int by Delegates.notNull()
    private var H5: Int by Delegates.notNull()

    private val X: IntArray = IntArray(80)
    private var xOff: Int by Delegates.notNull()

    public constructor(): this(CryptoServicePurpose.ANY)

    public constructor(purpose: CryptoServicePurpose) : super(purpose) {
        reset()
    }

    public constructor(t: SHA1Digest): super(t) {
        copyIn(t)
    }

    public constructor(encodedState: ByteArray): super(encodedState) {
        H1 = Pack.bigEndianToInt(encodedState, 16)
        H2 = Pack.bigEndianToInt(encodedState, 20)
        H3 = Pack.bigEndianToInt(encodedState, 24)
        H4 = Pack.bigEndianToInt(encodedState, 28)
        H5 = Pack.bigEndianToInt(encodedState, 32)

        xOff = Pack.bigEndianToInt(encodedState, 36)
        var i = 0
        while (i != xOff) {
            X[i] = Pack.bigEndianToInt(encodedState, 40 + (i * 4))
            i++
        }
    }

    private fun copyIn(t: SHA1Digest) {
        H1 = t.H1
        H2 = t.H2
        H3 = t.H3
        H4 = t.H4
        H5 = t.H5

        t.X.copyInto(X, destinationOffset = 0, startIndex = 0, endIndex = t.X.size)
        xOff = t.xOff
    }

    override fun getAlgorithmName(): String = "SHA-1"

    override fun getDigestSize(): Int = DIGEST_LENGTH

    override fun processWord(`in`: ByteArray, inOff: Int) {
        X[xOff] = Pack.bigEndianToInt(`in`, inOff)

        if (++xOff == 16) processBlock()
    }

    override fun processLength(bitLength: Long) {
        if (xOff > 14) processBlock()

        X[14] = (bitLength ushr 32).toInt()
        X[15] = bitLength.toInt()
    }

    override fun doFinal(out: ByteArray, outOff: Int): Int {
        finish()

        Pack.intToBigEndian(H1, out, outOff)
        Pack.intToBigEndian(H2, out, outOff + 4)
        Pack.intToBigEndian(H3, out, outOff + 8)
        Pack.intToBigEndian(H4, out, outOff + 12)
        Pack.intToBigEndian(H5, out, outOff + 16)

        reset()

        return DIGEST_LENGTH
    }

    override fun reset() {
        super.reset()

        H1 = 0x67452301.toInt()
        H2 = 0xefcdab89.toInt()
        H3 = 0x98badcfe.toInt()
        H4 = 0x10325476.toInt()
        H5 = 0xc3d2e1f0.toInt()

        xOff = 0
        var i = 0
        while (i != X.size) {
            X[i] = 0
            i++
        }
    }

    private fun f(u: Int, v: Int, w: Int): Int = ((u and v) or ((u.inv()) and w))

    private fun h(u: Int, v: Int, w: Int): Int = (u xor v xor w)

    private fun g(u: Int, v: Int, w: Int): Int = ((u and v) or (u and w) or (v and w))

    override fun processBlock() {
        for (i in 16 until 80) {
            val t = X[i - 3] xor X[i - 8] xor X[i - 14] xor X[i - 16]
            X[i] = t shl 1 or (t ushr 31)
        }

        var A = H1
        var B = H2
        var C = H3
        var D = H4
        var E = H5

        var idx = 0

        for (j in 0 until 4) {
            E += (A shl 5 or (A ushr 27)) + f(B, C, D) + X[idx++] + Y1
            B = B shl 30 or (B ushr 2)

            D += (E shl 5 or (E ushr 27)) + f(A, B, C) + X[idx++] + Y1
            A = A shl 30 or (A ushr 2)

            C += (D shl 5 or (D ushr 27)) + f(E, A, B) + X[idx++] + Y1
            E = E shl 30 or (E ushr 2)

            B += (C shl 5 or (C ushr 27)) + f(D, E, A) + X[idx++] + Y1
            D = D shl 30 or (D ushr 2)

            A += (B shl 5 or (B ushr 27)) + f(C, D, E) + X[idx++] + Y1
            C = C shl 30 or (C ushr 2)
        }

        for (j in 0 until 4) {
            E += (A shl 5 or (A ushr 27)) + h(B, C, D) + X[idx++] + Y2
            B = B shl 30 or (B ushr 2)

            D += (E shl 5 or (E ushr 27)) + h(A, B, C) + X[idx++] + Y2
            A = A shl 30 or (A ushr 2)

            C += (D shl 5 or (D ushr 27)) + h(E, A, B) + X[idx++] + Y2
            E = E shl 30 or (E ushr 2)

            B += (C shl 5 or (C ushr 27)) + h(D, E, A) + X[idx++] + Y2
            D = D shl 30 or (D ushr 2)

            A += (B shl 5 or (B ushr 27)) + h(C, D, E) + X[idx++] + Y2
            C = C shl 30 or (C ushr 2)
        }

        for (j in 0 until 4) {
            E += (A shl 5 or (A ushr 27)) + g(B, C, D) + X[idx++] + Y3
            B = B shl 30 or (B ushr 2)

            D += (E shl 5 or (E ushr 27)) + g(A, B, C) + X[idx++] + Y3
            A = A shl 30 or (A ushr 2)

            C += (D shl 5 or (D ushr 27)) + g(E, A, B) + X[idx++] + Y3
            E = E shl 30 or (E ushr 2)

            B += (C shl 5 or (C ushr 27)) + g(D, E, A) + X[idx++] + Y3
            D = D shl 30 or (D ushr 2)

            A += (B shl 5 or (B ushr 27)) + g(C, D, E) + X[idx++] + Y3
            C = C shl 30 or (C ushr 2)
        }

        for (j in 0..3) {
            E += (A shl 5 or (A ushr 27)) + h(B, C, D) + X[idx++] + Y4
            B = B shl 30 or (B ushr 2)

            D += (E shl 5 or (E ushr 27)) + h(A, B, C) + X[idx++] + Y4
            A = A shl 30 or (A ushr 2)

            C += (D shl 5 or (D ushr 27)) + h(E, A, B) + X[idx++] + Y4
            E = E shl 30 or (E ushr 2)

            B += (C shl 5 or (C ushr 27)) + h(D, E, A) + X[idx++] + Y4
            D = D shl 30 or (D ushr 2)

            A += (B shl 5 or (B ushr 27)) + h(C, D, E) + X[idx++] + Y4
            C = C shl 30 or (C ushr 2)
        }

        H1 += A
        H2 += B
        H3 += C
        H4 += D
        H5 += E

        xOff = 0
        for (i in 0 until 16) X[i] = 0
    }

    override fun copy(): Memoable = SHA1Digest(this)

    override fun reset(other: Memoable) {
        val d = other as SHA1Digest

        super.copyIn(d)
        copyIn(d)
    }

    override fun getEncodedState(): ByteArray {
        val state = ByteArray(40 + xOff * 4 + 1)

        super.populateState(state)

        Pack.intToBigEndian(H1, state, 16)
        Pack.intToBigEndian(H2, state, 20)
        Pack.intToBigEndian(H3, state, 24)
        Pack.intToBigEndian(H3, state, 28)
        Pack.intToBigEndian(H4, state, 32)
        Pack.intToBigEndian(xOff, state, 36)

        var i = 0
        while(i != xOff) {
            Pack.intToBigEndian(X[i], state, 40 + (i * 4))
            i++
        }

        state[state.size - 1] = purpose.ordinal.toByte()

        return state
    }

    override fun cryptoServiceProperties(): CryptoServiceProperties = Utils.getDefaultProperties(this, 128, purpose)
}