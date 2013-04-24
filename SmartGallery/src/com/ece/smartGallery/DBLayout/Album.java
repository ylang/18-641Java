package com.ece.smartGallery.DBLayout;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.ece.smartGallery.entities.Datastorage;

public class Album implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7615579591647445134L;
	private int count;
	private List<String> photoList;
	
	/**
	 * Get the album.
	 * @param context
	 * @return
	 * @throws IOException
	 */
	public static Album getAlbum(Context context) throws IOException {
		return Datastorage.recoverAlbum(context);
	}
	
	/**
	 * Create a new album.
	 * @return
	 */
	public static Album getNewAlbum() {
		return new Album();
	}
	
	protected Album() {
		this.count = 0;
		this.photoList = new ArrayList<String>();
	}
	
	public List<String> getPhotoFiles() {
		return this.photoList;
	}
	
	/**
	 * Add a new photo into the album
	 * @param photo
	 * @return id of the new photo, or -1 if failed to save.
	 */
	public int addNewPhoto(Context context, Photo photo) {
		String fileName = "photo_" + this.count + ".dat";
		try {
			Datastorage.savePhoto(context, fileName, photo);
			this.photoList.add(fileName);
			this.count ++;
			return (this.count - 1);
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public String getPhotoFile(int id) {
		if (id >= count) {
			return null;
		}
		return this.photoList.get(id);
	}
//	private int id;
//	private int userId;
//	private String name;
//	private String description;
//	private Photo[] photos;
//
//	public int getId() {
//		return id;
//	}
//
//	public void setId(int id) {
//		this.id = id;
//	}
//
//	public int getUserId() {
//		return userId;
//	}
//
//	public void setUserId(int userId) {
//		this.userId = userId;
//	}
//
//	public String getName() {
//		return name;
//	}
//
//	public void setName(String name) {
//		this.name = name;
//	}
//
//	public String getDescription() {
//		return description;
//	}
//
//	public void setDescription(String description) {
//		this.description = description;
//	}
//
//	public Photo[] getPhotos() {
//		return photos;
//	}
//
//	public void setPhotos(Photo[] photos) {
//		this.photos = photos;
//	}
}