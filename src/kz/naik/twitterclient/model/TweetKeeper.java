package kz.naik.twitterclient.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

public class TweetKeeper {
	private List<Tweet> tweetList;

	public TweetKeeper(List<Tweet> tweetList) {
		this.tweetList = tweetList;
	}

	public List<Tweet> getTweetList() {
		return tweetList;
	}

	public void setTweetList(List<Tweet> tweetList) {
		this.tweetList = tweetList;
	}
	
	public static TweetKeeper parseArray(String json) throws JSONException{
		JSONArray jsonArray = new JSONArray(json);
		List<Tweet> list = new ArrayList<Tweet>();
		for(int i=0;i<jsonArray.length();i++){
			list.add(Tweet.parse(jsonArray.getJSONObject(i)));
		}
		return new TweetKeeper(list);
	}
	
}
