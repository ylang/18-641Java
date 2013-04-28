package com.ece.smartGallery.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class IO {
	public static byte[] getByteArray(java.io.Serializable s) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
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
