package id.feinn.utiliy.connection.monitor

import kotlinx.coroutines.flow.Flow

public interface FeinnConnectionMonitor {

    public val hasCapabilityInternet: Flow<CapabilityInternetType>

    public val activeConnection: Flow<List<ConnectionType>>

}
