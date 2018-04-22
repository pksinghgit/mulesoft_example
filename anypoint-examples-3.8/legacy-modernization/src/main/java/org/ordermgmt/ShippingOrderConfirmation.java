/**
 * MuleSoft Examples
 * Copyright 2014 MuleSoft, Inc.
 *
 * This product includes software developed at
 * MuleSoft, Inc. (http://www.mulesoft.com/).
 */

package org.ordermgmt;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ShippingOrderConfirmation {

	public ShippingOrder shippingOrder;
	public boolean orderReceivedStatus;
	
	public ShippingOrderConfirmation(){
		// default no-argument constructor
	}
	
	public ShippingOrderConfirmation(ShippingOrder order, boolean status){
		// initialize ShippingOrderConfirmation
		setShippingOrder(order);
		setOrderReceivedStatus(status);		
	}
	
	@XmlTransient
	public ShippingOrder getShippingOrder(){
		return this.shippingOrder;
	}
	
	public void setShippingOrder(ShippingOrder order){
		this.shippingOrder = order;
	}
	
	@XmlTransient
	public boolean getOrderReceivedStatus(){
		return this.orderReceivedStatus;
	}
	
	public void setOrderReceivedStatus(boolean status){
		this.orderReceivedStatus = status;
	}	
}
