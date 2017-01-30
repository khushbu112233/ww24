package com.westwood24.connect.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.westwood24.R;
import com.westwood24.connect.activity.DashBoardActivity;
import com.westwood24.connect.utils.Pref;

/**
 * Created by Dharmesh on 2/9/2016.
 */

public class WebViewForAboutusFragment extends Fragment {

    View mRootView;
    WebView myWebView;

    ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_webview_menu, container, false);

        DashBoardActivity.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        dialog = new ProgressDialog(getActivity());
        myWebView = (WebView) mRootView.findViewById(R.id.webview);

        String menu = getArguments().getString("menu");

        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        dialog.setMessage("Loading..Please wait.");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        if (menu.equals("aboutus")) {
            // title.setText(getString(R.string.About_Us));
            Pref.setValue(getActivity(), "current", "aboutus");
            myWebView.loadUrl("http://ww24connect.com/api/cmsPagebySlug/aboutus");
        } else if (menu.equals("terms")) {
            Pref.setValue(getActivity(), "current", "terms");
            myWebView.loadUrl("http://ww24connect.com/api/cmsPagebySlug/termsofuse");
        } else {
            Pref.setValue(getActivity(), "current", "privacy");
            myWebView.loadUrl("http://ww24connect.com/api/cmsPagebySlug/privacypolicy");
        }

        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        return mRootView;
    }
}
