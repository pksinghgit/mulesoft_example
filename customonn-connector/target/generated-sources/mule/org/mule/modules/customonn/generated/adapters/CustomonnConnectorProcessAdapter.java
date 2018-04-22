
package org.mule.modules.customonn.generated.adapters;

import javax.annotation.Generated;
import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.api.devkit.ProcessAdapter;
import org.mule.api.devkit.ProcessTemplate;
import org.mule.api.processor.MessageProcessor;
import org.mule.api.routing.filter.Filter;
import org.mule.modules.customonn.CustomonnConnector;
import org.mule.security.oauth.callback.ProcessCallback;


/**
 * A <code>CustomonnConnectorProcessAdapter</code> is a wrapper around {@link CustomonnConnector } that enables custom processing strategies.
 * 
 */
@SuppressWarnings("all")
@Generated(value = "Mule DevKit Version 3.9.0", date = "2017-11-20T09:42:04+05:30", comments = "Build UNNAMED.2793.f49b6c7")
public class CustomonnConnectorProcessAdapter
    extends CustomonnConnectorLifecycleInjectionAdapter
    implements ProcessAdapter<CustomonnConnectorCapabilitiesAdapter>
{


    public<P >ProcessTemplate<P, CustomonnConnectorCapabilitiesAdapter> getProcessTemplate() {
        final CustomonnConnectorCapabilitiesAdapter object = this;
        return new ProcessTemplate<P,CustomonnConnectorCapabilitiesAdapter>() {


            @Override
            public P execute(ProcessCallback<P, CustomonnConnectorCapabilitiesAdapter> processCallback, MessageProcessor messageProcessor, MuleEvent event)
                throws Exception
            {
                return processCallback.process(object);
            }

            @Override
            public P execute(ProcessCallback<P, CustomonnConnectorCapabilitiesAdapter> processCallback, Filter filter, MuleMessage message)
                throws Exception
            {
                return processCallback.process(object);
            }

        }
        ;
    }

}
