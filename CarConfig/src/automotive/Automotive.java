package automotive;

import java.util.ArrayList;

public class Automotive {
	private ArrayList<OptionSet> options;
	private int basePrice;
	private String name;
	
	public Automotive(int basePrice, String name, ArrayList<OptionSet> options) {
		this.basePrice = basePrice;
		this.name = name;
		this.options = options;
	}
	
	public int basePrice() {
		return this.basePrice;
	}
	
	public String getName() {
		return this.name;
	}
	
	public ArrayList<OptionSet> getOptions() {
		return this.options;
	}
	
}
