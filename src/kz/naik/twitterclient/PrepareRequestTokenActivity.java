package kz.naik.twitterclient;

import static kz.naik.twitterclient.Constants.*;
import kz.naik.twitterclient.task.OAuthRequestTokenTask;
import kz.naik.twitterclient.task.RetrieveAccessTokenTask;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

public class PrepareRequestTokenActivity extends Activity {
	private OAuthConsumer consumer;
	private OAuthProvider provider;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        
        Log.d(TAG, getClass().getSimpleName() + ".onCreate");
        try{
	        consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
	        provider = new CommonsHttpOAuthProvider(REQUEST_TOKEN_URL, ACCESS_TOKEN_URL, AUTHORIZE_URL);
        }catch(Throwable t){
        	Log.e(TAG, "Error creating consumer / provider",t);
        }
        
        Log.i(TAG, "Starting async task to retrieve request token.");
        new OAuthRequestTokenTask(this, consumer, provider).execute();
        
    }
    
    @Override
    public void onNewIntent(Intent intent){
    	super.onNewIntent(intent);
    	Log.i(TAG, this.getClass().getSimpleName() + ".onNewIntent");
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    	final Uri uri = intent.getData();
    	if(uri != null && uri.getScheme().equals(OAUTH_CALLBACK_SCHEME)){
    		Log.i(TAG, "Callback received: " + uri);
    		Log.i(TAG, "Retrieving Access Token");
    		new RetrieveAccessTokenTask(this, provider, consumer, prefs).execute(uri);
    		finish();
    		
    	}
    }
}
