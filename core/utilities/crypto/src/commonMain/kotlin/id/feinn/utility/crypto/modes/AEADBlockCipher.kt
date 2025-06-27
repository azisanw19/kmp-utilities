package id.feinn.utility.crypto.modes

import id.feinn.utility.crypto.BlockCipher

public interface AEADBlockCipher : AEADCipher {

    public fun getUnderlyingCipher(): BlockCipher

}