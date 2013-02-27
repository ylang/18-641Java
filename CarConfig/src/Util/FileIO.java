package Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import Model.Automotive;

public class FileIO {
	
	public static Automotive readFromInput(String fileName) {
		BufferedReader in = null;
		String make = null;
		String model = null;
		int basePrice = 0;

		try {
			in = new BufferedReader(new FileReader(fileName));
			make = in.readLine().trim();
			model = in.readLine().trim();
			basePrice = Integer.parseInt(in.readLine().trim());
			Automotive car = new Automotive();
			car.setMake(make);
			car.setBasePrice(basePrice);
			car.setModel(model);
			
			// set up options
			String line = null;
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if ("end of setup".equals(line)) {
					break;
				}
				String args[] = line.split(",");
				String setName = args[1];
				int setCount = Integer.parseInt(args[2]);
				car.addOptionSet(setName, setCount);
				for (int i = 0; i < setCount; i++) {
					car.setOption(setName, i, args[i * 2 + 3],
							Integer.parseInt((args[i * 2 + 4])));
				}
			}
			
			// set up choices
			while ((line = in.readLine()) != null) {
				String args[] = line.trim().split(":");
				car.setOptionChoice(args[0], args[1]);
			}
			in.close();
			return car;
		} catch (FileNotFoundException fnfe) {
			System.err.println(fnfe.getMessage());
			return null;
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
			return null;
		}
	}
	
	public static void serializeAutomotive(Automotive car, String fileName) {
		File file = new File(fileName);
		if (file.exists()) {
			file.delete();
		}
		try {
			file.createNewFile();
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream(file));
			out.writeObject(car);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public static Automotive deserializeAutomotive(String fileName) {
		File file = new File(fileName);
		try {
			if (!file.exists()) {
				throw new FileNotFoundException(fileName + "is not found");
			}
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(
					file));
			Automotive car = (Automotive) in.readObject();
			in.close();
			return car;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

}
