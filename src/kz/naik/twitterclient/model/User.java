package kz.naik.twitterclient.model;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
	private String id;
	private String name;
	private String screenName;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getScreenName() {
		return screenName;
	}
	
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
	
	@Override
	public String toString(){
		return "[" + id + "|" + name + "|" + screenName + "]";
	}
	
	private static String ID = "id";
	private static String NAME = "name";
	private static String SCREEN_NAME = "screen_name";
	
	
	public static User parse(String json) throws JSONException{
		return parse(new JSONObject(json));
	}
	
	public static User parse(JSONObject jsonObject) throws JSONException{
		User user = new User();
		user.id = jsonObject.getString(ID);
		user.name = jsonObject.getString(NAME);
		user.screenName = jsonObject.getString(SCREEN_NAME);
		return user;
	}
	
}
