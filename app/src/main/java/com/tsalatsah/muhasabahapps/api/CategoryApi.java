package com.tsalatsah.muhasabahapps.api;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tsalatsah.muhasabahapps.authentication.Authenticator;

import java.io.IOException;

/**
 * Created by ibnujakaria on 19/04/16.
 */
public class CategoryApi extends Api{

    private Context context;
    private final String TAG = CategoryApi.class.getSimpleName();

    public CategoryApi(Context context)
    {
        this.context = context;
    }

    public void get(final AsyncHttpResponseHandler handler)
    {
        // get the token from AccontManager
        AccountManager accountManager = AccountManager.get(context);

        accountManager.getAuthTokenByFeatures(Authenticator.ACCOUNT_TYPE,
                Authenticator.ACCOUNT_AUTH_TOKEN_TYPE, null, null, null, null,
                new AccountManagerCallback<Bundle>() {
                    @Override
                    public void run(AccountManagerFuture<Bundle> future) {
                        String token = null;
                        try {
                            token = future.getResult().getString("authtoken");
                        } catch (OperationCanceledException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (AuthenticatorException e) {
                            e.printStackTrace();
                        };

                        if (token != null) {
                            RequestParams params = new RequestParams();
                            params.add("token", token);

                            client.get(CATEGORY_GET_URL, params, handler);
                        }
                    }
                }, null);
    }

    public void newCategory(final String name, final String type, final AsyncHttpResponseHandler handler)
    {
        // get the token from AccontManager
        AccountManager accountManager = AccountManager.get(context);

        accountManager.getAuthTokenByFeatures(Authenticator.ACCOUNT_TYPE,
                Authenticator.ACCOUNT_AUTH_TOKEN_TYPE, null, null, null, null,
                new AccountManagerCallback<Bundle>() {
                    @Override
                    public void run(AccountManagerFuture<Bundle> future) {
                        String token = null;
                        try {
                            token = future.getResult().getString("authtoken");
                        } catch (OperationCanceledException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (AuthenticatorException e) {
                            e.printStackTrace();
                        };

                        if (token != null) {
                            RequestParams params = new RequestParams();
                            params.put("token", token);
                            params.put("name", name);
                            params.put("type", type);

                            client.post(Api.CATEGORY_NEW_URL + "?" + params.toString(), null, handler);
                        }
                    }
                }, null);
    }

    public void getCategoryDetail(final int id, final AsyncHttpResponseHandler handler) {
        // get the token from AccontManager
        AccountManager accountManager = AccountManager.get(context);

        accountManager.getAuthTokenByFeatures(Authenticator.ACCOUNT_TYPE,
                Authenticator.ACCOUNT_AUTH_TOKEN_TYPE, null, null, null, null,
                new AccountManagerCallback<Bundle>() {
                    @Override
                    public void run(AccountManagerFuture<Bundle> future) {
                        String token = null;
                        try {
                            token = future.getResult().getString("authtoken");
                        } catch (OperationCanceledException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (AuthenticatorException e) {
                            e.printStackTrace();
                        };

                        if (token != null) {
                            RequestParams params = new RequestParams();
                            params.put("token", token);

                            client.get(Api.CATEGORY_SHOW_URL+id, params, handler);
                        }
                    }
                }, null);
    }
}
