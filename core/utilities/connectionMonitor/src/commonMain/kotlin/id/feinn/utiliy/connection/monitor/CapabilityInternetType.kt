package id.feinn.utiliy.connection.monitor

public sealed class CapabilityInternetType {
    public data class Available(
        val connectionType: ConnectionType,
        val downstreamKbps: Double,
        val upstreamKbps: Double,
    ) : CapabilityInternetType()
    public data object Unavailable : CapabilityInternetType()
}