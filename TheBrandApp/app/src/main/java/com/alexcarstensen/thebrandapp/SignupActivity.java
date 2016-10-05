package com.alexcarstensen.thebrandapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity
{
    private static final String TAG = "SignupActivity";


    private EditText _emailField;
    private EditText _passwordField;
    private EditText _userNameField;
    private Button _signupButton;
    private TextView _loginText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        findViews();

        _signupButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                createAccount();
            }
        });

        _loginText.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }

    private void findViews()
    {
        Log.d(TAG, "Finding views");

        _emailField = (EditText) findViewById(R.id.signupEmailEditText);
        _passwordField = (EditText) findViewById(R.id.signupPasswordEditText);
        _userNameField = (EditText) findViewById(R.id.signupUserNameEditText);
        _signupButton = (Button) findViewById(R.id.signupCreateAccoutButton);
        _loginText = (TextView) findViewById(R.id.signupAlreadyMemberTextView);
    }

    private void createAccount()
    {
        Log.d(TAG, "Creating account");

        if (!validateCreateAccount())
        {
            onFailedCreateLogin();
            return;
        }

        authenticateAccountCreationWithServer();

    }


    private boolean validateCreateAccount()
    {
        Log.d(TAG, "Validating account creation");


        Boolean validLogin = true;

        String email = _emailField.getText().toString();
        String password = _passwordField.getText().toString();
        String userName = _userNameField.getText().toString();
        validLogin = checkValidEmailPasswordAndUsername(validLogin, email, password, userName);

        return validLogin;
    }

    private Boolean checkValidEmailPasswordAndUsername(Boolean validLogin, String email, String password, String userName)
    {
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
            Log.d(TAG, "pPassword field was empty");
        }
        if (userName.isEmpty())
        {
            _userNameField.setError("Please enter a username");
            validLogin = false;
            Log.d(TAG, "Username field was empty");
        }
        return validLogin;
    }

    private void onFailedCreateLogin()
    {
        Log.d(TAG, "Failed to create account");

        Toast.makeText(this, "Create account failed", Toast.LENGTH_LONG).show();
    }

    private void authenticateAccountCreationWithServer()
    {
        Log.d(TAG, "Authenticating with server");

        ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setMessage("Creating account...");
        progressDialog.show();

        String email = _emailField.getText().toString();
        String password = _passwordField.getText().toString();
        String userName = _userNameField.getText().toString();

        // TODO authenticate with server and Create account
        UserItem user = new UserItem(null, userName, email, password);
    }




}
