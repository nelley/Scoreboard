package com.anklebreaker.basketball.tw.summary;

import android.graphics.Bitmap;

public class RecordBoardBtn {
	
	protected Bitmap image_check;
    protected Bitmap image;
    protected String title;
	
	/**
	 * constructor
	 * */
	public RecordBoardBtn(Bitmap image, String title) {
		this.image = image;
		this.title = title;
	}

	public Bitmap getImage_check() {
		return image_check;
	}
	public void setImage_check(Bitmap image_check) {
		this.image_check = image_check;
	}
	public Bitmap getImage() {
		return image;
	}
	public void setImage(Bitmap image) {
		this.image = image;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
}
