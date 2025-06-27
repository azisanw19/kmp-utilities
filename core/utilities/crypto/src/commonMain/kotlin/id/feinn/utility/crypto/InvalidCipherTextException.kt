package id.feinn.utility.crypto

public class InvalidCipherTextException: CryptoException {

    public constructor(): super()

    public constructor(message: String): super(message)

    public constructor(message: String, cause: Throwable): super(message, cause)

}