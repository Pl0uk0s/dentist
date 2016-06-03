package com.trelokopoi.core.util;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.trelokopoi.core.R;

// http://dmytrodanylyk.com/pages/blog/flat-button.html

public class CustomChoosePictureDialog extends Dialog{

	private Button dialogButtonTakePhoto;
	private Button dialogButtonLibrary;
	private Button dialogButtonCancel;
	private Button dialogButtonFacebook;

	public CustomChoosePictureDialog(final Context context) {
		super(context, R.style.DialogTheme);
		
    	requestWindowFeature(Window.FEATURE_NO_TITLE); 
    	setContentView(R.layout._custom_choose_picture_dialog);
    	
    	
    	dialogButtonTakePhoto = (Button) findViewById(R.id.dialogButtonTakePhoto);
    	dialogButtonLibrary = (Button) findViewById(R.id.dialogButtonLibrary);
    	dialogButtonCancel = (Button) findViewById(R.id.dialogButtonCancel);
    	dialogButtonFacebook = (Button) findViewById(R.id.dialogButtonFacebook);  
    	
    	dialogButtonFacebook.setVisibility(View.GONE);
    	
    	if (!Tools.isCameraAvailable()) {
    		dialogButtonTakePhoto.setVisibility(View.GONE);
    	}
    	
	}
	  
	public void setText(String msg) {
		TextView txt = (TextView) findViewById(R.id.txt);
		txt.setText(msg);
	}
	
	public void setTakePhoto(View.OnClickListener onClickListener) {		
		dialogButtonTakePhoto.setOnClickListener(onClickListener);
		//dialogButtonTakePhoto.setVisibility(View.VISIBLE);
	}
	
	public void setLibrary(View.OnClickListener onClickListener) {
		
		dialogButtonLibrary.setOnClickListener(onClickListener);
		//dialogButtonNo.setVisibility(View.VISIBLE);
	}    

	public void setFacebook(View.OnClickListener onClickListener) {
		dialogButtonFacebook.setOnClickListener(onClickListener);
	}
	
	public void setCancel(View.OnClickListener onClickListener) {
		
		dialogButtonCancel.setOnClickListener(onClickListener);
			//dialogButtonNo.setVisibility(View.VISIBLE);
	}	
	
	
}
