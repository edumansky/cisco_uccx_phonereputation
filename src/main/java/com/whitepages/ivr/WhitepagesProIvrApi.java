package com.whitepages.ivr;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.HashMap;


import com.whitepages.ivr.json.JSONObject;
import com.whitepages.ivr.rest.JsonMap;
import com.whitepages.ivr.rest.SimpleHttpClient;
import com.whitepages.ivr.rest.SimpleHttpClient.Response;

/***
 * A simple API to interact with the WhitepagesPRO API, for use with Cisco IVRs.
 */
public class WhitepagesProIvrApi {

	private static final String BASE_URL = "http://proapi.whitepages.com/";
	
	private static final String REPUTATION_API_VERSION = "2.1";
    private static final String REPUTATION_API_URL = BASE_URL + REPUTATION_API_VERSION + "/phone.json?phone_number=%s&api_key=%s";
	
    private static final String REVERSE_LOOKUP_API_VERSION = "3.0";
    private static final String REVERSE_LOOKUP_API_URL = BASE_URL + REVERSE_LOOKUP_API_VERSION + "/phone?phone=%s&api_key=%s";
   
    private static final int DEFAULT_TIMEOUT = 5000;
    
    private String apiKey;
    private int timeout = DEFAULT_TIMEOUT;
    private int lastStatusCode = -1;
    private String lastErrorMessage = null;
    private String lastBody = null;
    
    /***
     * Constructor
     * 
     * @param apiKey
     */
	public WhitepagesProIvrApi(String apiKey) {
    	this.apiKey = apiKey;
    }
   
    /***
     * Constructor
     * 
     * @param apiKey
     * @param timeout
     */
    public WhitepagesProIvrApi(String apiKey, int timeout) {
    	this.apiKey = apiKey;
    	this.timeout = timeout;
    }
    
    /***
     * Lookup the spam score for a given phone number.
     * 
     * @param phone
     * @return
     */
    public int lookupSpamScore(String phone) {
    	int score = -1;
    	
    	String fullUrl = String.format(REPUTATION_API_URL, phone, apiKey);
    	Response response = null;
		try {
			response = SimpleHttpClient.get(fullUrl, timeout);
		} catch (ProtocolException e) {
			lastErrorMessage = e.getLocalizedMessage();
		} catch (MalformedURLException e) {
			lastErrorMessage = e.getLocalizedMessage();
		} catch (IOException e) {
			lastErrorMessage = e.getLocalizedMessage();
		}
		
		if (response != null) {
    		lastStatusCode = response.getStatusCode();
    		lastBody = response.getBody();
    	}
        
        if (response == null ||
        		response.getStatusCode() < 200 || response.getStatusCode() >= 300) {
        	return -1;
        }
        
        HashMap<String, Object> values = JsonMap.map("whitepages", new JSONObject(response.getBody()));
        score = Integer.parseInt(values.get("whitepages.results[0].reputation.level").toString());

    	return score;
    }
    
    /***
     * Lookup the contact information for a given phone number.
     * 
     * @param phone
     * @return
     */
    public HashMap<String, Object> reversePhoneLookup(String phone) {
    	HashMap<String, Object> result = new HashMap<String, Object>();
    	
    	String fullUrl = String.format(REVERSE_LOOKUP_API_URL, phone, apiKey);
    	Response response = null;
		try {
			response = SimpleHttpClient.get(fullUrl, timeout);
		} catch (ProtocolException e) {
			lastErrorMessage = e.getLocalizedMessage();
		} catch (MalformedURLException e) {
			lastErrorMessage = e.getLocalizedMessage();
		} catch (IOException e) {
			lastErrorMessage = e.getLocalizedMessage();
		}
		
		if (response != null) {
    		lastStatusCode = response.getStatusCode();
    		lastBody = response.getBody();
    	}
        
        if (response == null ||
        		response.getStatusCode() < 200 || response.getStatusCode() >= 300) {
        	return result;
        }
        
        result = JsonMap.map("whitepages", new JSONObject(response.getBody()));
    	return result;
    }
    
    /***
     * Get the last HTTP status code returned from a lookup.
     * 
     * @return
     */
    public int getLastStatusCode() {
		return lastStatusCode;
	}

    
    /***
     * Get the last response body returned from a lookup.
     * 
     * @return
     */
    public String getLastBody() {
		return lastBody;
	}

	/***
     * Get the last error message returned from a lookup.
     * 
     * @return
     */
	public String getLastErrorMessage() {
		return lastErrorMessage;
	}
}