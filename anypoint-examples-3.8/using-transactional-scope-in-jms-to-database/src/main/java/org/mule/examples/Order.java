/**
 * MuleSoft Examples
 * Copyright 2014 MuleSoft, Inc.
 *
 * This product includes software developed at
 * MuleSoft, Inc. (http://www.mulesoft.com/).
 */

package org.mule.examples;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Order {

	private int itemId;
	private int itemUnits;
	private int customerId;

	public int getItemId() {
		return itemId;
	}

	public int getItemUnits() {
		return itemUnits;
	}

	public int getCustomerId() {
		return customerId;
	}

	@Override
	public String toString() {
		return "Order [item_id=" + itemId + ", item_units=" + itemUnits
				+ ", customer_id=" + customerId + "]";
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public void setItemUnits(int itemUnits) {
		this.itemUnits = itemUnits;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

		
	
}
