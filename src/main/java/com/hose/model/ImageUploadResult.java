package com.hose.model;

public class ImageUploadResult {
	public ImageUploadResult(String result, String url) {
		super();
		this.result = result;
		this.url = url;
	}
	private String result;
	private String url;
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
}
