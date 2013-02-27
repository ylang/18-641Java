package Export;

import Model.Option;
import Model.OptionSet;

public interface CommonAutoOps {
	public void createautofromfile(String fname);
	   
	public void printauto();
	
	public void renameAutomotive(String newMake, String newModel);
	
	public void renameOptionSet(String oldSetName, String newSetName);
	
	public void renameOption(String setName, String oldOptionName, String newOptionName);
	
	public void addOptionSet(OptionSet newOptionSet);
	
	public void addOption(String setName, Option newOption);
	
	public void removeOptionSet(String setName);
	
	public void removeOption(String setName, String optionName);

}
