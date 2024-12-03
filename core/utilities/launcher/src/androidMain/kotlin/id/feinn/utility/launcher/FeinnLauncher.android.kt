package id.feinn.utility.launcher

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import id.feinn.utility.context.FeinnLocalContext

public actual class FeinnLauncher(
    private val context: Context
) {
    public actual fun launch(uri: String) {
        val mimeType = context.contentResolver.getType(Uri.parse(uri))

        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(Uri.parse(uri), mimeType)
        intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
        context.startActivity(intent)
    }
}

@Composable
public actual fun rememberFeinnLauncer(context: FeinnLocalContext): FeinnLauncher {
    return remember { FeinnLauncher(context = context) }
}