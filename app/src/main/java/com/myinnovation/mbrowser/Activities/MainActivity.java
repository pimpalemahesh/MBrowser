package com.myinnovation.mbrowser.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.myinnovation.mbrowser.MyWebViewClient;
import com.myinnovation.mbrowser.R;

public class MainActivity extends AppCompatActivity {

    ImageView link, imageview, back, forward, refresh, more, share;
    EditText searchField;
    Button setting, bookmarks;
    WebView webView;
    ProgressBar bar;
    DrawerLayout drawerLayout;
//    NavigationView navigationView;

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
        drawerLayout = findViewById(R.id.drawerLayout);
        setting = findViewById(R.id.setting);
        bookmarks = findViewById(R.id.bookmarks);

        setting.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, SettingActivity.class));
        });

        bookmarks.setOnClickListener(view -> {
            Toast.makeText(MainActivity.this, "Bookmark page", Toast.LENGTH_LONG).show();
        });
//        navigationView = findViewById(R.id.navigationView);

//        if(drawerLayout.isOpen()){
//            drawerLayout.closeDrawer(GravityCompat.END, false);
//        }


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
//            if(drawerLayout.isOpen()){
//                drawerLayout.closeDrawers();
//            } else{
//                drawerLayout.openDrawer(GravityCompat.END);
//            }

            if(drawerLayout.getVisibility() == View.VISIBLE){
                drawerLayout.setVisibility(View.GONE);
            } else{
                drawerLayout.setVisibility(View.VISIBLE);
            }
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

        link.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, LinksActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
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