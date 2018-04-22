/**
 * MuleSoft Examples
 * Copyright 2014 MuleSoft, Inc.
 *
 * This product includes software developed at
 * MuleSoft, Inc. (http://www.mulesoft.com/).
 */

package org.mule.examples.transformers;

import java.util.List;
import java.util.Map;

import org.mule.DefaultMuleEvent;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.routing.AggregationContext;
import org.mule.routing.AggregationStrategy;

/**
 * This transformer will take to list as input and create a third one that will be the merge of the previous two. The identity of an element of the list is defined by its email.
 */
public class ContactMergeAggregationStrategy implements AggregationStrategy {
	
	@Override
	public MuleEvent aggregate(AggregationContext context) throws MuleException {
		List<MuleEvent> muleEventsWithoutException = context.collectEventsWithoutExceptions();
		int muleEventsWithoutExceptionCount = muleEventsWithoutException.size();
		
		// data should be loaded from both sources (A and B)
		if (muleEventsWithoutExceptionCount != 2) {
			throw new IllegalStateException("Data from at least one source was not able to be obtained correctly.");
		}
		
		// mule event that will be rewritten
		MuleEvent originalEvent = context.getOriginalEvent();
		// message which payload will be rewritten
		MuleMessage message = originalEvent.getMessage();
		
		// events are ordered so the event index corresponds to the index of each route
		List<Map<String, String>> listA = getContactsList(muleEventsWithoutException, 0);
		List<Map<String, String>> listB = getContactsList(muleEventsWithoutException, 1);

		ContactMerge contactMerge = new ContactMerge();
		List<Map<String, String>> mergedContactList = (List<Map<String, String>>) contactMerge.mergeList(listA, listB);

		message.setPayload(mergedContactList.iterator());

		return new DefaultMuleEvent(message, originalEvent);
	}

	private List<Map<String, String>> getContactsList(List<MuleEvent> events, int index) {
		return (List<Map<String, String>>) events.get(index).getMessage().getPayload();
	}

}
