package com.tsalatsah.muhasabahapps.api;

import android.content.Context;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by ibnujakaria on 19/04/16.
 */
public class UserApi extends Api {

    public UserApi(Context context)
    {
        super(context);
    }

    public void authenticate(RequestParams params, AsyncHttpResponseHandler handler)
    {
        client.post(Api.LOGIN_URL, params, handler);
    }
}
