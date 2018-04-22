/**
 * MuleSoft Examples
 * Copyright 2014 MuleSoft, Inc.
 *
 * This product includes software developed at
 * MuleSoft, Inc. (http://www.mulesoft.com/).
 */

package org.ordermgmt;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
public class OrderItem {

	public String merchantSKU;
	public int quantity;

	public OrderItem(){
		//default no-arg constructor
	}
	
	public OrderItem(String merchantSKU, int quantity){
		setMerchantSKU(merchantSKU);
		setQuantity(quantity);
	}
	
	@XmlTransient
	public String getMerchantSKU(){
		return this.merchantSKU;
	}
	
	public void setMerchantSKU(String merchantSKU){
		this.merchantSKU = merchantSKU;
	}
	
	@XmlTransient
	public int getQuantity(){
		return this.quantity;
	}
	
	public void setQuantity(int quantity){
		this.quantity = quantity;
	}
}
