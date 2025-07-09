package id.feinn.utility.crypto

public interface CryptoServiceProperties {

    public fun bitsOfSecurity(): Int

    public fun getServiceName(): String

    public fun getPurpose(): CryptoServicePurpose

    public fun <T>getParams(): T?

}