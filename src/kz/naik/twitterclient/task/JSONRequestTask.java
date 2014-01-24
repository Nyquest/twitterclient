package kz.naik.twitterclient.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import kz.naik.twitterclient.R;
import kz.naik.twitterclient.common.HttpMethod;
import kz.naik.twitterclient.common.IResponseWrapperHandler;
import kz.naik.twitterclient.common.RequestWrapper;
import kz.naik.twitterclient.common.ResponseWrapper;
import kz.naik.twitterclient.common.WrapperType;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;

import static kz.naik.twitterclient.Constants.*;
import oauth.signpost.OAuthConsumer;
import android.os.AsyncTask;
import android.util.Log;

public class JSONRequestTask extends AsyncTask<RequestWrapper, Void, ResponseWrapper>{

	private OAuthConsumer consumer;
	private IResponseWrapperHandler handler;
	
	public JSONRequestTask(OAuthConsumer consumer,IResponseWrapperHandler handler) {
		this.consumer = consumer;
		this.handler = handler;
	}

	@Override
	protected ResponseWrapper doInBackground(RequestWrapper... params) {
		Log.d(TAG, this.getClass().getSimpleName() + ".doInBackground");
		RequestWrapper requestWrapper = params[0];  
		WrapperType wrapperType = requestWrapper.getWrapperType();
		String uri = requestWrapper.getUri();
		HttpMethod httpMethod = requestWrapper.getHttpMethod();
		Log.d(TAG, "wrapperType = " + wrapperType);
		Log.d(TAG, "httpMethod = " + httpMethod);
		Log.d(TAG, "uri = " + uri);
		HttpRequestBase httpRequest = null;
		if(httpMethod == HttpMethod.GET){
			httpRequest = new HttpGet(uri);
		}else{
			httpRequest = new HttpPost(uri);
			List<NameValuePair> parameterList = requestWrapper.getParameterList();
			if(parameterList != null){
				HttpPost httpPost = (HttpPost)httpRequest;
				try {
					httpPost.setEntity(new UrlEncodedFormEntity(parameterList,UTF_8));
				} catch (UnsupportedEncodingException e) {
					Log.e(TAG, "Unsupported encoding", e);
				}
			}
		}
		
		try {
			consumer.sign(httpRequest);			
		} catch (Throwable tr) {
			Log.e(TAG, "Sign error", tr);
			return null;
		}
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse httpResponse;
		try {
			httpResponse = httpClient.execute(httpRequest);
		} catch (Throwable tr) {
			tr.printStackTrace();
			Log.e(TAG, "Request error");
			return new ResponseWrapper(R.string.request_error);
		}
		
		Log.d(TAG, "status: " + httpResponse.getStatusLine().getStatusCode()
				+ " : " + httpResponse.getStatusLine().getReasonPhrase());
		
		HttpEntity httpEntity = httpResponse.getEntity();
		InputStream is;
		try{
			is = httpEntity.getContent();
		}catch(IOException e){
			Log.e(TAG, "Error getting content", e);			
			return null;
		}
		
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		try {
			br = new BufferedReader(new InputStreamReader(is,UTF_8),8);
			String line = null;
			while( (line = br.readLine()) != null){
				Log.d(TAG, line);
				sb.append(line).append(EOL);
			}
			
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, "Unsupported encoding", e);
			return null;
		} catch (IOException e){
			Log.e(TAG, "Error reading data", e);
			return null;
		} finally{
			try {
				is.close();
				if(br != null) br.close();
			} catch (IOException e) {
				Log.e(TAG, "Error closing stream", e);
			}
			
		}
		return new ResponseWrapper(wrapperType, sb.toString());
	}

	@Override
	protected void onPostExecute(ResponseWrapper responseWrapper) {
		Log.d(TAG, this.getClass().getSimpleName() + ".onPostExecute");
		super.onPostExecute(responseWrapper);		
		if(responseWrapper != null){
			if(responseWrapper.isHasError()){
				handler.responseWrapperHandlerMessage(responseWrapper.getErrorMessageId());
			}else{
				handler.responseWrapperHandler(responseWrapper);
			}
		}else{
			handler.responseWrapperHandlerMessage(R.string.error_occured);
		}
	}
	
	

}
