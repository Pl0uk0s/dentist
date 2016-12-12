package com.kieslog.dbutil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDiskIOException;

import com.kieslog.util.L;

import java.util.ArrayList;

public class diaryDataSource {

    private SQLiteDatabase database;
    private DB_Helper dbHelper;
    private String[] allColumns = {
            DB_Helper.TABLE1_COLUMN_ID,
            DB_Helper.TABLE1_COLUMN_PRODID,
            DB_Helper.TABLE1_COLUMN_QUANTITY,
            DB_Helper.TABLE1_COLUMN_DATE,
            DB_Helper.TABLE1_COLUMN_TIME,
            DB_Helper.TABLE1_COLUMN_BELONGS,
            DB_Helper.TABLE1_COLUMN_DIARYID,
            DB_Helper.TABLE1_COLUMN_PRODUNIT
    };

    public diaryDataSource(Context context) {
        dbHelper = new DB_Helper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void deleteOld(int id) {
        database.delete(DB_Helper.TABLE1_COMMENTS, DB_Helper.TABLE1_COLUMN_ID + " = " + id, null);
    }

    public void addDiaryObject(diaryObj object) {

        ContentValues values = new ContentValues();

        values.put(DB_Helper.TABLE1_COLUMN_PRODID, object.getProdId());
        values.put(DB_Helper.TABLE1_COLUMN_QUANTITY, object.getQuantity());
        values.put(DB_Helper.TABLE1_COLUMN_DATE, object.getDate());
        values.put(DB_Helper.TABLE1_COLUMN_TIME, object.getTime());
        values.put(DB_Helper.TABLE1_COLUMN_BELONGS, object.getBelongs());
        values.put(DB_Helper.TABLE1_COLUMN_DIARYID, object.getDiaryId());
        values.put(DB_Helper.TABLE1_COLUMN_PRODUNIT, object.getProdUnit());
        try {
            database.insert(DB_Helper.TABLE1_COMMENTS, null, values);
        } catch (SQLiteDiskIOException e) {
            L.debug(e.toString());
        }
    }

    public ArrayList<diaryObj> getDiaryObj() {
        diaryObj singleObject = null;
        ArrayList<diaryObj> aDiaryObjs = new ArrayList<diaryObj>();

        Cursor cursor = database.query(DB_Helper.TABLE1_COMMENTS, null, null, null, null, null, null);

//        cursor.moveToFirst();
//        int count = cursor.getCount();
//        if (count > 0) {
        while (cursor.moveToNext()) {
            singleObject = cursorToComment(cursor);
            aDiaryObjs.add(singleObject);
        }
//        }

        // make sure to close the cursor
        cursor.close();

        return aDiaryObjs;
    }

    private diaryObj cursorToComment(Cursor cursor) {
        diaryObj object = new diaryObj();
        object.setId(cursor.getInt(0));
        object.setProdId(cursor.getInt(1));
        object.setQuantity(cursor.getInt(2));
        object.setDate(cursor.getString(3));
        object.setTime(cursor.getString(4));
        object.setBelongs(cursor.getInt(5));
        object.setDiaryId(cursor.getInt(6));
        object.setProdUnit(cursor.getString(7));

        return object;
    }

    public static void createDiaryObj(Context ctx, diaryObj object) {

        diaryDataSource dBdiary = new diaryDataSource(ctx);
        dBdiary.open();
        dBdiary.addDiaryObject(object);
        dBdiary.close();
    }

    public static ArrayList<diaryObj> retrieveDiaryObj(Context ctx) {

        diaryDataSource dBdiary = new diaryDataSource(ctx);
        dBdiary.open();
        ArrayList<diaryObj> aDiaryObjs = dBdiary.getDiaryObj();
        dBdiary.close();

        return aDiaryObjs;
    }

    public static void deleteOld(Context ctx, int id) {

        diaryDataSource dBdiary = new diaryDataSource(ctx);
        dBdiary.open();
        dBdiary.deleteOld(id);
        dBdiary.close();

    }
}
