package com.myinnovation.mbrowser.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import com.myinnovation.mbrowser.Adapters.LinkAdapter;
import com.myinnovation.mbrowser.Models.LinkModel;
import com.myinnovation.mbrowser.R;
import com.myinnovation.mbrowser.databinding.ActivityLinksBinding;

import java.util.ArrayList;

public class LinksActivity extends AppCompatActivity {

    Toolbar toolbar;
    ArrayList<LinkModel> impList;
    LinkAdapter impAdapter;

    ArrayList<LinkModel> trendList;
    LinkAdapter trendAdapter;

    ActivityLinksBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLinksBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        impList = new ArrayList<>();
        trendList = new ArrayList<>();

        impList.add(new LinkModel("Google", R.drawable.google, "https://www.google.com/"));
        impList.add(new LinkModel("Facebook", R.drawable.facebook, "https://www.facebook.com/"));
        impList.add(new LinkModel("YouTube", R.drawable.youtube, "https://www.youtube.com/"));
        impList.add(new LinkModel("Instagram", R.drawable.instagram, "https://www.instagram.com/"));
        impList.add(new LinkModel("Twitter", R.drawable.twitter, "https://www.twitter.com/"));
        impList.add(new LinkModel("Google Map", R.drawable.googlemaps, "https://www.googlemaps.com/"));
        impList.add(new LinkModel("Wikipedia", R.drawable.wikipedia, "https://www.wikipedia.com/"));
        impList.add(new LinkModel("Linkedin", R.drawable.linkedin, "https://www.linkedin.com/"));

        impAdapter = new LinkAdapter(this.impList, LinksActivity.this);
        binding.impUrlRclv.setLayoutManager(new GridLayoutManager(this, 4));
        binding.impUrlRclv.setHasFixedSize(true);
        binding.impUrlRclv.setAdapter(impAdapter);

        trendList.add(new LinkModel("Google", R.drawable.google, "https://www.google.com/"));
        trendList.add(new LinkModel("Facebook", R.drawable.facebook, "https://www.google.com/"));
        trendList.add(new LinkModel("YouTube", R.drawable.youtube, "https://www.google.com/"));
        trendList.add(new LinkModel("Instagram", R.drawable.instagram, "https://www.google.com/"));
        trendList.add(new LinkModel("Twitter", R.drawable.twitter, "https://www.google.com/"));
        trendList.add(new LinkModel("Gmail", R.drawable.gmail, "https://www.google.com/"));
        trendList.add(new LinkModel("Wikipedia", R.drawable.wikipedia, "https://www.google.com/"));
        trendList.add(new LinkModel("Linkedin", R.drawable.linkedin, "https://www.google.com/"));

        trendAdapter = new LinkAdapter(this.trendList, LinksActivity.this);
        binding.trendingUrlRclv.setLayoutManager(new GridLayoutManager(this, 4));
        binding.trendingUrlRclv.setHasFixedSize(true);
        binding.trendingUrlRclv.setAdapter(trendAdapter);

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(LinksActivity.this, MainActivity.class));
        super.onBackPressed();
    }
}