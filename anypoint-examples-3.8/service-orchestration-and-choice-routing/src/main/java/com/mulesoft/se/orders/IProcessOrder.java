/**
 * MuleSoft Examples
 * Copyright 2014 MuleSoft, Inc.
 *
 * This product includes software developed at
 * MuleSoft, Inc. (http://www.mulesoft.com/).
 */

package com.mulesoft.se.orders;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

/**
 * Interface for order processing.
 * 
 * @author Derek
 */
@WebService
public interface IProcessOrder {
	/**
	 * Process an order.
	 * 
	 * @param order
	 * @return
	 */
	@WebResult(name = "summary")
	Order processOrder(@WebParam(name = "order") Order order);
}
