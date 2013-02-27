package Export;

import java.util.ArrayList;

import Model.Automotive;
import Model.Option;
import Model.OptionSet;

public abstract class  CommAutoOpsAbs implements CommonAutoOps{
	private Automotive a1; 
	
	public void createautofromfile(String fileName) {
		a1 = Util.FileIO.readFromInput(fileName);
	}

	public void printauto() {
		a1.printInfo();
	}

	public void renameAutomotive(String newMake, String newModel) {
		a1.setMake(newMake);
		a1.setModel(newModel);
	}

	
	public void renameOptionSet(String oldSetName, String newSetName) {
		// TODO Auto-generated method stub
	}

	
	public void renameOption(String setName, String oldOptionName,
			String newOptionName) {
		// TODO Auto-generated method stub
		
	}

	
	public void addOptionSet(OptionSet newOptionSet) {
		ArrayList<Option> optionList = newOptionSet.getOptions();
		a1.addOptionSet(newOptionSet.getName(),
				optionList.size());
		for (int i = 0; i < optionList.size(); i++) {
			a1.setOption(newOptionSet.getName(), i, 
					optionList.get(i).getName(), 
					optionList.get(i).getPrice());
		}
		
	}

	
	public void addOption(String setName, Option newOption) {
		// TODO Auto-generated method stub
		
	}

	
	public void removeOptionSet(String setName) {
		// TODO Auto-generated method stub
		
	}

	
	public void removeOption(String setName, String optionName) {
		// TODO Auto-generated method stub
		
	}

}
