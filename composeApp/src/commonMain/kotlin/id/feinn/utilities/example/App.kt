package id.feinn.utilities.example

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import examplekmputilities.composeapp.generated.resources.Res
import examplekmputilities.composeapp.generated.resources.compose_multiplatform
import id.feinn.utility.time.FeinnDate
import id.feinn.utility.time.FeinnDateTime
import id.feinn.utility.time.getFormattedDate
import id.feinn.utility.time.getFormattedDateTime
import id.feinn.utility.time.now
import id.feinn.utility.time.parse
import id.feinn.utility.time.toFeinnDate
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = {
                showContent = !showContent
                val feinnDate = FeinnDate.now()

                feinnDate.getFormattedDate()
                FeinnDate.parse("2021-01-01", format = "yyyy-MM-dd")

                val formattedDate = feinnDate.getFormattedDate("dd-MM-yyyy")

                println(formattedDate)

                val feinnDateTime = FeinnDateTime.now()
                val formattedDateTime = feinnDateTime.getFormattedDateTime("yyyy-MM-dd HH:mm:ss")

                println(formattedDateTime)

                val parsedDateTime = FeinnDateTime.parse("2021-01-01T12:00:00", "yyyy-MM-dd'T'HH:mm:ss")

                println(parsedDateTime)

                val feinnDate2 = feinnDateTime.toFeinnDate()

                println(feinnDate2)
            }) {
                Text("Click me!")
            }
            AnimatedVisibility(showContent) {
                val greeting = remember { Greeting().greet() }
                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(painterResource(Res.drawable.compose_multiplatform), null)
                    Text("Compose: $greeting")
                }
            }
        }
    }
}