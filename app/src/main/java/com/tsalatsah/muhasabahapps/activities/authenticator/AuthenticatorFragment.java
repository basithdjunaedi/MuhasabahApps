package com.tsalatsah.muhasabahapps.activities.authenticator;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.tsalatsah.muhasabahapps.R;
import com.tsalatsah.muhasabahapps.authentication.Authenticator;

/*
* This fragment is used to authenticate users using their google accounts
* */
public class AuthenticatorFragment extends Fragment implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private final String TAG = AuthenticatorFragment.class.getSimpleName();
    private static final int RC_SIGN_IN = 9001;

    private GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;
    private SignInButton btnSignIn;
    private AccountManager mAccountManager;

    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.google_client_id))
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        btnSignIn = (SignInButton) getActivity().findViewById(R.id.sign_in_button);
        btnSignIn.setOnClickListener(this);

        mAccountManager = AccountManager.get(mContext);
    }

    @Override
    public void onStart() {
        super.onStart();

        mGoogleApiClient.connect();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_authenticator, container, false);

        return layout;
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
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            Toast
                    .makeText(getActivity().getApplicationContext(), "Welcome " + account.getDisplayName(), Toast.LENGTH_SHORT)
                    .show();

            Log.d(TAG, "name -> " + account.getDisplayName());
            Log.d(TAG, "email -> " + account.getEmail());
            Log.d(TAG, "id -> " + account.getId());
            Log.d(TAG, "token -> " + account.getIdToken());
            Log.d(TAG, "avatar -> " + account.getPhotoUrl());

            Account user = new Account(account.getDisplayName(), Authenticator.ACCOUNT_TYPE);
//            mAccountManager.addAccountExplicitly(user, "", null);
        }
        else {
            Log.d(TAG, "login gagal");
        }
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
    public void onStop() {
        super.onStop();

        mGoogleApiClient.disconnect();
    }

    private void doSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
