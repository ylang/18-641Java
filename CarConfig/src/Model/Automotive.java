package Model;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;

import Exception.AutomotiveExceptionHandler;

public class Automotive implements Serializable {

	private static final long serialVersionUID = -207791647369713043L;
	private AutomotiveExceptionHandler exceptionHandler = 
			new AutomotiveExceptionHandler();

	//private OptionSet options;
	private LinkedHashMap<String, OptionSet> optionSets;
	private int basePrice;
	private String make;
	private String model;

	public Automotive() {
		this.basePrice = 0;
		this.optionSets = new LinkedHashMap<String, OptionSet>();
	}

	public String getMake() {
		if (this.make == null) {
			this.exceptionHandler
				.raiseException(this.exceptionHandler.MAKE_NOT_DEFINED);
			this.make = "default make";
			this.exceptionHandler
			.fixException(this.exceptionHandler.MAKE_NOT_DEFINED);
		}
		return this.make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		if (this.model == null) {
			this.exceptionHandler
				.raiseException(this.exceptionHandler.MODEL_NOT_DEFINED);
			this.model = "default model";
			this.exceptionHandler
			.fixException(this.exceptionHandler.MODEL_NOT_DEFINED);
		}
		return this.model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public int getBasePrice() {
		return this.basePrice;
	}

	public void setBasePrice(int basePrice) {
		this.basePrice = basePrice;
	}

	public void addOptionSet(String setName, int count) {
		this.optionSets.put(setName, new OptionSet(setName, count));
	}
	
	/**
	 * Add a new option set. If the new option set is null, do nothing.
	 * @param newOptionSet the new option set would be added.
	 */
	public void addOptionSet(OptionSet newOptionSet) {
		if (newOptionSet == null) {
			return;
		}
		this.optionSets.put(newOptionSet.getName(), newOptionSet);
	}
	
	/**
	 * A null will be returned if there is no such set.
	 * Otherwise, the removed option set will be returned.
	 * @param setName the name of the option set.
	 */
	public OptionSet removeOptionSet(String setName) {
		return this.optionSets.remove(setName);
	}

	public OptionSet getOptionSet(String setName) {
		return this.optionSets.get(setName);
	}

	public void setOption(String setName, int index, String optionName, int price) {
		OptionSet targetSet = this.optionSets.get(setName);
		if (targetSet == null) {
			// create a new optionSet
			this.exceptionHandler.raiseException(this.exceptionHandler.OPTION_SET_NOT_DEFINED);
			this.addOptionSet(setName, index + 1);
			targetSet = this.optionSets.get(setName);
			this.exceptionHandler.fixException(this.exceptionHandler.OPTION_SET_NOT_DEFINED);
		}
		targetSet.setOption(index, optionName, price);
		this.optionSets.put(setName, targetSet);
	}

	public Iterator<String> getOptionSetNamesIterator() {
		return this.optionSets.keySet().iterator();
	}

	public Option getOptionChoice(String setName) {
		OptionSet targetSet = this.optionSets.get(setName);
		if (targetSet == null) {
			this.exceptionHandler.raiseException(this.exceptionHandler.OPTION_SET_NOT_DEFINED);
			Option empty = new Option("empty", 0);
			this.exceptionHandler.fixException(this.exceptionHandler.OPTION_SET_NOT_DEFINED);
			return empty;
		}
		return targetSet.getOptionChoice();
	}

	public int getOptionChoicePrice(String setName) {
		OptionSet targetSet = this.optionSets.get(setName);
		if (targetSet == null) {
			this.exceptionHandler.raiseException(this.exceptionHandler.OPTION_SET_NOT_DEFINED);
			this.exceptionHandler.fixException(this.exceptionHandler.OPTION_SET_NOT_DEFINED);
			return 0;
		}
		if (targetSet.getOptionChoice() == null) {
			this.exceptionHandler.raiseException(this.exceptionHandler.OPTION_NOT_SET_YET);
			this.exceptionHandler.fixException(this.exceptionHandler.OPTION_NOT_SET_YET);
			return 0;
		}
		return targetSet.getOptionChoice().getPrice();
	}

	public void setOptionChoice(String setName, String optionName) {
		OptionSet targetSet = this.optionSets.get(setName);
		if (targetSet == null) {
			this.exceptionHandler.raiseException(this.exceptionHandler.OPTION_SET_NOT_DEFINED);
			return;
		}
		targetSet.setOptionChoice(optionName);
		this.optionSets.put(setName, targetSet);
	}

	public int getTotalPrice() {
		Iterator<String> iterator = this.getOptionSetNamesIterator();
		int totalPrice = this.basePrice;
		while (iterator.hasNext()) {
			String setName = iterator.next();
			totalPrice += this.getOptionChoice(setName).getPrice();
		}
		return totalPrice;
	}

	/**
	 * A helper function to print out all the information of the automotive.
	 */
	public void printInfo() {
		System.out.println("model : " + this.model);
		System.out.println("make : " + this.make);
		System.out.println("base price : " + this.basePrice);
		System.out.println("option availables:");
		Iterator<String> setNames = this.getOptionSetNamesIterator();
		while (setNames.hasNext()) {
			String setName = setNames.next();
			System.out.println(setName + " :");
			this.optionSets.get(setName).printAllOptions();
			System.out.println("Selected choice : " + 
					this.optionSets.get(setName).getOptionChoice().getName());
		}
		System.out.println("total price : " + this.getTotalPrice());
		
	}
}