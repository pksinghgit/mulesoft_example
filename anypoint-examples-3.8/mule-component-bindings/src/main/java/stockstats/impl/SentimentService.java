/**
 * MuleSoft Examples
 * Copyright 2014 MuleSoft, Inc.
 *
 * This product includes software developed at
 * MuleSoft, Inc. (http://www.mulesoft.com/).
 */

package stockstats.impl;

import java.util.List;

/**
 * Defines an interface for classes that can classify Twitter tweets
 * as positive, negative or neutral.
 */
public interface SentimentService {

	void classify(List<Tweet> tweets);
	
}
