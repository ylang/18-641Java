package com.ece.smartGallery.util;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import android.content.Context;

import com.ece.smartGallery.DBLayout.Photo;

public class TransforablePhoto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6090493483762339387L;
	private String name;
	private long timeStamp;
	private String text; // text comment

	private byte[] imageBytes;
	private byte[] voiceBytes;
	private byte[] scratchBytes;

	private int commentType;
	private String location;
	private double lng, lat;

	public TransforablePhoto(Photo photo) {
		this.name = photo.getName();
		this.timeStamp = photo.getTimeStamp();
		this.text = photo.getText();
		this.location = photo.getLocation();
		this.lat = photo.getLat();
		this.lng = photo.getLng();
		try {
			this.imageBytes = IO.loadFile(new File(photo.getImage().getPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (photo.getVoice() != null && photo.getVoice().length() != 0) {
			try {
				this.voiceBytes = IO.loadFile(new File(photo.getVoice()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (photo.getScratchURI() != null
				&& photo.getScratchURI().getPath().length() != 0) {
			try {
				this.voiceBytes = IO.loadFile(new File(photo.getScratchURI()
						.getPath()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}