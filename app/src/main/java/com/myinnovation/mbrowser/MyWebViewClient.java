package com.myinnovation.mbrowser;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ProgressBar;

public class MyWebViewClient extends WebViewClient {
    ProgressBar bar;
    EditText text;
    Activity activity;


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
}
