package id.feinn.utility.crypto.util

public interface Memoable {

    public fun copy(): Memoable

    public fun reset(other: Memoable)

}