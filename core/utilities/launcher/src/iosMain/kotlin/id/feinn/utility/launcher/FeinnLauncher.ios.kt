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

public actual class FeinnLauncher{
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

@Composable
public actual fun rememberFeinnLauncer(context: FeinnLocalContext): FeinnLauncher {
    return remember { FeinnLauncher() }
}