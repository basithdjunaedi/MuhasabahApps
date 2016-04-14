package com.tsalatsah.muhasabahapps.authentication;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.tsalatsah.muhasabahapps.activities.authenticator.AuthenticatorActivity;

/**
 * Created by ibnujakaria on 10/04/16.
 */
public class Authenticator extends AbstractAccountAuthenticator {

    public static final String ACCOUNT_TYPE = "com.muhasabahapps.user";
    public static final String ACCOUNT_AUTH_TOKEN_TYPE = "fullaccess";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_AVATAR = "avatar";
    private final String TAG = Authenticator.class.getSimpleName();
    private final Context mContext;

    public Authenticator(Context context) {
        super(context);

        mContext = context;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        return null;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
        Log.d(TAG, "-- add account");

        Intent intent = new Intent(mContext, AuthenticatorActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);

        Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        Log.d(TAG, "-- get auth token");

        AccountManager mAccountManager = AccountManager.get(mContext);

        String token = mAccountManager.peekAuthToken(account, authTokenType);

        // try to get the token
        if (!TextUtils.isEmpty(token)) {
            Bundle bundle = new Bundle();
            bundle.putString(AccountManager.KEY_AUTHTOKEN, token);
            return bundle;
        }

        // if there is nothing we can do to get the token, prompt the user to perform re-sign-in
        Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, new Intent(mContext, AuthenticatorActivity.class));
        return bundle;
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        return null;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        return null;
    }
}
