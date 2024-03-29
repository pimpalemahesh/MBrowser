package com.myinnovation.mbrowser.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.monstertechno.adblocker.AdBlockerWebView;
import com.myinnovation.mbrowser.Fragment.LinkFragment;
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
    LinkFragment fragment = new LinkFragment();

    TextView logout, setting, bookmarks, share, about, feedback;
    ImageView profileImage;
    NeumorphButton signIn;

    DrawerLayout drawerLayout;
    CheckBox desktopMode;
    SwitchCompat adBlockerSwitch;
    boolean blockAd = false;
    boolean isFragmentOpened = false;
    boolean isUserSignIn = false;

    private static final String DESKTOP_USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2049.0 Safari/537.36";
    private static final String MOBILE_USER_AGENT = "Mozilla/5.0 (Linux; U; Android 4.4; en-us; Nexus 4 Build/JOP24G) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30";
    private String SEARCH_ENGINE_URL = "google.com/search?q=";

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        InitializeViews();
        checkSearchEngine();
        checkCurrentUser();
        LoadHomeImage();
        drawerLayout.setVisibility(View.GONE);

        
        // use getIntent value only at once and then intent is killed.
        try{
            Bundle data = getIntent().getExtras();
            if(data != null){
                String PREVIOUSURL = data.getString("URL");
                LoadUrl(PREVIOUSURL);
                binding.webPage.setVisibility(View.VISIBLE);
                binding.homeImageContainer.setVisibility(View.GONE);
                binding.webPage.reload();
                getIntent().removeExtra("URL");
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        // WebView implementation
        WebSettings settings = binding.webPage.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);

        // Ad Blocker Implementation
        if(!blockAd){
            new AdBlockerWebView.init(this).initializeWebView(binding.webPage);
            binding.webPage.setWebViewClient(new AdBlockViewClient(binding.bar, binding.addresslink, MainActivity.this));
        } else{
            binding.webPage.setWebViewClient(new MyWebViewClient(binding.bar, binding.addresslink, MainActivity.this));
        }
        binding.webPage.setWebChromeClient(new MyChrome());

        binding.link.setOnClickListener(view -> {
            drawerClose();
            if(isFragmentOpened){
                closeFragment();
            }
            else{
                openFragment();
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
            if(isFragmentOpened){
                closeFragment();
            }
            gotoHomePage();
        });

        binding.back.setOnClickListener(view -> {
            drawerClose();
            if (binding.webPage.canGoBack()) {
                binding.webPage.goBack();
            }
        });

        binding.forward.setOnClickListener(view -> {
            drawerClose();
            if (binding.webPage.canGoForward()) {
                binding.webPage.goForward();
            }
        });

        binding.refresh.setOnClickListener(view -> {
            drawerClose();
            if(binding.addresslink.getText().toString().isEmpty()){
                LoadHomeImage();
                gotoHomePage();
            } else{
                binding.webPage.reload();
            }
        });

        binding.more.setOnClickListener(view -> {
            if(drawerLayout.getVisibility() == View.VISIBLE){
                drawerLayout.setVisibility(View.GONE);
            } else{
                drawerLayout.setVisibility(View.VISIBLE);
            }
        });


        // These are methods implemented on textViews in drawerLayout field

        signIn.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, SignInActivity.class)));

        logout.setOnClickListener(view -> {
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

            if(mAuth.getCurrentUser() != null){
                mAuth.signOut();
                drawerClose();
                Toast.makeText(MainActivity.this, "Logged out successfully", Toast.LENGTH_LONG).show();
                recreate();
            }
            else if(account != null){
                GoogleSignInOptions gso = new GoogleSignInOptions.
                        Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                        build();

                GoogleSignInClient googleSignInClient=GoogleSignIn.getClient(this,gso);
                googleSignInClient.signOut().addOnSuccessListener(unused -> {
                    drawerClose();
                    Toast.makeText(MainActivity.this, "Logged out successfully", Toast.LENGTH_LONG).show();
                    recreate();
                });

            } else{
                drawerClose();
                Toast.makeText(MainActivity.this, "You are already logout", Toast.LENGTH_LONG).show();
            }

        });

        share.setOnClickListener(view -> {
            drawerClose();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, binding.webPage.getUrl());
            intent.setType("text/plain");
            startActivity(intent);
        });

        setting.setOnClickListener(view -> {
            drawerClose();
            startActivity(new Intent(MainActivity.this, SettingActivity.class));
        });

        bookmarks.setOnClickListener(view -> {
            if(isUserSignIn){
                drawerClose();
                startActivity(new Intent(this, BookMarksActivity.class));
            } else{
                Toast.makeText(MainActivity.this, "Sign in required", Toast.LENGTH_LONG).show();
            }
        });

        desktopMode.setOnCheckedChangeListener((compoundButton, b) -> {
            if(desktopMode.isChecked()){
                binding.webPage.getSettings().setUserAgentString(DESKTOP_USER_AGENT);
            }
            else{
                binding.webPage.getSettings().setUserAgentString(MOBILE_USER_AGENT);
            }
            binding.webPage.reload();
            drawerClose();
        });

        adBlockerSwitch.setOnCheckedChangeListener((compoundButton, checked) -> {
            blockAd = checked;
            drawerClose();
            binding.webPage.reload();
        });

        feedback.setOnClickListener(view -> {
            if(isUserSignIn){
                drawerClose();
                startActivity(new Intent(this, FeedbackActivity.class));
            } else{
                Toast.makeText(MainActivity.this, "Sign in required", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void checkSearchEngine() {
        String searchEngine = "Google";
        if(searchEngine.equals("Google")){
            SEARCH_ENGINE_URL = "google.com/search?q=";
            binding.addresslink.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_icongoogle, 0, 0, 0);
        }
        else if(searchEngine.equals("DuckDuckGo")){
            SEARCH_ENGINE_URL = "duckduckgo.com/?q";
            binding.addresslink.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_icongoogle, 0, 0, 0);
        }
    }

    private void LoadHomeImage() {
        String HOMEIMAGEURL = "https://source.unsplash.com/1600x900/?nature,water,flower,sea,mountain,forest,river,stars,space,waterfall,snow,rain";
        String[] URLs = new String[]{"https://source.unsplash.com/1600x900/?nature", "https://source.unsplash.com/1600x900/?water", "https://source.unsplash.com/1600x900/?sea", "https://source.unsplash.com/1600x900/?flower",
                                        "https://source.unsplash.com/1600x900/?forest", "https://source.unsplash.com/1600x900/?mountain", "https://source.unsplash.com/1600x900/?river", "https://source.unsplash.com/1600x900/?forest",
                                        "https://source.unsplash.com/1600x900/?sunrise", "https://source.unsplash.com/1600x900/?sunset", "https://source.unsplash.com/1600x900/?ocean", "https://source.unsplash.com/1600x900/?canyon",
                                        "https://source.unsplash.com/1600x900/?sand,bay", "https://source.unsplash.com/1600x900/?hill", "https://source.unsplash.com/1600x900/?ice", "https://source.unsplash.com/1600x900/?clouds",
                                        "https://source.unsplash.com/1600x900/?forest", "https://source.unsplash.com/1600x900/?mountain", "https://source.unsplash.com/1600x900/?ice", "https://source.unsplash.com/1600x900/?cloud",
                                        "https://source.unsplash.com/1600x900/?space", "https://source.unsplash.com/1600x900/?waterfall", "https://source.unsplash.com/1600x900/?snow", "https://source.unsplash.com/1600x900/?rain"};

        int b = (int)(Math.random()*(20+1)+1);

        Picasso.get()
                .load(URLs[b])
                .placeholder(R.drawable.ic_logo)
                .into(binding.homeImage);
    }

    private void InitializeViews() {

        // DrawerLayout Views
        profileImage = findViewById(R.id.profileImage);
        logout = findViewById(R.id.logout);
        drawerLayout = findViewById(R.id.drawerLayout);
        signIn = findViewById(R.id.signIn);
        about = findViewById(R.id.about);
        setting = findViewById(R.id.setting);
        bookmarks = findViewById(R.id.bookmarks);
        share = findViewById(R.id.share);
        desktopMode = findViewById(R.id.check_desktop_mode);
        adBlockerSwitch = findViewById(R.id.switch_ad_blocker);
        feedback = findViewById(R.id.feedback);


        binding.addresslink.setText("");


    }


    void LoadUrl(String url) {
        ConnectivityManager cm = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnectedOrConnecting()){
            if(url.isEmpty()){
                binding.homeImage.setVisibility(View.VISIBLE);
                binding.webPage.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Empty URL cannot be processed", Toast.LENGTH_LONG).show();
            } else{
                binding.webPage.setVisibility(View.VISIBLE);
                binding.homeImage.setVisibility(View.GONE);
                boolean matchUrl = Patterns.WEB_URL.matcher(url).matches();
                if (matchUrl) {
                    binding.webPage.loadUrl(url);
                } else {
                    binding.webPage.loadUrl(SEARCH_ENGINE_URL + url);
                }
            }
        } else{
            Toast.makeText(this, "You are offline", Toast.LENGTH_LONG).show();
        }

    }

    private void gotoHomePage() {
        binding.addresslink.setText("");
        binding.webPage.clearHistory();
        binding.webPage.clearCache(true);
        binding.webPage.destroy();
        recreate();
    }

    private void drawerClose(){
        if(drawerLayout.getVisibility() == View.VISIBLE){
            drawerLayout.setVisibility(View.GONE);
        }
        if(binding.homeImageContainer.getVisibility() == View.VISIBLE){
            binding.homeImageContainer.setVisibility(View.GONE);
            binding.webPage.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onBackPressed() {
        if (binding.webPage.canGoBack()) {
            binding.webPage.goBack();
        } else{
            gotoHomePage();
        }
        if(isFragmentOpened){
            closeFragment();
        }
    }

    private void checkCurrentUser(){
        if(mAuth.getCurrentUser() != null){
            isUserSignIn = true;
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
                                    binding.homeUserName.setText(user.getUsername());
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
            isUserSignIn = true;
            signIn.setText(account.getDisplayName());
            profileImage.setImageURI(account.getPhotoUrl());
            binding.homeUserName.setText(account.getDisplayName());
        }
    }


    public void openFragment() {
        isFragmentOpened = true;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
        transaction.addToBackStack(null);
        transaction.add(R.id.fragment_container, fragment, "BLANK_FRAGMENT").commit();
    }

    public void closeFragment(){
        isFragmentOpened = false;
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.remove(fragment);
        trans.commit();
        manager.popBackStack();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            binding.toolbar3.setVisibility(View.GONE);
        }
        else if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            binding.toolbar3.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        binding.webPage.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        binding.webPage.restoreState(savedInstanceState);
    }

    private class MyChrome extends WebChromeClient {

        private View mCustomView;
        private WebChromeClient.CustomViewCallback mCustomViewCallback;
        protected FrameLayout mFullscreenContainer;
        private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;

        MyChrome() {}

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onPermissionRequest(final PermissionRequest request) {
            request.grant(request.getResources());
        }

        public Bitmap getDefaultVideoPoster()
        {
            if (mCustomView == null) {
                return null;
            }
            return BitmapFactory.decodeResource(getApplicationContext().getResources(), 2130837573);
        }

        public void onHideCustomView()
        {
            ((FrameLayout)getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
            setRequestedOrientation(this.mOriginalOrientation);
            this.mCustomViewCallback.onCustomViewHidden();
            this.mCustomViewCallback = null;
        }

        public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback)
        {
            if (this.mCustomView != null)
            {
                onHideCustomView();
                return;
            }
            this.mCustomView = paramView;
            this.mOriginalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
            this.mOriginalOrientation = getRequestedOrientation();
            this.mCustomViewCallback = paramCustomViewCallback;
            ((FrameLayout)getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
            getWindow().getDecorView().setSystemUiVisibility(3846 | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }
}