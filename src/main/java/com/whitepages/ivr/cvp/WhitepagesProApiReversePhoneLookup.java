package com.whitepages.ivr.cvp;

import java.util.HashMap;
import java.util.Map;

import com.audium.server.session.DecisionElementData;
import com.audium.server.voiceElement.DecisionElementBase;
import com.audium.server.voiceElement.ElementData;
import com.audium.server.voiceElement.ElementException;
import com.audium.server.voiceElement.ElementInterface;
import com.audium.server.voiceElement.ExitState;
import com.audium.server.voiceElement.Setting;
import com.audium.server.xml.DecisionElementConfig;
import com.whitepages.ivr.WhitepagesProIvrApi;

/***
 * A simple Cisco CVP element used to lookup the contact information for a given phone number.
 */
public class WhitepagesProApiReversePhoneLookup extends DecisionElementBase implements
		ElementInterface {
	
    private static final String ELEMENT_ERROR = "error";
    private static final String ELEMENT_VALUE = "value";
    private static final String ELEMENT_RESPONSE_STATUS_CODE = "responseStatusCode";
    private static final String ELEMENT_RESPONSE_BODY = "responseBody";
    
    private static final String SETTING_TIMEOUT = "timeout";
	private static final String SETTING_API_KEY = "apiKey";
	private static final String SETTING_CALLING_NUMBER = "callingNumber";
	
	private static final String EXIT_STATE_DONE = "done";
	private static final String EXIT_STATE_ERROR = "error";

    @Override
	public String getElementName() {
		return "Reverse Phone Lookup";
	}

	@Override
	public String getDisplayFolderName() {
		return "Whitepages PRO";
	}

	@Override
	public String getDescription() {
		return "Looks up the contact information for the given calling number.";

	}

    @Override()
	public Setting[] getSettings() throws ElementException {
		Setting[] configSettings = new Setting[3];

		configSettings[0] = new Setting(SETTING_CALLING_NUMBER, "Calling Number",
                "The number to lookup.",
                Setting.REQUIRED,
                Setting.SINGLE,
                Setting.SUBSTITUTION_ALLOWED,
                Setting.STRING);
		
        configSettings[1] = new Setting(SETTING_API_KEY, "API Key",
                "The Whitepages PRO API key to use for the request.",
                Setting.REQUIRED,
                Setting.SINGLE,
                Setting.SUBSTITUTION_ALLOWED,
                Setting.STRING);
        
        configSettings[2] = new Setting(SETTING_TIMEOUT, "Timeout in seconds",
                "The request timeout in seconds.",
                Setting.REQUIRED,
                Setting.SINGLE,
                Setting.SUBSTITUTION_ALLOWED,
                Setting.INT);
        configSettings[2].setDefaultValue("5000");

		return configSettings;
	}

	@Override
	public ElementData[] getElementData() throws ElementException {
		ElementData[] resultData = new ElementData[4];
		resultData[0] = new ElementData(ELEMENT_VALUE, "The spam score value returned.");
        resultData[1] = new ElementData(ELEMENT_ERROR, "The error message if an error occurred.");
        resultData[2] = new ElementData(ELEMENT_RESPONSE_STATUS_CODE, "The status code of the HTTP response.");
        resultData[3] = new ElementData(ELEMENT_RESPONSE_BODY, "The body of the HTTP response.");
		return resultData;
	}

	@Override
	public ExitState[] getExitStates() throws ElementException {
		ExitState[] exitStates = new ExitState[2];
		exitStates[0] = new ExitState(EXIT_STATE_DONE, "done", "done");
		exitStates[1] = new ExitState(EXIT_STATE_ERROR, "error", "error");
		return exitStates;
	}

	@Override
	public String doDecision(String name, DecisionElementData data)
			throws ElementException {

		DecisionElementConfig config = data.getDecisionElementConfig();
		String callingNumber = config.getSettingValue(SETTING_CALLING_NUMBER, data);
        String apiKey = config.getSettingValue(SETTING_API_KEY, data);
        int timeout = config.getIntSettingValue(SETTING_TIMEOUT, data);
        timeout = timeout * 1000;
        
        data.addToLog("Calling Number", callingNumber);
        data.addToLog("API Key", apiKey);
        data.addToLog("Timeout", String.valueOf(timeout));
        
        WhitepagesProIvrApi api = new WhitepagesProIvrApi(apiKey, timeout);
    	HashMap<String, Object> results = api.reversePhoneLookup(callingNumber);
        
    	data.setElementData(ELEMENT_RESPONSE_STATUS_CODE, Integer.toString(api.getLastStatusCode()));
    	data.setElementData(ELEMENT_RESPONSE_BODY, api.getLastBody());
    	
        if (results.size() == 0) {
        	String errorMessage = api.getLastErrorMessage();
        	data.logWarning(errorMessage);
    		data.setElementData(ELEMENT_ERROR, errorMessage);
        	return EXIT_STATE_ERROR;
        }

        for (Map.Entry<String, Object> entry : results.entrySet()) {
        	data.setElementData(entry.getKey(), entry.getValue().toString());
        }

		return EXIT_STATE_DONE;
	}
	
	
}
