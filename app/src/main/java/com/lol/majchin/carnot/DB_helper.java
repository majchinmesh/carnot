package com.lol.majchin.carnot;

/**
 * Created by majch on 04-05-2017.
 */


import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

class DB_helper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="carnot.db";
    private static final int SCHEMA_VERSION=1;

    public DB_helper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE comments (id INTEGER PRIMARY KEY, postId INTEGER , name TEXT, email TEXT, body TEXT);");
        db.execSQL("CREATE TABLE photos (id INTEGER PRIMARY KEY  , albumId INTEGER ,  title TEXT, url TEXT, thumbnailUrl TEXT);");
        db.execSQL("CREATE TABLE todos (id INTEGER PRIMARY KEY , userId INTEGER , title TEXT, completed INTEGER );");
        db.execSQL("CREATE TABLE posts (id INTEGER PRIMARY KEY , userId INTEGER ,  title TEXT , body TEXT);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // no-op, since will not be called until 2nd schema
        // version exists
    }


    public void insertComment(Integer id , Integer pid , String name, String email, String body ) {
        ContentValues cv=new ContentValues();

        cv.put("id",id);
        cv.put("postId",pid);
        cv.put("name", name);
        cv.put("email", email);
        cv.put("body", body);

        // insert or replace
        getWritableDatabase().replace("comments", null , cv);

    }

    public void insertPhoto(Integer id , Integer aid , String title, String url, String thumbnailUrl ) {
        ContentValues cv=new ContentValues();

        cv.put("id",id);
        cv.put("albumId",aid);
        cv.put("title", title);
        cv.put("url", url);
        cv.put("thumbnailUrl", thumbnailUrl);

        getWritableDatabase().replace("photos", null , cv);
    }


    public void insertTodo(Integer id , Integer uid , String title, String completed ) {
        ContentValues cv=new ContentValues();

        cv.put("id",id);
        cv.put("userId",uid);
        cv.put("title", title);
        cv.put("completed", completed);

        getWritableDatabase().replace("todos", null , cv);
    }


    public void insertPost(Integer id , Integer uid , String title, String body ) {
        ContentValues cv=new ContentValues();

        cv.put("id",id);
        cv.put("userId",uid);
        cv.put("title", title);
        cv.put("body", body);

        getWritableDatabase().replace("posts", null , cv);

    }


    // used to test if database was really getting populated with data
    // we are basically returning the last entry from each table
    public String test_1() {

        Cursor c = getWritableDatabase().rawQuery("SELECT * FROM comments ORDER BY id DESC LIMIT 1", null);   //.execSQL("SELECT * FROM Table_Name LIMIT 5");
        c.moveToNext();
        return c.getString(c.getColumnIndex("email")) ;
    }

    public String test_2() {

        Cursor c = getWritableDatabase().rawQuery("SELECT * FROM photos ORDER BY id DESC LIMIT 1", null);   //.execSQL("SELECT * FROM Table_Name LIMIT 5");
        c.moveToNext();
        return c.getString(c.getColumnIndex("url")) ;
    }

    public String test_3() {

        Cursor c = getWritableDatabase().rawQuery("SELECT * FROM todos ORDER BY id DESC LIMIT 1", null);   //.execSQL("SELECT * FROM Table_Name LIMIT 5");
        c.moveToNext();
        return c.getString(c.getColumnIndex("title")) ;
    }

    public String test_4() {

        Cursor c = getWritableDatabase().rawQuery("SELECT * FROM posts ORDER BY id DESC LIMIT 1", null);   //.execSQL("SELECT * FROM Table_Name LIMIT 5");
        c.moveToNext();
        return c.getString(c.getColumnIndex("title")) ;
    }


    public int getNoOfRows(String TABLE_NAME , String FIELD_NAME  ) {
        String countQuery = "SELECT  * FROM " + TABLE_NAME+  " WHERE LENGTH(" + FIELD_NAME + ") < 35 " ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }


    public void removeAll()
    {
        // db.delete(String tableName, String whereClause, String[] whereArgs);
        // If whereClause is null, it will delete all rows.
        SQLiteDatabase db = getWritableDatabase(); // helper is object extends SQLiteOpenHelper
        db.delete("comments", null, null);
        db.delete("photos", null, null);
        db.delete("todos", null, null);
        db.delete("posts", null, null);
    }

    public void remove(String TABLE_NAME)
    {
        // db.delete(String tableName, String whereClause, String[] whereArgs);
        // If whereClause is null, it will delete all rows.
        SQLiteDatabase db = getWritableDatabase(); // helper is object extends SQLiteOpenHelper
        db.delete(TABLE_NAME, null, null);
    }

}