package com.tsalatsah.muhasabahapps.api;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.tsalatsah.muhasabahapps.BuildConfig;
import com.tsalatsah.muhasabahapps.authentication.Authenticator;

import java.io.IOException;

/**
 * Created by ibnujakaria on 16/04/16.
 */
public class Api {
    public static final String BASE_URL = "http://192.168.43.112:8080/api/";
    public static final String LOGIN_URL = BASE_URL + "auth/authenticate";
    public static final String CATEGORY_GET_URL = BASE_URL + "categories";
    public static final String CATEGORY_NEW_URL = BASE_URL + "categories/new";
    public static final String CATEGORY_SHOW_URL = BASE_URL + "categories/";
    protected static AsyncHttpClient client = new AsyncHttpClient();

    protected Context context;
    protected final String TAG = CategoryApi.class.getSimpleName();
    protected String token = null;
    protected   AccountManager accountManager;

    public Api (Context context)
    {
        this.context = context;

        withToken(null);
    }

    public static String getSubCategoryNewURL(int categoryId)
    {
        return BASE_URL + "categories/" + categoryId + "/sub-categories/new";
    }

    public static String getSubCategoryDetailURL(int categoryId, int subCategoryId)
    {
        return BASE_URL + "categories/" + categoryId + "/sub-categories/" + subCategoryId;
    }

    protected void withToken(final Runnable callback)
    {
        if (token == null) {
            accountManager = AccountManager.get(context);
            accountManager.getAuthTokenByFeatures(Authenticator.ACCOUNT_TYPE,
                    Authenticator.ACCOUNT_AUTH_TOKEN_TYPE, null, null, null, null,
                    new AccountManagerCallback<Bundle>() {
                        @Override
                        public void run(AccountManagerFuture<Bundle> future) {
                            try {
                                token = future.getResult().getString("authtoken");
                                if (callback != null)
                                    callback.run();
                            } catch (OperationCanceledException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (AuthenticatorException e) {
                                e.printStackTrace();
                            };

                            Log.d(TAG, "CALLING AUTHMANAGER");
                        }

                    }, null);
        }
        else {
            if (callback != null)
                callback.run();
        }
    }
}
