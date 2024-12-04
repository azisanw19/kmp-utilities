package id.feinn.utility.launcher

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import id.feinn.utility.context.FeinnLocalContext
import kotlin.jvm.Throws

/**
 * A class responsible for launching URIs using an intent in the Android context.
 *
 * @property context The Android `Context` used to access the content resolver and launch activities.
 */
public actual class FeinnLauncher(
    private val context: Context
) {
    /**
     * Launches a URI using an intent with the appropriate MIME type.
     *
     * The method determines the MIME type of the URI using the content resolver,
     * creates an `Intent` with the action `ACTION_VIEW`, and sets the URI and MIME type.
     * Additional flags are set to ensure:
     * - No activity history is stored (`FLAG_ACTIVITY_NO_HISTORY`).
     * - A new task is started (`FLAG_ACTIVITY_NEW_TASK`).
     * - Read URI permission is granted (`FLAG_GRANT_READ_URI_PERMISSION`).
     *
     * @param uri The URI string to be launched.
     * @throws ActivityNotFoundException If no activity is found to handle the intent.
     */
    @Throws(ActivityNotFoundException::class)
    public actual fun launch(uri: String) {
        val mimeType = context.contentResolver.getType(Uri.parse(uri))

        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(Uri.parse(uri), mimeType)
        intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY or
                Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_GRANT_READ_URI_PERMISSION
        context.startActivity(intent)
    }
}

/**
 * A Composable function that remembers and provides an instance of `FeinnLauncher`.
 *
 * This function uses Jetpack Compose's `remember` to create and retain a single
 * instance of `FeinnLauncher` throughout the composable lifecycle.
 *
 * @param context The context from `FeinnLocalContext`, used to initialize the launcher.
 * @return An instance of `FeinnLauncher`.
 */
@Composable
public actual fun rememberFeinnLauncer(context: FeinnLocalContext): FeinnLauncher {
    return remember { FeinnLauncher(context = context) }
}
