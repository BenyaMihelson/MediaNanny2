package com.mediananny.benya.mediananny.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mediananny.benya.mediananny.R;
import com.mediananny.benya.mediananny.objects.Article;
import com.mediananny.benya.mediananny.utils.FavoritePosts;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;

/**
 * Created by benya on 11/11/15.
 */
public class MNListAdapter extends BaseAdapter {
    public final String LOG_TAG = "MNListAdapter";

    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Article>list;
    String[] category;
    private ImageView fav;
    private boolean[] stars;




    public MNListAdapter(Context ctx,  ArrayList<Article> list, String[] category) {
       // super();
        this.ctx = ctx;
        this.lInflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = list;

       // Log.d(LOG_TAG, size + " list size in constructor");
        this.category = category;
        stars = new boolean[list.size()];


       // fillStars();
       // new MNListAdapter();



    }







    @Override
    public int getCount() {


        return list.size();
    }


    @Override
    public Article getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        Log.d(LOG_TAG, list.size() + " list items in get View");


        Log.d(LOG_TAG, stars.length + " stars in getView");

        View view = convertView;
        if(view == null){
            view = lInflater.inflate(R.layout.list_item, parent, false);

        }
        final Article article = getItem(position);

        ((TextView) view.findViewById(R.id.tvTitle)).setText(article.getTitle());
        ((TextView) view.findViewById(R.id.tvCategory)).setText(getCategoryName(article.getCategory_id()));
        fav = (ImageView) view.findViewById(R.id.addFav);
       // fav.setTag(new Integer(position));

        Log.d(LOG_TAG, article.isFavorite() + " " + position + " position before change");

        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                article.setFavorite(!article.isFavorite());
                    new FavoritePosts(article, MNListAdapter.this, ctx).add();

               // int position = (int) v.getTag();

                /* stars[position] = !article.isFavorite();
                notifyDataSetChanged();

               Log.d(LOG_TAG, stars[position] + " " + position + " position in click");

                *//*fav = (ImageView) v.findViewById(R.mipmap.ic_favorite_on);

                fav.setImageResource(R.mipmap.ic_favorite_on);*//*

                Log.d(LOG_TAG, "ImageView clicked for the row = " + position);*/
            }
        });
Log.d(LOG_TAG, article.isFavorite()+" " + position + " position after change");

        /*if (stars[position])
            fav.setImageResource(R.mipmap.ic_favorite_on);
        else
            fav.setImageResource(R.mipmap.ic_favorite_off);
*/
        if (article.isFavorite())
            fav.setImageResource(R.mipmap.ic_favorite_on);
        else
            fav.setImageResource(R.mipmap.ic_favorite_off);



        //String date = String.valueOf(article.getTime());
        int date  = article.getTime();
        Date timestamp = new Date((long)date*1000);

     //   Log.d(LOG_TAG, date + " date in adapter");

      //  GregorianCalendar gc = new GregorianCalendar();


        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");
        String created_at = sdf.format(timestamp);


        ((TextView) view.findViewById(R.id.tvDate)).setText(created_at);

        File storagePath =  ctx.getApplicationContext().getExternalCacheDir();
        File f = new File(ctx.getApplicationContext().getExternalCacheDir()+"/"+article.getImg());

        ImageView iv = (ImageView)view.findViewById(R.id.list_image);


        Bitmap bmp = BitmapFactory.decodeFile(f.getAbsolutePath());
        iv.setImageBitmap(bmp);

        return view;
    }


    public String getCategoryName(int id){
        HashMap<Integer, String> hmap = new HashMap<Integer, String>();
        hmap.put(78, category[1]);
        hmap.put(91, category[2]);
        hmap.put(88, category[3]);
        hmap.put(80, category[4]);
        hmap.put(83, category[5]);
        hmap.put(82, category[6]);
        hmap.put(187, category[8]);


        return hmap.get(id);
    }

    class OnFavClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {

        }
    }
}
