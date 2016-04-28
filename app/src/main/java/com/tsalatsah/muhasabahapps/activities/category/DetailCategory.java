package com.tsalatsah.muhasabahapps.activities.category;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.tsalatsah.muhasabahapps.R;
import com.tsalatsah.muhasabahapps.api.CategoryApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class DetailCategory extends AppCompatActivity {

    public final static String EXTRA_CATEGORY = "category";
    private static final String TAG = DetailCategory.class.getSimpleName();
    private JSONObject category;
    private SubCategoryAdapter subCategoryAdapter;
    private TextView loadingText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_category);

        try {
            category = new JSONObject(getIntent().getExtras().getString(EXTRA_CATEGORY));
            setTitle(category.getString("name"));
            callApiToLoadTheDataOfThisCategory();
            Log.d(TAG, "ayo mosok iki gak diceluk..");
        } catch (JSONException e) {
            category = null;
            e.printStackTrace();
        }

        loadingText = (TextView) findViewById(R.id.loadingText);
        subCategoryAdapter = new SubCategoryAdapter(getApplicationContext());
    }

    private void callApiToLoadTheDataOfThisCategory() throws JSONException{
        /*
        * Perhatian: jadi ada dua jenis kategori.
        * Yang pertama, dia memiliki sub-category (jadi di dalamnya masih ada kategori lagi).
        * Sedang yang kedua, maka dia tidak memiliki kategori lagi. Jadi, untuk yang tidak memiliki
        * kategori lagi, maka langsung ditampilkan record-nya...
        * Tetapi yang memiliki sub-kategori, maka yang ditampilkan adalah sub kategorinya...
        * */

        final boolean hasSubCategory = category.getBoolean("has_sub_category");

        Log.d(TAG, "hasSubCategory -> " + hasSubCategory);

        CategoryApi categoryApi = new CategoryApi(getApplicationContext());
        categoryApi.getCategoryDetail(category.getInt("id"), new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                if (hasSubCategory) {
                    ListView listView = (ListView) findViewById(R.id.listView);

                    try {
                        JSONObject responseCategory = response.getJSONObject("category");
                        JSONArray subCategories = responseCategory.getJSONArray("sub_categories");
                        subCategoryAdapter.setData(subCategories);
                        subCategoryAdapter.notifyDataSetChanged();
                        listView.setAdapter(subCategoryAdapter);

                        // sembunyikan loading textnya jika panjang sub category lebih dari 0
                        if (subCategories.length() > 0)
                            loadingText.setVisibility(View.GONE);
                        else
                            loadingText.setText("Sub kategori kosong.");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.d(TAG, "response -> " + response.toString());
            }
        });
    }
}
