/**
 * MuleSoft Examples
 * Copyright 2014 MuleSoft, Inc.
 *
 * This product includes software developed at
 * MuleSoft, Inc. (http://www.mulesoft.com/).
 */

package stockstats.impl.mule;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.processor.MessageProcessor;

/**
 * A Mule custom transformer that sets Mule Message invocation properties
 * to be used by the Twitter cloud connector.
 */
public class TwitterRequestProcessor implements MessageProcessor {

	@Override
	public MuleEvent process(MuleEvent event) throws MuleException {
		
		MuleMessage msg = event.getMessage();
		
		Object[] args = msg.getPayload(Object[].class);
		msg.setInvocationProperty("stock", "$" + args[0]);
		Date since = (Date) args[1];
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		msg.setInvocationProperty("since", df.format(since));
		Calendar cal = Calendar.getInstance();
		cal.setTime(since);
		cal.add(Calendar.DATE, 10);
		msg.setInvocationProperty("until", df.format(cal.getTime()));
		msg.setInvocationProperty("rpp", args[2]);
		msg.setInvocationProperty("page", args[3]);
		
		return event;
	}

}
