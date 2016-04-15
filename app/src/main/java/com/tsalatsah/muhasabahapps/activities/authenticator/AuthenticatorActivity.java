package com.tsalatsah.muhasabahapps.activities.authenticator;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.FragmentManager;
import android.os.Bundle;
import android.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import com.tsalatsah.muhasabahapps.R;
import com.tsalatsah.muhasabahapps.authentication.Authenticator;

public class AuthenticatorActivity extends AccountAuthenticatorActivity implements View.OnClickListener {

    private AuthenticatorFragment authenticatorFragment;

    Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticator);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        authenticatorFragment = new AuthenticatorFragment();
        fragmentTransaction.add(R.id.fragment_container, authenticatorFragment);
        fragmentTransaction.commit();
    }


    @Override
    public void onClick(View v) {
//        int id = v.getId();

//        switch (id) {
//            case R.id.btnLogin:
//                AccountManager accountManager = AccountManager.get(getApplicationContext());
//                Account account = new Account("Nurul Huda", Authenticator.ACCOUNT_TYPE);
//
//                accountManager.addAccountExplicitly(account, null, null);
//                accountManager.setAuthToken(account, Authenticator.ACCOUNT_AUTH_TOKEN_TYPE, "initokenku");
//
//        }
    }

    public void doneLogin () {
        Bundle result = new Bundle();
        setAccountAuthenticatorResult(result);
        finish();
    }
}
