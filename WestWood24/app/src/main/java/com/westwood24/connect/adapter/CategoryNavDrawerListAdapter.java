package com.westwood24.connect.adapter;

/**
 * Created by aipxperts on 3/2/16.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.westwood24.R;
import com.westwood24.connect.activity.DashBoardActivity;
import com.westwood24.connect.fragment.CategoryTypeFragment;
import com.westwood24.connect.fragment.HomeFragment;
import com.westwood24.connect.model.CategoryList;
import com.westwood24.connect.utils.CircularImageView;
import com.westwood24.connect.utils.Pref;

import java.util.ArrayList;

public class CategoryNavDrawerListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<CategoryList> catnavDrawerItems, list_filter;

    String user;
    Bitmap image_url;
    public static CircularImageView mUserImage;
    DashBoardActivity activity;
    Bitmap bmp;
    String activity_name;

    public CategoryNavDrawerListAdapter(Context context, ArrayList<CategoryList> catnavDrawerItems, DashBoardActivity activity) {
        this.context = context;
        this.catnavDrawerItems = catnavDrawerItems;
        this.user = user;
        this.image_url = image_url;
        this.activity = activity;
        this.activity_name = activity_name;

    }

    public CategoryNavDrawerListAdapter(Context context, ArrayList<CategoryList> catnavDrawerItems, DashBoardActivity activity, String activity_name) {
        this.context = context;
        this.catnavDrawerItems = catnavDrawerItems;
        this.activity = activity;
        this.activity_name = activity_name;

        Log.v("activity_name", activity_name + "");
    }

    @Override
    public int getCount() {

        return catnavDrawerItems.size();

    }

    @Override
    public Object getItem(int position) {
        return catnavDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.row_category_drawer_list, null);
        }


        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        View v11 = (View)convertView.findViewById(R.id.v11);
        TextView title1 = (TextView) convertView.findViewById(R.id.title1);
        TextView usertxt = (TextView) convertView.findViewById(R.id.usertxt);
        RelativeLayout mHeaderBackRl = (RelativeLayout) convertView.findViewById(R.id.header_layout_back_rl);
        RelativeLayout mHeaderMainRl = (RelativeLayout) convertView.findViewById(R.id.header_main_rl);
        TextView mHeaderTitleTv = (TextView) convertView.findViewById(R.id.txt_title);

        View v1 = (View) convertView.findViewById(R.id.v1);
        mHeaderMainRl.setVisibility(View.GONE);

        if (position == 0) {

            mHeaderMainRl.setVisibility(View.VISIBLE);
            mHeaderTitleTv.setTypeface(null, Typeface.BOLD);
            mHeaderTitleTv.setText(R.string.Categories);
            mHeaderBackRl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*HomeFragment fragment = new HomeFragment();
                    ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
*/
                    DashBoardActivity.mDrawerLayout.closeDrawer(Gravity.LEFT);

                }
            });

        } /*else if (position == 1) {

            title1.setVisibility(View.GONE);
            mHeaderMainRl.setVisibility(View.GONE);
            v11.setVisibility(View.GONE);
        }*/ else {

            mHeaderMainRl.setVisibility(View.GONE);
            title1.setVisibility(View.GONE);
            v11.setVisibility(View.GONE);
        }

        Log.e("catnavDrawerItems.size", "" + catnavDrawerItems.size());
        Log.e("position", "" + position);
        title1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*HomeFragment fragment = new HomeFragment();
                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
                DashBoardActivity.mDrawerLayout.closeDrawer(Gravity.LEFT);*/
                // activity.callHomefragment();
                Log.e("category", "3333");
                DashBoardActivity.mDrawerLayout.closeDrawer(Gravity.LEFT);
                context.startActivity(new Intent(context, DashBoardActivity.class));
                ((FragmentActivity) context).overridePendingTransition(0, 0);

            }
        });
        txtTitle.setAllCaps(true);
        if(position==0) {
            txtTitle.setText("HOME");
        }else
        {
            txtTitle.setText((catnavDrawerItems.get(position).getCategoryname()));

        }

        Log.e("category", "txtTitle " + txtTitle.getText().toString());
        txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (position==0) {
                   /* DashBoardActivity.mDrawerLayout.closeDrawer(Gravity.LEFT);
                    HomeFragment homefragment = new HomeFragment();
                    ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, homefragment).addToBackStack(null).commit();
*/
                    DashBoardActivity.mDrawerLayout.closeDrawer(Gravity.LEFT);
                    context.startActivity(new Intent(context, DashBoardActivity.class));
                    ((FragmentActivity) context).overridePendingTransition(0, 0);
                } else {
                    DashBoardActivity.mDrawerLayout.closeDrawer(Gravity.LEFT);

                    Pref.setValue(context, "id", catnavDrawerItems.get(position).getCid());
                    CategoryTypeFragment fragment = new CategoryTypeFragment();
                    ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();

                }
                Log.e("on click position", "" + catnavDrawerItems.get(position).getCid());
            }
        });

        return convertView;
    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                //list_filter = new ArrayList<HashMap<String, String>>();
                ArrayList<CategoryList> results1 = new ArrayList<CategoryList>();
                //  final ArrayList<LeaderRank> results = new ArrayList<LeaderRank>();
                if (list_filter == null)
                    list_filter = catnavDrawerItems;
                if (constraint != null) {
                    if (list_filter != null && list_filter.size() > 0) {
                        for (int i = 0; i < list_filter.size(); i++) {
                            if (list_filter.get(i).getCategoryname().toLowerCase().startsWith(constraint.toString(), 0))

                            //.contains(constraint.toString()))
                            {
                                results1.add(list_filter.get(i));
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

                catnavDrawerItems = (ArrayList<CategoryList>) results.values;

                Log.e("list...filter", "" + catnavDrawerItems);
                if (catnavDrawerItems.size() == 0) {
                    // Toast.makeText(context, R.string.no_value_found, Toast.LENGTH_SHORT).show();
                }
                notifyDataSetChanged();
            }
        };
    }
}