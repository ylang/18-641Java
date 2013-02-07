package automotive;

import java.util.ArrayList;

import javax.swing.text.html.Option;


public class Automotive {
	private OptionSet options;
	private int basePrice;
	private String model;
	
	public Automotive(int basePrice, String model, OptionSet options) {
		this.basePrice = basePrice;
		this.model = model;
		this.options = options;
	}
	
	public int basePrice() {
		return this.basePrice;
	}
	
	public String getModel() {
		return this.model;
	}
	
	public OptionSet getOptions() {
		return this.options;
	}
	
	public void printInfo() {
		System.out.println("model : " + this.model);
		System.out.println("base price : " + this.basePrice);
		this.options.printAllOptions();
	}
	
}
