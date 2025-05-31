package id.feinn.utility.bytebuffer

import kotlin.test.Test

class AccessibilityTest {

    private val byteBuffer = FeinnByteBuffer.wrap(byteArrayOf(72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100, 72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100))

    @Test
    fun `test get byteBuffer`() {
        byteBuffer.get()
    }

}