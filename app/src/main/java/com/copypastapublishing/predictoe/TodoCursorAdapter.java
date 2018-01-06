package com.copypastapublishing.predictoe;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by ghoulish on 1/6/2018.
 */

public class TodoCursorAdapter extends CursorAdapter {
    public TodoCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView titleView = (TextView) view.findViewById(R.id.titleView);
        TextView authorView = (TextView) view.findViewById(R.id.authorView);
        // Extract properties from cursor
        String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
        String author = cursor.getString(cursor.getColumnIndexOrThrow("author"));
        // Populate fields with extracted properties
        titleView.setText(title);
        authorView.setText(author);
    }
}
