package id.feinn.utility.crypto.params

import id.feinn.utility.crypto.CipherParameters

public class AEADParameters : CipherParameters {

    private var associatedText: ByteArray?
    private var nonce: ByteArray
    private var key: KeyParameter
    private var macSize: Int

    public constructor(key: KeyParameter, macSize: Int, nonce: ByteArray): this(key, macSize, nonce, null)

    public constructor(key: KeyParameter, macSize: Int, nonce: ByteArray, associatedText: ByteArray?) {
        this.key = key
        this.nonce = nonce.copyOf()
        this.macSize = macSize
        this.associatedText = associatedText?.copyOf()
    }

    public fun getKey(): KeyParameter = key

    public fun getMacSize(): Int = macSize

    public fun getAssociatedText(): ByteArray? = associatedText?.copyOf()

    public fun getNonce(): ByteArray = nonce.copyOf()

}