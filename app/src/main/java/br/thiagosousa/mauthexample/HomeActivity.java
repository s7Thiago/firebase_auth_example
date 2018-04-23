package br.thiagosousa.mauthexample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import br.thiagosousa.mauthexample.auth.AuthActivity;

public class HomeActivity extends AuthActivity implements View.OnClickListener {

    private static final String TAG = "HomeActivity";
    private TextView welcomeTextView;
    private Button signOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        initViews(true);

//        Entering greeting message
        String message = getString(R.string.homeLoggedWelcomeText);
        welcomeTextView.setText(String.format(message, getCurrentUserEmail()));

        signOutButton.setOnClickListener(this);
    }

    private void initViews(boolean init) {
        if (init) {
            Log.i(TAG, "initViews: is activated");

//            Initializing views
            welcomeTextView = (TextView) findViewById(R.id.home_welcome_text);
//            signOutButton = (Button) findViewById(R.id.sign_out_button);

        } else {
            Log.i(TAG, "initViews: is desactivated");
        }
    }


    public String getCurrentUserEmail() {
        Intent intent = getIntent();
        String email = intent.getStringExtra("user_email");
        return email;
    }

    @Override
    public void onClick(View view) {
        int viewID = view.getId();
        switch (viewID) {
            case R.id.sign_out_button:
                signOut();
                break;
        }
    }
}
