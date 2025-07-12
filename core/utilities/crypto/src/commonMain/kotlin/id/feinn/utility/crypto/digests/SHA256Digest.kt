package id.feinn.utility.crypto.digests

import id.feinn.utility.crypto.CryptoServiceProperties
import id.feinn.utility.crypto.CryptoServicePurpose
import id.feinn.utility.crypto.Digest
import id.feinn.utility.crypto.SavableDigest
import id.feinn.utility.crypto.util.Memoable
import id.feinn.utility.crypto.util.Pack
import id.feinn.utility.crypto.util.Utils
import kotlin.properties.Delegates

public class SHA256Digest : GeneralDigest, SavableDigest {

    public companion object {
        private const val DIGEST_LENGTH: Int = 32

        public fun newInstance(): SavableDigest = SHA256Digest()
        public fun newInstance(purpose: CryptoServicePurpose): SavableDigest = SHA256Digest(purpose)
        public fun newInstance(digest: Digest): SavableDigest {
            if (digest is SHA256Digest) return SHA256Digest(digest)

            throw IllegalArgumentException("receiver digest not available for input type ${digest::class.simpleName}")
        }
        public fun newInstance(encoded: ByteArray): SHA256Digest = SHA256Digest(encoded)

        private fun Ch(x: Int, y: Int, z: Int): Int = (x and y) xor ((x.inv()) and z)
        private fun Maj(x: Int, y: Int, z: Int): Int = (x and y) or (z and (x xor y))
        private fun Sum0(x: Int): Int = ((x ushr 2) or (x shl 30)) xor ((x ushr 13) or (x shl 19)) xor ((x ushr 22) or (x shl 10))
        private fun Sum1(x: Int): Int = ((x ushr 6) or (x shl 26)) xor ((x ushr 11) or (x shl 21)) xor ((x ushr 25) or (x shl 7))
        private fun Theta0(x: Int): Int = ((x ushr 7) or (x shl 25)) xor ((x ushr 18) or (x shl 14)) xor (x ushr 3)
        private fun Theta1(x: Int): Int = ((x ushr 17) or (x shl 15)) xor ((x ushr 19) or (x shl 13)) xor (x ushr 10)

        public val K: IntArray = intArrayOf(
            0x428a2f98.toInt(), 0x71374491.toInt(), 0xb5c0fbcf.toInt(), 0xe9b5dba5.toInt(), 0x3956c25b.toInt(), 0x59f111f1.toInt(), 0x923f82a4.toInt(), 0xab1c5ed5.toInt(),
            0xd807aa98.toInt(), 0x12835b01.toInt(), 0x243185be.toInt(), 0x550c7dc3.toInt(), 0x72be5d74.toInt(), 0x80deb1fe.toInt(), 0x9bdc06a7.toInt(), 0xc19bf174.toInt(),
            0xe49b69c1.toInt(), 0xefbe4786.toInt(), 0x0fc19dc6.toInt(), 0x240ca1cc.toInt(), 0x2de92c6f.toInt(), 0x4a7484aa.toInt(), 0x5cb0a9dc.toInt(), 0x76f988da.toInt(),
            0x983e5152.toInt(), 0xa831c66d.toInt(), 0xb00327c8.toInt(), 0xbf597fc7.toInt(), 0xc6e00bf3.toInt(), 0xd5a79147.toInt(), 0x06ca6351.toInt(), 0x14292967.toInt(),
            0x27b70a85.toInt(), 0x2e1b2138.toInt(), 0x4d2c6dfc.toInt(), 0x53380d13.toInt(), 0x650a7354.toInt(), 0x766a0abb.toInt(), 0x81c2c92e.toInt(), 0x92722c85.toInt(),
            0xa2bfe8a1.toInt(), 0xa81a664b.toInt(), 0xc24b8b70.toInt(), 0xc76c51a3.toInt(), 0xd192e819.toInt(), 0xd6990624.toInt(), 0xf40e3585.toInt(), 0x106aa070.toInt(),
            0x19a4c116.toInt(), 0x1e376c08.toInt(), 0x2748774c.toInt(), 0x34b0bcb5.toInt(), 0x391c0cb3.toInt(), 0x4ed8aa4a.toInt(), 0x5b9cca4f.toInt(), 0x682e6ff3.toInt(),
            0x748f82ee.toInt(), 0x78a5636f.toInt(), 0x84c87814.toInt(), 0x8cc70208.toInt(), 0x90befffa.toInt(), 0xa4506ceb.toInt(), 0xbef9a3f7.toInt(), 0xc67178f2.toInt()
        )
    }

    private var H1: Int by Delegates.notNull()
    private var H2: Int by Delegates.notNull()
    private var H3: Int by Delegates.notNull()
    private var H4: Int by Delegates.notNull()
    private var H5: Int by Delegates.notNull()
    private var H6: Int by Delegates.notNull()
    private var H7: Int by Delegates.notNull()
    private var H8: Int by Delegates.notNull()

    private val X: IntArray = IntArray(64)
    private var xOff: Int by Delegates.notNull()

    public constructor() : this(CryptoServicePurpose.ANY)

    public constructor(purpose: CryptoServicePurpose) : super(purpose) {
        reset()
    }

    public constructor(t: SHA256Digest) : super(t) {
        copyIn(t)
    }

    private fun copyIn(t: SHA256Digest) {
        super.copyIn(t)

        H1 = t.H1
        H2 = t.H2
        H3 = t.H3
        H4 = t.H4
        H5 = t.H5
        H6 = t.H6
        H7 = t.H7
        H8 = t.H8

        t.X.copyInto(destination = X, destinationOffset = 0, startIndex = 0, endIndex = t.X.size)
        xOff = t.xOff
    }

