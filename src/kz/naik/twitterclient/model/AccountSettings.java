package kz.naik.twitterclient.model;

import org.json.JSONException;
import org.json.JSONObject;

public class AccountSettings {
	
	private String screenName;
	
	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
	
	private static String SCREEN_NAME = "screen_name";
	
	public static AccountSettings parse(String json) throws JSONException{
		JSONObject jsonObject = new JSONObject(json);
		AccountSettings accountSettings = new AccountSettings();
		accountSettings.screenName = jsonObject.getString(SCREEN_NAME);
		return accountSettings;
	}
}
