package id.feinn.utility.crypto.modes

import id.feinn.utility.crypto.BlockCipher
import id.feinn.utility.crypto.MultiBlockCipher

public interface CBCModeCipher : MultiBlockCipher {

    public fun getUnderlyingCipher(): BlockCipher

}