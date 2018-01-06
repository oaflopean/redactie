package com.copypastapublishing.predictoe;

import android.app.Application;
import android.content.Context;

/**
 * Created by ghoulish on 1/6/2018.
 */

public class ResourcesSubclass extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext(){
        return mContext;
    }
}
