package com.myinnovation.mbrowser.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.myinnovation.mbrowser.databinding.ActivityBookMarksBinding;

public class BookMarksActivity extends AppCompatActivity {

    ActivityBookMarksBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookMarksBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}