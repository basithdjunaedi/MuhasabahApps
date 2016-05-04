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
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.tsalatsah.muhasabahapps.R;
import com.tsalatsah.muhasabahapps.api.UserApi;
import com.tsalatsah.muhasabahapps.authentication.Authenticator;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;

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

    private UserApi userApi;

    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext = context;

        Log.d(TAG, "onAttach");
        Log.d(TAG, "mContext -> " + mContext);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();

        Log.d(TAG, "onStart");
        Log.d(TAG, "mContext -> " + mContext);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_authenticator, container, false);
        mContext = layout.getContext();


        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.google_client_id))
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        btnSignIn = (SignInButton) layout.findViewById(R.id.sign_in_button);
        btnSignIn.setOnClickListener(this);

        mAccountManager = AccountManager.get(mContext);
        userApi = new UserApi(mContext);

        mGoogleApiClient.connect();
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
            final GoogleSignInAccount account = result.getSignInAccount();
            Toast
                    .makeText(getActivity().getApplicationContext(), "Welcome " + account.getDisplayName(), Toast.LENGTH_SHORT)
                    .show();

            Log.d(TAG, "name -> " + account.getDisplayName());
            Log.d(TAG, "email -> " + account.getEmail());
            Log.d(TAG, "id -> " + account.getId());
            Log.d(TAG, "token -> " + account.getIdToken());
            Log.d(TAG, "avatar -> " + account.getPhotoUrl());

            /**
             * this should be where the real auth triggered
             * */
        }
        else {
            result.getStatus();
            Log.d(TAG, "login gagal");
        }

        // but, because it is still in development mode. I place the code here

        final Account user = new Account("Sample User 1", Authenticator.ACCOUNT_TYPE);
        final Bundle userData = new Bundle();
        userData.putString(Authenticator.KEY_NAME, "Sample User 1");
        userData.putString(Authenticator.KEY_EMAIL, "sampleuser@gmail.com");
        userData.putString(Authenticator.KEY_AVATAR, "http://uc48.net/europeana/images/fpo_avatar.png");
        // call the api to verify the data
        RequestParams params = new RequestParams();
        params.add("google-id-token", "this should be your google id token");
        userApi.authenticate(params, new JsonHttpResponseHandler(){
            @Override
            public void onPreProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {
                Log.d(TAG, "right before requesting to server");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject userResponse = response.getJSONObject("user");
                    String jwt = response.getString("token");

                    mAccountManager.addAccountExplicitly(user, null, userData);
                    mAccountManager.setAuthToken(user, Authenticator.ACCOUNT_AUTH_TOKEN_TYPE, jwt);
                    Log.d(TAG, "response -> " + response.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "response -> "+response.toString());
            }
        });

        AuthenticatorActivity mActivity = (AuthenticatorActivity) getActivity();
        if (mActivity != null) {
            mActivity.doneLogin();
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
