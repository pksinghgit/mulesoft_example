/**
 * MuleSoft Examples
 * Copyright 2014 MuleSoft, Inc.
 *
 * This product includes software developed at
 * MuleSoft, Inc. (http://www.mulesoft.com/).
 */

package stockstats.impl.mule;

import java.util.ArrayList;
import java.util.List;

import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.processor.MessageProcessor;

import stockstats.impl.Tweet;
import twitter4j.QueryResult;

/**
 * A Mule custom transformer that transforms the Mule Twitter cloud connector response
 * into the TwitterService.search response type.
 */
public class TwitterResponseProcessor implements MessageProcessor {
	
	
	@Override
	public MuleEvent process(MuleEvent event) throws MuleException {
		
		MuleMessage msg = event.getMessage();
		
		List<Tweet> tweetList = new ArrayList<Tweet>();
		
		
		QueryResult queryResult = (QueryResult) msg.getPayload();
		
		for (twitter4j.Status t: queryResult.getTweets()) {
			Tweet tweet = new Tweet();
			tweet.setId(t.getId()+"");
			tweet.setText(t.getText());
			tweetList.add(tweet);
		}
		msg.setPayload(tweetList);
		
		return event;
	}

}
