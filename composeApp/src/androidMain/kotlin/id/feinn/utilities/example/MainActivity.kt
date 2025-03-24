package id.feinn.utilities.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import id.feinn.utility.notification.FeinnNotificationAndroid

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val feinnNotificationAndroid = FeinnNotificationAndroid.getInstance()
        feinnNotificationAndroid.setDrawableId(R.drawable.ic_launcher_foreground)

        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}