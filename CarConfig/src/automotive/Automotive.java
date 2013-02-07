package automotive;


public class Automotive {
	private OptionSet options;
	private int basePrice;
	private String name;
	
	public Automotive(int basePrice, String name, OptionSet options) {
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
	
	public OptionSet getOptions() {
		return this.options;
	}
	
	public void printInfo() {
		
	}
	
}
