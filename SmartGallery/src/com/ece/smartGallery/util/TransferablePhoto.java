package com.ece.smartGallery.util;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import com.ece.smartGallery.entity.Photo;

public class TransferablePhoto implements Serializable {
	private static final long serialVersionUID = -6090493483762339387L;
	private String name;
	private long timeStamp;
	private String text;

	private byte[] imageBytes;
	private byte[] voiceBytes;
	private byte[] scratchBytes;
	private int commentType;
	private String location;
	private double lng;
	private double lat;

	public TransferablePhoto(Photo photo) {
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
				this.scratchBytes = IO.loadFile(new File(photo.getScratchURI()
						.getPath()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public byte[] getImageBytes() {
		return imageBytes;
	}

	public void setImageBytes(byte[] imageBytes) {
		this.imageBytes = imageBytes;
	}

	public byte[] getVoiceBytes() {
		return voiceBytes;
	}

	public void setVoiceBytes(byte[] voiceBytes) {
		this.voiceBytes = voiceBytes;
	}

	public void setScratchBytes(byte[] sBytes) {
		this.scratchBytes = sBytes;
	}

	public byte[] getScratchBytes() {
		return this.scratchBytes;
	}

	public String getLocation() {
		return location;
	}

	public String getLoction() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}
}