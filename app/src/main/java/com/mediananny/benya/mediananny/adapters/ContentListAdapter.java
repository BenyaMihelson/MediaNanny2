package com.mediananny.benya.mediananny.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mediananny.benya.mediananny.R;
import com.mediananny.benya.mediananny.objects.Article;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

/**
 * Created by benya on 12/17/15.
 */
public class ContentListAdapter extends BaseAdapter {
    public final String LOG_TAG = "CAdapter";

    Context ctx;
    LayoutInflater lInflater;
    ArrayList<String> list;

    public ContentListAdapter(Context ctx,  ArrayList<String> list) {
        Log.d(LOG_TAG, "constructor was called");

        this.ctx = ctx;
        this.lInflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.list = list;


    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        String str = list.get(position);
       //if(view == null){
            if(str.contains(".jpg")){
                view = lInflater.inflate(R.layout.content_list_item_image, parent, false);
                ImageView imageView = (ImageView)view.findViewById(R.id.content_iv);

                view.setEnabled(false);

                view.setOnClickListener(null);
                Log.d(LOG_TAG, str + " to picassa");
             /* String strRep =   str.replaceAll("/original/", "/620x480/");
                Log.d(LOG_TAG, strRep + " to picassa after raplace");
*/
                Picasso.with(ctx)
                       // .setIndicatorsEnabled(true)
                        .load(str)

                       // .resize(480, 480)
                      //  .centerInside()
                        .placeholder(R.drawable.progress_animation)

                        .into(imageView);


            }else{
                view = lInflater.inflate(R.layout.content_list_item_text, parent, false);
                Spanned spanned = Html.fromHtml(str);

                TextView tv = (TextView)view.findViewById(R.id.content_tv);
                tv.setMovementMethod(LinkMovementMethod.getInstance());
                tv.setText(spanned);

            }



//        }


        /*Spanned spanned4 = Html.fromHtml(str);

        Log.d(LOG_TAG, spanned4 + " string");*/


     /*   ((TextView) view.findViewById(R.id.content_tv)).setText(spanned);*/







        return view;
    }
}
