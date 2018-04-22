
package org.mule.modules.customrest.generated.adapters;

import javax.annotation.Generated;
import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.api.devkit.ProcessAdapter;
import org.mule.api.devkit.ProcessTemplate;
import org.mule.api.processor.MessageProcessor;
import org.mule.api.routing.filter.Filter;
import org.mule.modules.customrest.CustomRESTConnector;
import org.mule.security.oauth.callback.ProcessCallback;


/**
 * A <code>CustomRESTConnectorProcessAdapter</code> is a wrapper around {@link CustomRESTConnector } that enables custom processing strategies.
 * 
 */
@SuppressWarnings("all")
@Generated(value = "Mule DevKit Version 3.9.0", date = "2018-02-20T10:33:21+05:30", comments = "Build UNNAMED.2793.f49b6c7")
public abstract class CustomRESTConnectorProcessAdapter
    extends CustomRESTConnectorLifecycleInjectionAdapter
    implements ProcessAdapter<CustomRESTConnectorCapabilitiesAdapter>
{


    public<P >ProcessTemplate<P, CustomRESTConnectorCapabilitiesAdapter> getProcessTemplate() {
        final CustomRESTConnectorCapabilitiesAdapter object = this;
        return new ProcessTemplate<P,CustomRESTConnectorCapabilitiesAdapter>() {


            @Override
            public P execute(ProcessCallback<P, CustomRESTConnectorCapabilitiesAdapter> processCallback, MessageProcessor messageProcessor, MuleEvent event)
                throws Exception
            {
                return processCallback.process(object);
            }

            @Override
            public P execute(ProcessCallback<P, CustomRESTConnectorCapabilitiesAdapter> processCallback, Filter filter, MuleMessage message)
                throws Exception
            {
                return processCallback.process(object);
            }

        }
        ;
    }

}
