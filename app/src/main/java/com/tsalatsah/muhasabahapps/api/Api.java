package com.tsalatsah.muhasabahapps.api;

import com.loopj.android.http.AsyncHttpClient;
import com.tsalatsah.muhasabahapps.BuildConfig;

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
}
