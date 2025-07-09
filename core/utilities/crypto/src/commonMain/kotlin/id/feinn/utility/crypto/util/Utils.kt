package id.feinn.utility.crypto.util

import id.feinn.utility.crypto.CryptoServiceProperties
import id.feinn.utility.crypto.CryptoServicePurpose
import id.feinn.utility.crypto.Digest

public object Utils {

    public fun getDefaultProperties(digest: Digest, purpose: CryptoServicePurpose): CryptoServiceProperties = DefaultProperties(digest.getDigestSize() * 4, digest.getAlgorithmName(), purpose)

    public fun getDefaultProperties(digest: Digest, prfBitsOfSecurity: Int, purpose: CryptoServicePurpose): CryptoServiceProperties = DefaultPropertiesWithPRF(digest.getDigestSize() * 4, prfBitsOfSecurity, digest.getAlgorithmName(), purpose)

    private class DefaultPropertiesWithPRF: CryptoServiceProperties {
        private val bitsOfSecurity: Int
        private val prfBitsOfSecurity: Int
        private val algorithmName: String
        private val purpose: CryptoServicePurpose

        constructor(bitsOfSecurity: Int, prfBitsOfSecurity: Int, algorithmName: String, purpose: CryptoServicePurpose) {
            this.bitsOfSecurity = bitsOfSecurity
            this.prfBitsOfSecurity = prfBitsOfSecurity
            this.algorithmName = algorithmName
            this.purpose = purpose
        }

        override fun bitsOfSecurity(): Int {
            if (purpose == CryptoServicePurpose.PRF) return prfBitsOfSecurity
            return bitsOfSecurity
        }

        override fun getServiceName(): String = algorithmName

        override fun getPurpose(): CryptoServicePurpose = purpose

        override fun <T> getParams(): T? = null
    }

    private class DefaultProperties: CryptoServiceProperties {
        private val bitsOfSecurity: Int
        private val algorithmName: String
        private val purpose: CryptoServicePurpose

        constructor(bitsOfSecurity: Int, algorithmName: String, purpose: CryptoServicePurpose) {
            this.bitsOfSecurity = bitsOfSecurity
            this.algorithmName = algorithmName
            this.purpose = purpose
        }

        override fun bitsOfSecurity(): Int = bitsOfSecurity

        override fun getServiceName(): String = algorithmName

        override fun getPurpose(): CryptoServicePurpose = purpose

        override fun <T> getParams(): T? = null


    }

}