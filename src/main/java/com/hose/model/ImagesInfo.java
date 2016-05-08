package com.hose.model;

import java.util.ArrayList;
import java.util.List;

public class ImagesInfo {
	public int count;
	private List images = new ArrayList();
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public List getImages() {
		return images;
	}
	public void setImages(List images) {
		this.images = images;
	}
}
