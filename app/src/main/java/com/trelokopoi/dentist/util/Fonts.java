package com.trelokopoi.dentist.util;

import android.content.Context;
import android.graphics.Typeface;

public class Fonts {
	public final static String ROBOTO_MEDIUM = "fonts/roboto_medium.ttf";
	public final static String ROBOTO_BOLD = "fonts/roboto_bold.ttf";
	public final static String ROBOTO_BLACK =  "fonts/roboto_black.ttf";
	public final static String BP_REPLAY_BOLD_ITALICS = "fonts/bp_replay_bold_italics.otf";
	public final static String SEGUI_BOLD = "fonts/segoeuib.ttf";
	
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
