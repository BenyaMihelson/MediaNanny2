package com.mediananny.benya.mediananny.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by benya on 11/9/15.
 */
public class APItoDB {

    Context context;


    private static final String API_TAG = "API" ;
    // http://mediananny.com/api/category/?offset=0&category_id=79&debug=true
    final String API_HOST = "http://mediananny.com/api/";
    final String SUBSTR_API_HOST = "category/?offset=";
    final String SUBSTR_CAT_API_HOST = "&category_id=";
    final String SUBSTR_DEBUG_API_HOST = "&debug=true";


    private static final String ARTICLE_TAG = "articles";
    private static final String ID_TAG = "id";
    private static final String CATEGORY_TAG = "category_id";
    private static final String DATE_TAG = "created_at";
    private static final String TITLE_TAG = "title";
    private static final String IMAGE_TAG = "image";
    private static final String CONTENT_TAG = "content";



    private int category;
    private int page;

    private long timeOfFirstItem = 0;
    private long timeOfLastItem;


    public APItoDB(int category, int page, Context context) {
        this.category = category;
        this.page = page;
        this.context = context;

        Log.d(API_TAG, "called first contructor");

    }

    public APItoDB(int category, int page,long timeOfFirstItem, long timeOfLastItem, Context context) {
        this.category = category;
        this.page = page;
        this.context = context;
        this.timeOfFirstItem = timeOfFirstItem;
        this.timeOfLastItem = timeOfLastItem;
        Log.d(API_TAG, "called second contructor");

    }

    public APItoDB(int category, int page, long firstItemTime, Context context) {

        this.category = category;
        this.page = page;
        this.context = context;
        this.timeOfFirstItem = firstItemTime;

        Log.d(API_TAG, "called THIRD contructor");
    }

    private String URLString(){
        Log.d(API_TAG, API_HOST+SUBSTR_API_HOST+page+SUBSTR_CAT_API_HOST+category+SUBSTR_DEBUG_API_HOST);

        return API_HOST+SUBSTR_API_HOST+page+SUBSTR_CAT_API_HOST+category+SUBSTR_DEBUG_API_HOST;

    }

    public void getContent(){

     JsonToDB(GetStringFromAPI(URLString()));


    }

    public  void updateContent(){
    int page = 1;


    }



    private String GetStringFromAPI(String url){
        String data = "";
        HttpURLConnection httpUrlConnection = null;

        try {
            httpUrlConnection = (HttpURLConnection) new URL(url)
                    .openConnection();
            InputStream in = new BufferedInputStream(
                    httpUrlConnection.getInputStream());
            data = readStream(in);


        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;

    }
    private void JsonToDB(String data){
        Bitmap bitmap;
        String image = "0";

        ContentResolver cr = context.getContentResolver();


        JSONObject jsonRootObject = null;

        try {
            jsonRootObject = new JSONObject(data);
            JSONArray jsonArray = jsonRootObject.optJSONArray(ARTICLE_TAG);
            Log.d(API_TAG, jsonArray.length() + "Langth of jsonArray");



                for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                    if(timeOfFirstItem!=0){



                    Log.d(API_TAG, "timeOfFirstItem!=0");

                if(timeOfFirstItem<jsonObject.getInt(DATE_TAG)||timeOfLastItem>jsonObject.getInt(DATE_TAG)){
                    Log.d(API_TAG, jsonObject.getInt(DATE_TAG) + "timeOfJSON Object >>>>>");
              //          if(jsonObject.getInt(DATE_TAG)!=timeOfFirstItem|jsonObject.getInt(DATE_TAG)!=timeOfLastItem){

                String urlImg = "http://mediananny.com" + jsonObject.getString(IMAGE_TAG);

                File storagePath = new File(context.getExternalCacheDir(),
                        jsonObject.getString("image").replaceAll("/content/images_new/news/220x148/", ""));

                storagePath.getParentFile().mkdir();
                OutputStream fOut = new FileOutputStream(storagePath);

                bitmap = BitmapFactory.decodeStream((InputStream) new URL(urlImg).getContent());
                bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut);

                image = storagePath.getName();

                fOut.flush();
                fOut.close();

                ContentValues values = new ContentValues();

                values.put(MNContentProvider.ARTICLES_ID, jsonObject.getInt(ID_TAG));
                values.put(MNContentProvider.ARTICLES_TITLE, jsonObject.getString(TITLE_TAG));
                values.put(MNContentProvider.ARTICLES_CONTENT, jsonObject.getString(CONTENT_TAG));
                values.put(MNContentProvider.ARTICLES_CATEGORY, jsonObject.getInt(CATEGORY_TAG));
                values.put(MNContentProvider.ARTICLES_TIME, jsonObject.getInt(DATE_TAG));
                values.put(MNContentProvider.ARTICLES_IMG, image);
                values.put(MNContentProvider.ARTICLES_IMG_URL, urlImg);
                values.put(MNContentProvider.IS_FAVORITE, false);

                cr.insert(MNContentProvider.CONTENT_URI, values);

            }else{
                    Log.d(API_TAG, jsonObject.getInt(DATE_TAG) + "timeOfJSON Object");
                    //break;

                    }
                }
                else{
                    String urlImg = "http://mediananny.com" + jsonObject.getString(IMAGE_TAG);

                    File storagePath = new File(context.getExternalCacheDir(),
                            jsonObject.getString("image").replaceAll("/content/images_new/news/220x148/", ""));

                    storagePath.getParentFile().mkdir();
                    OutputStream fOut = new FileOutputStream(storagePath);

                    bitmap = BitmapFactory.decodeStream((InputStream) new URL(urlImg).getContent());
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut);

                    image = storagePath.getName();

                    fOut.flush();
                    fOut.close();

                    ContentValues values = new ContentValues();

                    values.put(MNContentProvider.ARTICLES_ID, jsonObject.getInt(ID_TAG));
                    values.put(MNContentProvider.ARTICLES_TITLE, jsonObject.getString(TITLE_TAG));
                    values.put(MNContentProvider.ARTICLES_CONTENT, jsonObject.getString(CONTENT_TAG));
                    values.put(MNContentProvider.ARTICLES_CATEGORY, jsonObject.getInt(CATEGORY_TAG));
                    values.put(MNContentProvider.ARTICLES_TIME, jsonObject.getInt(DATE_TAG));
                    values.put(MNContentProvider.ARTICLES_IMG, image);
                    values.put(MNContentProvider.ARTICLES_IMG_URL, urlImg);
                        values.put(MNContentProvider.IS_FAVORITE, false);

                    cr.insert(MNContentProvider.CONTENT_URI, values);
                }
                    }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer data = new StringBuffer("");
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                data.append(line);
            }
        } catch (IOException e) {
            Log.e(API_TAG, "IOException");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data.toString();
    }

}
