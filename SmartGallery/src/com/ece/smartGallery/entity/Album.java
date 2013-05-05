package com.ece.smartGallery.entity;

import java.io.IOException;

import android.content.Context;

import com.ece.smartGallery.db.Datastorage;

public class Album {

	public static final String ALBUM = "album";
	private int id;
	private int count;
	private String name;
	
	/**
	 * Get the album.
	 * @param context
	 * @return
	 * @throws IOException
	 */
	@Deprecated
	public static Album getAlbum(Context context) throws IOException {
		return Datastorage.recoverAlbum(context);
	}
	
	/**
	 * Create a new album.
	 * @return
	 */
	@Deprecated
	public static Album getNewAlbum() {
		return new Album();
	}
	
	public static Album getNewAlbum(String name) {
		return new Album(name);
	}
	
	public static Album getNewAlbum(int id, String name, int count) {
		return new Album(id, name, count);
	}
	
	protected Album(int id, String name, int count) {
		this.name = name;
		this.id = id;
		this.count = count;
	}
	
	protected Album(String name) {
		this.name = name;
		this.count = 0;
	}
	
	protected Album() {
		this.count = 0;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getId() {
		return this.id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getCount() {
		return this.count;
	}
	
	public void addCountByOne() {
		this.count ++;
	}
	/**
	 * Add a new photo into the album
	 * @param photo
	 * @return id of the new photo, or -1 if failed to save.
	 */
	public String addNewPhoto(Context context, Photo photo) {
		String fileName = "photo_" + this.id + "_" + this.count + ".dat";
		try {
			Datastorage.savePhoto(context, fileName, photo);
			this.count ++;
			return fileName;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void updatePhoto(Context context, String fileName, Photo photo) {
		try {
			Datastorage.savePhoto(context, fileName, photo);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}