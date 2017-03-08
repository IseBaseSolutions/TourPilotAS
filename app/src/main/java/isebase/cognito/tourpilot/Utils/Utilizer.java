package isebase.cognito.tourpilot.Utils;

import isebase.cognito.tourpilot.Data.BaseObject.BaseObject;

import java.util.List;

public class Utilizer {

	/**
	 * Create array of ids from items
	 * @param items to collect ids from
	 * @return array of ids.
	 * */
	public static int[] getIDs(List<? extends BaseObject> items){
		if(items == null)
			return new int[0];
		int[] retVal = new int[items.size()];
		for(int i = 0; i < items.size();i++)
			retVal[i] = items.get(i).getId();
		return retVal;
	}
	
	/**
	 * Create string of ids from items separated by comma
	 * @param items to collect ids from
	 * @return string of ids separated by comma. Return empty string if items size = 0
	 * */
	public static String getIDsString(Iterable<? extends BaseObject> items){
		String retVal = "";
		if(items == null)
			return retVal;
		for(BaseObject item : items)
			retVal += String.format("%s%s" 
					, retVal.equals("") ? "" : ", "
					, item.getId());
		
		return retVal;
	}
	
	public static String getNewIDsString(Iterable<? extends BaseObject> items){
		String retVal = "";
		if(items == null)
			return retVal;
		for(BaseObject item : items)
			retVal += String.format("%s%s" 
					, retVal.equals("") ? "" : ", "
					, item.getId());
		
		return retVal;
	}
	
	/**
	 * Create string of ids from items
	 * @param items to collect ids from
	 * @return string of ids separated by comma. Return empty string if items size = 0
	 * */
	public static String getIDsString(int[] items){
		String retVal = "";
		if(items == null)
			return retVal;
		for(int i = 0; i < items.length; i++)
		{
			retVal += String.format("%s%s" 
					, retVal.equals("") ? "" : ", "
					, items[i]);
		}
		return retVal;
	}	
}
