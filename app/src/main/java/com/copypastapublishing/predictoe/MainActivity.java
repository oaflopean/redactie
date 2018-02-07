package com.copypastapublishing.predictoe;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Paint;
import android.media.Image;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
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
    private static String contents = "";
    private static String corpusinfotext = "highstrangeness.txt";
    private static String story = "";
    private static String lines = "";
    Hashtable<String, Vector<String>> markovChain = new Hashtable<String, Vector<String>>();
    Integer spinner=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        saveCorpus();
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        TextView storyMode = (TextView) findViewById(R.id.story_text);
        storyMode.setMovementMethod(new ScrollingMovementMethod());
        CheckBox endCheck = (CheckBox)findViewById(R.id.checkBox);

        endCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                               @Override
                                               public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                                                updateUserChoiceList(MarkovChain.tipFinder(lines));
                                               }
                                           }
        );

        Spinner mspin=(Spinner) findViewById(R.id.spinner);



        Integer[] items = new Integer[]{1,2,3,4,5,6,7,8};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, items);



        mspin.setAdapter(adapter);
        mspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView adapter, View v, int i, long lng) {

                updateUserChoiceList(MarkovChain.tipFinder(lines));            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView)
            {

            }
        });
        View.OnClickListener myListner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = "";
                switch (v.getId()) {
                    case R.id.refresh:
                        if (lines!="") {
                            updateUserChoiceList(MarkovChain.tipFinder(lines));
                        }else {updateUserChoiceList(". ");}
                        break;
                    case R.id.paragraph_button:

                        token = "\n \t";
                        updateEndOfMarkovChain(token);

                        break;
                    case R.id.period_button:

                        token = ". ";
                        updateEndOfMarkovChain(token);

                        break;
                    case R.id.exclamation_button:

                        token = "! ";
                        updateEndOfMarkovChain(token);

                        break;
                    case R.id.question_button:

                        token = "? ";
                        updateEndOfMarkovChain(token);

                        break;
                    case R.id.comma_button:

                        token = ", ";
                        updateEndOfMarkovChain(token);

                        break;
                    case R.id.backspace_button:


                        String[] backspace = story.split(" ");
                        String redone = "";
                        for (int z = 0; z < backspace.length - 1; z++) {
                            redone = redone + " " + backspace[z];
                            lines = redone;
                        }
                        if (lines != " "||lines!=null||lines!="  "||lines!="") {
                            setLines(lines);
                            String tip = MarkovChain.tipFinder(lines);
                            updateUserChoiceList(tip);
                            break;
                        } else {
                            token = "\n \t";
                            updateEndOfMarkovChain(token);
                            break;
                        }
                }
            }

        };
        ImageButton text_manip2 = (ImageButton) findViewById(R.id.paragraph_button);
        ImageButton refresh1=(ImageButton) findViewById(R.id.refresh);
        Button text_manip3 = (Button) findViewById(R.id.period_button);
        ImageButton text_manip4 = (ImageButton) findViewById(R.id.backspace_button);
        Button text_manip5 = (Button) findViewById(R.id.comma_button);
        Button text_manip6 = (Button) findViewById(R.id.question_button);
        Button text_manip7 = (Button) findViewById(R.id.exclamation_button);
        refresh1.setOnClickListener(myListner);
        text_manip2.setOnClickListener(myListner);
        text_manip3.setOnClickListener(myListner);
        text_manip4.setOnClickListener(myListner);
        text_manip5.setOnClickListener(myListner);
        text_manip6.setOnClickListener(myListner);
        text_manip7.setOnClickListener(myListner);
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
        setLines(lines);
        TextView storyMode = (TextView) findViewById(R.id.story_text);
        storyMode.setText(lines);
        updateUserChoiceList(MarkovChain.tipFinder(lines));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICKFILE_RESULT_CODE && resultCode == Activity.RESULT_OK) {
            contents = "";
            Uri content_describer = data.getData();
            if (corpusinfotext == "") {
                corpusinfotext = getRealPathFromURI(content_describer);
            } else {
                corpusinfotext = corpusinfotext + ", " + getRealPathFromURI(content_describer);
            }
            TextView corpus = (TextView) findViewById(R.id.corpus_info);
            corpus.setText(corpusinfotext);
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
                    builder.append(line +" ");
                }
                // Do something with the content in
                contents = builder.toString();
                MarkovChain.addWords(contents, markovChain);
            } catch (FileNotFoundException e) {
                ((TextView) findViewById(R.id.line_text)).setText("FileNotFoundException");
                e.printStackTrace();
            } catch (IOException e) {
                ((TextView) findViewById(R.id.line_text)).setText("IOException");
                e.printStackTrace();
            } catch (RuntimeException r) {
                r.printStackTrace();
            } finally {
                setLines(lines);
                updateEndOfMarkovChain("\n \t");
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
        String output = corpusinfotext + story;
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, output);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.share)));
    }

    public void clear(MenuItem item) {
        lines = "";
        story = "";
        String update = "\n \t";
        try {
            updateEndOfMarkovChain(update);

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
        contents = "";
        updateUserChoiceList("");
        TextView corpus = (TextView) findViewById(R.id.corpus_info);
        corpus.setText("");
        corpusinfotext = "";
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
                    try {
                        MarkovChain.addWords(contents, markovChain);
                    } catch (NullPointerException n) {
                    }
                    updateUserChoiceList("\n \t");
                } finally {
                    reader.close();
                }
            } finally {
                inputStream.close();
            }
        } catch (IOException e) {
            ((TextView) findViewById(R.id.line_text)).setText("IOException");
        }
    }

    public void updateEndOfMarkovChain(String tip) {
        if (lines == "") {
            lines = "\n\t" + TextManipulate.toTitleCase(tip);
            setLines(lines);
            updateUserChoiceList(tip);
        } else if (tip == ". " || tip == "? " || tip == "! " || tip == "\n \t") {
            setLines(lines + tip);
            updateUserChoiceList(tip);

        } else if (tip == ", ") {
            lines = lines + tip;
            setLines(lines);
            tip = MarkovChain.tipFinder(lines);
            updateUserChoiceList(tip);

        } else {

            lines = lines + " " + tip;
            setLines(lines);
            tip = MarkovChain.tipFinder(lines);
            updateUserChoiceList(tip);
        }
    }

    public void updateUserChoiceList(String tip) {
        ArrayList<String> ink = new ArrayList<String>();
        try {
            Spinner mspin=(Spinner) findViewById(R.id.spinner);
            spinner = (Integer) mspin.getSelectedItem();
        }
        catch (NullPointerException n){

        }
        if (tip == ". " || tip == "? " || tip == "! " || tip == ", " || tip == "\n \t") {
            try {
                Spinner mspin=(Spinner) findViewById(R.id.spinner);
                spinner = (Integer) mspin.getSelectedItem();
            }
            catch (NullPointerException n){

            }
            for (int i = 0; i < 60; i++) {
                String blink = MarkovChain.rndWord(markovChain, spinner);
                ink.add(blink);
            }
            Set<String> hs = new HashSet<>(ink);
            hs.addAll(ink);
            ink.clear();
            ink.addAll(hs);

            setList(ink);
        } else {
            CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox);
            Boolean toEnd=checkBox.isChecked();
            for (int i = 0; i < 60; i++) {
                String blink = MarkovChain.generateInk(tip, markovChain, spinner, toEnd);
                if (blink != " " && blink != null && blink != "") {
                    ink.add(blink);
                }
                Set<String> hs = new HashSet<>(ink);
                hs.addAll(ink);
                ink.clear();
                ink.addAll(hs);

                setList(ink);
            }
        }
    }

    public void setLines(String newLine) {
        TextView tipLine = (TextView) findViewById(R.id.line_text);
        TextView storyMode = (TextView) findViewById(R.id.story_text);
        storyMode.setPaintFlags(storyMode.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        String newStoryModeValue;
        String newTipLineValue;
        if (lines == "") {
            newStoryModeValue = "";
            newTipLineValue = "";
            
        } else {
            newStoryModeValue = newLine;
            newTipLineValue = newLine.replace("\n", "");
        }
        storyMode.setText(newStoryModeValue);
        storyMode.setMovementMethod(new ScrollingMovementMethod());
        tipLine.setText(newTipLineValue);
        lines = newLine;
        story = newLine;
    }

    public void setList(ArrayList<String> ink) {

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.list_white_text, R.id.list_content, ink);
        final ListView listView = (ListView) findViewById(R.id.ListView);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) listView.getItemAtPosition(position);

                updateEndOfMarkovChain(item);

            }
        });
    }
}