    public constructor(encodedState: ByteArray) : super(encodedState) {
        H1 = Pack.bigEndianToInt(encodedState, 16)
        H2 = Pack.bigEndianToInt(encodedState, 20)
        H3 = Pack.bigEndianToInt(encodedState, 24)
        H4 = Pack.bigEndianToInt(encodedState, 28)
        H5 = Pack.bigEndianToInt(encodedState, 32)
        H6 = Pack.bigEndianToInt(encodedState, 36)
        H7 = Pack.bigEndianToInt(encodedState, 40)
        H8 = Pack.bigEndianToInt(encodedState, 44)

        xOff = Pack.bigEndianToInt(encodedState, 48)
        for (i in 0 until xOff) X[i] = Pack.bigEndianToInt(encodedState, 52 + (i * 4))
    }

    override fun getAlgorithmName(): String = "SHA-256"

    override fun getDigestSize(): Int = DIGEST_LENGTH

    override fun processWord(`in`: ByteArray, inOff: Int) {
        X[xOff] = Pack.bigEndianToInt(`in`, inOff)

        if (++xOff == 16) processBlock()
    }

    override fun processLength(bitLength: Long) {
        if (xOff > 14) processBlock()

        X[14] = (bitLength ushr 32).toInt()
        X[15] = (bitLength and 0xffffffffL).toInt()
    }

    override fun doFinal(out: ByteArray, outOff: Int): Int {
        finish()

        Pack.intToBigEndian(H1, out, outOff)
        Pack.intToBigEndian(H2, out, outOff + 4)
        Pack.intToBigEndian(H3, out, outOff + 8)
        Pack.intToBigEndian(H4, out, outOff + 12)
        Pack.intToBigEndian(H5, out, outOff + 16)
        Pack.intToBigEndian(H6, out, outOff + 20)
        Pack.intToBigEndian(H7, out, outOff + 24)
        Pack.intToBigEndian(H8, out, outOff + 28)

        reset()

        return DIGEST_LENGTH
    }

    override fun reset() {
        super.reset()

        H1 = 0x6a09e667.toInt()
        H2 = 0xbb67ae85.toInt()
        H3 = 0x3c6ef372.toInt()
        H4 = 0xa54ff53a.toInt()
        H5 = 0x510e527f.toInt()
        H6 = 0x9b05688c.toInt()
        H7 = 0x1f83d9ab.toInt()
        H8 = 0x5be0cd19.toInt()

        xOff = 0
        for (i in X.indices) X[i] = 0
    }

    override fun processBlock() {
        for (t in 16 .. 63) X[t] = Theta1(X[t - 2]) + X[t - 7] + Theta0(X[t - 15]) + X[t - 16]

        var a = H1
        var b = H2
        var c = H3
        var d = H4
        var e = H5
        var f = H6
        var g = H7
        var h = H8

        var t = 0
        for (i in 0 until 8) {
            h += Sum1(e) + Ch(e, f, g) + K[t] + X[t]
            d += h
            h += Sum0(a) + Maj(a, b, c)
            ++t

            g += Sum1(d) + Ch(d, e, f) + K[t] + X[t]
            c += g
            g += Sum0(h) + Maj(h, a, b)
            ++t

            f += Sum1(c) + Ch(c, d, e) + K[t] + X[t]
            b += f
            f += Sum0(g) + Maj(g, h, a)
            ++t

            e += Sum1(b) + Ch(b, c, d) + K[t] + X[t]
            a += e
            e += Sum0(f) + Maj(f, g, h)
            ++t

            d += Sum1(a) + Ch(a, b, c) + K[t] + X[t]
            h += d
            d += Sum0(e) + Maj(e, f, g)
            ++t

            c += Sum1(h) + Ch(h, a, b) + K[t] + X[t]
            g += c
            c += Sum0(d) + Maj(d, e, f)
            ++t

            b += Sum1(g) + Ch(g, h, a) + K[t] + X[t]
            f += b
            b += Sum0(c) + Maj(c, d, e)
            ++t

            a += Sum1(f) + Ch(f, g, h) + K[t] + X[t]
            e += a
            a += Sum0(b) + Maj(b, c, d)
            ++t
        }

        H1 += a
        H2 += b
        H3 += c
        H4 += d
        H5 += e
        H6 += f
        H7 += g
        H8 += h

        xOff = 0
        for (i in 0 until 16) X[i] = 0
    }

    override fun copy(): Memoable = SHA256Digest(this)

    override fun reset(other: Memoable) {
        val d: SHA256Digest = other as SHA256Digest

        copyIn(d)
    }

    override fun getEncodedState(): ByteArray {
        val state = ByteArray(52 + xOff * 4 + 1)

        super.populateState(state)

        Pack.intToBigEndian(H1, state, 16)
        Pack.intToBigEndian(H2, state, 20)
        Pack.intToBigEndian(H3, state, 24)
        Pack.intToBigEndian(H4, state, 28)
        Pack.intToBigEndian(H5, state, 32)
        Pack.intToBigEndian(H6, state, 36)
        Pack.intToBigEndian(H7, state, 40)
        Pack.intToBigEndian(H8, state, 44)
        Pack.intToBigEndian(xOff, state, 48)

        for (i in 0 until xOff) Pack.intToBigEndian(X[i], state, 52 + (i * 4))

        state[state.size - 1] = purpose.ordinal.toByte()

        return state
    }

    protected override fun cryptoServiceProperties(): CryptoServiceProperties = Utils.getDefaultProperties(this, 256, purpose)
}