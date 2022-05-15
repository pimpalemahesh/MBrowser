package com.myinnovation.mbrowser.Activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.text.method.SingleLineTransformationMethod;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.myinnovation.mbrowser.Models.UserModel;
import com.myinnovation.mbrowser.R;

import java.util.Objects;

import soup.neumorphism.NeumorphCardView;
import soup.neumorphism.NeumorphImageView;

public class SignInActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseDatabase mBase;
    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;
    CallbackManager callbackManager;

    EditText _username, _email, _password, _cPassword;
    NeumorphImageView _eyePassword, _eyeCPassword;
    Button _signIn, _googleSignIn, _facebookSignIn, _switchText, _forgotPassword;
    NeumorphCardView _userNameCardView, _emailCardView, _passwordCardView, _confirmPasswordCardView;
    ProgressBar _progressBar;

    String username, email, password, cpassword;
    private boolean showPass = false;

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

        _eyePassword.setOnClickListener(view -> {
            showPass = !showPass;
            showPassword(showPass, _password, _eyePassword);
        });

        _eyeCPassword.setOnClickListener(view -> {
            showPass = !showPass;
            showPassword(showPass, _cPassword, _eyeCPassword);
        });


        _googleSignIn.setOnClickListener(view -> SignInUsingGoogle());

        _facebookSignIn.setOnClickListener(view -> Toast.makeText(SignInActivity.this, "Not implemented yet!", Toast.LENGTH_LONG).show());

        _switchText.setOnClickListener(view -> {
            if (_switchText.getText() == getResources().getString(R.string.loginText)) {
                _userNameCardView.setVisibility(View.INVISIBLE);
                _confirmPasswordCardView.setVisibility(View.INVISIBLE);
                _eyeCPassword.setVisibility(View.INVISIBLE);
                _forgotPassword.setVisibility(View.VISIBLE);
                _signIn.setText(getResources().getString(R.string.login));
                _switchText.setText(getResources().getString(R.string.signInText));
                _username.requestFocus();
            } else {
                _userNameCardView.setVisibility(View.VISIBLE);
                _confirmPasswordCardView.setVisibility(View.VISIBLE);
                _signIn.setText(getResources().getString(R.string.sign_up));
                _forgotPassword.setVisibility(View.INVISIBLE);
                _googleSignIn.setVisibility(View.VISIBLE);
                _facebookSignIn.setVisibility(View.VISIBLE);
                _eyeCPassword.setVisibility(View.VISIBLE);
                _switchText.setText(getResources().getString(R.string.loginText));
                _email.requestFocus();
            }
        });
    }

    private void showPassword(boolean showPass, EditText pWord, NeumorphImageView eyeImage) {
        if(showPass){
            pWord.setTransformationMethod(new PasswordTransformationMethod());
            pWord.setSelection(pWord.getText().length());
            eyeImage.setImageResource(R.drawable.ic_hidden_eye);
        } else{
            pWord.setTransformationMethod(new SingleLineTransformationMethod());
            pWord.setSelection(pWord.getText().length());
            eyeImage.setImageResource(R.drawable.ic_view_eye);
        }
    }

    private void SignInUsingGoogle() {
        _progressBar.setVisibility(View.VISIBLE);
        Intent signInIntent = googleSignInClient.getSignInIntent();
        someActivityResultLauncher.launch(signInIntent);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // There are no request codes
                    Intent data = result.getData();
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

                    try {
                        task.getResult(ApiException.class);
                        NavigationToNextActivity();
                    } catch (ApiException e){
                        _progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(SignInActivity.this, "Something went wrong" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });

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
                    if (Objects.requireNonNull(mAuth.getCurrentUser()).isEmailVerified()) {
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
                            .child(Objects.requireNonNull(mAuth.getUid()))
                            .setValue(user)
                            .addOnSuccessListener(unused -> Objects.requireNonNull(mAuth.getCurrentUser()).sendEmailVerification()
                                    .addOnSuccessListener(unused1 -> {
                                        _progressBar.setVisibility(View.INVISIBLE);
                                        _username.setText("");
                                        _email.setText("");
                                        _password.setText("");
                                        _cPassword.setText("");
                                        _progressBar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(SignInActivity.this, "Verification email has been sent please verify your email...", Toast.LENGTH_LONG).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        _progressBar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(SignInActivity.this, "Not able to send verification email", Toast.LENGTH_LONG).show();
                                    }))
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
        _eyePassword = findViewById(R.id.eyePassword);
        _eyeCPassword = findViewById(R.id.eyeCPassword);
        _signIn = findViewById(R.id.signInButton);
        _googleSignIn = findViewById(R.id.signInGoogleBtn);
        _facebookSignIn = findViewById(R.id.signInFacebookBtn);
        _forgotPassword = findViewById(R.id.forgotPassword);
        _progressBar = findViewById(R.id.bar);
        _switchText = findViewById(R.id.neuMorphSwitchText);
        _userNameCardView = findViewById(R.id.usernameCardView);
        _emailCardView = findViewById(R.id.emailCardView);
        _passwordCardView = findViewById(R.id.passwordCardView);
        _confirmPasswordCardView = findViewById(R.id.cPasswordCardView);


    }

    private void    NavigationToNextActivity(){
        _progressBar.setVisibility(View.INVISIBLE);
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

}