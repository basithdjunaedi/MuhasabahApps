package com.tsalatsah.muhasabahapps;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.tsalatsah.muhasabahapps.authentication.Authenticator;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener{

    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final String MY_ACCOUNT_TYPE = "com.tsalatsah.muhasabahapps.user";
    private static final String ACCOUNT_TOKEN_TYPE = "jwt";

    GoogleSignInOptions gso;
    GoogleApiClient mGoogleApiClient;
    SignInButton btnSignIn;
    AccountManager mAccountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.google_client_id))
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        btnSignIn = (SignInButton) findViewById(R.id.sign_in_button);
        btnSignIn.setOnClickListener(this);

        mAccountManager = AccountManager.get(getBaseContext());
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast
                .makeText(getApplicationContext(), connectionResult.getErrorMessage(), Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.sign_in_button:
                doSignIn();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // return yang dihasilkan dari intent google sign in
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
//        Log.d(TAG, "handleSignInResult -> " + result.isSuccess());

        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            Toast
                    .makeText(getApplicationContext(), "Welcome " + account.getDisplayName(), Toast.LENGTH_SHORT)
                    .show();

            Log.d(TAG, "name -> " + account.getDisplayName());
            Log.d(TAG, "email -> " + account.getEmail());
            Log.d(TAG, "id -> " + account.getId());
            Log.d(TAG, "token -> " + account.getIdToken());
            Log.d(TAG, "avatar -> " + account.getPhotoUrl());

            Account user = new Account(account.getDisplayName(), MY_ACCOUNT_TYPE);
            mAccountManager.addAccountExplicitly(user, "", null);
        }
        else {
            Log.d(TAG, "login gagal");
        }
    }

    private void doSignIn() {
        Account[] accounts = mAccountManager.getAccountsByType(MY_ACCOUNT_TYPE);
        Account account;

        // get the account
        if (accounts.length > 0) {
            account = accounts[0];
            Log.d(TAG, "account -> " + account.toString());
        }
        else {
            Log.d(TAG, "account -> kosong");
        }

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

}
