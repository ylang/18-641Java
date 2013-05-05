package com.ece.smartGallery.entity;

import java.io.File;
import java.io.Serializable;

import android.net.Uri;

public class Photo implements Serializable {
	public static final String PHOTO = "photo";
	public static final String IMAGE = "image";
	public static final int VOICE_TYPE = 1;
	public static final int TEXT_TYPE = 2;
	public static final int TOUCHPAD_TYPE = 3;

	private static final long serialVersionUID = -4783890754000515921L;
	private int albumId = 1;
	private int id = -1;
	private String name;
	private long timeStamp;
	private String text; // text comment
	private String voice; // voice comment
	private String imageURI; // the URI of the image file. (in string)
	private String scratchURI; // the URI of the scratch comment file. (in
								// string)

	private int commentType;
	private String location;
	private double lng, lat;
	private boolean hasSractch = false;
	private boolean hasVoice = false;

	public Photo() {

	}
	
	public void setAlbumId(int id) {
		this.albumId = id;
	}
	
	public int getAlbumId() {
		return this.albumId;
	}

	public int getCommentType() {
		return this.commentType;
	}

	public void setCommentType(int commentType) {
		if (commentType < 4 && commentType > 0) {
			this.commentType = commentType;
		}
	}

	public Uri getImage() {

		return Uri.fromFile(new File(this.imageURI));
	}

	public void setImage(Uri img) {
		this.imageURI = img.getPath();
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

	public Uri getScratchURI() {
		if (this.hasSractch) {
			return Uri.fromFile(new File(this.scratchURI));
		} else {
			return null;
		}
	}

	public void setScratchURI(Uri uri) {
		if (uri != null) {
			this.scratchURI = uri.getPath();
			this.hasSractch = true;
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

	public String getVoice() {
		if (this.hasVoice) {
			return voice;
		} else {
			return null;
		}
	}

	public void setVoice(String voice) {
		this.voice = voice;
		this.hasVoice = true;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}