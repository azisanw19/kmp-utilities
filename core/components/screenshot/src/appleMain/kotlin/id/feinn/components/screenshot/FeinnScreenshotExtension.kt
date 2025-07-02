package id.feinn.components.screenshot

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.Image
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import platform.Foundation.NSData
import platform.UIKit.UIImage
import platform.UIKit.UIImagePNGRepresentation
import platform.posix.memcpy
import kotlinx.cinterop.*

@OptIn(ExperimentalForeignApi::class)
public fun UIImage.toImageBitmap(): ImageBitmap {
    val nsData: NSData = UIImagePNGRepresentation(this)
        ?: throw NullPointerException("UIImagePNGRepresentation returned null")

    val size = nsData.length.toInt()
    val buffer = ByteArray(size)

    memScoped {
        val rawBytes = allocArray<ByteVar>(size)
        memcpy(rawBytes, nsData.bytes, size.convert())
        for (i in 0 until size) {
            buffer[i] = rawBytes[i]
        }
    }

    val skiaImage = Image.makeFromEncoded(buffer)
    return skiaImage.toComposeImageBitmap()
}