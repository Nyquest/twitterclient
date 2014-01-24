package kz.naik.twitterclient.util;

import static kz.naik.twitterclient.Constants.*;
import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.util.Patterns;

public class Utils {
	
	public static boolean isAuthenticated(SharedPreferences prefs){
		Log.i(TAG, "Utils.isAuthenticated");
		OAuthConsumer consumer = getConsumer(prefs);
		if(consumer.getToken().length() > 0 && consumer.getTokenSecret().length() > 0){
			return true;
		}
		return false;
	}
	
	public static OAuthConsumer getConsumer(SharedPreferences prefs){
		OAuthConsumer consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
		String token = prefs.getString(OAuth.OAUTH_TOKEN, "");
		String secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");
		Log.d(TAG, "token = " + token);
		Log.d(TAG, "secret = " + secret);
		consumer.setTokenWithSecret(token, secret);
		return consumer;
	}
	
	public static void unlogin(SharedPreferences prefs){
		final Editor editor = prefs.edit();
		editor.remove(OAuth.OAUTH_TOKEN);
		editor.remove(OAuth.OAUTH_TOKEN_SECRET);
		editor.commit();
	}
	
	public static boolean isOnline(Context context){
		ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo nInfo = cm.getActiveNetworkInfo();
		if(nInfo != null && nInfo.isConnected()){
			Log.v("status", "ONLINE");
			return true;
		}else{
			Log.v("status","ONLINE");
			return false;
		}
	}
	
	public static String addHyperLink(String html){
		return html
				.replaceAll("@([A-Za-z0-9_-]+)", "<a href='" + TWEETS_CALLBACK_SCHEME + SCHEME_DELIM + "$1'>$0</a>")
				.replaceAll(Patterns.WEB_URL.toString(), "<a href='$0'>$0</a>");
				
	}
	
}
