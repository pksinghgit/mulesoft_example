
package org.mule.modules.customsdkconnectorr.generated.adapters;

import javax.annotation.Generated;
import org.mule.api.devkit.capability.Capabilities;
import org.mule.api.devkit.capability.ModuleCapability;
import org.mule.modules.customsdkconnectorr.CustomSDKConnectorrConnector;


/**
 * A <code>CustomSDKConnectorrConnectorCapabilitiesAdapter</code> is a wrapper around {@link CustomSDKConnectorrConnector } that implements {@link org.mule.api.Capabilities} interface.
 * 
 */
@SuppressWarnings("all")
@Generated(value = "Mule DevKit Version 3.9.0", date = "2018-02-20T10:08:50+05:30", comments = "Build UNNAMED.2793.f49b6c7")
public class CustomSDKConnectorrConnectorCapabilitiesAdapter
    extends CustomSDKConnectorrConnector
    implements Capabilities
{


    /**
     * Returns true if this module implements such capability
     * 
     */
    public boolean isCapableOf(ModuleCapability capability) {
        if (capability == ModuleCapability.LIFECYCLE_CAPABLE) {
            return true;
        }
        return false;
    }

}
