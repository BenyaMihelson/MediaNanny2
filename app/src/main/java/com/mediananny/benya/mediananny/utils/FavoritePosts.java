package com.mediananny.benya.mediananny.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.mediananny.benya.mediananny.adapters.MNListAdapter;
import com.mediananny.benya.mediananny.objects.Article;

import java.net.URI;

/**
 * Created by benya on 12/25/15.
 */
public class FavoritePosts {

    private String LOG = "Favorite";

    private Article item;
    private MNListAdapter adapter;
    private Context context;

    public FavoritePosts(Article item, MNListAdapter adapter, Context context) {
        this.item = item;
        this.adapter = adapter;
        this.context = context;
    }

    public void add(){

        Log.d(LOG, item.isFavorite() + " on add()");


        Uri uri = Uri.parse(MNContentProvider.CONTENT_URI  + "/" + item.getArticleId());
        ContentValues values = new ContentValues();
        values.put(MNContentProvider.IS_FAVORITE, item.isFavorite());
        context.getContentResolver().update(uri, values, null,null);
        adapter.notifyDataSetChanged();

    }
}
