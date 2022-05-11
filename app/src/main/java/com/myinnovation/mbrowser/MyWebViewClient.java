package com.myinnovation.mbrowser;

import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ProgressBar;

public class MyWebViewClient extends WebViewClient {
    ProgressBar bar;


    public MyWebViewClient(ProgressBar bar) {
        this.bar = bar;
//        this.text = text;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return false;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
//        text.setText(url);
        bar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        bar.setVisibility(View.INVISIBLE);
    }
}
