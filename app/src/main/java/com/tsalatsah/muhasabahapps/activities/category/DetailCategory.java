package com.tsalatsah.muhasabahapps.activities.category;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.tsalatsah.muhasabahapps.R;
import com.tsalatsah.muhasabahapps.api.CategoryApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class DetailCategory extends AppCompatActivity implements View.OnClickListener {

    public final static String EXTRA_CATEGORY = "category";
    private static final String TAG = DetailCategory.class.getSimpleName();
    private JSONObject category;
    private SubCategoryAdapter subCategoryAdapter;
    private RecordAdapter recordAdapter;
    private TextView loadingText;
    private CategoryApi categoryApi;
    private boolean hasSubCategory;
    private FloatingActionButton addNewSubBtn;
    public static boolean loadCategoryFromServer;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_category);

        categoryApi = new CategoryApi(this);

        loadingText = (TextView) findViewById(R.id.loadingText);
        subCategoryAdapter = new SubCategoryAdapter(this);
        recordAdapter = new RecordAdapter(this);

        addNewSubBtn = (FloatingActionButton) findViewById(R.id.btnAddNewSub);
        addNewSubBtn.setOnClickListener(this);

        loadCategoryFromServer = true;

        mContext = this;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (loadCategoryFromServer) {
            loadCategoryDetailFromServer();
            loadCategoryFromServer = false;
        }
    }

    private void loadCategoryDetailFromServer() {
        try {
            category = new JSONObject(getIntent().getExtras().getString(EXTRA_CATEGORY));
            setTitle(category.getString("name"));
            callApiToLoadTheDataOfThisCategory();;
        } catch (JSONException e) {
            category = null;
            e.printStackTrace();
        }
    }

    private void callApiToLoadTheDataOfThisCategory() throws JSONException{
        /*
        * Perhatian: jadi ada dua jenis kategori.
        * Yang pertama, dia memiliki sub-category (jadi di dalamnya masih ada kategori lagi).
        * Sedang yang kedua, maka dia tidak memiliki kategori lagi. Jadi, untuk yang tidak memiliki
        * kategori lagi, maka langsung ditampilkan record-nya...
        * Tetapi yang memiliki sub-kategori, maka yang ditampilkan adalah sub kategorinya...
        * */

        hasSubCategory = category.getBoolean("has_sub_category");

        categoryApi.getCategoryDetail(category.getInt("id"), new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                ListView listView = (ListView) findViewById(R.id.listView);

                if (hasSubCategory) {
                    try {
                        JSONObject responseCategory = response.getJSONObject("category");
                        final JSONArray subCategories = responseCategory.getJSONArray("sub_categories");
                        subCategoryAdapter.setData(subCategories);
                        subCategoryAdapter.notifyDataSetChanged();
                        listView.setAdapter(subCategoryAdapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(mContext, SubCategoryDetailActivity.class);
                                String subCategory = null;

                                try {
                                    subCategory = subCategories.getJSONObject(position).toString();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                intent.putExtra(SubCategoryDetailActivity.EXTRA_SUB_CATEGORY, subCategory);
                                startActivity(intent);
                            }
                        });

                        // sembunyikan loading textnya jika panjang sub category lebih dari 0
                        if (subCategories.length() > 0)
                            loadingText.setVisibility(View.GONE);
                        else
                            loadingText.setText("Sub kategori kosong.");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    try {
                        JSONObject responseCategory = response.getJSONObject("category");
                        JSONArray records = responseCategory.getJSONArray("records");
                        recordAdapter.setJSONResponse(records);
                        listView.setAdapter(recordAdapter);


                        if (records.length() > 0)
                            loadingText.setVisibility(View.GONE);
                        else
                            loadingText.setText("Sub kategori kosong.");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_detail_category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_delete_category:
                callApiToDeleteThisCategory();
                break;
        }

        return true;
    }

    private void callApiToDeleteThisCategory() {
        try {
            categoryApi.deteleCategory(category.getInt("id"), new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    finish();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.d(TAG, new String(responseBody));
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btnAddNewSub:
                if (hasSubCategory) {
                    try {
                        Intent intent = new Intent(this, NewSubCategory.class);
                        intent.putExtra("categoryId", category.getInt("id"));
                        startActivity(intent); // start the new sub category activity
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(DetailCategory.this, "Bikin record baru tah?", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}