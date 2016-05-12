package com.tsalatsah.muhasabahapps.activities.flashscreen;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.tsalatsah.muhasabahapps.activities.main.MainActivity;
import com.tsalatsah.muhasabahapps.R;
import com.tsalatsah.muhasabahapps.authentication.Authenticator;

import java.util.Arrays;


/*
* This activity is the first activity to launch
* And its purpose is to check whether the user has logged in to the application or not.
* If not, then, it will trigger the authenticator service
* to display a login screen
*
* If the user has already been logged in. This activity will open MainActivity
* and every one be happy!
* */
public class FlashScreen extends Activity implements Runnable
{

    final String TAG = FlashScreen.class.getSimpleName();
    private AccountManager accountManager;
    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_screen);

        accountManager = AccountManager.get(getApplicationContext());

        thread = new Thread(this);
        thread.start();
    }

    private void checkTheLogin()
    {
        final Account accounts[] = accountManager.getAccountsByType(Authenticator.ACCOUNT_TYPE);


        Log.d(TAG, "accounts -> " + Arrays.toString(accounts));
        if (accounts.length < 1) {
            // if the account is 0, then, add a new one
            Log.d(TAG, "The account is zero");
            accountManager.addAccount(Authenticator.ACCOUNT_TYPE,
                    Authenticator.ACCOUNT_AUTH_TOKEN_TYPE, null, null, this,
                    new AccountManagerCallback<Bundle>() {
                        @Override
                        public void run(AccountManagerFuture<Bundle> future) {
                            // after the process done. Then, redirect to MainActivity
                            openMainActivity();
                        }
                    }, null);
        }
        else {
            // just open the open main activity
            openMainActivity();
        }
    }

    private void openMainActivity()
    {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }


    @Override
    public void run() {
        Log.d(TAG, "The thread is launched");
        try {
            thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "The thread after pausing");
        checkTheLogin();
    }
}
