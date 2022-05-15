package com.myinnovation.mbrowser.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myinnovation.mbrowser.Adapters.LinkParentAdapter;
import com.myinnovation.mbrowser.Models.LinkChildModel;
import com.myinnovation.mbrowser.Models.LinkParentModel;
import com.myinnovation.mbrowser.R;
import com.myinnovation.mbrowser.databinding.FragmentLinkBinding;

import java.util.ArrayList;

public class LinkFragment extends Fragment {

    FragmentLinkBinding binding;
    ArrayList<LinkParentModel> parentModelArrayList;
    ArrayList<LinkChildModel> impList;
    ArrayList<LinkChildModel> trendList;
    ArrayList<LinkChildModel> otherList;

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

        parentModelArrayList = new ArrayList<>();
        impList = new ArrayList<>();
        trendList = new ArrayList<>();
        otherList = new ArrayList<>();

        impList.add(new LinkChildModel("Google", R.drawable.google, "https://www.google.com/"));
        impList.add(new LinkChildModel("Facebook", R.drawable.facebook, "https://www.facebook.com/"));
        impList.add(new LinkChildModel("YouTube", R.drawable.youtube, "https://www.youtube.com/"));
        impList.add(new LinkChildModel("Google Map", R.drawable.googlemaps, "https://www.googlemaps.com/"));
        impList.add(new LinkChildModel("WhatsApp", R.drawable.whatsapp, "https://web.whatsapp.com/"));
        impList.add(new LinkChildModel("Twitter", R.drawable.twitter, "https://www.twitter.com/"));
        impList.add(new LinkChildModel("Telegram", R.drawable.telegram, "https://www.telegram.com/"));
        impList.add(new LinkChildModel("SnapChat", R.drawable.snapchat, "https://www.snapchat.com/"));
        impList.add(new LinkChildModel("Wikipedia", R.drawable.wikipedia, "https://www.wikipedia.org/"));
        impList.add(new LinkChildModel("Linkedin", R.drawable.linkedin, "https://www.linkedin.com/"));
        impList.add(new LinkChildModel("Gmail", R.drawable.gmail, "https://www.gmail.com/"));

        trendList.add(new LinkChildModel("Netflix", R.drawable.netflix, "https://www.netflix.com/"));
        trendList.add(new LinkChildModel("Amazon Prime", R.drawable.amazon_prime, "https://www.primevideo.com/"));
        trendList.add(new LinkChildModel("Disney+Hotstar", R.drawable.hotstar, "https://www.hotstar.com/"));
        trendList.add(new LinkChildModel("Instagram", R.drawable.instagram, "https://www.instagram.com/"));
        trendList.add(new LinkChildModel("JioCinema", R.drawable.jiocinema, "https://www.jiocinema.com/"));
        trendList.add(new LinkChildModel("Tiktok", R.drawable.tiktok, "https://www.tiktok.com/"));
        trendList.add(new LinkChildModel("Amazon", R.drawable.amazon, "https://www.amazon.com/"));
        trendList.add(new LinkChildModel("FlipKart", R.drawable.flipkart, "https://www.flipkart.com/"));

        otherList.add(new LinkChildModel("Netflix", R.drawable.netflix, "https://www.netflix.com/"));
        otherList.add(new LinkChildModel("Amazon Prime", R.drawable.amazon_prime, "https://www.primevideo.com/"));
        otherList.add(new LinkChildModel("Disney+Hotstar", R.drawable.hotstar, "https://www.hotstar.com/"));
        otherList.add(new LinkChildModel("Zee5", R.drawable.zee5, "https://www.zee5.com/"));
        otherList.add(new LinkChildModel("JioCinema", R.drawable.jiocinema, "https://www.jiocinema.com/"));
        otherList.add(new LinkChildModel("Tiktok", R.drawable.tiktok, "https://www.tiktok.com/"));
        otherList.add(new LinkChildModel("Amazon", R.drawable.amazon, "https://www.amazon.com/"));
        otherList.add(new LinkChildModel("FlipKart", R.drawable.flipkart, "https://www.flipkart.com/"));
        otherList.add(new LinkChildModel("Zomato", R.drawable.zomato, "https://www.zomato.com/"));
        otherList.add(new LinkChildModel("Swiggy", R.drawable.swiggy, "https://www.swiggy.com/"));

        parentModelArrayList.add(new LinkParentModel("Important Sites", impList));
        parentModelArrayList.add(new LinkParentModel("Trending Sites", trendList));
        parentModelArrayList.add(new LinkParentModel("Other Useful Sites", trendList));
        LinkParentAdapter adapter = new LinkParentAdapter(parentModelArrayList, getContext());

        binding.parentRclv.setAdapter(adapter);
        binding.parentRclv.setHasFixedSize(true);
        binding.parentRclv.setNestedScrollingEnabled(false);
        binding.parentRclv.setLayoutManager(new LinearLayoutManager(getContext()));


        return binding.getRoot();
    }

}