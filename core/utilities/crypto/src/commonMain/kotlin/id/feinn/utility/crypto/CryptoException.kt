package id.feinn.utility.crypto

public open class CryptoException : Exception {

    public constructor(): super()

    public constructor(message: String): super(message)

    public constructor(message: String, cause: Throwable): super(message, cause)

}