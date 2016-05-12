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

    public CategoryApi(Context context)
    {
        super(context);
    }

    public void get(final AsyncHttpResponseHandler handler)
    {
        withToken(new Runnable() {
            @Override
            public void run() {
                RequestParams params = new RequestParams();
                params.add("token", token);

                client.get(CATEGORY_GET_URL, params, handler);
            }
        });
    }

    public void newCategory(final JSONObject dataPost, final AsyncHttpResponseHandler handler)
    {
        withToken(new Runnable() {
            @Override
            public void run() {
                RequestParams params = new RequestParams();
                try {
                    // aku wes bingung. Iki gak work kabeh
                    params.put("name", dataPost.getString("name"));
                    List <Map<String, String>> records = new ArrayList<Map<String, String>>();

                    for (int i = 0; i < dataPost.getJSONArray("records").length(); i++) {
                        JSONArray jsonArray = dataPost.getJSONArray("records");
                        JSONObject record = new JSONObject(jsonArray.getString(i));
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("name", record.getString("name"));
                        map.put("type", record.getString("type"));
                        records.add(map);
                    }

                    params.put("records", records);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d(TAG, "params decoded -> " + params.toString());
                client.post(Api.CATEGORY_NEW_URL + "?token=" + token, params, handler);
            }
        });
    }

    public void getCategoryDetail(final int id, final AsyncHttpResponseHandler handler)
    {
        withToken(new Runnable() {
            @Override
            public void run() {
                RequestParams params = new RequestParams();
                params.put("token", token);

                client.get(Api.CATEGORY_SHOW_URL+id, params, handler);
            }
        });
    }

    public void deteleCategory(final int categoryId, final AsyncHttpResponseHandler handler)
    {
        withToken(new Runnable() {
            @Override
            public void run() {
                RequestParams params = new RequestParams();
                params.put("_method", "delete");

                client.post(Api.CATEGORY_SHOW_URL+categoryId + "?token="+token, params, handler);
            }
        });
    }

    public void newSubCategory(final JSONObject dataPost, final AsyncHttpResponseHandler handler) {
        withToken(new Runnable() {
            @Override
            public void run() {
                RequestParams params = new RequestParams();
                try {
                    // aku wes bingung. Iki gak work kabeh
                    params.put("name", dataPost.getString("name"));
                    List <Map<String, String>> records = new ArrayList<Map<String, String>>();

                    for (int i = 0; i < dataPost.getJSONArray("records").length(); i++) {
                        JSONArray jsonArray = dataPost.getJSONArray("records");
                        JSONObject record = new JSONObject(jsonArray.getString(i));
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("name", record.getString("name"));
                        map.put("type", record.getString("type"));
                        records.add(map);
                    }

                    params.put("records", records);
                    Log.d(TAG, "params decoded -> " + params.toString());
                    client.post(Api.getSubCategoryNewURL(dataPost.getInt("categoryId")) + "?token=" + token, params, handler);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getSubCategoryDetail(final int categoryId, final int subCategoryId, final AsyncHttpResponseHandler handler)
    {
        withToken(new Runnable() {
            @Override
            public void run() {
                RequestParams params = new RequestParams();
                params.put("token", token);

                client.get(Api.getSubCategoryDetailURL(categoryId, subCategoryId), params, handler);
            }
        });
    }
}
