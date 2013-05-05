package com.ece.smartGallery.db;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ece.smartGallery.entity.Album;
import com.ece.smartGallery.entity.Photo;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 2;

	// Database Name
	private static final String DATABASE_NAME = "photoManager";

	// Photos table name
	private static final String TABLE_PHOTOS = "photo_";

	// Albums table name
	private static final String TABLE_ALBUMS = "albums";

	// Albums Table Columns names
	private static final String KEY_NAME = "name";
	private static final String KEY_COUNT = "count";

	// Photos Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_FILE = "file_name";

	private Context context;

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_ALBUM_TABLE = "CREATE TABLE " + TABLE_ALBUMS + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
				+ KEY_COUNT + " INTEGER" + ")";
		db.execSQL(CREATE_ALBUM_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALBUMS);

		// Create tables again
		onCreate(db);
	}

	public Album getAlbum(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_ALBUMS, new String[] { KEY_ID, KEY_NAME, KEY_COUNT }, KEY_ID
				+ "=?", new String[] { String.valueOf(id) }, null, null, null,
				null);
		if (cursor != null)
			cursor.moveToFirst();
		Album album = Album.getNewAlbum(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), Integer.parseInt(cursor.getString(2)));
		db.close();
		return album;
	}

	public Album addAlbum(String name) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		Album album = Album.getNewAlbum(name);
		values.put(KEY_NAME, album.getName());
		values.put(KEY_COUNT, album.getCount());
		// Inserting Row
		long id = db.insert(TABLE_ALBUMS, null, values);
		album.setId((int) id);

		String CREATE_PHOTOS_TABLE = "CREATE TABLE " + TABLE_PHOTOS + id + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_FILE + " TEXT" + ")";
		db.execSQL(CREATE_PHOTOS_TABLE);

		db.close(); // Closing database connection
		return album;
	}

	/**
	 * Adding new photo
	 * 
	 * @param album
	 * @param photo
	 * @return true if added successfully, otherwise false
	 */
	public boolean addPhoto(Album album, Photo photo) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		String file = album.addNewPhoto(context, photo);
		if (file != null) {
			values.put(KEY_FILE, file);
			long id = db.insert(TABLE_PHOTOS + album.getId(), null, values);
			photo.setId((int) id);
			album.updatePhoto(context, file, photo);
			
			values = new ContentValues();
			values.put(KEY_NAME, album.getName());
			values.put(KEY_COUNT, album.getCount());
			// updating row
			db.update(TABLE_ALBUMS, values, KEY_ID + " = ?",
					new String[] { String.valueOf(album.getId()) });
			db.close(); // Closing database connection
			return true;
		} else {
			db.close(); // Closing database connection
			return false;
		}
	}

	public List<Album> getAllAlbums() {
		List<Album> albumList = new ArrayList<Album>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_ALBUMS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Album album = Album.getNewAlbum(
						Integer.parseInt(cursor.getString(0)),
						cursor.getString(1),
						Integer.parseInt(cursor.getString(2)));
				// Adding contact to list
				albumList.add(album);
			} while (cursor.moveToNext());
		}

		// return contact list
		db.close();
		return albumList;
	}

	// Getting single contact
	public Photo getPhoto(Album album, int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_PHOTOS + album.getId(), new String[] {
				KEY_ID, KEY_FILE }, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();
		Photo photo = null;
		try {
			photo = Datastorage.loagPhoto(context, cursor.getString(1));
		} catch (IOException e) {
			e.printStackTrace();
		}
		db.close();
		return photo;
	}

	// Getting All Photos
	public List<Photo> getAllPhotos(int albumId) {
		List<Photo> photoList = new ArrayList<Photo>();

		SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "SELECT  * FROM " + TABLE_PHOTOS + albumId;
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Photo photo;
				try {
					photo = Datastorage.loagPhoto(context, cursor.getString(1));
					photoList.add(photo);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} while (cursor.moveToNext());
		}
		db.close();
		return photoList;
	}

	/**
	 * 
	 * Updating single Photo
	 * 
	 * @param album
	 * @param photo
	 * @return ture if successed, false otherwise.
	 */
	public boolean updatePhoto(Album album, Photo photo) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_PHOTOS + album.getId(), new String[] {
				KEY_ID, KEY_FILE }, KEY_ID + "=?",
				new String[] { String.valueOf(photo.getId()) }, null, null,
				null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		try {
			Datastorage.savePhoto(context, cursor.getString(1), photo);
			db.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			db.close();
			return false;
		}
	}
}
