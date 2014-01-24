package kz.naik.twitterclient;

import static kz.naik.twitterclient.Constants.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import kz.naik.twitterclient.common.HttpMethod;
import kz.naik.twitterclient.common.IInputTextHandler;
import kz.naik.twitterclient.common.IResponseWrapperHandler;
import kz.naik.twitterclient.common.RequestWrapper;
import kz.naik.twitterclient.common.ResponseWrapper;
import kz.naik.twitterclient.common.WrapperType;
import kz.naik.twitterclient.dialog.InputTextDialog;
import kz.naik.twitterclient.model.AccountSettings;
import kz.naik.twitterclient.model.Tweet;
import kz.naik.twitterclient.model.TweetKeeper;
import kz.naik.twitterclient.model.User;
import kz.naik.twitterclient.task.JSONRequestTask;
import kz.naik.twitterclient.util.Utils;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements IResponseWrapperHandler,IInputTextHandler{

	private SharedPreferences prefs;
	private TextView tvUser;
	private TextView tvTweets;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, getClass().getSimpleName() + ".onCreate");
        this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
        
        tvUser = (TextView)findViewById(R.id.tv_user);
        tvTweets = (TextView)findViewById(R.id.tv_tweets);        
        
		tvTweets.setMovementMethod(LinkMovementMethod.getInstance());
		tvUser.setMovementMethod(LinkMovementMethod.getInstance());
		
        Button btnTweet = (Button) findViewById(R.id.btn_tweet);
        Button btnExit = (Button) findViewById(R.id.btn_exit);
        
        btnTweet.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.i(TAG, "btnTweet.onClick");
				InputTextDialog inputTextDialog = new InputTextDialog(MainActivity.this,MainActivity.this);
				inputTextDialog.callDialog();
			}
		});

        btnExit.setOnClickListener(new OnClickListener() {
        	
        	@Override
        	public void onClick(View v) {
        		Log.i(TAG, "btnExit.onClick");
        		Utils.unlogin(prefs);
        		showTweets();
        	}
        	
        });

        
        final Uri uri = getIntent().getData();
    	if(uri != null && uri.getScheme().equals(TWEETS_CALLBACK_SCHEME)){
    		Log.d(TAG, TWEETS_CALLBACK_SCHEME);
    		String userName = uri.getHost();    		
    		Log.d(TAG, "User name: " + userName);
    		showTweets(userName);
    		return;
    	}
    	showTweets();
        
    }

    private void showTweets(){
    	Log.i(TAG, "showTweets()");		
    	
		if(isAuthenticated()){
			RequestWrapper requestWrapper = new RequestWrapper(WrapperType.ACCOUNT_SETTINGS,
					HttpMethod.GET, "https://api.twitter.com/1.1/account/settings.json");
			new JSONRequestTask(Utils.getConsumer(prefs),this).execute(requestWrapper);
		}
    }
    
    private void showTweets(String userName){
    	Log.i(TAG, "showTweets(" + userName + ")");
    	showUserName(userName);
    	if(isAuthenticated()){
    		RequestWrapper requestWrapper = new RequestWrapper(WrapperType.TWEETS, HttpMethod.GET,
    				"https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=" + userName);
    		new JSONRequestTask(Utils.getConsumer(prefs),this).execute(requestWrapper);    		
    	}
    }
    
    private boolean isAuthenticated(){
    	if(!isOnline()) return false;
    	if(!Utils.isAuthenticated(prefs)){
    		Log.e(TAG, "Пользователь не авторизован");
    		Intent i = new Intent(getApplicationContext(), PrepareRequestTokenActivity.class);
			startActivity(i);
			return false;
    	}
    	return true; 
    }
    
    private boolean isOnline(){
    	Log.i(TAG, this.getClass().getSimpleName() + ".isOnline");
    	boolean isOnline = Utils.isOnline(this); 
    	Log.d(TAG, isOnline ? "Online" : "No internet");
    	if(!isOnline){
    		Toast.makeText(getBaseContext(), getResources().getString(R.string.internet_connection_is_not_available), Toast.LENGTH_LONG).show();
    	}
    	return true;	
    }
    
	@Override
	public void responseWrapperHandler(ResponseWrapper responseWrapper) {
		String json = responseWrapper.getJson();
		switch(responseWrapper.getWrapperType()){
		case ACCOUNT_SETTINGS:
			try {
				AccountSettings accountSettings = AccountSettings.parse(json);
				String userName = accountSettings.getScreenName();
				Log.d(TAG, "Current user: " + userName);
				showTweets(userName);
			} catch (JSONException e) {
				Log.e(TAG, "Error parsing json", e);
			}
			break;
		case TWEETS:			
			StringBuilder sb = new StringBuilder();
			TweetKeeper tweetKeeper;
			try{
				tweetKeeper = TweetKeeper.parseArray(json);
			}catch(Exception e){
				Log.e(TAG, "Error in TweetKeeper.parseArray",e);
				return;
			}
			
			for(Tweet tweet : tweetKeeper.getTweetList()){
				Log.d(TAG, "tweet = " + tweet);
				User user = tweet.getUser();
				String name = user.getName(); 
				String screenName = tweet.getUser().getScreenName();
				sb.append(name)
				.append(" ").append(AT).append(screenName).append(BR)
				.append(tweet.getText()).append(BR).append(BR);
			}
			
			tvTweets.setText(Html.fromHtml(Utils.addHyperLink(sb.toString())));
			
			break;
		case NEW_TWEET:
			showTweets();			
			break;
		default:
			throw new RuntimeException("Handler is not defined");	
		}
		
	}

	@Override
	public void responseWrapperHandlerMessage(int stringId) {
		Log.d(TAG, this.getClass().getSimpleName() + ".responseWrapperHandlerMessage");
		Toast.makeText(getBaseContext(), getResources().getString(stringId), Toast.LENGTH_LONG).show();
	}

	@Override
	public void inputTextHandler(String message) {
		Log.i(TAG, this.getClass().getSimpleName() + ".inputTextHandler");
		Log.d(TAG, "message = " + message);
		RequestWrapper requestWrapper;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("status", message));
		requestWrapper = new RequestWrapper(WrapperType.NEW_TWEET,HttpMethod.POST, 
				"https://api.twitter.com/1.1/statuses/update.json",params);
		new JSONRequestTask(Utils.getConsumer(prefs),this).execute(requestWrapper);
	}
	
	private void showUserName(String userName){
		tvUser.setText(Html.fromHtml(Utils.addHyperLink(AT + userName)));
	}
	
}
