package com.copypastapublishing.predictoe;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ghoulish on 1/6/2018.
 */

public class ExampleDBHelper extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "GutenbergBooks.db";
    private static final int DATABASE_VERSION = 1;
    public static final String BOOK_TABLE_NAME = "books";
    public static final String BOOK_COLUMN_ID = "_id";
    public static final String BOOK_COLUMN_TITLE = "title";
    public static final String BOOK_COLUMN_AUTHOR = "author";
    public static final String BOOK_COLUMN_SUBJECTS = "subjects";
    private static Map<String, String> map = new HashMap<String, String>();
    private static ExampleDBHelper helperAnchor;

    public ExampleDBHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        helperAnchor=this;
        db.execSQL("CREATE TABLE " + BOOK_TABLE_NAME + "(" + BOOK_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                BOOK_COLUMN_TITLE + " TEXT, " +
                BOOK_COLUMN_AUTHOR + " TEXT, " +
                BOOK_COLUMN_SUBJECTS + " INTEGER)"
        );
        ExampleDBHelper.MyAsyncTask async = new MyAsyncTask();
        InputStream inputStream = ResourcesSubclass.getContext().getResources().openRawResource(R.raw.gutenbergmapfull);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder result = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String JSONdata = result.toString();
        JSONArray title = new JSONArray();
        JSONArray url = new JSONArray();
        JSONArray author = new JSONArray();

        try {

            JSONObject object = new JSONObject(JSONdata);

            title = object.getJSONArray("books");
            //author = object2.getJSONArray("author");


        } catch (JSONException j) {
            j.printStackTrace();
        }
        Integer bookId = 0000;
        String name = null;
        String person = null;
        String link;
        for (int i = 0; i < title.length(); i++) {
            try {
                name = title.getString(i);
                link = url.getString(i);
                //person = author.getString(i);
            } catch (JSONException k) {
                k.printStackTrace();
            }
            System.out.println(name);



            addNewBook(name, person, bookId, i);


    }}

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        
    }
    public boolean insertPerson(String title, String author, int bookId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BOOK_COLUMN_TITLE, title);
        contentValues.put(BOOK_COLUMN_AUTHOR, author);
        contentValues.put(BOOK_COLUMN_SUBJECTS, bookId);
        db.insert(BOOK_TABLE_NAME, null, contentValues);
        return true;
    }
    public boolean updatePerson(Integer id, String title, String author, int bookId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BOOK_COLUMN_TITLE, title);
        contentValues.put(BOOK_COLUMN_AUTHOR, author);
        contentValues.put(BOOK_COLUMN_SUBJECTS, bookId);
        db.update(BOOK_TABLE_NAME, contentValues, BOOK_COLUMN_ID + " = ? ", new String[] { Integer.toString(id) } );
        return true;
    }
    public static Cursor getBook(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM " + BOOK_TABLE_NAME + " WHERE " +
                BOOK_COLUMN_ID + "=?", new String[] { Integer.toString(id) } );
        return res;
    }
    public Cursor getAllBooks() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM " + BOOK_TABLE_NAME, null );
        return res;
    }



    public static Cursor searchTasks(SQLiteDatabase db, String searchTxt)

    {
        Cursor cursor;
        String q = "select * from "+BOOK_TABLE_NAME+" where "+BOOK_COLUMN_TITLE+" or "+BOOK_COLUMN_AUTHOR+" Like '"+searchTxt+"%'";
        cursor = db.rawQuery(q,null);
        return cursor;
    }
    public boolean addNewBook(String title, String author, int bookId, int id) {


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(BOOK_COLUMN_TITLE, title);
        values.put(BOOK_COLUMN_AUTHOR, author);
        values.put(BOOK_COLUMN_SUBJECTS, bookId);
        db.insert(BOOK_TABLE_NAME, null, values);
        db.close();
        return true;
    }



    }


