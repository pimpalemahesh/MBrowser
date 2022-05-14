package com.myinnovation.mbrowser.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myinnovation.mbrowser.Adapters.LinkAdapter;
import com.myinnovation.mbrowser.Models.LinkModel;
import com.myinnovation.mbrowser.R;
import com.myinnovation.mbrowser.databinding.FragmentLinkBinding;

import java.util.ArrayList;

public class LinkFragment extends Fragment {

    FragmentLinkBinding binding;
    ArrayList<LinkModel> impList;
    LinkAdapter impAdapter;
    ArrayList<LinkModel> trendList;
    LinkAdapter trendAdapter;

    public LinkFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLinkBinding.inflate(inflater);

        impList = new ArrayList<>();
        trendList = new ArrayList<>();

        impList.add(new LinkModel("Google", R.drawable.google, "https://www.google.com/"));
        impList.add(new LinkModel("Facebook", R.drawable.facebook, "https://www.facebook.com/"));
        impList.add(new LinkModel("YouTube", R.drawable.youtube, "https://www.youtube.com/"));
        impList.add(new LinkModel("Google Map", R.drawable.googlemaps, "https://www.googlemaps.com/"));
        impList.add(new LinkModel("Instagram", R.drawable.instagram, "https://www.instagram.com/"));
        impList.add(new LinkModel("WhatsApp", R.drawable.whatsapp, "https://web.whatsapp.com/"));
        impList.add(new LinkModel("Twitter", R.drawable.twitter, "https://www.twitter.com/"));
        impList.add(new LinkModel("Telegram", R.drawable.telegram, "https://www.telegram.com/"));
        impList.add(new LinkModel("SnapChat", R.drawable.snapchat, "https://www.snapchat.com/"));
        impList.add(new LinkModel("Wikipedia", R.drawable.wikipedia, "https://www.wikipedia.org/"));
        impList.add(new LinkModel("Linkedin", R.drawable.linkedin, "https://www.linkedin.com/"));
        impList.add(new LinkModel("Gmail", R.drawable.gmail, "https://www.gmail.com/"));

        impAdapter = new LinkAdapter(this.impList, getContext());
        binding.impUrlRclv.setLayoutManager(new GridLayoutManager(getContext(), 4));
        binding.impUrlRclv.setHasFixedSize(true);
        binding.impUrlRclv.setNestedScrollingEnabled(false);
        binding.impUrlRclv.setAdapter(impAdapter);

        trendList.add(new LinkModel("Netflix", R.drawable.netflix, "https://www.netflix.com/"));
        trendList.add(new LinkModel("Amazon Prime", R.drawable.amazon_prime, "https://www.primevideo.com/"));
        trendList.add(new LinkModel("Disney+Hotstar", R.drawable.hotstar, "https://www.hotstar.com/"));
        trendList.add(new LinkModel("Zee5", R.drawable.zee5, "https://www.zee5.com/"));
        trendList.add(new LinkModel("JioCinema", R.drawable.jiocinema, "https://www.jiocinema.com/"));
        trendList.add(new LinkModel("Tiktok", R.drawable.tiktok, "https://www.tiktok.com/"));
        trendList.add(new LinkModel("Amazon", R.drawable.amazon, "https://www.amazon.com/"));
        trendList.add(new LinkModel("FlipKart", R.drawable.flipkart, "https://www.flipkart.com/"));
        trendList.add(new LinkModel("Zomato", R.drawable.zomato, "https://www.zomato.com/"));
        trendList.add(new LinkModel("Swiggy", R.drawable.swiggy, "https://www.swiggy.com/"));



        trendAdapter = new LinkAdapter(this.trendList, getContext());
        binding.trendingUrlRclv.setLayoutManager(new GridLayoutManager(getContext(), 4));
        binding.trendingUrlRclv.setHasFixedSize(true);
        binding.trendingUrlRclv.setNestedScrollingEnabled(false);
        binding.trendingUrlRclv.setAdapter(trendAdapter);

        return binding.getRoot();
    }

}