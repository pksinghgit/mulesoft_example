/**
 * MuleSoft Examples
 * Copyright 2014 MuleSoft, Inc.
 *
 * This product includes software developed at
 * MuleSoft, Inc. (http://www.mulesoft.com/).
 */

package stockstats;

import stockstats.impl.Tweet;

public class SocialStats {

	private int tweetsLoaded;
	private boolean moreTweets;
	private int negativeTweets;
	private int neutralTweets;
	private int positiveTweets;
	public boolean isMoreTweets() {
		return moreTweets;
	}
	public void setMoreTweets(boolean moreTweets) {
		this.moreTweets = moreTweets;
	}
	public int getTweetsLoaded() {
		return tweetsLoaded;
	}
	public int getNegativeTweets() {
		return negativeTweets;
	}
	public int getNeutralTweets() {
		return neutralTweets;
	}
	public int getPositiveTweets() {
		return positiveTweets;
	}
	public void addTweet(Tweet tweet) {
		tweetsLoaded++;
		if (tweet.getSentiment() == Sentiment.NEGATIVE) {
			negativeTweets++;
		} else if (tweet.getSentiment() == Sentiment.NEUTRAL) {
			neutralTweets++;
		} else {
			positiveTweets++;
		}
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (moreTweets ? 1231 : 1237);
		result = prime * result + negativeTweets;
		result = prime * result + neutralTweets;
		result = prime * result + positiveTweets;
		result = prime * result + tweetsLoaded;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SocialStats other = (SocialStats) obj;
		if (moreTweets != other.moreTweets)
			return false;
		if (negativeTweets != other.negativeTweets)
			return false;
		if (neutralTweets != other.neutralTweets)
			return false;
		if (positiveTweets != other.positiveTweets)
			return false;
		if (tweetsLoaded != other.tweetsLoaded)
			return false;
		return true;
	}
	
	
}
