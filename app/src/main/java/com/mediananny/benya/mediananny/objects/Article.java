package com.mediananny.benya.mediananny.objects;

import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;

import com.mediananny.benya.mediananny.R;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by benya on 11/11/15.
 */
public class Article implements Serializable{
    private Integer id;
    private Integer articleId;
    private String title;
    private String img;
    private String shortStory;
    private String content;
    private int time;
    private int category_id;
    private boolean favorite;
 //   private String[] category = Resources.getSystem().getStringArray(R.array.categories);




    public Article() {

    }


    public boolean isFavorite() {
        return favorite;
    }

   /* public String getCategoryName(int id){



        HashMap<Integer, String> hmap = new HashMap<Integer, String>();
        hmap.put(78, category[1]);
        hmap.put(91, category[2]);
        hmap.put(88, category[3]);
        hmap.put(80, category[4]);
        hmap.put(83, category[5]);
        hmap.put(82, category[6]);

        return hmap.get(id);
    }*/

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getShortStory() {
        return shortStory;
    }

    public void setShortStory(String shortStory) {
        this.shortStory = shortStory;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }


}
