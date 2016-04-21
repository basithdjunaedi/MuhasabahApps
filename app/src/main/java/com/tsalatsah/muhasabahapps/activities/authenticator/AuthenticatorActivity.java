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

public class AuthenticatorActivity extends AccountAuthenticatorActivity{

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



    public void doneLogin () {
        Bundle result = new Bundle();
        setAccountAuthenticatorResult(result);
        finish();
    }
}
