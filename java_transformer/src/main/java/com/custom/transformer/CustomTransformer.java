package com.custom.transformer;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.api.transport.PropertyScope;
import org.mule.transformer.AbstractMessageTransformer;

public class CustomTransformer extends AbstractMessageTransformer {

	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {
		// TODO Auto-generated method stub
		String name = message.getProperty("name", PropertyScope.INVOCATION);
		CustomerDetails customerDetails = new CustomerDetails();
		customerDetails.setName(name);
		customerDetails.setBankName("sbi");
		return "Hello " + name;
	}

}
