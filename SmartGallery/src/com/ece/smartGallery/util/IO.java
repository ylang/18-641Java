package com.ece.smartGallery.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.UUID;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.ece.smartGallery.entity.Photo;

public class IO {

	@Deprecated
	public static byte[] getByteArray(java.io.Serializable s)
			throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream out = null;
		byte[] bytes = null;
		try {
			out = new ObjectOutputStream(bos);
			out.writeObject(s);
			bytes = bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			out.close();
			bos.close();
		}
		return bytes;
	}

	// private String encodeFileToBase64Binary(String fileName)
	// throws IOException {
	//
	// File file = new File(fileName);
	// byte[] bytes = loadFile(file);
	// byte[] encoded = Base64.encodeBase64(bytes);
	// String encodedString = new String(encoded);
	//
	// return encodedString;
	// }

	public static byte[] loadFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);

		long length = file.length();
		if (length > Integer.MAX_VALUE) {
			// File is too large
		}
		byte[] bytes = new byte[(int) length];

		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length
				&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}

		if (offset < bytes.length) {
			is.close();
			throw new IOException("Could not completely read file "
					+ file.getName());
		}

		is.close();
		return bytes;
	}

	@Deprecated
	public static byte[] getBase64ByteArray(Serializable s) throws IOException {
		byte[] raw = getByteArray(s);
		return Base64.encode(raw, Base64.DEFAULT);
	}

	public static Photo convertToPhoto(TransferablePhoto p) {
		Photo photo = new Photo();
		photo.setAlbumId(1);
		photo.setLat(p.getLat());
		photo.setLng(p.getLng());
		photo.setLocation(p.getLocation());
		photo.setName(p.getName());
		photo.setText(p.getText());
		photo.setTimeStamp(p.getTimeStamp());
		String fileName = UUID.randomUUID() + ".jpg";
		photo.setImage(Uri.fromFile(new File(Environment
				.getExternalStorageDirectory().getAbsoluteFile(), fileName)));
		((new IO()).new SaveFileTask(p.getImageBytes(), fileName))
				.execute();

		if (p.getVoiceBytes() != null) {
			String vName = UUID.randomUUID() + ".3pg";
			photo.setVoice(new File(Environment
					.getExternalStorageDirectory().getAbsoluteFile(), vName).getAbsolutePath());
			((new IO()).new SaveFileTask(p.getVoiceBytes(), vName))
					.execute();
		}

		if (p.getScratchBytes() != null) {
			String sName = UUID.randomUUID() + ".png";
			photo.setScratchURI(Uri.fromFile(new File(Environment
					.getExternalStorageDirectory().getAbsoluteFile(), sName)));
			((new IO()).new SaveFileTask(p.getScratchBytes(), sName))
					.execute();
		}
		return photo;
	}

	class SaveFileTask extends AsyncTask<Void, Void, Void> {
		private byte[] bytes;
		private String fileName;

		SaveFileTask(byte[] bytes, String fileName) {
			this.bytes = bytes;
			this.fileName = fileName;
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			File photo = new File(
					Environment.getExternalStorageDirectory().getAbsoluteFile(),
					fileName);

			if (photo.exists()) {
				photo.delete();
			}

			try {
				FileOutputStream fos = new FileOutputStream(photo.getPath());

				fos.write(bytes);
				fos.close();
			} catch (java.io.IOException e) {
				Log.e("save picture", "Exception in photoCallback", e);
			}

			return (null);
		}
	}
}
