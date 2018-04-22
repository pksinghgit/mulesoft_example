
package org.mule.modules.restconnectorcustom.generated.adapters;

import javax.annotation.Generated;
import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.api.devkit.ProcessAdapter;
import org.mule.api.devkit.ProcessTemplate;
import org.mule.api.processor.MessageProcessor;
import org.mule.api.routing.filter.Filter;
import org.mule.modules.restconnectorcustom.RestConnectorCustomConnector;
import org.mule.security.oauth.callback.ProcessCallback;


/**
 * A <code>RestConnectorCustomConnectorProcessAdapter</code> is a wrapper around {@link RestConnectorCustomConnector } that enables custom processing strategies.
 * 
 */
@SuppressWarnings("all")
@Generated(value = "Mule DevKit Version 3.9.0", date = "2018-02-20T05:08:25+05:30", comments = "Build UNNAMED.2793.f49b6c7")
public abstract class RestConnectorCustomConnectorProcessAdapter
    extends RestConnectorCustomConnectorLifecycleInjectionAdapter
    implements ProcessAdapter<RestConnectorCustomConnectorCapabilitiesAdapter>
{


    public<P >ProcessTemplate<P, RestConnectorCustomConnectorCapabilitiesAdapter> getProcessTemplate() {
        final RestConnectorCustomConnectorCapabilitiesAdapter object = this;
        return new ProcessTemplate<P,RestConnectorCustomConnectorCapabilitiesAdapter>() {


            @Override
            public P execute(ProcessCallback<P, RestConnectorCustomConnectorCapabilitiesAdapter> processCallback, MessageProcessor messageProcessor, MuleEvent event)
                throws Exception
            {
                return processCallback.process(object);
            }

            @Override
            public P execute(ProcessCallback<P, RestConnectorCustomConnectorCapabilitiesAdapter> processCallback, Filter filter, MuleMessage message)
                throws Exception
            {
                return processCallback.process(object);
            }

        }
        ;
    }

}
