package com.tsalatsah.muhasabahapps.activities.category;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.ResponseHandlerInterface;
import com.tsalatsah.muhasabahapps.R;
import com.tsalatsah.muhasabahapps.api.CategoryApi;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;

public class NewCategoryActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener, TextView.OnEditorActionListener
{
    private final String TAG = NewCategoryActivity.class.getSimpleName();
    private CheckBox subCategoryCheckBox;
    private Button submitButton;
    private EditText categoryNameEditText;
    private RadioGroup radioGroup;
    private RadioButton checkerTypeRadio, counterTypeRadio;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_category);

        subCategoryCheckBox = (CheckBox) findViewById(R.id.checkBoxSubCategory);
        subCategoryCheckBox.setOnClickListener(this);

        submitButton = (Button) findViewById(R.id.btnSubmit);
        submitButton.setOnClickListener(this);

        categoryNameEditText = (EditText) findViewById(R.id.editTextCategoryName);
        categoryNameEditText.setOnClickListener(this);
        categoryNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkIfTheFormCompleted();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        checkerTypeRadio = (RadioButton) findViewById(R.id.radioCheckerType);
        counterTypeRadio = (RadioButton) findViewById(R.id.radioCounterType);

        checkerTypeRadio.setOnClickListener(this);
        counterTypeRadio.setOnClickListener(this);
    }


    @Override
    public void onClick(View view)
    {
        int id = view.getId();

        switch (id) {
            case R.id.checkBoxSubCategory:
                toggleTheRecordTypeForm();
                break;
            case R.id.btnSubmit:
                callApiToCreateNewCategory();
                break;
        }

        checkIfTheFormCompleted();
    }

    private void callApiToCreateNewCategory()
    {
        Log.d(TAG, "opo iki");
        CategoryApi categoryApi = new CategoryApi(getApplicationContext());
        final Snackbar snackbar = Snackbar.make(submitButton, "Loading...", Snackbar.LENGTH_INDEFINITE);

        categoryApi.newCategory(categoryNameEditText.getText().toString(), getTypeValue(), new AsyncHttpResponseHandler(){
            @Override
            public void onPreProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {
                snackbar.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d(TAG, new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d(TAG, new String(responseBody));
            }
        });
    }

    private void toggleTheRecordTypeForm()
    {
        if (subCategoryCheckBox.isChecked()) {
            radioGroup.setVisibility(View.VISIBLE);
        }
        else {
            radioGroup.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event)
    {
        int action = event.getAction();

        Log.d(TAG, "There is a key action");
        switch (action) {
            case KeyEvent.ACTION_UP:
                checkIfTheFormCompleted();
                Log.d(TAG, "action up");
                break;
        }

        return false;
    }

    private void checkIfTheFormCompleted()
    {
        if (!ifCategoryNameEmpty() && ((ifSubCategoryChecked() && ifRadioGroupSelected()) || !ifSubCategoryChecked())) {
            submitButton.setEnabled(true);
        }
        else {
            submitButton.setEnabled(false);
        }
    }

    private boolean ifRadioGroupSelected() {
        return checkerTypeRadio.isChecked() || counterTypeRadio.isChecked();
    }

    private boolean ifCategoryNameEmpty()
    {
        return categoryNameEditText.getText().toString().length() < 1;
    }

    private boolean ifSubCategoryChecked()
    {
        return subCategoryCheckBox.isChecked();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        checkIfTheFormCompleted();
        Log.d(TAG, "On editor action fired!");
        return false;
    }

    private String getTypeValue()
    {
        if (ifRadioGroupSelected()) {
            return checkerTypeRadio.isChecked() ? "checker" : "counter";
        }

        return null;
    }
}
