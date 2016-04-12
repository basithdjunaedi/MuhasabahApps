package com.tsalatsah.muhasabahapps;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tsalatsah.muhasabahapps.authentication.Authenticator;

public class AuthenticatorActivity extends AccountAuthenticatorActivity implements View.OnClickListener {

    Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticator);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btnLogin:
                AccountManager accountManager = AccountManager.get(getApplicationContext());
                Account account = new Account("Nurul Huda", Authenticator.ACCOUNT_TYPE);

                accountManager.addAccountExplicitly(account, null, null);
                accountManager.setAuthToken(account, Authenticator.ACCOUNT_AUTH_TOKEN_TYPE, "initokenku");

                Bundle result = new Bundle();
                setAccountAuthenticatorResult(result);
                finish();
        }
    }
}
