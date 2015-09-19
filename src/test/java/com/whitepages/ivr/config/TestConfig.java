package com.whitepages.ivr.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestConfig {

  public static String get(String key){

    	Properties prop = new Properties();
    	InputStream input = null;
    	
    	try {
        
    		String filename = "config.properties";
    		input = TestConfig.class.getClassLoader().getResourceAsStream(filename);
    		if (input == null) {
    		    return null;
    		}

    		prop.load(input);

    		return prop.getProperty(key);
    	} catch (IOException ex) {
    		ex.printStackTrace();
        } finally {
        	if(input!=null){
        		try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        }
    	
    	return null;
    }
}
