package com.trelokopoi.dentist.util;

import android.content.Context;
import android.graphics.Typeface;

public class Fonts {
	public final static String LATO_REGULAR = "fonts/Lato-Regular.ttf";
	public final static String LATO_BOLD = "fonts/Lato-Bold.ttf";

	public static Typeface returnFont(Context context, String font) {
		 try {
			 
			 return Typeface.createFromAsset(context.getAssets(), font);
		 }  catch (Exception e) {
             L.debug("Could not get typeface '" + font
                     + "' because " + e.getMessage());
             return null;
         }
	}
}
