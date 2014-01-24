package kz.naik.twitterclient.common;

public class ResponseWrapper {
	private WrapperType wrapperType;
	private String json;
	private boolean hasError;
	private int errorMessageId;
	
	public ResponseWrapper(WrapperType wrapperType, String json) {
		this.wrapperType = wrapperType;
		this.json = json;
	}

	public ResponseWrapper(int errorMessageId) {
		this(true,errorMessageId);
	}
	
	public ResponseWrapper(boolean hasError, int errorMessageId) {
		this.hasError = hasError;
		this.errorMessageId = errorMessageId;
	}



	public WrapperType getWrapperType() {
		return wrapperType;
	}
	
	public void setWrapperType(WrapperType wrapperType) {
		this.wrapperType = wrapperType;
	}
	
	public String getJson() {
		return json;
	}
	
	public void setJson(String json) {
		this.json = json;
	}

	public boolean isHasError() {
		return hasError;
	}

	public void setHasError(boolean hasError) {
		this.hasError = hasError;
	}

	public int getErrorMessageId() {
		return errorMessageId;
	}

	public void setErrorMessageId(int errorMessageId) {
		this.errorMessageId = errorMessageId;
	}
	
	
}
