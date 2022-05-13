package com.myinnovation.mbrowser.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.myinnovation.mbrowser.Models.UserModel;
import com.myinnovation.mbrowser.R;

import soup.neumorphism.NeumorphCardView;

public class SignInActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseDatabase mBase;
    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;
    CallbackManager callbackManager;

    EditText _username, _email, _password, _cPassword;
    Button _signIn, _googleSignIn, _facebookSignIn, _switchText;
    NeumorphCardView _userNameCardView, _emailCardView, _passwordCardView, _confirmPasswordCardView;
    ProgressBar _progressBar;

    String username, email, password, cpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        InitializeFields();

        _signIn.setOnClickListener(view -> {
            if (_signIn.getText() == getResources().getString(R.string.sign_up)) {
                validateFieldsForSignUp();
            } else {
                validateFieldsForLogin();
            }

        });

        _googleSignIn.setOnClickListener(view -> {
            SignInUsingGoogle();
        });

        _facebookSignIn.setOnClickListener(view -> {
            Toast.makeText(SignInActivity.this, "Not implemented yet!", Toast.LENGTH_LONG).show();
        });

        _switchText.setOnClickListener(view -> {
            if (_switchText.getText() == getResources().getString(R.string.loginText)) {
                _userNameCardView.setVisibility(View.INVISIBLE);
                _confirmPasswordCardView.setVisibility(View.INVISIBLE);
                _signIn.setText(getResources().getString(R.string.login));
                _switchText.setText(getResources().getString(R.string.signInText));
            } else {
                _userNameCardView.setVisibility(View.VISIBLE);
                _confirmPasswordCardView.setVisibility(View.VISIBLE);
                _signIn.setText(getResources().getString(R.string.sign_up));
                _googleSignIn.setVisibility(View.VISIBLE);
                _facebookSignIn.setVisibility(View.VISIBLE);
                _switchText.setText(getResources().getString(R.string.loginText));
            }
        });
    }

    private void SignInUsingGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 101);
    }

    private void validateFieldsForLogin() {
        email = _email.getText().toString();
        password = _password.getText().toString();

        if (TextUtils.isEmpty(email)) {
            _email.setError("Required");
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Check your email again...", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            _password.setError("Required");
            return;
        } else if (password.length() < 8) {
            Toast.makeText(this, "Password Length should be 8 or greater than 8.", Toast.LENGTH_LONG).show();
            return;
        }

        LoginUsingEmailPassword();
    }

    private void LoginUsingEmailPassword() {
        _progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    if (mAuth.getCurrentUser().isEmailVerified()) {
                        _username.setText("");
                        _email.setText("");
                        _password.setText("");
                        _cPassword.setText("");
                        _progressBar.setVisibility(View.INVISIBLE);
                        NavigationToNextActivity();
                    } else {
                        _username.setText("");
                        _email.setText("");
                        _password.setText("");
                        _cPassword.setText("");
                        _progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(SignInActivity.this, "Email is not verified yet please verify...", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(e -> {
                    _username.setText("");
                    _email.setText("");
                    _password.setText("");
                    _cPassword.setText("");
                    _progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(SignInActivity.this, "Login Failed try again...", Toast.LENGTH_LONG).show();
                });
    }

    private void validateFieldsForSignUp() {
        username = _username.getText().toString();
        email = _email.getText().toString();
        password = _password.getText().toString();
        cpassword = _cPassword.getText().toString();

        if (TextUtils.isEmpty(username)) {
            _username.setError("Required");
            return;
        }

        if (TextUtils.isEmpty(email)) {
            _email.setError("Required");
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Check your email again...", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            _password.setError("Required");
            return;
        } else if (password.length() < 8) {
            Toast.makeText(this, "Password Length should be 8 or greater than 8.", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(cpassword)) {
            _cPassword.setError("Required");
            return;
        } else if (cpassword.length() < 8) {
            Toast.makeText(this, "Password Length should be 8 or greater than 8.", Toast.LENGTH_LONG).show();
            return;
        } else if (!cpassword.equals(password)) {
            Toast.makeText(this, "Password not matched", Toast.LENGTH_LONG).show();
            return;
        }

        registerUsingEmailPassword();
    }

    private void registerUsingEmailPassword() {
        _progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {

                    UserModel user = new UserModel(username, email);
                    mBase.getReference()
                            .child("Users")
                            .child(mAuth.getUid())
                            .setValue(user)
                            .addOnSuccessListener(unused -> {
                                mAuth.getCurrentUser().sendEmailVerification()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                _progressBar.setVisibility(View.INVISIBLE);
                                                _username.setText("");
                                                _email.setText("");
                                                _password.setText("");
                                                _cPassword.setText("");
                                                _progressBar.setVisibility(View.INVISIBLE);
                                                Toast.makeText(SignInActivity.this, "Verification email has been sent please verify your email...", Toast.LENGTH_LONG).show();
                                            }
                                        })
                                        .addOnFailureListener(e -> {
                                            _progressBar.setVisibility(View.INVISIBLE);
                                            Toast.makeText(SignInActivity.this, "Not able to send verification email", Toast.LENGTH_LONG).show();
                                        });
                                })
                            .addOnFailureListener(e -> {
                                _progressBar.setVisibility(View.INVISIBLE);
                                _username.setText("");
                                _email.setText("");
                                _password.setText("");
                                _cPassword.setText("");
                                Toast.makeText(SignInActivity.this, "Enable to save your data", Toast.LENGTH_LONG).show();
                            });
                })
                .addOnFailureListener(e -> {
                    _username.setText("");
                    _email.setText("");
                    _password.setText("");
                    _cPassword.setText("");
                    _progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(SignInActivity.this, "Enable to create new account", Toast.LENGTH_LONG).show();
                });
    }

    private void InitializeFields() {
        mAuth = FirebaseAuth.getInstance();
        mBase = FirebaseDatabase.getInstance();
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        
        _username = findViewById(R.id.username);
        _email = findViewById(R.id.email);
        _password = findViewById(R.id.password);
        _cPassword = findViewById(R.id.cPassword);
        _signIn = findViewById(R.id.signInButton);
        _googleSignIn = findViewById(R.id.signInGoogleBtn);
        _facebookSignIn = findViewById(R.id.signInFacebookBtn);
        _progressBar = findViewById(R.id.bar);
        _switchText = findViewById(R.id.neuMorphSwitchText);
        _userNameCardView = findViewById(R.id.usernameCardView);
        _emailCardView = findViewById(R.id.emailCardView);
        _passwordCardView = findViewById(R.id.passwordCardView);
        _confirmPasswordCardView = findViewById(R.id.cPasswordCardView);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 101){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                task.getResult(ApiException.class);
                NavigationToNextActivity();
            } catch (ApiException e){
                Toast.makeText(this, "Something went wrong" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void NavigationToNextActivity(){
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

}