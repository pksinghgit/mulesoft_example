/**
 * MuleSoft Examples
 * Copyright 2014 MuleSoft, Inc.
 *
 * This product includes software developed at
 * MuleSoft, Inc. (http://www.mulesoft.com/).
 */

package org.exceptions;

public class MyException extends Exception{

	private static final long serialVersionUID = 1123544523432L;
	String mistake;
	
	public MyException() {
		super();
		mistake = "test";
	}
	
	public MyException(String err) {
		super(err);
		mistake = err;
	}
	
	 public String getError()
	  {
	    return mistake;
	  }

}