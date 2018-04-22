/**
 * MuleSoft Examples
 * Copyright 2014 MuleSoft, Inc.
 *
 * This product includes software developed at
 * MuleSoft, Inc. (http://www.mulesoft.com/).
 */

package org.mule.examples.test.utils;

import org.mule.tck.probe.Probe;

public class ListenerProbe implements Probe {
	private PipelineSynchronizeListener pipelineListener;

	public ListenerProbe(PipelineSynchronizeListener pipelineListener) {
		super();
		this.pipelineListener = pipelineListener;
	}

	@Override
	public boolean isSatisfied() {
		return pipelineListener.readyToContinue();
	}

	@Override
	public String describeFailure() {
		return "The listener never flaged that the notification of flow completion was received";
	}

}
