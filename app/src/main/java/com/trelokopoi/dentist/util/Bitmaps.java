package com.trelokopoi.dentist.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

public class Bitmaps {
	
	public void set(Resources res, int resid, ImageView destination) {
	    int targetW = destination.getWidth();
	    int targetH = destination.getHeight();
	    // Get the dimensions of the bitmap
	    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
	    bmOptions.inPreferredConfig=Bitmap.Config.RGB_565;
	    bmOptions.inJustDecodeBounds = true;
	    //BitmapFactory.decodeFile(imagePath, bmOptions);
	    
		BitmapFactory.decodeResource(res, resid, bmOptions);
	     
	    int photoW = bmOptions.outWidth;
	    int photoH = bmOptions.outHeight;

	    // Determine how much to scale down the image
	    int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

	    // Decode the image file into a Bitmap sized to fill the View
	    bmOptions.inPreferredConfig=Bitmap.Config.RGB_565;
	    bmOptions.inJustDecodeBounds = false;
	    bmOptions.inSampleSize = scaleFactor;
	    bmOptions.inPurgeable = true;

	    Bitmap bitmap = BitmapFactory.decodeResource(res, resid, bmOptions);
	    destination.setImageBitmap(bitmap);
	}
	
}
