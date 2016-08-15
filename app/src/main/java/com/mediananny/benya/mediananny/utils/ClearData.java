package com.mediananny.benya.mediananny.utils;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.io.File;

/**
 * Created by benya on 12/2/15.
 */
public class ClearData {

    final String LOG_TAG = "ClearData";
    private Cursor cursor;
    private Context context;

    public ClearData(
            //Cursor cursor,
            Context context) {
      //  this.cursor = cursor;
        this.context = context;

    }

    public void calear() {

        File dir = context.getExternalCacheDir();
        File[] files = dir.listFiles();

        Log.d(LOG_TAG, dir.getPath()+  " cache directory");

        for (File file : files) {

            Log.d(LOG_TAG, file.getName() + " deleted");

            file.delete();
        }

    }
}

