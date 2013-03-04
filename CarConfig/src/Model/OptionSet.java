package Model;

import java.io.Serializable;
import java.util.ArrayList;

public class OptionSet implements Serializable {

	private static final long serialVersionUID = -7562075447134713419L;
	private String name;
	private ArrayList<Option> options;
	private Option choice;

	public OptionSet() {
		// leave everything null
	}

	public OptionSet(String name) {
		this.name = name;
	}

	public OptionSet(String name, int count) {
		this.name = name;
		this.options = new ArrayList<Option>(count);
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Option> getOptions() {
		return this.options;
	}

	public void setOptions(ArrayList<Option> options) {
		this.options = options;
	}

	public void setOption(int i, String name, int price) {
		if (this.options == null) {
			this.options = new ArrayList<Option>();
		}
		if (i < this.options.size())
			this.options.set(i, new Option(name, price));
		else 
			this.options.add(i, new Option(name, price));
	}
	
	/**
	 * Updating an option. If the a option with the specific name is 
	 * existed, the price of the option is updated and it will return 
	 * true. Otherwise, it will do nothing and return false.
	 * @param name the name of the option
	 * @param price the updated price
	 * @return true if updating successes, otherwise false.
	 */
	public boolean updateOption(String name, int price) {
		if (this.options == null) {
			this.options = new ArrayList<Option>();
		}
		int index = this.findOption(name);
		if (index == -1) {
			return false;
		} else {
			this.setOption(index, name, price);
			return true;
		}
	}

	public Option getOption(String name) {
		int index = findOption(name);
		if (index == -1) {
			return null;
		} else {
			return this.options.get(index);
		}
	}

	public int getOptionPrice(String name) {
		int index = findOption(name);
		if (index == -1) {
			return 0;
		} else {
			return this.options.get(index).getPrice();
		}
	}

	public void printAllOptions() {
		for (Option op : this.options) {
			System.out.println(op.getName() + " : " + op.getPrice());
		}
	}
	
	public Option getOptionChoice() {
		return this.choice;
	}
	
	public void setOptionChoice(String optionName) {
		this.choice = this.getOption(optionName);
		if (this.choice == null) {
			throw new IllegalArgumentException(optionName + " is not a valid choice");
		}
	}
	
	/**
	 * It will remove a specific option corresponding to the name.
	 * If the option exists, it will be removed and a true will be returned.
	 * Otherwise, nothing would be done and a false will be returned.
	 * @param name option name
	 * @return true if removing successfully, otherwise false.
	 */
	public boolean removeOption(String name) {
		int index = this.findOption(name);
		if (index == -1) {
			return false;
		}
		this.options.remove(index);
		return true;
	}

	private int findOption(String name) {
		if (name == null) {
			return -1;
		}
		if (this.options != null) {
			for (int i = 0; i < this.options.size(); i++) {
				if (this.options.get(i) != null
						&& this.options.get(i).getName().equals(name)) {
					return i;
				}
			}
			return -1;
		} else {
			return -1;
		}
	}
}
