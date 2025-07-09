package id.feinn.utility.crypto.digests

import id.feinn.utility.crypto.Digest

public interface ExtendedDigest : Digest {

    public fun getByteLength(): Int

}