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

    //등록쿼리
    public void insertHomeworkBySQL(String name, String addr, String phone, String image, String time) {
        try {
            String sql = String.format (
                    "INSERT INTO %s (%s, %s, %s, %s, %s, %s) VALUES (NULL, '%s', '%s', '%s', '%s', '%s')",
                    HomeworkContract.Homework.TABLE_NAME,
                    HomeworkContract.Homework._ID,
                    HomeworkContract.Homework.KEY_NAME,
                    HomeworkContract.Homework.KEY_ADDR,
                    HomeworkContract.Homework.KEY_PHONE,
                    HomeworkContract.Homework.KEY_IMAGE,
                    HomeworkContract.Homework.KEY_TIME,
                    name,
                    addr,
                    phone,
                    image,
                    time);

            getWritableDatabase().execSQL(sql);
        } catch (SQLException e) {
            Log.e(TAG,"Error in inserting recodes");
        }
    }

    //모든 데이터 조회하기
    public Cursor getAllHomeworksBySQL() {
        String sql = "Select * FROM " + HomeworkContract.Homework.TABLE_NAME;
        return getReadableDatabase().rawQuery(sql,null);
    }

    //like 검색으로 데이터 조회하기
    public Cursor getAllHomeworksBySQLForName(String name) {
        String sql = "Select * FROM " + HomeworkContract.Homework.TABLE_NAME+ " where "+HomeworkContract.Homework.KEY_NAME+" like '%"+name+"%'";
        return getReadableDatabase().rawQuery(sql,null);
    }

    //ID값으로 단건 조회하기
    public Cursor getSelectHomeworksBySQL(String _id) {
        String sql = "Select * FROM " + HomeworkContract.Homework.TABLE_NAME+" where _id="+_id;
        return getReadableDatabase().rawQuery(sql,null);
    }

    //등록쿼리
    public long insertHomeworkByMethod(String name, String addr, String phone, String iamge, String time, double mlati, double mlong) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HomeworkContract.Homework.KEY_NAME, name);
        values.put(HomeworkContract.Homework.KEY_ADDR, addr);
        values.put(HomeworkContract.Homework.KEY_PHONE,phone);
        values.put(HomeworkContract.Homework.KEY_IMAGE,iamge);
        values.put(HomeworkContract.Homework.KEY_TIME,time);
        values.put(HomeworkContract.Homework.KEY_LATI,mlati);
        values.put(HomeworkContract.Homework.KEY_LONG,mlong);

        return db.insert(HomeworkContract.Homework.TABLE_NAME,null,values);
    }




}
