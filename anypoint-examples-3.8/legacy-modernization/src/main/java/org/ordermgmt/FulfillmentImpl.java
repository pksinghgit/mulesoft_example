/**
 * MuleSoft Examples
 * Copyright 2014 MuleSoft, Inc.
 *
 * This product includes software developed at
 * MuleSoft, Inc. (http://www.mulesoft.com/).
 */

package org.ordermgmt;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService(endpointInterface = "com.mule.example.legacy.IFulfillment", serviceName = "Fulfillment")
public class FulfillmentImpl implements IFulfillment{
	
	public ShippingOrder shippingOrder;
	public ShippingOrderConfirmation shippingOrderConfirmation;
	
	@Override
	@WebResult(name="ShippingOrderConfirmation")
	public ShippingOrderConfirmation putShippingOrder(@WebParam(name="shippingId") String shippingId,
			  										  @WebParam(name="billingAddress")  Address billingAddress,
			  										  @WebParam(name="shippingAddress")  Address shippingAddress,
			  										  @WebParam(name="order")  Order order){

		try {
			shippingOrder = new ShippingOrder(shippingId, billingAddress, shippingAddress, order);
			shippingOrderConfirmation = new ShippingOrderConfirmation(shippingOrder,true);
		} catch(Exception e)
		
		{
			e.printStackTrace();
		}

		return shippingOrderConfirmation;
	}
}
