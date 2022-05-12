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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.monstertechno.adblocker.AdBlockerWebView;
import com.myinnovation.mbrowser.UtilitiClasses.AdBlockViewClient;
import com.myinnovation.mbrowser.UtilitiClasses.AdBlocker;
import com.myinnovation.mbrowser.UtilitiClasses.MyWebViewClient;
import com.myinnovation.mbrowser.R;

public class MainActivity extends AppCompatActivity {

    ImageView link, clearText, back, forward, refresh, more, home;
    EditText searchField;
    TextView setting, bookmarks, share, about;
    WebView webView;
    ProgressBar bar;
    DrawerLayout drawerLayout;
    CheckBox desktopMode;
    Switch adBlockerSwitch;
    boolean blockAd = false;

    private static final String DESKTOP_USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2049.0 Safari/537.36";
    private static final String MOBILE_USER_AGENT = "Mozilla/5.0 (Linux; U; Android 4.4; en-us; Nexus 4 Build/JOP24G) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30";
    private final String SEARCH_ENGINE_URL = "google.com/search?q=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitializeViews();

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
//        new AdBlockerWebView.init(this).initializeWebView(webView);
//        webView.setWebViewClient(new AdBlockViewClient(bar, searchField, MainActivity.this));
        if(!blockAd){
            new AdBlockerWebView.init(this).initializeWebView(webView);
            webView.setWebViewClient(new AdBlockViewClient(bar, searchField, MainActivity.this));
        } else{
            webView.setWebViewClient(new MyWebViewClient(bar, searchField, MainActivity.this));
        }
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
                drawerClose();
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

        clearText.setOnClickListener(view -> {
            drawerClose();
            searchField.setText("");
        });

        home.setOnClickListener(view -> {
            LoadUrl("google.com");
        });

        back.setOnClickListener(view -> {
            drawerClose();
            if (webView.canGoBack()) {
                webView.goBack();
            }
        });

        forward.setOnClickListener(view -> {
            drawerClose();
            if (webView.canGoForward()) {
                webView.goForward();
            }
        });

        refresh.setOnClickListener(view -> {
            drawerClose();
            webView.reload();
        });

        webView.setOnClickListener(view -> {
            drawerClose();
        });

        more.setOnClickListener(view -> {
            if(drawerLayout.getVisibility() == View.VISIBLE){
                drawerLayout.setVisibility(View.GONE);
            } else{
                drawerLayout.setVisibility(View.VISIBLE);
            }
        });

        share.setOnClickListener(view -> {
            drawerClose();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, webView.getUrl());
            intent.setType("text/plain");
            startActivity(intent);
        });

        link.setOnClickListener(view -> {
            drawerClose();
            startActivity(new Intent(MainActivity.this, LinksActivity.class).setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP));
        });

        setting.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, SettingActivity.class));
        });

        bookmarks.setOnClickListener(view -> {
            Toast.makeText(MainActivity.this, "Bookmark page", Toast.LENGTH_LONG).show();
        });

        desktopMode.setOnCheckedChangeListener((compoundButton, b) -> {
            if(desktopMode.isChecked()){
                webView.getSettings().setUserAgentString(DESKTOP_USER_AGENT);
                webView.reload();
                drawerClose();
            }
            else{
                webView.getSettings().setUserAgentString(MOBILE_USER_AGENT);
                webView.reload();
                drawerClose();
            }
        });

        adBlockerSwitch.setOnCheckedChangeListener((compoundButton, checked) -> {
            blockAd = checked;
            drawerClose();
            webView.reload();
        });
    }

    private void InitializeViews() {
        link = findViewById(R.id.link);
        clearText = findViewById(R.id.desImageview);
        home = findViewById(R.id.home);
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
        desktopMode = findViewById(R.id.check_desktop_mode);
        adBlockerSwitch = findViewById(R.id.switch_ad_blocker);
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
                webView.loadUrl(SEARCH_ENGINE_URL + url);
            }
        }
    }

    private void drawerClose(){
        if(drawerLayout.getVisibility() == View.VISIBLE){
            drawerLayout.setVisibility(View.GONE);
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