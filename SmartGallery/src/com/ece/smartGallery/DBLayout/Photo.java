package com.ece.smartGallery.DBLayout;

import java.io.Serializable;

public class Photo implements Serializable{
	
	private static final long serialVersionUID = -4783890754000515921L;
	private int id;
//	private int albumId;
	private String name;
	private long timeStamp;
	private String text;
	private byte[] voice;
	private String location;
	private double lng, lat;
	
	public Photo() {
		
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

//	public int getAlbumId() {
//		return albumId;
//	}
//
//	public void setAlbumId(int albumId) {
//		this.albumId = albumId;
//	}

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

	public byte[] getVoice() {
		return voice;
	}

	public void setVoice(byte[] voice) {
		this.voice = voice;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}