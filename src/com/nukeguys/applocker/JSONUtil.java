package com.nukeguys.applocker;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

public class JSONUtil 
{
	public static String makeJsonString(List<String> values)
	{
		JSONArray jsonArray = new JSONArray();
		for (String item : values)
			jsonArray.put(item);
		
		if(values.isEmpty())
			return null;
		else
			return jsonArray.toString();
	}
	
	public static List<String> makeListFromJson(String jsonString)
	{
		ArrayList<String> values = new ArrayList<String>();
		
		try
		{
			JSONArray jsonArray = new JSONArray(jsonString);
			for (int i=0; i<jsonArray.length(); i++)
				values.add(jsonArray.optString(i));
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		
		return values;
	}
}
