package id.feinn.utility.crypto.params

import id.feinn.utility.crypto.CipherParameters
import id.feinn.utility.crypto.security.SecureRandom

public class ParametersWithRandom : CipherParameters {
    private val random: SecureRandom?
    private val parameters: CipherParameters

    public constructor(parameters: CipherParameters, secureRandom: SecureRandom?) {
        this.random = secureRandom
        this.parameters = parameters
    }

    public constructor(parameters: CipherParameters): this(parameters, null)

    public fun getRandom(): SecureRandom? = random

    public fun getParameters(): CipherParameters = parameters
}