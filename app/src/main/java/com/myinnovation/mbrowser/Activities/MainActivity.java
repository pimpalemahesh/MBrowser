package com.myinnovation.mbrowser.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.monstertechno.adblocker.AdBlockerWebView;
import com.myinnovation.mbrowser.Models.UserModel;
import com.myinnovation.mbrowser.UtilitiClasses.AdBlockViewClient;
import com.myinnovation.mbrowser.UtilitiClasses.MyWebViewClient;
import com.myinnovation.mbrowser.R;
import com.myinnovation.mbrowser.databinding.ActivityMainBinding;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import soup.neumorphism.NeumorphButton;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    TextView setting, bookmarks, share, about, history;
    ImageView profileImage;
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
    private final String HOMEIMAGEURL = "https://source.unsplash.com/1600x900/?nature,water,flower,sea,mountain,forest,river,stars,space,waterfall,snow,rain";

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        InitializeViews();
        checkCurrentUser();
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
                .placeholder(R.drawable.ic_logo)
                .into(binding.homeImage);
    }

    private void InitializeViews() {

        // DrawerLayout Views
        profileImage = findViewById(R.id.profileImage);
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

        }
    }


    private void destroyWebView() {

        binding.webViewParent.removeAllViews();
        webView.clearHistory();
        webView.clearCache(true);
        webView.loadUrl("about:blank");
        webView.onPause();
        webView.removeAllViews();
        webView.destroyDrawingCache();
        webView.pauseTimers();
        webView.destroy();
        webView = null;
    }

    private void checkCurrentUser(){
        if(mAuth.getCurrentUser() != null){
            FirebaseDatabase mBase = FirebaseDatabase.getInstance();
            mBase.getReference().child("Users")
                    .child(Objects.requireNonNull(mAuth.getUid()))
                    .addValueEventListener(new ValueEventListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                    UserModel user = snapshot.getValue(UserModel.class);
                                    assert user != null;
                                    signIn.setText(user.getUsername());
                            }
                            else{
                                signIn.setText("User In");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

        }

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null){
            signIn.setText(account.getDisplayName());
            profileImage.setImageURI(account.getPhotoUrl());
        }
    }

}