package com.mediananny.benya.mediananny.utils;

import java.util.HashMap;

/**
 * Created by benya on 4/27/16.
 */
public class LinkForWeb {

    final private String SITE_URL = "http://mediananny.com";

    public String getWebLink(int cat_id, int id){
        HashMap<Integer, String> categories = new HashMap<Integer, String>();
        categories.put(78, "novosti");
        categories.put(91, "intervju");
        categories.put(88, "obzory");
        categories.put(80, "oprosy");
        categories.put(83, "reportazhi");
        categories.put(82, "raznoe");
        categories.put(187, "video");

        return SITE_URL+"/"+categories.get(cat_id)+"/"+id+"/";

    }



}
