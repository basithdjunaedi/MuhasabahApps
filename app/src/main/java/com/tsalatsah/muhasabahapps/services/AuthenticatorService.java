package com.tsalatsah.muhasabahapps.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.tsalatsah.muhasabahapps.authentication.Authenticator;

/**
 * Created by ibnujakaria on 12/04/16.
 */
public class AuthenticatorService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Authenticator authenticator = new Authenticator(this);
        return authenticator.getIBinder();
    }
}
