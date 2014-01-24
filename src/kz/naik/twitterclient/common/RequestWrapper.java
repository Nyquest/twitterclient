package kz.naik.twitterclient.common;

import java.util.List;

import org.apache.http.NameValuePair;

public class RequestWrapper {
	private WrapperType wrapperType;
	private HttpMethod httpMethod;
	private String uri;
	private List<NameValuePair> parameterList;

	public RequestWrapper(WrapperType wrapperType, HttpMethod httpMethod, String uri) {
		this(wrapperType,httpMethod,uri,null);
	}
	
	public RequestWrapper(WrapperType wrapperType, HttpMethod httpMethod, String uri,List<NameValuePair> parameterList) {
		this.wrapperType = wrapperType;
		this.setHttpMethod(httpMethod);
		this.uri = uri;
		this.setParameterList(parameterList);
	}

	public WrapperType getWrapperType() {
		return wrapperType;
	}

	public void setWrapperType(WrapperType wrapperType) {
		this.wrapperType = wrapperType;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public HttpMethod getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(HttpMethod httpMethod) {
		this.httpMethod = httpMethod;
	}

	public List<NameValuePair> getParameterList() {
		return parameterList;
	}

	public void setParameterList(List<NameValuePair> parameterList) {
		this.parameterList = parameterList;
	}
	
	
}
