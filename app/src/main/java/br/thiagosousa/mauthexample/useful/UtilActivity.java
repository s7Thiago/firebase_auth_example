package br.thiagosousa.mauthexample.useful;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import br.thiagosousa.mauthexample.R;

/**
 * Created by Thiago on 11/02/2018.
 */

public class UtilActivity extends AppCompatActivity {

    private static final String TAG = "UtlActivityAuthExample";
    ProgressDialog progressDialog;
    protected ProgressBar mProgressBar;

    public void showProgressDialog(boolean display) {
        if (display) {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage(getString(R.string.loading));
                progressDialog.setIndeterminate(true);
            }
            progressDialog.show();
        } else {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    public void showProgressBar(boolean display) {
        if (!display) {

            Log.w(TAG, "Progressbar is invisible");
            mProgressBar.setVisibility(View.GONE);

        } else {

            Log.w(TAG, "Progressbar is visible");

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        showProgressDialog(false);
    }

    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        Log.i(TAG, "hideKeyboard is called");

    }
}
