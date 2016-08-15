package com.mediananny.benya.mediananny;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.mediananny.benya.mediananny.adapters.CustomDrawerAdapter;
import com.mediananny.benya.mediananny.adapters.MNListAdapter;
import com.mediananny.benya.mediananny.objects.Article;
import com.mediananny.benya.mediananny.objects.DrawerItem;
import com.mediananny.benya.mediananny.utils.APItoDB;
import com.mediananny.benya.mediananny.utils.ClearData;
import com.mediananny.benya.mediananny.utils.LinkForWeb;
import com.mediananny.benya.mediananny.utils.MNContentProvider;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ItemsFragment.OnFragmentInteractionListener, AbsListView.OnScrollListener, SwipeRefreshLayout.OnRefreshListener {
    final static String  LOG_TAG = "MainLog";
    final static long TIME_TO_UPDATE = 86400; //24 hours

    private TextView tv;
    private ListView listView;
    private View footer;
    SwipeRefreshLayout swipeRefreshLayout;



    private ArrayList<Article> list;
    private Article article;
    private MNListAdapter adapter;
    private int PAGE;
    private ShowContent showContent;

    private boolean isloading = false;
    private long firstItemTime;
    private long lastItemTime;
    private boolean onRefresh = false;
    private int category;
    private int TYPE_OF_LOAD_CONTENT;
    // 1 - whothot params;
    // 2 - update END of list
    // 3 - update of Swipe
    String[]  subtitle;
    android.support.v7.app.ActionBar ab;

    private static final String STATE_ID = "id";
    private static final String STATE_ITEMS = "items";
    private static final String STATE_ITEMS2 = "items";
    private static final String STATE_ITEMS3 = "items";



  /*  //new vrsion of navigation
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    CustomDrawerAdapter cdadapter;

    List<DrawerItem> dataList;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        savedInstanceState.setClassLoader(getClass().getClassLoader());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView)findViewById(R.id.textView2);
        tv.setVisibility(View.INVISIBLE);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


   NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        subtitle = getResources().getStringArray(R.array.categories);

        ab = getSupportActionBar();
        ab.setTitle(subtitle[0]);

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.blue, R.color.green, R.color.yellow, R.color.red);

        footer = getLayoutInflater().inflate(R.layout.lisview_footer, null);
        footer.setOnClickListener(null);
        listView = (ListView) findViewById(R.id.listViewItems);


        listView.addFooterView(footer);

        Log.d(LOG_TAG, " ...initing View");

        if(savedInstanceState != null){


            Log.d(LOG_TAG, savedInstanceState + " savedInstanceState object");
            list = (ArrayList<Article>) savedInstanceState.getSerializable(STATE_ITEMS);
           // list = savedInstanceState.getParcelableArrayList(STATE_ITEMS);

            adapter = new MNListAdapter(this, list, subtitle);

            listView.setAdapter(adapter);

            Log.d(LOG_TAG, list.size() + " in savedInstanceStat");
        }else{

            Log.d(LOG_TAG, savedInstanceState + " was null");

            list = new ArrayList<>();

            PAGE = 0;
            category = 0;

            //Veryfify data on DB
            testForOld();


            selectTypeOfLoading();
        }





        listView.setOnScrollListener(this);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                article = list.get(position);
                String title = article.getTitle();
                String content = article.getContent();
                int articleId = article.getArticleId();
                int articleCategory = article.getCategory_id();
                String articleImg = article.getImg();
                //Intent intent = new Intent(MainActivity.this, FullTextActivity.class);
                Intent intent = new Intent(MainActivity.this, FullTextActivity.class);
                intent.putExtra("header", title);
                intent.putExtra("content", content);
                intent.putExtra("articleID", articleId);
                intent.putExtra("articleCategory", articleCategory);
                intent.putExtra("articleImg", articleImg);
                intent.putExtra("webLink", new LinkForWeb().getWebLink(articleCategory, articleId));

                intent.putExtra("category", adapter.getCategoryName(article.getCategory_id()));
                startActivity(intent);


            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {


        Log.d(LOG_TAG, list + " list in onSaveInstanceState");


        outState.putSerializable(STATE_ITEMS, list);
       // outState.putParcelableArrayList(STATE_ITEMS, list);

        super.onSaveInstanceState(outState);




    }

    private void testForOld(){
        String ORDER = MNContentProvider.ARTICLES_TIME + " DESC";
        String WHERE = null;
        Cursor cursor = getContentResolver().query(MNContentProvider.CONTENT_URI, null, WHERE, null, ORDER);

        if(cursor.getCount()!=0){

        cursor.moveToFirst();

        firstItemTime = cursor.getInt(cursor.getColumnIndex(MNContentProvider.ARTICLES_TIME));
        long currentTime = System.currentTimeMillis()/1000;

        Log.d(LOG_TAG, firstItemTime + " time of last Item");
        Log.d(LOG_TAG, currentTime + " current time");

        if (currentTime-firstItemTime>=TIME_TO_UPDATE&category ==0) {
            Log.d(LOG_TAG, firstItemTime + " delete Items from DB and files form SD|| then Load from API to DB and show");

            //delete Items from DB and files form SD
            new ClearData(getApplication()).calear();

            WHERE = MNContentProvider.IS_FAVORITE +  " == 0";

            getContentResolver().delete(MNContentProvider.CONTENT_URI,WHERE, null);

            Log.d(LOG_TAG, cursor.getCount() + " records in db");
        }

        }
    }

    private void selectTypeOfLoading(){


        if(PAGE>0){

            TYPE_OF_LOAD_CONTENT = 3;
            Log.d(LOG_TAG, TYPE_OF_LOAD_CONTENT + " TYPE OF LOAD/ UPPLOADING TO DOWN");
            lastItemTime = list.get(list.size()-1).getTime();
            Log.d(LOG_TAG, lastItemTime + " lastItemTime");

            showContent =    new ShowContent();
            showContent.execute();


        }else{

        String ORDER = MNContentProvider.ARTICLES_TIME + " DESC";
        String WHERE;

        if(category == 0){
            Log.d(LOG_TAG, "category = 0 "+ category);

            long currentTime = System.currentTimeMillis()/1000;
            long timeLast = currentTime-TIME_TO_UPDATE*6;



            WHERE  = MNContentProvider.IS_FAVORITE + " != 1 AND " + MNContentProvider.ARTICLES_TIME+ ">="+ timeLast;
        }else{
            Log.d(LOG_TAG, "category != 0 "+ category);

            WHERE  = MNContentProvider.ARTICLES_CATEGORY + "=" + category;
        }

        Cursor cursor = getContentResolver().query(MNContentProvider.CONTENT_URI, null, WHERE, null, ORDER);

        if(cursor.getCount() == 0){
            Log.d(LOG_TAG, "0 records in DB. Starting load info from API");
            TYPE_OF_LOAD_CONTENT = 1;
            //Start load 10 last items from API
            showContent =    new ShowContent();
            showContent.execute();

        }else{ if(cursor.getCount()<10&cursor.getCount()>0){
            Log.d(LOG_TAG, cursor.getCount() + "records in DB. Starting load info from API");
            cursor.moveToFirst();
            firstItemTime = cursor.getInt(cursor.getColumnIndex(MNContentProvider.ARTICLES_TIME));
            cursor.moveToLast();
            lastItemTime = cursor.getInt(cursor.getColumnIndexOrThrow(MNContentProvider.ARTICLES_TIME));
            TYPE_OF_LOAD_CONTENT = 3;
            Log.d(LOG_TAG, "Start showContent with TYPE 3");
            Log.d(LOG_TAG, category +  " Category");
            Log.d(LOG_TAG, PAGE + " Page");

            showContent =    new ShowContent();
            showContent.execute();

        }
        else{
            Log.d(LOG_TAG, cursor.getCount() + "records in DB. Starting load info from API");
            cursor.moveToFirst();
            firstItemTime = cursor.getInt(cursor.getColumnIndex(MNContentProvider.ARTICLES_TIME));
            cursor.moveToLast();
            // lastItemTime = cursor.getInt(cursor.getColumnIndexOrThrow(MNContentProvider.ARTICLES_TIME));
            TYPE_OF_LOAD_CONTENT = 2;
            Log.d(LOG_TAG, "Start showContent with TYPE 3");
            Log.d(LOG_TAG, category +  " Category");
            Log.d(LOG_TAG, PAGE + " Page");

            showContent =    new ShowContent();
            showContent.execute();

        }}



        }
    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        switch (id){
            case R.id.main:
                category = 0;
                ab.setTitle(subtitle[0]);
                break;

            case R.id.news:
                category = 78;
                ab.setTitle(subtitle[1]);
                break;

            case R.id.interview:
                category = 91;
                ab.setTitle(subtitle[2]);
                break;

            case R.id.obzor:
                category = 88;
                ab.setTitle(subtitle[3]);
                break;

            case R.id.opros:
                category = 80;
                ab.setTitle(subtitle[4]);;
                break;

            case R.id.report:
                category = 83;
                ab.setTitle(subtitle[5]);
                break;

            case R.id.raznoe:
                category = 82;
                ab.setTitle(subtitle[6]);
                break;
            case R.id.video:
                category = 187;
                ab.setTitle(subtitle[8]);
                break;
            case R.id.favorite:
                category = -1;
                ab.setTitle(subtitle[7]);
                break;

        }
        PAGE = 0;

        tv.setVisibility(View.INVISIBLE);
        listView.setVisibility(View.VISIBLE);
        list.clear();
        adapter.notifyDataSetChanged();

        if(category>=0){

             TYPE_OF_LOAD_CONTENT = 2;
           // listView.setOnScrollListener(null);
            selectTypeOfLoading();
            listView.setOnScrollListener(this);
            listView.addFooterView(footer);
        }else{

            listView.setOnScrollListener(null);
            listView.removeFooterView(footer);
            fillList();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

   @Override
    public void onFragmentInteraction(String id) {

    }



    private void fillList(){
        //isloading = true;
        Cursor cursor = null;
        String ORDER = MNContentProvider.ARTICLES_TIME + " DESC";

        if(category<0){
            String WHERE = MNContentProvider.IS_FAVORITE +  " == 1";

            cursor = getContentResolver().query(MNContentProvider.CONTENT_URI, null, WHERE, null, ORDER);

            if(cursor.getCount()<1){
                listView.setVisibility(View.INVISIBLE);
                tv.setVisibility(View.VISIBLE);
                tv.setText("0000");

            }

        }else{
            if(category>0){
                String WHERE  = MNContentProvider.ARTICLES_CATEGORY + "=" + category;

                cursor = getContentResolver().query(MNContentProvider.CONTENT_URI, null, WHERE, null, ORDER);

            }

            else{

                long currentTime = System.currentTimeMillis()/1000;
                long timeLast = currentTime-TIME_TO_UPDATE*6;



                String WHERE  = MNContentProvider.IS_FAVORITE + " != 1 AND " + MNContentProvider.ARTICLES_TIME+ ">="+ timeLast;
                cursor = getContentResolver().query(MNContentProvider.CONTENT_URI, null, WHERE, null, ORDER);

            }
        }

        while(cursor.moveToNext()){
            article = new Article();

            article.setId(cursor.getInt(cursor.getColumnIndexOrThrow(MNContentProvider.AUTO_ID)));
            article.setArticleId(cursor.getInt(cursor.getColumnIndexOrThrow(MNContentProvider.ARTICLES_ID)));
            article.setCategory_id(cursor.getInt(cursor.getColumnIndexOrThrow(MNContentProvider.ARTICLES_CATEGORY)));
            article.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MNContentProvider.ARTICLES_TITLE)));
            article.setTime(cursor.getInt(cursor.getColumnIndexOrThrow(MNContentProvider.ARTICLES_TIME)));
            article.setImg(cursor.getString(cursor.getColumnIndexOrThrow(MNContentProvider.ARTICLES_IMG)));
            article.setContent(cursor.getString(cursor.getColumnIndexOrThrow(MNContentProvider.ARTICLES_CONTENT)));
            article.setFavorite(cursor.getInt(cursor.getColumnIndexOrThrow(MNContentProvider.IS_FAVORITE))>0);
            list.add(article);

        }

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        Log.d(LOG_TAG, "scroll: firstVisibleItem = " + firstVisibleItem
                + ", visibleItemCount" + visibleItemCount
                + ", totalItemCount" + totalItemCount);
        int loadedItems = firstVisibleItem + visibleItemCount;



//        Log.d(LOG_TAG, showContent.getStatus() + " status of Async");
        Log.d(LOG_TAG, "isloading " + isloading);

        if((loadedItems == totalItemCount) && !isloading){
            if(showContent != null && (showContent.getStatus() == AsyncTask.Status.FINISHED)){

                if(list.size()<10){
                    PAGE = 0;
                }else{
                    PAGE = PAGE+10;


                }
                selectTypeOfLoading();





            }
        }



    }

    @Override
    public void onRefresh() {
        onRefresh = true;
        swipeRefreshLayout.setRefreshing(true);

        PAGE = 0;

        if(list.size()>0){
            firstItemTime = list.get(0).getTime();
            TYPE_OF_LOAD_CONTENT = 2;
        }else{
            TYPE_OF_LOAD_CONTENT = 1;
        }



       /* list.clear();

        showContent = new ShowContent();

        showContent.execute();*/

        showContent =    new ShowContent();
        showContent.execute();


    }




    class ShowContent extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {

            isloading = true;
        }


        @Override
        protected Void doInBackground(Void... params) {
            Log.d(LOG_TAG, TYPE_OF_LOAD_CONTENT+ " TYPE_OF_LOAD_CONTENT");

           switch (TYPE_OF_LOAD_CONTENT){

                case 1:


                    new APItoDB(category, PAGE, getApplicationContext()).getContent();
                    break;

                case 2:

                    //PAGE = 0;
                    Log.d(LOG_TAG, " case2 Starting APItoDB with PGAGE = "+ PAGE);
                    Log.d(LOG_TAG, " LastItemTime = "+ lastItemTime);
                    Log.d(LOG_TAG, " FirstItemTime = "+ firstItemTime);

                    new APItoDB(category, PAGE, firstItemTime, getApplicationContext()).getContent();

                    break;

                case 3:

                    Log.d(LOG_TAG, " case3 Starting APItoDB with PGAGE = "+ PAGE);
                    Log.d(LOG_TAG, " LastItemTime = "+ lastItemTime);
                    Log.d(LOG_TAG, " FirstItemTime = "+ firstItemTime);

                    new APItoDB(category, PAGE, firstItemTime, lastItemTime, getApplicationContext()).getContent();

                    break;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            list.clear();
            fillList();
            // listView.setOnScrollListener(MainActivity.this);
            Log.d(LOG_TAG, " setting adapter");

           // adapter.notifyDataSetChanged();
        if(TYPE_OF_LOAD_CONTENT<3){
            adapter = new MNListAdapter(getApplicationContext(), list, subtitle);

            Log.d(LOG_TAG, " setting adapter with TYPE+OF LOAD<3");

            listView.setAdapter(adapter);

        }else {
            Log.d(LOG_TAG, " Notify adapter with TYPE+OF LOAD = 3");
           // adapter = new MNListAdapter(getApplicationContext(), list, subtitle);
            adapter.notifyDataSetChanged();
        }

            isloading = false;
            swipeRefreshLayout.setRefreshing(false);


///            lastItemTime = list.get(list.size()-1).getTime();

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }


    }

}
