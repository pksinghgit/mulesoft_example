/**
 * MuleSoft Examples
 * Copyright 2014 MuleSoft, Inc.
 *
 * This product includes software developed at
 * MuleSoft, Inc. (http://www.mulesoft.com/).
 */

package org.mule.examples;

import org.exceptions.MyException;
import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;

public class TestComponent implements Callable{

	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {
		// TODO Auto-generated method stub
		throw new MyException("exception customized");
		//return eventContext;
	}

}
