package com.myinnovation.mbrowser;

import static com.myinnovation.mbrowser.R.drawable.ic_cancle;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ImageView link, imageview, back, forward, refresh, more, share;
    EditText searchField;
    WebView webView;
    ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        link = findViewById(R.id.link);
        imageview = findViewById(R.id.desImageview);
        back = findViewById(R.id.leftarrow);
        forward = findViewById(R.id.rightarrow);
        refresh = findViewById(R.id.refresh);
        more = findViewById(R.id.more);
        share = findViewById(R.id.share);
        searchField = findViewById(R.id.addresslink);
        webView = findViewById(R.id.webPage);
        bar = findViewById(R.id.bar);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(true);

        webView.setWebViewClient(new MyWebViewClient(bar, searchField, MainActivity.this));
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                bar.setProgress(newProgress);
            }
        });

        LoadUrl("google.com");

        searchField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                if(searchField.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Empty URL cannot be processed", Toast.LENGTH_LONG).show();
                    return false;
                } else{
                    LoadUrl(searchField.getText().toString());
//                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
//                        imm.hideSoftInputFromWindow(searchField.getWindowToken(),0);
                    return true;
                }
            }
        });

//        searchField.setOnFocusChangeListener((arg0, gotfocus) -> {
//            if(gotfocus)
//            {
//                searchField.setCompoundDrawables(null, null, null, null);
//            }
//            else
//            {
//                if(searchField.getText().length()==0)
//                    searchField.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search, 0, 0, 0);
//            }
//        });

        imageview.setOnClickListener(view -> {
            searchField.setText("");
        });

        back.setOnClickListener(view -> {
            if (webView.canGoBack()) {
                webView.goBack();
            }
        });

        forward.setOnClickListener(view -> {
            if (webView.canGoForward()) {
                webView.goForward();
            }
        });

        refresh.setOnClickListener(view -> {
            webView.reload();
        });

        more.setOnClickListener(view -> {
            Toast.makeText(this, "Coming Soon", Toast.LENGTH_LONG).show();
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, webView.getUrl());
                intent.setType("text/plain");
                startActivity(intent);
            }
        });
    }


    void LoadUrl(String url) {
        if(url.isEmpty()){
            Toast.makeText(MainActivity.this, "Empty URL cannot be processed", Toast.LENGTH_LONG).show();
            return;
        } else{
            boolean matchUrl = Patterns.WEB_URL.matcher(url).matches();
            if (matchUrl) {
                webView.loadUrl(url);
            } else {
                webView.loadUrl("google.com/search?q=" + url);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}