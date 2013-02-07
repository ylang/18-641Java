package automotive;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Controller {

	public static void main(String[] args) {
		OptionSet options = new OptionSet("Focus Wagon ZTW");
		
		Automotive focus = new Automotive(18445, "Focus Wagon ZTW", options);
		
		File file = new File("car.txt");
		if (file.exists()) {
			file.delete();
		}
		try {
			file.createNewFile();
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
			out.writeObject(focus);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
			Automotive inFocus = (Automotive) in.readObject();
			inFocus.printInfo();
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public static void preset() {
		// set colors
		OptionSet colors = new OptionSet("Color", 10);
		colors.setOption(0, "Fort Knox Gold Clearcoat Metallic", 0);
		colors.setOption(1, "Liquid Grey Clearcoat Metallic", 0);
		colors.setOption(2, "Infra-Red Clearcoat", 0);
		colors.setOption(3, "Grabber Green Clearcoat Metallic", 0);
		colors.setOption(4, "Sangria Red Clearcoat Metallic", 0);
		colors.setOption(5, "French Blue Clearcoat Metallic", 0);
		colors.setOption(6, "Twilight Blue Clearcoat Metallic", 0);
		colors.setOption(7, "CD Silver Clearcoat Metallic", 0);
		colors.setOption(8, "Pitch Black Clearcoat", 0);
		colors.setOption(9, "Cloud 9 White Clearcoat", 0);
		// set transmission
		OptionSet transmissions = new OptionSet("Transmission", 2);
		transmissions.setOption(0, "automatic", 0);
		transmissions.setOption(1, "manual", -815);
		// set brakes/traction control
	}
}
