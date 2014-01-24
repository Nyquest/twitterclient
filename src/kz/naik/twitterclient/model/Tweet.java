package kz.naik.twitterclient.model;

import java.text.ParseException;
import java.util.Date;

import kz.naik.twitterclient.util.JSONUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class Tweet {
	private Date createdAt;
	private String id;
	private String text;
	private User user;
	
	public Date getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}	
	
	@Override
	public String toString(){
		return "[" + id + "|" + createdAt + "|" + text + "|" + user + "]";
	}
	
	private static String ID = "id";
	private static String CREATED_AT = "created_at";
	private static String TEXT = "text";
	private static String USER = "user";
	
	public static Tweet parse(String json) throws JSONException{
		return parse(new JSONObject(json));
	}
	
	public static Tweet parse(JSONObject jsonObject) throws JSONException{
		Tweet tweet = new Tweet();
		tweet.id = jsonObject.getString(ID);
		String dateText = jsonObject.getString(CREATED_AT);
		try{
			tweet.createdAt = JSONUtils.parseDate(dateText);
		}catch(ParseException e){
			throw new JSONException("Error format date: " + dateText);
		}
		tweet.text = jsonObject.getString(TEXT);
		tweet.user = User.parse(jsonObject.getJSONObject(USER));
		return tweet;
	}
	
}
