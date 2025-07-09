package id.feinn.utility.crypto.raw

public object Bits {

    public fun bitPermuteStep(x: Int, m: Int, s: Int): Int {
        val t = (x xor (x ushr s)) and m
        return (t xor (t shl s)) xor x
    }

    public fun bitPermuteStep(x: Long, m: Long, s: Int): Long {
        val t = (x xor (x ushr s)) and m
        return (t xor (t shl s)) xor x
    }

    public fun bitPermuteStepSimple(x: Int, m: Int, s: Int): Int = ((x and m) shl s) or ((x ushr s) and m)

    public fun bitPermuteStepSimple(x: Long, m: Long, s: Int): Long = ((x and m) shl s) or ((x ushr s) and m)

}