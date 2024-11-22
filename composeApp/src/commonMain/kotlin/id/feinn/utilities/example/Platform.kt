package id.feinn.utilities.example

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform