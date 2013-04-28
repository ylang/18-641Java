package com.ece.smartGallery.util;

import java.io.Serializable;

import com.ece.smartGallery.DBLayout.Photo;

public class TransforablePhoto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6090493483762339387L;
	private Photo photo;
	
	public TransforablePhoto(Photo photo) {
		this.photo = photo;
	}

}
