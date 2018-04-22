package com.test;

import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;

import com.google.protobuf.Message;

public class Person implements Callable {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {
		
		return null;
	}

}
