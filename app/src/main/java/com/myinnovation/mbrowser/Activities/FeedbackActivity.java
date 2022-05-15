package com.myinnovation.mbrowser.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.myinnovation.mbrowser.R;
import com.myinnovation.mbrowser.databinding.ActivityFeedbackBinding;

import java.util.Objects;

public class FeedbackActivity extends AppCompatActivity {

    ActivityFeedbackBinding binding;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase mBase = FirebaseDatabase.getInstance();
    ProgressDialog  dialog;
    String userId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFeedbackBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dialog = new ProgressDialog(this);
        dialog.setTitle("Feedback Sending");
        dialog.setIcon(R.drawable.ic_feedback);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        binding.sendFeedback.setOnClickListener(view -> {
            if (validateField()) {
                if (mAuth.getCurrentUser() != null) {
                    dialog.show();
                    userId = mAuth.getCurrentUser().getUid();
                    try {
                        mBase.getReference()
                                .child("Feedbacks")
                                .child(Objects.requireNonNull(userId))
                                .push()
                                .setValue(binding.feedbackText.getText().toString())
                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(FeedbackActivity.this, "Feedback Sent successfully", Toast.LENGTH_SHORT).show();
                                    binding.feedbackText.setText("");
                                    dialog.dismiss();
                                })
                                .addOnFailureListener(e -> Toast.makeText(FeedbackActivity.this, "Unable to Send Feedback", Toast.LENGTH_SHORT).show());
                    } catch (Exception e) {
                        dialog.dismiss();
                        Toast.makeText(FeedbackActivity.this, "Error occurred during feedback : \n" + e.getMessage() + "\ntry again", Toast.LENGTH_LONG).show();
                    }
                }

                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
                if (account != null) {
                    dialog.show();
                    userId = account.getId();
                    try {
                        mBase.getReference()
                                .child("Feedbacks")
                                .child(Objects.requireNonNull(userId))
                                .push()
                                .setValue(binding.feedbackText.getText().toString())
                                .addOnSuccessListener(unused -> {
                                    dialog.dismiss();
                                    Toast.makeText(FeedbackActivity.this, "Feedback Sent successfully", Toast.LENGTH_SHORT).show();
                                    binding.feedbackText.setText("");
                                })
                                .addOnFailureListener(e -> {
                                    dialog.dismiss();
                                    Toast.makeText(FeedbackActivity.this, "Unable to Send Feedback", Toast.LENGTH_SHORT).show();
                                });
                    } catch (Exception e) {
                        Toast.makeText(FeedbackActivity.this, "Error occurred during feedback : \n" + e.getMessage()+ "\ntry again", Toast.LENGTH_LONG).show();
                    }
                }
            } else{
                if(dialog.isShowing()){
                    dialog.dismiss();
                }
                Toast.makeText(FeedbackActivity.this, "Field cannot be empty", Toast.LENGTH_LONG).show();
                binding.feedbackText.setError("Required");
            }
        });

    }

    private boolean validateField() {
        if(binding.feedbackText.getText().toString().isEmpty()){
            return false;
        }
        return true;
    }
}