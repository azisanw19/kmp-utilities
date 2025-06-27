package id.feinn.utility.crypto.modes

import id.feinn.utility.crypto.BlockCipher
import id.feinn.utility.crypto.CipherParameters
import id.feinn.utility.crypto.DataLengthException
import id.feinn.utility.crypto.StreamBlockCipher
import id.feinn.utility.crypto.params.ParametersWithIV
import id.feinn.utility.crypto.util.Pack

public class SICBlockCipher(c: BlockCipher) : StreamBlockCipher(c), CTRModeCipher {

    private val cipher: BlockCipher = c
    private val blockSize: Int = cipher.getBlockSize()

    private var IV: ByteArray = ByteArray(blockSize)
    private val counter: ByteArray = ByteArray(blockSize)
    private val counterOut: ByteArray = ByteArray(blockSize)
    private var byteCount: Int = 0

    public companion object {

        public fun newInstance(cipher: BlockCipher): CTRModeCipher = SICBlockCipher(cipher)

    }

    override fun init(forEncryption: Boolean, params: CipherParameters) {
        if (params is ParametersWithIV) {
            this.IV = params.getIV().copyOf()

            if (blockSize < IV.size) throw IllegalArgumentException("CTR/SIC mode requires IV no greater than: $blockSize bytes.")

            val maxCounterSize: Int = if (8 > blockSize / 2) blockSize / 2 else 8

            if (blockSize - IV.size > maxCounterSize) throw IllegalArgumentException("CTR/SIC mode requires IV of at least: " + (blockSize - maxCounterSize) + " bytes.")

            cipher.init(true, params.getParameters())

            reset()
        } else throw IllegalArgumentException("CTR/SIC mode requires ParametersWithIV")
    }

    override fun getAlgorithmName(): String = cipher.getAlgorithmName() + "/SIC"

    override fun getBlockSize(): Int = cipher.getBlockSize()

    override fun processBlock(input: ByteArray, inOff: Int, output: ByteArray, outOff: Int): Int {
        if (byteCount != 0) {
            processBytes(input, inOff, blockSize, output, outOff)
            return blockSize
        }

        if (inOff + blockSize > input.size) throw DataLengthException("input buffer too small")
        if (outOff + blockSize > output.size) throw DataLengthException("output buffer too short")

        cipher.processBlock(counter, 0, counterOut, 0)
        for (i in 0 until blockSize) output[outOff + i] = (input.get(inOff + i).toInt() xor counterOut[i].toInt()).toByte()
        incrementCounter()
        return blockSize
    }

    @Throws(DataLengthException::class)
    override fun processBytes(`in`: ByteArray, inOff: Int, len: Int, out: ByteArray, outOff: Int): Int {
        if (inOff + len > `in`.size) throw DataLengthException("input buffer too small")
        if (outOff + len > out.size) throw DataLengthException("output buffer too short")

        for (i in 0 until len) {
            var next: Byte

            if (byteCount == 0) {
                checkLastIncrement()

                cipher.processBlock(counter, 0, counterOut, 0)
                next = (`in`[inOff + i].toInt() xor counterOut[byteCount++].toInt()).toByte()
            } else {
                next = (`in`[inOff + i].toInt() xor counterOut[byteCount++].toInt()).toByte()
                if (byteCount == counter.size) {
                    byteCount = 0
                    incrementCounter()
                }
            }
            out[outOff + i] = next
        }

        return len
    }

    override fun calculateByte(b: Byte): Byte {
        if (byteCount == 0) {
            checkLastIncrement()

            cipher.processBlock(counter, 0, counterOut, 0)

            return (counterOut[byteCount++].toInt() xor b.toInt()).toByte()
        }

        val rv = (counterOut[byteCount++].toInt() xor b.toInt()).toByte()

        if (byteCount == counter.size) {
            byteCount = 0
            incrementCounter()
        }

        return rv
    }

    private fun checkCounter() {
        if (IV.size < blockSize)
            for (i in IV.size-1 downTo 0)
                if (counter[i] != IV[i]) throw IllegalStateException("Counter in CTR/SIC mode out of range.")
    }

    private fun checkLastIncrement() {
        if (IV.size < blockSize)
            if (counter[IV.size - 1] != IV[IV.size - 1]) throw IllegalStateException("Counter in CTR/SIC mode out of range.")
    }

    private fun incrementCounter() {
        var i = counter.size
        while (--i >= 0) if ((++counter[i]).toInt() != 0) break
    }

    private fun incrementCounterAt(pos: Int) {
        var i = counter.size - pos
        while (--i >= 0) if ((++counter[i]).toInt() != 0) break
    }

    private fun incrementCounter(offset: Int) {
        val old = counter[counter.size - 1]

        counter[counter.size - 1] = (counter[counter.size - 1] + offset).toByte()

        if (old.toInt() != 0 && counter[counter.size - 1].toInt() < old) incrementCounterAt(1)
    }

    private fun decrementCounterAt(pos: Int) {
        var i = counter.size - pos
        while (--i >= 0) if ((--counter[i]).toInt() != -1) return
    }

    private fun adjustCounter(n: Long) {
        if (n >= 0) {
            val numBlocks: Long = (n + byteCount) / blockSize

            var rem: Long = numBlocks
            if (rem > 255) {
                for (i in 5 downTo 1) {
                    val diff: Long = 1L shl (8 * i)
                    while (rem >= diff) {
                        incrementCounterAt(i)
                        rem -= diff
                    }
                }
            }

            incrementCounter(rem.toInt())

            byteCount = ((n + byteCount) - (blockSize * numBlocks)).toInt()
        } else {
            val numBlocks: Long = (-n - byteCount) / blockSize

            var rem: Long = numBlocks
            if (rem > 255) {
                for (i in 5 downTo 1) {
                    val diff = 1L shl (8 * i)
                    while (rem > diff) {
                        decrementCounterAt(i)
                        rem -= diff
                    }
                }
            }

            var i = 0L
            while (i != rem) {
                decrementCounterAt(0)
                i++
            }

            val gap: Int = (byteCount + n + (blockSize * numBlocks)).toInt()

            if (gap >= 0) byteCount = 0
            else {
                decrementCounterAt(0)
                byteCount = blockSize + gap
            }
        }
    }

    override fun reset() {
        counter.fill(0)
        IV.copyInto(counter, destinationOffset = 0, startIndex = 0, endIndex = IV.size)
        cipher.reset()
        this.byteCount = 0
    }

    override fun skip(numberOfByte: Long): Long {
        adjustCounter(numberOfByte)

        checkCounter()

        cipher.processBlock(counter, 0, counterOut, 0)

        return numberOfByte
    }

    override fun seekTo(position: Long): Long {
        reset()

        return skip(position)
    }

    override fun getPosition(): Long {
        val res = ByteArray(counter.size)

        counter.copyInto(res, destinationOffset = 0, startIndex = 0, endIndex = res.size)

        for (i in res.size - 1 downTo 1) {
            var v: Int
            if (i < IV.size) v = (res[i].toInt() and 0xff) - (IV[i].toInt() and 0xff)
            else v = (res[i].toInt() and 0xff)

            if (v < 0) {
                res[i - 1]--
                v += 256
            }

            res[i] = v.toByte()
        }

        return Pack.bigEndianToLong(res, res.size - 8) * blockSize + byteCount
    }

}


































