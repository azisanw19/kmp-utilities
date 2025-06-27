package id.feinn.utility.crypto.modes

import id.feinn.utility.crypto.BlockCipher
import id.feinn.utility.crypto.MultiBlockCipher
import id.feinn.utility.crypto.SkippingStreamCipher

public interface CTRModeCipher : MultiBlockCipher, SkippingStreamCipher {

    public fun getUnderlyingCipher(): BlockCipher

}