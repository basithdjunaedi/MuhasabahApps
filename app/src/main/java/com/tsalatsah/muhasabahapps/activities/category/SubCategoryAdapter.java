package com.tsalatsah.muhasabahapps.activities.category;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tsalatsah.muhasabahapps.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ibnujakaria on 24/04/16.
 */
public class SubCategoryAdapter extends BaseAdapter {
    private JSONArray subCategories;
    private Context context;

    public SubCategoryAdapter(Context context)
    {
        this.context = context;
    }

    public void setData(JSONArray subCategories)
    {
        this.subCategories = subCategories;
    }

    @Override
    public int getCount()
    {
        if (subCategories == null) {
            return 0;
        }

        return subCategories.length();
    }

    @Override
    public Object getItem(int position)
    {
        try {
            return (Object) subCategories.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_sub_category, null);

        try {
            TextView textView = (TextView) view.findViewById(R.id.textView);
            JSONObject subCategory = (JSONObject) getItem(position);
            textView.setText(subCategory.getString("name"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }
}
