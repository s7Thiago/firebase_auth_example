package br.thiagosousa.mauthexample.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import br.thiagosousa.mauthexample.HomeActivity;
import br.thiagosousa.mauthexample.R;

import static br.thiagosousa.mauthexample.R.id.TextViewFirebaseCurrentUser;
import static br.thiagosousa.mauthexample.R.id.create_new_account_button;
import static br.thiagosousa.mauthexample.R.id.main_email_field;
import static br.thiagosousa.mauthexample.R.id.main_password_field;
import static br.thiagosousa.mauthexample.R.id.sign_in_button;
import static br.thiagosousa.mauthexample.R.id.sign_out_button;
import static br.thiagosousa.mauthexample.R.id.testTextView;

public class EmailPasswordActivity extends AuthActivity implements View.OnClickListener {

    private static final String TAG = "EmailPasswordActivity ";
    private TextView testTextview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        showProgressBar(true);
        initViews();

        Log.i(TAG, "onCreate of EmailPasswordActivity is called");

        //        inicializando o Firebase
        mAuth = FirebaseAuth.getInstance();

//        Setting up onClick of auth buttons
        mSignInButton.setOnClickListener(this);
        mSignOutButton.setOnClickListener(this);
        mCreateNewAccountButton.setOnClickListener(this);
    }

    /*@Override
    public void onStart() {
        super.onStart();

        String userEmail = user.getEmail();

        Log.i(TAG, "onStart: current user captured: user " +  userEmail);
        updateUI(user);
    }*/

    public void initViews() {
        mCurrentUserTxt = findViewById(TextViewFirebaseCurrentUser);
        testTextview = findViewById(testTextView);
        mSignInButton = findViewById(sign_in_button);
        mCreateNewAccountButton = findViewById(create_new_account_button);
        mSignOutButton = findViewById(sign_out_button);
        mEmailField = findViewById(main_email_field);
        mPasswordField = findViewById(main_password_field);

    }

    @Override
    public void onClick(View view) {

        Intent intent;
        Context context = getApplicationContext();
        user = mAuth.getCurrentUser();

        int id = view.getId();
        switch (id) {
            case sign_in_button:
                Log.i(TAG, "Sign in is called");

                String email = "";
                try {
                    email = user.getEmail();
                    Log.w(TAG, "sign in button is called. Email extracted of current user" + email);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                try {
                    signIn(mEmailField.getText().toString(), mPasswordField.getText().toString(), view);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Log.w(TAG, "getEmail in View.OnclickListener is a null object reference");
                }
                showToast("signed In");

//                open the home activity sending current user email
                intent = new Intent(context, HomeActivity.class);
                intent.putExtra("user_email", email);
                break;
            case sign_out_button:
                Log.i(TAG, "Sign out is called");
                showToast("Sign out");
                signOut();
                break;
            case create_new_account_button:
                Log.i(TAG, "Create new account is called");
                createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
                showToast("Create new accont called");
                break;
        }
    }

    public void testEvent(View view) {

        try {
            testTextview.setText("User logged in: " + mAuth.getCurrentUser().getEmail());
            showToast("user connected?:" + userIsLogged());
        } catch (IllegalStateException e) {
            StackTraceElement[] trace = e.getStackTrace();
            Log.e(TAG, "IllegalStateException" + trace);
            testTextview.setText("User logged in: IllegalStateException");
        } catch (NullPointerException e) {
            Log.e(TAG, "NullPointerException" + e.getStackTrace());
            testTextview.setText("User logged in: null object");
        }
    }
}