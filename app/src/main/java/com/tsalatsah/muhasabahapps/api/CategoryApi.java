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
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tsalatsah.muhasabahapps.authentication.Authenticator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

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

    public void newCategory(final JSONObject dataPost, final AsyncHttpResponseHandler handler)
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
                            try {
                                // aku wes bingung. Iki gak work kabeh

                                List <Map<String, String>> records = new ArrayList<Map<String, String>>();

                                for (int i = 0; i < dataPost.getJSONArray("records").length(); i++) {
                                    JSONArray jsonArray = dataPost.getJSONArray("records");
                                    JSONObject record = new JSONObject(jsonArray.getString(i));
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("name", record.getString("name"));
                                    map.put("type", record.getString("type"));
                                    records.add(map);
                                }

                                RequestParams params = new RequestParams();
                                params.put("records", records);
                                params.put("name", dataPost.getString("name"));

                                Log.d(TAG, "params encoded -> " + URLEncoder.encode(params.toString(), "UTF-8"));
                                Log.d(TAG, "params decoded -> " + params.toString());
                                client.post(Api.CATEGORY_NEW_URL + "?token=" + token, params, handler);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
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

    public void deteleCategory(final int categoryId, final AsyncHttpResponseHandler handler) {
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
                            params.put("_method", "delete");

                            client.post(Api.CATEGORY_SHOW_URL+categoryId + "?token="+token, params, handler);
                        }
                    }
                }, null);
    }
}
