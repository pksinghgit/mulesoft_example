package org.mule.modules.customsdkconnectorr;

import org.mule.api.annotations.Config;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;

import org.mule.modules.customsdkconnectorr.config.ConnectorConfig;

@Connector(name = "custom-sdk-connectorr", friendlyName = "CustomSDKConnectorr")
public class CustomSDKConnectorrConnector {

	@Config
	ConnectorConfig config;

	/**
	 * Custom processor
	 *
	 * @param friend
	 *            Name to be used to generate a greeting message.
	 * @return A greeting message
	 */
	/**
	 * @param value1
	 * @param value2
	 * @param actions
	 * @return
	 */
	@Processor
	public String getCalculator(int value1, int value2, String actions) {
		String finalValue = "";
		if (actions.equalsIgnoreCase("add")) {
			finalValue = "" + (value2 + value1);
		} else if (actions.equalsIgnoreCase("sub")) {
			finalValue = "" + (value1 - value2);
		}
		return finalValue;
	}

	public ConnectorConfig getConfig() {
		return config;
	}

	public void setConfig(ConnectorConfig config) {
		this.config = config;
	}

}