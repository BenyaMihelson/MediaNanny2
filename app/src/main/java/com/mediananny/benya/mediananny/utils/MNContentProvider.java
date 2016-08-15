package com.mediananny.benya.mediananny.utils;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by benya on 11/9/15.
 */
public class MNContentProvider extends ContentProvider {
    private String LOG = "Provider";

    public static final Uri CONTENT_URI =
            Uri.parse("content://com.mediananny.benya.mediananny/news");

    public static final String AUTO_ID = "id";
    public static final String ARTICLES_ID = "articlesId";
    public static final String ARTICLES_TITLE = "title";
    public static final String ARTICLES_IMG = "img";
    public static final String ARTICLES_IMG_URL = "img_url";
    public static final String ARTICLES_SHORT_STORY = "shortStory";
    public static final String ARTICLES_CONTENT = "content";
    public static final String ARTICLES_TIME = "time";
    public static final String ARTICLES_CATEGORY = "category_id";
    public static final String IS_FAVORITE = "favorite";

    private MySQLiteOpenHelper myOpenHelper;

    private static final int ALLROWS = 1;
    private static final int SINGLE_ROW = 2;
    private static final int ROWS_FROM_CATEGORY = 3;

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("com.mediananny.benya.mediananny", "news", ALLROWS);
        uriMatcher.addURI("com.mediananny.benya.mediananny", "news/#", SINGLE_ROW);
        uriMatcher.addURI("com.mediananny.benya.mediananny", "news/cat", ROWS_FROM_CATEGORY);
    }


    @Override
    public boolean onCreate() {
        myOpenHelper = new MySQLiteOpenHelper(getContext(),MySQLiteOpenHelper.DATABASE_NAME, null,
                MySQLiteOpenHelper.DATABASE_VERSION);

        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = myOpenHelper.getWritableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        switch (uriMatcher.match(uri)) {
            case ALLROWS:
                queryBuilder.setTables(MySQLiteOpenHelper.ARTICLES_TABLE);
                break;
            case SINGLE_ROW:
                queryBuilder.setTables(MySQLiteOpenHelper.ARTICLES_TABLE);
                queryBuilder.appendWhere(AUTO_ID + uri.getLastPathSegment());
                break;
            case ROWS_FROM_CATEGORY:
                queryBuilder.setTables(MySQLiteOpenHelper.ARTICLES_TABLE);
                queryBuilder.appendWhere(ARTICLES_CATEGORY + uri.getLastPathSegment());
                break;
        }

        String whereAgs[] = null;
        String groupBy = null;
        String having = null;
        String order  = null;
        Cursor cursor = queryBuilder.query(db,projection,selection,
                selectionArgs,groupBy,having,sortOrder);

        return cursor;

    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {

         /*   case ALLROWS: return "vnd.android.cursor.dir/vnd.cards.words";
            case SINGLE_ROW: return "vnd.android.cursor.item/vnd.cards.words";*/

            default: throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = myOpenHelper.getWritableDatabase();

        String nullColumnHack = null;

        long id  = db.insert(MySQLiteOpenHelper.ARTICLES_TABLE, nullColumnHack, values);

        if(id > -1){
            Uri insertedId = ContentUris.withAppendedId(CONTENT_URI, id);
            getContext().getContentResolver().notifyChange(insertedId, null);
            return insertedId;
        }
        else{
            return null;
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = uriMatcher.match(uri);
        SQLiteDatabase db = myOpenHelper.getWritableDatabase();
        int rowsDeleted = 0;

        switch (uriType) {
            case ALLROWS:
                rowsDeleted = db.delete(MySQLiteOpenHelper.ARTICLES_TABLE, selection, selectionArgs);
                break;
        }


       // rowsDeleted = db.delete(MySQLiteOpenHelper.ARTICLES_TABLE, selection, selectionArgs);

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.d(LOG, "update was called");

        int updateCount;

        SQLiteDatabase db = myOpenHelper.getWritableDatabase();

        String rowID = uri.getPathSegments().get(1);
        selection = ARTICLES_ID + " = " + rowID + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");
        Log.d(LOG, selection + " update was called"+ values + " values" + selectionArgs + " selectionArgs");

        updateCount = db.update(MySQLiteOpenHelper.ARTICLES_TABLE, values, selection, selectionArgs);

        getContext().getContentResolver().notifyChange(uri, null);

        return updateCount;

    }



    private class MySQLiteOpenHelper extends SQLiteOpenHelper {
        private static final int DATABASE_VERSION = 9;
        private static final String DATABASE_NAME = "nanny.db";
        private static final String ARTICLES_TABLE = "articles";

        private static final String DATABASE_CREATE = "create table " + ARTICLES_TABLE + " (" + AUTO_ID +
                " integer primary key autoincrement, " + ARTICLES_ID + " integer, " + ARTICLES_TITLE +
                " text, " + ARTICLES_IMG + " text, " + ARTICLES_IMG_URL + " text, " + ARTICLES_SHORT_STORY + " text," +
                ARTICLES_CONTENT + " text, " + ARTICLES_TIME + " integer, " +
                ARTICLES_CATEGORY + " integer, " + IS_FAVORITE + " text);";


        public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
           Log.d(LOG, "called inCreate" + DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d(LOG, oldVersion + " " + newVersion + "int oldVersion, int newVersion");

            if (oldVersion < newVersion) {

                db.execSQL("DROP TABLE IF EXISTS " + ARTICLES_TABLE);
                onCreate(db);

            }
        }

    }
}
