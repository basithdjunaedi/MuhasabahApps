package com.tsalatsah.muhasabahapps.activities.category;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.tsalatsah.muhasabahapps.R;
import com.tsalatsah.muhasabahapps.api.CategoryApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class NewSubCategory extends AppCompatActivity implements View.OnClickListener {

    private RadioButton checkerRadioButton, counterRadioButton;
    private Button btnAddRecords, submitBtn, cancelRecordButton, saveRecordButton;
    private EditText subCategoryNameEditText, recordNameEditText;
    private LinearLayout linearLayout;
    private LayoutInflater inflater;
    private View contentView;
    private PopupWindow popupWindow;
    private JSONObject dataPost; // data to send to the server for creating new sub category
    private CategoryApi categoryApi;
    private boolean recordHaveBeenAdded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_sub_category);

        subCategoryNameEditText = (EditText) findViewById(R.id.subCategoryNameEditText);
        btnAddRecords = (Button) findViewById(R.id.addRecordBtn);
        btnAddRecords.setOnClickListener(this);
        submitBtn = (Button) findViewById(R.id.btnSubmit);
        submitBtn.setOnClickListener(this);

        dataPost = new JSONObject();
        categoryApi = new CategoryApi(this);

        inflater = LayoutInflater.from(this);
        linearLayout = (LinearLayout) findViewById(R.id.recordsContainer);
        preparePopupWindow();

        subCategoryNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkIfTheFormComplete();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void preparePopupWindow() {
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
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.addRecordBtn:
                showPopupMenu();
                break;
            case R.id.btnCancel:
                dismissPopupWindow();
                break;
            case R.id.btnSave:
                saveNewRecordLocally();
                break;
            case R.id.btnSubmit:
                callTheApiToCreateNewSubCategory();
        }

        checkIfTheFormComplete();
        checkIfTheRecordFormCompleted();
    }

    private void showPopupMenu() {
        popupWindow.showAtLocation(linearLayout, Gravity.CENTER, 0, 0);
    }

    private void dismissPopupWindow() {
        popupWindow.dismiss();
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
        } catch (JSONException json) {
            json.printStackTrace();
        }

        dismissPopupWindow();
        // clear the form
        recordNameEditText.setText("");
        recordHaveBeenAdded = true;
    }

    private void callTheApiToCreateNewSubCategory() {
        try {
            dataPost.put("name", subCategoryNameEditText.getText().toString());
            dataPost.put("categoryId", getIntent().getExtras().getInt("categoryId"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        categoryApi.newSubCategory(dataPost, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                DetailCategory.loadCategoryFromServer = true;
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private boolean checkIfTheFormComplete() {
        if (subCategoryNameEditText.getText().length() > 0 && recordHaveBeenAdded) {
            submitBtn.setEnabled(true);
            return true;
        }
        submitBtn.setEnabled(false);
        return false;
    }

    private boolean checkIfTheRecordFormCompleted() {
        if (recordNameEditText.length() > 0 && (checkerRadioButton.isChecked() || counterRadioButton.isChecked())) {
            saveRecordButton.setEnabled(true);

            return true;
        }

        saveRecordButton.setEnabled(false);

        return false;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
