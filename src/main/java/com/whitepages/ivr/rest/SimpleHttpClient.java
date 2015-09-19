package com.whitepages.ivr.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/***
 * A very simple HTTP client.
 */
public class SimpleHttpClient {

    public static class Response {
        int statusCode;
        String body;

        public Response(int statusCode, String body) {
            this.statusCode = statusCode;
            this.body = body;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }
    }
    
    /***
     * Make a GET request to a URL with the default timeout.
     * 
     * @param urlString
     * @return
     */
    public static Response get(String urlString) throws
			ProtocolException, MalformedURLException, IOException
    {
    	return get(urlString, 10000);
    }
	
    /***
     * Make a GET request to a URL with the specified timeout.
     * 
     * @param urlString
     * @param timeout	The timeout for the request in milliseconds.
     * @return
     */ 
    public static Response get(String urlString, int timeout) throws
    		ProtocolException, MalformedURLException, IOException
	{
		int statusCode = 0;
		StringBuffer responseBuffer = new StringBuffer();
		HttpURLConnection http = null;
		BufferedReader in = null;
		try
		{
			URL url = new URL(urlString);
			http = (HttpURLConnection) url.openConnection();
	 
			http.setRequestMethod("GET");
			http.setConnectTimeout(timeout);
            http.setReadTimeout(timeout);
	 
			statusCode = http.getResponseCode();
			
			in = new BufferedReader(
			        new InputStreamReader(http.getInputStream()));
			
			String inputLine = in.readLine();
			while (inputLine != null) {
				responseBuffer.append(inputLine);
				inputLine = in.readLine();
			}
		}
		finally
		{
			if (http != null)
			{
				http.disconnect();
			}
			if (in != null)
			{
				try {
					in.close();
				} catch (IOException e) {
					// TODO log this somewhere better
					e.printStackTrace();
				}
			}
			
		}

		Response httpResponse = new Response(statusCode, responseBuffer.toString());
        return httpResponse;
	}

}
