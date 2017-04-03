
import java.util.HashMap;

import com.whitepages.ivr.WhitepagesProIvrApi;

/***
 * A simple API used to lookup information for a given phone number,
 * for use with Cisco UCCX. This is in the default package for convenient use 
 * from Cisco UCCX, since the UCCX editor does not allow importing fully 
 * qualified package names.
 */
public class WhitepagesProApi {
    
    private WhitepagesProIvrApi api;
    
    /***
     * Static version of spam lookup used for more convenient usage
     * from UCCX. Doesn't allow retrieving previous errors though.
     * 
     * @param phone
     * @param apiKey
     * @return
     */
    public static int lookupSpamScore(String phone, String apiKey) {
    	WhitepagesProIvrApi proApi = new WhitepagesProIvrApi(apiKey);
    	return proApi.lookupSpamScore(phone);
    }
    
    /***
     * Static version of reverse phone lookup used for more convenient usage
     * from UCCX. Doesn't allow retrieving previous errors though.
     * 
     * @param phone
     * @param apiKey
     * @return
     */
    public static HashMap<String, Object> reversePhoneLookup(String phone, String apiKey) {
    	WhitepagesProIvrApi proApi = new WhitepagesProIvrApi(apiKey);
    	return proApi.reversePhoneLookup(phone);
    }
    
    /***
     * Constructor with default timeout.
     * 
     * @param apiKey
     */
    public WhitepagesProApi(String apiKey) {
    	api = new WhitepagesProIvrApi(apiKey);
    }
    
    /***
     * Constructor.
     * 
     * @param apiKey
     */
    public WhitepagesProApi(String apiKey, int timeout) {
    	api = new WhitepagesProIvrApi(apiKey, timeout);
    }
    
    /***
     * Lookup the spam score for the given number.
     * 
     * @param apiKey
     */
    public int lookupSpamScore(String phone) {
    	return api.lookupSpamScore(phone);
    }
    
    /***
     * Perform reverse phone lookup for the given number.
     * 
     * @param apiKey
     */
    public HashMap<String, Object> reversePhoneLookup(String phone) {
    	return api.reversePhoneLookup(phone);
    }
    
    /***
     * Get the last HTTP status code returned from a lookup.
     * 
     * @return
     */
    public int getLastStatusCode() {
		return api.getLastStatusCode();
	}
    
    /***
     * Get the last response body returned from a lookup.
     * 
     * @return
     */
    public String getLastBody() {
		return api.getLastBody();
	}

    /***
     * Get the last error message returned from a lookup.
     * 
     * @return
     */
	public String getLastErrorMessage() {
		return api.getLastErrorMessage();
	}
	
}
