
package org.mule.modules.customsdkconnectorr.generated.adapters;

import javax.annotation.Generated;
import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.api.devkit.ProcessAdapter;
import org.mule.api.devkit.ProcessTemplate;
import org.mule.api.processor.MessageProcessor;
import org.mule.api.routing.filter.Filter;
import org.mule.modules.customsdkconnectorr.CustomSDKConnectorrConnector;
import org.mule.security.oauth.callback.ProcessCallback;


/**
 * A <code>CustomSDKConnectorrConnectorProcessAdapter</code> is a wrapper around {@link CustomSDKConnectorrConnector } that enables custom processing strategies.
 * 
 */
@SuppressWarnings("all")
@Generated(value = "Mule DevKit Version 3.9.0", date = "2018-02-20T10:08:50+05:30", comments = "Build UNNAMED.2793.f49b6c7")
public class CustomSDKConnectorrConnectorProcessAdapter
    extends CustomSDKConnectorrConnectorLifecycleInjectionAdapter
    implements ProcessAdapter<CustomSDKConnectorrConnectorCapabilitiesAdapter>
{


    public<P >ProcessTemplate<P, CustomSDKConnectorrConnectorCapabilitiesAdapter> getProcessTemplate() {
        final CustomSDKConnectorrConnectorCapabilitiesAdapter object = this;
        return new ProcessTemplate<P,CustomSDKConnectorrConnectorCapabilitiesAdapter>() {


            @Override
            public P execute(ProcessCallback<P, CustomSDKConnectorrConnectorCapabilitiesAdapter> processCallback, MessageProcessor messageProcessor, MuleEvent event)
                throws Exception
            {
                return processCallback.process(object);
            }

            @Override
            public P execute(ProcessCallback<P, CustomSDKConnectorrConnectorCapabilitiesAdapter> processCallback, Filter filter, MuleMessage message)
                throws Exception
            {
                return processCallback.process(object);
            }

        }
        ;
    }

}
