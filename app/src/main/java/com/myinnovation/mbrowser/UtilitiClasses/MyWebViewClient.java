package com.myinnovation.mbrowser.UtilitiClasses;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MyWebViewClient extends WebViewClient {
    ProgressBar bar;
    EditText text;
    Activity activity;
    private String siteUrl = "";


    public MyWebViewClient(ProgressBar bar, EditText text, Activity activity) {
        this.bar = bar;
        this.text = text;
        this.activity = activity;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return false;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        siteUrl = url;
        text.setText(url);
        text.setSelection(text.getText().length());
        text.requestFocus();
        bar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        bar.setVisibility(View.INVISIBLE);
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(text.getWindowToken(),0);
    }

//    private Map<String, Boolean> loadedUrls = new HashMap<>();
//    @Nullable
//    @Override
//    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
//        boolean ad;
//        if (!loadedUrls.containsKey(url)) {
//            ad = AdBlocker.isAd(url);
//            loadedUrls.put(url, ad);
//        } else {
//            ad = loadedUrls.get(url);
//        }
//        return ad ? AdBlocker.createEmptyResource() :
//                super.shouldInterceptRequest(view, url);
//    }
}
