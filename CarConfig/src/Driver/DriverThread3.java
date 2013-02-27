package Driver;

import Model.OptionSet;
import Util.EditOptions;

public class DriverThread3 extends Thread {
	private EditOptions e;
	DriverThread3(EditOptions e) {
		this.e = e;
	}
	
	@Override
	public void run() {
		OptionSet opSet = new OptionSet("Integrated GPS", 2);
		opSet.setOption(0, "present", 3000);
		opSet.setOption(1, "not present", 0);
		opSet.setOptionChoice("present");
		e.addNewOptionSet(opSet);
		OptionSet opSet2 = new OptionSet("Base active noise reduce system", 2);
		opSet2.setOption(0, "present", 1000);
		opSet2.setOption(1, "not present", 0);
		opSet2.setOptionChoice("not present");
		e.addNewOptionSet(opSet2);

	}
}
