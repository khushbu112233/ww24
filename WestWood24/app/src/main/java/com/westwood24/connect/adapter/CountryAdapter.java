package com.westwood24.connect.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.westwood24.R;
import com.westwood24.connect.activity.SplashActivity;
import com.westwood24.connect.utils.Country;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CountryAdapter extends BaseAdapter implements Filterable {

    // public static List<Country> countryArrayList;
    // public static List<Country> countryArrayList_filter;
    public static ArrayList<HashMap<String, String>> countryArrayList, countryArrayList_filter;

    private Context context;

    public CountryAdapter(Context context, ArrayList<HashMap<String, String>> countryArrayList) {
        this.context = context;
        this.countryArrayList = countryArrayList;


    }

    @Override
    public int getCount() {
        if (countryArrayList != null) {
            return countryArrayList.size();
        } else {
            return 0;
        }
    }

    @Override
    public String getItem(int position) {
        if (countryArrayList != null) {
    return countryArrayList.get(position).get("countryID");
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        TextView txtcountry_name;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.row_country_list, null);

        Log.e("countrylist",""+ countryArrayList.get(position).get("countryID")+countryArrayList.get(position).get("countryName"));

        txtcountry_name = (TextView) v.findViewById(R.id.txtcountry_name);
        txtcountry_name.setText(countryArrayList.get(position).get("countryName"));

        return v;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
//list_filter = new ArrayList<HashMap<String, String>>();
                ArrayList<HashMap<String, String>> results1 = new ArrayList<HashMap<String, String>>();
// final ArrayList<LeaderRank> results = new ArrayList<LeaderRank>();
                if (countryArrayList_filter == null)
                    countryArrayList_filter = countryArrayList;
                if (constraint != null) {
                    if (countryArrayList_filter != null && countryArrayList_filter.size() > 0) {
                        for (int i = 0; i < countryArrayList_filter.size(); i++) {
                            if (countryArrayList_filter.get(i).get("countryName").toLowerCase().startsWith(constraint.toString(), 0))
//.contains(constraint.toString()))
                            {
                                results1.add(countryArrayList_filter.get(i));
                            }
                        }
                    }


                    oReturn.values = results1;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {

                countryArrayList = (ArrayList<HashMap<String, String>>) results.values;

                Log.e("countryArrayList filter", "" + countryArrayList);
                if (countryArrayList.size() == 0) {
                    Toast.makeText(context, R.string.No_result_found, Toast.LENGTH_SHORT).show();
                }
                notifyDataSetChanged();
            }
        };
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }


}
