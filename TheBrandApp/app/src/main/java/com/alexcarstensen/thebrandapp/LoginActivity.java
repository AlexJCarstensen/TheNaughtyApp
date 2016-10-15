package com.alexcarstensen.thebrandapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity
{
    private static final int REQUEST_SIGNUP = 0;
    private static final int OVERLAY_PERMISSION = 0;
    public static final String TAG = "LoginActivity";
    public static final String SEND_EMAIL = "SendEmail";
    private EditText _emailField;
    private EditText _passwordField;
    private Button _loginButton;
    private TextView _signupText;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SetupFireBase();

        findViews();


        _loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                login();
            }
        });

        _signupText.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });


    }

    //Below code is taken from firebase documentation
    private void SetupFireBase()
    {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    @Override
    public void onStart()
    {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void findViews()
    {
        Log.d(TAG, "Finding views");
        _emailField = (EditText) findViewById(R.id.emailInputEditText);
        _passwordField = (EditText) findViewById(R.id.passwordInputEditText);
        _loginButton = (Button) findViewById(R.id.loginBtn);
        _signupText = (TextView) findViewById(R.id.createAccountTextView);
    }

    private void login()
    {
        Log.d(TAG, "Logging in");

        if (!validateLogin())
        {
            onFailedLogin();
            return;
        }

        authenticateLoginWithServer();

    }

    private boolean validateLogin()
    {
        Log.d(TAG, "Validating login");

        Boolean validLogin = true;

        String email = _emailField.getText().toString();
        String password = _passwordField.getText().toString();
        validLogin = checkValidEmailAndPassword(validLogin, email, password);


        return validLogin;
    }

    private Boolean checkValidEmailAndPassword(Boolean validLogin, String email, String password)
    {
        //check for email address? or does it do it
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            _emailField.setError("Enter a valid email address");
            validLogin = false;
            Log.d(TAG, "Email field was incorrect");
        }

        // should it be between certain length and or special chars??
        if (password.isEmpty())
        {
            _passwordField.setError("Please enter a password");
            validLogin = false;
            Log.d(TAG, "Password field was empty");
        }
        return validLogin;
    }

    private void onFailedLogin()
    {
        Log.d(TAG, "Failed to login");

        Toast.makeText(this, "Login failed", Toast.LENGTH_LONG).show();
    }

    private void authenticateLoginWithServer()
    {
        Log.d(TAG, "Authenticating with server");

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Authenticating with server...");
        progressDialog.show();

        final String email = _emailField.getText().toString();
        String password = _passwordField.getText().toString();

        // TODO authenticate with server and login


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            progressDialog.dismiss();
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(LoginActivity.this, "Authenticastion failed... try again",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else{
                            progressDialog.dismiss();
                            //Todo check if you want anything sent to the mainactivity
                            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                            mainIntent.putExtra(SEND_EMAIL, email);
                            startActivity(mainIntent);
                            finish();
                        }
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SIGNUP)
        {
            if (resultCode == RESULT_OK)
            {
                // TODO Login automatically or return used data and put them in fields??
                String email = data.getStringExtra(getResources().getString(R.string.emailHint));
                String password = data.getStringExtra(getResources().getString(R.string.passwordHint));

                _emailField.setText(email);
                _passwordField.setText(password);
            }
        }

        
    }


}
