package Driver;

import Util.EditOptions;

public class DriverThread extends Thread {
	private EditOptions e;
	DriverThread(EditOptions e) {
		this.e = e;
	}
	
	@Override
	public void run() {
		e.updateBasePrice(10000);
	}
}
