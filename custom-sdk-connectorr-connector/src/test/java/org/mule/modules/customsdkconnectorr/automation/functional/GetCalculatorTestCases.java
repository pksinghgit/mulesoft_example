package org.mule.modules.customsdkconnectorr.automation.functional;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.customsdkconnectorr.CustomSDKConnectorrConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class GetCalculatorTestCases extends AbstractTestCase<CustomSDKConnectorrConnector> {

	public GetCalculatorTestCases() {
		super(CustomSDKConnectorrConnector.class);
	}

	@Before
	public void setup() {
		// TODO
	}

	@After
	public void tearDown() {
		// TODO
	}

	@Test
	public void verify() {
		java.lang.String expected = null;
		int value1=1;
		int value2=2;
		java.lang.String actions = null;
		assertEquals(getConnector().getCalculator(value1, value2, actions), expected);
	}

}