package automotive;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Controller {

	public static void main(String[] args) {

		File file = new File("car.dat");
		
		
		System.out.println("parse and create =====================");
		Automotive focus = parse();
		focus.printInfo();
		
		System.out.println("serialize and output to file ===============");
		if (file.exists()) {
			file.delete();
		}
		try {
			file.createNewFile();
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream(file));
			out.writeObject(focus);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		

		System.out.println("deserialize and print ======================");
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(
					file));
			Automotive inFocus = (Automotive) in.readObject();
			inFocus.printInfo();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
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

	public static Automotive parse() {
		BufferedReader in = null;
		String name = null;
		int basePrice = 0;
		OptionSet options = null;
		int optionNum = 0;

		try {
			in = new BufferedReader(new FileReader("input.txt"));
			String line = null;
			int n = 0;
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (n == 0) {
					name = line;
				} else if (n == 1) {
					basePrice = Integer.parseInt(line);
				} else if (n == 2) {
					optionNum = Integer.parseInt(line);
					options = new OptionSet(name, optionNum);
				} else {
					options.setOption(n - 3, line.split(":")[0],
							Integer.parseInt(line.split(":")[1]));
				}
				n++;
			}
			in.close();
		} catch (FileNotFoundException fnfe) {
			System.err.println(fnfe.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
		return new Automotive(basePrice, name, options);
	}
}
