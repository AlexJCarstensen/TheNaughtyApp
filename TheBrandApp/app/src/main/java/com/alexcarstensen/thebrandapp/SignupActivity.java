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

import com.alexcarstensen.thebrandapp.Helpers.EmailNameHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity
{
    private static final String TAG = "SignupActivity";


    private EditText _emailField;
    private EditText _passwordField;
    private EditText _userNameField;
    private Button _signupButton;
    private TextView _loginText;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;

    //ReturnIntent
    public static String EMAIL_RETURN = "email";
    public static String PASSWORD_RETURN = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        SetupFirebase();

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

    private void SetupFirebase()
    {
        //Below code is taken from firebase documentation
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null)
                {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                }
                else{
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop()
    {
        super.onStop();
        if(mAuthListener != null)
        {
            mAuth.removeAuthStateListener(mAuthListener);
        }
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

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setMessage("Creating account...");
        progressDialog.show();

        final String email = _emailField.getText().toString();
        final String password = _passwordField.getText().toString();
        String userName = _userNameField.getText().toString();

        // TODO authenticate with server and Create account
        final UserItem user = new UserItem(userName,email);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.

                        if(!task.isSuccessful())
                        {
                            progressDialog.dismiss();
                            Toast.makeText(SignupActivity.this, "Creating- and autheticating user failed",
                                    Toast.LENGTH_SHORT).show();



                        }
                        else{
                            progressDialog.dismiss();
                            Toast.makeText(SignupActivity.this, "Creating- and autheticating user succeeded!",
                                    Toast.LENGTH_SHORT).show();

                            //Write new user to database
                            WriteNewUserToDatabase(user);
                            Intent resultIntent = new Intent();

                            resultIntent.putExtra(EMAIL_RETURN, email);
                            resultIntent.putExtra(PASSWORD_RETURN, password);

                            setResult(LoginActivity.REQUEST_SIGNUP, resultIntent);
                            finish();

                        }
                    }
                });
    }

    private void WriteNewUserToDatabase(UserItem user)
    {
        //Creating new entry in database with the name of the email
        mDatabase.child("Users").child( EmailNameHelper.ConvertEmail(user.get_email())).setValue(user);
    }




}
