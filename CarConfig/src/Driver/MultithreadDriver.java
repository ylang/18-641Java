package Driver;

import Model.Automotive;
import Util.EditOptions;
import Util.FileIO;

public class MultithreadDriver {

	public static void main(String[] args) {
		String fileName = "car.dat";
		System.out.println("parse and create =====================");
		Automotive focus = FileIO.readFromInput("input.txt");
		focus.printInfo();
		System.out.println("serialize and output to file ===============");
		FileIO.serializeAutomotive(focus, fileName);

		System.out.println("deserialize and print ======================");
		Automotive anotherFocus = FileIO.deserializeAutomotive(fileName);
		if (anotherFocus != null) {
			anotherFocus.printInfo();
		}
		EditOptions e = new EditOptions(focus);
		
		DriverThread dt1 = new DriverThread(e);
		DriverThread2 dt2 = new DriverThread2(e);
		DriverThread3 dt3 = new DriverThread3(e);
		dt2.start();
		try {
			dt2.join();
			System.out.println("dt2 finished ============");
			focus.printInfo();
			dt1.start();
			dt3.start();
			dt1.join();
			dt3.join();
			System.out.println("dt1 & dt3 finished ============");
			focus.printInfo();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		
	}
}
