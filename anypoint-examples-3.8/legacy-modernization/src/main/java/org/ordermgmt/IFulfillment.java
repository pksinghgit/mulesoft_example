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

@WebService
public interface IFulfillment {

	/**
	 * 
	 * @param shippingId
	 * @param order
	 * @return
	 */
	
	@WebResult(name="ShippingOrderConfirmation")
	public ShippingOrderConfirmation putShippingOrder(@WebParam(name="shippingId") String shippingId,
													  @WebParam(name="billingAddress")  Address billingAddress,
													  @WebParam(name="shippingAddress")  Address shippingAddress,
			 										  @WebParam(name="order")  Order order);
	
}
