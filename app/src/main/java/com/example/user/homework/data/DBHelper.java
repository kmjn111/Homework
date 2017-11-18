package com.example.user.homework.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    final static String TAG="HomeworkDB";

    public DBHelper(Context context) {
        super(context, HomeworkContract.DB_NAME, null, HomeworkContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG,getClass().getName()+".onCreate()");
        db.execSQL(HomeworkContract.Homework.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        Log.i(TAG,getClass().getName() +".onUpgrade()");
        db.execSQL(HomeworkContract.Homework.DELETE_TABLE);
        onCreate(db);
    }

    public void insertHomeworkBySQL(String name, String addr, String phone, String image) {
        try {
            String sql = String.format (
                    "INSERT INTO %s (%s, %s, %s, %s, %s) VALUES (NULL, '%s', '%s', '%s', '%s')",
                    HomeworkContract.Homework.TABLE_NAME,
                    HomeworkContract.Homework._ID,
                    HomeworkContract.Homework.KEY_NAME,
                    HomeworkContract.Homework.KEY_ADDR,
                    HomeworkContract.Homework.KEY_PHONE,
                    HomeworkContract.Homework.KEY_IMAGE,
                    name,
                    addr,
                    phone,
                    image);

            getWritableDatabase().execSQL(sql);
        } catch (SQLException e) {
            Log.e(TAG,"Error in inserting recodes");
        }
    }

    public Cursor getAllHomeworksBySQL() {
        String sql = "Select * FROM " + HomeworkContract.Homework.TABLE_NAME;
        return getReadableDatabase().rawQuery(sql,null);
    }

    public Cursor getSelectHomeworksBySQL(String _id) {
        String sql = "Select * FROM " + HomeworkContract.Homework.TABLE_NAME+" where _id="+_id;
        return getReadableDatabase().rawQuery(sql,null);
    }

    public void deleteHomeworkBySQL(String _id) {
        try {
            String sql = String.format (
                    "DELETE FROM %s WHERE %s = %s",
                    HomeworkContract.Homework.TABLE_NAME,
                    HomeworkContract.Homework._ID,
                    _id);
            getWritableDatabase().execSQL(sql);
        } catch (SQLException e) {
            Log.e(TAG,"Error in deleting recodes");
        }
    }

    public void updateHomeworkBySQL(String _id, String name, String addr, String phone, String image) {
        try {
            String sql = String.format (
                    "UPDATE  %s SET %s = '%s', %s = '%s', %s = '%s', %s = '%s' WHERE %s = %s",
                    HomeworkContract.Homework.TABLE_NAME,
                    HomeworkContract.Homework.KEY_NAME, name,
                    HomeworkContract.Homework.KEY_ADDR, addr,
                    HomeworkContract.Homework.KEY_PHONE, phone,
                    HomeworkContract.Homework.KEY_IMAGE, image,
                    HomeworkContract.Homework._ID, _id) ;
            getWritableDatabase().execSQL(sql);
        } catch (SQLException e) {
            Log.e(TAG,"Error in updating recodes");
        }
    }

    public long insertHomeworkByMethod(String name, String addr, String phone, String iamge) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HomeworkContract.Homework.KEY_NAME, name);
        values.put(HomeworkContract.Homework.KEY_ADDR, addr);
        values.put(HomeworkContract.Homework.KEY_PHONE,phone);
        values.put(HomeworkContract.Homework.KEY_IMAGE,iamge);

        return db.insert(HomeworkContract.Homework.TABLE_NAME,null,values);
    }

    public Cursor getAllHomeworksByMethod() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(HomeworkContract.Homework.TABLE_NAME,null,null,null,null,null,null);
    }

     public long deleteHomeworkByMethod(String _id) {
        SQLiteDatabase db = getWritableDatabase();

        String whereClause = HomeworkContract.Homework._ID +" = ?";
        String[] whereArgs ={_id};
        return db.delete(HomeworkContract.Homework.TABLE_NAME, whereClause, whereArgs);
    }

    public long updateHomeworkByMethod(String _id, String name, String addr, String phone, String image) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(HomeworkContract.Homework.KEY_NAME, name);
        values.put(HomeworkContract.Homework.KEY_ADDR, addr);
        values.put(HomeworkContract.Homework.KEY_PHONE,phone);
        values.put(HomeworkContract.Homework.KEY_IMAGE,image);

        String whereClause = HomeworkContract.Homework._ID +" = ?";
        String[] whereArgs ={_id};

        return db.update(HomeworkContract.Homework.TABLE_NAME, values, whereClause, whereArgs);
    }


}
