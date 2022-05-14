package com.myinnovation.mbrowser.Fragment;

import android.os.Bundle;

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

    private static String SendURL = "google.com";
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLinkBinding.inflate(inflater);

        impList = new ArrayList<>();
        trendList = new ArrayList<>();

        impList.add(new LinkModel("Google", R.drawable.google, "https://www.google.com/"));
        impList.add(new LinkModel("Facebook", R.drawable.facebook, "https://www.facebook.com/"));
        impList.add(new LinkModel("YouTube", R.drawable.youtube, "https://www.youtube.com/"));
        impList.add(new LinkModel("Instagram", R.drawable.instagram, "https://www.instagram.com/"));
        impList.add(new LinkModel("Twitter", R.drawable.twitter, "https://www.twitter.com/"));
        impList.add(new LinkModel("Google Map", R.drawable.googlemaps, "https://www.googlemaps.com/"));
        impList.add(new LinkModel("Wikipedia", R.drawable.wikipedia, "https://www.wikipedia.org/"));
        impList.add(new LinkModel("Linkedin", R.drawable.linkedin, "https://www.linkedin.com/"));

        impAdapter = new LinkAdapter(this.impList, getContext());
        binding.impUrlRclv.setLayoutManager(new GridLayoutManager(getContext(), 4));
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

        trendAdapter = new LinkAdapter(this.trendList, getContext());
        binding.trendingUrlRclv.setLayoutManager(new GridLayoutManager(getContext(), 4));
        binding.trendingUrlRclv.setHasFixedSize(true);
        binding.trendingUrlRclv.setAdapter(trendAdapter);

        return binding.getRoot();
    }

}