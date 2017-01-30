package com.westwood24.connect.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.westwood24.R;
import com.westwood24.connect.utils.Pref;

/**
 * Created by aipxperts on 28/12/16.
 */
public class WebviewActivity extends Activity {

    WebView webview;
    ImageView img_left;
    RelativeLayout mHeaderBackRl;
    TextView mHeaderTitleTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_activity_layout);
        webview = (WebView)findViewById(R.id.webview);
        img_left = (ImageView)findViewById(R.id.img_left);
        mHeaderBackRl = (RelativeLayout) findViewById(R.id.header_layout_back_rl);
        mHeaderBackRl.setVisibility(View.GONE);
        mHeaderTitleTv = (TextView) findViewById(R.id.txt_title);
/*
        if(Pref.getValue(WebviewActivity.this, "fromweb", "").equalsIgnoreCase("contact"))
        {

            webview.loadUrl("http://ww24connect.com/new/contactus");
        }else if(Pref.getValue(WebviewActivity.this, "fromweb", "").equalsIgnoreCase("contact")){

        }*/
        webview.loadUrl("https://ww24connect.com/api/cmsPagebySlug/aboutus");
    //    webview.loadUrl("http://www.teluguoneradio.com/rssHostDescr.php?hostId=147");
      //  webview.loadUrl("http://ww24connect.com/new/termsofuse");

        webview.getSettings().setLoadsImagesAutomatically(true);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (!TextUtils.isEmpty(title)) {

                    mHeaderTitleTv.setText(webview.getTitle());
                    // mHeaderTitleTv.setText("About US");
                    mHeaderTitleTv.setAllCaps(true);
                }
            }
        });

        img_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

    }
}
