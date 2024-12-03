package id.feinn.utility.launcher

import androidx.compose.runtime.Composable
import id.feinn.utility.context.FeinnLocalContext

public expect class FeinnLauncher {
    public fun launch(uri: String)
}

@Composable
public expect fun rememberFeinnLauncer(context: FeinnLocalContext): FeinnLauncher