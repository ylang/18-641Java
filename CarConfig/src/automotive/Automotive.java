package automotive;

import java.io.Serializable;

public class Automotive implements Serializable {

	private static final long serialVersionUID = -207791647369713043L;
	
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