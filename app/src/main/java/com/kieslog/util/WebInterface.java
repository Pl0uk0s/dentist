package com.kieslog.util;

import android.content.Context;
import android.widget.Toast;
import com.kieslog.App;
import com.kieslog.BuildConfig;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;


public class WebInterface {
		
	
	public static boolean hasInternetConnection() {
		
		String result = WebInterface.getUrl(WebApi.URL_CHECKINTERNET);
		
		if (result.equals("1")) {
			
			L.debug(App.TAG, "Internet Connection Working!");
			return true;

		} else {
			
			L.debug(App.TAG, "No Internet Connection!");
			return false;
		}						
	}
	
	public static boolean hasInternetConnectionASync() {
		
		String result = "0";
		
		try {
		 URL url = new URL(WebApi.URL_CHECKINTERNET);
		 
		  HttpURLConnection con = (HttpURLConnection) url.openConnection();
		  
		  //'Connection Timeout' is only called at the beginning to child_header if the server is up or not.
		  con.setConnectTimeout(3000);
		  // 'Read Timeout' is to child_header a bad network all along the transfer.
		  con.setReadTimeout(10000);		  
		  result = readStream(con.getInputStream());
		  con.disconnect();
		  
		  } catch (Exception e) {
			  L.debug(App.TAG, "No or bad Internet Connection!");
		   return false;	
		  }
		
		if (result.equals("1")) {
			
			L.debug(App.TAG, "Internet Connection Working!");
			return true;

		} else {
			
			L.debug(App.TAG, "No Internet Connection!");
			return false;
		}			
		
		
	}
	
	private static String readStream(InputStream in) {
		  BufferedReader reader = null;
		  final StringBuilder sb = new StringBuilder();
		  try {
		    reader = new BufferedReader(new InputStreamReader(in));
		    String result = "";
		    String line = "";
		    while ((line = reader.readLine()) != null) {
		    	result+=line;
		    }
		    sb.append(result);
		  } catch (IOException e) {
		    e.printStackTrace();
		  } finally {
		    if (reader != null) {
		      try {
		        reader.close();
		      } catch (IOException e) {
		        e.printStackTrace();
		        }
		    }
		  }
		  
		  return sb.toString();
	} 	
	
	public static String getUrl(final String url) {
		
		L.debug(App.TAG, url);		
				
		final StringBuilder sb = new StringBuilder();
		
		Thread thread = new Thread(new Runnable() {
    	    public void run() 
    	        {
    	    	InputStream is = null;
    	    	BufferedReader reader = null;
    	        try 
    	        {
    	            is = (InputStream) new URL(url).getContent();
    	            reader = new BufferedReader(new InputStreamReader(is));
    	            
    	            String result, line = reader.readLine();
    	            result = line;
    	            while((line=reader.readLine())!=null){
    	                result+=line;
    	            }
    	            
    	            sb.append(result);
    	            
    	            
    	        } catch (Exception e)
    	        {
    	        // TODO: handle exception
    	        	e.printStackTrace();
    	        } finally {
    	        	if (is != null) {
    	        		
    	        		try {
    	        			is.close();
    	        		} catch (IOException e) {
    	        			// TODO Auto-generated catch block
    	        			e.printStackTrace();
    	        		}
    	        	}
    	        	
    	        	if (reader != null) {
    	        		
    	        		try {
    	        			reader.close();
    	        		} catch (IOException e) {
    	        			// TODO Auto-generated catch block
    	        			e.printStackTrace();
    	        		}
    	        	}
    	        }
    	    }
    	  });	
		
		thread.start();

		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		String result = sb.toString();
		L.debug(App.TAG, result);
		
		return result;
		
	}		
		
	public static JSONObject executeWeb(String url) {
										
		String result = WebInterface.getUrl(WebApi.encryptedUrl(url));
		
		String res;
		try {
			res = new String( new Crypt().decrypt( result ), "UTF-8" );
			//result = URLDecoder.decode(res,"UTF-8");
			result = res;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 		
		
		JSONObject json = null;
		
		try {
			json = new JSONObject(result.trim());
				
		} catch (JSONException e) {
					// TODO Auto-generated catch block
			try {
				json = new JSONObject("{}");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
						
		return json;				
		
	}
	
	public static JSONObject validateJSON(final Context applicationContext, String result) {
		L.debug("validate json");
		JSONObject json = null;
		boolean msgShownAlready = false;
		
		if (!result.isEmpty()) {
			
			try {
				json = new JSONObject(result);
				
			}
			catch (JSONException e) {
				WebInterface.catchJSONException(applicationContext, result, msgShownAlready);
				result = null;
				json = null;
			}	
			
			if (result != null && !result.isEmpty()) {		
				json = makeJSON(applicationContext, result, msgShownAlready);
			}
		}
		else {
			WebInterface.catchJSONException(applicationContext, result, msgShownAlready);
		}
				
		return json;
	}
		
	private static JSONObject makeJSON(Context applicationContext, String result, boolean msgShownAlready) {
		JSONObject json = null;
		
		try {
			json = new JSONObject(result);
			
		} catch (JSONException e) {
			WebInterface.catchJSONException(applicationContext, result, msgShownAlready);
		}	
		
		return json;
	}

	private static void catchJSONException(Context applicationContext, String result, boolean msgShownAlready) {
		if (BuildConfig.DEBUG) {
			Toast.makeText(applicationContext, result, Toast.LENGTH_LONG).show();
			
		}
		else if (!msgShownAlready) {
			//Toast.makeText(applicationContext, applicationContext.getResources().getString(R.string.error_backend), Toast.LENGTH_LONG).show();
			Toast.makeText(applicationContext, "No internet connection. Cannot connect to the server. Please reopen the app.", Toast.LENGTH_LONG).show();
		}
		
	}
	
}
