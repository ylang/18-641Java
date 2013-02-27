package Util;

import Model.Automotive;
import Model.Option;
import Model.OptionSet;

public class EditOptions {
	private static Automotive a;
	
	public EditOptions(Automotive a) {
		EditOptions.a = a;
	}
	
	public synchronized void updateModel(String model) {
		a.setModel(model);
	}
	
	public synchronized void updateMake(String make) {
		a.setMake(make);
	}
	
	public synchronized void updateBasePrice(int basePrice) {
		a.setBasePrice(basePrice);
	}
	
	/**
	 * add a new option set, containing all the information in the set:
	 * each options it contains and also the option choice.
	 * @param opSet the new option set that to be added.
	 */
	public synchronized void addNewOptionSet(OptionSet opSet) {
		a.addOptionSet(opSet);
	}
	
	/**
	 * Remove an optionSet. If the option set is not existed, 
	 * just do nothing and return false. Otherwise, the target option
	 * set will be removed.
	 * @param setName the option set name
	 * @return false if the option set is not existed, otherwise true.
	 */
	public synchronized boolean removeOptionSet(String setName) {
		OptionSet opSet = a.removeOptionSet(setName);
		if (opSet == null) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Will firstly find the target optionSet based on name of
	 * the passing in option set. If the target is not existed,
	 * just do nothing and return false. Otherwise, update the
	 * option set with the passing in option set.
	 * @param opSet the updated option set
	 * @return false if the target option set is not existed. Otherwise true.
	 */
	public synchronized boolean updateOptionSet(OptionSet opSet) {
		String setName = opSet.getName();
		if (a.getOptionSet(setName) == null) {
			return false;
		} else {
			this.removeOptionSet(setName);
			this.addNewOptionSet(opSet);
			return true;
		}
	}
	
	/**
	 * Add a option to an option set. If the option set is not existed,
	 * it will created a new option set with the corresponding name.
	 * The new option set will only contains the passing in option.
	 * @param setName the name of the target option set.
	 * @param option the new option that would be added.
	 */
	public synchronized void addOption(String setName, Option option) {
		OptionSet opSet = a.getOptionSet(setName);
		int index = -1;
		if (opSet == null) {
			a.addOptionSet(setName, 1);
			index = 0;
			a.setOption(setName, index, option.getName(), option.getPrice());
		} else {
			index = opSet.getOptions().size();
			if (opSet.getOption(option.getName()) != null) {
				opSet.updateOption(option.getName(), option.getPrice());
			} else {
				opSet.setOption(index, option.getName(), option.getPrice());
			}
		}
	}
	
	/**
	 * It will delete a target option in a specific option set.
	 * @param setName name of the option set
	 * @param optionName name of the option
	 * @return true if removing successfully, false if the option set or
	 * the option is not existed (and removing fails).
	 */
	public synchronized boolean removeOption(String setName,
			String optionName) {
		OptionSet opSet = a.getOptionSet(setName);
		if (opSet == null) {
			return false;
		}
		return opSet.removeOption(optionName);
	}
	
	/**
	 * Updating an option of a specific option set with the
	 * specific option name.
	 * @param setName name of the option set
	 * @param optionName name of the option
	 * @param optionPrice price of the option
	 * @return false if either the option set is not existed, or the option
	 * is not existed. True if the update successfully.
	 */
	public synchronized boolean updateOption(String setName,
			String optionName, int optionPrice) {
		OptionSet opSet = a.getOptionSet(setName);
		if (opSet == null) {
			return false;
		}
		return opSet.updateOption(optionName, optionPrice);
	}
	
}
