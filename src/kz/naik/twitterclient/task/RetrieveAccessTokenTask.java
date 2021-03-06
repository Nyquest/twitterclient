package kz.naik.twitterclient.task;

import static kz.naik.twitterclient.Constants.*;
import kz.naik.twitterclient.MainActivity;
import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

public class RetrieveAccessTokenTask extends AsyncTask<Uri, Void, Void>{

	private Context context;
	private OAuthProvider provider;
	private OAuthConsumer consumer;
	private SharedPreferences prefs;
	
	public RetrieveAccessTokenTask(Context context, OAuthProvider provider,
			OAuthConsumer consumer, SharedPreferences prefs) {
		this.context = context;
		this.provider = provider;
		this.consumer = consumer;
		this.prefs = prefs;
	}



	@Override
	protected Void doInBackground(Uri... params) {
		final Uri uri = params[0];
		final String oauth_verifier = uri.getQueryParameter(OAuth.OAUTH_VERIFIER);
		Log.d(TAG, "oauth_verifier = " + oauth_verifier);
		try{
			provider.retrieveAccessToken(consumer, oauth_verifier);
			final Editor edit = prefs.edit();
			edit.putString(OAuth.OAUTH_TOKEN, consumer.getToken());
			edit.putString(OAuth.OAUTH_TOKEN_SECRET, consumer.getTokenSecret());
			
			Log.d(TAG, "put " + OAuth.OAUTH_TOKEN + " " + consumer.getToken());
			Log.d(TAG, "put " + OAuth.OAUTH_TOKEN_SECRET + " " + consumer.getTokenSecret());
			
			edit.commit();
			
			String token = prefs.getString(OAuth.OAUTH_TOKEN, "");
			String secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");
			
			Log.d(TAG, "token = " + token);
			Log.d(TAG, "secret = " + secret);
			
			consumer.setTokenWithSecret(token, secret);
			context.startActivity(new Intent(context, MainActivity.class));
			
			Log.i(TAG, "OAuth - Access Token Retrieved");
			
			
		}catch(Throwable tr){
			Log.e(TAG, "OAuth - Access Token Retrieval Error", tr);
		}
		return null;
	}

}
