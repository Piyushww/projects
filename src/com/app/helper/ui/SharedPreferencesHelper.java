package com.app.helper.ui;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * <b>SharedPreferenceHepler</b> : class for manipulating data with SharedPreferences.
 * @author Piyush Bobhate.
 * @since 11th Sep 2012 
 */

public class SharedPreferencesHelper {
	
	private SharedPreferences sharedPreference;
	private SharedPreferences sharedPreferenceCounter;
	private SharedPreferences sharedPreferenceDelete;
	private SharedPreferences.Editor prefsEdtr;
	private SharedPreferences.Editor prefsEdtrCounter;
	private SharedPreferences.Editor prefsEdtrDelete;
	private Context context;
	private int counter;
	
	public SharedPreferencesHelper(Context context) {
		this.context = context;
		counter = 0;
	}
	
	/**
	 * setSharedPreferences(String, String) method sets values to preferences
	 * @param prefName : String Name of the preference.
	 * @param values : Hashmap<String, String> values to save in respective preference.
	 */
	public void setSharedPreferences(String prefName, HashMap<String, Object> values){
		incrementCounter();
		sharedPreference = context.getSharedPreferences(prefName+counter,
				Context.MODE_WORLD_WRITEABLE);
		prefsEdtr = sharedPreference.edit();
		CharSequence[] valuesInHashmap = values.keySet().toArray(new CharSequence[values.size()]);
		
		for(int i = 0; i < valuesInHashmap.length; i++){
			prefsEdtr.putString(valuesInHashmap[i].toString(), values.get(valuesInHashmap[i]).toString());
			Log.d("",valuesInHashmap[i].toString()+" : "+values.get(valuesInHashmap[i]).toString());
		}
		prefsEdtr.commit();
	}
	
	private void incrementCounter() {
		sharedPreferenceCounter = context.getSharedPreferences("sharedPreferenceCounter",
				Context.MODE_WORLD_WRITEABLE);
		prefsEdtrCounter = sharedPreferenceCounter.edit();
		counter = sharedPreferenceCounter.getInt("COUNTER", 0);
		prefsEdtrCounter.putInt("COUNTER", counter+1);
		prefsEdtrCounter.commit();
	}

	/**
	 * <i><b>public String getSingleValue(String valueName, String prefName, String nullValue)</b></i> : method to get single value from SharedPreferences
	 * @param valueName : name of the value that needs to be retrieved from preferences.
	 * @param prefName : name of the preference from which value will be retrieved.
	 * @param nullValue : if value is null then method will return nullValue.
	 * @param count : current number of preferences 
	 * @return ArrayList<String> values for the specified key.
	 */
	public ArrayList<String> getSingleKeyValues(String valueName,
			String prefName, String nullValue, int count) {
		ArrayList<String> valueList = new ArrayList<String>();
		if (count != 0) {
			for (int i = 0; i < count; i++) {
				sharedPreference = context.getSharedPreferences(prefName + i,
						Context.MODE_WORLD_WRITEABLE);
				String returnValue = "empty";
				try {
					returnValue = sharedPreference.getString(valueName,
							nullValue);
					valueList.add(returnValue);
				} catch (Exception e) {
				}
			}
		}
		return valueList;
	}
	
	/**
	 * <i><b>public HashMap<String, String> getPreferenceValues(String prefName)</b></i> : returns all SharedPreference values.
	 * @param prefName : name of preference to be retrieved.
	 * @return HashMap<String, String> : containing all values of preference.
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> getAllPreferenceValues(String prefName){
		sharedPreference = context.getSharedPreferences(prefName,
				Context.MODE_WORLD_WRITEABLE);
		HashMap<String, Object> tempMap = null;
		try{
			tempMap = (HashMap<String, Object>) sharedPreference.getAll();
		}catch (Exception e) {
			// TODO: handle exception
		}
		return tempMap;
	}
	
	/**
	 * <i><b>public void deleteSharedPreference(String prefName)</b></i> : To delete single sharedpreference.
	 * @param prefName : name of the preference which is to be deleted.
	 */
	public void deleteSharedPreference(String prefName){
		sharedPreferenceDelete = context.getSharedPreferences(prefName,
				Context.MODE_WORLD_WRITEABLE);
		prefsEdtrDelete = sharedPreferenceDelete.edit();
		prefsEdtrDelete.remove(prefName);
		prefsEdtrDelete.clear();
		prefsEdtrDelete.commit();
	}
}
