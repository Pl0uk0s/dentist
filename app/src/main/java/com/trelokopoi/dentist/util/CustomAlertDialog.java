package com.trelokopoi.dentist.util;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.trelokopoi.dentist.R;

// http://dmytrodanylyk.com/pages/blog/flat-button.html

public class CustomAlertDialog extends Dialog{

	private Button dialogButtonYes;
	private Button dialogButtonNo;

	public CustomAlertDialog(Context context) {
		super(context, R.style.DialogTheme);
		
    	requestWindowFeature(Window.FEATURE_NO_TITLE); 
    	setContentView(R.layout._custom_alert_dialog);
    	
    	
    	dialogButtonYes = (Button) findViewById(R.id.dialogButtonYes);
    	dialogButtonNo = (Button) findViewById(R.id.dialogButtonNo);
    	/*
    	dialogButtonYes.setVisibility(View.GONE);
    	dialogButtonNo.setVisibility(View.GONE);
    	*/
	}
	
	public void setText(String msg) {
		TextView txt = (TextView) findViewById(R.id.txt);
		txt.setText(msg);
	}
	
	public void setYes(View.OnClickListener onClickListener) {		
		dialogButtonYes.setOnClickListener(onClickListener);
		dialogButtonYes.setVisibility(View.VISIBLE);
	}
	
	public void setNo(View.OnClickListener onClickListener) {
		
		dialogButtonNo.setOnClickListener(onClickListener);
		dialogButtonNo.setVisibility(View.VISIBLE);
	}    

}
