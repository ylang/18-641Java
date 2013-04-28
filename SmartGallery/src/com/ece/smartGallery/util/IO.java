package com.ece.smartGallery.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

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

}
