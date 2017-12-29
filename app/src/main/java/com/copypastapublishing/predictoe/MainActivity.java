package com.copypastapublishing.predictoe;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Paint;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;


public class MainActivity extends AppCompatActivity {
    static final int PICKFILE_RESULT_CODE = 1;
    String word;
    String input;
    private static String contents = null;
    private static String corpustext = null;
    private static String story = null;
    private static String lines = null;
    Hashtable<String, Vector<String>> markovChain = new Hashtable<String, Vector<String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        saveCorpus();
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        TextView storyMode = (TextView) findViewById(R.id.story_text);
        storyMode.setMovementMethod(new ScrollingMovementMethod());
        View.OnClickListener myListner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String update = null;
                switch (v.getId()) {
                    case R.id.paragraph_button:
                        try {
                            update = "\n\t \t \t";
                            update(update);
                        } catch (NullPointerException n) {
                            n.printStackTrace();
                        }
                        break;
                    case R.id.period_button:
                        try {
                            update = ". ";
                            update(update);
                        } catch (NullPointerException n) {
                            n.printStackTrace();
                        }
                        break;
                    case R.id.exclamation_button:
                        try {
                            update = "! ";
                            update(update);
                        } catch (NullPointerException n) {
                            n.printStackTrace();
                        }
                        break;
                    case R.id.question_button:
                        try {
                            update = "? ";
                            update(update);
                        } catch (NullPointerException n) {
                            n.printStackTrace();
                        }
                        break;
                    case R.id.comma_button:
                        try {
                            update = ", ";
                            update(update);
                        } catch (NullPointerException n) {
                            n.printStackTrace();
                        }
                        break;
                    case R.id.backspace_button:
                        try {
                            try {
                                String[] backspace = story.split(" ");
                                String[] backspace2 = lines.split(" ");
                                String redone = "";
                                String redone2 = "";
                                for (int z = 0; z < backspace.length - 1; z++) {
                                    redone = redone + " " + backspace[z];
                                    lines = redone;
                                }
                                setLines(lines);
                                String tip = MarkovChain.tipFinder(lines);
                                fillList(tip);
                            } catch (NullPointerException n) {
                                n.printStackTrace();
                            }
                            break;
                        } catch (NullPointerException n) {
                            n.printStackTrace();
                        }
                        break;
                }
            }
        };
        Button b2 = (Button) findViewById(R.id.paragraph_button);
        Button b3 = (Button) findViewById(R.id.period_button);
        Button b4 = (Button) findViewById(R.id.backspace_button);
        Button b5 = (Button) findViewById(R.id.comma_button);
        Button b6 = (Button) findViewById(R.id.question_button);
        Button b7 = (Button) findViewById(R.id.exclamation_button);
        b2.setOnClickListener(myListner);
        b3.setOnClickListener(myListner);
        b4.setOnClickListener(myListner);
        b5.setOnClickListener(myListner);
        b6.setOnClickListener(myListner);
        b7.setOnClickListener(myListner);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tool_menu, menu);
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICKFILE_RESULT_CODE && resultCode == Activity.RESULT_OK) {
            contents = null;
            Uri content_describer = data.getData();
            if (corpustext == null) {
                corpustext = getRealPathFromURI(content_describer);
            } else {
                corpustext = corpustext + ", " + getRealPathFromURI(content_describer);
            }
            TextView corpus = (TextView) findViewById(R.id.corpus_info);
            corpus.setText(corpustext);
            //get the path
            Log.d("Path???", content_describer.getPath());
            BufferedReader reader = null;
            try {
                // open the user-picked file for reading:
                InputStream in = this.getContentResolver().openInputStream(content_describer);
                // now read the content:
                reader = new BufferedReader(new InputStreamReader(in));
                String line;
                StringBuilder builder = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                // Do something with the content in
                contents = builder.toString();
                MarkovChain.addWords(contents, markovChain);
            } catch (FileNotFoundException e) {
                ((TextView) findViewById(R.id.line_text)).setText("FileNotFoundException");
                e.printStackTrace();
                e.printStackTrace();
            } catch (IOException e) {
                ((TextView) findViewById(R.id.line_text)).setText("IOException");
                e.printStackTrace();
            } catch (RuntimeException r) {
                r.printStackTrace();
            } finally {
                setLines(lines);
                update("\n\t \t \t");
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        ((TextView) findViewById(R.id.line_text)).setText("IOException");
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public String getRealPathFromURI(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public void load(MenuItem item) {
        try {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("text/plain");
            startActivityForResult(intent, PICKFILE_RESULT_CODE);
        } catch (NullPointerException n) {
            n.printStackTrace();
        } catch (RuntimeException r) {
            r.printStackTrace();
        }
    }

    public void share(MenuItem item) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, story);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.share)));
    }

    public void clear(MenuItem item) {
        TextView tipLine = (TextView) findViewById(R.id.line_text);
        TextView storyMode = (TextView) findViewById(R.id.story_text);
        tipLine.setText("\t \t \t ");
        storyMode.setText("\t \t \t ");
        lines = "";
        story = "\t \t \t";
        try {
            fillList("\n\t \t \t");
        } catch (NullPointerException n) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("You must upload a text file.");
            builder.setTitle("Error");
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    public void clearContents(MenuItem item) {

        markovChain.clear();
        contents=null;
        fillList("");
        TextView corpus = (TextView) findViewById(R.id.corpus_info);
        corpus.setText("");
        corpustext=null;
    }

    public void setLines(String newLine) {
        TextView tipLine = (TextView) findViewById(R.id.line_text);
        TextView storyMode = (TextView) findViewById(R.id.story_text);
        storyMode.setPaintFlags(storyMode.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        if (lines == null) {
            storyMode.setText("");
            storyMode.setMovementMethod(new ScrollingMovementMethod());
            tipLine.setText("");
            lines = newLine;
            story = newLine;
        } else {
            storyMode.setText(lines);
            storyMode.setMovementMethod(new ScrollingMovementMethod());
            tipLine.setText(lines.replace("\n", ""));
            lines = newLine;
            story = newLine;

        }
    }
    public void saveCorpus() {
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.corpus);
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                try {
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    contents = result.toString();
                    System.out.println(contents);
                    try {
                        MarkovChain.addWords(contents, markovChain);
                    } catch (NullPointerException n) {

                    }
                } finally {
                    reader.close();
                }
            } finally {
                inputStream.close();
            }
        } catch (IOException e) {
        }
    }
    public void update(String tip) {
        if (lines == null) {
            lines = "\n\t \t \t " + TextManipulate.toTitleCase(tip);
            setLines(lines);
            fillList(tip);
        } else if (tip == ". " || tip == "? " || tip == "! " || tip == ", " || tip == "\n\t \t \t") {
            setLines(lines + tip);
            fillList(tip);

        } else {
            lines = lines + " " + tip;
            setLines(lines);
            tip = MarkovChain.tipFinder(lines);
            fillList(tip);
        }
    }

    public void fillList(String tip) {
        ArrayList<String> ink = new ArrayList<String>();
        if (tip == ". " || tip == "? " || tip == "! " || tip == ", " || tip == "\n\t \t \t") {
            for (int i = 0; i < 60 ; i++) {
                String blink = TextManipulate.rndWord(contents);
                if (tip != ", ") {
                    try {
                        blink = blink.substring(0, 1).toUpperCase() + blink.substring(1);
                    } catch (StringIndexOutOfBoundsException s) {
                        continue;
                    }
                }
                ink.add(blink);
// add elements to al, including duplicates
                Set<String> hs = new HashSet<>(ink);
                hs.addAll(ink);
                ink.clear();
                ink.addAll(hs);
            }

            updateList(ink);
        } else {
            for (int i = 0; i < 60; i++) {
                String blink = MarkovChain.generateInk(markovChain, tip);
                System.out.println(blink);
                if (blink != " " && blink != null && blink != "") {
                    ink.add(blink);
                }
                // add elements to al, including duplicates
                Set<String> hs = new HashSet<>(ink);
                hs.addAll(ink);
                ink.clear();
                ink.addAll(hs);
                updateList(ink);
            }
        }
    }

    public void updateList(ArrayList<String> ink) {
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ink);
        final ListView listView = (ListView) findViewById(R.id.ListView);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) listView.getItemAtPosition(position);
                update(item);
            }
        });
    }

}