package id.feinn.utility.crypto.params

import id.feinn.utility.crypto.CipherParameters

public class ParametersWithIV : CipherParameters {

    private val iv: ByteArray
    private val parameters: CipherParameters

    public constructor(parameters: CipherParameters, iv: ByteArray): this(parameters, iv, 0, iv.size)

    public constructor(parameters: CipherParameters, iv: ByteArray, ivOff: Int, ivLen: Int) {
        this.iv = ByteArray(ivLen)
        this.parameters = parameters

        iv.copyInto(this.iv, destinationOffset = 0, startIndex = ivOff, endIndex = ivOff + ivLen)
    }

    public fun getIV(): ByteArray = iv

    public fun getParameters(): CipherParameters = parameters

}