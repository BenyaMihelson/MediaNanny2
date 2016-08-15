package com.mediananny.benya.mediananny;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;

import android.support.v4.view.ActionProvider;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;



import android.widget.Toast;

public class FullTextActivity extends AppCompatActivity implements View.OnClickListener,
        ShareActionProvider.OnShareTargetSelectedListener {
    WebView ww;
    ImageButton share;
    private ShareActionProvider mShareActionProvider;
    MenuItem shareItem;
  //  private Intent shareIntent=new Intent(Intent.ACTION_SEND);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_text);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.showOverflowMenu();
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setElevation(10);






        initWebView();


    }

    private void initWebView() {


      WebView  mWebView = (WebView) findViewById(R.id.webView);

        mWebView.setWebChromeClient(new WebChromeClient());



        // WebViewの設定
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setBuiltInZoomControls(true);

            settings.setPluginState(WebSettings.PluginState.ON);



        String html = "";
        html += "<html><body>";
        html += "<style>img{display: inline;height: auto;max-width: 100%;}" +
                "h3 {\n" +
                "  font: bold 7dp \"Arial\";\n" +
                "  color: #a6774a;\n" +
                "  margin: 0 0 5px;\n" +
                "}" +
                "" +
                "</style>" +
                "<h3>" + getIntent().getStringExtra("header") + "</h3>" + "<img src=\"http://mediananny.com/content/images_new/news/original/"+
                         getIntent().getStringExtra("articleImg")+"\">" +
                getIntent().getStringExtra("content").replace("../..", "http://mediananny.com");
        html += "</body></html>";


        mWebView.loadDataWithBaseURL("", html, "text/html", "utf-8", null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fulltext, menu);

        shareItem = menu.findItem(R.id.share);

        return super.onCreateOptionsMenu(menu);
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
        switch (item.getItemId()) {
            case android.R.id.home:

                this.finish();
                return true;
            case R.id.share:

                shareIt();

            //NavUtils.navigateUpFromSameTask(this);

           /* case R.id.action_settings:
                intent = new Intent(this, ThirdActivity.class);
                startActivity(intent);*/
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        shareIt();

    }

    private void shareIt() {


        mShareActionProvider = new ShareActionProvider(this);

        MenuItemCompat.setActionProvider(shareItem, mShareActionProvider);

        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, getIntent().getStringExtra("webLink"));
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(intent);
        }else{
            Toast.makeText(this, "hhhh", Toast.LENGTH_LONG).show();
        }


//sharing implementation here
    }

    @Override
    public boolean onShareTargetSelected(ShareActionProvider source, Intent intent) {
        shareIt();

        return(false);
    }
}
