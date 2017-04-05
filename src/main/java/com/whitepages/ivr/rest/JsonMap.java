package com.whitepages.ivr.rest;

import java.util.HashMap;

import com.whitepages.ivr.json.JSONArray;
import com.whitepages.ivr.json.JSONObject;

/***
 * Represents a JSON object as a map of key/value pairs.
 */
public class JsonMap {
	
	/***
	 * <p>
	 * Takes a JSONObject and returns all the data within as a HashMap with dot syntax keys.
	 * For arrays, this will also automatically encode the JSONArray object itself and
	 * the length of the array.
	 * </p>
	 * <p>
	 * e.g. { "data": { "type": "names", "myArray": [ { "name": "Bob" }, { "name": "Chuck" } ] } }
	 * 		would be converted to the key/value pairs:
	 * 		"data.type -> "names"
	 * 		"data.myArray -> JSONArray
	 * 		"data.myArray.length" -> 2
	 * </p>
	 * 
	 * @param 	baseKey		base key name to use for the key string values.
	 * @param 	jsonObject	the json object to map.
	 * @return 	HashMap<String, Object>	A map of the JSON data.
	 */
	public static HashMap<String, Object> map(String baseKey, JSONObject jsonObject) {
		HashMap<String, Object> values = new HashMap<String, Object>();
		for (Object keyObj : jsonObject.keySet()) {
			String key = keyObj.toString();
		    Object element = jsonObject.get(key);
		    
		    values.putAll(evaluateElementByType(baseKey, key, element));
		}
		return values;
	}
	
	private static HashMap<String, Object> evaluateElementByType(String baseKey, String key, Object element) {
		HashMap<String, Object> values = new HashMap<String, Object>();
		if (element instanceof String) {
			values.put(baseKey + "." + key, element.toString());
	    }
	    else if (element instanceof Double) {
	    	values.put(baseKey + "." + key, ((Double) element).doubleValue());
	    }
	    else if (element instanceof Float) {
	    	values.put(baseKey + "." + key, ((Float) element).floatValue());
	    }
	    else if (element instanceof Number) {
	    	values.put(baseKey + "." + key, ((Number) element).intValue());
	    }
	    else if (element instanceof Boolean) {
	    	values.put(baseKey + "." + key, element.toString());
	    }
	    else if (element instanceof JSONArray) {
	    	JSONArray elementArray = (JSONArray)element;
	    	values.put(baseKey + "." + key + ".length", "" + elementArray.length());
	    	values.put(baseKey + "." + key, elementArray);
	    	
	    	// Adds each of the array items as a key/value pair.
	    	for (int i = 0; i < elementArray.length(); i++)
	    	{
	    		values.putAll(evaluateElementByType(baseKey, key + "[" + i + "]", elementArray.get(i)));
	    	}
	    }
	    else if (element instanceof JSONObject) {
	    	values.putAll(map(baseKey + "." + key, (JSONObject)element));
	    }
		return values;
	}
}
