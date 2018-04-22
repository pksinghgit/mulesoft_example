/**
 * MuleSoft Examples
 * Copyright 2014 MuleSoft, Inc.
 *
 * This product includes software developed at
 * MuleSoft, Inc. (http://www.mulesoft.com/).
 */

package stockstats.impl.sentiment140;

import java.util.List;


public class BulkClassifyRequest {
	private List<ClassifyRequest> data;

	public List<ClassifyRequest> getData() {
		return data;
	}

	public void setData(List<ClassifyRequest> data) {
		this.data = data;
	}

}
