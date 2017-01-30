package com.westwood24.connect.adapter;

/**
 * Created by aipxperts on 3/2/16.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.westwood24.R;
import com.westwood24.connect.activity.DashBoardActivity;
import com.westwood24.connect.utils.CircularImageView;
import com.westwood24.connect.utils.Constants;
import com.westwood24.connect.utils.Pref;

import java.util.ArrayList;

public class SettingWithoutLoginNavDrawerListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> navDrawerItems;
    String user;
    Bitmap image_url;
    public static CircularImageView mUserImage;
    DashBoardActivity activity;
    Bitmap bmp;
    ImageView img_left;
    String activity_name;
    LinearLayout main_layout;


    public SettingWithoutLoginNavDrawerListAdapter(Context context, ArrayList<String> navDrawerItems, DashBoardActivity activity, String activity_name) {
        this.context = context;
        this.navDrawerItems = navDrawerItems;
        this.activity = activity;
        this.activity_name = activity_name;

        Log.v("activity_name", activity_name + "");
    }

    @Override
    public int getCount() {
        return navDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return navDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.row_withoutlogin_drawer_list, null);
        }


        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        RelativeLayout mHeaderBackRl = (RelativeLayout) convertView.findViewById(R.id.header_layout_back_rl);
        RelativeLayout mHeaderMainRl = (RelativeLayout) convertView.findViewById(R.id.header_main_rl);
        TextView mHeaderTitleTv = (TextView) convertView.findViewById(R.id.txt_title);
        img_left = (ImageView)convertView.findViewById(R.id.img_left);
        View mListDivederView = (View) convertView.findViewById(R.id.list_divider);
        main_layout=(LinearLayout)convertView.findViewById(R.id.main_layout);
        mHeaderMainRl.setVisibility(View.GONE);
        mHeaderBackRl.setVisibility(View.GONE);

        if (position == 0) {
            main_layout.setVisibility(View.VISIBLE);
            mHeaderMainRl.setVisibility(View.VISIBLE);
            mHeaderTitleTv.setTypeface(null, Typeface.BOLD);
            mHeaderTitleTv.setText(R.string.Settings);
            img_left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //DashBoardActivity.mDrawerLayout.closeDrawer(Gravity.RIGHT);
                    context.startActivity(new Intent(context, DashBoardActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

                }
            });
            txtTitle.setVisibility(View.GONE);
            mListDivederView.setVisibility(View.GONE);
        }
        else {
            main_layout.setVisibility(View.VISIBLE);
            mHeaderMainRl.setVisibility(View.GONE);
            txtTitle.setVisibility(View.VISIBLE);
            mListDivederView.setVisibility(View.VISIBLE);
            txtTitle.setText(navDrawerItems.get(position));
        }



       /* if (position == navDrawerItems.size() - 1) {

            if (Pref.getValue(context, Constants.IS_LOGIN, "").equals("0")) {

                txtTitle.setText("LOGIN");

            } else {
                txtTitle.setText("LOGOUT");
            }
            txtTitle.setTextColor(Color.parseColor("#f26c6c"));


        } else {
            //txtTitle.setText(navDrawerItems.get(position));

        }*/

        // txtTitle.setText(navDrawerItems.get(position));
        /*image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activity.callfragment();

            }
        });

        usertxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activity.callfragment();

            }
        });

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activity.callfragment();

            }
        });*/


        return convertView;
    }

}