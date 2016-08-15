package com.mediananny.benya.mediananny;

import android.app.ActionBar;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mediananny.benya.mediananny.adapters.ContentListAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ScrollingActivity extends AppCompatActivity implements Html.ImageGetter {

    final String TAG = "FullText";
    TextView tv;
    String text;
    ListView lv;
    ArrayList<String> content;
    ContentListAdapter contentListAdapter;
    android.support.v7.app.ActionBar ab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        ActionBar db = getActionBar();

//        db.setTitle("header");
        db.setHomeButtonEnabled(true);
/*
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
*/
      /*  ab = getSupportActionBar();
        ab.setTitle("header");
        ab.setHomeButtonEnabled(true);*/



      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        //tv = (TextView) findViewById(R.id.sTv);
        content = new ArrayList<>();
        lv = (ListView) findViewById(R.id.content);

        contentListAdapter = new ContentListAdapter(this, content);









        String[] string = text.split("<img title=\\\"([^\\\"]+)\" src=\\\"([^\\\"]+)\" alt=\\\"([^\\\"]+)\" />");






        // Spanned spanned = Html.fromHtml(text, this, null);


         Spanned spanned1 = Html.fromHtml(string[0]);

    //  new  ForTheLulz().execute();
/*
        tv.setLinksClickable(true);
        tv.setMovementMethod(LinkMovementMethod.getInstance());
*/

     //   tv.setText(spanned1);
        text = getIntent().getStringExtra("content").replace("../..", "http://mediananny.com");
        new ContentRender().execute();
    }

    class ContentRender extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected Void doInBackground(Void... params) {
            String[] string = text.split("<img title=\\\"([^\\\"]+)\" src=\\\"([^\\\"]+)\" alt=\\\"([^\\\"]+)\" />");
          //  Spanned spanned;
            Document doc = Jsoup.parse(text);
            Elements e = doc.getElementsByTag("img");

            Log.d(TAG, string.length + " in string[]");
            Log.d(TAG, e.size() + " in IMG");

         /*  for(int i = 0; i< string.length; i++){
               Log.d(TAG, i + " " + String.valueOf(Html.fromHtml(string[i])) + " string[NUMBER]");
           }
*/






            for(int i =0; i<e.size(); i++){
                if(!String.valueOf(Html.fromHtml(string[i])).equals("")){
                   // spanned = Html.fromHtml(string[i]);

                    content.add(string[i]);
                }


                content.add(String.valueOf(e.get(i).attr("src")));

            }
            if(string.length>e.size()){
                content.add(String.valueOf(Html.fromHtml(string[string.length-1])));
            }



            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            lv.setAdapter(contentListAdapter);

            contentListAdapter.notifyDataSetChanged();


        }
/*
            Log.d(TAG, content.size() + "content size");
            for(int i=0; i<content.size(); i++){
                Log.d(TAG, i + " " + content.get(i));

                Spanned spanned = Html.fromHtml(content.get(i));



            }
            Spanned spanned1 = Html.fromHtml(content.get(0));
            tv.setText(spanned1);

        }
*/
    }

    class ForTheLulz extends AsyncTask<Void, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(Void... args) {
            Bitmap result = null;


                Document doc = Jsoup.parse(text);



           // String text = doc.body().text();

                Elements e = doc.getElementsByTag("p");
                Elements e2 = doc.getElementsByTag("img");

          //  e2 = doc.select("p");
            Element content = doc.getElementById("content");


            Log.d(TAG, content + " TEXT");

           /*     String str = String.valueOf(e.get(0));

            for(int i = 0; i < e.size(); i++){
                Log.d(TAG, String.valueOf(e.get(i)) + " Elemtnt first");

            }
            for(int i = 0; i < e2.size(); i++){
                Log.d(TAG, String.valueOf(e2.get(i)) + " Elemtnt first");
            }*/



              //  Log.d(TAG, str + " Elemtnt first");

                /*Document doc = Jsoup.connect("http://lulpix.com")
                        .referrer("http://www.google.com")
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .get();
                //parse("http://lulpix.com");
                *//*if (doc != null) {
                    Elements elems = doc.getElementsByAttributeValue();
                    if (elems != null && !elems.isEmpty()) {
                        Element elem = elems.first();
                        elems = elem.getElementsByTag("img");
                        if (elems != null && !elems.isEmpty()) {
                            elem = elems.first();
                            String src = elem.attr("src");
                            if (src != null) {
                                URL url = new URL(src);
                                // Just assuming that "src" isn't a relative URL is probably stupid.
                                InputStream is = url.openStream();
                                try {
                                    result = BitmapFactory.decodeStream(is);
                                } finally {
                                    is.close();
                                }
                            }
                        }
                    }
                }*/

                return result;


    }
        @Override
        protected void onPostExecute(Bitmap result) {
            /*ImageView lulz = (ImageView) findViewById(R.id.lulpix);
            if (result != null) {
                lulz.setImageBitmap(result);
            } else {
                //Your fallback drawable resource goes here
                //lulz.setImageResource(R.drawable.nolulzwherehad);
            }*/
            Log.d(TAG, result + " ");
        }
    }






    @Override
    public Drawable getDrawable(String source) {
        LevelListDrawable d = new LevelListDrawable();
        Drawable empty = getResources().getDrawable(R.drawable.ic_launcher);
        d.addLevel(0, 0, empty);
        d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());

        new LoadImage().execute(source, d);

        return d;
    }







    class LoadImage extends AsyncTask<Object, Void, Bitmap> {

        private LevelListDrawable mDrawable;

        @Override
        protected Bitmap doInBackground(Object... params) {
            String sourceT = (String) params[0];

            String source = sourceT.replace("../..", "http://mediananny.com");

            mDrawable = (LevelListDrawable) params[1];
            Log.d(TAG, "doInBackground " + source);
            try {
                InputStream is = new URL(source).openStream();
                return BitmapFactory.decodeStream(is);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            Log.d(TAG, "onPostExecute drawable " + mDrawable);
            Log.d(TAG, "onPostExecute bitmap " + bitmap);
            if (bitmap != null) {
                BitmapDrawable d = new BitmapDrawable(bitmap);
                mDrawable.addLevel(1, 1, d);
                mDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                mDrawable.setLevel(1);
                // i don't know yet a better way to refresh TextView
                // mTv.invalidate() doesn't work as expected
                CharSequence t = tv.getText();
                tv.setText(t);
                tv.setLinksClickable(true);
            }
        }


    }
}
