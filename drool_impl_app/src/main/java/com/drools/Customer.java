package com.drools;

import com.ibm.icu.math.BigDecimal;

public class Customer {
	private String name;
	private int age;
	private BigDecimal purchage;
	private String customerType;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public BigDecimal getPurchage() {
		return purchage;
	}

	public void setPurchage(BigDecimal purchage) {
		this.purchage = purchage;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

}
