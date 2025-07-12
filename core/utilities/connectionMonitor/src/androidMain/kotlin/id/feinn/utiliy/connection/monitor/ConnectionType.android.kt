package id.feinn.utiliy.connection.monitor

import android.net.NetworkCapabilities

internal fun NetworkCapabilities.toConnectionType(): ConnectionType {
    return when {
        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> ConnectionType.WIFI
        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> ConnectionType.CELLULAR
        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> ConnectionType.ETHERNET
        hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> ConnectionType.BLUETOOTH
        hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> ConnectionType.VPN
        hasTransport(NetworkCapabilities.TRANSPORT_USB) -> ConnectionType.USB
        hasTransport(NetworkCapabilities.TRANSPORT_WIFI_AWARE) -> ConnectionType.WIFI_AWARE
        hasTransport(NetworkCapabilities.TRANSPORT_LOWPAN) -> ConnectionType.LOWPAN
        else -> ConnectionType.UNKNOWN
    }
}