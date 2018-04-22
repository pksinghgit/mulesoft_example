package com.calculator;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface CalculatorService {
	@WebMethod
	public int add(int a, int b);

	@WebMethod
	public int sub(int a, int b);

	@WebMethod
	public int mul(int a, int b);

	@WebMethod
	public int dev(int a, int b);

}
