package kz.naik.twitterclient;

import java.util.regex.Pattern;

public interface Constants {
	String TAG = "TW";
	String CONSUMER_KEY = "z5OGTa2snjA9auNce5tykQ";
	String CONSUMER_SECRET = "gnmZiit5RJhQFsdEMCwzbN0pRF17lMN3FXql1pqDKg";
	String REQUEST_TOKEN_URL = "https://api.twitter.com/oauth/request_token";
	String AUTHORIZE_URL = "https://api.twitter.com/oauth/authorize";
	String ACCESS_TOKEN_URL = "https://api.twitter.com/oauth/access_token";
	
	String SCHEME_DELIM = "://";
	
	String OAUTH_CALLBACK_SCHEME = "x-oauthflow-twitter";
	String OAUTH_CALLBACK_HOST = "callback";
	String OAUTH_CALLBACK_URL = OAUTH_CALLBACK_SCHEME + SCHEME_DELIM + OAUTH_CALLBACK_HOST;
	
	String TWEETS_CALLBACK_SCHEME = "x-tweets-twitter";
	
	String UTF_8 = "UTF-8";
	String EOL = "\n";
	String AT = "@";
	String BR = "<br/>";
	
}
