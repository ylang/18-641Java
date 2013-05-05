package com.ece.smartGallery.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;

import android.content.Context;
import android.os.Environment;

import com.ece.smartGallery.entity.Album;
import com.ece.smartGallery.entity.Photo;

public class Datastorage {
	private static final String ALBUM_FILE = "smart_gallery_album.dat";

	public static boolean storageReady() {
		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
		    mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
		    mExternalStorageAvailable = true;
		    mExternalStorageWriteable = false;
		} else {
		    mExternalStorageAvailable = mExternalStorageWriteable = false;
		}
		return (mExternalStorageAvailable && mExternalStorageWriteable); 
	}
	
	@Deprecated 
	public static Album recoverAlbum(Context context) throws IOException{
		if (!storageReady()) {
			throw new IOException("Storage is not available");
		}
		File path = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
		File file = new File(path, ALBUM_FILE);
		if (file.exists()) {
			Album album = getAlbumFromFile(file);
			return album;
		} else {
			Album album = Album.getNewAlbum();
			saveAlbumToFile(album, file);
			return album;
		}
	}
	
	public static void saveAlbum(Context context, Album album) throws IOException {
		if (!storageReady()) {
			throw new IOException("Storage is not available");
		}
		File path = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
		File file = new File(path, ALBUM_FILE);
		saveAlbumToFile(album, file);
	}
	
	public static Photo loagPhoto (Context context, String fileName) throws IOException {
		if (!storageReady()) {
			throw new IOException("Storage is not available");
		}
		File path = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
		File file = new File(path, fileName);
		if (!file.exists()) {
			throw new IOException("Photo does not exist");
		}
		return getPhotoFromFile(file);
	}
	
	public static void savePhoto(Context context, String fileName, Photo photo) throws IOException {
		if (!storageReady()) {
			throw new IOException("Storage is not available");
		}
		File path = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
		File file = new File(path, fileName);
		if (file.exists()) {
			file.delete();
		}
		savePhotoToFile(photo, file);
	}
	
	private static Album getAlbumFromFile(File file) throws FileNotFoundException, IOException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
		Album album = null;
		try {
			album = (Album) ois.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			ois.close();
			return null;
		}
		ois.close();
		return album;
	}
	
	private static void saveAlbumToFile(Album album, File file) throws FileNotFoundException, IOException {
		if (file.exists()) {
			file.delete();
		}
		file.createNewFile();
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
		oos.writeObject(album);
		oos.close();
		return;
	}
	
	private static Photo getPhotoFromFile(File file) throws OptionalDataException, IOException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
		Photo photo;
		try {
			photo = (Photo) ois.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			ois.close();
			return null;
		}
		ois.close();
		return photo;
	}
	
	private static void savePhotoToFile(Photo photo, File file) throws FileNotFoundException, IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
		oos.writeObject(photo);
		oos.close();
		return;
	}
}
