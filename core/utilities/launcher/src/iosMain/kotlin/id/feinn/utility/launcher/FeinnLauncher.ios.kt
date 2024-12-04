package id.feinn.utility.launcher

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import id.feinn.utility.context.FeinnLocalContext
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIDocumentInteractionController
import platform.UIKit.UIDocumentInteractionControllerDelegateProtocol
import platform.UIKit.UIViewController
import platform.darwin.NSObject

/**
 * A class responsible for handling URI launching on iOS platforms.
 *
 * This class provides a method to either open a URL in the default browser or
 * preview documents using `UIDocumentInteractionController`.
 */
public actual class FeinnLauncher {
    /**
     * Launches a URI on iOS.
     *
     * If the URI scheme is "https" or "http", the method opens the URL in the default browser
     * using `UIApplication.sharedApplication.openURL`. For other URI schemes, it uses
     * `UIDocumentInteractionController` to preview the document.
     *
     * The `UIDocumentInteractionController` is configured with a delegate to provide the
     * current `UIViewController` for document previews.
     *
     * @param uri The URI string to be launched.
     * @throws IllegalArgumentException If the URI string is invalid or cannot be parsed.
     */
    public actual fun launch(uri: String) {
        val paths = uri.split(":")
        if (paths.firstOrNull() == "https" || paths.firstOrNull() == "http") {
            UIApplication.sharedApplication.openURL(NSURL(string = uri))
            return
        }

        val url = NSURL(string = uri)
        val documentInteractionController = UIDocumentInteractionController()
        documentInteractionController.URL = url
        documentInteractionController.delegate = object : NSObject(),
            UIDocumentInteractionControllerDelegateProtocol {
            override fun documentInteractionControllerViewControllerForPreview(controller: UIDocumentInteractionController): UIViewController {
                return UIApplication.sharedApplication.keyWindow!!.rootViewController!!
            }
        }
        documentInteractionController.presentPreviewAnimated(animated = true)
    }
}

/**
 * A Composable function that remembers and provides an instance of `FeinnLauncher` for iOS.
 *
 * This function uses Jetpack Compose's `remember` to create and retain a single instance of
 * `FeinnLauncher` throughout the composable lifecycle.
 *
 * @param context The `FeinnLocalContext` (not used directly in this implementation).
 * @return An instance of `FeinnLauncher`.
 */
@Composable
public actual fun rememberFeinnLauncer(context: FeinnLocalContext): FeinnLauncher {
    return remember { FeinnLauncher() }
}
