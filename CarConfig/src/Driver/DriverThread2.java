package Driver;

import Model.OptionSet;
import Util.EditOptions;

public class DriverThread2 extends Thread {
	private EditOptions e;
	DriverThread2(EditOptions e) {
		this.e = e;
	}
	
	@Override
	public void run() {
		e.updateBasePrice(20000);
		OptionSet opSet = new OptionSet("Integrated GPS", 2);
		opSet.setOption(0, "present", 3000);
		opSet.setOption(1, "not present", 0);
		opSet.setOptionChoice("present");
		e.addNewOptionSet(opSet);
	}
}
