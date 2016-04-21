package com.tsalatsah.muhasabahapps.activities.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
 * Created by ibnujakaria on 21/04/16.
 */
public class CustomListAdapter extends BaseAdapter implements View.OnClickListener{

    private JSONArray categories;
    private LayoutInflater inflater;
    private Context context;

    public CustomListAdapter(MainActivity activity)
    {
        this.context = activity;
        inflater = LayoutInflater.from(context);
    }

    public void setJSONResponse(JSONObject response)
    {
        try {
            categories = response.getJSONArray("categories");
            Log.d("tes", "panjang kategori -> " + getCount());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCount()
    {
        return categories.length();
    }

    @Override
    public Object getItem(int position)
    {
        try {
            return categories.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public long getItemId(int position)
    {
        JSONObject item = (JSONObject) getItem(position);

        if (item != null){
            try {
                return item.getInt("id");
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = inflater.inflate(R.layout.card_view, null);

        JSONObject category = (JSONObject) getItem(position);
        try {
            TextView textView = (TextView) view.findViewById(R.id.card_view_text);
            textView.setText(category.getString("name"));
        }
        catch (JSONException e){
            // who cares?
        }

        Log.d("tes", "memanggil view ke-" + position);
        return view;
    }

    @Override
    public void onClick(View v)
    {

    }
}
