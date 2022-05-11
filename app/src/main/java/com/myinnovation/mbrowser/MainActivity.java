package com.myinnovation.mbrowser;

import static com.myinnovation.mbrowser.R.drawable.ic_baseline_mic_24;
import static com.myinnovation.mbrowser.R.drawable.ic_cancle;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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

        webView.setWebViewClient(new MyWebViewClient(bar));
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                bar.setProgress(newProgress);
            }
        });

        LoadUrl("google.com");

        searchField.setOnEditorActionListener((textView, i, keyEvent) -> {
            if(i == EditorInfo.IME_ACTION_GO || i == EditorInfo.IME_ACTION_DONE){
                InputMethodManager manager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(searchField.getWindowToken(), 0);
                LoadUrl(searchField.getText().toString());
                return true;
            }
            return false;
        });

//        searchField.setOnFocusChangeListener((arg0, gotfocus) -> {
//            // TODO Auto-generated method stub
//            if(gotfocus)
//            {
//                searchField.setCompoundDrawables(null, null, null, null);
//                imageview.setImageResource(ic_cancle);
//            }
//            else if(!gotfocus)
//            {
//                if(searchField.getText().length()==0)
//                    imageview.setImageResource(ic_baseline_mic_24);
//                    searchField.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search, 0, 0, 0);
//            }
//        });

        imageview.setOnClickListener(view -> {
            if(imageview.getDrawable() == getDrawable(ic_cancle)){
                searchField.setText("");
            } else{

            }
        });

        back.setOnClickListener(view -> {
            if(webView.canGoBack()){
                webView.goBack();
            }
        });

        forward.setOnClickListener(view -> {
            if(webView.canGoForward()){
                webView.goForward();
            }
        });

        refresh.setOnClickListener(view -> {
            webView.reload();
        });

        share.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, webView.getUrl());
            startActivity(intent);
        });
    }


    private void LoadUrl(String url){
        boolean rightUrl = Patterns.WEB_URL.matcher(url).matches();
        if(rightUrl){
            webView.loadUrl(url);
        } else{
            webView.loadUrl("google.com/search?q="+url);
        }
    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack();
        } else{
            super.onBackPressed();
        }
    }
}