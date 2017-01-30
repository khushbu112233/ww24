package com.westwood24.connect.adapter;

/**
 * Created by aipxperts on 3/2/16.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.westwood24.R;
import com.westwood24.connect.activity.DashBoardActivity;
import com.westwood24.connect.utils.CircularImageView;
import com.westwood24.connect.utils.Constants;
import com.westwood24.connect.utils.Pref;
import com.westwood24.connect.utils.Utils;

import java.util.ArrayList;

public class SettingWithLoginNavDrawerListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> navDrawerItems;
    String user;
    // Bitmap image_url;
    ImageView img_left;
    public static CircularImageView mUserImage;
    DashBoardActivity activity;
    Bitmap bmp;
    String activity_name;

    public SettingWithLoginNavDrawerListAdapter(Context context, ArrayList<String> navDrawerItems, String user, DashBoardActivity activity, String activity_name) {
        this.context = context;
        this.navDrawerItems = navDrawerItems;
        this.user = user;
        // this.image_url = image_url;
        this.activity = activity;
        this.activity_name = activity_name;

    }

    public SettingWithLoginNavDrawerListAdapter(Context context, ArrayList<String> navDrawerItems, DashBoardActivity activity, String activity_name) {
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
            convertView = mInflater.inflate(R.layout.drawer_list, null);
        }


        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.layout);
        mUserImage = (CircularImageView) convertView.findViewById(R.id.drawer_list_user_image);
        TextView edit_profile = (TextView) convertView.findViewById(R.id.edit_profile);
        img_left = (ImageView)convertView.findViewById(R.id.img_left);
        TextView usertxt = (TextView) convertView.findViewById(R.id.usertxt);
        RelativeLayout mHeaderBackRl = (RelativeLayout) convertView.findViewById(R.id.header_layout_back_rl);
        RelativeLayout mHeaderMainRl = (RelativeLayout) convertView.findViewById(R.id.header_main_rl);
        TextView mHeaderTitleTv = (TextView) convertView.findViewById(R.id.txt_title);
        View v1 = (View) convertView.findViewById(R.id.v1);
        View mListDivederView = (View) convertView.findViewById(R.id.list_divider);
        mHeaderMainRl.setVisibility(View.GONE);
        mHeaderBackRl.setVisibility(View.GONE);
        if (position == 0) {
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
            layout.setVisibility(View.GONE);

        }/* else {
            layout.setVisibility(View.GONE);
        }*/ else if (position == 1) {


            layout.setVisibility(View.VISIBLE);

            if (!Pref.getValue(context, Constants.TAG_PROFILE_PICTURE, "").isEmpty()) {
                int width = Utils.getPixel(90, context);
                Log.e("image url",""+"https://ww24connect.com"+Pref.getValue(context, Constants.TAG_PROFILE_PICTURE, ""));

                Picasso.with(context).load("https://ww24connect.com"+Pref.getValue(context, Constants.TAG_PROFILE_PICTURE, "")).resize(width, width).error(R.mipmap.ic_launcher).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).into(mUserImage);
            }
            usertxt.setText(user);


            txtTitle.setVisibility(View.GONE);
            mListDivederView.setVisibility(View.GONE);
        } else {
            layout.setVisibility(View.GONE);
            txtTitle.setText(navDrawerItems.get(position));
            mHeaderMainRl.setVisibility(View.GONE);
            mListDivederView.setVisibility(View.VISIBLE);
        }


        if (position == navDrawerItems.size() - 1) {

            txtTitle.setTypeface(null, Typeface.BOLD);
            txtTitle.setTextColor(context.getResources().getColor(R.color.explore_btn_color));
            mListDivederView.setVisibility(View.GONE);


        } else {
            //txtTitle.setText(navDrawerItems.get(position));

        }

        //txtTitle.setText(navDrawerItems.get(position));
        mUserImage.setOnClickListener(new View.OnClickListener() {
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

                Pref.setValue(context,"open_drawer","true");
                activity.callfragment();

            }
        });


        return convertView;
    }



}