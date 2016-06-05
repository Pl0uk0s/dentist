package com.trelokopoi.dentist.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ProgressBar;
import java.io.UnsupportedEncodingException;

public class AsyncApiCall extends AsyncTask<String, Void, String> {
			
	private Context ctx;
	private int thread;
	private Bundle vars;
	private ProgressDialog progressDialog = null;
	private boolean displayProgressDialog = false;
	private String progressText = "";
	private boolean networkAvailable = false;
	
	public AsyncApiCall(int thread, AsyncApiCallOnTaskCompleted listener, boolean displayProgressDialog) {
		
		this.ctx=(Context) listener;
		this.thread=thread;
		this.vars = null;
		this.displayProgressDialog = displayProgressDialog;
		this.progressText = "";
	}
	
	public AsyncApiCall(int thread, AsyncApiCallOnTaskCompleted listener, boolean displayProgressDialog, String progressText) {
		
		this.ctx=(Context) listener;
		this.thread=thread;
		this.vars = null;
		this.displayProgressDialog = displayProgressDialog;
		this.progressText = progressText;
	}	
	
	public AsyncApiCall(int thread, Bundle vars, AsyncApiCallOnTaskCompleted listener, boolean displayProgressDialog) {
		
		this.ctx=(Context) listener;
		this.thread=thread;
		this.displayProgressDialog = displayProgressDialog;
		this.vars = vars;
		this.progressText = "";
	}	
	
	public AsyncApiCall(int thread, Bundle vars, AsyncApiCallOnTaskCompleted listener, boolean displayProgressDialog, String progressText) {
		
		this.ctx=(Context) listener;
		this.thread=thread;
		this.displayProgressDialog = displayProgressDialog;
		this.vars = vars;
		this.progressText = progressText;
	}		
	
	private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager=(ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}	
	
    @Override
    protected void onPreExecute() {
    	
    	networkAvailable = false;
    		
		if (isNetworkAvailable()) {    			
			networkAvailable = true;    							
		}    	
    	
    	if (displayProgressDialog) {    		
    		//this.progressDialog = MyProgressDialog.show((Context) this.listener, this.progressText, "", true);
    		this.progressDialog = new ProgressDialog(this.ctx); 
    		/*
    		if (this.progressText.length() == 0) {
    			this.progressText = this.ctx.getString(R.string.loading);
    		}*/
    		this.progressDialog.setMessage(this.progressText);
    		//this.progressDialog.getWindow().addFlags(flags);
    		
    		WindowManager.LayoutParams lp = this.progressDialog.getWindow().getAttributes();  
    		lp.dimAmount=0.0f;  
    		
    		this.progressDialog.getWindow().setAttributes(lp);      		
    		this.progressDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    		
    		this.progressDialog.show();
    		if (this.progressText.length() == 0) {
    		
    			this.progressDialog.setContentView(new ProgressBar(this.ctx));
    		}
    		
    	}
    			
    }
    
	@Override
    protected String doInBackground(String... urls) {

		if (networkAvailable) {
							
			if (WebInterface.hasInternetConnectionASync()) {
				
				if (urls[0].length() > 0) {
					
					return WebInterface.getUrl(WebApi.encryptedUrl(urls[0]));
				} else {
					return "1";
				}
			} else {
				return "0";
			}
			
				
			
		} else {
			return "0";
		}
    }

    @Override
    protected void onPostExecute(String result) {
    	try {
    		
			String res;
			try {
				if (!result.equals("1") && !result.equals("0")) {					
					res = new String( new Crypt().decrypt( result ), "UTF-8" );
					//result = URLDecoder.decode(res,"UTF-8");
					result = res;
					L.debug(result);
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}    		
    		
    		if (this.progressDialog != null && this.progressDialog.isShowing()) {
    			this.progressDialog.dismiss();
    		}
    	} catch (final IllegalArgumentException e) {
            // Handle or log or ignore
        } catch (final Exception e) {
            // Handle or log or ignore
        } finally {
            this.progressDialog = null;
        }  
    	    	
    	if (this.vars == null) {
    		
    		((AsyncApiCallOnTaskCompleted) this.ctx).onTaskCompleted(thread, result);
    	} else {
    		((AsyncApiCallOnTaskCompleted) this.ctx).onTaskCompleted(thread, vars, result);
    	}
    }

  }


