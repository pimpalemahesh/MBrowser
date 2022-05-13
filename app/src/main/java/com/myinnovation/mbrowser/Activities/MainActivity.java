package com.myinnovation.mbrowser.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.monstertechno.adblocker.AdBlockerWebView;
import com.myinnovation.mbrowser.UtilitiClasses.AdBlockViewClient;
import com.myinnovation.mbrowser.UtilitiClasses.MyWebViewClient;
import com.myinnovation.mbrowser.R;
import com.myinnovation.mbrowser.databinding.ActivityMainBinding;
import com.squareup.picasso.Picasso;

import soup.neumorphism.NeumorphButton;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    TextView setting, bookmarks, share, about, history;
    NeumorphButton signIn;
    WebView webView;
    DrawerLayout drawerLayout;
    CheckBox desktopMode;
    SwitchCompat adBlockerSwitch;
    boolean blockAd = false;
    private String PREVIOUSURL = "";
    private static final String DESKTOP_USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2049.0 Safari/537.36";
    private static final String MOBILE_USER_AGENT = "Mozilla/5.0 (Linux; U; Android 4.4; en-us; Nexus 4 Build/JOP24G) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30";
    private final String SEARCH_ENGINE_URL = "google.com/search?q=";
    private final String HOMEIMAGEURL = "https://source.unsplash.com/1600x900/?nature,water,flower,sea,mountain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        InitializeViews();
        LoadHomeImage();
        drawerLayout.setVisibility(View.GONE);

        
        // use getIntent value only at once and then intent is killed.
        try{
            Bundle data = getIntent().getExtras();
            if(data != null){
                PREVIOUSURL = data.getString("URL");
                LoadUrl(PREVIOUSURL);
                webView.setVisibility(View.VISIBLE);
                binding.homeImage.setVisibility(View.GONE);
                webView.reload();
                getIntent().removeExtra("URL");
            }
        } catch (Exception e){
            e.printStackTrace();
        }


        // drawerClose checks
//        drawerClose();

        // WebView implementation
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);

        // Ad Blocker Implementation
        if(!blockAd){
            new AdBlockerWebView.init(this).initializeWebView(webView);
            webView.setWebViewClient(new AdBlockViewClient(binding.bar, binding.addresslink, MainActivity.this));
        } else{
            webView.setWebViewClient(new MyWebViewClient(binding.bar, binding.addresslink, MainActivity.this));
        }
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                binding.bar.setProgress(newProgress);
            }
        });

//        LoadUrl("google.com");
//        if(binding.addresslink.isFocused() || !binding.addresslink.getText().toString().isEmpty()){
//            webView.setVisibility(View.VISIBLE);
//            binding.homeImage.setVisibility(View.GONE);
//        }
//        else{
//            webView.setVisibility(View.GONE);
//            binding.homeImage.setVisibility(View.VISIBLE);
//        }

        binding.addresslink.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            drawerClose();
            if(binding.addresslink.getText().toString().isEmpty()){
                Toast.makeText(MainActivity.this, "Empty URL cannot be processed", Toast.LENGTH_LONG).show();
                return false;
            } else{
                LoadUrl(binding.addresslink.getText().toString());
                return true;
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

        binding.clearText.setOnClickListener(view -> {
            drawerClose();
            binding.addresslink.setText("");
            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.showSoftInput(binding.addresslink, 0);
        });

        binding.home.setOnClickListener(view -> {
            binding.addresslink.setText("");
            recreate();
        });

        binding.back.setOnClickListener(view -> {
            drawerClose();
            if (webView.canGoBack()) {
                webView.goBack();
            }
        });

        binding.forward.setOnClickListener(view -> {
            drawerClose();
            if (webView.canGoForward()) {
                webView.goForward();
            }
        });

        binding.refresh.setOnClickListener(view -> {
            drawerClose();
            if(binding.addresslink.getText().toString().isEmpty()){
                LoadHomeImage();
                recreate();
                LoadUrl("");
            } else{
                webView.reload();
            }
        });

        binding.more.setOnClickListener(view -> {
            if(drawerLayout.getVisibility() == View.VISIBLE){
                drawerLayout.setVisibility(View.GONE);
            } else{
                drawerLayout.setVisibility(View.VISIBLE);
            }
        });



        binding.link.setOnClickListener(view -> {
            drawerClose();
            startActivity(new Intent(MainActivity.this, LinksActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
        });

        // These are methods implemented on textViews in drawerLayout field

        signIn.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, SignInActivity.class));
        });
        share.setOnClickListener(view -> {
            drawerClose();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, webView.getUrl());
            intent.setType("text/plain");
            startActivity(intent);
        });

        setting.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, SettingActivity.class));
        });

        history.setOnClickListener(view -> {
            Toast.makeText(MainActivity.this, "Sign in required", Toast.LENGTH_LONG).show();
        });

        bookmarks.setOnClickListener(view -> {
            Toast.makeText(MainActivity.this, "Sign in required", Toast.LENGTH_LONG).show();
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

    private void LoadHomeImage() {
        Picasso.get()
                .load(HOMEIMAGEURL)
                .placeholder(R.drawable.ic__)
                .into(binding.homeImage);
    }

    private void InitializeViews() {
        webView = findViewById(R.id.webPage);
        drawerLayout = findViewById(R.id.drawerLayout);
        signIn = findViewById(R.id.signIn);
        about = findViewById(R.id.about);
        setting = findViewById(R.id.setting);
        bookmarks = findViewById(R.id.bookmarks);
        share = findViewById(R.id.share);
        history = findViewById(R.id.history);
        desktopMode = findViewById(R.id.check_desktop_mode);
        adBlockerSwitch = findViewById(R.id.switch_ad_blocker);
    }


    void LoadUrl(String url) {
        if(url.isEmpty()){
            binding.homeImage.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this, "Empty URL cannot be processed", Toast.LENGTH_LONG).show();
            return;
        } else{
            webView.setVisibility(View.VISIBLE);
            binding.homeImage.setVisibility(View.GONE);
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
        if(binding.homeImage.getVisibility() == View.VISIBLE){
            binding.homeImage.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
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


    private void destroyWebView() {

        // Make sure you remove the WebView from its parent view before doing anything.
        binding.webViewParent.removeAllViews();

        webView.clearHistory();

        // NOTE: clears RAM cache, if you pass true, it will also clear the disk cache.
        // Probably not a great idea to pass true if you have other WebViews still alive.
        webView.clearCache(true);

        // Loading a blank page is optional, but will ensure that the WebView isn't doing anything when you destroy it.
        webView.loadUrl("about:blank");

        webView.onPause();
        webView.removeAllViews();
        webView.destroyDrawingCache();

        // NOTE: This pauses JavaScript execution for ALL WebViews,
        // do not use if you have other WebViews still alive.
        // If you create another WebView after calling this,
        // make sure to call mWebView.resumeTimers().
        webView.pauseTimers();

        // NOTE: This can occasionally cause a segfault below API 17 (4.2)
        webView.destroy();

        // Null out the reference so that you don't end up re-using it.
        webView = null;
    }
}