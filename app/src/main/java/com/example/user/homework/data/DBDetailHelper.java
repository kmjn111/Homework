package com.example.user.homework.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBDetailHelper extends SQLiteOpenHelper {
    final static String TAG="HomeworkDetailDB";

    public DBDetailHelper(Context context) {
        super(context, HomeworkDetailContract.DB_NAME, null, HomeworkDetailContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG,getClass().getName()+".onCreate()");
        db.execSQL(HomeworkDetailContract.Homework.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        Log.i(TAG,getClass().getName() +".onUpgrade()");
        db.execSQL(HomeworkDetailContract.Homework.DELETE_TABLE);
        onCreate(db);
    }

    public void insertHomeworkBySQL( String parent, String image,String title, String price, String explain) {
        try {
            String sql = String.format (
                    "INSERT INTO %s (%s, %s, %s, %s, %s, %s) VALUES (NULL, '%s', '%s', '%s', '%s', '%s')",
                    HomeworkDetailContract.Homework.TABLE_NAME,
                    HomeworkDetailContract.Homework._ID,
                    HomeworkDetailContract.Homework.KEY_PARENT,
                    HomeworkDetailContract.Homework.KEY_IMAGE,
                    HomeworkDetailContract.Homework.KEY_TITLE,
                    HomeworkDetailContract.Homework.KEY_PRICE,
                    HomeworkDetailContract.Homework.KEY_EXPLAIN,
                    parent,
                    image,
                    title,
                    price,
                    explain);

            getWritableDatabase().execSQL(sql);
        } catch (SQLException e) {
            Log.e(TAG,"Error in inserting recodes");
        }
    }

    public Cursor getAllHomeworksBySQL() {
        String sql = "Select * FROM " + HomeworkDetailContract.Homework.TABLE_NAME;
        return getReadableDatabase().rawQuery(sql,null);
    }

    public void deleteHomeworkBySQL(String _id) {
        try {
            String sql = String.format (
                    "DELETE FROM %s WHERE %s = %s",
                    HomeworkDetailContract.Homework.TABLE_NAME,
                    HomeworkDetailContract.Homework._ID,
                    _id);
            getWritableDatabase().execSQL(sql);
        } catch (SQLException e) {
            Log.e(TAG,"Error in deleting recodes");
        }
    }

    public void updateHomeworkBySQL(String _id, String parent, String image, String title, String price, String explain) {
        try {
            String sql = String.format (
                    "UPDATE  %s SET %s = '%s', %s = '%s', %s = '%s',%s = '%s',%s = '%s' WHERE %s = %s",
                    HomeworkDetailContract.Homework.TABLE_NAME,
                    HomeworkDetailContract.Homework.KEY_PARENT, parent,
                    HomeworkDetailContract.Homework.KEY_IMAGE, image,
                    HomeworkDetailContract.Homework.KEY_TITLE, title,
                    HomeworkDetailContract.Homework.KEY_PRICE, price,
                    HomeworkDetailContract.Homework.KEY_EXPLAIN, explain,
                    HomeworkDetailContract.Homework._ID, _id) ;
            getWritableDatabase().execSQL(sql);
        } catch (SQLException e) {
            Log.e(TAG,"Error in updating recodes");
        }
    }

    public long insertHomeworkByMethod( String parent, String image, String title, String price, String explain) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HomeworkDetailContract.Homework.KEY_PARENT, parent);
        values.put(HomeworkDetailContract.Homework.KEY_IMAGE, image);
        values.put(HomeworkDetailContract.Homework.KEY_TITLE, title);
        values.put(HomeworkDetailContract.Homework.KEY_PRICE, price);
        values.put(HomeworkDetailContract.Homework.KEY_EXPLAIN,explain);

        return db.insert(HomeworkDetailContract.Homework.TABLE_NAME,null,values);
    }

    public Cursor getAllHomeworksByMethod() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(HomeworkDetailContract.Homework.TABLE_NAME,null,null,null,null,null,null);
    }

    public long deleteHomeworkByMethod(String _id) {
        SQLiteDatabase db = getWritableDatabase();

        String whereClause = HomeworkDetailContract.Homework._ID +" = ?";
        String[] whereArgs ={_id};
        return db.delete(HomeworkDetailContract.Homework.TABLE_NAME, whereClause, whereArgs);
    }

    public long updateHomeworkByMethod(String _id, String title, String price, String explain) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(HomeworkDetailContract.Homework.KEY_PARENT, title);
        values.put(HomeworkDetailContract.Homework.KEY_IMAGE, title);
        values.put(HomeworkDetailContract.Homework.KEY_TITLE, title);
        values.put(HomeworkDetailContract.Homework.KEY_PRICE, price);
        values.put(HomeworkDetailContract.Homework.KEY_EXPLAIN,explain);

        String whereClause = HomeworkDetailContract.Homework._ID +" = ?";
        String[] whereArgs ={_id};

        return db.update(HomeworkDetailContract.Homework.TABLE_NAME, values, whereClause, whereArgs);
    }

}
