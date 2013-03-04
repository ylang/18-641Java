package Util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ObjectIO {

	/** Write the object to string. */
	public static String toString(Serializable o) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(o);
		oos.close();
		return Base64.encodeBytes(baos.toByteArray());
	}

	/** Read the object string. */
	public static Object fromString(String s) throws IOException,
			ClassNotFoundException {
		byte[] data = Base64.decode(s);
		if (data == null) {
			System.out.println("data = null");
		}
		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(
				data));
		Object o = ois.readObject();
		ois.close();
		return o;
	}
}
