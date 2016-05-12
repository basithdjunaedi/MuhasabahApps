package com.tsalatsah.muhasabahapps.activities.category;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.tsalatsah.muhasabahapps.R;
import com.tsalatsah.muhasabahapps.api.CategoryApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SubCategoryDetailActivity extends AppCompatActivity {

    private JSONObject subCategory;
    private RecordAdapter adapter;
    private static final String TAG = SubCategoryDetailActivity.class.getSimpleName();
    private ListView recordList;
    private TextView loadingText;
    private SwipeRefreshLayout swipeRefreshLayout;

    public static final String EXTRA_SUB_CATEGORY = "sub_category";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category_detail);

        adapter = new RecordAdapter(this);
        recordList = (ListView) findViewById(R.id.recordsContainer);
        loadingText = (TextView) findViewById(R.id.loadingText);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(
                Color.RED,
                Color.GREEN,
                Color.BLUE
        );
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                readingMessageFromIntent();
            }
        });

        readingMessageFromIntent();
    }

    private void readingMessageFromIntent() {
        try {
            subCategory = new JSONObject(getIntent().getExtras().getString(EXTRA_SUB_CATEGORY));
            setTitle(subCategory.getString("name"));
            callApiToLoadTheDataOfThisSubCategory(subCategory.getInt("category_id"), subCategory.getInt("id"));
            loadingText.setVisibility(TextView.VISIBLE);
            loadingText.setText("Loading...");
            recordList.setVisibility(View.INVISIBLE);
        } catch (JSONException e) {
            subCategory = null;
            e.printStackTrace();
        }
    }

    private void callApiToLoadTheDataOfThisSubCategory(int categoryId, int subCategoryId) {
        CategoryApi categoryApi = new CategoryApi(this);

        categoryApi.getSubCategoryDetail(categoryId, subCategoryId, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONArray responseRecords = new JSONObject(new String(responseBody))
                            .getJSONObject("subCategory")
                            .getJSONArray("records");
                    adapter.setJSONResponse(responseRecords);
                    recordList.setAdapter(adapter);
                    loadingText.setVisibility(TextView.GONE);
                    recordList.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}
