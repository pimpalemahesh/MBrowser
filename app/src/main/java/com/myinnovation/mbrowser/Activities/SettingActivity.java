package com.myinnovation.mbrowser.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.myinnovation.mbrowser.R;
import com.myinnovation.mbrowser.databinding.ActivitySettingBinding;

public class SettingActivity extends AppCompatActivity {

    ActivitySettingBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}