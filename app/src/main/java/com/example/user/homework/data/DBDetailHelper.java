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
        db.execSQL(HomeworkDetailContract.HomeworkDetail.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        Log.i(TAG,getClass().getName() +".onUpgrade()");
        db.execSQL(HomeworkDetailContract.HomeworkDetail.DELETE_TABLE);
        onCreate(db);
    }

    public void insertHomeworkBySQL( String parent, String image,String title, String price, String explain) {
        try {
            String sql = String.format (
                    "INSERT INTO %s (%s, %s, %s, %s, %s, %s) VALUES (NULL, '%s', '%s', '%s', '%s', '%s')",
                    HomeworkDetailContract.HomeworkDetail.TABLE_NAME,
                    HomeworkDetailContract.HomeworkDetail._ID,
                    HomeworkDetailContract.HomeworkDetail.KEY_PARENT,
                    HomeworkDetailContract.HomeworkDetail.KEY_IMAGE,
                    HomeworkDetailContract.HomeworkDetail.KEY_TITLE,
                    HomeworkDetailContract.HomeworkDetail.KEY_PRICE,
                    HomeworkDetailContract.HomeworkDetail.KEY_EXPLAIN,
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

    public Cursor getAllHomeworksBySQL(String parentId) {
        String sql = "Select * FROM " + HomeworkDetailContract.HomeworkDetail.TABLE_NAME+" where parent="+parentId;
        return getReadableDatabase().rawQuery(sql,null);
    }

    public Cursor getSelectHomeworksBySQL(String parentId, String subId) {
        String sql = "Select * FROM " + HomeworkDetailContract.HomeworkDetail.TABLE_NAME+" where parent="+parentId+" and _id="+subId;
        return getReadableDatabase().rawQuery(sql,null);
    }

    public void deleteHomeworkBySQL(String _id) {
        try {
            String sql = String.format (
                    "DELETE FROM %s WHERE %s = %s",
                    HomeworkDetailContract.HomeworkDetail.TABLE_NAME,
                    HomeworkDetailContract.HomeworkDetail._ID,
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
                    HomeworkDetailContract.HomeworkDetail.TABLE_NAME,
                    HomeworkDetailContract.HomeworkDetail.KEY_PARENT, parent,
                    HomeworkDetailContract.HomeworkDetail.KEY_IMAGE, image,
                    HomeworkDetailContract.HomeworkDetail.KEY_TITLE, title,
                    HomeworkDetailContract.HomeworkDetail.KEY_PRICE, price,
                    HomeworkDetailContract.HomeworkDetail.KEY_EXPLAIN, explain,
                    HomeworkDetailContract.HomeworkDetail._ID, _id) ;
            getWritableDatabase().execSQL(sql);
        } catch (SQLException e) {
            Log.e(TAG,"Error in updating recodes");
        }
    }

    public long insertHomeworkByMethod( String parent, String image, String title, String price, String explain) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HomeworkDetailContract.HomeworkDetail.KEY_PARENT, parent);
        values.put(HomeworkDetailContract.HomeworkDetail.KEY_IMAGE, image);
        values.put(HomeworkDetailContract.HomeworkDetail.KEY_TITLE, title);
        values.put(HomeworkDetailContract.HomeworkDetail.KEY_PRICE, price);
        values.put(HomeworkDetailContract.HomeworkDetail.KEY_EXPLAIN,explain);

        return db.insert(HomeworkDetailContract.HomeworkDetail.TABLE_NAME,null,values);
    }

    public Cursor getAllHomeworksByMethod() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(HomeworkDetailContract.HomeworkDetail.TABLE_NAME,null,null,null,null,null,null);
    }

    public long deleteHomeworkByMethod(String _id) {
        SQLiteDatabase db = getWritableDatabase();

        String whereClause = HomeworkDetailContract.HomeworkDetail._ID +" = ?";
        String[] whereArgs ={_id};
        return db.delete(HomeworkDetailContract.HomeworkDetail.TABLE_NAME, whereClause, whereArgs);
    }

    public long updateHomeworkByMethod(String _id, String title, String price, String explain) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(HomeworkDetailContract.HomeworkDetail.KEY_PARENT, title);
        values.put(HomeworkDetailContract.HomeworkDetail.KEY_IMAGE, title);
        values.put(HomeworkDetailContract.HomeworkDetail.KEY_TITLE, title);
        values.put(HomeworkDetailContract.HomeworkDetail.KEY_PRICE, price);
        values.put(HomeworkDetailContract.HomeworkDetail.KEY_EXPLAIN,explain);

        String whereClause = HomeworkDetailContract.HomeworkDetail._ID +" = ?";
        String[] whereArgs ={_id};

        return db.update(HomeworkDetailContract.HomeworkDetail.TABLE_NAME, values, whereClause, whereArgs);
    }

}
