package com.trelokopoi.dentist.dbutil;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.trelokopoi.dentist.App;
import com.trelokopoi.dentist.util.L;

public class DB_Helper extends SQLiteOpenHelper {

	public static final String TABLE1_COMMENTS = "diary";
	public static final String TABLE1_COLUMN_ID = "_id";
	public static final String TABLE1_COLUMN_PRODID = "prodId";
	public static final String TABLE1_COLUMN_QUANTITY = "quantity";
	public static final String TABLE1_COLUMN_DATE = "date";
	public static final String TABLE1_COLUMN_TIME = "time";
	public static final String TABLE1_COLUMN_BELONGS = "belongs";
	    
	public static final String TABLE2_COMMENTS = "chats";
	public static final String TABLE2_COLUMN_ID = "_id";
	public static final String TABLE2_COLUMN_CHAT_FROM = "username1";
	public static final String TABLE2_COLUMN_CHAT_TO = "username2";
	public static final String TABLE2_COLUMN_DATE = "date";
	public static final String TABLE2_COLUMN_MSG = "msg";

	private static final String DATABASE_NAME = "dentist.db";
	//private static final int DATABASE_VERSION = 1;

	// Database creation sql statement

	private static final String DATABASE_CREATE1 = "create table "
		+ TABLE1_COMMENTS + "(" + TABLE1_COLUMN_ID + " integer primary key , "
		+ TABLE1_COLUMN_PRODID + " integer not null,"
		+ TABLE1_COLUMN_QUANTITY + " integer,"
		+ TABLE1_COLUMN_DATE + " text,"
		+ TABLE1_COLUMN_TIME + " text,"
		+ TABLE1_COLUMN_BELONGS + " integer not null"
		+");";

//	private static final String DATABASE_CREATE2 = "create table "
//		+ TABLE2_COMMENTS
//		+ "("
//		+ TABLE2_COLUMN_ID  + " text primary key, "
//		+ TABLE2_COLUMN_CHAT_FROM  + " text not null, "
//		+ TABLE2_COLUMN_CHAT_TO  + " text not null, "
//		+ TABLE2_COLUMN_DATE  + " text not null, "
//		+ TABLE2_COLUMN_MSG + " text not null, "
//		+ "UNIQUE("+TABLE2_COLUMN_ID+") ON CONFLICT REPLACE "
//		+");";

	public DB_Helper(Context context) {
		super(context, DATABASE_NAME, null, App.VERSION_ID);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE1);
//		database.execSQL(DATABASE_CREATE2);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		L.debug(DB_Helper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE1_COMMENTS);
//		db.execSQL("DROP TABLE IF EXISTS " + TABLE2_COMMENTS);
		onCreate(db);
	}
}
