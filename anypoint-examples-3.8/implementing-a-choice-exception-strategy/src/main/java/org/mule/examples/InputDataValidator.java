/**
 * MuleSoft Examples
 * Copyright 2014 MuleSoft, Inc.
 *
 * This product includes software developed at
 * MuleSoft, Inc. (http://www.mulesoft.com/).
 */

package org.mule.examples;

import java.util.Map;

import org.mule.api.MuleMessage;
import org.mule.api.routing.filter.Filter;

public class InputDataValidator implements Filter {

	@Override
	public boolean accept(MuleMessage message) {
		Map<String, Object> payloadMap = (Map<String, Object>) message.getPayload();
		
		if (!payloadMap.containsKey("email")) 
			throw new NullPointerException("email is missing");	
		if (Integer.parseInt(payloadMap.get("item units").toString()) < 1)
			throw new IllegalArgumentException("item units is negative");
		if (Integer.parseInt(payloadMap.get("item price per unit").toString()) < 0)
			throw new IllegalArgumentException("item price per unit is negative");
		
		return true;
	}

}
