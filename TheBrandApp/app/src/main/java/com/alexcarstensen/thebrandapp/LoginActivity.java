package com.alexcarstensen.thebrandapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity
{
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    private EditText _emailField;
    private EditText _passwordField;
    private Button _loginButton;
    private TextView _signupText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
        Log.d(TAG, "validating login");

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
            Log.d(TAG, "email field was incorrect");
        }

        // should it be between certain length and or special chars??
        if (password.isEmpty())
        {
            _passwordField.setError("Please enter a password");
            validLogin = false;
            Log.d(TAG, "password field was empty");
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

        ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Authenticating with server...");
        progressDialog.show();

        String email = _emailField.getText().toString();
        String password = _passwordField.getText().toString();

        // TODO authenticate with server and login
        UserItem user = new UserItem(null, "", email, password);

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
            }
        }
    }


}
