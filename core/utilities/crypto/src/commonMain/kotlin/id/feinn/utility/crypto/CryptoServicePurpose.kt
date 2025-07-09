package id.feinn.utility.crypto

public enum class CryptoServicePurpose {
    AGREEMENT,
    ENCRYPTION,
    DECRYPTION,
    KEYGEN,
    SIGNING,         // for signatures (and digests)
    VERIFYING,
    AUTHENTICATION,  // for MACs (and digests)
    VERIFICATION,
    PRF,
    ANY
}