/**
 * MuleSoft Examples
 * Copyright 2014 MuleSoft, Inc.
 *
 * This product includes software developed at
 * MuleSoft, Inc. (http://www.mulesoft.com/).
 */

package stockstats.impl.sentiment140;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import stockstats.Sentiment;
import stockstats.impl.SentimentService;
import stockstats.impl.Tweet;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * A SentimentService implementation that delegates to the Sentiment140 cloud API.
 */
public class Sentiment140SentimentService implements SentimentService {
	
	private String appId;
	private Client client;
	private static final String BASE_URL = "http://www.sentiment140.com/api/";
	
	public Sentiment140SentimentService(String appId) {
		this.appId = appId;
		client = ClientBuilder.newClient();
	}

	@Override
	public void classify(List<Tweet> tweets) {
		
		WebTarget webTarget = client.target(BASE_URL + "bulkClassifyJson");
		
		BulkClassifyRequest bulkRequest = new BulkClassifyRequest();
		Map<String,Tweet> idToTweet = new HashMap<String, Tweet>();
		List<ClassifyRequest> data = new ArrayList<ClassifyRequest>();
		for (Tweet tweet: tweets) {
			ClassifyRequest request = new ClassifyRequest();
			request.setId(tweet.getId());
			request.setText(tweet.getText());
			data.add(request);
			idToTweet.put(tweet.getId(), tweet);
		}
		bulkRequest.setData(data);
		
		Response response = webTarget.request(MediaType.APPLICATION_JSON)
				.header("appid", appId)
				.post(Entity.text(bulkRequest));
		
		if (response.getStatus() != Response.Status.OK.getStatusCode()) {
			throw new RuntimeException("Sentiment140 returned status " + response.getStatus());
		}
		
		String jsonStr = response.getEntity().toString();
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.readTree(jsonStr);
			JsonNode dataNode = rootNode.path("data");
			for (Iterator<JsonNode> i = dataNode.getElements(); i.hasNext();) {
				JsonNode resultNode = i.next();
				String id = resultNode.path("id").getTextValue();
				int polarity = resultNode.path("polarity").getIntValue();
				
				Sentiment sentiment;
				if (polarity == 0) {
					sentiment = Sentiment.NEGATIVE;
				} else if (polarity == 2) {
					sentiment = Sentiment.NEUTRAL;
				} else {
					sentiment = Sentiment.POSITIVE;
				}
				
				Tweet tweet = idToTweet.get(id);
				tweet.setSentiment(sentiment);
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}

}
