package com.copypastapublishing.predictoe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.copypastapublishing.predictoe.ExampleDBHelper.BOOK_COLUMN_AUTHOR;
import static com.copypastapublishing.predictoe.ExampleDBHelper.BOOK_COLUMN_SUBJECTS;
import static com.copypastapublishing.predictoe.ExampleDBHelper.BOOK_COLUMN_TITLE;
import static com.copypastapublishing.predictoe.ExampleDBHelper.BOOK_TABLE_NAME;

public class GutenbergActivity extends AppCompatActivity {
    private static List<String> titles = new ArrayList<String>();
    private static List<String> urls = new ArrayList<String>();
    private static Map<String, String> map = new HashMap<String, String>();

    ExampleDBHelper dbHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gutenberg);
        dbHelper = new ExampleDBHelper(this);

        ExampleDBHelper newDB = new ExampleDBHelper(GutenbergActivity.this);
        SQLiteDatabase sdb = newDB.getReadableDatabase();
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.list_white_text, R.id.list_content, database);
        final ListView listView = (ListView) findViewById(R.id.ListView);
        listView.setAdapter(arrayAdapter);

        }
    }


}





