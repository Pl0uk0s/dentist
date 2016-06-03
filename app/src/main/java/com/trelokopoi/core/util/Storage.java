package com.trelokopoi.core.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.util.ArrayMap;

import org.json.JSONArray;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Storage {

	public static boolean saveImageToInternalStorage(Context ctx, Bitmap image, String filename) {

		try {
			// Use the compress method on the Bitmap object to write image to
			// the OutputStream
			FileOutputStream fos = ctx.openFileOutput(filename, Context.MODE_PRIVATE);

			String fileExt = getFileExtension(filename);
			
			// Writing the bitmap to the output stream
			if (fileExt.equals(".png")) {				
				image.compress(Bitmap.CompressFormat.PNG, 100, fos);
			} else if (fileExt.equals(".jpg")) {
				image.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			}
			fos.close();

			return true;
		} catch (Exception e) {
			L.debug("saveToInternalStorage()"+ e.getMessage());
			return false;
		}
	}
	
	public static Bitmap retrieveBitmapFromInternalStorage(Context ctx, String filename) {

		Bitmap image = null;
		
		try {
		File filePath = ctx.getFileStreamPath(filename);
		
		FileInputStream fi = new FileInputStream(filePath);
		image = BitmapFactory.decodeStream(fi);
		} catch (Exception ex) {
			L.debug("retrieveBitmapFromInternalStorage() on internal storage" +ex.getMessage());
		}
		
		return image;
	}	
	
	public static File retrieveFileFromInternalStorage(Context ctx, String filename) {

		L.debug(filename);

		File filePath = ctx.getFileStreamPath(filename);
		
		if (filePath.exists()) {		
			return filePath;
		} else {
			return null;
		}
	}
	
	public static String getFileNameFromPath(String path) {
		if (path != null && path.length() > 0) {			
			return path.substring(path.lastIndexOf('/')+1, path.length());
		} else return "";
	}
	
	public static String getFileExtension(String path) {
		if (path.lastIndexOf(".") > -1) {			
			return path.substring(path.lastIndexOf(".")).toLowerCase();
		} else {
			return "";
		}
	}
	
	public static void deleteFilesExcept(Context ctx, List<String> exceptList) {
		String[] files = ctx.fileList();
		List<String> fileList = new ArrayList(Arrays.asList(files));
		
		for(String ele : exceptList){
		    if(fileList.contains(ele)){
		    	fileList.remove(ele);
		    }
		}	
		
		for(String ele : fileList){
			
			String fileExt = getFileExtension(ele);
			L.debug(ele);
			L.debug(fileExt);
			if (fileExt.equals(".jpg") || fileExt.equals(".png")) {
				L.debug("delete"+ele);
				ctx.deleteFile(ele);
			}
		}

		
	}
	
	public static void deleteFilesFromExternalStorageExcept(String filepath, JSONArray exceptList, String profilePic) {
			
		final ArrayMap<String,String> exceptFileList= new ArrayMap<String,String>();
		  
		if (exceptList != null) { 
		   for (int i=0;i<exceptList.length();i++){ 
		    //listdata.add(exceptList.get(i).toString());
			   String value = exceptList.opt(i).toString();
			   if (value != null && value.length() > 0) {				   
				   exceptFileList.put(value, value);
			   }
		   } 
		} 	
		
		if (profilePic != null) {
			if (profilePic.length() > 0) {
				exceptFileList.put(profilePic, profilePic);
			}
		}
		exceptFileList.put(".nomedia", ".nomedia");
				
		L.debug("exceptFileList:"+exceptFileList.toString());
		File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),filepath);        
		File file[] = f.listFiles(new FilenameFilter() {
	        @Override
	        public boolean accept(File dir, String name) {
	        	
	            if (exceptFileList.containsKey(name)) {
	            	return false;
	            }
	    
	            if(name.endsWith("png")) {
	            	return false;
	            }
	            
	            return true;
	        }
	    });
				
		if (file != null && file.length > 0) {
			
			for (int i=0; i < file.length; i++)
			{
				
				file[i].delete();
			}		
		}
		
	}
		
	public static File saveImageToExternalStorage(Bitmap image, String path, String filepath) {

		final File pathExist = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),path);		
		
		if (!pathExist.exists()) {
			pathExist.mkdirs();
			File nomediaFile = new File(pathExist, ".nomedia");
			try {
				nomediaFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		
		 final File destination = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),filepath);
		 
			OutputStream fOut=null;
			
			try {
				fOut = new BufferedOutputStream(new FileOutputStream(destination));
				image.compress(Bitmap.CompressFormat.JPEG, 80, fOut);
				fOut.flush();
				fOut.close();		
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		return destination;
	}
	
	public static File saveProfilePicToExternalStorage(Bitmap image, String path, String filepath) {

		final File pathExist = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),path);		
		
		if (!pathExist.exists()) {
			pathExist.mkdirs();
			File nomediaFile = new File(pathExist, ".nomedia");
			try {
				nomediaFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		
		 final File destination = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),filepath);
		 
			OutputStream fOut=null;
			
			try {
				fOut = new BufferedOutputStream(new FileOutputStream(destination));
				image.compress(Bitmap.CompressFormat.PNG, 80, fOut);
				fOut.flush();
				fOut.close();		
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		return destination;
	}	
	
	public static File saveCategoryPicToExternalStorage(Bitmap image, int groupId, int categoryId, String extra) {

		String path = "quizdom_categories";
		if (extra.equals(null) || extra == null) {
			extra = "";
		}
		
		String filename = path+File.separator+"group_"+groupId+"_category_"+categoryId+extra+".png";	
		
		final File pathExist = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),path);		
		
		if (!pathExist.exists()) {
			pathExist.mkdirs();
			File nomediaFile = new File(pathExist, ".nomedia");
			try {
				nomediaFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		
		 final File destination = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),filename);
		 
			OutputStream fOut=null;
			
			try {
				fOut = new BufferedOutputStream(new FileOutputStream(destination));
				image.compress(Bitmap.CompressFormat.PNG, 80, fOut);
				fOut.flush();
				fOut.close();		
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		return destination;
	}		
	
	public static File retrieveCategoryPicFromExternalStorage(int groupId, int categoryId, String extra) {
		
		String path = "quizdom_categories";
		
		if (extra == null || extra.equals(null)) {
			extra = "";
		}
						
		String filename = path+File.separator+"group_"+groupId+"_category_"+categoryId+extra+".png";	
		//L.debug("retrieveCategoryPicFromExternalStorage:"+filename);
		File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),filename);
				
		if (file.exists()) {

			// return null after seven days so categories get recreated
			int days = 7 * 24 * 60 * 60 * 1000;
			long diff = new Date().getTime() - file.lastModified();
			
			//L.debug("groupId:"+groupId + " categoryId:"+categoryId+" " + extra + " " + diff + " " + days);	
			if (diff > days) {
				return null;
			}
							
			return file;
		} else {
			return null;
		}
	}	
	
	public static Bitmap retrieveBitmapFromExternalStorage(Context ctx, String filename) {

		Bitmap image = null;
		
		try {
		final File destination = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),filename);
		
		FileInputStream fi = new FileInputStream(destination);
		image = BitmapFactory.decodeStream(fi);
		} catch (Exception ex) {
			L.debug("retrieveBitmapFromInternalStorage() on internal storage" +ex.getMessage());
		}
		
		return image;
	}	
	
	public static void deleteFromExternalStorage(Context ctx, String filename) {
		L.debug("delete:"+filename);
		final File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),filename);
		//if (file.exists()) {
			L.debug("file exists:"+file.getPath());
			file.delete();
		//}		
			
	}		
	
	public static File retrieveFileFromExternalStorage(String filename) {
		
		File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),filename);
		if (file.exists()) {
			return file;
		} else {
			return null;
		}
	}
}
