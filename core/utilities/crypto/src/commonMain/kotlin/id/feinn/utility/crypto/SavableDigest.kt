package id.feinn.utility.crypto

import id.feinn.utility.crypto.digests.EncodableDigest
import id.feinn.utility.crypto.digests.ExtendedDigest
import id.feinn.utility.crypto.util.Memoable

public interface SavableDigest : ExtendedDigest, EncodableDigest, Memoable
