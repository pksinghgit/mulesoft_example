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
public class Address {

	public String name;
	public String line1;
	public String line2;
	public String city;
	public String stateOrProvinceCode;
	public String countryCode;
	public String postalCode;

	public Address(){
		//no argument default constructor
	}
	
	public Address(String name, String line1, String line2, String city, String stateOrProvinceCode, String countryCode, String postalCode){
		this.setName(name);
		this.setLine1(line1);
		this.setLine2(line2);
		this.setCity(city);
		this.setStateOrProvinceCode(stateOrProvinceCode);
		this.setCountryCode(countryCode);
		this.setPostalCode(postalCode);
	}
	
	@XmlTransient
	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.name = name;
	}

	@XmlTransient
	public String getLine1(){
		return this.line1;
	}
	
	public void setLine1(String line1){
		this.line1 = line1;
	}

	@XmlTransient
	public String getLine2(){
		return this.line2;
	}
	
	public void setLine2(String line2){
		this.line2 = line2;
	}

	@XmlTransient
	public String getCity(){
		return this.city;
	}
	
	public void setCity(String city){
		this.city = city;
	}

	@XmlTransient
	public String getStateOrProvinceCode(){
		return this.stateOrProvinceCode;
	}
	
	public void setStateOrProvinceCode(String stateOrProvinceCode){
		this.stateOrProvinceCode = stateOrProvinceCode;
	}
		
	@XmlTransient
	public String getCountryCode(){
		return this.countryCode;
	}
	
	public void setCountryCode(String countryCode){
		this.countryCode = countryCode;
	}

	@XmlTransient
	public String getPostalCode(){
		return this.postalCode;
	}
	
	public void setPostalCode(String postalCode){
		this.postalCode = postalCode;
	}
	
}
