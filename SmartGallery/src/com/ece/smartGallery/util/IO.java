package com.ece.smartGallery.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import android.util.Base64;

public class IO {
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
	
//	private String encodeFileToBase64Binary(String fileName)
//			throws IOException {
// 
//		File file = new File(fileName);
//		byte[] bytes = loadFile(file);
//		byte[] encoded = Base64.encodeBase64(bytes);
//		String encodedString = new String(encoded);
// 
//		return encodedString;
//	}
 
	public static byte[] loadFile(File file) throws IOException {
	    InputStream is = new FileInputStream(file);
 
	    long length = file.length();
	    if (length > Integer.MAX_VALUE) {
	        // File is too large
	    }
	    byte[] bytes = new byte[(int)length];
	    
	    int offset = 0;
	    int numRead = 0;
	    while (offset < bytes.length
	           && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
	        offset += numRead;
	    }
 
	    if (offset < bytes.length) {
	    	is.close();
	        throw new IOException("Could not completely read file "+file.getName());
	    }
 
	    is.close();
	    return bytes;
	}
	
	public static byte[] getBase64ByteArray(Serializable s) throws IOException {
		byte[] raw = getByteArray(s);
		return Base64.encode(raw, Base64.DEFAULT);
	}
}
