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

import java.util.ArrayList;
import java.util.HashMap;

public class StateAdapter extends BaseAdapter implements Filterable {

    // public static List<State> stateArrayList;
    // public static List<State> stateArrayList_filter;
    public static ArrayList<HashMap<String, String>> stateArrayList, stateArrayList_filter;

    private Context context;

    public StateAdapter(Context context, ArrayList<HashMap<String, String>> stateArrayList) {
        this.context = context;
        this.stateArrayList = stateArrayList;

    }

    @Override
    public int getCount() {
        if (stateArrayList != null) {

            Log.e("size state", "" + stateArrayList.size());
            return stateArrayList.size();
        } else {
            return 0;
        }
    }

    @Override
    public String getItem(int position) {
        if (stateArrayList != null) {
            return stateArrayList.get(position).get("stateID");
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

        txtcountry_name = (TextView) v.findViewById(R.id.txtcountry_name);
        txtcountry_name.setText(stateArrayList.get(position).get("stateName"));
        Log.e("ada state",""+stateArrayList.get(position).get("stateName"));

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
                if (stateArrayList_filter == null)
                    stateArrayList_filter = stateArrayList;
                if (constraint != null) {
                    if (stateArrayList_filter != null && stateArrayList_filter.size() > 0) {
                        for (int i = 0; i < stateArrayList_filter.size(); i++) {
                            if (stateArrayList_filter.get(i).get("stateName").toLowerCase().startsWith(constraint.toString(), 0))
//.contains(constraint.toString()))
                            {
                                results1.add(stateArrayList_filter.get(i));
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

                stateArrayList = (ArrayList<HashMap<String, String>>) results.values;

                Log.e("stateArrayList filter", "" + stateArrayList);
                if (stateArrayList.size() == 0) {
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
