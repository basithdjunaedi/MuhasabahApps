package com.tsalatsah.muhasabahapps.activities.category;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.ResponseHandlerInterface;
import com.tsalatsah.muhasabahapps.R;
import com.tsalatsah.muhasabahapps.api.CategoryApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;

public class NewCategoryActivity extends AppCompatActivity implements View.OnClickListener
{
    private final String TAG = NewCategoryActivity.class.getSimpleName();
    private CheckBox subCategoryCheckBox;
    private RadioButton checkerRadioButton, counterRadioButton;
    private Button submitButton, addRecordButton, cancelRecordButton, saveRecordButton;
    private EditText categoryNameEditText, recordNameEditText;
    private LinearLayout linearLayout;
    private LayoutInflater inflater;
    private View contentView;
    private PopupWindow popupWindow;
    private JSONObject dataPost; // data to send to the server for creating new category

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_category);

        subCategoryCheckBox = (CheckBox) findViewById(R.id.checkBoxSubCategory);
        subCategoryCheckBox.setOnClickListener(this);

        submitButton = (Button) findViewById(R.id.btnSubmit);
        submitButton.setOnClickListener(this);

        addRecordButton = (Button) findViewById(R.id.btnAddRecord);
        addRecordButton.setOnClickListener(this);

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

        linearLayout = (LinearLayout) findViewById(R.id.linierLayout);

        dataPost = new JSONObject();

        preparePopupWindow();
    }

    private void preparePopupWindow()
    {
        inflater = LayoutInflater.from(this);
        contentView = inflater.inflate(R.layout.popup_new_category, null);
        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);

        cancelRecordButton = (Button) contentView.findViewById(R.id.btnCancel);
        saveRecordButton = (Button) contentView.findViewById(R.id.btnSave);
        checkerRadioButton = (RadioButton) contentView.findViewById(R.id.checkerRadioButton);
        counterRadioButton = (RadioButton) contentView.findViewById(R.id.counterRadioButton);

        recordNameEditText = (EditText) contentView.findViewById(R.id.recordNameEditText);
        // add listener to the views
        cancelRecordButton.setOnClickListener(this);
        saveRecordButton.setOnClickListener(this);
        checkerRadioButton.setOnClickListener(this);
        counterRadioButton.setOnClickListener(this);
        recordNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkIfTheRecordFormCompleted();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    @Override
    public void onClick(View view)
    {
        int id = view.getId();

        switch (id) {
            case R.id.checkBoxSubCategory:
                toggleTheRecordsForm();
                break;
            case R.id.btnSubmit:
                callApiToCreateNewCategory();
                break;
            case R.id.btnAddRecord:
                displayNewRecordPopup();
                break;
            case R.id.btnCancel:
                dismissPopupWindow();
                break;
            case R.id.btnSave:
                saveNewRecordLocally();
        }

        checkIfTheFormCompleted();
        checkIfTheRecordFormCompleted();
    }

    private void saveNewRecordLocally() {
        // get the text
        String recordName = recordNameEditText.getText().toString();
        String recordType = counterRadioButton.isChecked() ? "counter" : "checker";
        // create new view from layout
        View view = inflater.inflate(R.layout.list_ordinary_record, null);
        TextView recordNameTextView = (TextView) view.findViewById(R.id.recordName);
        TextView recordTypeTextView = (TextView) view.findViewById(R.id.recordType);
        recordNameTextView.setText(recordName);
        recordTypeTextView.setText(recordType);
        // get the records container
        LinearLayout recordContainer = (LinearLayout) findViewById(R.id.recordsContainer);
        recordContainer.addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        // save record data to the json
        JSONArray dataRecords;
        try {
            dataRecords = dataPost.getJSONArray("records");
        } catch (JSONException e) {
            try {
                dataPost.put("records", new JSONArray());
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }

        try {
            dataRecords = dataPost.getJSONArray("records");

            JSONObject newRecord = new JSONObject();
            newRecord.put("name", recordName);
            newRecord.put("type", recordType);

            dataRecords.put(newRecord.toString());

            dataPost.put("records", dataRecords);
        }
        catch (JSONException json) {
            json.printStackTrace();
        }

        dismissPopupWindow();
        // clear the form
        recordNameEditText.setText("");
    }

    private void dismissPopupWindow()
    {
        popupWindow.dismiss();
    }

    private void displayNewRecordPopup()
    {
        popupWindow.showAtLocation(linearLayout, Gravity.CENTER, 0, 0);
    }

    private void callApiToCreateNewCategory()
    {
        try {
            dataPost.put("name", categoryNameEditText.getText().toString() );
        } catch (JSONException e) {
        }
        Log.d(TAG, dataPost.toString());
        CategoryApi categoryApi = new CategoryApi(getApplicationContext());
        final Snackbar snackbar = Snackbar.make(submitButton, "Loading...", Snackbar.LENGTH_INDEFINITE);

//        categoryApi.newCategory(categoryNameEditText.getText().toString(), getTypeValue(), new AsyncHttpResponseHandler(){
//            @Override
//            public void onPreProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {
//                snackbar.show();
//            }
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                Log.d(TAG, new String(responseBody));
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                Log.d(TAG, new String(responseBody));
//            }
//        });
    }

    private void toggleTheRecordsForm()
    {
        if (!subCategoryCheckBox.isChecked()) {
            linearLayout.setVisibility(View.VISIBLE);
        }
        else {
            linearLayout.setVisibility(View.INVISIBLE);
        }
    }

    private void checkIfTheFormCompleted()
    {
        if (!ifCategoryNameEmpty()) {
            submitButton.setEnabled(true);
        }
        else {
            submitButton.setEnabled(false);
        }
    }

    private boolean ifCategoryNameEmpty()
    {
        return categoryNameEditText.getText().toString().length() < 1;
    }

    private boolean ifSubCategoryChecked()
    {
        return subCategoryCheckBox.isChecked();
    }

    private boolean checkIfTheRecordFormCompleted()
    {
        if (recordNameEditText.length() > 0 && (checkerRadioButton.isChecked() || counterRadioButton.isChecked())) {
            saveRecordButton.setEnabled(true);

            return true;
        }

        saveRecordButton.setEnabled(false);

        return false;
    }

}
