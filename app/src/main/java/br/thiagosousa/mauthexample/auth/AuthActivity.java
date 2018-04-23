package br.thiagosousa.mauthexample.auth;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import br.thiagosousa.mauthexample.R;
import br.thiagosousa.mauthexample.useful.UtilActivity;

/**
 * Created by Thiago on 12/02/2018.
 */

@SuppressLint("Registered")
public class AuthActivity extends UtilActivity {

    private static final String TAG = "FirebaseAuthExampleTest";
    protected FirebaseAuth mAuth;
    protected FirebaseUser user;
    protected TextView mCurrentUserTxt;
    protected TextView mStatusTextView;
    protected EditText mEmailField;
    protected EditText mPasswordField;
    protected Button mSignInButton;
    protected Button mSignOutButton;
    protected Button mCreateNewAccountButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        Log.i(TAG, "onCreate of AuthActivity is called");

        //        inicializando o Firebase
//        mAuth = FirebaseAuth.getInstance();
    }

    //    [Start]: onStart method
    @Override
    public void onStart() {
        super.onStart();

//        String userEmail = user.getEmail();
//
//        Log.i(TAG, "onStart: current user captured: user " +  userEmail);
//        updateUI(user);
    }
    //    [End]: onStart method


    public void createAccount(String email, String password) {
        Log.i(TAG, "create new account: " + email);

        if (!validateForm()) {
            return;
        }

        showProgressDialog(true);

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail: success! :D");
//                            FirebaseUser user = mAuth.getCurrentUser();

                            updateUI(user);
                        } else {

                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                showToast("Weak password exeption");
                                mPasswordField.setError("Weak password");
                                mPasswordField.requestFocus();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                showToast("Invalid email exeption");
                                mEmailField.setError("Invalid email");
                                mEmailField.requestFocus();
                            } catch (FirebaseAuthUserCollisionException e) {
                                showToast("User exists!");
                                mEmailField.setError("User exists");
                                mEmailField.requestFocus();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail: failure :(", task.getException());
                            showToast("Authentication failure :(");

                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        showProgressDialog(false);
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }

    //    [Start]: Sign in method
    public void signIn(String email, String password, final View view) {
        Log.d(TAG, "opening account " + email);

        if (!validateForm()) {
            return;
        }

        showProgressDialog(true);

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmailAndPAssword: Sucess! :D");
//                    FirebaseUser firebaseUser = mAuth.getCurrentUser(); /*anteriormente usado na próxima linha*/
                            user = mAuth.getCurrentUser();
                            updateUI(user);

                            hideKeyboard(view);

                        } else {

                            //                    Exeptions for auth
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                mPasswordField.setError("Weak password");
                                mPasswordField.requestFocus();
                                showToast("Weak password exeption");
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                mEmailField.setError("Invalid email");
                                mEmailField.requestFocus();
                                showToast("Invalid Email Exeption!");
                            } catch (FirebaseAuthInvalidUserException e) {
                                mEmailField.setError("User invalid");
                                mEmailField.requestFocus();
                                showToast("User is invalid!");
                            } catch (FirebaseAuthException e) {
                                mEmailField.setError("User invalid");
                                mEmailField.requestFocus();
                                showToast("User is invalid!");
                            } catch (NullPointerException e) {
                                mEmailField.setError("NullPoint!!!");
                                mEmailField.requestFocus();
                                showToast("User is invalid!");
                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage());
                            }

                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmailAndPAssword: Failure! :(", task.getException());
                            showToast(getString(R.string.auth_failed));
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            mStatusTextView.setText(R.string.auth_failed);
                        }
                        showProgressDialog(false);
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }
//    [End]: Sign in method

    //    [Start]: Sign out method
    public void signOut() {
        mAuth.signOut();
        updateUI(null);
    }
//    [End]: Sign out method

    //    [Start]: Validate form method
    private boolean validateForm() {

        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();
        boolean valid = true;

//        Validating email field
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required!");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

//        Validating password
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }
//    [End]: Validate form method

    //    [Start]: updateUI method
    public void updateUI(FirebaseUser user) {
        showProgressDialog(false);

        String email = null;
        String userID = null;

        try {
            email = user.getEmail();
            userID = user.getUid();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (user != null) {
            mCurrentUserTxt.setText("user ID: " + userID);

            try {
                showToast(email);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        } else {
            Log.w(TAG, "user is null : updateUI method else stretch");
            try {
                mCurrentUserTxt.setText("user signed out");
                showToast("User " + getString(R.string.signed_out));
            } catch (NullPointerException e) {
                e.printStackTrace();
                showToast("NullPointException!!");
            }
        }
    }
//    [End]: updateUI method

    //    [Start]: isUserLogged method
    public boolean isUserLogged() {
        boolean isUserLogged = true;
        boolean confirmation = false;

        try {
            confirmation = mAuth.getInstance().getCurrentUser() == null;
        } catch (NullPointerException e) {
            Log.e(TAG, "isUserLogged try block with NullPointException");
            e.printStackTrace();
        }

        if (confirmation) {
//            is user not logged in
            Log.w(TAG, "isUserLogged: user not logged in");
            isUserLogged = false;
        } else {
//            user logged in
            Log.w(TAG, "isUserLogged: user logged in");
            isUserLogged = true;
        }
        return isUserLogged;
    }
//    [End]: isUserLogged method

    public boolean userIsLogged() {
        boolean isLogged = false;
        boolean confirm = FirebaseAuth.getInstance().getCurrentUser() == null;

        switch (returnLoggedConfirmation()) {

            case "userIsLogged":
                isLogged = true;
                Log.w(TAG, "isUserLogged: user logged in");
                break;
            default:
                isLogged = false;
                Log.w(TAG, "isUserLogged: user not logged in");
                break;

        }

        return isLogged;
    }

    public String returnLoggedConfirmation() {
        String returnValue = "";
        boolean logged = FirebaseAuth.getInstance().getCurrentUser() != (null);

        Log.w(TAG, "returnLoggedConfirmation: initialized");

        if (logged) {

            returnValue = "userIsLogged";
            Log.w(TAG, "returnLoggedConfirmation: mAuth.getcurrentUser() positive login confirmation!");

        } else {

            returnValue = "notLogged";
            Log.w(TAG, "returnLoggedConfirmation: mAuth.getcurrentUser() received a null object");
        }

        return returnValue;
    }

}
