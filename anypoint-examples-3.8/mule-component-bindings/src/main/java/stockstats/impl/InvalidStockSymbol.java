/**
 * MuleSoft Examples
 * Copyright 2014 MuleSoft, Inc.
 *
 * This product includes software developed at
 * MuleSoft, Inc. (http://www.mulesoft.com/).
 */

package stockstats.impl;

public class InvalidStockSymbol extends Exception {

	private static final long serialVersionUID = 2222417175418465035L;

	public InvalidStockSymbol() {
		super();
	}

	public InvalidStockSymbol(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidStockSymbol(String message) {
		super(message);
	}

	public InvalidStockSymbol(Throwable cause) {
		super(cause);
	}
	
}
