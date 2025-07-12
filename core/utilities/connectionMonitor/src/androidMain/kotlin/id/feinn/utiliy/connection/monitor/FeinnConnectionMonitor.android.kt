package id.feinn.utiliy.connection.monitor

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged

public class FeinnConnectionMonitorImpl(
    private val context: Context
): FeinnConnectionMonitor {
    public override val hasCapabilityInternet: Flow<CapabilityInternetType>
        @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
        get() = callbackFlow {
            val connectivityManager = context.getSystemService(ConnectivityManager::class.java)
            if (connectivityManager == null) {
                channel.trySend(CapabilityInternetType.Unavailable)
                channel.close()
                return@callbackFlow
            }

            /**
             * The callback's methods are invoked on changes to *any* network matching the [NetworkRequest],
             * not just the active network. So we can simply track the presence (or absence) of such [Network].
             */
            val callback = object : ConnectivityManager.NetworkCallback() {

                val activeInternetNetworks = mutableSetOf<Network>()

                @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
                override fun onAvailable(network: Network) {
                    val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
                    if (networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                        activeInternetNetworks += network
                        networkCapabilities.toConnectionType().let { connectionType ->
                            channel.trySend(
                                CapabilityInternetType.Available(
                                    connectionType = connectionType,
                                    downstreamKbps = networkCapabilities.linkDownstreamBandwidthKbps.toDouble(),
                                    upstreamKbps = networkCapabilities.linkUpstreamBandwidthKbps.toDouble()
                                )
                            )
                        }
                    }
                }

                override fun onLost(network: Network) {
                    activeInternetNetworks -= network
                    if (activeInternetNetworks.isEmpty()) {
                        channel.trySend(CapabilityInternetType.Unavailable)
                    }
                }
            }

            val request = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                .build()
            connectivityManager.registerNetworkCallback(request, callback)

            /**
             * Sends initial value
             */
            val activeNetwork = connectivityManager.activeNetwork
            val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)

            val initial = if (networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true) {
                networkCapabilities.toConnectionType().let { connectionType ->
                    CapabilityInternetType.Available(
                        connectionType = connectionType,
                        downstreamKbps = networkCapabilities.linkDownstreamBandwidthKbps.toDouble(),
                        upstreamKbps = networkCapabilities.linkUpstreamBandwidthKbps.toDouble()
                    )
                }
            } else {
                CapabilityInternetType.Unavailable
            }
            channel.trySend(initial)

            awaitClose {
                connectivityManager.unregisterNetworkCallback(callback)
            }
        }
            .distinctUntilChanged()
            .conflate()

    public override val activeConnection: Flow<List<ConnectionType>>
        @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
        get() = callbackFlow {
            val connectivityManager = context.getSystemService(ConnectivityManager::class.java)
            if (connectivityManager == null) {
                channel.trySend(listOf())
                channel.close()
                return@callbackFlow
            }

            val activeNetworks = mutableMapOf<Network, ConnectionType>()

            val callback = object : ConnectivityManager.NetworkCallback() {
                @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
                override fun onAvailable(network: Network) {
                    val caps = connectivityManager.getNetworkCapabilities(network)
                    val type = caps?.toConnectionType() ?: return
                    activeNetworks[network] = type
                    channel.trySend(activeNetworks.values.distinct())
                }

                override fun onLost(network: Network) {
                    activeNetworks.remove(network)
                    channel.trySend(activeNetworks.values.distinct())
                }
            }

            val request = NetworkRequest.Builder().build()
            connectivityManager.registerNetworkCallback(request, callback)

            /**
             * Initial send
             */
            val active = connectivityManager.activeNetwork
            val caps = connectivityManager.getNetworkCapabilities(active)
            val initialType = caps?.toConnectionType()

            if (initialType != null) {
                activeNetworks[active!!] = initialType
                channel.trySend(activeNetworks.values.distinct())
            } else {
                channel.trySend(listOf())
            }

            awaitClose {
                connectivityManager.unregisterNetworkCallback(callback)
            }
        }
            .distinctUntilChanged()
            .conflate()

}

@Composable
public fun rememberFeinnConnectionMonitor(): FeinnConnectionMonitor {
    val context = LocalContext.current

    return remember { FeinnConnectionMonitorImpl(context) }
}