
package org.mule.modules.restconnectorcustom.generated.adapters;

import javax.annotation.Generated;
import org.mule.api.devkit.capability.Capabilities;
import org.mule.api.devkit.capability.ModuleCapability;
import org.mule.modules.restconnectorcustom.RestConnectorCustomConnector;


/**
 * A <code>RestConnectorCustomConnectorCapabilitiesAdapter</code> is a wrapper around {@link RestConnectorCustomConnector } that implements {@link org.mule.api.Capabilities} interface.
 * 
 */
@SuppressWarnings("all")
@Generated(value = "Mule DevKit Version 3.9.0", date = "2018-02-20T05:08:25+05:30", comments = "Build UNNAMED.2793.f49b6c7")
public abstract class RestConnectorCustomConnectorCapabilitiesAdapter
    extends RestConnectorCustomConnector
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
